import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.FileToCompilation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RefusedBequestDetection extends VoidVisitorAdapter<Void> {
    /**
     * If a subclass uses only some of the methods and properties inherited from its parents, the hierarchy is off-kilter.
     * The unneeded methods may simply go unused or be redefined and give off exceptions.
     */
    private static final Logger log = Logger.getLogger(RefusedBequestDetection.class.getName());
    private static String className;

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void count) {
        ClassOrInterfaceType extension;
        ClassOrInterfaceDeclaration superClass;
        className = cd.getNameAsString();

        if (!cd.isInterface() && !cd.getExtendedTypes().isEmpty()) {
            //make sure class inherits something first
            extension = cd.getExtendedTypes().get(0);
            log.info("Extension Class: " + extension.getNameAsString());
            Optional<CompilationUnit> superUnit = FileToCompilation.getCompilationUnitByName(extension.getNameAsString());
            if (superUnit.isPresent()) {
                log.info("Getting methods of the super class.... ");
                superClass = superUnit.get().getClassByName(extension.getNameAsString()).get();
                compareMethodsOfEachClass(superClass.getMethods(), cd.getMethods());
            }
        }

        super.visit(cd, count);
    }

    private void compareMethodsOfEachClass(List<MethodDeclaration> superMethods, List<MethodDeclaration> classMethods) {
        List<String> superMethodNames = new ArrayList<>();
        superMethods.forEach(sm -> superMethodNames.add(sm.getNameAsString()));

        List<String> classMethodNames = new ArrayList<>();
        classMethods.forEach(cm -> classMethodNames.add(cm.getNameAsString()));

        for (String methodName : superMethodNames) {
            if (classMethodNames.contains(methodName)) {
                //get methods inherited that local class is using
                List<MethodDeclaration> inheritedMethods = classMethods.stream()
                        .filter(cm -> cm.getNameAsString().equals(methodName)).collect(Collectors.toList());

                analyseMethod(inheritedMethods);
            }
        }
    }

    private void analyseMethod(List<MethodDeclaration> inheritedMethods) {
        for (MethodDeclaration methodDeclaration : inheritedMethods) {
            if(methodDeclaration.getBody().isPresent()){
                if (methodDeclaration.getBody().get().isEmpty() || !methodDeclaration.isAnnotationPresent("Override")) {
                    log.warning("Class " + className + " is inheriting methods it is not implementing or not overriding");
                }
            }

        }
    }
}

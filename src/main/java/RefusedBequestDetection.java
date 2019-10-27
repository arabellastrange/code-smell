import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.ClassCollector;
import helpers.FileToCompilation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class RefusedBequestDetection extends VoidVisitorAdapter<Void> {
    /**
     * If a subclass uses only some of the methods and properties inherited from its parents, the hierarchy is off-kilter.
     * The unneeded methods may simply go unused or be redefined and give off exceptions.
     */
    private static final Logger log = Logger.getLogger(RefusedBequestDetection.class.getName());

    @Override
    public void visit(CompilationUnit cu, Void count) {
        ClassOrInterfaceType extension;
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = new ArrayList<>();

        VoidVisitor<List<ClassOrInterfaceDeclaration>> classCollector = new ClassCollector();
        classCollector.visit(cu, classOrInterfaceDeclarations);
        ClassOrInterfaceDeclaration superClass;

        for (ClassOrInterfaceDeclaration cd : classOrInterfaceDeclarations) {
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
        }

        super.visit(cu, count);
    }

    private void compareMethodsOfEachClass(List<MethodDeclaration> superMethods, List<MethodDeclaration> classMethods) {
        log.info("Super methods: " + superMethods.toString());
        log.info("Class Methods: " + classMethods.toString());
    }
}

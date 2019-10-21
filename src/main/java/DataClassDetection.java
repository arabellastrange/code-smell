import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataClassDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());
    private List<MethodDeclaration> methods = new ArrayList<>();
    private List<FieldDeclaration> fields = new ArrayList<>();

    /**
     * A data class refers to a class that contains only fields and crude methods for accessing them (getters and setters).
     * These are simply containers for data used by other classes.
     * These classes don’t contain any additional functionality and can’t independently operate on the data that they own.
     */

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
        super.visit(cd, arg);

        log.info("Calling DataClassDetection on " + cd.getName());
        //returning the getters of the property model?????
        cd.getMetaModel().getAllPropertyMetaModels().forEach(pm -> {
            log.info("For class " + cd.getName() + " getter method names: " + pm.getGetterMethodName());
        });

        fields = getFields(cd);
        methods = getMethods(cd);
        discardGetterAndSetterMethods(methods);

    }

    private List<FieldDeclaration> getFields(ClassOrInterfaceDeclaration cd) {
        List<FieldDeclaration> fields = cd.getFields();
        log.info("Class " + cd.getName() + "has " + fields.size() + " fields in class ");
        return fields;
    }

    private List<MethodDeclaration> getMethods(ClassOrInterfaceDeclaration cd) {
        List<MethodDeclaration> methods = cd.getMethods();
        log.info("Class" + cd.getName() + " has " + methods.size() + " methods in class ");

        methods.forEach(m -> log.info("The methods of " + cd.getName() + " are " + m.getName()));

        return methods;
    }

    private void discardGetterAndSetterMethods(List<MethodDeclaration> allMethods) {
        //TODO
    }
}

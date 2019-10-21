import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.logging.Logger;

public class ClassLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());
    private int classLength = 0;

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
        super.visit(cd, arg);

        log.info("Calling ClassLengthCounter on " + cd.getName());

        // add method length, field size and constructor length to class length
        classLength += getMethodsLength(cd);
        classLength += getFieldsLength(cd);
        classLength += getConstructorsLength(cd);

        log.info("Class " + cd.getName() + " has " + classLength + " lines.");

        if (classLength > 50) {
            log.warning("Class " + cd.getName() + " has " + classLength + " lines.");
            log.warning("Class  " + cd.getName() + " is too long, maximum recommended number of statements is 50.");
        }
    }

    private int getConstructorsLength(ClassOrInterfaceDeclaration cd) {
        int totalConstructorLength = 0;
        List<ConstructorDeclaration> constructors = cd.getConstructors();
        for (ConstructorDeclaration c : constructors) {
            totalConstructorLength += c.getBody().getStatements().size();
        }
        log.info("Class " + cd.getName() + " has " + totalConstructorLength + " constructors.");
        return totalConstructorLength;
    }

    private int getFieldsLength(ClassOrInterfaceDeclaration cd) {
        List<FieldDeclaration> fields = cd.getFields();
        log.info("Class " + cd.getName() + " has " + fields.size() + " fields in class ");
        return fields.size();
    }

    private int getMethodsLength(ClassOrInterfaceDeclaration cd) {
        int totalMethodLength = 0;
        List<MethodDeclaration> methods = cd.getMethods();
        log.info("Class " + cd.getName() + "has " + methods.size() + " methods in class ");

        methods.forEach(m -> log.info("The methods of "+ cd.getName()+" are " + m.getName()));

        for (MethodDeclaration md : methods) {
            if (md.getBody().isPresent()) {
                totalMethodLength += md.getBody().get().getStatements().size();
            }
        }
        return totalMethodLength;
    }
}

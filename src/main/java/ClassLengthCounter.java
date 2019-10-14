import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClassLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());
    private int classLength = 0;
    private int totalMethodLength = 0;
    private int totalConstructorLength = 0;

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
        super.visit(cd, arg);

        log.info("Calling ClassLengthCounter on " + cd.getName());

        List<MethodDeclaration> methods = cd.getMethods();
        log.info("This class has " + methods.size() + " methods in class ");

        methods.forEach(m -> log.info("The methods of this class are " + m.getName()));

        //gather the length of all the methods in the class
        List<Integer> methodLengths = new ArrayList<>();
        for (MethodDeclaration md : methods) {
            VoidVisitor<List<Integer>> methodLengthCounter = new MethodLengthCounter();
            methodLengthCounter.visit(md, methodLengths);
        }

        //sum all method lengths
        log.info("Method lengths: " + methodLengths.toString());
        for (int methodLength : methodLengths) {
            totalMethodLength += methodLength;
        }

        List<FieldDeclaration> fields = cd.getFields();
        log.info("This class has " + fields.size() + " fields in class ");

        List<ConstructorDeclaration> constructors = cd.getConstructors();
        constructors.forEach(c -> {
            totalConstructorLength += c.getBody().getStatements().size();
        });

        // add method length, field size and constructor length to class length
        classLength += totalMethodLength;
        classLength += fields.size();
        classLength += totalConstructorLength;

        log.info("Class " + cd.getName() + " has " + classLength + " lines.");

        if (classLength > 50) {
            log.warning("Class " + cd.getName() + " has " + classLength + " lines.");
            log.warning("Class  " + cd.getName() + " is too long, maximum recommended number of statements is 50.");
        }
    }
}

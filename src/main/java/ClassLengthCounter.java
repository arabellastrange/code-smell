import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.logging.Logger;

public class ClassLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void arg){
        super.visit(cd, arg);

        List<MethodDeclaration> methods = cd.getMethods();
        Integer methodSize = 0;
        Integer classSize = 0;

        for(MethodDeclaration md : methods){
            VoidVisitor<Integer> methodLengthCounter = new MethodLengthCounter();
            methodLengthCounter.visit(md, methodSize);
            classSize+= methodSize;
        }

        if(classSize > 10){
            log.warning("Class " + cd.getName() + " has " + classSize + " lines.");
            log.warning("Class  " + cd.getName() + " is too long, maximum recommended number of statements is 10.");
        }
    }
}

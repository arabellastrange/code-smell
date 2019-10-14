import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.logging.Logger;

public class MethodParameterCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);
        int methodParameterCount = md.getParameters().size();

        //for debugging
        //log.info("Method Argument Count: " + methodParameterCount);

        if(methodParameterCount > 4){
            log.warning("Method " + md.getName() + " uses too many parameter, maximum recommended number of parameters is 4.");
        }
    }
}

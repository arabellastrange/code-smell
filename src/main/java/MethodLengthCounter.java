import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.logging.Logger;

public class MethodLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);

        md.getBody().ifPresent(b -> {
            if (b.getStatements().size() > 15) {
                log.warning("Method " + md.getName() + " has " + b.getStatements().size() + " lines.");
                log.warning("Method " + md.getName() + " is too long, maximum recommended number of statements is 15.");
            }
        });
    }
}

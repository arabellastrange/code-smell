import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class MethodLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, Void count) {
        super.visit(md, count);
        final AtomicInteger methodSize = new AtomicInteger();

        md.getBody().ifPresent(b -> {
            methodSize.set(b.getStatements().size());
            if (methodSize.get() > 15) {
                log.warning("Method " + md.getName() + " has " + methodSize.get() + " lines.");
                log.warning("Method " + md.getName() + " is too long, maximum recommended number of statements is 15.");
            }
        });
    }
}

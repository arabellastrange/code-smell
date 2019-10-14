import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class MethodLengthCounter extends VoidVisitorAdapter<Integer> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, Integer count) {
        super.visit(md, count);
        final AtomicInteger methodSize = new AtomicInteger();

        md.getBody().ifPresent(b -> {
            methodSize.set(b.getStatements().size());
            if (methodSize.get() > 15) {
                log.warning("Method " + md.getName() + " has " + methodSize.get() + " lines.");
                log.warning("Method " + md.getName() + " is too long, maximum recommended number of statements is 15.");
            }
        });

        count = methodSize.get();
    }
}

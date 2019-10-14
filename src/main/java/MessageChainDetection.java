import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;
import java.util.logging.Logger;

public class MessageChainDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MessageChainDetection.class.getName());

    @Override
    public void visit(MethodCallExpr mc, Void count) {
        //TODO Fix: scope.toString() gives message chains for some methods but not the complete message chain for intermediateOp
        Optional scope = mc.getScope();
        String name = mc.getNameAsString();

        //log.info("Method Call: " + scope + " - " + name);

        if(scope.toString().contains("().")){
            log.warning("Method With Message Chain: " + name);
        }
    }
}

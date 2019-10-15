import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class MessageChainDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MessageChainDetection.class.getName());

    @Override
    public void visit(MethodCallExpr mc, Void count) {
        //TODO fix character counting
        Optional scope = mc.getScope();
        String name = mc.getNameAsString();
        ArrayList<Character> chars = new ArrayList<Character>();
        //log.info("Method Call: " + scope + " - " + name);

        //if more than 2 method calls, raise warning
        for(int i = 0; i < scope.toString().length(); i++){
            final char c = scope.toString().charAt(i);
            if(c == '.'){
                chars.add(c);
            }
        }

        if(chars.size() > 2){
            log.warning("Method With Message Chain: " + name);
        }
//        if(scope.toString().contains("().")){
//            log.warning("Method With Message Chain: " + name);
//        }
    }
}

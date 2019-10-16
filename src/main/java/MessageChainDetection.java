import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class MessageChainDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MessageChainDetection.class.getName());

    @Override
    public void visit(MethodCallExpr mc, Void count) {
        Optional scope = mc.getScope();
        String scopeString = scope.toString();
        String name = mc.getNameAsString();
        ArrayList<Character> chars = new ArrayList<Character>();


        //if more than 2 method calls, raise warning
        for(int i = 0; i < scopeString.length(); i++){
            final char c = scopeString.charAt(i);
            if(c == '.'){
                chars.add(c);
            }
        }

        if(chars.size() > 1){
            log.warning("Method " + name + " contains a message chain.");
        }

    }
}

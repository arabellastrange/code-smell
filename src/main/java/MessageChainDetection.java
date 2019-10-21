import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class MessageChainDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MessageChainDetection.class.getName());

    /**
     * A message chain occurs when a client requests another object, that object requests yet another one, and so on.
     * These chains mean that the client is dependent on navigation along the class structure.
     * Any changes in these relationships require modifying the client.
     */

    @Override
    public void visit(MethodCallExpr mc, Void count) {
        Optional scope = mc.getScope();
        String scopeString = scope.toString();
        String name = mc.getNameAsString();
        ArrayList<Character> chars = new ArrayList<>();


        //if more than 2 method calls, raise warning
        for(int i = 0; i < scopeString.length(); i++){
            final char c = scopeString.charAt(i);
            if(c == '.'){
                chars.add(c);
            }
        }

        if(chars.size() > 1){
            log.warning("Method " + name + " in " + mc.getClass().getCanonicalName() + " contains a message chain.");
        }

    }
}

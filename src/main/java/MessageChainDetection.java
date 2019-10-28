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
        for(int i = 1; i < scopeString.length()-1; i++){
            final char b = scopeString.charAt(i-1);
            final char c = scopeString.charAt(i);
            final char d = scopeString.charAt(i+1);

            if(c == '.' && !Character.isDigit(d) ){
                chars.add(c);
                //System.out.println(scopeString);
            }
        }

//        if(chars.size() > 2){
//            System.out.println(scope.toString());
//            log.warning("Method " + name + " contains a message chain.");
//        }

        if (scope.toString().contains(").") && !scope.toString().contains("new ")) {
            System.out.println("Class: " + name + " -- " + scope);
            log.warning("Method With Message Chain: " + name);
        }
    }
}

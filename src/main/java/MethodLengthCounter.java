import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class MethodLengthCounter extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodLengthCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, Void count) {
        super.visit(md, count);
        int methodSize = 0;

        if(md.getBody().isPresent()){
            NodeList<Statement> statements = md.getBody().get().getStatements();

            for(Statement statement: statements){
                methodSize += statement.getChildNodes().size();
            }

            if(methodSize > 15){
                log.info("Statements are: " + md.getBody().get().getStatements().toString());
                log.warning("Method " + md.getDeclarationAsString() + " has " + methodSize + " Statements.");
                log.warning("Method " + md.getDeclarationAsString() + " is too long, maximum recommended number of statements is 15.");
            }
        }
    }
}

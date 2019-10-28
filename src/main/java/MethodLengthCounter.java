import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.logging.Logger;

public class MethodLengthCounter extends VoidVisitorAdapter<List<Integer>> {
    private static final Logger log = Logger.getLogger(MethodLengthCounter.class.getName());

    @Override
    public void visit(MethodDeclaration md, List<Integer> methodSize) {
        super.visit(md, methodSize);
        int length = 0;

        if (md.getBody().isPresent()) {
            NodeList<Statement> statements = md.getBody().get().getStatements();

            for (Statement statement : statements) {
                length += statement.getChildNodes().size();
            }

            if (length > 15) {
                log.info("Statements are: " + md.getBody().get().getStatements().toString());
                log.warning("Method " + md.getDeclarationAsString() + " has " + methodSize + " Statements.");
                log.warning("Method " + md.getDeclarationAsString() + " is too long, maximum recommended number of statements is 15.");
            }
        }
        methodSize.clear();
        methodSize.add(length);
    }
}

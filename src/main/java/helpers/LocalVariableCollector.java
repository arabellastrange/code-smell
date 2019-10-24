package helpers;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class LocalVariableCollector extends VoidVisitorAdapter<List<VariableDeclarationExpr>> {
    @Override
    public void visit(MethodDeclaration md, List<VariableDeclarationExpr> variables) {
        super.visit(md, variables);
        if (md.getBody().isPresent()) {
            for (Statement st : md.getBody().get().getStatements()) {
                if (st.isExpressionStmt()) {
                    Expression expression = (st.asExpressionStmt()).getExpression();
                    if (expression instanceof VariableDeclarationExpr) {
                        variables.add((VariableDeclarationExpr) expression);
                    }
                }
            }
        }
    }

}

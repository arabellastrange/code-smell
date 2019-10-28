import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.ClassHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataClassDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(DataClassDetection.class.getName());

    /**
     * A data class refers to a class that contains only fields and crude methods for accessing them (getters and setters).
     * These are simply containers for data used by other classes.
     * These classes don’t contain any additional functionality and can’t independently operate on the data that they own.
     */

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
        super.visit(cd, arg);

        log.info("Calling DataClassDetection on " + cd.getName());

        if (!cd.isInterface() && !ClassHelper.isExceptionClass(cd) && !cd.isEmpty()) {
            List<MethodDeclaration> methods = cd.getMethods();
            log.info("Class" + cd.getName() + " has " + methods.size() + " methods in class ");
            methods.forEach(method -> log.info("The methods of " + cd.getName() + " are " + method.getName()));

            List<MethodDeclaration> functionalMethods = filterGetterOrSetterMethods(methods);

            if (functionalMethods.isEmpty()) {
                log.warning("This class " + cd.getName() + " contains no logic, only data access methods");
            }
        }

    }

    private List<MethodDeclaration> filterGetterOrSetterMethods(List<MethodDeclaration> allMethods) {
        List<MethodDeclaration> functionalMethods = new ArrayList<>();

        allMethods.forEach(m -> {
            if (m.getBody().isPresent()) {
                List<Statement> statements = m.getBody().get().getStatements();
                List<Statement> functionalStatements = filterSetterOrGetterStatements(statements);
                if (!functionalStatements.isEmpty()) {
                    functionalMethods.add(m);
                }
            }
        });

        return functionalMethods;
    }

    private List<Statement> filterSetterOrGetterStatements(List<Statement> statements) {
        List<Statement> functionalStatements = new ArrayList<>();
        for (Statement st : statements) {
            if (st.isBlockStmt()) {
                for (Statement statement : st.asBlockStmt().getStatements()) {
                    if (!isSetterOrGetterStatement(statement)) {
                        functionalStatements.add(statement);
                    }
                }
            } else {
                st.toBlockStmt();
                if (!isSetterOrGetterStatement(st)) {
                    functionalStatements.add(st);
                }
            }
        }
        return functionalStatements;
    }

    private boolean isSetterOrGetterStatement(Statement statement) {
        if (statement.isExpressionStmt()) {
            Expression expression = (statement.asExpressionStmt()).getExpression();
            if (expression instanceof AssignExpr || expression instanceof NameExpr) {
                log.info("Assign statement or name statement: " + expression.toString());
                AssignExpr.Operator operator = expression.asAssignExpr().getOperator();
                if (operator.equals(AssignExpr.Operator.ASSIGN)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (statement.isReturnStmt()) {
            Expression expression = statement.asReturnStmt().getExpression().get();
            log.info("Return statement: " + expression.toString());
            if (expression.isLiteralExpr()) {
                return true;
            } else {
                return false;
            }
        } else {
            log.info("Other Statement: " + statement.toString());
            return true;
        }
    }

}

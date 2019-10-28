import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.ClassHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
* Majority of methods just delegations to one other class or interface.
*/
public class MiddleManDetection extends VoidVisitorAdapter<Void> {
    private static final Logger log = Logger.getLogger(MethodParameterCounter.class.getName());

    public void visit(ClassOrInterfaceDeclaration cd, Void arg){
        if(!cd.isInterface() && !ClassHelper.isExceptionClass(cd) && !cd.isEmpty()){
            List<MethodDeclaration> methods = cd.getMethods();

            if(!methods.isEmpty()) {
                List<MethodDeclaration> usefulMethods = checkMethods(methods);

                if (usefulMethods.isEmpty()) {
                    log.warning("Class: " + cd.getNameAsString() + " contains no useful methods");
                }
            }
        }
    }

    public List<MethodDeclaration> checkMethods(List<MethodDeclaration> methods){
        List<MethodDeclaration> usefulMethods = new ArrayList<>();

        methods.forEach(m -> {
            if (m.getBody().isPresent()) {
                List<Statement> statements = m.getBody().get().getStatements();
                List<Statement> usefulStatements = filterUsefulStatements(statements);
                String typeString = m.getType().toString();
                if(statements.size() > 1){
                    usefulMethods.add(m);
                }else if (!usefulStatements.isEmpty()) {
                    usefulMethods.add(m);
                }
            }
        });
        return usefulMethods;
    }

    public List<Statement> filterUsefulStatements(List<Statement> statements){
        List<Statement> usefulStatements = new ArrayList<>();
        for(Statement s : statements){
            if(s.isExpressionStmt() || s.isAssertStmt() || s.isForEachStmt() || s.isForStmt() || s.isIfStmt() || s.isSwitchStmt() || s.isTryStmt() || s.isThrowStmt()){
                usefulStatements.add(s);
            }else if (s.isReturnStmt()){

            }
        }
        return usefulStatements;
    }
}

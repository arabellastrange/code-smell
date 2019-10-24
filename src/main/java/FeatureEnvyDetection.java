import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import helpers.LocalVariableCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FeatureEnvyDetection extends VoidVisitorAdapter<Void> {
    //A method accesses the data of another object more than its own data.

    private static final Logger log = Logger.getLogger(FeatureEnvyDetection.class.getName());
    private List<FieldDeclaration> fields = new ArrayList<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void count){
        super.visit(cd, count);
        log.info("Calling FeatureEnvyDetection on " + cd.getName());

        fields = cd.getFields();

    }

    private void visitMethods(List<MethodDeclaration> methodDeclarations){
        methodDeclarations.forEach(methodDeclaration -> {
            List<VariableDeclarationExpr> localVariables = new ArrayList<>();
            VoidVisitor<List<VariableDeclarationExpr>> localVariableCollector = new LocalVariableCollector();

            localVariableCollector.visit(methodDeclaration, localVariables);
            visitMethod(methodDeclaration.getParameters(), localVariables);
        });
    }

    private void visitMethod(NodeList<Parameter> parameters, List<VariableDeclarationExpr> localVariables) {
        //todo
    }

}

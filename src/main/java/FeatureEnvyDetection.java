import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FeatureEnvyDetection extends VoidVisitorAdapter<Void> {
    //A method accesses the data of another object more than its own data.

    private static final Logger log = Logger.getLogger(FeatureEnvyDetection.class.getName());
    private List<FieldDeclaration> fields = new ArrayList<>();
    private ClassOrInterfaceDeclaration classOrInterfaceDeclaration;

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Void count) {
        log.info("Calling FeatureEnvyDetection on " + cd.getName());
        super.visit(cd, count);
        classOrInterfaceDeclaration = cd;
        fields = cd.getFields();

        visitMethods(cd.getMethods());
    }

    private void visitMethods(List<MethodDeclaration> methodDeclarations) {

        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            List<Node> inClassStatements = new ArrayList<>();
            methodDeclaration.getBody().ifPresent(bd -> bd.getStatements().forEach(
                    statement -> statement
                            .getChildNodes()
                            .forEach(child -> child.getChildNodes().forEach(c -> {
                                        log.info("child node: " + c.getChildNodes());
                                        inClassStatements.addAll(c.findAll(classOrInterfaceDeclaration.getClass()));
                                    })
                            )
            ));
            log.info("Local nodes: " + inClassStatements.toString());
        }
    }
}

package helpers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassCollector extends VoidVisitorAdapter<List<ClassOrInterfaceDeclaration>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration cd, List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations) {
        super.visit(cd, classOrInterfaceDeclarations);
        classOrInterfaceDeclarations.add(cd);
    }

}

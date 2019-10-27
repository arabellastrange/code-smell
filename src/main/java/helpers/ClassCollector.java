package helpers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassCollector extends VoidVisitorAdapter<List<ClassOrInterfaceDeclaration>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration cd, List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations) {
        super.visit(cd, classOrInterfaceDeclarations);
        classOrInterfaceDeclarations.add(cd);
    }

}

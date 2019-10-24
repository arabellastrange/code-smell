package helpers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ClassHelper {
    private ClassHelper(){}

    public static boolean isExceptionClass(ClassOrInterfaceDeclaration cd) {
        if (!cd.getExtendedTypes().isEmpty()) {
            ClassOrInterfaceType extended = cd.getExtendedTypes().get(0);
            String name = extended.getNameAsString();
            return name.contains("Exception") || name.contains("Error") || name.contains("Throwable");
        }
        return false;
    }
}

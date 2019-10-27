package helpers;

import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileToCompilation {
    private static Map<String, CompilationUnit> fileNameToCompilationUnit = new HashMap<>();

    public FileToCompilation(List<File> files, List<CompilationUnit> units) {
        for (int i = 0; i < files.size(); i++) {
            String fileName = files.get(i).getName().substring(0, files.get(i).getName().lastIndexOf('.'));
            fileNameToCompilationUnit.put(fileName, units.get(i));
        }
    }

    public static Optional<CompilationUnit> getCompilationUnitByName(String name) {
        return Optional.ofNullable(fileNameToCompilationUnit.get(name));
    }

}

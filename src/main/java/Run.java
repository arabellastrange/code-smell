import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import helpers.FileToCompilation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Run {

    private static final String DIRECTORY = "src/main/java/testcode/";
    private static List<File> files = new ArrayList<>();

    private static final Logger log = Logger.getLogger(Run.class.getName());

    public static void main(String[] args) {
        Iterator it = FileUtils.iterateFiles(new File(DIRECTORY), null, true);
        log.info("Loaded directory: " + DIRECTORY);

        while (it.hasNext()) {
            files.add((File) it.next());
        }
        analyseFiles(files);
    }

    private static void analyseFiles(List<File> files) {
        List<CompilationUnit> units = new ArrayList<>();

        for (File file : files) {
            try {
                CompilationUnit cu = StaticJavaParser.parse(file);
                units.add(cu);
            } catch (FileNotFoundException e) {
                log.warning("File not found exception: " + e.getMessage());
            }
        }

        initializeMap(files, units);
        callSmellDetectors(units);
    }

    private static void initializeMap(List<File> files, List<CompilationUnit> units) {
        new FileToCompilation(files, units);
    }

    private static void callSmellDetectors(List<CompilationUnit> units) {
        for (CompilationUnit cu : units) {
            List<Integer> methodSize = new ArrayList<>();

            VoidVisitor<?> methodParameterCounter = new MethodParameterCounter();
            VoidVisitor<List<Integer>> methodLengthCounter = new MethodLengthCounter();
            VoidVisitor<?> classLengthCounter = new ClassLengthCounter();
            VoidVisitor<?> dataClassDetection = new DataClassDetection();
            VoidVisitor<?> methodChainDetector = new MessageChainDetection();
            VoidVisitor<?> middleManDetector = new MiddleManDetection();
            VoidVisitor<?> refusedBequestDetector = new RefusedBequestDetection();


//            methodParameterCounter.visit(cu, null);
//            methodLengthCounter.visit(cu, methodSize);
//            classLengthCounter.visit(cu, null);
            dataClassDetection.visit(cu, null);
//            methodChainDetector.visit(cu, null);
//            middleManDetector.visit(cu, null);
//            refusedBequestDetector.visit(cu, null);
        }
    }

}



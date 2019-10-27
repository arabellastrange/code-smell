import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Run {

    private static final String direcotry = "src/main/java/testcode/";
    private static List<File> files = new ArrayList<>();

    private static final Logger log = Logger.getLogger(Run.class.getName());

    public static void main(String[] args) {
        Iterator it = FileUtils.iterateFiles(new File(direcotry), null, true);
        log.info("Loaded directory: " + direcotry);

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
        callSmellDetectors(units);
    }

    private static void callSmellDetectors(List<CompilationUnit> units) {
        for (CompilationUnit cu : units) {
//            VoidVisitor<?> methodParameterCounter = new MethodParameterCounter();
//            VoidVisitor<?> methodLengthCounter = new MethodLengthCounter();
//            VoidVisitor<?> classLengthCounter = new ClassLengthCounter();
//            VoidVisitor<?> dataClassDetection = new DataClassDetection();
//            VoidVisitor<?> methodChainDetector = new MessageChainDetection();
//            VoidVisitor<?> middleManDetector = new MiddleManDetection();
            VoidVisitor<?> refusedBequestDetector = new RefusedBequestDetection();
//            methodParameterCounter.visit(cu, null);
//            methodLengthCounter.visit(cu, null);
//            classLengthCounter.visit(cu, null);
//            dataClassDetection.visit(cu, null);
//            methodChainDetector.visit(cu, null);
//            middleManDetector.visit(cu, null);
            refusedBequestDetector.visit(cu, null);
        }
    }
}



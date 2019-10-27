import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Run {

    private static final String direcotry = "src/main/java/testcode/";
    private static List<InputStream> files = new ArrayList<>();

    private static final Logger log = Logger.getLogger(Run.class.getName());

    public static void main(String[] args) {
        Iterator it = FileUtils.iterateFiles(new File(direcotry), null, true);
        log.info("Loaded directory: " + direcotry);

        while (it.hasNext()) {
            InputStream inputStream;
            try {
                inputStream = new FileInputStream((File) it.next());
                files.add(inputStream);
            } catch (IOException e) {
                log.warning("File not found exception: " + e.getMessage());
            }
        }
        analyseFiles(files);
    }

    private static void analyseFiles(List<InputStream> files) {
        Enumeration<InputStream> stream = Collections.enumeration(files);
        InputStream inputStream = new SequenceInputStream(stream);
        CompilationUnit cu = StaticJavaParser.parse(inputStream);


        VoidVisitor<?> methodParameterCounter = new MethodParameterCounter();
        VoidVisitor<?> methodLengthCounter = new MethodLengthCounter();
        VoidVisitor<?> classLengthCounter = new ClassLengthCounter();
        VoidVisitor<?> dataClassDetection = new DataClassDetection();
        VoidVisitor<?> methodChainDetector = new MessageChainDetection();
        VoidVisitor<?> middleManDetector = new MiddleManDetection();
        VoidVisitor<?> refusedBequestDetector = new RefusedBequestDetection();

        methodParameterCounter.visit(cu, null);
        methodLengthCounter.visit(cu, null);
        classLengthCounter.visit(cu, null);
        dataClassDetection.visit(cu, null);
        methodChainDetector.visit(cu, null);
        middleManDetector.visit(cu, null);
        refusedBequestDetector.visit(cu, null);
    }
}



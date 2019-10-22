import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class Run {

    //TODO automate scan and adding files in path
    /**
     * "src/main/java/testcode/Abusers/RefusedBequest/Account.java", "src/main/java/testcode/Abusers/RefusedBequest/ChqAcc.java",
     * "src/main/java/testcode/Abusers/RefusedBequest/SavingsAcc.java", "src/main/java/testcode/Abusers/BarnsleyFernTwo.java",
     * "src/main/java/testcode/Couplers/MessageChains/Child.java", "src/main/java/testcode/Couplers/MessageChains/Client.java",
     * "src/main/java/testcode/Couplers/MessageChains/Intermediate.java", "src/main/java/testcode/Couplers/MessageChains/Parent.java",
     **/
    private static final String[] FILE_PATHS = new String[]{"src/main/java/testcode/Bloaters/Grid.java", "src/main/java/testcode/FalsePositives/BoxingTheCompass.java",
            "src/main/java/testcode/Dispensibles/Luhn.java", "src/main/java/testcode/Dispensibles/CipollasAlgorithm.java", "src/main/java/testcode/Dispensibles/Test.java"};

    private static final Logger log = Logger.getLogger(Run.class.getName());

    public static void main(String[] args) {
        analyseFiles();
    }

    private static void analyseFiles() {
        try {
            for (String FILE_PATH : FILE_PATHS) {

                log.info("Analysing class: " + FILE_PATH.substring(FILE_PATH.lastIndexOf('/') + 1));

                CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));
                VoidVisitor<?> methodParameterCounter = new MethodParameterCounter();
                VoidVisitor<?> methodLengthCounter = new MethodLengthCounter();
                VoidVisitor<?> classLengthCounter = new ClassLengthCounter();
                VoidVisitor<?> dataClassDetection = new DataClassDetection();
                VoidVisitor<?> methodChainDetector = new MessageChainDetection();

                methodParameterCounter.visit(cu, null);
                methodLengthCounter.visit(cu, null);
                classLengthCounter.visit(cu, null);
                dataClassDetection.visit(cu, null);
                methodChainDetector.visit(cu, null);
            }

        } catch (FileNotFoundException e) {
            log.warning("File not found: " + e);
        }
    }
}



package actions;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AllureReportGenerator {

    private static final String ALLURE_PATH = "C:\\allure-2.44.0\\bin\\allure.bat";

    public static void generaReport() {

        try {

            File resultsDir = new File("target/allure-results");

            if (!resultsDir.exists()
                    || resultsDir.listFiles() == null
                    || resultsDir.listFiles().length == 0) {

                log.error("No existen resultados de Allure en {}", resultsDir.getAbsolutePath());
                return;
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File reportDir = new File("reports/allure-" + timestamp);

            log.info("Generando reporte Allure...");

            ProcessBuilder generate = new ProcessBuilder(
                    ALLURE_PATH,
                    "generate",
                    resultsDir.getAbsolutePath(),
                    "-o",
                    reportDir.getAbsolutePath(),
                    "--clean"
            );

            generate.directory(new File(System.getProperty("user.dir")));
            generate.redirectErrorStream(true);

            Process generateProcess = generate.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(generateProcess.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            int exitCode = generateProcess.waitFor();

            if (exitCode != 0) {
                log.error("Error al generar el reporte. Código: {}", exitCode);
                return;
            }

            log.info("Reporte generado correctamente.");
            log.info("Ubicación: {}", reportDir.getAbsolutePath());

            // Esperar un momento para asegurar que todos los archivos fueron escritos
            Thread.sleep(1000);

            log.info("Abriendo reporte...");

            ProcessBuilder open = new ProcessBuilder(
                    ALLURE_PATH,
                    "open",
                    reportDir.getAbsolutePath()
            );

            open.directory(new File(System.getProperty("user.dir")));
            open.redirectErrorStream(true);

            Process openProcess = open.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openProcess.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

        } catch (Exception e) {

            log.error("Error generando el reporte Allure", e);

        }

    }

}
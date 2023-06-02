    package com.dateBuzz.backend.util;

    import org.springframework.stereotype.Component;

    import java.io.IOException;

    @Component
    public class TelegramNotifier {

        public void sendErrorMessage(String errorMessage) {
            try {
                String currentPath = System.getProperty("user.dir");
                String scriptPath = currentPath + "/python/main.py";
                String command = "python3 " + scriptPath + " '" + errorMessage + "'";

                Process process = Runtime.getRuntime().exec(command);

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("Python script executed successfully.");
                } else {
                    System.out.println("Error executing Python script. Exit code: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
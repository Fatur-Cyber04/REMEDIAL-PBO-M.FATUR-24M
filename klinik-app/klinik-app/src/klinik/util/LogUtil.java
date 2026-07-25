package klinik.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    private static final String FILE_LOG = "log_aktivitas.txt";

    public static void tulisLog(String aktivitas) {
        try (FileWriter fw = new FileWriter(FILE_LOG, true);
            PrintWriter pw = new PrintWriter(fw)) {

            String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            pw.println("[" + waktu + "] " + aktivitas);

        } catch (IOException e) {
            System.err.println("Gagal menulis log: " + e.getMessage());
        }
    }
}

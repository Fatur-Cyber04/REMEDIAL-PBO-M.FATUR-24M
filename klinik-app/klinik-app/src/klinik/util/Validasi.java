package klinik.util;

public class Validasi {

    public static boolean isKosong(String teks) {
        return teks == null || teks.trim().isEmpty();
    }

    public static boolean isNoTelpValid(String noTelp) {
        if (isKosong(noTelp)) return false;
        String bersih = noTelp.trim().replaceAll("[^0-9]", "");
        return bersih.matches("\\d{8,15}");
    }

    public static String generateNoRm(int nomorUrut) {
        return "RM" + String.format("%04d", nomorUrut);
    }

    public static String kapitalisasi(String teks) {
        if (isKosong(teks)) return teks;
        String[] kata = teks.trim().toLowerCase().split("\\s+");
        StringBuilder hasil = new StringBuilder();
        for (String k : kata) {
            hasil.append(Character.toUpperCase(k.charAt(0)))
                .append(k.substring(1))
                .append(" ");
        }
        return hasil.toString().trim();
    }

    public static double parseDoubleAman(String teks, double defaultValue) {
        try {
            return Double.parseDouble(teks.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseIntAman(String teks, int defaultValue) {
        try {
            return Integer.parseInt(teks.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

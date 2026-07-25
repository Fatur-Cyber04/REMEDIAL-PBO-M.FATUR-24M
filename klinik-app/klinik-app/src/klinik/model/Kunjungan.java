package klinik.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Kunjungan {
    private int idKunjungan;
    private int idPasien;
    private int idDokter;
    private String namaPasien;   
    private String namaDokter;   
    private LocalDateTime tanggalKunjungan;
    private String keluhan;
    private String diagnosa;
    private double biayaPeriksa;
    private List<ResepObat> daftarResep = new ArrayList<>();

    public Kunjungan(int idKunjungan, int idPasien, int idDokter, LocalDateTime tanggalKunjungan,
                String keluhan, String diagnosa, double biayaPeriksa) {
        this.idKunjungan = idKunjungan;
        this.idPasien = idPasien;
        this.idDokter = idDokter;
        this.tanggalKunjungan = tanggalKunjungan;
        this.keluhan = keluhan;
        this.diagnosa = diagnosa;
        this.biayaPeriksa = biayaPeriksa;
    }

    public int getIdKunjungan() { return idKunjungan; }
    public void setIdKunjungan(int idKunjungan) { this.idKunjungan = idKunjungan; }

    public int getIdPasien() { return idPasien; }
    public void setIdPasien(int idPasien) { this.idPasien = idPasien; }

    public int getIdDokter() { return idDokter; }
    public void setIdDokter(int idDokter) { this.idDokter = idDokter; }

    public String getNamaPasien() { return namaPasien; }
    public void setNamaPasien(String namaPasien) { this.namaPasien = namaPasien; }

    public String getNamaDokter() { return namaDokter; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }

    public LocalDateTime getTanggalKunjungan() { return tanggalKunjungan; }
    public void setTanggalKunjungan(LocalDateTime tanggalKunjungan) { this.tanggalKunjungan = tanggalKunjungan; }

    public String getKeluhan() { return keluhan; }
    public void setKeluhan(String keluhan) { this.keluhan = keluhan; }

    public String getDiagnosa() { return diagnosa; }
    public void setDiagnosa(String diagnosa) { this.diagnosa = diagnosa; }

    public double getBiayaPeriksa() { return biayaPeriksa; }
    public void setBiayaPeriksa(double biayaPeriksa) { this.biayaPeriksa = biayaPeriksa; }

    public List<ResepObat> getDaftarResep() { return daftarResep; }
    public void tambahResep(ResepObat resep) { this.daftarResep.add(resep); }

    public double getTotalBiaya() {
        double totalObat = 0;
        for (ResepObat r : daftarResep) {
            totalObat += r.getSubtotal();
        }
        return biayaPeriksa + totalObat;
    }
}

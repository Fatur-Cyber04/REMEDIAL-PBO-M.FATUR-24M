package klinik.model;

import java.time.LocalDate;

public class Pasien extends Person {

    private int idPasien;
    private String noRm;
    private String jenisKelamin; 
    private LocalDate tanggalLahir;
    private String alamat;

    public Pasien(int idPasien, String noRm, String nama, String jenisKelamin,
            LocalDate tanggalLahir, String alamat, String noTelp) {
        super(nama, noTelp); 
        this.idPasien = idPasien;
        this.noRm = noRm;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
    }

    public Pasien(String noRm, String nama, String jenisKelamin,
                LocalDate tanggalLahir, String alamat, String noTelp) {
        this(0, noRm, nama, jenisKelamin, tanggalLahir, alamat, noTelp);
    }

    public int getIdPasien() { return idPasien; }
    public void setIdPasien(int idPasien) { this.idPasien = idPasien; }

    public String getNoRm() { return noRm; }
    public void setNoRm(String noRm) { this.noRm = noRm; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String getInfo() {
        return "[Pasien] " + noRm + " - " + getNama() + " (" + jenisKelamin + ")";
    }
}

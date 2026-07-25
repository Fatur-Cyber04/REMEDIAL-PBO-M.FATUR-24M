package klinik.model;

public class Dokter extends Person {

    private int idDokter;
    private String spesialisasi;

    public Dokter(int idDokter, String nama, String spesialisasi, String noTelp) {
        super(nama, noTelp);
        this.idDokter = idDokter;
        this.spesialisasi = spesialisasi;
    }

    public Dokter(String nama, String spesialisasi, String noTelp) {
        this(0, nama, spesialisasi, noTelp);
    }

    public int getIdDokter() { return idDokter; }
    public void setIdDokter(int idDokter) { this.idDokter = idDokter; }

    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }

    @Override
    public String getInfo() {
        return "[Dokter] " + getNama() + " - Spesialis " + spesialisasi;
    }
}

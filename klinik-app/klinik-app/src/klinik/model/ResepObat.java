package klinik.model;

public class ResepObat {
    private int idResep;
    private int idKunjungan;
    private int idObat;
    private String namaObat; 
    private int jumlah;
    private double subtotal;

    public ResepObat(int idResep, int idKunjungan, int idObat, String namaObat, int jumlah, double subtotal) {
        this.idResep = idResep;
        this.idKunjungan = idKunjungan;
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public int getIdResep() { return idResep; }
    public int getIdKunjungan() { return idKunjungan; }
    public int getIdObat() { return idObat; }
    public String getNamaObat() { return namaObat; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal() { return subtotal; }

    public void setIdResep(int idResep) { this.idResep = idResep; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

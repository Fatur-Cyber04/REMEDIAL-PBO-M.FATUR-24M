package klinik.model;

public class Obat {
    private int idObat;
    private String namaObat;
    private String satuan;
    private double harga;
    private int stok;

    public Obat(int idObat, String namaObat, String satuan, double harga, int stok) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.satuan = satuan;
        this.harga = harga;
        this.stok = stok;
    }

    public Obat(String namaObat, String satuan, double harga, int stok) {
        this(0, namaObat, satuan, harga, stok);
    }

    public int getIdObat() { return idObat; }
    public void setIdObat(int idObat) { this.idObat = idObat; }

    public String getNamaObat() { return namaObat; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return namaObat + " (Stok: " + stok + ", Rp" + harga + ")";
    }
}

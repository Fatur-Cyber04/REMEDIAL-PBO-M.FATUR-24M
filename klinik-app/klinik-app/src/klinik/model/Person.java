package klinik.model;

public abstract class Person {

    private String nama;
    private String noTelp;

    public Person(String nama, String noTelp) {
        this.nama = nama;
        this.noTelp = noTelp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public abstract String getInfo();

    @Override
    public String toString() {
        return getInfo();
    }
}

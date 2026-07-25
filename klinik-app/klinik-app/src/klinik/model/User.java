package klinik.model;

public class User {
    private int idUser;
    private String username;
    private String password;
    private String namaLengkap;
    private String role; 

    public User(int idUser, String username, String password, String namaLengkap, String role) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.role = role;
    }

    public int getIdUser() { return idUser; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getRole() { return role; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}

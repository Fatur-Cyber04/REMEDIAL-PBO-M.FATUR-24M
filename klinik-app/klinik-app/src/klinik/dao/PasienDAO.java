package klinik.dao;

import klinik.model.Pasien;
import klinik.util.KoneksiDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PasienDAO implements ICrud<Pasien> {

    @Override
    public void tambah(Pasien p) throws SQLException {
        String sql = "INSERT INTO pasien (no_rm, nama, jenis_kelamin, tanggal_lahir, alamat, no_telp) VALUES (?,?,?,?,?,?)";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNoRm());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenisKelamin());
            ps.setDate(4, p.getTanggalLahir() != null ? Date.valueOf(p.getTanggalLahir()) : null);
            ps.setString(5, p.getAlamat());
            ps.setString(6, p.getNoTelp());
            ps.executeUpdate();
        }
    }

    @Override
    public void ubah(Pasien p) throws SQLException {
        String sql = "UPDATE pasien SET no_rm=?, nama=?, jenis_kelamin=?, tanggal_lahir=?, alamat=?, no_telp=? WHERE id_pasien=?";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNoRm());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getJenisKelamin());
            ps.setDate(4, p.getTanggalLahir() != null ? Date.valueOf(p.getTanggalLahir()) : null);
            ps.setString(5, p.getAlamat());
            ps.setString(6, p.getNoTelp());
            ps.setInt(7, p.getIdPasien());
            ps.executeUpdate();
        }
    }

    @Override
    public void hapus(int id) throws SQLException {
        String sql = "DELETE FROM pasien WHERE id_pasien=?";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Pasien> tampilkanSemua() throws SQLException {
        List<Pasien> daftar = new ArrayList<>();
        String sql = "SELECT * FROM pasien ORDER BY id_pasien DESC";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    @Override
    public List<Pasien> cari(String kataKunci) throws SQLException {
        List<Pasien> daftar = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE nama LIKE ? OR no_rm LIKE ? ORDER BY id_pasien DESC";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + kataKunci + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(mapRow(rs));
                }
            }
        }
        return daftar;
    }

    private Pasien mapRow(ResultSet rs) throws SQLException {
        Date tglLahir = rs.getDate("tanggal_lahir");
        return new Pasien(
                rs.getInt("id_pasien"),
                rs.getString("no_rm"),
                rs.getString("nama"),
                rs.getString("jenis_kelamin"),
                tglLahir != null ? tglLahir.toLocalDate() : null,
                rs.getString("alamat"),
                rs.getString("no_telp")
        );
    }
}

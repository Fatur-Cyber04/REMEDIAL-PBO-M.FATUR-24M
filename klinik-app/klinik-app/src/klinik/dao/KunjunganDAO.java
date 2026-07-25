package klinik.dao;

import klinik.exception.DataTidakDitemukanException;
import klinik.exception.StokObatException;
import klinik.model.Kunjungan;
import klinik.model.ResepObat;
import klinik.util.KoneksiDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KunjunganDAO implements ICrud<Kunjungan> {

    private ObatDAO obatDAO = new ObatDAO();

    @Override
    public void tambah(Kunjungan k) throws SQLException {
        String sql = "INSERT INTO kunjungan (id_pasien, id_dokter, tanggal_kunjungan, keluhan, diagnosa, biaya_periksa) VALUES (?,?,?,?,?,?)";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, k.getIdPasien());
            ps.setInt(2, k.getIdDokter());
            ps.setTimestamp(3, Timestamp.valueOf(k.getTanggalKunjungan() != null ? k.getTanggalKunjungan() : LocalDateTime.now()));
            ps.setString(4, k.getKeluhan());
            ps.setString(5, k.getDiagnosa());
            ps.setDouble(6, k.getBiayaPeriksa());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    k.setIdKunjungan(keys.getInt(1));
                }
            }
        }
    }

    public void simpanResep(ResepObat resep) throws SQLException, StokObatException, DataTidakDitemukanException {
        obatDAO.kurangiStok(resep.getIdObat(), resep.getJumlah());

        String sql = "INSERT INTO resep_obat (id_kunjungan, id_obat, jumlah, subtotal) VALUES (?,?,?,?)";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resep.getIdKunjungan());
            ps.setInt(2, resep.getIdObat());
            ps.setInt(3, resep.getJumlah());
            ps.setDouble(4, resep.getSubtotal());
            ps.executeUpdate();
        }
    }

    @Override
    public void ubah(Kunjungan k) throws SQLException {
        String sql = "UPDATE kunjungan SET id_pasien=?, id_dokter=?, keluhan=?, diagnosa=?, biaya_periksa=? WHERE id_kunjungan=?";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, k.getIdPasien());
            ps.setInt(2, k.getIdDokter());
            ps.setString(3, k.getKeluhan());
            ps.setString(4, k.getDiagnosa());
            ps.setDouble(5, k.getBiayaPeriksa());
            ps.setInt(6, k.getIdKunjungan());
            ps.executeUpdate();
        }
    }

    @Override
    public void hapus(int id) throws SQLException {
        String sql = "DELETE FROM kunjungan WHERE id_kunjungan=?";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Kunjungan> tampilkanSemua() throws SQLException {
        List<Kunjungan> daftar = new ArrayList<>();
        String sql = "SELECT k.*, p.nama AS nama_pasien, d.nama AS nama_dokter " +
                    "FROM kunjungan k " +
                    "JOIN pasien p ON k.id_pasien = p.id_pasien " +
                    "JOIN dokter d ON k.id_dokter = d.id_dokter " +
                    "ORDER BY k.id_kunjungan DESC";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) daftar.add(mapRow(rs));
        }
        return daftar;
    }

    @Override
    public List<Kunjungan> cari(String kataKunci) throws SQLException {
        List<Kunjungan> daftar = new ArrayList<>();
        String sql = "SELECT k.*, p.nama AS nama_pasien, d.nama AS nama_dokter " +
                    "FROM kunjungan k " +
                    "JOIN pasien p ON k.id_pasien = p.id_pasien " +
                    "JOIN dokter d ON k.id_dokter = d.id_dokter " +
                    "WHERE p.nama LIKE ? OR d.nama LIKE ? " +
                    "ORDER BY k.id_kunjungan DESC";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + kataKunci + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) daftar.add(mapRow(rs));
            }
        }
        return daftar;
    }

    private Kunjungan mapRow(ResultSet rs) throws SQLException {
        Kunjungan k = new Kunjungan(
                rs.getInt("id_kunjungan"),
                rs.getInt("id_pasien"),
                rs.getInt("id_dokter"),
                rs.getTimestamp("tanggal_kunjungan").toLocalDateTime(),
                rs.getString("keluhan"),
                rs.getString("diagnosa"),
                rs.getDouble("biaya_periksa")
        );
        k.setNamaPasien(rs.getString("nama_pasien"));
        k.setNamaDokter(rs.getString("nama_dokter"));
        return k;
    }
}

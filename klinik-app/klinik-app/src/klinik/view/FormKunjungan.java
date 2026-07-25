package klinik.view;

import klinik.dao.*;
import klinik.exception.DataTidakDitemukanException;
import klinik.exception.StokObatException;
import klinik.model.*;
import klinik.util.LogUtil;
import klinik.util.Validasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FormKunjungan extends JFrame {

    private PasienDAO pasienDAO = new PasienDAO();
    private DokterDAO dokterDAO = new DokterDAO();
    private ObatDAO obatDAO = new ObatDAO();
    private KunjunganDAO kunjunganDAO = new KunjunganDAO();

    private JComboBox<Pasien> cbPasien;
    private JComboBox<Dokter> cbDokter;
    private JComboBox<Obat> cbObat;
    private JTextArea txtKeluhan, txtDiagnosa;
    private JTextField txtBiayaPeriksa, txtJumlahObat;
    private JTable tabelResep;
    private DefaultTableModel modelResep;

    private Kunjungan kunjunganAktif; 

    public FormKunjungan() {
        setTitle("Transaksi Kunjungan / Pemeriksaan");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelAtas = new JPanel(new GridLayout(0, 2, 5, 5));
        panelAtas.setBorder(BorderFactory.createTitledBorder("Data Kunjungan"));

        cbPasien = new JComboBox<>();
        cbDokter = new JComboBox<>();
        txtKeluhan = new JTextArea(2, 20);
        txtDiagnosa = new JTextArea(2, 20);
        txtBiayaPeriksa = new JTextField("25000");

        panelAtas.add(new JLabel("Pasien:")); panelAtas.add(cbPasien);
        panelAtas.add(new JLabel("Dokter:")); panelAtas.add(cbDokter);
        panelAtas.add(new JLabel("Keluhan:")); panelAtas.add(new JScrollPane(txtKeluhan));
        panelAtas.add(new JLabel("Diagnosa:")); panelAtas.add(new JScrollPane(txtDiagnosa));
        panelAtas.add(new JLabel("Biaya Periksa:")); panelAtas.add(txtBiayaPeriksa);

        JButton btnMulai = new JButton("Mulai Kunjungan Baru");
        btnMulai.addActionListener(e -> mulaiKunjungan());

        JPanel panelTengah = new JPanel(new GridLayout(1, 3, 5, 5));
        panelTengah.setBorder(BorderFactory.createTitledBorder("Tambah Resep Obat"));
        cbObat = new JComboBox<>();
        txtJumlahObat = new JTextField("1");
        JButton btnTambahResep = new JButton("Tambah Resep");
        panelTengah.add(cbObat);
        panelTengah.add(txtJumlahObat);
        panelTengah.add(btnTambahResep);
        btnTambahResep.addActionListener(e -> tambahResep());

        modelResep = new DefaultTableModel(new String[]{"Nama Obat", "Jumlah", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelResep = new JTable(modelResep);

        JButton btnSimpanTransaksi = new JButton("Simpan Transaksi Kunjungan");
        btnSimpanTransaksi.addActionListener(e -> simpanTransaksi());

        JPanel panelAtasGabung = new JPanel(new BorderLayout());
        panelAtasGabung.add(panelAtas, BorderLayout.CENTER);
        panelAtasGabung.add(btnMulai, BorderLayout.SOUTH);

        JPanel panelBawah = new JPanel(new BorderLayout(5, 5));
        panelBawah.add(panelTengah, BorderLayout.NORTH);
        panelBawah.add(new JScrollPane(tabelResep), BorderLayout.CENTER);
        panelBawah.add(btnSimpanTransaksi, BorderLayout.SOUTH);

        add(panelAtasGabung, BorderLayout.NORTH);
        add(panelBawah, BorderLayout.CENTER);

        muatComboBox();
        aturStatusForm(false);
    }

    private void muatComboBox() {
        try {
            cbPasien.removeAllItems();
            for (Pasien p : pasienDAO.tampilkanSemua()) cbPasien.addItem(p);

            cbDokter.removeAllItems();
            for (Dokter d : dokterDAO.tampilkanSemua()) cbDokter.addItem(d);

            cbObat.removeAllItems();
            for (Obat o : obatDAO.tampilkanSemua()) cbObat.addItem(o);
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void mulaiKunjungan() {
        Pasien p = (Pasien) cbPasien.getSelectedItem();
        Dokter d = (Dokter) cbDokter.getSelectedItem();
        if (p == null || d == null) {
            JOptionPane.showMessageDialog(this, "Data pasien/dokter belum tersedia.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double biaya = Validasi.parseDoubleAman(txtBiayaPeriksa.getText(), 0);

        kunjunganAktif = new Kunjungan(0, p.getIdPasien(), d.getIdDokter(), java.time.LocalDateTime.now(),
                txtKeluhan.getText().trim(), txtDiagnosa.getText().trim(), biaya);
        try {
            kunjunganDAO.tambah(kunjunganAktif); 
            LogUtil.tulisLog("Mulai kunjungan baru untuk pasien: " + p.getNama());
            modelResep.setRowCount(0);
            aturStatusForm(true);
            JOptionPane.showMessageDialog(this, "Kunjungan dimulai. Silakan tambahkan resep obat jika diperlukan lalu simpan transaksi.");
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void tambahResep() {
        if (kunjunganAktif == null) {
            JOptionPane.showMessageDialog(this, "Mulai kunjungan baru terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Obat obat = (Obat) cbObat.getSelectedItem();
        if (obat == null) return;
        int jumlah = Validasi.parseIntAman(txtJumlahObat.getText(), -1);
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah obat tidak valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double subtotal = obat.getHarga() * jumlah;
        ResepObat resep = new ResepObat(0, kunjunganAktif.getIdKunjungan(), obat.getIdObat(), obat.getNamaObat(), jumlah, subtotal);

        try {
            kunjunganDAO.simpanResep(resep);
            modelResep.addRow(new Object[]{obat.getNamaObat(), jumlah, subtotal});
            LogUtil.tulisLog("Tambah resep obat: " + obat.getNamaObat() + " x" + jumlah);
            muatComboBox(); 
        } catch (StokObatException ex) {
            
            JOptionPane.showMessageDialog(this, "Gagal menambah resep: " + ex.getMessage(), "Stok Tidak Cukup", JOptionPane.ERROR_MESSAGE);
        } catch (DataTidakDitemukanException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Data Tidak Ditemukan", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            tampilkanError(ex);
        }
    }

    private void simpanTransaksi() {
        if (kunjunganAktif == null) {
            JOptionPane.showMessageDialog(this, "Belum ada kunjungan aktif.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Transaksi kunjungan berhasil disimpan.\nTotal item resep: " + modelResep.getRowCount());
        LogUtil.tulisLog("Selesai transaksi kunjungan ID " + kunjunganAktif.getIdKunjungan());
        kunjunganAktif = null;
        aturStatusForm(false);
        txtKeluhan.setText("");
        txtDiagnosa.setText("");
        modelResep.setRowCount(0);
    }

    private void aturStatusForm(boolean kunjunganSedangBerjalan) {
        cbPasien.setEnabled(!kunjunganSedangBerjalan);
        cbDokter.setEnabled(!kunjunganSedangBerjalan);
        txtBiayaPeriksa.setEnabled(!kunjunganSedangBerjalan);
    }

    private void tampilkanError(Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

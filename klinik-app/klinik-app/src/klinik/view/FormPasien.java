package klinik.view;

import klinik.dao.PasienDAO;
import klinik.model.Pasien;
import klinik.util.LogUtil;
import klinik.util.Validasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FormPasien extends JFrame {

    private PasienDAO pasienDAO = new PasienDAO();
    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNoRm, txtNama, txtTglLahir, txtAlamat, txtTelp, txtCari;
    private JComboBox<String> cbJenisKelamin;
    private int idTerpilih = 0;

    public FormPasien() {
        setTitle("Data Master Pasien");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---- Panel Form ----
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Form Pasien"));

        txtNoRm = new JTextField();
        txtNama = new JTextField();
        cbJenisKelamin = new JComboBox<>(new String[]{"L", "P"});
        txtTglLahir = new JTextField("yyyy-mm-dd");
        txtAlamat = new JTextField();
        txtTelp = new JTextField();

        form.add(new JLabel("No. RM:")); form.add(txtNoRm);
        form.add(new JLabel("Nama:")); form.add(txtNama);
        form.add(new JLabel("Jenis Kelamin:")); form.add(cbJenisKelamin);
        form.add(new JLabel("Tanggal Lahir:")); form.add(txtTglLahir);
        form.add(new JLabel("Alamat:")); form.add(txtAlamat);
        form.add(new JLabel("No. Telp:")); form.add(txtTelp);

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");

        JPanel panelTombol = new JPanel();
        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBersih);

        JPanel panelKiri = new JPanel(new BorderLayout());
        panelKiri.add(form, BorderLayout.CENTER);
        panelKiri.add(panelTombol, BorderLayout.SOUTH);

        // ---- Panel Tabel + Pencarian ----
        model = new DefaultTableModel(new String[]{"ID", "No RM", "Nama", "JK", "Tgl Lahir", "Alamat", "Telp"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        JPanel panelCari = new JPanel(new BorderLayout(5, 5));
        panelCari.add(new JLabel("Cari (Nama/No RM): "), BorderLayout.WEST);
        panelCari.add(txtCari, BorderLayout.CENTER);
        panelCari.add(btnCari, BorderLayout.EAST);

        JPanel panelKanan = new JPanel(new BorderLayout(5, 5));
        panelKanan.add(panelCari, BorderLayout.NORTH);
        panelKanan.add(scroll, BorderLayout.CENTER);

        add(panelKiri, BorderLayout.WEST);
        add(panelKanan, BorderLayout.CENTER);

        // ---- Event ----
        btnTambah.addActionListener(e -> tambahData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> bersihkanForm());
        btnCari.addActionListener(e -> muatData(txtCari.getText().trim()));

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idTerpilih = (int) model.getValueAt(row, 0);
                txtNoRm.setText((String) model.getValueAt(row, 1));
                txtNama.setText((String) model.getValueAt(row, 2));
                cbJenisKelamin.setSelectedItem(model.getValueAt(row, 3));
                Object tgl = model.getValueAt(row, 4);
                txtTglLahir.setText(tgl != null ? tgl.toString() : "");
                txtAlamat.setText((String) model.getValueAt(row, 5));
                txtTelp.setText((String) model.getValueAt(row, 6));
            }
        });

        muatData("");
    }

    private void muatData(String kataKunci) {
        try {
            model.setRowCount(0);
            List<Pasien> daftar = kataKunci.isEmpty() ? pasienDAO.tampilkanSemua() : pasienDAO.cari(kataKunci);
            for (Pasien p : daftar) {
                model.addRow(new Object[]{
                        p.getIdPasien(), p.getNoRm(), p.getNama(), p.getJenisKelamin(),
                        p.getTanggalLahir(), p.getAlamat(), p.getNoTelp()
                });
            }
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private Pasien ambilInputForm() {
        String noRm = txtNoRm.getText().trim();
        String nama = Validasi.kapitalisasi(txtNama.getText());
        String jk = (String) cbJenisKelamin.getSelectedItem();
        String alamat = txtAlamat.getText().trim();
        String telp = txtTelp.getText().trim();

        if (Validasi.isKosong(noRm) || Validasi.isKosong(nama)) {
            JOptionPane.showMessageDialog(this, "No. RM dan Nama wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validasi.isKosong(telp) && !Validasi.isNoTelpValid(telp)) {
            JOptionPane.showMessageDialog(this, "Format No. Telp tidak valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate tglLahir = null;
        try {
            String teksTgl = txtTglLahir.getText().trim();
            if (!teksTgl.isEmpty() && !teksTgl.equals("yyyy-mm-dd")) {
                tglLahir = LocalDate.parse(teksTgl);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format tanggal lahir salah. Gunakan yyyy-mm-dd.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Pasien(idTerpilih, noRm, nama, jk, tglLahir, alamat, telp);
    }

    private void tambahData() {
        Pasien p = ambilInputForm();
        if (p == null) return;
        try {
            pasienDAO.tambah(p);
            LogUtil.tulisLog("Tambah data pasien: " + p.getNama());
            JOptionPane.showMessageDialog(this, "Data pasien berhasil ditambahkan.");
            bersihkanForm();
            muatData("");
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void ubahData() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pasien p = ambilInputForm();
        if (p == null) return;
        try {
            pasienDAO.ubah(p);
            LogUtil.tulisLog("Ubah data pasien ID " + idTerpilih);
            JOptionPane.showMessageDialog(this, "Data pasien berhasil diubah.");
            bersihkanForm();
            muatData("");
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void hapusData() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                pasienDAO.hapus(idTerpilih);
                LogUtil.tulisLog("Hapus data pasien ID " + idTerpilih);
                JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus.");
                bersihkanForm();
                muatData("");
            } catch (SQLException e) {
                tampilkanError(e);
            }
        }
    }

    private void bersihkanForm() {
        idTerpilih = 0;
        txtNoRm.setText("");
        txtNama.setText("");
        txtTglLahir.setText("yyyy-mm-dd");
        txtAlamat.setText("");
        txtTelp.setText("");
        table.clearSelection();
    }

    private void tampilkanError(Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

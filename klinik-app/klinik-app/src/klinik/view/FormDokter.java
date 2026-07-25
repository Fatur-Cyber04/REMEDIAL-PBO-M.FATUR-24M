package klinik.view;

import klinik.dao.DokterDAO;
import klinik.model.Dokter;
import klinik.util.LogUtil;
import klinik.util.Validasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FormDokter extends JFrame {

    private DokterDAO dokterDAO = new DokterDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtSpesialisasi, txtTelp, txtCari;
    private int idTerpilih = 0;

    public FormDokter() {
        setTitle("Data Master Dokter");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Form Dokter"));
        txtNama = new JTextField();
        txtSpesialisasi = new JTextField();
        txtTelp = new JTextField();
        form.add(new JLabel("Nama:")); form.add(txtNama);
        form.add(new JLabel("Spesialisasi:")); form.add(txtSpesialisasi);
        form.add(new JLabel("No. Telp:")); form.add(txtTelp);

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");
        JPanel panelTombol = new JPanel();
        panelTombol.add(btnTambah); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnBersih);

        JPanel panelKiri = new JPanel(new BorderLayout());
        panelKiri.add(form, BorderLayout.CENTER);
        panelKiri.add(panelTombol, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Spesialisasi", "Telp"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        JPanel panelCari = new JPanel(new BorderLayout(5, 5));
        panelCari.add(new JLabel("Cari (Nama/Spesialisasi): "), BorderLayout.WEST);
        panelCari.add(txtCari, BorderLayout.CENTER);
        panelCari.add(btnCari, BorderLayout.EAST);

        JPanel panelKanan = new JPanel(new BorderLayout(5, 5));
        panelKanan.add(panelCari, BorderLayout.NORTH);
        panelKanan.add(scroll, BorderLayout.CENTER);

        add(panelKiri, BorderLayout.WEST);
        add(panelKanan, BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> bersihkanForm());
        btnCari.addActionListener(e -> muatData(txtCari.getText().trim()));

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idTerpilih = (int) model.getValueAt(row, 0);
                txtNama.setText((String) model.getValueAt(row, 1));
                txtSpesialisasi.setText((String) model.getValueAt(row, 2));
                txtTelp.setText((String) model.getValueAt(row, 3));
            }
        });

        muatData("");
    }

    private void muatData(String kataKunci) {
        try {
            model.setRowCount(0);
            List<Dokter> daftar = kataKunci.isEmpty() ? dokterDAO.tampilkanSemua() : dokterDAO.cari(kataKunci);
            for (Dokter d : daftar) {
                model.addRow(new Object[]{d.getIdDokter(), d.getNama(), d.getSpesialisasi(), d.getNoTelp()});
            }
        } catch (SQLException e) { tampilkanError(e); }
    }

    private Dokter ambilInputForm() {
        String nama = Validasi.kapitalisasi(txtNama.getText());
        String spesialisasi = txtSpesialisasi.getText().trim();
        String telp = txtTelp.getText().trim();
        if (Validasi.isKosong(nama)) {
            JOptionPane.showMessageDialog(this, "Nama wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Dokter(idTerpilih, nama, spesialisasi, telp);
    }

    private void tambahData() {
        Dokter d = ambilInputForm();
        if (d == null) return;
        try {
            dokterDAO.tambah(d);
            LogUtil.tulisLog("Tambah data dokter: " + d.getNama());
            JOptionPane.showMessageDialog(this, "Data dokter berhasil ditambahkan.");
            bersihkanForm(); muatData("");
        } catch (SQLException e) { tampilkanError(e); }
    }

    private void ubahData() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Dokter d = ambilInputForm();
        if (d == null) return;
        try {
            dokterDAO.ubah(d);
            LogUtil.tulisLog("Ubah data dokter ID " + idTerpilih);
            JOptionPane.showMessageDialog(this, "Data dokter berhasil diubah.");
            bersihkanForm(); muatData("");
        } catch (SQLException e) { tampilkanError(e); }
    }

    private void hapusData() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                dokterDAO.hapus(idTerpilih);
                LogUtil.tulisLog("Hapus data dokter ID " + idTerpilih);
                JOptionPane.showMessageDialog(this, "Data dokter berhasil dihapus.");
                bersihkanForm(); muatData("");
            } catch (SQLException e) { tampilkanError(e); }
        }
    }

    private void bersihkanForm() {
        idTerpilih = 0;
        txtNama.setText(""); txtSpesialisasi.setText(""); txtTelp.setText("");
        table.clearSelection();
    }

    private void tampilkanError(Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

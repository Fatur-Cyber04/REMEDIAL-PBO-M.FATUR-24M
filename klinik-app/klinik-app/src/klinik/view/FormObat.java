package klinik.view;

import klinik.dao.ObatDAO;
import klinik.model.Obat;
import klinik.util.LogUtil;
import klinik.util.Validasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FormObat extends JFrame {

    private ObatDAO obatDAO = new ObatDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtSatuan, txtHarga, txtStok, txtCari;
    private int idTerpilih = 0;

    public FormObat() {
        setTitle("Data Master Obat");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Form Obat"));
        txtNama = new JTextField();
        txtSatuan = new JTextField();
        txtHarga = new JTextField();
        txtStok = new JTextField();
        form.add(new JLabel("Nama Obat:")); form.add(txtNama);
        form.add(new JLabel("Satuan:")); form.add(txtSatuan);
        form.add(new JLabel("Harga:")); form.add(txtHarga);
        form.add(new JLabel("Stok:")); form.add(txtStok);

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");
        JPanel panelTombol = new JPanel();
        panelTombol.add(btnTambah); panelTombol.add(btnUbah); panelTombol.add(btnHapus); panelTombol.add(btnBersih);

        JPanel panelKiri = new JPanel(new BorderLayout());
        panelKiri.add(form, BorderLayout.CENTER);
        panelKiri.add(panelTombol, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"ID", "Nama Obat", "Satuan", "Harga", "Stok"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        JPanel panelCari = new JPanel(new BorderLayout(5, 5));
        panelCari.add(new JLabel("Cari (Nama Obat): "), BorderLayout.WEST);
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
                txtSatuan.setText((String) model.getValueAt(row, 2));
                txtHarga.setText(String.valueOf(model.getValueAt(row, 3)));
                txtStok.setText(String.valueOf(model.getValueAt(row, 4)));
            }
        });

        muatData("");
    }

    private void muatData(String kataKunci) {
        try {
            model.setRowCount(0);
            List<Obat> daftar = kataKunci.isEmpty() ? obatDAO.tampilkanSemua() : obatDAO.cari(kataKunci);
            for (Obat o : daftar) {
                model.addRow(new Object[]{o.getIdObat(), o.getNamaObat(), o.getSatuan(), o.getHarga(), o.getStok()});
            }
        } catch (SQLException e) { tampilkanError(e); }
    }

    private Obat ambilInputForm() {
        String nama = Validasi.kapitalisasi(txtNama.getText());
        String satuan = txtSatuan.getText().trim();
        if (Validasi.isKosong(nama)) {
            JOptionPane.showMessageDialog(this, "Nama obat wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        double harga = Validasi.parseDoubleAman(txtHarga.getText(), -1);
        int stok = Validasi.parseIntAman(txtStok.getText(), -1);
        if (harga < 0 || stok < 0) {
            JOptionPane.showMessageDialog(this, "Harga/Stok harus berupa angka valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Obat(idTerpilih, nama, satuan, harga, stok);
    }

    private void tambahData() {
        Obat o = ambilInputForm();
        if (o == null) return;
        try {
            obatDAO.tambah(o);
            LogUtil.tulisLog("Tambah data obat: " + o.getNamaObat());
            JOptionPane.showMessageDialog(this, "Data obat berhasil ditambahkan.");
            bersihkanForm(); muatData("");
        } catch (SQLException e) { tampilkanError(e); }
    }

    private void ubahData() {
        if (idTerpilih == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Obat o = ambilInputForm();
        if (o == null) return;
        try {
            obatDAO.ubah(o);
            LogUtil.tulisLog("Ubah data obat ID " + idTerpilih);
            JOptionPane.showMessageDialog(this, "Data obat berhasil diubah.");
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
                obatDAO.hapus(idTerpilih);
                LogUtil.tulisLog("Hapus data obat ID " + idTerpilih);
                JOptionPane.showMessageDialog(this, "Data obat berhasil dihapus.");
                bersihkanForm(); muatData("");
            } catch (SQLException e) { tampilkanError(e); }
        }
    }

    private void bersihkanForm() {
        idTerpilih = 0;
        txtNama.setText(""); txtSatuan.setText(""); txtHarga.setText(""); txtStok.setText("");
        table.clearSelection();
    }

    private void tampilkanError(Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

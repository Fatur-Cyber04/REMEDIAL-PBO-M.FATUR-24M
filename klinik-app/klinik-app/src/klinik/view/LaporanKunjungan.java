package klinik.view;

import klinik.dao.KunjunganDAO;
import klinik.model.Kunjungan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LaporanKunjungan extends JFrame {

    private KunjunganDAO kunjunganDAO = new KunjunganDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtCari;
    private JLabel lblTotal;

    public LaporanKunjungan() {
        setTitle("Laporan Kunjungan");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari (Pasien/Dokter)");
        JButton btnRefresh = new JButton("Tampilkan Semua");
        JPanel panelAtas = new JPanel(new BorderLayout(5, 5));
        panelAtas.add(txtCari, BorderLayout.CENTER);
        JPanel panelTombolAtas = new JPanel();
        panelTombolAtas.add(btnCari);
        panelTombolAtas.add(btnRefresh);
        panelAtas.add(panelTombolAtas, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "Tanggal", "Pasien", "Dokter", "Keluhan", "Diagnosa", "Total Biaya"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        lblTotal = new JLabel("Total keseluruhan: Rp 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 13));

        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);

        btnCari.addActionListener(e -> muatData(txtCari.getText().trim()));
        btnRefresh.addActionListener(e -> muatData(""));

        muatData("");
    }

    private void muatData(String kataKunci) {
        try {
            model.setRowCount(0);
            List<Kunjungan> daftar = kataKunci.isEmpty() ? kunjunganDAO.tampilkanSemua() : kunjunganDAO.cari(kataKunci);
            double totalKeseluruhan = 0;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            for (Kunjungan k : daftar) {
                model.addRow(new Object[]{
                        k.getIdKunjungan(),
                        k.getTanggalKunjungan().format(fmt),
                        k.getNamaPasien(),
                        k.getNamaDokter(),
                        k.getKeluhan(),
                        k.getDiagnosa(),
                        k.getBiayaPeriksa()
                });
                totalKeseluruhan += k.getBiayaPeriksa();
            }
            lblTotal.setText(String.format("Total keseluruhan (biaya periksa): Rp %,.0f", totalKeseluruhan));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

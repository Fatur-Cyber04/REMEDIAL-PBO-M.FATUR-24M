package klinik.view;

import klinik.model.User;
import klinik.util.LogUtil;

import javax.swing.*;
import java.awt.*;

public class MenuUtama extends JFrame {

    private User userLogin;

    public MenuUtama(User user) {
        this.userLogin = user;

        setTitle("Menu Utama - Sistem Klinik Sederhana");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblUser = new JLabel("Login sebagai: " + user.getNamaLengkap() + " (" + user.getRole() + ")", SwingConstants.CENTER);
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));

        JButton btnPasien = new JButton("Data Master Pasien");
        JButton btnDokter = new JButton("Data Master Dokter");
        JButton btnObat = new JButton("Data Master Obat");
        JButton btnKunjungan = new JButton("Transaksi Kunjungan / Pemeriksaan");
        JButton btnLaporan = new JButton("Laporan Kunjungan");
        JButton btnLogout = new JButton("Logout");

        btnPasien.addActionListener(e -> new FormPasien().setVisible(true));
        btnDokter.addActionListener(e -> new FormDokter().setVisible(true));
        btnObat.addActionListener(e -> new FormObat().setVisible(true));
        btnKunjungan.addActionListener(e -> new FormKunjungan().setVisible(true));
        btnLaporan.addActionListener(e -> new LaporanKunjungan().setVisible(true));
        btnLogout.addActionListener(e -> {
            LogUtil.tulisLog("Logout: " + userLogin.getUsername());
            dispose();
            new LoginForm().setVisible(true);
        });

        panel.add(lblUser);
        panel.add(btnPasien);
        panel.add(btnDokter);
        panel.add(btnObat);
        panel.add(btnKunjungan);
        panel.add(btnLaporan);
        panel.add(btnLogout);

        add(panel);
    }
}

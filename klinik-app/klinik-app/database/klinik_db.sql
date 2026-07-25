CREATE DATABASE IF NOT EXISTS klinik_db;
USE klinik_db;

CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    role ENUM('admin','petugas') NOT NULL DEFAULT 'petugas'
);

CREATE TABLE pasien (
    id_pasien INT AUTO_INCREMENT PRIMARY KEY,
    no_rm VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    jenis_kelamin ENUM('L','P') NOT NULL,
    tanggal_lahir DATE,
    alamat VARCHAR(255),
    no_telp VARCHAR(20)
);

CREATE TABLE dokter (
    id_dokter INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    spesialisasi VARCHAR(100),
    no_telp VARCHAR(20)
);

CREATE TABLE obat (
    id_obat INT AUTO_INCREMENT PRIMARY KEY,
    nama_obat VARCHAR(100) NOT NULL,
    satuan VARCHAR(20),
    harga DECIMAL(10,2) NOT NULL DEFAULT 0,
    stok INT NOT NULL DEFAULT 0
);

CREATE TABLE kunjungan (
    id_kunjungan INT AUTO_INCREMENT PRIMARY KEY,
    id_pasien INT NOT NULL,
    id_dokter INT NOT NULL,
    tanggal_kunjungan DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    keluhan TEXT,
    diagnosa TEXT,
    biaya_periksa DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_pasien) REFERENCES pasien(id_pasien) ON DELETE CASCADE,
    FOREIGN KEY (id_dokter) REFERENCES dokter(id_dokter) ON DELETE CASCADE
);

CREATE TABLE resep_obat (
    id_resep INT AUTO_INCREMENT PRIMARY KEY,
    id_kunjungan INT NOT NULL,
    id_obat INT NOT NULL,
    jumlah INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_kunjungan) REFERENCES kunjungan(id_kunjungan) ON DELETE CASCADE,
    FOREIGN KEY (id_obat) REFERENCES obat(id_obat)
);

INSERT INTO users (username, password, nama_lengkap, role) VALUES
('admin', 'admin123', 'Administrator', 'admin'),
('petugas1', 'petugas123', 'Siti Petugas', 'petugas');

INSERT INTO pasien (no_rm, nama, jenis_kelamin, tanggal_lahir, alamat, no_telp) VALUES
('RM0001', 'Andi Saputra', 'L', '1995-05-10', 'Jl. Merdeka No. 1', '081234567890'),
('RM0002', 'Rina Wulandari', 'P', '1998-08-21', 'Jl. Sudirman No. 5', '081298765432');

INSERT INTO dokter (nama, spesialisasi, no_telp) VALUES
('dr. Budi Santoso', 'Umum', '081211112222'),
('dr. Maya Kartika', 'Anak', '081233334444');

INSERT INTO obat (nama_obat, satuan, harga, stok) VALUES
('Paracetamol 500mg', 'Strip', 5000, 100),
('Amoxicillin 500mg', 'Strip', 8000, 50),
('Vitamin C', 'Botol', 15000, 30);

package klinik.dao;

import java.sql.SQLException;
import java.util.List;

public interface ICrud<T> {
    void tambah(T data) throws SQLException;
    void ubah(T data) throws SQLException;
    void hapus(int id) throws SQLException;
    List<T> tampilkanSemua() throws SQLException;
    List<T> cari(String kataKunci) throws SQLException;
}

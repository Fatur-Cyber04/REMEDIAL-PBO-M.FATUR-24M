package klinik.dao;

import klinik.model.User;
import klinik.util.KoneksiDatabase;

import java.sql.*;

public class UserDAO {

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = KoneksiDatabase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("nama_lengkap"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}

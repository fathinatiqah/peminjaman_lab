package backend;

import java.sql.*;
import java.util.ArrayList;

public class Kategori {
    
    private int idKategori;
    private String namaKategori;

    // ===== CONSTRUCTOR =====
    public Kategori() {}

    public Kategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    // ==================================================================
    //                          GET ALL
    // ==================================================================
    public static ArrayList<Kategori> getAll() {
        return getAll("");
    }

    public static ArrayList<Kategori> getAll(String keyword) {
        ArrayList<Kategori> listKategori = new ArrayList<>();

        String sql = "SELECT * FROM kategori WHERE nama_kategori LIKE ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kategori k = new Kategori();
                    k.setIdKategori(rs.getInt("id_kategori"));
                    k.setNamaKategori(rs.getString("nama_kategori"));
                    listKategori.add(k);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listKategori;
    }

    // ==================================================================
    //                          GET BY ID
    // ==================================================================
    public static Kategori getById(int id) {
        Kategori kat = null;
        String sql = "SELECT * FROM kategori WHERE id_kategori = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kat = new Kategori();
                    kat.setIdKategori(rs.getInt("id_kategori"));
                    kat.setNamaKategori(rs.getString("nama_kategori"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kat;
    }

    // ==================================================================
    //                          INSERT / UPDATE
    // ==================================================================
    public void save() {

        // INSERT
        if (this.idKategori == 0) {

            String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";

            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, this.namaKategori);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.idKategori = rs.getInt(1);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } 
        // UPDATE
        else {

            String sql = "UPDATE kategori SET nama_kategori = ? WHERE id_kategori = ?";

            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, this.namaKategori);
                ps.setInt(2, this.idKategori);
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================================================================
    //                          DELETE
    // ==================================================================
    public static boolean delete(int idKategori) {

        String sql = "DELETE FROM kategori";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idKategori);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    // ==================================================================
    //                     GETTER & SETTER
    // ==================================================================
    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
}

package backend;

/**
 *
 * @author Fathzyya_
 */
import java.sql.*;
import java.util.ArrayList;

public class Mahasiswa {
    
    private int idMahasiswa;
    private String nim;
    private String nama;
    private String prodi;
    private String noHp;
    
    // ===== CONSTRUCTOR =====
    public Mahasiswa() {
    }

    public Mahasiswa(String nim, String nama, String prodi, String noHp) {
        this.nim = nim;
        this.nama = nama;
        this.prodi = prodi;
        this.noHp = noHp;
    }
    
    // ==================================================================
    //                          GET ALL
    // ==================================================================
    public static ArrayList<Mahasiswa> getAll() {
        return getAll("");
    }

    public static ArrayList<Mahasiswa> getAll(String keyword) {
        ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();

        // Mencari berdasarkan NIM atau Nama Mahasiswa
        String sql = "SELECT * FROM mahasiswa WHERE nim LIKE CONCAT('%', ?, '%') OR nama LIKE CONCAT('%', ?, '%')";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Parameter 1 dan 2 menggunakan keyword yang sama
            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mahasiswa mhs = new Mahasiswa();
                    mhs.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                    mhs.setNim(rs.getString("nim"));
                    mhs.setNama(rs.getString("nama"));
                    mhs.setProdi(rs.getString("prodi"));
                    mhs.setNoHp(rs.getString("no_hp"));
                    listMahasiswa.add(mhs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listMahasiswa;
    }
    // ==================================================================
    //                         GET BY ID
    // ==================================================================
    public static Mahasiswa getById(int id) {
        Mahasiswa mhs = null;
        String sql = "SELECT * FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mhs = new Mahasiswa();
                    mhs.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                    mhs.setNim(rs.getString("nim"));
                    mhs.setNama(rs.getString("nama"));
                    mhs.setProdi(rs.getString("prodi"));
                    mhs.setNoHp(rs.getString("no_hp"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mhs;
    }
    // ==================================================================
    //                         INSERT / UPDATE (Menyimpan Data)
    // ==================================================================
    public void save() {

        // INSERT
        if (this.idMahasiswa == 0) {

            String sql = "INSERT INTO mahasiswa (nim, nama, prodi, no_hp) VALUES (?, ?, ?, ?)";

            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, this.nim);
                ps.setString(2, this.nama);
                ps.setString(3, this.prodi);
                ps.setString(4, this.noHp);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.idMahasiswa = rs.getInt(1);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } 
        // UPDATE
        else {

            String sql = "UPDATE mahasiswa SET nim = ?, nama = ?, prodi = ?, no_hp = ? WHERE id_mahasiswa = ?";

            try (Connection conn = DBHelper.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, this.nim);
                ps.setString(2, this.nama);
                ps.setString(3, this.prodi);
                ps.setString(4, this.noHp);
                ps.setInt(5, this.idMahasiswa);
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================================================================
    //                         DELETE (Menghapus Data)
    // ==================================================================
    public static boolean delete(int idMahasiswa) {

        String sql = "DELETE FROM mahasiswa WHERE id_mahasiswa = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMahasiswa);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================================================================
    //                      GETTER & SETTER
    // ==================================================================
    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
    
    @Override
    public String toString() {
        return this.nama;
    }
}

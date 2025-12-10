package backend;

/**
 *
 * @author Fathzyya_
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Peminjaman {
    private int idPeminjaman;
    private Mahasiswa mahasiswa; // Foreign Key ke tabel mahasiswa
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status; // enum('Dipinjam', 'Dikembalikan', 'Terlambat')

    // ===== CONSTRUCTOR =====
    public Peminjaman() {
        this.mahasiswa = new Mahasiswa();
    }

    public Peminjaman(Mahasiswa mahasiswa, Date tanggalPinjam, Date tanggalKembali, String status) {
        this.mahasiswa = mahasiswa;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
    }

    // ==================================================================
    //                         GET ALL
    // ==================================================================
    // Helper method untuk mengisi objek Peminjaman dari ResultSet
    private static Peminjaman populatePeminjaman(ResultSet rs) throws SQLException {
        Peminjaman p = new Peminjaman();
        p.setIdPeminjaman(rs.getInt("id_peminjaman"));
        
        // Ambil data Mahasiswa (FK)
        int idMahasiswa = rs.getInt("id_mahasiswa");
        // Anda perlu method getById(int) di kelas Mahasiswa untuk ini
        p.setMahasiswa(Mahasiswa.getById(idMahasiswa)); 
        
        p.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
        p.setTanggalKembali(rs.getDate("tanggal_kembali"));
        p.setStatus(rs.getString("status"));
        return p;
    }
    
    public static ArrayList<Peminjaman> getAll() {
        return getAll("");
    }

    public static ArrayList<Peminjaman> getAll(String keyword) {
        ArrayList<Peminjaman> listPeminjaman = new ArrayList<>();
        // Query untuk mencari berdasarkan ID Peminjaman atau NIM Mahasiswa
        String sql = "SELECT p.* FROM peminjaman p JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa WHERE p.id_peminjaman LIKE CONCAT('%', ?, '%') OR m.nim LIKE CONCAT('%', ?, '%') ORDER BY p.tanggal_pinjam DESC";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listPeminjaman.add(populatePeminjaman(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPeminjaman;
    }

    // ==================================================================
    //                         GET BY ID
    // ==================================================================
    public static Peminjaman getById(int id) {
        Peminjaman p = null;
        String sql = "SELECT * FROM peminjaman WHERE id_peminjaman = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = populatePeminjaman(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // ==================================================================
    //                         INSERT / UPDATE
    // ==================================================================
    public void save() {

        if (this.idPeminjaman == 0) { // INSERT

            String sql = "INSERT INTO peminjaman (id_mahasiswa, tanggal_pinjam, tanggal_kembali, status) VALUES (?, ?, ?, ?)";

            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, this.mahasiswa.getIdMahasiswa());
                ps.setDate(2, new java.sql.Date(this.tanggalPinjam.getTime()));
                ps.setDate(3, new java.sql.Date(this.tanggalKembali.getTime()));
                ps.setString(4, this.status);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.idPeminjaman = rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else { // UPDATE

            String sql = "UPDATE peminjaman SET id_mahasiswa = ?, tanggal_pinjam = ?, tanggal_kembali = ?, status = ? WHERE id_peminjaman = ?";

            try (Connection conn = DBHelper.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, this.mahasiswa.getIdMahasiswa());
                ps.setDate(2, new java.sql.Date(this.tanggalPinjam.getTime()));
                ps.setDate(3, new java.sql.Date(this.tanggalKembali.getTime()));
                ps.setString(4, this.status);
                ps.setInt(5, this.idPeminjaman);
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================================================================
    //                         DELETE
    // ==================================================================
    public static boolean delete(int idPeminjaman) {
        String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPeminjaman);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gagal (Pasti karena Foreign Key ke detail_peminjaman)
        }
    }

    // ==================================================================
    //                      GETTER & SETTER
    // ==================================================================
    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

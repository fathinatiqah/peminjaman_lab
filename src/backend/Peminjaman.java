package backend;

/**
 *
 * @author Fathzyya_
 */
import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;

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
    public static ArrayList<Peminjaman> getAll() {
        return getAll("");
    }

    public static ArrayList<Peminjaman> getAll(String keyword) {
    ArrayList<Peminjaman> listPeminjaman = new ArrayList<>();

    // Perhatikan bagian id_peminjaman (sesuaikan dengan database Anda)
    String sql = "SELECT p.id_peminjaman, p.tanggal_pinjam, p.tanggal_kembali, p.status, " // Pastikan nama kolom benar
               + "m.id_mahasiswa, m.nama AS nama_mahasiswa " 
               + "FROM peminjaman p "
               + "LEFT JOIN mahasiswa m ON p.id_mahasiswa = m.id_mahasiswa "
               + "WHERE m.nama LIKE CONCAT('%', ?, '%') " 
               + "OR p.id_peminjaman LIKE CONCAT('%', ?, '%')";

    try (Connection conn = DBHelper.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, keyword); 
        ps.setString(2, keyword); 

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Peminjaman p = new Peminjaman();
                
                Mahasiswa mhs = new Mahasiswa();
                mhs.setIdMahasiswa(rs.getInt("id_mahasiswa")); // Sesuaikan nama kolom RS
                mhs.setNama(rs.getString("nama_mahasiswa"));
                p.setMahasiswa(mhs);

                // Ambil data menggunakan nama kolom yang ada di database
                p.setIdPeminjaman(rs.getInt("id_peminjaman")); 
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam")); 
                p.setTanggalKembali(rs.getDate("tanggal_kembali"));
                p.setStatus(rs.getString("status"));

                listPeminjaman.add(p);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return listPeminjaman;
}

    // ================================
    //           GET BY ID
    // ================================
    public static Peminjaman getById(int id) {
        Peminjaman peminjaman = null;

        String sql = "SELECT * FROM peminjaman WHERE id_peminjaman = ?";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    peminjaman = new Peminjaman();

                    peminjaman.setIdPeminjaman(rs.getInt("id_peminjaman"));
                    peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                    peminjaman.setTanggalKembali(rs.getDate("tanggal_kembali"));
                    peminjaman.setStatus(rs.getString("status"));

                    // Foreign Key Mahasiswa
                    int idMahasiswa = rs.getInt("id_mahasiswa");
                    Mahasiswa mhs = Mahasiswa.getById(idMahasiswa);
                    peminjaman.setMahasiswa(mhs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return peminjaman;
    }

    // ================================
    //        SAVE (Insert/Update)
    // ================================
    public void save() {
        // INSERT
        if (this.idPeminjaman == 0) {

            String sql = "INSERT INTO peminjaman (id_mahasiswa, tanggal_pinjam, tanggal_kembali, status) "
                    + "VALUES (?, ?, ?, ?)";

            try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, this.mahasiswa.getIdMahasiswa());
                ps.setDate(2, this.tanggalPinjam);
                ps.setDate(3, this.tanggalKembali);
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

        } else {
            // UPDATE

            String sql = "UPDATE peminjaman SET id_mahasiswa = ?, tanggal_pinjam = ?, "
                    + "tanggal_kembali = ?, status = ? WHERE id_peminjaman = ?";

            try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, this.mahasiswa.getIdMahasiswa());
                ps.setDate(2, this.tanggalPinjam);
                ps.setDate(3, this.tanggalKembali);
                ps.setString(4, this.status);
                ps.setInt(5, this.idPeminjaman);

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ================================
    //             DELETE
    // ================================
    public static boolean delete(int idPeminjaman) {

        String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";

        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPeminjaman);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================================
    //        GETTER & SETTER
    // ================================
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

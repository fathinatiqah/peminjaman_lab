package backend;

/**
 *
 * @author Fathzyya_
 */
import java.sql.*;
import java.util.ArrayList;

public class AlatLab {
    private int idAlatLab;
    private Kategori kategori;   // FK sebagai object
    private String namaAlat;
    private int stok;
    private String kondisi;

    // ================================
    //         CONSTRUCTOR
    // ================================
    public AlatLab() {}

    public AlatLab(Kategori kategori, String namaAlat, int stok, String kondisi) {
        this.kategori = kategori;
        this.namaAlat = namaAlat;
        this.stok = stok;
        this.kondisi = kondisi;
    }

    // ================================
    //            GET ALL
    // ================================
    public static ArrayList<AlatLab> getAll() {
        return getAll("");
    }

    public static ArrayList<AlatLab> getAll(String keyword) {
        ArrayList<AlatLab> listAlat = new ArrayList<>();

        String sql = "SELECT * FROM alat_lab WHERE nama_alat LIKE CONCAT('%', ?, '%') "
                   + "OR id_alat_lab LIKE CONCAT('%', ?, '%')";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AlatLab alat = new AlatLab();

                    alat.setIdAlatLab(rs.getInt("id_alat_lab"));
                    alat.setNamaAlat(rs.getString("nama_alat"));
                    alat.setStok(rs.getInt("stok"));
                    alat.setKondisi(rs.getString("kondisi"));

                    // Ambil kategori melalui FK
                    int idKategori = rs.getInt("id_kategori");
                    Kategori kat = Kategori.getById(idKategori);
                    alat.setKategori(kat);

                    listAlat.add(alat);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listAlat;
    }

    // ================================
    //           GET BY ID
    // ================================
    public static AlatLab getById(int id) {
        AlatLab alat = null;

        String sql = "SELECT * FROM alat_lab WHERE id_alat_lab = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    alat = new AlatLab();

                    alat.setIdAlatLab(rs.getInt("id_alat_lab"));
                    alat.setNamaAlat(rs.getString("nama_alat"));
                    alat.setStok(rs.getInt("stok"));
                    alat.setKondisi(rs.getString("kondisi"));

                    // FK kategori
                    int idKategori = rs.getInt("id_kategori");
                    Kategori kat = Kategori.getById(idKategori);
                    alat.setKategori(kat);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return alat;
    }

    // ================================
    //            SAVE (Insert/Update)
    // ================================
    public void save() {

        // INSERT
        if (this.idAlatLab == 0) {

            String sql = "INSERT INTO alat_lab (id_kategori, nama_alat, stok, kondisi) "
                       + "VALUES (?, ?, ?, ?)";

            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, this.kategori.getIdKategori());
                ps.setString(2, this.namaAlat);
                ps.setInt(3, this.stok);
                ps.setString(4, this.kondisi);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.idAlatLab = rs.getInt(1);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
        // UPDATE

            String sql = "UPDATE alat_lab SET id_kategori = ?, nama_alat = ?, stok = ?, kondisi = ? "
                       + "WHERE id_alat_lab = ?";

            try (Connection conn = DBHelper.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, this.kategori.getIdKategori());
                ps.setString(2, this.namaAlat);
                ps.setInt(3, this.stok);
                ps.setString(4, this.kondisi);
                ps.setInt(5, this.idAlatLab);

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ================================
    //             DELETE
    // ================================
    public static boolean delete(int idAlatLab) {

        String sql = "DELETE FROM alat_lab WHERE id_alat_lab = ?";

        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlatLab);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================================
    //      GETTER & SETTER
    // ================================
    public int getIdAlatLab() {
        return idAlatLab;
    }

    public void setIdAlatLab(int idAlatLab) {
        this.idAlatLab = idAlatLab;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }
}

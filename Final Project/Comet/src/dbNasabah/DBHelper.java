package dbNasabah;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper {

    private static final String SQCONN = "jdbc:sqlite:HoloMyth.sqlite";

    public static Connection getConnection(String driver) throws SQLException {
        Connection conn = null;
        switch (driver) {
            case "SQLITE": {
                try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection(SQCONN);
                    createTable(conn, driver);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Library Kosong");
                    Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        return conn;
    }

    public static void createTable(Connection conn, String driver) throws SQLException {
        String sqlCreate = "";
        switch (driver) {
            case "SQLITE": {
                sqlCreate = "CREATE TABLE IF NOT EXISTS nasabah ("
                        + "    nasabah_id INT (10)      PRIMARY KEY,"
                        + "    nama       VARCHAR (100),"
                        + "    alamat     VARCHAR (100) "
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS rekening ("
                        + "    no_rekening INT (10)       PRIMARY KEY,"
                        + "    saldo       DOUBLE (16, 2),"
                        + "    nasabah_id  INT (10)       REFERENCES nasabah (nasabah_id) ON DELETE RESTRICT"
                        + "                                                               ON UPDATE CASCADE"
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS individu ("
                        + "    nasabah_id INT (10)     PRIMARY KEY"
                        + "                            REFERENCES nasabah (nasabah_id) ON DELETE RESTRICT"
                        + "                                                            ON UPDATE CASCADE,"
                        + "    nik        VARCHAR (20),"
                        + "    npwp       VARCHAR (20) "
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS perusahaan ("
                        + "    nasabah_id INT (10)     PRIMARY KEY"
                        + "                            REFERENCES nasabah (nasabah_id) ON DELETE RESTRICT"
                        + "                                                            ON UPDATE CASCADE,"
                        + "    nib        VARCHAR (20) "
                        + ");";
                break;
            }
        }
        String sqls[] = sqlCreate.split(";");
        for (String sql : sqls) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        }
    }
}

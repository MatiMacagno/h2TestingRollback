import java.sql.*;

public class Test {

    private static final String SQL_CREATE_TABLE = "DROP TABLE IF EXISTS USUARIO; CREATE TABLE USUARIO" +
            "("
            + " ID INT PRIMARY KEY, "
            + " NOMBRE VARCHAR(100) NOT NULL, "
            + " EMAIL VARCHAR(100) NOT NULL, "
            + " SUELDO numeric(15, 2) NOT NULL"
            + ")";

    private static final String SQL_INSERT = "INSERT INTO USUARIO (ID, NOMBRE, EMAIL, SUELDO) VALUES (?, ?, ?, ?) ";
    private static final String SQL_UPDATE = "UPDATE USUARIO SET SUELDO=? WHERE EMAIL=?";

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:h2:~/test4", "sa", "");
    }

    public static void main(String[] args) throws Exception {

        Usuario usuario1 = new Usuario("Matias", "matiasmacagno10@gmail.com", 10000d);

        Connection connection = null;

        try {

            connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(SQL_CREATE_TABLE);

            PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);

            // Empiezo a insertar en la BD.

            psInsert.setInt(1, 1);
            psInsert.setString(2, usuario1.getNombre());
            psInsert.setString(3, usuario1.getEmail());
            psInsert.setDouble(4, usuario1.getSueldo());

            psInsert.execute();

            //Empezamos la transaccion.

            connection.setAutoCommit(false);

            PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);
            psUpdate.setDouble(1, usuario1.subirSueldo(500d));
            psUpdate.setString(2, usuario1.getEmail());
            psUpdate.execute();

            int a = 4 / 0;

            connection.commit();



            connection.setAutoCommit(true);

            String sql = "SELECT * FROM USUARIO";
            Statement stmt2 = connection.createStatement();
            ResultSet rs = stmt2.executeQuery(sql);
            while (rs.next()){
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDouble(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.close();
        }

        Connection connection1 = getConnection();
        String sql = "SELECT * FROM USUARIO";
        Statement stmt2 = connection1.createStatement();
        ResultSet rs = stmt2.executeQuery(sql);
        while (rs.next()){
            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDouble(4));
        }


    }
}

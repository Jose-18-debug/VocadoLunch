package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
    public static Connection conectar() {
        Connection conexion = null;
        String url = "jdbc:mysql://localhost:3306/vocado_lunch_db ";
        String usuario = "root";
        String password = "moises1234";
        try {
            conexion = DriverManager.getConnection(
                    url, usuario, password
            );
            System.out.println("Conexion exitosa");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conexion;
    }
}

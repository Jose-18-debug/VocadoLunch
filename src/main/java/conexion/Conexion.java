package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza la conexión JDBC.
 * Configura VOCADO_DB_USER y VOCADO_DB_PASSWORD como variables de entorno
 * o reemplaza los valores predeterminados por los de TU instalación local.
 */
public class Conexion {
    private static final String URL =
            System.getenv().getOrDefault("VOCADO_DB_URL",
                    "jdbc:mysql://localhost:3306/vocado_lunch_db?serverTimezone=America/Mexico_City&useSSL=false&allowPublicKeyRetrieval=true");
    private static final String USUARIO =
            System.getenv().getOrDefault("VOCADO_DB_USER", "root");
    private static final String PASSWORD =
            System.getenv().getOrDefault("VOCADO_DB_PASSWORD", "");

    private Conexion() {}

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}

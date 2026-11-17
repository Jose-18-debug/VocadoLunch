package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
    private static final String URL_PREDETERMINADA =
            "jdbc:mysql://localhost:3306/vocado_lunch_db" + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" + "&characterEncoding=UTF-8";

    private Conexion() {}

    public static Connection conectar() throws SQLException {
        String url = URL_PREDETERMINADA;
        String usuario = config("VOCADO_DB_USER", "vocado.db.user", "root");
        String password = config("VOCADO_DB_PASSWORD", "vocado.db.password", ""); //Aquí va tu contraseña local de tu BD
        return DriverManager.getConnection(url, usuario, password);
    }

    private static String config(String env, String property, String fallback) {
        String value = System.getenv(env);
        if (value == null || value.isBlank()) value = System.getProperty(property);
        if (value == null || value.isBlank()) value = fallback;
        return value;
    }
}

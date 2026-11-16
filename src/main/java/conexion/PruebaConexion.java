package conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {
    public static void main(String[] args) {
        try (Connection cn = Conexion.conectar()) {
            System.out.println("CONEXIÓN EXITOSA CON MYSQL");
            System.out.println("Base de datos: " + cn.getCatalog());
        } catch (SQLException e) {
            System.err.println("NO SE PUDO CONECTAR A MYSQL");
            System.err.println("Detalle: " + e.getMessage());
            System.err.println("Verifica que MySQL esté iniciado, que exista vocado_lunch_db "
                    + "y que el usuario/contraseña sean los de ESTA computadora.");
        }
    }
}

package conexion;
import java.sql.Connection;
public class PruebaConexion {
    public static void main(String[] args) {
        Connection cn = Conexion.conectar();
        if (cn != null) {
            System.out.println("CONEXION EXITOSA CON MYSQL");
        } else {
            System.out.println("ERROR DE CONEXION");
        }
    }
}

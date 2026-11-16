package conexion;

import java.sql.*;

public class PruebaConexion {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXION VOCADOLUNCH ===");

        try (Connection cn = Conexion.conectar()) {
            DatabaseMetaData meta = cn.getMetaData();
            System.out.println("CONEXION EXITOSA");
            System.out.println("Servidor: " + meta.getDatabaseProductName());
            System.out.println("Version: " + meta.getDatabaseProductVersion());
            System.out.println("Base de datos: " + cn.getCatalog());

            comprobarTabla(cn, "desayuno");
            comprobarTabla(cn, "usuarios");
            comprobarTabla(cn, "pedidos");
            comprobarTabla(cn, "detalle_pedido");

            try (PreparedStatement ps =
                         cn.prepareStatement("SELECT COUNT(*) FROM desayuno");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Registros en desayuno: " + rs.getInt(1));
                }
            }
            System.out.println("PRUEBA TERMINADA CORRECTAMENTE.");
        } catch (SQLException e) {
            System.err.println("NO SE PUDO COMPLETAR LA CONEXION: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState()
                    + " | Codigo: " + e.getErrorCode());
            System.err.println("Verifica MySQL Server, base de datos y credenciales.");
            e.printStackTrace();
        }
    }

    private static void comprobarTabla(Connection cn, String tabla)
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.tables "
                + "WHERE table_schema = DATABASE() AND table_name = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tabla);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                boolean existe = rs.getInt(1) > 0;
                System.out.println("Tabla " + tabla + ": "
                        + (existe ? "ENCONTRADA" : "NO ENCONTRADA"));
                if (!existe) throw new SQLException("No existe la tabla: " + tabla);
            }
        }
    }
}

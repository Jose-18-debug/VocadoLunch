package conexion;

import java.sql.*;

/**
 * Prueba INSERT, SELECT, UPDATE y DELETE en desayuno.
 * Los cambios se revierten con ROLLBACK; no deja registros de prueba.
 */
public class PruebaIntegracionBD {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA CRUD DE DESAYUNO ===");
        try (Connection cn = Conexion.conectar()) {
            cn.setAutoCommit(false);
            try {
                int id = insertar(cn);
                consultar(cn, id);
                actualizar(cn, id);
                consultar(cn, id);
                eliminar(cn, id);
                cn.rollback();
                System.out.println("CRUD VERIFICADO. Cambios temporales revertidos.");
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("FALLO LA PRUEBA CRUD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int insertar(Connection cn) throws SQLException {
        String sql = "INSERT INTO desayuno "
                + "(nombre, precio, descripcion, ruta, url_imagen, disponible) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps =
                     cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "PRUEBA_VOCADOLUNCH");
            ps.setDouble(2, 1.00);
            ps.setString(3, "Registro temporal de prueba");
            ps.setNull(4, Types.VARCHAR);
            ps.setNull(5, Types.VARCHAR);
            ps.setBoolean(6, true);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("INSERT OK. ID: " + id);
                    return id;
                }
            }
        }
        throw new SQLException("No se obtuvo el ID generado.");
    }

    private static void consultar(Connection cn, int id) throws SQLException {
        String sql = "SELECT nombre, precio, disponible FROM desayuno "
                + "WHERE id_desayuno = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("No se encontro el registro.");
                System.out.println("SELECT OK: " + rs.getString("nombre")
                        + " | precio: " + rs.getDouble("precio")
                        + " | disponible: " + rs.getBoolean("disponible"));
            }
        }
    }

    private static void actualizar(Connection cn, int id) throws SQLException {
        String sql = "UPDATE desayuno SET precio = ?, disponible = ? "
                + "WHERE id_desayuno = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, 2.00);
            ps.setBoolean(2, false);
            ps.setInt(3, id);
            if (ps.executeUpdate() != 1)
                throw new SQLException("La actualizacion no afecto una fila.");
            System.out.println("UPDATE OK.");
        }
    }

    private static void eliminar(Connection cn, int id) throws SQLException {
        try (PreparedStatement ps =
                     cn.prepareStatement("DELETE FROM desayuno WHERE id_desayuno = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1)
                throw new SQLException("La eliminacion no afecto una fila.");
            System.out.println("DELETE OK.");
        }
    }
}

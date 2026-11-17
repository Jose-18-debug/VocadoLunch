package dao;

import conexion.Conexion;
import modelo.Pedido;
import modelo.DetallePedido;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PedidoDAO {

    public boolean guardarPedido(Pedido pedido, List<DetallePedido> detalles) {
        // Columnas exactas según tu imagen de la BD
        String sqlPedido = "INSERT INTO pedidos (id_cliente, id_repartidor, fecha_hora, total, estado) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_desayuno, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psDetalle = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            conn.setAutoCommit(false); // INICIAMOS TRANSACCIÓN

            // 1. Insertar el Pedido
            psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, pedido.getIdCliente());

            // Manejo del repartidor por si viene en 0
            if (pedido.getIdRepartidor() > 0) {
                psPedido.setInt(2, pedido.getIdRepartidor());
            } else {
                psPedido.setNull(2, java.sql.Types.INTEGER);
            }

            psPedido.setString(3, pedido.getFechaHora());
            psPedido.setFloat(4, pedido.getTotal());
            psPedido.setString(5, pedido.getEstado());

            int filasAfectadas = psPedido.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el pedido.");
            }

            // Obtener el ID autogenerado
            rs = psPedido.getGeneratedKeys();
            int idPedidoGenerado = 0;
            if (rs.next()) {
                idPedidoGenerado = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el id_pedidos generado.");
            }

            // 2. Insertar los Detalles
            psDetalle = conn.prepareStatement(sqlDetalle);
            for (DetallePedido detalle : detalles) {
                psDetalle.setInt(1, idPedidoGenerado);
                psDetalle.setInt(2, detalle.getIdDesayuno());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setFloat(4, detalle.getSubtotal());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            // 3. Confirmar
            conn.commit();
            System.out.println("✅ Pedido guardado. ID: " + idPedidoGenerado);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("❌ ROLLBACK ejecutado por error.");
                } catch (SQLException ex) {
                    System.err.println("Error en rollback: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (psDetalle != null) psDetalle.close(); } catch (SQLException e) {}
            try { if (psPedido != null) psPedido.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    public List<Pedido> listarPedidos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedidos, id_cliente, id_repartidor, fecha_hora, total, estado FROM pedidos ORDER BY id_pedidos DESC";

        try (Connection cn = Conexion.conectar();
             Statement stmt = cn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedidos(rs.getInt("id_pedidos"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setIdRepartidor(rs.getInt("id_repartidor"));
                p.setFechaHora(rs.getString("fecha_hora"));
                p.setTotal(rs.getFloat("total"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        }
        return lista;
    }

    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id_pedidos = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean asignarRepartidor(int idPedido, int idRepartidor) {
        String sql = "UPDATE pedidos SET id_repartidor = ? WHERE id_pedidos = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idRepartidor);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
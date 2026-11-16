package dao;

import conexion.Conexion;
import modelo.Pedido;
import modelo.DetallePedido;
import java.sql.*;
import java.util.List;

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
}
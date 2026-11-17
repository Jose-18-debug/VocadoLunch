package dao;

import conexion.Conexion;
import modelo.Pedido;
import modelo.DetallePedido;
import java.sql.*;
import java.util.*;

public class PedidoDAO {
    /** Guarda pedido y detalles en una sola transacción. Devuelve el ID generado. */
    public int guardarPedido(Pedido pedido, List<DetallePedido> detalles) throws SQLException {
        if(detalles==null || detalles.isEmpty()) throw new SQLException("Agrega al menos un desayuno al pedido.");
        String qPedido="INSERT INTO pedidos(id_cliente,id_repartidor,fecha_hora,total,estado,metodo_pago,pagado) VALUES(?,NULL,?,?,?, ?,0)";
        String qDetalle="INSERT INTO detalle_pedido(id_pedido,id_desayuno,cantidad,subtotal) VALUES(?,?,?,?)";
        try(Connection cn=Conexion.conectar()){
            cn.setAutoCommit(false);
            try {
                validarUsuario(cn,pedido.getIdCliente(),"Cliente");
                double total=0;
                for(DetallePedido d:detalles){
                    if(d.getCantidad()<=0) throw new SQLException("La cantidad debe ser mayor a cero.");
                    try(PreparedStatement p=cn.prepareStatement("SELECT precio,disponible FROM desayuno WHERE id_desayuno=?")){
                        p.setInt(1,d.getIdDesayuno());
                        try(ResultSet r=p.executeQuery()){
                            if(!r.next() || !r.getBoolean("disponible")) throw new SQLException("Uno de los desayunos ya no existe o no está disponible.");
                            double subtotal=r.getDouble("precio")*d.getCantidad();
                            d.setSubtotal((float)subtotal); total+=subtotal;
                        }
                    }
                }
                int id;
                try(PreparedStatement ps=cn.prepareStatement(qPedido,Statement.RETURN_GENERATED_KEYS)){
                    ps.setInt(1,pedido.getIdCliente()); ps.setString(2,pedido.getFechaHora());
                    ps.setDouble(3,total); ps.setString(4,pedido.getEstado()); ps.setString(5,pedido.getMetodoPago());
                    ps.executeUpdate();
                    try(ResultSet keys=ps.getGeneratedKeys()){
                        if(!keys.next()) throw new SQLException("No se obtuvo el folio del pedido.");
                        id=keys.getInt(1);
                    }
                }
                try(PreparedStatement ps=cn.prepareStatement(qDetalle)){
                    for(DetallePedido d:detalles){
                        ps.setInt(1,id); ps.setInt(2,d.getIdDesayuno()); ps.setInt(3,d.getCantidad());
                        ps.setDouble(4,d.getSubtotal()); ps.addBatch();
                    }
                    ps.executeBatch();
                }
                cn.commit(); return id;
            } catch(Exception ex) {
                cn.rollback();
                if(ex instanceof SQLException) throw (SQLException)ex;
                throw new SQLException("No se pudo guardar el pedido.",ex);
            } finally { cn.setAutoCommit(true); }
        }
    }
    private void validarUsuario(Connection cn,int id,String rol) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("SELECT 1 FROM usuarios WHERE id_usuario=? AND LOWER(rol)=LOWER(?)")){
            ps.setInt(1,id); ps.setString(2,rol);
            try(ResultSet rs=ps.executeQuery()){if(!rs.next()) throw new SQLException("No existe un usuario con ID "+id+" y rol "+rol+". Registra o selecciona un cliente válido.");}
        }
    }
    public List<Pedido> listarPedidos() throws SQLException {
        List<Pedido> lista=new ArrayList<>();
        String sql="SELECT p.id_pedidos,p.id_cliente,p.id_repartidor,p.fecha_hora,p.total,p.estado,p.metodo_pago,p.pagado,u.nombre cliente FROM pedidos p LEFT JOIN usuarios u ON u.id_usuario=p.id_cliente ORDER BY p.id_pedidos DESC";
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                Pedido p=new Pedido(); p.setIdPedidos(rs.getInt("id_pedidos")); p.setIdCliente(rs.getInt("id_cliente"));
                p.setIdRepartidor(rs.getInt("id_repartidor")); p.setFechaHora(rs.getString("fecha_hora"));
                p.setTotal(rs.getFloat("total")); p.setEstado(rs.getString("estado"));
                p.setMetodoPago(rs.getString("metodo_pago")); p.setPagado(rs.getBoolean("pagado")); p.setNombreCliente(rs.getString("cliente")); lista.add(p);
            }
        } return lista;
    }
    public boolean actualizarEstado(int id,String estado) throws SQLException {
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement("UPDATE pedidos SET estado=? WHERE id_pedidos=?")){
            ps.setString(1,estado); ps.setInt(2,id); return ps.executeUpdate()>0;
        }
    }
    public boolean asignarRepartidor(int idPedido,int idRepartidor) throws SQLException {
        try(Connection cn=Conexion.conectar()){
            validarUsuario(cn,idRepartidor,"Repartidor");
            try(PreparedStatement ps=cn.prepareStatement("UPDATE pedidos SET id_repartidor=? WHERE id_pedidos=?")){
                ps.setInt(1,idRepartidor); ps.setInt(2,idPedido); return ps.executeUpdate()>0;
            }
        }
    }
    public boolean confirmarPago(int idPedido) throws SQLException {
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement("UPDATE pedidos SET pagado=1 WHERE id_pedidos=?")){
            ps.setInt(1,idPedido); return ps.executeUpdate()>0;
        }
    }

    /** Indicadores del dashboard: pedidos del día, en preparación y ventas pagadas. */
    public Map<String, Object> resumenDashboardHoy() throws SQLException {
        String sql = "SELECT COUNT(*) AS pedidos, " +
                "SUM(CASE WHEN LOWER(estado) IN ('en preparación','en preparacion','en_preparacion') THEN 1 ELSE 0 END) AS preparacion, " +
                "COALESCE(SUM(CASE WHEN pagado=1 THEN total ELSE 0 END),0) AS ventas " +
                "FROM pedidos WHERE DATE(STR_TO_DATE(fecha_hora,'%Y-%m-%d %H:%i:%s'))=CURDATE()";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            rs.next();
            Map<String, Object> datos = new HashMap<>();
            datos.put("pedidos", rs.getInt("pedidos"));
            datos.put("preparacion", rs.getInt("preparacion"));
            datos.put("ventas", rs.getDouble("ventas"));
            return datos;
        }
    }

    public Map<String,Object> resumenVentasHoy() throws SQLException {
        String sql="SELECT COUNT(*) pedidos,COALESCE(SUM(total),0) total FROM pedidos WHERE pagado=1 AND DATE(STR_TO_DATE(fecha_hora,'%Y-%m-%d %H:%i:%s'))=CURDATE()";
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            rs.next(); Map<String,Object> m=new HashMap<>(); m.put("pedidos",rs.getInt("pedidos")); m.put("total",rs.getDouble("total")); return m;
        }
    }
}

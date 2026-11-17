package modelo;
import java.util.List;

public class Pedido {
    private int idPedidos,idCliente,idRepartidor;
    private String fechaHora,estado,metodoPago,nombreCliente;
    private float total;
    private boolean pagado;
    private List<DetallePedido> detalles;
    public int getIdPedidos(){return idPedidos;} public void setIdPedidos(int v){idPedidos=v;}
    public int getIdCliente(){return idCliente;} public void setIdCliente(int v){idCliente=v;}
    public int getIdRepartidor(){return idRepartidor;} public void setIdRepartidor(int v){idRepartidor=v;}
    public String getFechaHora(){return fechaHora;} public void setFechaHora(String v){fechaHora=v;}
    public float getTotal(){return total;} public void setTotal(float v){total=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
    public String getMetodoPago(){return metodoPago;} public void setMetodoPago(String v){metodoPago=v;}
    public boolean isPagado(){return pagado;} public void setPagado(boolean v){pagado=v;}
    public String getNombreCliente(){return nombreCliente;} public void setNombreCliente(String v){nombreCliente=v;}
    public List<DetallePedido> getDetalles(){return detalles;} public void setDetalles(List<DetallePedido> v){detalles=v;}
}

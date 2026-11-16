package modelo;

import java.util.List;

public class Pedido {
    private int idPedidos;
    private int idCliente;
    private int idRepartidor;
    private String fechaHora;
    private float total;
    private String estado;
    private List<DetallePedido> detalles;

    public Pedido() {}

    public int getIdPedidos() { return idPedidos; }
    public void setIdPedidos(int idPedidos) { this.idPedidos = idPedidos; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(int idRepartidor) { this.idRepartidor = idRepartidor; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
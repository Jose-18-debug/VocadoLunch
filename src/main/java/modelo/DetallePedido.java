package modelo;

public class DetallePedido {
    private int idDetalle;
    private int idPedidos; // FK a la tabla pedidos
    private int idDesayuno; // FK a la tabla desayuno
    private int cantidad;
    private float subtotal;

    public DetallePedido() {}

    public DetallePedido(int idDesayuno, int cantidad, float subtotal) {
        this.idDesayuno = idDesayuno;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdPedidos() { return idPedidos; }
    public void setIdPedidos(int idPedidos) { this.idPedidos = idPedidos; }

    public int getIdDesayuno() { return idDesayuno; }
    public void setIdDesayuno(int idDesayuno) { this.idDesayuno = idDesayuno; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public float getSubtotal() { return subtotal; }
    public void setSubtotal(float subtotal) { this.subtotal = subtotal; }
}
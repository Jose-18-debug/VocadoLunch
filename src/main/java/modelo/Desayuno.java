package modelo;

public class Desayuno {
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String ruta;
    private String urlImagen;
    private boolean disponible;

    public Desayuno(int id, String nombre, double precio, String descripcion,
                    String ruta, String urlImagen, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.ruta = ruta;
        this.urlImagen = urlImagen;
        this.disponible = disponible;
    }

    public Desayuno(String nombre, double precio, String descripcion, boolean disponible) {
        this(0, nombre, precio, descripcion, "", "", disponible);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }
    public String getRuta() { return ruta; }
    public String getUrlImagen() { return urlImagen; }
    public boolean isDisponible() { return disponible; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}

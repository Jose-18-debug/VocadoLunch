package modelo;

public class Usuario {
    private int id;
    private String nombre, telefono, direccion, rol, correo;
    public Usuario(int id,String nombre,String telefono,String direccion,String rol,String correo){
        this.id=id; this.nombre=nombre; this.telefono=telefono; this.direccion=direccion; this.rol=rol; this.correo=correo;
    }
    public int getId(){return id;} public String getNombre(){return nombre;}
    public String getTelefono(){return telefono;} public String getDireccion(){return direccion;}
    public String getRol(){return rol;} public String getCorreo(){return correo;}
    @Override public String toString(){return id+" - "+nombre;}
}

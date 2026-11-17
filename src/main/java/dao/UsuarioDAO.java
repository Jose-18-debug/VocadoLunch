package dao;

import conexion.Conexion;
import modelo.Usuario;
import java.sql.*;
import java.util.*;

public class UsuarioDAO {
    public List<Usuario> listarPorRol(String rol) throws SQLException {
        List<Usuario> lista=new ArrayList<>();
        String sql="SELECT id_usuario,nombre,telefono,direccion,rol,correo FROM usuarios WHERE LOWER(rol)=LOWER(?) ORDER BY id_usuario ASC";
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setString(1,rol);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) lista.add(new Usuario(rs.getInt("id_usuario"),rs.getString("nombre"),
                        rs.getString("telefono"),rs.getString("direccion"),rs.getString("rol"),rs.getString("correo")));
            }
        }
        return lista;
    }
    public List<Usuario> listarClientes() throws SQLException { return listarPorRol("Cliente"); }
    public List<Usuario> listarRepartidores() throws SQLException { return listarPorRol("Repartidor"); }
    public void insertarCliente(String nombre,String telefono,String direccion,String correo) throws SQLException {
        String sql="INSERT INTO usuarios(nombre,telefono,direccion,rol,correo,`contraseña`) VALUES(?,?,?,'Cliente',?,?)";
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setString(1,nombre); ps.setString(2,telefono); ps.setString(3,direccion); ps.setString(4,correo);
            ps.setString(5,""); ps.executeUpdate();
        }
    }
    public boolean existe(int id,String rol) throws SQLException {
        String sql="SELECT 1 FROM usuarios WHERE id_usuario=? AND LOWER(rol)=LOWER(?)";
        try(Connection cn=Conexion.conectar(); PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setInt(1,id); ps.setString(2,rol);
            try(ResultSet rs=ps.executeQuery()){return rs.next();}
        }
    }
}

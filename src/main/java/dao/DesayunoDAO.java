
package dao;

import conexion.Conexion;
import modelo.Desayuno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DesayunoDAO {

    // CONSULTAR TODOS LOS DESAYUNOS
    public List<Desayuno> listar() throws SQLException {

        //noinspection SqlResolve
        String sql = "SELECT id_desayuno, nombre, precio, descripcion, ruta, url_imagen, disponible "
                + "FROM desayuno ORDER BY id_desayuno DESC";

        List<Desayuno> lista = new ArrayList<>();

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Desayuno(
                        rs.getInt("id_desayuno"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("descripcion"),
                        rs.getString("ruta"),
                        rs.getString("url_imagen"),
                        rs.getBoolean("disponible")
                ));
            }
        }

        return lista;
    }

    // INSERTAR UN DESAYUNO
    public void insertar(Desayuno d) throws SQLException {

        //noinspection SqlResolve
        String sql = "INSERT INTO desayuno "
                + "(nombre, precio, descripcion, ruta, url_imagen, disponible) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.setDouble(2, d.getPrecio());
            ps.setString(3, d.getDescripcion());
            ps.setString(4, d.getRuta());
            ps.setString(5, d.getUrlImagen());
            ps.setBoolean(6, d.isDisponible());

            ps.executeUpdate();
        }
    }

    // ACTUALIZAR UN DESAYUNO
    public void actualizar(Desayuno d) throws SQLException {

        //noinspection SqlResolve
        String sql = "UPDATE desayuno SET nombre=?, precio=?, "
                + "descripcion=?, disponible=? WHERE id_desayuno=?";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.setDouble(2, d.getPrecio());
            ps.setString(3, d.getDescripcion());
            ps.setBoolean(4, d.isDisponible());
            ps.setInt(5, d.getId());

            ps.executeUpdate();
        }
    }

    // ELIMINAR UN DESAYUNO
    public void eliminar(int id) throws SQLException {

        //noinspection SqlResolve
        String sql = "DELETE FROM desayuno WHERE id_desayuno=?";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
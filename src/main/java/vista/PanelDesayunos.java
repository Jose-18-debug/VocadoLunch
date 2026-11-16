package vista;

import dao.DesayunoDAO;
import modelo.Desayuno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelDesayunos extends JPanel {
    private final DesayunoDAO dao = new DesayunoDAO();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Descripción", "Disponible"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private boolean conexionDisponible;

    public PanelDesayunos() {
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(30, 34, 25, 34));
        setLayout(new BorderLayout(0, 18));

        JLabel titulo = new JLabel("Desayunos");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.TEXTO);

        JLabel descripcion = new JLabel("Administra los productos que aparecen en el menú.");
        descripcion.setFont(Estilo.NORMAL);
        descripcion.setForeground(Estilo.GRIS);

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(descripcion);

        tabla.setRowHeight(30);
        tabla.setFont(Estilo.NORMAL);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(239, 218, 207)));

        JButton agregar = boton("＋ Agregar desayuno", Estilo.NARANJA);
        JButton editar = boton("Editar", Estilo.ROSA);
        JButton eliminar = boton("Eliminar", new Color(174, 91, 85));
        JButton actualizar = boton("↻ Actualizar", new Color(120, 130, 120));

        agregar.addActionListener(e -> agregar());
        editar.addActionListener(e -> editar());
        eliminar.addActionListener(e -> eliminar());
        actualizar.addActionListener(e -> cargarDatos());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        acciones.setOpaque(false);
        acciones.add(agregar); acciones.add(editar); acciones.add(eliminar); acciones.add(actualizar);

        JPanel inferior = new JPanel(new BorderLayout(0, 10));
        inferior.setOpaque(false);
        inferior.add(acciones, BorderLayout.NORTH);

        add(encabezado, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
        cargarDatos();
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        try {
            List<Desayuno> desayunos = dao.listar();
            conexionDisponible = true;
            for (Desayuno d : desayunos) {
                modelo.addRow(new Object[]{d.getId(), d.getNombre(),
                        String.format("$%.2f", d.getPrecio()), d.getDescripcion(),
                        d.isDisponible() ? "Sí" : "No"});
            }
            if (desayunos.isEmpty()) {
                modelo.addRow(new Object[]{"—", "No hay desayunos registrados", "—", "Agrega el primer producto", "—"});
            }
        } catch (SQLException ex) {
            conexionDisponible = false;
            modelo.addRow(new Object[]{"—", "Sin conexión a MySQL", "—",
                    "Revisa Conexion.java y ejecuta PruebaConexion", "—"});
        }
    }

    private void agregar() {
        FormularioDesayuno f = new FormularioDesayuno(this);
        Desayuno d = f.mostrar(null);
        if (d == null) return;
        try {
            dao.insertar(d);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Desayuno agregado correctamente.");
        } catch (SQLException ex) {
            errorBD(ex);
        }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || !conexionDisponible) {
            JOptionPane.showMessageDialog(this, "Selecciona un desayuno registrado.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        String nombreActual = modelo.getValueAt(fila, 1).toString();
        String precioActual = modelo.getValueAt(fila, 2).toString().replace("$", "");
        String descripcionActual = modelo.getValueAt(fila, 3).toString();
        boolean disponible = "Sí".equals(modelo.getValueAt(fila, 4).toString());
        Desayuno actual = new Desayuno(id, nombreActual, Double.parseDouble(precioActual),
                descripcionActual, "", "", disponible);
        FormularioDesayuno f = new FormularioDesayuno(this);
        Desayuno editado = f.mostrar(actual);
        if (editado == null) return;
        try {
            dao.actualizar(editado);
            cargarDatos();
        } catch (SQLException ex) {
            errorBD(ex);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || !conexionDisponible) {
            JOptionPane.showMessageDialog(this, "Selecciona un desayuno registrado.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar este desayuno?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;
        try {
            dao.eliminar(id);
            cargarDatos();
        } catch (SQLException ex) {
            errorBD(ex);
        }
    }

    private void errorBD(SQLException ex) {
        JOptionPane.showMessageDialog(this, "No se pudo completar la operación en MySQL:\n"
                + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}

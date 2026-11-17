package vista;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelRepartidores extends JPanel {
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID Pedido", "ID Cliente", "Repartidor", "Fecha / Hora", "Total", "Estado"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelRepartidores() {
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(30, 34, 25, 34));
        setLayout(new BorderLayout(0, 18));

        JLabel titulo = new JLabel("Repartidores y Gestión de Pedidos");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.TEXTO);

        JLabel descripcion = new JLabel("Asigna repartidores y actualiza el flujo de entrega de cada pedido.");
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

        JButton btnEstado = boton("Cambiar Estado", Estilo.NARANJA);
        JButton btnRepartidor = boton("Asignar Repartidor", Estilo.ROSA);
        JButton btnRecargar = boton("↻ Actualizar", new Color(120, 130, 120));

        btnEstado.addActionListener(e -> cambiarEstado());
        btnRepartidor.addActionListener(e -> asignarRepartidor());
        btnRecargar.addActionListener(e -> cargarDatos());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        acciones.setOpaque(false);
        acciones.add(btnEstado);
        acciones.add(btnRepartidor);
        acciones.add(btnRecargar);

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
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        // Truco para forzar colores personalizados en botones bajo Windows
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        try {
            List<Pedido> lista = pedidoDAO.listarPedidos();
            for (Pedido p : lista) {
                modelo.addRow(new Object[]{
                        p.getIdPedidos(),
                        p.getIdCliente(),
                        p.getIdRepartidor() == 0 ? "Sin asignar" : p.getIdRepartidor(),
                        p.getFechaHora(),
                        String.format("$%.2f", p.getTotal()),
                        p.getEstado()
                });
            }
            if (lista.isEmpty()) {
                modelo.addRow(new Object[]{"—", "—", "No hay pedidos", "—", "—", "—"});
            }
        } catch (Exception ex) {
            modelo.addRow(new Object[]{"—", "—", "Error de conexión a BD", "—", "—", "—"});
        }
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla.");
            return;
        }
        try {
            int idPedido = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            String[] opciones = {"Recibido", "En preparación", "En camino", "Entregado"};
            String nuevoEstado = (String) JOptionPane.showInputDialog(
                    this, "Selecciona el nuevo estado:", "Actualizar Estado",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (nuevoEstado != null) {
                if (pedidoDAO.actualizarEstado(idPedido, nuevoEstado)) {
                    JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void asignarRepartidor() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla.");
            return;
        }
        try {
            int idPedido = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

            // Consultamos los repartidores directamente de la tabla usuarios filtrando por rol
            List<String> repartidoresList = new ArrayList<>();
            Map<String, Integer> mapNombresIds = new HashMap<>();

            String sql = "SELECT id_usuario, nombre FROM usuarios WHERE rol = 'Repartidor'";
            try (Connection cn = conexion.Conexion.conectar();
                 Statement stmt = cn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String item = id + " - " + nombre;
                    repartidoresList.add(item);
                    mapNombresIds.put(item, id);
                }
            }

            if (repartidoresList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay usuarios con rol 'Repartidor' registrados en la base de datos.\nAsegúrate de registrar uno en la tabla usuarios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    this, "Selecciona el repartidor:", "Asignar Repartidor",
                    JOptionPane.QUESTION_MESSAGE, null,
                    repartidoresList.toArray(), repartidoresList.get(0));

            if (seleccion != null) {
                int idRepartidorElegido = mapNombresIds.get(seleccion);

                if (pedidoDAO.asignarRepartidor(idPedido, idRepartidorElegido)) {
                    JOptionPane.showMessageDialog(this, "¡Repartidor asignado correctamente!");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo asignar el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la asignación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
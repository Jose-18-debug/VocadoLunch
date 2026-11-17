package vista;

import dao.PedidoDAO;
import dao.UsuarioDAO;
import modelo.Pedido;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelRepartidores extends JPanel {
    private final PedidoDAO pedidosDAO = new PedidoDAO();
    private final UsuarioDAO usuariosDAO = new UsuarioDAO();
    private final DefaultTableModel repartidoresTM = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono", "Dirección", "Correo"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel entregasTM = new DefaultTableModel(new Object[]{"Folio", "Cliente", "Repartidor", "Fecha", "Total", "Estado"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tablaRepartidores = new JTable(repartidoresTM);
    private final JTable tablaEntregas = new JTable(entregasTM);

    public PanelRepartidores() {
        setLayout(new BorderLayout(10, 12));
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(25, 28, 22, 28));
        JLabel titulo = new JLabel("Repartidores y entregas");
        titulo.setFont(Estilo.TITULO); titulo.setForeground(Estilo.TEXTO);
        JLabel descripcion = new JLabel("Registra repartidores y asígnalos a los pedidos pendientes.");
        descripcion.setFont(Estilo.NORMAL); descripcion.setForeground(Estilo.GRIS);
        JPanel head = new JPanel(); head.setOpaque(false); head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(titulo); head.add(Box.createVerticalStrut(5)); head.add(descripcion);
        add(head, BorderLayout.NORTH);

        Estilo.estilizarTabla(tablaRepartidores);
        Estilo.estilizarTabla(tablaEntregas);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Repartidores registrados", new JScrollPane(tablaRepartidores));
        tabs.addTab("Entregas y asignaciones", new JScrollPane(tablaEntregas));
        add(tabs, BorderLayout.CENTER);

        JButton registrar = Estilo.boton("＋ Registrar repartidor", Estilo.NARANJA);
        JButton actualizarRepartidores = Estilo.boton("Actualizar repartidores", Estilo.ROSA);
        JButton asignar = Estilo.boton("Asignar repartidor", Estilo.ROSA);
        JButton estado = Estilo.boton("Cambiar estado", Estilo.NARANJA_OSCURO);
        JButton actualizarEntregas = Estilo.boton("Actualizar entregas", new Color(120, 130, 120));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); acciones.setOpaque(false);
        acciones.add(registrar); acciones.add(actualizarRepartidores); acciones.add(asignar); acciones.add(estado); acciones.add(actualizarEntregas);
        add(acciones, BorderLayout.SOUTH);

        registrar.addActionListener(e -> registrarRepartidor());
        actualizarRepartidores.addActionListener(e -> cargarRepartidores());
        asignar.addActionListener(e -> asignar());
        estado.addActionListener(e -> cambiarEstado());
        actualizarEntregas.addActionListener(e -> cargarEntregas());
        cargarRepartidores(); cargarEntregas();
    }

    private void cargarRepartidores() {
        repartidoresTM.setRowCount(0);
        try {
            for (Usuario u : usuariosDAO.listarRepartidores())
                repartidoresTM.addRow(new Object[]{u.getId(), u.getNombre(), u.getTelefono(), u.getDireccion(), u.getCorreo()});
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void cargarEntregas() {
        entregasTM.setRowCount(0);
        try {
            for (Pedido p : pedidosDAO.listarPedidos())
                entregasTM.addRow(new Object[]{p.getIdPedidos(), p.getNombreCliente(),
                        p.getIdRepartidor() == 0 ? "Sin asignar" : nombreRepartidor(p.getIdRepartidor()),
                        p.getFechaHora(), String.format("$%.2f", p.getTotal()), p.getEstado()});
        } catch (Exception ex) { mostrarError(ex); }
    }

    private String nombreRepartidor(int id) {
        try { for (Usuario u : usuariosDAO.listarRepartidores()) if (u.getId() == id) return u.getNombre(); }
        catch (SQLException ignored) { }
        return "ID " + id;
    }

    private void registrarRepartidor() {
        JTextField nombre = new JTextField(), telefono = new JTextField(), direccion = new JTextField(), correo = new JTextField();
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Nombre completo")); form.add(nombre);
        form.add(new JLabel("Teléfono (7 a 15 dígitos)")); form.add(telefono);
        form.add(new JLabel("Dirección")); form.add(direccion);
        form.add(new JLabel("Correo")); form.add(correo);
        int opcion = JOptionPane.showConfirmDialog(this, form, "Registrar repartidor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        String tel = telefono.getText().trim();
        if (nombre.getText().isBlank() || !tel.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Escribe un nombre y un teléfono válido.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            usuariosDAO.insertarRepartidor(nombre.getText().trim(), tel, direccion.getText().trim(), correo.getText().trim());
            cargarRepartidores();
            JOptionPane.showMessageDialog(this, "Repartidor registrado. Ya estará disponible para asignarlo a un pedido.");
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private int pedidoSeleccionado() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila < 0) throw new IllegalArgumentException("Selecciona un pedido en la pestaña Entregas y asignaciones.");
        return Integer.parseInt(entregasTM.getValueAt(fila, 0).toString());
    }

    private void asignar() {
        try {
            int idPedido = pedidoSeleccionado();
            List<Usuario> lista = usuariosDAO.listarRepartidores();
            if (lista.isEmpty()) { JOptionPane.showMessageDialog(this, "Primero registra un repartidor."); return; }
            JComboBox<Usuario> combo = new JComboBox<>(lista.toArray(new Usuario[0]));
            if (JOptionPane.showConfirmDialog(this, combo, "Selecciona repartidor", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            Usuario elegido = (Usuario) combo.getSelectedItem();
            if (elegido != null && pedidosDAO.asignarRepartidor(idPedido, elegido.getId())) {
                cargarEntregas(); JOptionPane.showMessageDialog(this, "Repartidor asignado correctamente.");
            }
        } catch (Exception ex) { mostrarError(ex); }
    }

    private void cambiarEstado() {
        try {
            int id = pedidoSeleccionado();
            String[] estados = {"Recibido", "En preparación", "En camino", "Entregado"};
            String nuevo = (String) JOptionPane.showInputDialog(this, "Nuevo estado:", "Cambiar estado",
                    JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
            if (nuevo != null && pedidosDAO.actualizarEstado(id, nuevo)) cargarEntregas();
        } catch (Exception ex) { mostrarError(ex); }
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "VocadoLunch", JOptionPane.WARNING_MESSAGE);
    }
}

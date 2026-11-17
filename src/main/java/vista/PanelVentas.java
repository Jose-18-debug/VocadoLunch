
package vista;

import dao.PedidoDAO;
import modelo.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PanelVentas extends JPanel {

    private final PedidoDAO dao = new PedidoDAO();

    private final DefaultTableModel tm = new DefaultTableModel(
            new Object[]{"Folio", "Cliente ID", "Fecha", "Total", "Método", "Estado pedido", "Pago"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable tabla = new JTable(tm);
    private final JLabel resumen = new JLabel("Ventas de hoy: —");

    public PanelVentas() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(25, 28, 22, 28));

        //Encabezado de la pestaña
        JLabel titulo = new JLabel("Pagos e historial de ventas");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.TEXTO);

        resumen.setFont(Estilo.SUBTITULO);
        resumen.setForeground(Estilo.TEXTO);

        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.setOpaque(false);
        north.add(titulo, BorderLayout.NORTH);
        north.add(resumen, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);

        //Aplicar el diseño general a la tabla
        Estilo.estilizarTabla(tabla);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //Botones con los colores del sistema
        JButton confirmar = Estilo.boton("Confirmar pago", Estilo.NARANJA);
        JButton actualizar = Estilo.boton("↻ Actualizar", Estilo.ROSA);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        south.setOpaque(false);
        south.add(confirmar);
        south.add(actualizar);

        add(south, BorderLayout.SOUTH);

        //Eventos de los botones
        confirmar.addActionListener(e -> confirmarPago());
        actualizar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        tm.setRowCount(0);

        try {
            List<Pedido> pedidos = dao.listarPedidos();

            for (Pedido p : pedidos) {
                tm.addRow(new Object[]{
                        p.getIdPedidos(),
                        p.getNombreCliente() == null ? p.getIdCliente() : p.getNombreCliente(),
                        p.getFechaHora(),
                        String.format("$%.2f", p.getTotal()),
                        p.getMetodoPago(),
                        p.getEstado(),
                        p.isPagado() ? "PAGADO" : "PENDIENTE"
                });
            }

            Map<String, Object> m = dao.resumenVentasHoy();

            resumen.setText(
                    "Pedidos pagados hoy: " + m.get("pedidos")
                            + "   |   Total vendido: "
                            + String.format("$%.2f", m.get("total"))
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo consultar ventas. Ejecuta primero la migración SQL.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void confirmarPago() {
        int row = tabla.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido.");
            return;
        }

        int id = Integer.parseInt(tm.getValueAt(row, 0).toString());

        if ("PAGADO".equals(tm.getValueAt(row, 6))) {
            JOptionPane.showMessageDialog(this, "Este pedido ya está pagado.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "¿Confirmar el pago del pedido " + id + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.confirmarPago(id)) {
                JOptionPane.showMessageDialog(this, "Pago confirmado.");
                cargar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

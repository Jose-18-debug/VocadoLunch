package vista;

import dao.PedidoDAO;
import modelo.Pedido;
import modelo.DetallePedido;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PanelPedidos extends JPanel {

    private JTextField txtIdCliente;
    private JTextField txtIdRepartidor;
    private JTextArea areaDetalles;
    private JLabel lblTotal;
    private JButton btnGuardar;

    public PanelPedidos() {
        // Usamos GridBagLayout para centrar una "tarjeta" de contenido
        setLayout(new GridBagLayout());
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TARJETA CONTENEDORA ---
        JPanel tarjeta = new JPanel(new BorderLayout(15, 15));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        tarjeta.setPreferredSize(new Dimension(650, 500)); // Tamaño fijo para que no se estire

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Registrar Nuevo Pedido");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Estilo.NARANJA_OSCURO);
        tarjeta.add(titulo, BorderLayout.NORTH);

        // --- PANEL CENTRAL (Formulario + Detalles) ---
        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setOpaque(false);

        // Formulario arriba
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 15));
        formulario.setOpaque(false);

        JLabel lblCliente = new JLabel("ID del Cliente:");
        lblCliente.setFont(new Font("SansSerif", Font.BOLD, 14));
        formulario.add(lblCliente);

        txtIdCliente = new JTextField("1");
        txtIdCliente.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formulario.add(txtIdCliente);

        JLabel lblRepartidor = new JLabel("ID del Repartidor (0 = sin asignar):");
        lblRepartidor.setFont(new Font("SansSerif", Font.BOLD, 14));
        formulario.add(lblRepartidor);

        txtIdRepartidor = new JTextField("0");
        txtIdRepartidor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formulario.add(txtIdRepartidor);

        JLabel lblTotalTexto = new JLabel("Total Calculado:");
        lblTotalTexto.setFont(new Font("SansSerif", Font.BOLD, 14));
        formulario.add(lblTotalTexto);

        lblTotal = new JLabel("$ 0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotal.setForeground(new Color(200, 50, 50));
        formulario.add(lblTotal);

        centro.add(formulario, BorderLayout.NORTH);

        // Área de detalles (simulación)
        areaDetalles = new JTextArea();
        areaDetalles.setText("--- Simulación de Carrito ---\n" +
                "2 x Desayuno ID 1 (Subtotal: $100.0)\n" +
                "1 x Desayuno ID 2 (Subtotal: $60.0)\n" +
                "-----------------------------");
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaDetalles.setBackground(new Color(250, 250, 250));

        JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
        scrollDetalles.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Productos del Pedido",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12))
        );
        centro.add(scrollDetalles, BorderLayout.CENTER);

        tarjeta.add(centro, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Botón) ---
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setOpaque(false);

        btnGuardar = new JButton("Guardar Pedido");
        btnGuardar.setBackground(Estilo.NARANJA_OSCURO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnGuardar.setOpaque(true);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnGuardar.setPreferredSize(new Dimension(180, 45));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnGuardar.addActionListener(e -> guardarPedidoPrueba());

        panelBoton.add(btnGuardar);
        tarjeta.add(panelBoton, BorderLayout.SOUTH);

        // Agregamos la tarjeta al panel principal (GridBagLayout la centra)
        add(tarjeta);
    }

    private void guardarPedidoPrueba() {
        try {
            int idCliente = Integer.parseInt(txtIdCliente.getText().trim());
            int idRepartidor = Integer.parseInt(txtIdRepartidor.getText().trim());

            if (idCliente <= 0) {
                JOptionPane.showMessageDialog(this, "El ID del cliente debe ser mayor a 0.");
                return;
            }

            // 1. Formatear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaActual = LocalDateTime.now().format(formatter);

            // 2. Crear el Pedido
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setIdCliente(idCliente);
            nuevoPedido.setIdRepartidor(idRepartidor);
            nuevoPedido.setFechaHora(fechaActual);
            nuevoPedido.setEstado("Recibido");

            // 3. Simular los detalles (Aquí luego conectarás tu JTable)
            List<DetallePedido> listaDetalles = new ArrayList<>();

            DetallePedido d1 = new DetallePedido();
            d1.setIdDesayuno(1);
            d1.setCantidad(2);
            d1.setSubtotal(100.0f);
            listaDetalles.add(d1);

            DetallePedido d2 = new DetallePedido();
            d2.setIdDesayuno(2);
            d2.setCantidad(1);
            d2.setSubtotal(60.0f);
            listaDetalles.add(d2);

            // 4. Calcular total
            float totalCalculado = 0;
            for (DetallePedido d : listaDetalles) {
                totalCalculado += d.getSubtotal();
            }
            nuevoPedido.setTotal(totalCalculado);
            lblTotal.setText("$ " + totalCalculado);

            // 5. Guardar en BD
            PedidoDAO dao = new PedidoDAO();
            boolean exito = dao.guardarPedido(nuevoPedido, listaDetalles);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "¡Pedido guardado con éxito!\nTotal: $" + totalCalculado,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar. Revisa la consola de IntelliJ.\n(Asegúrate de que el cliente y los desayunos existan en la BD)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa números válidos en los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
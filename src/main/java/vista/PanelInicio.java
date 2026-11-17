package vista;

import dao.PedidoDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class PanelInicio extends JPanel {
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final JLabel valorPedidos = new JLabel("—");
    private final JLabel valorPreparacion = new JLabel("—");
    private final JLabel valorVentas = new JLabel("—");
    private final JLabel estado = new JLabel("Última actualización: pendiente");

    public PanelInicio() {
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(30, 34, 26, 34));
        setLayout(new BorderLayout(0, 20));

        JLabel titulo = new JLabel("¡Bienvenido a VocadoLunch!");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.TEXTO);
        JLabel subtitulo = new JLabel("Resumen general de pedidos, preparación y ventas.");
        subtitulo.setFont(Estilo.NORMAL);
        subtitulo.setForeground(Estilo.GRIS);
        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(5));
        encabezado.add(subtitulo);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("Pedidos de hoy", valorPedidos, "Pedidos registrados", Estilo.NARANJA));
        tarjetas.add(tarjeta("En preparación", valorPreparacion, "Pedidos en preparación", Estilo.ROSA));
        tarjetas.add(tarjeta("Ventas de hoy", valorVentas, "Pedidos cuyo pago fue confirmado", new Color(190, 145, 83)));

        JPanel bienvenida = new JPanel(new BorderLayout(12, 12));
        bienvenida.setBackground(Color.WHITE);
        bienvenida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE), new EmptyBorder(22, 24, 22, 24)));
        JLabel logo = cargarLogo(120, 90);
        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        JLabel mensaje = new JLabel("Tu negocio, organizado desde un solo lugar");
        mensaje.setFont(Estilo.SUBTITULO);
        mensaje.setForeground(Estilo.NARANJA_OSCURO);
        JTextArea descripcion = new JTextArea("Administra los desayunos, registra clientes, crea pedidos, "
                + "organiza las entregas y consulta los pagos y ventas desde el menú lateral.");
        descripcion.setFont(Estilo.NORMAL);
        descripcion.setForeground(Estilo.GRIS);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        texto.add(mensaje);
        texto.add(Box.createVerticalStrut(10));
        texto.add(descripcion);
        bienvenida.add(texto, BorderLayout.CENTER);
        bienvenida.add(logo, BorderLayout.EAST);

        JButton actualizar = Estilo.boton("↻ Actualizar estadísticas", Estilo.NARANJA);
        actualizar.addActionListener(e -> actualizarEstadisticas());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.setOpaque(false);
        pie.add(actualizar);
        pie.add(Box.createHorizontalStrut(14));
        estado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        estado.setForeground(Estilo.GRIS);
        pie.add(estado);

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(tarjetas, BorderLayout.NORTH);
        centro.add(bienvenida, BorderLayout.CENTER);
        centro.add(pie, BorderLayout.SOUTH);
        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        actualizarEstadisticas();
    }

    private JPanel tarjeta(String titulo, JLabel valor, String detalle, Color acento) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, acento), new EmptyBorder(17, 18, 17, 18)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo); t.setFont(Estilo.NORMAL); t.setForeground(Estilo.GRIS);
        valor.setFont(new Font("SansSerif", Font.BOLD, 29)); valor.setForeground(Estilo.TEXTO);
        JLabel d = new JLabel(detalle); d.setFont(new Font("SansSerif", Font.PLAIN, 11)); d.setForeground(Estilo.GRIS);
        p.add(t); p.add(Box.createVerticalStrut(9)); p.add(valor); p.add(Box.createVerticalStrut(5)); p.add(d);
        return p;
    }

    private JLabel cargarLogo(int ancho, int alto) {
        java.net.URL recurso = getClass().getResource("/logo.png");
        if (recurso == null) return new JLabel("VocadoLunch");
        ImageIcon original = new ImageIcon(recurso);
        Image imagen = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(imagen));
    }

    public void actualizarEstadisticas() {
        try {
            Map<String, Object> datos = pedidoDAO.resumenDashboardHoy();
            valorPedidos.setText(String.valueOf(datos.get("pedidos")));
            valorPreparacion.setText(String.valueOf(datos.get("preparacion")));
            valorVentas.setText(String.format("$%.2f", ((Number) datos.get("ventas")).doubleValue()));
            estado.setText("Actualizado: " + java.time.LocalTime.now().withNano(0));
        } catch (Exception ex) {
            valorPedidos.setText("—"); valorPreparacion.setText("—"); valorVentas.setText("—");
            estado.setText("No se pudieron consultar las estadísticas: " + ex.getMessage());
        }
    }
}

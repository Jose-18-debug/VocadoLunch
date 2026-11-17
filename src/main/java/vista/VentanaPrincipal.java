package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final JPanel contenido = new JPanel(new CardLayout());
    private final CardLayout tarjetas = (CardLayout) contenido.getLayout();
    private final PanelInicio panelInicio = new PanelInicio();

    public VentanaPrincipal() {
        setTitle("VocadoLunch | Gestión de desayunos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(980, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(crearBarraLateral(), BorderLayout.WEST);
        contenido.setBackground(Estilo.CREMA);
        contenido.add(panelInicio, "inicio");
        contenido.add(new PanelDesayunos(), "desayunos");
        contenido.add(new PanelPedidos(), "pedidos");
        contenido.add(new PanelClientes(), "clientes");
        contenido.add(new PanelRepartidores(), "repartidores");
        contenido.add(new PanelVentas(), "ventas");
        add(contenido, BorderLayout.CENTER);
        mostrar("inicio");
    }

    private JPanel crearBarraLateral() {
        JPanel lateral = new JPanel();
        lateral.setPreferredSize(new Dimension(235, 0));
        lateral.setBackground(Estilo.LATERAL);
        lateral.setBorder(new EmptyBorder(22, 16, 20, 16));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        JLabel nombre = new JLabel("VocadoLunch");
        nombre.setFont(new Font("SansSerif", Font.BOLD, 23));
        nombre.setForeground(Estilo.NARANJA_OSCURO);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitulo = new JLabel("GESTIÓN DE DESAYUNOS");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 9));
        subtitulo.setForeground(Estilo.GRIS);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel logo = cargarLogo(); logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(nombre); lateral.add(Box.createVerticalStrut(4)); lateral.add(subtitulo);
        lateral.add(Box.createVerticalStrut(17)); lateral.add(logo); lateral.add(Box.createVerticalStrut(28));
        JLabel menu = new JLabel("MENÚ PRINCIPAL"); menu.setFont(new Font("SansSerif", Font.BOLD, 10));
        menu.setForeground(Estilo.GRIS); menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(menu); lateral.add(Box.createVerticalStrut(10));
        agregarBoton(lateral, "⌂   Inicio", "inicio");
        agregarBoton(lateral, "☕   Desayunos", "desayunos");
        agregarBoton(lateral, "▤   Pedidos", "pedidos");
        agregarBoton(lateral, "♙   Clientes", "clientes");
        agregarBoton(lateral, "♧   Repartidores", "repartidores");
        agregarBoton(lateral, "▥   Pagos y ventas", "ventas");
        lateral.add(Box.createVerticalGlue());
        JLabel pie = new JLabel("VocadoLunch • Proyecto escolar"); pie.setFont(new Font("SansSerif", Font.PLAIN, 10));
        pie.setForeground(Estilo.GRIS); lateral.add(pie);
        return lateral;
    }

    private JLabel cargarLogo() {
        java.net.URL recurso = getClass().getResource("/logo.png");

        if (recurso == null) {
            JLabel label = new JLabel("Logo no encontrado");
            label.setForeground(Estilo.GRIS);
            return label;
        }

        ImageIcon icon = new ImageIcon(recurso);
        int anchoOriginal = icon.getIconWidth();
        int altoOriginal = icon.getIconHeight();
        //Tamaño máximo del logo
        int anchoMaximo = 160;
        int altoMaximo = 105;

        //Mantener proporciones originales
        double escala = Math.min((double) anchoMaximo / anchoOriginal, (double) altoMaximo / altoOriginal);

        int nuevoAncho = (int) (anchoOriginal * escala);
        int nuevoAlto = (int) (altoOriginal * escala);

        Image imagen = icon.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);

        JLabel label = new JLabel(new ImageIcon(imagen));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private void agregarBoton(JPanel panel, String texto, String destino) {
        JButton boton = new JButton(texto);
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Estilo.TEXTO);
        boton.setBackground(new Color(255, 248, 242));
        boton.setOpaque(true); boton.setContentAreaFilled(true); boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Estilo.BORDE),
                new EmptyBorder(10, 13, 10, 8)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(e -> mostrar(destino));
        panel.add(boton); panel.add(Box.createVerticalStrut(7));
    }

    private void mostrar(String nombre) {
        tarjetas.show(contenido, nombre);
        if ("inicio".equals(nombre)) panelInicio.actualizarEstadisticas();
    }
}

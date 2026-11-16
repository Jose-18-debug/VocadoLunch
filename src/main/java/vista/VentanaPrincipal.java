package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final JPanel contenido = new JPanel(new BorderLayout());
    private final CardLayout tarjetas = new CardLayout();

    public VentanaPrincipal() {
        setTitle("VocadoLunch | Gestión de desayunos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(980, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraLateral(), BorderLayout.WEST);
        contenido.setLayout(tarjetas);
        contenido.setBackground(Estilo.CREMA);
        contenido.add(new PanelInicio(), "inicio");
        contenido.add(new PanelDesayunos(), "desayunos");
        contenido.add(new PanelInformativo("Pedidos", "Aquí aparecerán los pedidos registrados y sus estados."), "pedidos");
        contenido.add(new PanelInformativo("Clientes", "Aquí se consultarán los clientes registrados."), "clientes");
        contenido.add(new PanelInformativo("Repartidores", "Aquí se podrán consultar y asignar repartidores."), "repartidores");
        contenido.add(new PanelInformativo("Ventas", "Aquí se mostrará el historial y el total de ventas."), "ventas");
        add(contenido, BorderLayout.CENTER);
        mostrar("inicio");
    }

    private JPanel crearBarraLateral() {
        JPanel lateral = new JPanel();
        lateral.setPreferredSize(new Dimension(235, 0));
        lateral.setBackground(new Color(255, 239, 230));
        lateral.setBorder(new EmptyBorder(22, 16, 20, 16));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("VocadoLunch");
        logo.setFont(new Font("SansSerif", Font.BOLD, 23));
        logo.setForeground(Estilo.NARANJA_OSCURO);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("GESTIÓN DE DESAYUNOS");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 9));
        subtitulo.setForeground(Estilo.GRIS);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel imagen = cargarLogo();
        imagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        lateral.add(logo);
        lateral.add(Box.createVerticalStrut(4));
        lateral.add(subtitulo);
        lateral.add(Box.createVerticalStrut(18));
        lateral.add(imagen);
        lateral.add(Box.createVerticalStrut(28));
        lateral.add(etiqueta("MENÚ PRINCIPAL"));
        lateral.add(Box.createVerticalStrut(10));

        agregarBoton(lateral, "⌂   Inicio", "inicio");
        agregarBoton(lateral, "☕   Desayunos", "desayunos");
        agregarBoton(lateral, "▤   Pedidos", "pedidos");
        agregarBoton(lateral, "♙   Clientes", "clientes");
        agregarBoton(lateral, "♧   Repartidores", "repartidores");
        agregarBoton(lateral, "▥   Ventas", "ventas");

        lateral.add(Box.createVerticalGlue());
        JLabel pie = new JLabel("VocadoLunch • Proyecto escolar");
        pie.setFont(new Font("SansSerif", Font.PLAIN, 10));
        pie.setForeground(Estilo.GRIS);
        lateral.add(pie);
        return lateral;
    }

    private JLabel cargarLogo() {
        ImageIcon icon = null;
        java.net.URL recurso = getClass().getResource("/logo.png");
        if (recurso != null) icon = new ImageIcon(recurso);
        else {
            java.io.File archivo = new java.io.File("logo.png");
            if (archivo.exists()) icon = new ImageIcon(archivo.getAbsolutePath());
        }
        JLabel label;
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(img));
        } else {
            label = new JLabel(" [ logo.png ] ");
            label.setForeground(Estilo.GRIS);
            label.setFont(new Font("SansSerif", Font.ITALIC, 12));
        }
        return label;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(Estilo.GRIS);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void agregarBoton(JPanel panel, String texto, String destino) {
        JButton b = new JButton(texto);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(Estilo.TEXTO);
        b.setBackground(new Color(255, 248, 242));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 8));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> mostrar(destino));
        panel.add(b);
        panel.add(Box.createVerticalStrut(7));
    }

    private void mostrar(String nombre) {
        tarjetas.show(contenido, nombre);
    }
}

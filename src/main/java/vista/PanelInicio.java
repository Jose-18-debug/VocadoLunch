package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelInicio extends JPanel {
    public PanelInicio() {
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(34, 38, 30, 38));
        setLayout(new BorderLayout(0, 22));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("¡Buenos días!");
        titulo.setFont(Estilo.TITULO);
        titulo.setForeground(Estilo.TEXTO);
        JLabel texto = new JLabel("Bienvenido al panel de administración de VocadoLunch.");
        texto.setFont(Estilo.NORMAL);
        texto.setForeground(Estilo.GRIS);
        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(7));
        encabezado.add(texto);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 16, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("Pedidos de hoy", "—", "Resumen disponible al conectar la BD", Estilo.NARANJA));
        tarjetas.add(tarjeta("En preparación", "—", "Seguimiento de pedidos", Estilo.ROSA));
        tarjetas.add(tarjeta("Ventas del día", "—", "Total registrado", new Color(214, 157, 91)));

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(tarjetas, BorderLayout.NORTH);

        JPanel bienvenida = new JPanel(new BorderLayout(10, 10));
        bienvenida.setBackground(Color.WHITE);
        bienvenida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(244, 222, 211)),
                new EmptyBorder(24, 25, 24, 25)));
        JLabel subt = new JLabel("Tu negocio, organizado desde un solo lugar");
        subt.setFont(Estilo.SUBTITULO);
        subt.setForeground(Estilo.TEXTO);
        JTextArea descripcion = new JTextArea(
                "Desde este panel podrás administrar el menú de desayunos, consultar pedidos, "
                        + "revisar clientes y organizar las entregas. Selecciona una sección en el menú lateral "
                        + "para comenzar.");
        descripcion.setWrapStyleWord(true);
        descripcion.setLineWrap(true);
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setFont(Estilo.NORMAL);
        descripcion.setForeground(Estilo.GRIS);
        bienvenida.add(subt, BorderLayout.NORTH);
        bienvenida.add(descripcion, BorderLayout.CENTER);
        centro.add(bienvenida, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel tarjeta(String titulo, String valor, String detalle, Color acento) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, acento),
                new EmptyBorder(18, 18, 18, 18)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo);
        t.setForeground(Estilo.GRIS);
        t.setFont(Estilo.NORMAL);
        JLabel v = new JLabel(valor);
        v.setForeground(Estilo.TEXTO);
        v.setFont(new Font("SansSerif", Font.BOLD, 29));
        JLabel d = new JLabel(detalle);
        d.setForeground(Estilo.GRIS);
        d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(t); p.add(Box.createVerticalStrut(10)); p.add(v);
        p.add(Box.createVerticalStrut(5)); p.add(d);
        return p;
    }
}

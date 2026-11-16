package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelInformativo extends JPanel {
    public PanelInformativo(String titulo, String descripcion) {
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(38, 40, 30, 40));
        setLayout(new BorderLayout(0, 18));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(Estilo.TITULO);
        encabezado.setForeground(Estilo.TEXTO);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(new EmptyBorder(25, 25, 25, 25));
        JLabel mensaje = new JLabel("<html><div style='width:500px'>" + descripcion
                + "<br><br>Este módulo queda preparado para integrarse con las clases y tablas correspondientes.</div></html>");
        mensaje.setFont(Estilo.NORMAL);
        mensaje.setForeground(Estilo.GRIS);
        contenido.add(mensaje, BorderLayout.NORTH);

        add(encabezado, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }
}

package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class Estilo {
    public static final Color NARANJA = new Color(239, 126, 73);
    public static final Color NARANJA_OSCURO = new Color(190, 82, 48);
    public static final Color ROSA = new Color(225, 133, 153);
    public static final Color ROSA_CLARO = new Color(255, 237, 239);
    public static final Color CREMA = new Color(255, 248, 242);
    public static final Color LATERAL = new Color(255, 239, 230);
    public static final Color TEXTO = new Color(74, 54, 48);
    public static final Color GRIS = new Color(120, 110, 106);
    public static final Color BLANCO = Color.WHITE;
    public static final Color BORDE = new Color(239, 218, 207);
    public static final Color FILA_ALTERNA = new Color(255, 246, 240);
    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 25);
    public static final Font SUBTITULO = new Font("SansSerif", Font.BOLD, 17);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);

    private Estilo() {}

    public static void configurarUI() {
        UIManager.put("Button.background", NARANJA);
        UIManager.put("Button.foreground", BLANCO);
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 12));
        UIManager.put("Button.focus", new Color(255, 210, 190));
        UIManager.put("Table.font", NORMAL);
        UIManager.put("Table.foreground", TEXTO);
        UIManager.put("Table.background", BLANCO);
        UIManager.put("Table.selectionBackground", new Color(255, 222, 205));
        UIManager.put("Table.selectionForeground", TEXTO);
        UIManager.put("Table.gridColor", BORDE);
        UIManager.put("TableHeader.background", new Color(255, 230, 216));
        UIManager.put("TableHeader.foreground", NARANJA_OSCURO);
        UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 13));
    }

    public static JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setUI(new BasicButtonUI());
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(BLANCO);
        b.setBackground(color);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 15, 10, 15));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(NORMAL);
        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);
        tabla.setSelectionBackground(new Color(255, 222, 205));
        tabla.setSelectionForeground(TEXTO);
        tabla.setGridColor(BORDE);
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(255, 230, 216));
        header.setForeground(NARANJA_OSCURO);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value,
                                                                     boolean selected, boolean focus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, selected, focus, row, column);
                if (!selected) c.setBackground(row % 2 == 0 ? BLANCO : FILA_ALTERNA);
                setBorder(new EmptyBorder(0, 9, 0, 9));
                return c;
            }
        });
    }
}

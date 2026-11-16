package vista;

import modelo.Desayuno;

import javax.swing.*;
import java.awt.*;

public class FormularioDesayuno {
    private final Component padre;

    public FormularioDesayuno(Component padre) {
        this.padre = padre;
    }

    public Desayuno mostrar(Desayuno original) {
        JTextField nombre = new JTextField(original == null ? "" : original.getNombre(), 22);
        JTextField precio = new JTextField(original == null ? "" : String.valueOf(original.getPrecio()), 22);
        JTextArea descripcion = new JTextArea(original == null ? "" : original.getDescripcion(), 3, 22);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        JCheckBox disponible = new JCheckBox("Disponible", original == null || original.isDisponible());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);
        panel.add(new JLabel("Precio:"));
        panel.add(precio);
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(descripcion));
        panel.add(disponible);

        while (true) {
            int resultado = JOptionPane.showConfirmDialog(padre, panel,
                    original == null ? "Nuevo desayuno" : "Editar desayuno",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resultado != JOptionPane.OK_OPTION) return null;
            try {
                String n = nombre.getText().trim();
                double p = Double.parseDouble(precio.getText().trim());
                if (n.isEmpty() || p < 0) throw new IllegalArgumentException();
                Desayuno d = new Desayuno(n, p, descripcion.getText().trim(), disponible.isSelected());
                if (original != null) d.setId(original.getId());
                return d;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(padre,
                        "Escribe un nombre y un precio numérico válido.",
                        "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}

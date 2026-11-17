package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final JPanel contenido=new JPanel(new CardLayout()); private final CardLayout tarjetas=(CardLayout)contenido.getLayout();
    public VentanaPrincipal(){setTitle("VocadoLunch | Administración");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(1180,760);setMinimumSize(new Dimension(980,650));setLocationRelativeTo(null);setLayout(new BorderLayout());
        add(crearLateral(),BorderLayout.WEST);contenido.setBackground(Estilo.CREMA);
        contenido.add(new PanelInicio(),"inicio");
        contenido.add(new PanelDesayunos(),"desayunos");
        contenido.add(new PanelPedidos(),"pedidos");
        contenido.add(new PanelClientes(),"clientes");
        contenido.add(new PanelRepartidores(),"repartidores");
        contenido.add(new PanelVentas(),"ventas");
        add(contenido,BorderLayout.CENTER);mostrar("inicio");
    }
    private JPanel crearLateral(){JPanel p=new JPanel();p.setPreferredSize(new Dimension(225,0));p.setBackground(new Color(255,239,230));p.setBorder(new EmptyBorder(22,15,20,15));p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel logo=new JLabel("VocadoLunch");logo.setFont(new Font("SansSerif",Font.BOLD,23));logo.setForeground(Estilo.NARANJA_OSCURO);p.add(logo);
        JLabel img=logoImagen();p.add(Box.createVerticalStrut(12));p.add(img);p.add(Box.createVerticalStrut(24));
        for(String[] item:new String[][]{{"Inicio","inicio"},{"Desayunos","desayunos"},{"Pedidos","pedidos"},{"Clientes","clientes"},{"Repartidores","repartidores"},{"Pagos y ventas","ventas"}}){JButton b=new JButton(item[0]);b.setAlignmentX(Component.LEFT_ALIGNMENT);b.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));b.setHorizontalAlignment(SwingConstants.LEFT);b.setFocusPainted(false);b.setBackground(new Color(255,248,242));b.addActionListener(e->mostrar(item[1]));p.add(b);p.add(Box.createVerticalStrut(7));}
        p.add(Box.createVerticalGlue());p.add(new JLabel("Proyecto escolar"));return p;
    }
    private JLabel logoImagen(){java.net.URL url=getClass().getResource("/logo.png");if(url==null)return new JLabel("logo.png pendiente");ImageIcon icon=new ImageIcon(url);Image im=icon.getImage().getScaledInstance(155,90,Image.SCALE_SMOOTH);return new JLabel(new ImageIcon(im));}
    private void mostrar(String nombre){tarjetas.show(contenido,nombre);}
}

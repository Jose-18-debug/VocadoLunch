package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class PanelClientes extends JPanel {

    private final UsuarioDAO dao=new UsuarioDAO();
    private final DefaultTableModel tm=new DefaultTableModel(new Object[]{"ID","Nombre","Teléfono","Dirección","Correo"},0){public boolean isCellEditable(int r,int c){return false;}};
    public PanelClientes(){setLayout(new BorderLayout(12,12));setBackground(Estilo.CREMA);setBorder(new EmptyBorder(28,30,25,30));
        JLabel title=new JLabel("Clientes");title.setFont(Estilo.TITULO);title.setForeground(Estilo.TEXTO);
        JTable table=new JTable(tm);table.setRowHeight(28);add(title,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);
        JButton add=new JButton("Registrar cliente"), refresh=new JButton("Actualizar");
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);actions.add(add);actions.add(refresh);add(actions,BorderLayout.SOUTH);
        add.addActionListener(e->registrar());refresh.addActionListener(e->cargar());cargar();
    }
    private void cargar(){tm.setRowCount(0);try{for(Usuario u:dao.listarClientes())tm.addRow(new Object[]{u.getId(),u.getNombre(),u.getTelefono(),u.getDireccion(),u.getCorreo()});}
    catch(SQLException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error MySQL",JOptionPane.ERROR_MESSAGE);}}
    private void registrar(){
        JTextField n=new JTextField(),t=new JTextField(),d=new JTextField(),c=new JTextField();
        JPanel p=new JPanel(new GridLayout(0,1,5,5));p.add(new JLabel("Nombre"));p.add(n);p.add(new JLabel("Teléfono (solo números)"));p.add(t);p.add(new JLabel("Dirección"));p.add(d);p.add(new JLabel("Correo"));p.add(c);
        if(JOptionPane.showConfirmDialog(this,p,"Nuevo cliente",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        if(n.getText().isBlank()||!t.getText().trim().matches("\\d{7,15}")){JOptionPane.showMessageDialog(this,"Escribe nombre y teléfono válido.");return;}
        try{dao.insertarCliente(n.getText().trim(),t.getText().trim(),d.getText().trim(),c.getText().trim());cargar();}
        catch(SQLException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error MySQL",JOptionPane.ERROR_MESSAGE);}
    }
}

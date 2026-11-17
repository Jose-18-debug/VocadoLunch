package vista;

import dao.DesayunoDAO;
import modelo.Desayuno;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelDesayunos extends JPanel {

    private final DesayunoDAO dao=new DesayunoDAO();
    private final DefaultTableModel tm=new DefaultTableModel(new Object[]{"N.º","ID","Nombre","Precio","Descripción","Disponible"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(tm);
    public PanelDesayunos(){setLayout(new BorderLayout(0,16));setBackground(Estilo.CREMA);setBorder(new EmptyBorder(28,30,24,30));
        JLabel title=new JLabel("Desayunos");title.setFont(Estilo.TITULO);title.setForeground(Estilo.TEXTO);
        JLabel desc=new JLabel("Administra el menú. Los IDs reales se conservan aunque desactives un producto.");desc.setFont(Estilo.NORMAL);desc.setForeground(Estilo.GRIS);
        JPanel head=new JPanel();head.setOpaque(false);head.setLayout(new BoxLayout(head,BoxLayout.Y_AXIS));head.add(title);head.add(Box.createVerticalStrut(5));head.add(desc);
        Estilo.estilizarTabla(tabla);
        add(head,BorderLayout.NORTH);add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton add=Estilo.boton("＋ Agregar desayuno",Estilo.NARANJA),edit=Estilo.boton("Editar",Estilo.ROSA),disable=Estilo.boton("Desactivar",new Color(174,91,85)),refresh=Estilo.boton("↻ Actualizar",new Color(120,130,120));
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);actions.add(add);actions.add(edit);actions.add(disable);actions.add(refresh);add(actions,BorderLayout.SOUTH);
        add.addActionListener(e->agregar());edit.addActionListener(e->editar());disable.addActionListener(e->desactivar());refresh.addActionListener(e->cargar());cargar();
    }
    private void cargar(){tm.setRowCount(0);try{List<Desayuno> lista=dao.listar();int n=1;for(Desayuno d:lista)tm.addRow(new Object[]{n++,d.getId(),d.getNombre(),String.format("$%.2f",d.getPrecio()),d.getDescripcion(),d.isDisponible()?"Sí":"No"});}
    catch(SQLException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error MySQL",JOptionPane.ERROR_MESSAGE);}}
    private Desayuno seleccionado(){int r=tabla.getSelectedRow();if(r<0)throw new IllegalArgumentException("Selecciona un desayuno.");int id=(int)tm.getValueAt(r,1);
        return new Desayuno(id,tm.getValueAt(r,2).toString(),Double.parseDouble(tm.getValueAt(r,3).toString().replace("$","")),tm.getValueAt(r,4).toString(),"","",tm.getValueAt(r,5).equals("Sí"));}
    private void agregar(){Desayuno d=new FormularioDesayuno(this).mostrar(null);if(d==null)return;try{dao.insertar(d);cargar();}catch(SQLException e){error(e);}}
    private void editar(){try{Desayuno d=seleccionado();Desayuno edit=new FormularioDesayuno(this).mostrar(d);if(edit!=null){dao.actualizar(edit);cargar();}}catch(Exception e){error(e);}}
    private void desactivar(){try{Desayuno d=seleccionado();if(JOptionPane.showConfirmDialog(this,"El producto dejará de aparecer en el menú, pero se conservará para el historial. ¿Continuar?","Desactivar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){dao.eliminar(d.getId());cargar();}}
    catch(Exception e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Aviso",JOptionPane.WARNING_MESSAGE);}
}

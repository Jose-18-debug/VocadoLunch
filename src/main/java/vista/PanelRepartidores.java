package vista;

import dao.PedidoDAO;
import dao.UsuarioDAO;
import modelo.Pedido;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelRepartidores extends JPanel {
    private final PedidoDAO pedidosDAO=new PedidoDAO(); private final UsuarioDAO usuariosDAO=new UsuarioDAO();
    private final DefaultTableModel tm=new DefaultTableModel(new Object[]{"Folio","Cliente","Repartidor ID","Fecha","Total","Estado"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(tm);
    public PanelRepartidores(){setLayout(new BorderLayout(10,10));setBackground(Estilo.CREMA);setBorder(new EmptyBorder(25,28,22,28));
        JLabel title=new JLabel("Repartidores y entregas");title.setFont(Estilo.TITULO);title.setForeground(Estilo.TEXTO);tabla.setRowHeight(28);add(title,BorderLayout.NORTH);add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton estado=new JButton("Cambiar estado"),asignar=new JButton("Asignar repartidor"),actualizar=new JButton("Actualizar");
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT));p.setOpaque(false);p.add(estado);p.add(asignar);p.add(actualizar);add(p,BorderLayout.SOUTH);
        estado.addActionListener(e->cambiarEstado());asignar.addActionListener(e->asignar());actualizar.addActionListener(e->cargar());cargar();
    }
    private void cargar(){tm.setRowCount(0);try{for(Pedido p:pedidosDAO.listarPedidos())tm.addRow(new Object[]{p.getIdPedidos(),p.getNombreCliente(),p.getIdRepartidor()==0?"Sin asignar":p.getIdRepartidor(),p.getFechaHora(),String.format("$%.2f",p.getTotal()),p.getEstado()});}
    catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error MySQL",JOptionPane.ERROR_MESSAGE);}}
    private int pedidoSeleccionado(){int r=tabla.getSelectedRow();if(r<0)throw new IllegalArgumentException("Selecciona un pedido de la tabla.");return Integer.parseInt(tm.getValueAt(r,0).toString());}
    private void cambiarEstado(){try{int id=pedidoSeleccionado();String[] opts={"Recibido","En preparación","En camino","Entregado"};String v=(String)JOptionPane.showInputDialog(this,"Nuevo estado:","Estado",JOptionPane.QUESTION_MESSAGE,null,opts,opts[0]);if(v!=null&&pedidosDAO.actualizarEstado(id,v))cargar();}
    catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Aviso",JOptionPane.WARNING_MESSAGE);}}
    private void asignar(){try{int id=pedidoSeleccionado();List<Usuario> lista=usuariosDAO.listarRepartidores();if(lista.isEmpty()){JOptionPane.showMessageDialog(this,"No hay usuarios con rol Repartidor.");return;}
        JComboBox<Usuario> combo=new JComboBox<>(lista.toArray(new Usuario[0]));if(JOptionPane.showConfirmDialog(this,combo,"Selecciona repartidor",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){Usuario u=(Usuario)combo.getSelectedItem();if(u!=null&&pedidosDAO.asignarRepartidor(id,u.getId()))cargar();}}
    catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Aviso",JOptionPane.WARNING_MESSAGE);}}
}

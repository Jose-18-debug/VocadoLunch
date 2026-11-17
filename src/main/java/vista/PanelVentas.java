package vista;

import dao.PedidoDAO;
import modelo.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PanelVentas extends JPanel {

    private final PedidoDAO dao=new PedidoDAO();
    private final DefaultTableModel tm=new DefaultTableModel(new Object[]{"Folio","Cliente ID","Fecha","Total","Método","Estado pedido","Pago"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(tm); private final JLabel resumen=new JLabel("Ventas de hoy: —");
    public PanelVentas(){setLayout(new BorderLayout(12,12));setBackground(Estilo.CREMA);setBorder(new EmptyBorder(25,28,22,28));
        JLabel title=new JLabel("Pagos e historial de ventas");title.setFont(Estilo.TITULO);title.setForeground(Estilo.TEXTO);
        tabla.setRowHeight(28);resumen.setFont(Estilo.SUBTITULO);
        JPanel north=new JPanel(new BorderLayout());north.setOpaque(false);north.add(title,BorderLayout.NORTH);north.add(resumen,BorderLayout.SOUTH);add(north,BorderLayout.NORTH);add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton confirmar=new JButton("Confirmar pago"),actualizar=new JButton("Actualizar");
        JPanel south=new JPanel(new FlowLayout(FlowLayout.LEFT));south.setOpaque(false);south.add(confirmar);south.add(actualizar);add(south,BorderLayout.SOUTH);
        confirmar.addActionListener(e->confirmarPago());actualizar.addActionListener(e->cargar());cargar();
    }
    private void cargar(){tm.setRowCount(0);try{
        List<Pedido> pedidos=dao.listarPedidos();
        for(Pedido p:pedidos)tm.addRow(new Object[]{p.getIdPedidos(),p.getNombreCliente()==null?p.getIdCliente():p.getNombreCliente(),p.getFechaHora(),String.format("$%.2f",p.getTotal()),p.getMetodoPago(),p.getEstado(),p.isPagado()?"PAGADO":"PENDIENTE"});
        Map<String,Object> m=dao.resumenVentasHoy();resumen.setText("Pedidos pagados hoy: "+m.get("pedidos")+"   |   Total vendido: "+String.format("$%.2f",m.get("total")));
    }catch(Exception e){JOptionPane.showMessageDialog(this,"No se pudo consultar ventas. Ejecuta primero la migración SQL.\n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}}
    private void confirmarPago(){int row=tabla.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Selecciona un pedido.");return;}
        int id=Integer.parseInt(tm.getValueAt(row,0).toString());if("PAGADO".equals(tm.getValueAt(row,6))){JOptionPane.showMessageDialog(this,"Este pedido ya está pagado.");return;}
        if(JOptionPane.showConfirmDialog(this,"¿Confirmar el pago del pedido "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try{if(dao.confirmarPago(id)){JOptionPane.showMessageDialog(this,"Pago confirmado.");cargar();}}
        catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
}

package vista;

import dao.DesayunoDAO;
import dao.PedidoDAO;
import dao.UsuarioDAO;
import modelo.Desayuno;
import modelo.DetallePedido;
import modelo.Pedido;
import modelo.Usuario;
import javax.swing.*; import javax.swing.table.DefaultTableModel; import javax.swing.border.EmptyBorder;
import java.awt.*; import java.time.LocalDateTime; import java.time.format.DateTimeFormatter;
import java.util.*; import java.util.List;

public class PanelPedidos extends JPanel {
    private final UsuarioDAO usuarioDAO=new UsuarioDAO();
    private final DesayunoDAO desayunoDAO=new DesayunoDAO();
    private final PedidoDAO pedidoDAO=new PedidoDAO();
    private final JComboBox<Usuario> clientes=new JComboBox<>();
    private final JComboBox<Desayuno> desayunos=new JComboBox<>();
    private final JSpinner cantidad=new JSpinner(new SpinnerNumberModel(1,1,99,1));
    private final JComboBox<String> metodo=new JComboBox<>(new String[]{"EFECTIVO","TRANSFERENCIA"});
    private final DefaultTableModel tm=new DefaultTableModel(new Object[]{"ID desayuno","Desayuno","Precio","Cantidad","Subtotal"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabla=new JTable(tm);
    private final Map<Integer,DetallePedido> carrito=new LinkedHashMap<>();
    private final JLabel total=new JLabel("$0.00");

    public PanelPedidos(){
        setLayout(new BorderLayout(12,12));setBackground(Estilo.CREMA);setBorder(new EmptyBorder(25,28,22,28));
        JLabel titulo=new JLabel("Registrar pedido");titulo.setFont(Estilo.TITULO);titulo.setForeground(Estilo.TEXTO);
        JPanel top=new JPanel(new GridLayout(2,4,8,6));top.setOpaque(false);
        top.add(new JLabel("Cliente"));top.add(new JLabel("Desayuno"));top.add(new JLabel("Cantidad"));top.add(new JLabel("Método de pago"));
        top.add(clientes);top.add(desayunos);top.add(cantidad);top.add(metodo);
        JPanel north=new JPanel(new BorderLayout(0,12));north.setOpaque(false);north.add(titulo,BorderLayout.NORTH);north.add(top,BorderLayout.CENTER);
        add(north,BorderLayout.NORTH);
        tabla.setRowHeight(27);add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton agregar=new JButton("Agregar al pedido"),quitar=new JButton("Quitar producto"),guardar=new JButton("Guardar pedido"),limpiar=new JButton("Limpiar"),recargar=new JButton("Recargar listas");
        JPanel south=new JPanel(new BorderLayout());south.setOpaque(false);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));buttons.setOpaque(false);buttons.add(agregar);buttons.add(quitar);buttons.add(limpiar);buttons.add(recargar);buttons.add(guardar);
        total.setFont(new Font("SansSerif",Font.BOLD,20));total.setForeground(Estilo.NARANJA_OSCURO);
        south.add(buttons,BorderLayout.CENTER);south.add(total,BorderLayout.EAST);add(south,BorderLayout.SOUTH);
        agregar.addActionListener(e->agregarProducto());quitar.addActionListener(e->quitarProducto());limpiar.addActionListener(e->limpiar());recargar.addActionListener(e->cargarCombos());guardar.addActionListener(e->guardar());
        cargarCombos();
    }
    private void cargarCombos(){
        try{clientes.removeAllItems();for(Usuario u:usuarioDAO.listarClientes())clientes.addItem(u);
            desayunos.removeAllItems();for(Desayuno d:desayunoDAO.listarDisponibles())desayunos.addItem(d);
            if(clientes.getItemCount()==0) JOptionPane.showMessageDialog(this,"Registra primero al menos un cliente en la sección Clientes.");
        }catch(Exception e){JOptionPane.showMessageDialog(this,"No se pudieron cargar clientes/desayunos: "+e.getMessage(),"MySQL",JOptionPane.ERROR_MESSAGE);}
    }
    private void agregarProducto(){
        Desayuno d=(Desayuno)desayunos.getSelectedItem();if(d==null){JOptionPane.showMessageDialog(this,"No hay desayunos disponibles.");return;}
        int cant=(Integer)cantidad.getValue();DetallePedido existente=carrito.get(d.getId());
        if(existente==null)carrito.put(d.getId(),new DetallePedido(d.getId(),cant,(float)(d.getPrecio()*cant)));
        else {int nueva=existente.getCantidad()+cant;existente.setCantidad(nueva);existente.setSubtotal((float)(d.getPrecio()*nueva));}
        refrescarCarrito();
    }
    private void quitarProducto(){int row=tabla.getSelectedRow();if(row<0)return;int id=(int)tm.getValueAt(row,0);carrito.remove(id);refrescarCarrito();}
    private void refrescarCarrito(){
        tm.setRowCount(0);double suma=0;
        for(DetallePedido d:carrito.values()){
            Desayuno encontrado=null;for(int i=0;i<desayunos.getItemCount();i++)if(desayunos.getItemAt(i).getId()==d.getIdDesayuno())encontrado=desayunos.getItemAt(i);
            String nombre=encontrado==null?"Desayuno #"+d.getIdDesayuno():encontrado.getNombre();
            double precio=encontrado==null?(d.getCantidad()==0?0:d.getSubtotal()/d.getCantidad()):encontrado.getPrecio();
            tm.addRow(new Object[]{d.getIdDesayuno(),nombre,String.format("$%.2f",precio),d.getCantidad(),String.format("$%.2f",d.getSubtotal())});suma+=d.getSubtotal();
        }
        total.setText(String.format("Total: $%.2f",suma));
    }
    private void quitarProductoSeguro(){quitarProducto();}
    private void limpiar(){carrito.clear();refrescarCarrito();}
    private void guardar(){
        Usuario u=(Usuario)clientes.getSelectedItem();if(u==null){JOptionPane.showMessageDialog(this,"Selecciona un cliente registrado.");return;}
        if(carrito.isEmpty()){JOptionPane.showMessageDialog(this,"Agrega al menos un desayuno.");return;}
        Pedido p=new Pedido();p.setIdCliente(u.getId());p.setFechaHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        p.setEstado("Recibido");p.setMetodoPago((String)metodo.getSelectedItem());
        try{int id=pedidoDAO.guardarPedido(p,new ArrayList<>(carrito.values()));
            JOptionPane.showMessageDialog(this,"Pedido registrado correctamente.\nFolio: "+id+"\nTotal: "+total.getText()+"\nPago: pendiente.");
            limpiar();cargarCombos();
        }catch(Exception e){JOptionPane.showMessageDialog(this,"No se pudo guardar el pedido:\n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
}

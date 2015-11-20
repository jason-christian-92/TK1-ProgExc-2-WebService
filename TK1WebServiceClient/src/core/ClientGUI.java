package core;

import iface.IClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import objects.ItemObject;

public class ClientGUI extends JPanel{

	private JTable table;
	private CustomTableModel tblModel;
	
	private JPanel pnlSouth, pnlNorth;
	private JLabel lblStatus;
	private JTable tblCart;
	private CustomTableModel tblModelCart;
	
	private JButton btnBuy;
	private IClient reference;
	
	private ArrayList<CustomCell> customCells = new ArrayList<CustomCell>();
	
	private ActionListener checkListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			CustomCell selected = getCustomCellByButtonClick(e);
			int itemId = selected.getID();
			int amount = selected.getNumSpinnerValue();
			System.out.println("trying to send item "+itemId+"("+amount+") to cart...");
			if (reference !=null) {
				reference.sendItemToCart(itemId, amount);
			}
		}
	};
	
	private ActionListener buyListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("trying to check out...");
			int dgResult = JOptionPane.showConfirmDialog(null, 
					"Do you want to check out?", "Check out confirmation", JOptionPane.YES_NO_OPTION);
			if (dgResult == JOptionPane.YES_OPTION){
				if (reference != null) reference.sendBuyRequest();
			}
		}
	};
	
	private Object[] colNamesForCart = {"ID", "Name", "Amount/Price", "Total"};
	private Object[] colNames = {"ID", "Name", "Price(€)", "Amount", "Order"};
	
	public ClientGUI(IClient client){
		reference = client;
		this.setLayout(new BorderLayout());
		
		tblModel = new CustomTableModel(colNames, 0);
		table = new JTable(tblModel){
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				// TODO Auto-generated method stub
				if (column == 4){
					CustomCell cell = getCustomCellByRowCol(row, column);
					return cell;
				}
				return super.getCellRenderer(row, column);
			}
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				// TODO Auto-generated method stub
				if (column == 4){
					CustomCell cell = getCustomCellByRowCol(row, column);
					try{
						cell.setID((int)tblModel.getValueAt(row, 0));
					} catch (Exception ex){
						ex.printStackTrace();
					}
					return cell;
				}
				return super.getCellEditor(row, column);
			}
			
		};
		table.setModel(tblModel);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		pnlSouth = new JPanel();
		pnlSouth.setLayout(new FlowLayout(FlowLayout.TRAILING));
		btnBuy = new JButton("Check out");
		btnBuy.addActionListener(buyListener);
		pnlSouth.add(btnBuy);
		this.add(pnlSouth, BorderLayout.SOUTH);
		
		pnlNorth = new JPanel();
		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
		lblStatus = new JLabel("Status: Idle");
		tblModelCart = new CustomTableModel(colNamesForCart, 0);
		tblCart = new JTable(tblModelCart);
		tblCart.getColumnModel().getColumn(0).setPreferredWidth(20);
		tblCart.getColumnModel().getColumn(1).setPreferredWidth(200);
		tblCart.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pnlNorth.add(lblStatus);
		pnlNorth.add(tblCart);
		this.add(pnlNorth, BorderLayout.NORTH);
	}
	
	public void setStatus(String text){
		lblStatus.setText("Status: "+text);
	}
	
	private CustomCell getCustomCellByRowCol(int row, int col){
		for (CustomCell cell : customCells){
			if (cell.getRow() == row && cell.getCol() == col)
				return cell;
		}
		CustomCell newCell = new CustomCell();
		newCell.setRowCol(row, col);
		customCells.add(newCell);
		return newCell;
	}
	
	private CustomCell getCustomCellByButtonClick(ActionEvent e){
		for (CustomCell cell : customCells){
			if (e.getSource() == cell.getCheckButton())
				return cell;
		}
		return null;
	}
	
	public void setupCart(ArrayList<ItemObject> items){
		tblModelCart.setRowCount(0);
		if (items == null) return;
		for (ItemObject obj : items){
			tblModelCart.addRow(new Object[]{
					obj.getID(), obj.getName(), obj.getAmount()+" * € "+obj.getPrice(),
					"€ "+(obj.getAmount()*obj.getPrice())
			});
		}
	}
	
	public void setTableContent(ArrayList<ItemObject> items){
		tblModel.setRowCount(0);
		
		for (ItemObject obj : items){
			tblModel.addRow(new Object[]{
					obj.getID(), obj.getName(), obj.getPrice(), obj.getAmount()
			});
		}
	}
	
	private class CustomTableModel extends DefaultTableModel{
		
		public CustomTableModel(Object[] colNames, int rowCount){
			super(colNames, rowCount);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			// TODO Auto-generated method stub
			return (column == 4) ? true : false;
		}
	}
	
	private class CustomCell extends JPanel 
		implements TableCellRenderer, TableCellEditor{

		private int tblLocRow, tblLocCol;
		
		private int id;
		private SpinnerModel model;
		private JSpinner spnNumber;
		private JButton btn;
		
		public CustomCell(){
			init();
		}
		
		public CustomCell(int id){
			this.id = id;
			init();
		}
		
		public void setRowCol(int row, int col){
			tblLocRow = row;
			tblLocCol = col;
		}
		
		public void setID(int id){
			this.id = id;
		}
		
		public int getID(){
			return id;
		}
		
		public int getNumSpinnerValue(){
			return (int)model.getValue();
		}
		
		public int getRow(){
			return tblLocRow;
		}
		
		public int getCol(){
			return tblLocCol;
		}
		
		public JButton getCheckButton(){
			return btn;
		}
		
		private void init(){
			model = new SpinnerNumberModel(0,0,100,1);
			spnNumber = new JSpinner(model);
			btn = new JButton("Send to Cart");
			btn.addActionListener(checkListener);
			this.setLayout(new FlowLayout());
			add(spnNumber);
			add(btn);
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			// TODO Auto-generated method stub
			return this;
		}
		@Override
		public void addCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub	
		}
		@Override
		public void cancelCellEditing() {
			// TODO Auto-generated method stub
		}
		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return model.getValue();
		}
		@Override
		public boolean isCellEditable(EventObject arg0) {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public void removeCellEditorListener(CellEditorListener arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public boolean shouldSelectCell(EventObject arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean stopCellEditing() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public Component getTableCellEditorComponent(JTable arg0, Object arg1,
				boolean arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			return this;
		}
	}
}

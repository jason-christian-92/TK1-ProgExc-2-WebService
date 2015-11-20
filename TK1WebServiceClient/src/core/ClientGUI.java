package core;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import objects.ItemObject;

public class ClientGUI extends JPanel{

	private JTable table;
	private DefaultTableModel tblModel;
	
	private JPanel pnlSouth;
	private JButton btnBuy;
	
	private Object[] colNames = {"ID", "Name", "Price(€)", "Amount", "Order", "Opr."};
	
	public ClientGUI(){
		this.setLayout(new BorderLayout());
		
		tblModel = new DefaultTableModel(colNames, 7);
		table = new JTable(tblModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		pnlSouth = new JPanel();
		pnlSouth.setLayout(new FlowLayout(FlowLayout.TRAILING));
		btnBuy = new JButton("Buy");
		pnlSouth.add(btnBuy);
		this.add(pnlSouth, BorderLayout.SOUTH);
	}
	
	public void setTableContent(ArrayList<ItemObject> items){
		tblModel.setRowCount(0);
		for (ItemObject obj : items){
			tblModel.addRow(new Object[]{obj.getID(), obj.getName(), obj.getPrice(), obj.getAmount()});
		}
	}
	
	
}

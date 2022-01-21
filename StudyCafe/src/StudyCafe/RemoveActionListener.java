package StudyCafe;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class RemoveActionListener implements ActionListener {
	JTable table;
	RemoveActionListener(JTable table) {
		this.table = table;
	}
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row == -1)
			return;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(row);
	}
}

package StudyCafe;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class EditActionListener implements ActionListener {
	JTable table;
	JTextField text1, text2, text3;
	EditActionListener(JTable table, JTextField text1, JTextField text2, JTextField text3) {
		this.table = table;
		this.text1 = text1;
		this.text2 = text2;
		this.text3 = text3;
	}
	
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row == -1)
			return;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setValueAt(text1.getText(), row, 0);
		model.setValueAt(text2.getText(), row, 1);
		model.setValueAt(text3.getText(), row, 2);
		model.setValueAt("X", row, 3);
	}
}

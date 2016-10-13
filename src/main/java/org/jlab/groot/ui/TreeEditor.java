package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeTextFile;

public class TreeEditor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Tree tree;
	JTable table;
	int maxNColumns = 15;
	int maxNRows = 1000;

	public TreeEditor(Tree tree) {
		this.tree = tree;
		init();
	}

	public void setMaxColumns(int cols) {
		this.maxNColumns = cols;
		init();
	}

	public void setMaxRows(int rows) {
		this.maxNRows = rows;
		init();
	}

	private void init() {
		table = new JTable();
		table.setFillsViewportHeight(true);
		
		DefaultTableModel dataModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			List<String> branches = tree.getListOfBranches();

			public int getColumnCount() {
				if (tree.getListOfBranches().size() < maxNColumns) {
					return tree.getListOfBranches().size();
				} else {
					return maxNColumns;
				}
			}

			public int getRowCount() {
				if (tree.getEntries() < maxNRows) {
					return tree.getEntries();
				} else {
					return maxNRows;
				}
			}

			public Object getValueAt(int row, int col) {
				tree.readEntry(row);
				return tree.getBranch(branches.get(col)).getValue().toString();
			}

			@Override
			public String getColumnName(int index) {
				return branches.get(index);
			}
		};

		table.setModel(dataModel);
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(tablePanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		TreeTextFile tree = new TreeTextFile("T");
		//tree.readFile("/Users/gavalian/Desktop/pp_10k.txt");
        tree.readFile("/Users/wphelps/Desktop/GROOTTree/pp_10k.txt");
        System.out.println( " entries = " + tree.getEntries());
        for(int i = 0; i < 10; i++){
            tree.readEntry(i);
            tree.print();
        }
    
        JFrame frame = new JFrame("Test");
        TreeEditor editor = new TreeEditor(tree);
        frame.add(editor);
        frame.pack();
        frame.setVisible(true);
	}

}

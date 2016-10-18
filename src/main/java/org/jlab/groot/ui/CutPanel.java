package org.jlab.groot.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeCut;
import org.jlab.groot.tree.TreeSelector;

public class CutPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TreeCut cut = null;
	Tree tree;
	TreeSelector selector;
	String name = "";
	String cutString = "";
	ArrayList<String> branches;
	
	JTextField cutNameTextField  = new JTextField();
	JTextArea cutTextArea = new JTextArea();
	JTextField cutValueTextField = new JTextField();
	JComboBox cutOperator = new JComboBox();
	String[] operators = {"<",">",">=","<=","=="};
	JComboBox branchVariableSelector = new JComboBox();
	JTextArea textArea = new JTextArea();
    private static enum Mode { INSERT, COMPLETION };
	private Mode mode = Mode.INSERT;
    private static final String COMMIT_ACTION = "commit";


	
	public CutPanel(Tree tree){
		this.tree = tree;
		this.selector = tree.getSelector();
		this.name = "New Cut";
		this.cutString = "Cut expression goes here..."; 
		this.branches = (ArrayList<String>) tree.getListOfBranches();
		init();
	}
	
	public CutPanel(Tree tree, TreeCut cut){
		this.tree = tree;
		this.selector = tree.getSelector();
		this.name = cut.getName();
		this.cutString = cut.getExpression(); 
		this.branches = (ArrayList<String>) tree.getListOfBranches();
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init() {
	
		cutNameTextField.setText(name);
		for(int i=0; i<branches.size(); i++){
			branchVariableSelector.addItem(branches.get(i));
		}
		for(int i=0; i<operators.length; i++){
			cutOperator.addItem(operators[i]);
		}
		JButton addCutButton = new JButton("Add to Cut");
		
		addCutButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		cutTextArea.setPreferredSize(new Dimension(200,100));
		JButton saveButton = new JButton("Apply");

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		cutValueTextField.setColumns(6);

		int gridInt = 0;
		c.gridwidth = 1;
		c.gridy = gridInt++;
		this.add(new JLabel("Cut Name:"),c);
		c.gridwidth = 3;
		this.add(cutNameTextField,c);

		/*
		c.gridy = gridInt++;
		c.gridwidth = 1;
		this.add(branchVariableSelector,c);
		this.add(cutOperator,c);
		this.add(cutValueTextField, c);
		this.add(addCutButton, c);
		*/
		
		c.gridy = gridInt++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 4;
		//c.anchor = GridBagConstraints.HORIZONTAL;
		this.add(cutTextArea, c);
		
		
		c.gridy = gridInt++;
		c.gridx = 3;
		c.gridwidth = 1;
		//c.anchor = GridBagConstraints.EAST;
		this.add(saveButton, c);
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cut!=null){
					cut.setName(cutNameTextField.getText());
					cut.setExpression(cutTextArea.getText());
				}else{
					selector.addCut(new TreeCut(cutNameTextField.getText(),cutTextArea.getText(),branches));
				}
				SwingUtilities.getWindowAncestor(cutNameTextField).dispose();
			}
		});
		
	}
	    
	private void update(){
		
	}
	
}

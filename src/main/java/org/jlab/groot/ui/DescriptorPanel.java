package org.jlab.groot.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeAnalyzer;
import org.jlab.groot.tree.DatasetDescriptor;
import org.jlab.groot.tree.TreeCut;

public class DescriptorPanel extends JPanel {
	int nDim = 1;
	Tree tree;
	TreeAnalyzer treeAnalyzer;
	DatasetDescriptor descriptor = null;
	ArrayList<String> branches;
	ArrayList<String> cutStrings;
	Map<String,TreeCut> cutMap;
	JTextField name = new JTextField();

	JComboBox branchVariableSelectorX = new JComboBox();
	JTextField binTextFieldX = new JTextField();
	JTextField minTextFieldX = new JTextField();
	JTextField maxTextFieldX = new JTextField();
	
	JComboBox branchVariableSelectorY = new JComboBox();
	JTextField binTextFieldY = new JTextField();
	JTextField minTextFieldY = new JTextField();
	JTextField maxTextFieldY = new JTextField();

	DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer,int nDim){
		this.nDim = nDim;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		init();
	}
	
	DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer){
		this.nDim = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		init();
	}
	
	DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer, DatasetDescriptor descriptor){
		this.nDim = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		this.descriptor = descriptor;
		init();
	}
	
	
	private void init(){
		JButton saveAndClose = new JButton("Save and Close");
		cutMap = tree.getSelector().getSelectorCuts();
		cutStrings = new ArrayList<String>();
		Object[] keys = cutMap.keySet().toArray();
		for(int i=0; i<cutMap.keySet().size(); i++){
			cutStrings.add((String)keys[i]);
		}
		branches = (ArrayList<String>) tree.getListOfBranches();
		for(int i=0; i<branches.size(); i++){
			branchVariableSelectorX.addItem(branches.get(i));
			branchVariableSelectorY.addItem(branches.get(i));
		}
		name.setText("");
		name.setColumns(50);
		binTextFieldX.setText("");
		binTextFieldX.setColumns(5);
		minTextFieldX.setText("");
		minTextFieldX.setColumns(5);
		maxTextFieldX.setText("");
		maxTextFieldX.setColumns(5);
		binTextFieldY.setText("");
		binTextFieldY.setColumns(5);
		minTextFieldY.setText("");
		minTextFieldY.setColumns(5);
		maxTextFieldY.setText("");
		maxTextFieldY.setColumns(5);
		if(descriptor!=null){
			name.setText(descriptor.getName());
			name.setColumns(50);
			binTextFieldX.setText(""+descriptor.getNBinsX());
			binTextFieldX.setColumns(5);
			minTextFieldX.setText(""+descriptor.getMinX());
			minTextFieldX.setColumns(5);
			maxTextFieldX.setText(""+descriptor.getMaxX());
			maxTextFieldX.setColumns(5);
			if(nDim>1){
				binTextFieldY.setText(""+descriptor.getNBinsY());
				binTextFieldY.setColumns(5);
				minTextFieldY.setText(""+descriptor.getMinY());
				minTextFieldY.setColumns(5);
				maxTextFieldY.setText(""+descriptor.getMaxY());
				maxTextFieldY.setColumns(5);
			}
		}
		JButton addDescriptor = new JButton("Save and Close");
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		int gridy = 0;
		c.gridy = gridy++;
		this.add(new JLabel("Descriptor Name:"), c);
		this.add(name, c);
		if(nDim==1){
			c.gridy = gridy++;
			this.add(new JLabel("Variable:"), c);
			this.add(branchVariableSelectorX,c);
			c.gridy = gridy++;
			this.add(new JLabel("#Bins:"), c);
			this.add(binTextFieldX,c);
			c.gridy = gridy++;
			this.add(new JLabel("Min:"), c);
			this.add(minTextFieldX,c);
			c.gridy = gridy++;
			this.add(new JLabel("Max:"), c);
			this.add(maxTextFieldX,c);
		}else{
			c.gridy = gridy++;
			this.add(new JLabel("Variable:"), c);
			this.add(branchVariableSelectorX,c);
			this.add(branchVariableSelectorY,c);
			c.gridy = gridy++;
			this.add(new JLabel("#Bins:"), c);
			this.add(binTextFieldX,c);
			this.add(binTextFieldY,c);
			c.gridy = gridy++;
			this.add(new JLabel("Min:"), c);
			this.add(minTextFieldX,c);
			this.add(minTextFieldY,c);
			c.gridy = gridy++;
			this.add(new JLabel("Max:"), c);
			this.add(maxTextFieldX,c);
			this.add(maxTextFieldY,c);
		}
		c.gridy = gridy++;
		this.add(saveAndClose, c);
		
		
		saveAndClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(descriptor==null){
					descriptor =  new DatasetDescriptor(name.getText(),Integer.parseInt(binTextFieldX.getText()), Double.parseDouble(minTextFieldX.getText()),Double.parseDouble(maxTextFieldX.getText()));
					treeAnalyzer.addDescriptor(descriptor);
				}else{
					System.out.println("Nope");
				}
				
				System.out.println("Save and close descriptor!");
				SwingUtilities.getWindowAncestor(name).dispose();

			}
		});
	}
	
	
	
}

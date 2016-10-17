package org.jlab.groot.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

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
	ArrayList<JCheckBox> cutBoxes = new ArrayList<JCheckBox>();
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

	JPanel histogramOptions = new JPanel();
	JPanel cutOptions = new JPanel();

	
	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer,int nDim){
		this.nDim = nDim;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		//init();
	}
	
	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer){
		this.nDim = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		//init();
	}
	/*
	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer, DatasetDescriptor descriptor){
		this.nDim = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		this.descriptor = descriptor;
		init();
	}
	
	private void init(){
		initHistogramOptions();
		initCutOptions();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		int gridy = 0;
		c.gridy = gridy++;
		this.add(histogramOptions,c);
		if(cutStrings!=null&&cutStrings.size()>0){
			c.gridy = gridy++;
			this.add(cutOptions,c);
		}
		
		JButton saveAndClose = new JButton("Apply");
		c.gridy = gridy++;
		this.add(saveAndClose, c);
		saveAndClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(descriptor==null){
					//descriptor =  new DatasetDescriptor(name.getText(),Integer.parseInt(binTextFieldX.getText()), Double.parseDouble(minTextFieldX.getText()),Double.parseDouble(maxTextFieldX.getText()));
					for(int i=0; i<cutBoxes.size(); i++){
						if(cutBoxes.get(i).isSelected()){
							//descriptor.addCut(cutMap.get(cutStrings.get(i)).getExpression());
						}
					}
					treeAnalyzer.addDescriptor(descriptor);
				}else{
					System.out.println("Nope");
				}
				
				System.out.println("Save and close descriptor!");
				SwingUtilities.getWindowAncestor(name).dispose();

			}
		});
		
	}
	private void initCutOptions() {
		if(cutStrings!=null&&cutStrings.size()>0){
			cutOptions.setLayout(new GridBagLayout());
			cutOptions.setBorder(new TitledBorder("Cuts"));
			GridBagConstraints c = new GridBagConstraints();
			int gridy = 0;
			for(int i=0; i<cutStrings.size(); i++){
				System.out.println("Cut "+i+" "+cutStrings.get(i));
				cutBoxes.add(new JCheckBox(cutStrings.get(i)));
				c.gridy=i;
				cutOptions.add(cutBoxes.get(i), c);
			}
		}
	}

	private void initHistogramOptions(){
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
		name.setColumns(15);
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
			name.setColumns(15);
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
		
		histogramOptions.setLayout(new GridBagLayout());
		histogramOptions.setBorder(new TitledBorder("Histogram Options"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		int gridy = 0;
		c.gridy = gridy++;
		
		if(nDim==1){
			histogramOptions.add(new JLabel("Descriptor Name:"), c);
			histogramOptions.add(name, c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableSelectorX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("#Bins:"), c);
			histogramOptions.add(binTextFieldX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Min:"), c);
			histogramOptions.add(minTextFieldX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Max:"), c);
			histogramOptions.add(maxTextFieldX,c);
		}else{
			histogramOptions.add(new JLabel("Descriptor Name:"), c);
			c.gridwidth=2;
			histogramOptions.add(name, c);
			c.gridwidth=1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableSelectorX,c);
			histogramOptions.add(branchVariableSelectorY,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("#Bins:"), c);
			histogramOptions.add(binTextFieldX,c);
			histogramOptions.add(binTextFieldY,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Min:"), c);
			histogramOptions.add(minTextFieldX,c);
			histogramOptions.add(minTextFieldY,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Max:"), c);
			histogramOptions.add(maxTextFieldX,c);
			histogramOptions.add(maxTextFieldY,c);
	
		}
		
		
	}
	
	*/
	
}

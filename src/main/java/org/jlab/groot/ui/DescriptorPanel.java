package org.jlab.groot.ui;

import java.awt.Color;
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
import org.jlab.groot.tree.TreeExpression;

public class DescriptorPanel extends JPanel {
	int type = 1;
	Tree tree;
	TreeAnalyzer treeAnalyzer;
	DatasetDescriptor descriptor = null;
	ArrayList<String> branches;
	ArrayList<String> cutStrings;
	ArrayList<JCheckBox> cutBoxes = new ArrayList<JCheckBox>();
	Map<String,TreeCut> cutMap;
	
	JTextField name = new JTextField();
	JTextField descriptorName = new JTextField();
	JTextField branchVariableFieldX = new JTextField();
	JTextField branchVariableFieldXerr = new JTextField();
	JTextField binTextFieldX = new JTextField();
	JTextField minTextFieldX = new JTextField();
	JTextField maxTextFieldX = new JTextField();
	
	JTextField branchVariableFieldY = new JTextField();
	JTextField branchVariableFieldYerr = new JTextField();
	JTextField binTextFieldY = new JTextField();
	JTextField minTextFieldY = new JTextField();
	JTextField maxTextFieldY = new JTextField();

	JPanel histogramOptions = new JPanel();
	JPanel cutOptions = new JPanel();

	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer, DatasetDescriptor descriptor){
		this.type = descriptor.getDescriptorType();
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		init();
	}
	
	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer,int type){
		this.type = type;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		init();
	}
	
	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer){
		this.type = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		init();
	}
	
	private void init(){
		if(this.descriptor==null){
			this.descriptor = new DatasetDescriptor("Dataset Name", this.type);
		}
		JLabel typeLabel = new JLabel("Dataset Type:");
		String[] types = {"1D Histogram","2D Histogram", "GraphErrors"};
		JComboBox typeComboBox = new JComboBox(types);
		if(this.type==DatasetDescriptor.DESCRIPTOR_H1){
			typeComboBox.setSelectedIndex(0);
		}else if(this.type==DatasetDescriptor.DESCRIPTOR_H2){
			typeComboBox.setSelectedIndex(1);
		}else{
			typeComboBox.setSelectedIndex(2);
		}
		descriptorName = new JTextField(20);
		descriptorName.setText(this.descriptor.getDescName());
		
		JPanel headerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		headerPanel.add(typeLabel,c);
		headerPanel.add(typeComboBox, c);
		c.gridy++;
		headerPanel.add(new JLabel("Dataset Name:"), c);
		headerPanel.add(descriptorName, c);
		headerPanel.setBorder(new TitledBorder("Main"));

		this.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridy = 0;
		c1.weightx = 1.0;
		c1.weighty = 1.0;
		c1.fill = GridBagConstraints.BOTH;
		this.add(headerPanel, c1);
		this.initHistogramOptions();
		c1.gridy++;
		this.add(histogramOptions,c1);
		cutMap = tree.getSelector().getSelectorCuts();
		cutStrings = new ArrayList<String>();
		Object[] keys = cutMap.keySet().toArray();
		for(int i=0; i<cutMap.keySet().size(); i++){
			cutStrings.add((String)keys[i]);
		}
		initDatasetCreationPane();
		initCutOptions();
		if(cutStrings!=null&&cutStrings.size()>0){
			c1.gridy++;
			this.add(this.cutOptions,c1);
		}
		JButton saveAndClose = new JButton("Apply");
		c1.gridy++;
		this.add(saveAndClose,c1);
		saveAndClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//if(descriptor==null){
					if(type==DatasetDescriptor.DESCRIPTOR_H1){
						String expressionx = branchVariableFieldX.getText();
						int binsx = Integer.parseInt(binTextFieldX.getText());
						Double minx = Double.parseDouble(minTextFieldX.getText());
						Double maxx = Double.parseDouble(maxTextFieldX.getText());
						descriptor = new DatasetDescriptor(descriptorName.getText(),binsx,minx,maxx,expressionx,tree);
					}else if(type==DatasetDescriptor.DESCRIPTOR_H2){
						String expressionx = branchVariableFieldX.getText();
						int binsx = Integer.parseInt(binTextFieldX.getText());
						Double minx = Double.parseDouble(minTextFieldX.getText());
						Double maxx = Double.parseDouble(maxTextFieldX.getText());
						String expressiony = branchVariableFieldY.getText();
						int binsy = Integer.parseInt(binTextFieldY.getText());
						Double miny = Double.parseDouble(minTextFieldY.getText());
						Double maxy = Double.parseDouble(maxTextFieldY.getText());
						descriptor = new DatasetDescriptor(descriptorName.getText(),binsx,minx,maxx,binsy,miny,maxy,expressionx+":"+expressiony,tree);
					}else{
						String x = branchVariableFieldX.getText();
						String xerr = branchVariableFieldXerr.getText();
						String y = branchVariableFieldY.getText();
						String yerr = branchVariableFieldYerr.getText();
						if((xerr==""||xerr=="0")&&(yerr==""||yerr=="0")){
							descriptor = new DatasetDescriptor(descriptorName.getText(),x+":"+y,tree);
						}else if((xerr==""||xerr=="0")){
							descriptor = new DatasetDescriptor(descriptorName.getText(),x+":"+y+":"+yerr,tree);
						}else{
							descriptor = new DatasetDescriptor(descriptorName.getText(),x+":"+y+":"+xerr+":"+yerr,tree);
						}
					}
					//descriptor =  new DatasetDescriptor(name.getText(),Integer.parseInt(binTextFieldX.getText()), Double.parseDouble(minTextFieldX.getText()),Double.parseDouble(maxTextFieldX.getText()));
					for(int i=0; i<cutBoxes.size(); i++){
						if(cutBoxes.get(i).isSelected()){
							descriptor.addCut(cutMap.get(cutStrings.get(i)));
						}
					}
					treeAnalyzer.addDescriptor(descriptor);
				/*}else{
					System.out.println("Nope");
				}*/
				
				System.out.println("Save and close descriptor!");
				SwingUtilities.getWindowAncestor(branchVariableFieldX).dispose();

			}
		});

	}
	
	private void initDatasetCreationPane() {
		// TODO Auto-generated method stub
		
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
		
	}*/
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
		branches = (ArrayList<String>) tree.getListOfBranches();
		/*for(int i=0; i<branches.size(); i++){
			branchVariableSelectorX.addItem(branches.get(i));
			branchVariableSelectorY.addItem(branches.get(i));
		}*/
		this.branchVariableFieldX.setText("");
		this.branchVariableFieldY.setText("");
		this.branchVariableFieldXerr.setText("");
		this.branchVariableFieldYerr.setText("");
		branchVariableFieldX.setColumns(10);
		branchVariableFieldXerr.setColumns(10);
		branchVariableFieldY.setColumns(10);
		branchVariableFieldYerr.setColumns(10);
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
		branchVariableFieldX.addActionListener((e)->{
			validateExpression(0);
		});
		branchVariableFieldY.addActionListener((e)->{
			validateExpression(1);
		});
		/*
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
		}*/
		
		histogramOptions.setLayout(new GridBagLayout());
		histogramOptions.setBorder(new TitledBorder("Histogram Options"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		int gridy = 0;
		c.gridy = gridy++;
		
		if(type==DatasetDescriptor.DESCRIPTOR_H1){
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("#Bins:"), c);
			histogramOptions.add(binTextFieldX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Min:"), c);
			histogramOptions.add(minTextFieldX,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Max:"), c);
			histogramOptions.add(maxTextFieldX,c);
		}else if(type==DatasetDescriptor.DESCRIPTOR_H2){
			c.gridwidth=1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX,c);
			histogramOptions.add(branchVariableFieldY,c);
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
		}else{
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX,c);
			histogramOptions.add(branchVariableFieldY,c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable Error:"), c);
			histogramOptions.add(branchVariableFieldXerr,c);
			histogramOptions.add(branchVariableFieldYerr,c);
		}
		
		
	}

	private void validateExpression(int i) {
		if(i==0){
			if(TreeExpression.validateExpression(this.branchVariableFieldX.getText(), this.tree.getListOfBranches())){
				this.branchVariableFieldX.setForeground(Color.GREEN);
			}else{
				this.branchVariableFieldX.setForeground(Color.RED);
			}
		}
		if(i==1){
			if(TreeExpression.validateExpression(this.branchVariableFieldX.getText(), this.tree.getListOfBranches())){
				this.branchVariableFieldY.setForeground(Color.GREEN);
			}else{
				this.branchVariableFieldY.setForeground(Color.RED);
			}
		}
	}
	
	
	
}

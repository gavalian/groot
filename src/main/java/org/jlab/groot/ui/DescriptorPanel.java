package org.jlab.groot.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeAnalyzer;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;
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
	Map<String, TreeCut> cutMap;

	private int iconSizeX = 15;
	private int iconSizeY = 15;
	ImageIcon checkIcon = new ImageIcon();
	ImageIcon xIcon = new ImageIcon();
	boolean initialized = false;
	boolean editMode = false;

	// checkImage = checkImage.getScaledInstance(iconSizeX, iconSizeY,
	// Image.SCALE_SMOOTH);
	// xImage = xImage.getScaledInstance(iconSizeX, iconSizeY,
	// Image.SCALE_SMOOTH);

	JComboBox branchComboBoxX = new JComboBox();
	JComboBox branchComboBoxY = new JComboBox();
	JLabel validationPlaceHolderX = new JLabel();
	JLabel validationPlaceHolderY = new JLabel();
	JCheckBox previewCheckBox = new JCheckBox("Instant Preview");
	JCheckBox estimateCheckBox = new JCheckBox("Estimate Range/Binning");

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
	JPanel leftPanel = new JPanel();
	EmbeddedCanvas previewCanvas = new EmbeddedCanvas();

	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer, DatasetDescriptor descriptor) {
		editMode=true;
		this.type = descriptor.getDescriptorType();
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		if(this.type==DatasetDescriptor.DESCRIPTOR_H1){
			previewCheckBox.setSelected(true);
		}else{
			previewCheckBox.setSelected(false);
		}
		this.estimateCheckBox.setSelected(false);
		init();
		if(this.type==DatasetDescriptor.DESCRIPTOR_H1){
			this.branchVariableFieldX.setText(descriptor.getExpressionX());
			this.binTextFieldX.setText(""+descriptor.getNbinsX());
			this.minTextFieldX.setText(""+descriptor.getMinX());
			this.maxTextFieldX.setText(""+descriptor.getMaxX());
			this.descriptorName.setText(""+descriptor.getDescName());
			this.validateExpression(0);
		}else if(this.type==DatasetDescriptor.DESCRIPTOR_H2){
			this.descriptorName.setText(""+descriptor.getDescName());
			this.branchVariableFieldX.setText(descriptor.getExpressionX());
			this.binTextFieldX.setText(""+descriptor.getNbinsX());
			this.minTextFieldX.setText(""+descriptor.getMinX());
			this.maxTextFieldX.setText(""+descriptor.getMaxX());
			this.branchVariableFieldY.setText(descriptor.getExpressionY());
			this.binTextFieldY.setText(""+descriptor.getNbinsY());
			this.minTextFieldY.setText(""+descriptor.getMinY());
			this.maxTextFieldY.setText(""+descriptor.getMaxY());
			this.validateExpression(0);
			this.validateExpression(1);
		}
		descriptorName.setEditable(false);
		
	}

	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer, int type) {
		this.type = type;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		if(this.type==DatasetDescriptor.DESCRIPTOR_H1){
			previewCheckBox.setSelected(true);
		}else{
			previewCheckBox.setSelected(false);
		}		
		estimateCheckBox.setSelected(true);
		init();
	}

	public DescriptorPanel(Tree tree, TreeAnalyzer treeAnalyzer) {
		this.type = 1;
		this.tree = tree;
		this.treeAnalyzer = treeAnalyzer;
		if(this.type==DatasetDescriptor.DESCRIPTOR_H1){
			previewCheckBox.setSelected(true);
		}else{
			previewCheckBox.setSelected(false);
		}		
		estimateCheckBox.setSelected(true);
		init();
	}

	private void init() {
		this.removeAll();
		leftPanel.removeAll();
		this.repaint();
		if(!initialized) previewCheckBox.addActionListener(e -> {System.out.println("init!");init();});
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints cMain = new GridBagConstraints();
		cMain.gridy = 0;
		cMain.weightx = 1.0;
		cMain.weighty = 1.0;
		cMain.fill = GridBagConstraints.BOTH;
		try {
			Image checkImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/general/checkmark.png"));
			Image xImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/general/xmark.png"));
			checkIcon = new ImageIcon(checkImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
			xIcon = new ImageIcon(xImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
		} catch (Exception e) {

		}
		if (this.descriptor == null) {
			this.descriptor = new DatasetDescriptor("Dataset Name", this.type);
		}
		JLabel typeLabel = new JLabel("Dataset Type:");
		String[] types = {"1D Histogram", "2D Histogram", "GraphErrors"};
		JComboBox typeComboBox = new JComboBox(types);
		if (this.type == DatasetDescriptor.DESCRIPTOR_H1) {
			typeComboBox.setSelectedIndex(0);
		} else if (this.type == DatasetDescriptor.DESCRIPTOR_H2) {
			typeComboBox.setSelectedIndex(1);
		} else {
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
		headerPanel.add(typeLabel, c);
		headerPanel.add(typeComboBox, c);
		c.gridy++;
		headerPanel.add(new JLabel("Dataset Name:"), c);
		headerPanel.add(descriptorName, c);
		headerPanel.setBorder(new TitledBorder("Main"));

		this.setLayout(new GridBagLayout());

		leftPanel.add(headerPanel, cMain);
		if(!initialized)this.initHistogramOptions();
		cMain.gridy++;
		leftPanel.add(histogramOptions, cMain);
		cutMap = tree.getSelector().getSelectorCuts();
		cutStrings = new ArrayList<String>();
		Object[] keys = cutMap.keySet().toArray();
		for (int i = 0; i < cutMap.keySet().size(); i++) {
			cutStrings.add((String) keys[i]);
		}
		JPanel previewOptions = new JPanel(new GridBagLayout());
		GridBagConstraints cPreview = new GridBagConstraints();
		cPreview.fill = GridBagConstraints.BOTH;
		cPreview.weightx = 1.0;
		cPreview.weighty = 1.0;
		previewOptions.setBorder(new TitledBorder("Options"));
		// String[] previewOptionsList = {"Cut Preview", "Lines", "Blue/Red"};
		// JComboBox previewOptionBox = new JComboBox(previewOptionsList);
		previewOptions.add(previewCheckBox, cPreview);
		previewOptions.add(estimateCheckBox, cPreview);
		cMain.gridy++;
		leftPanel.add(previewOptions, cMain);
		if(!initialized)initCutOptions();
		if (cutStrings != null && cutStrings.size() > 0) {
			cMain.gridy++;
			leftPanel.add(this.cutOptions, cMain);
		}
		JButton saveAndClose = new JButton("Apply");
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridy = 0;
		c1.weightx = 1.0;
		c1.weighty = 1.0;
		c1.fill = GridBagConstraints.BOTH;
		c1.gridx = 0;
		this.add(leftPanel, c1);
		if(this.previewCheckBox.isSelected()){
			c1.gridx++;
			this.add(previewCanvas, c1);
		}
		c1.gridx = 0;
		c1.gridy++;
		this.add(saveAndClose, c1);
		saveAndClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if(descriptor==null){
				if (type == DatasetDescriptor.DESCRIPTOR_H1) {
					String expressionx = branchVariableFieldX.getText();
					int binsx = Integer.parseInt(binTextFieldX.getText());
					Double minx = Double.parseDouble(minTextFieldX.getText());
					Double maxx = Double.parseDouble(maxTextFieldX.getText());
					//descriptor = new DatasetDescriptor(descriptorName.getText(), binsx, minx, maxx, expressionx, tree);
					descriptor.setDescName(descriptorName.getText());
					descriptor.setNbinsX(binsx);
					descriptor.setMinX(minx);
					descriptor.setMaxX(maxx);
					descriptor.setExpression(expressionx,tree);
					descriptor.initDatasets();

				} else if (type == DatasetDescriptor.DESCRIPTOR_H2) {
					String expressionx = branchVariableFieldX.getText();
					int binsx = Integer.parseInt(binTextFieldX.getText());
					Double minx = Double.parseDouble(minTextFieldX.getText());
					Double maxx = Double.parseDouble(maxTextFieldX.getText());
					String expressiony = branchVariableFieldY.getText();
					int binsy = Integer.parseInt(binTextFieldY.getText());
					Double miny = Double.parseDouble(minTextFieldY.getText());
					Double maxy = Double.parseDouble(maxTextFieldY.getText());
					descriptor = new DatasetDescriptor(descriptorName.getText(), binsx, minx, maxx, binsy, miny, maxy,
							expressionx + ":" + expressiony, tree);
				} else {
					String x = branchVariableFieldX.getText();
					String xerr = branchVariableFieldXerr.getText();
					String y = branchVariableFieldY.getText();
					String yerr = branchVariableFieldYerr.getText();
					if ((xerr == "" || xerr == "0") && (yerr == "" || yerr == "0")) {
						descriptor = new DatasetDescriptor(descriptorName.getText(), x + ":" + y, tree);
					} else if ((xerr == "" || xerr == "0")) {
						descriptor = new DatasetDescriptor(descriptorName.getText(), x + ":" + y + ":" + yerr, tree);
					} else {
						descriptor = new DatasetDescriptor(descriptorName.getText(),
								x + ":" + y + ":" + xerr + ":" + yerr, tree);
					}
				}
				descriptor.clearCuts();
				for (int i = 0; i < cutBoxes.size(); i++) {
					if (cutBoxes.get(i).isSelected()) {
						descriptor.addCut(cutMap.get(cutStrings.get(i)));
						//System.out.println("Adding Cut"+cutMap.get(cutStrings.get(i)).getName()+" "+cutMap.get(cutStrings.get(i)).getExpression());
					}
				}
				if(!editMode){
					treeAnalyzer.addDescriptor(descriptor);
				}else{
					treeAnalyzer.updateDescriptor(descriptor);
					System.out.println("Update descriptor"+descriptor.getDescName());
				}

				System.out.println("Save and close descriptor!");
				SwingUtilities.getWindowAncestor(branchVariableFieldX).dispose();

			}
		});
		try{
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			topFrame.pack();
		}catch(Exception e){
			
		}
		initialized = true;
	}

	private void initCutOptions() {
		if (cutStrings != null && cutStrings.size() > 0) {
			cutOptions.setLayout(new GridBagLayout());
			cutOptions.setBorder(new TitledBorder("Cuts"));
			GridBagConstraints c = new GridBagConstraints();
			int gridy = 0;
			for (int i = 0; i < cutStrings.size(); i++) {
				System.out.println("Cut " + i + " " + cutStrings.get(i));
				cutBoxes.add(new JCheckBox(cutStrings.get(i)));
				cutBoxes.get(i).addActionListener((e) -> {
					this.validateExpression(0);
					this.validateExpression(1);
				});
				c.gridy = i;
				cutOptions.add(cutBoxes.get(i), c);
			}
		}
	}

	private void initHistogramOptions() {
		branches = (ArrayList<String>) tree.getListOfBranches();
		/*
		 * for(int i=0; i<branches.size(); i++){
		 * branchVariableSelectorX.addItem(branches.get(i));
		 * branchVariableSelectorY.addItem(branches.get(i)); }
		 */
		branchComboBoxX = new JComboBox(tree.getListOfBranches().toArray());
		branchComboBoxX.setMaximumSize(new Dimension(52, branchComboBoxX.getPreferredSize().height));
		branchComboBoxX.setPreferredSize(new Dimension(52, branchComboBoxX.getPreferredSize().height));
		branchComboBoxX.setMinimumSize(new Dimension(52, branchComboBoxX.getPreferredSize().height));
		branchComboBoxX.addActionListener((e) -> {
			branchVariableFieldX.setText(branchVariableFieldX.getText() + branchComboBoxX.getSelectedItem());
			this.validateExpression(0);
		});
		branchComboBoxY = new JComboBox(tree.getListOfBranches().toArray());
		branchComboBoxY.setMaximumSize(new Dimension(52, branchComboBoxY.getPreferredSize().height));
		branchComboBoxY.setPreferredSize(new Dimension(52, branchComboBoxY.getPreferredSize().height));
		branchComboBoxY.setMinimumSize(new Dimension(52, branchComboBoxY.getPreferredSize().height));
		branchComboBoxY.addActionListener((e) -> {
			branchVariableFieldY.setText(branchVariableFieldY.getText() + branchComboBoxY.getSelectedItem());
			this.validateExpression(1);
		});
		KeyListener binningListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				estimateCheckBox.setSelected(false);
				validateExpression(0);
				if (type == DatasetDescriptor.DESCRIPTOR_H2) {
					validateExpression(1);
				}
			}

		};
		/*this.branchVariableFieldX.setText("");
		this.branchVariableFieldY.setText("");
		this.branchVariableFieldXerr.setText("");
		this.branchVariableFieldYerr.setText("");*/
		branchVariableFieldX.setColumns(10);
		branchVariableFieldXerr.setColumns(10);
		branchVariableFieldY.setColumns(10);
		branchVariableFieldYerr.setColumns(10);
		//binTextFieldX.setText("");
		binTextFieldX.setColumns(5);
		binTextFieldX.addKeyListener(binningListener);
		//minTextFieldX.setText("");
		minTextFieldX.setColumns(5);
		minTextFieldX.addKeyListener(binningListener);
		//maxTextFieldX.setText("");
		maxTextFieldX.setColumns(5);
		maxTextFieldX.addKeyListener(binningListener);
		//binTextFieldY.setText("");
		binTextFieldY.setColumns(5);
		binTextFieldY.addKeyListener(binningListener);
		//minTextFieldY.setText("");
		minTextFieldY.setColumns(5);
		minTextFieldY.addKeyListener(binningListener);
		//maxTextFieldY.setText("");
		maxTextFieldY.setColumns(5);
		maxTextFieldY.addKeyListener(binningListener);
		branchVariableFieldX.addActionListener((e) -> {
			validateExpression(0);
		});
		branchVariableFieldY.addActionListener((e) -> {
			validateExpression(1);
		});
		branchVariableFieldX.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("key pressed:" + e.getKeyChar() + " " +
				// branchVariableFieldX.getText());
				validateExpression(0);

			}

		});
		branchVariableFieldY.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				validateExpression(1);
			}

		});
		/*
		 * if(descriptor!=null){ name.setText(descriptor.getName());
		 * name.setColumns(15);
		 * binTextFieldX.setText(""+descriptor.getNBinsX());
		 * binTextFieldX.setColumns(5);
		 * minTextFieldX.setText(""+descriptor.getMinX());
		 * minTextFieldX.setColumns(5);
		 * maxTextFieldX.setText(""+descriptor.getMaxX());
		 * maxTextFieldX.setColumns(5); if(nDim>1){
		 * binTextFieldY.setText(""+descriptor.getNBinsY());
		 * binTextFieldY.setColumns(5);
		 * minTextFieldY.setText(""+descriptor.getMinY());
		 * minTextFieldY.setColumns(5);
		 * maxTextFieldY.setText(""+descriptor.getMaxY());
		 * maxTextFieldY.setColumns(5); } }
		 */

		histogramOptions.setLayout(new GridBagLayout());
		histogramOptions.setBorder(new TitledBorder("Histogram Options"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		int gridy = 0;
		c.gridy = gridy++;

		if (type == DatasetDescriptor.DESCRIPTOR_H1) {
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX, c);
			histogramOptions.add(branchComboBoxX, c);
			histogramOptions.add(validationPlaceHolderX, c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("#Bins:"), c);
			c.gridwidth = 3;
			histogramOptions.add(binTextFieldX, c);
			c.gridwidth = 1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Min:"), c);
			c.gridwidth = 3;
			histogramOptions.add(minTextFieldX, c);
			c.gridwidth = 1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Max:"), c);
			c.gridwidth = 3;
			histogramOptions.add(maxTextFieldX, c);
			c.gridwidth = 1;
		} else if (type == DatasetDescriptor.DESCRIPTOR_H2) {
			c.gridwidth = 1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX, c);
			histogramOptions.add(branchComboBoxX, c);
			histogramOptions.add(validationPlaceHolderX, c);
			histogramOptions.add(branchVariableFieldY, c);
			histogramOptions.add(branchComboBoxY, c);
			histogramOptions.add(validationPlaceHolderY, c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("#Bins:"), c);
			c.gridwidth = 3;
			histogramOptions.add(binTextFieldX, c);
			histogramOptions.add(binTextFieldY, c);
			c.gridwidth = 1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Min:"), c);
			c.gridwidth = 3;
			histogramOptions.add(minTextFieldX, c);
			histogramOptions.add(minTextFieldY, c);
			c.gridwidth = 1;
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Max:"), c);
			c.gridwidth = 3;
			histogramOptions.add(maxTextFieldX, c);
			histogramOptions.add(maxTextFieldY, c);
			c.gridwidth = 3;

		} else {
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable:"), c);
			histogramOptions.add(branchVariableFieldX, c);
			histogramOptions.add(branchComboBoxX, c);
			histogramOptions.add(branchVariableFieldY, c);
			histogramOptions.add(branchComboBoxY, c);
			c.gridy = gridy++;
			histogramOptions.add(new JLabel("Variable Error:"), c);
			histogramOptions.add(branchVariableFieldXerr, c);
			histogramOptions.add(branchVariableFieldYerr, c);
		}
		validateExpression(0);
		validateExpression(1);
	}

	private void validateExpression(int i) {
		String cuts = "";
		boolean xPassed = false;
		for (int j = 0; j < cutBoxes.size(); j++) {
			if (cutBoxes.get(j).isSelected()) {
				if (cuts == "") {
					cuts += "("+cutMap.get(cutStrings.get(j)).getExpression()+")";
				} else {
					cuts += "*(" + cutMap.get(cutStrings.get(j)).getExpression()+")";
				}
			}
		}
		//System.out.println("cuts:[" + cuts + "]");
		if (i == 0 || i==1) {
			boolean passed = TreeExpression.validateExpression(this.branchVariableFieldX.getText(),
					this.tree.getListOfBranches());
			if (passed) {
				try {

					this.tree.scanTree(this.branchVariableFieldX.getText(), cuts, 1000, true);
					//System.out.println("Preview: "+ this.branchVariableFieldX.getText()+" "+cuts+" "+1000+" " +true);
					List<DataVector> vecs = this.tree.getScanResults();
					if (this.estimateCheckBox.isSelected()) {
						if (vecs.size() >= 1) {
							this.minTextFieldX.setText(String.format("%4.2f", vecs.get(0).getMin()));
							this.maxTextFieldX.setText(String.format("%4.2f", vecs.get(0).getMax()));
							this.binTextFieldX.setText(String.format("%d", vecs.get(0).getBinSuggestion()));
						}
					}
				} catch (Exception e) {
					passed = false;
				}
			}
			if (!passed) {
				validationPlaceHolderX.setIcon(xIcon);
				validationPlaceHolderX.repaint();
				if (this.estimateCheckBox.isSelected()) {
					this.minTextFieldX.setText("");
					this.maxTextFieldX.setText("");
					this.binTextFieldX.setText("");
				}
				//System.out.println("X Validation Failed");
			} else {
				validationPlaceHolderX.setIcon(checkIcon);
				validationPlaceHolderX.repaint();
				if (this.previewCheckBox.isSelected()) {
					drawPreviewHistogram();
				}
				//System.out.println("X Validation Succeeded");
			}
		}
		if (i == 1) {
			boolean passed = TreeExpression.validateExpression(this.branchVariableFieldY.getText(),
					this.tree.getListOfBranches());
			if (passed) {
				try {
					this.tree.scanTree(this.branchVariableFieldY.getText(), cuts, 1000, true);
					if (this.estimateCheckBox.isSelected()) {
						List<DataVector> vecs = this.tree.getScanResults();
						if (vecs.size() >= 1) {
							this.minTextFieldY.setText(String.format("%4.2f", vecs.get(0).getMin()));
							this.maxTextFieldY.setText(String.format("%4.2f", vecs.get(0).getMax()));
							this.binTextFieldY.setText(String.format("%d", vecs.get(0).getBinSuggestion()));
						}
					}
				} catch (Exception e) {
					passed = false;
				}
			}
			if (!passed) {
				validationPlaceHolderY.setIcon(xIcon);
				validationPlaceHolderY.repaint();
				if (this.estimateCheckBox.isSelected()) {
					this.minTextFieldY.setText("");
					this.maxTextFieldY.setText("");
					this.binTextFieldY.setText("");
				}
				//System.out.println("Y Validation Failed");
			} else {
				validationPlaceHolderY.setIcon(checkIcon);
				validationPlaceHolderY.repaint();
				//System.out.println("Y Validation Succeeded");
			}
		}
	}

	private void drawPreviewHistogram() {

		List<DataVector> vecs = this.tree.getScanResults();
		int bins = Integer.parseInt(binTextFieldX.getText());
		double min = Double.parseDouble(minTextFieldX.getText());
		double max = Double.parseDouble(maxTextFieldX.getText());

		// System.out.println("Histogram "+bins+" "+min+ " "+max);
		H1F htemp = new H1F("PreviewHistogram", bins, min, max);
		// System.out.println("Datavector size"+vecs.get(0).getSize());
		htemp.fill(vecs.get(0),vecs.get(1));
		// TCanvas can = new TCanvas("Blah",500,800);
		// can.draw(htemp);
		// previewCanvas.divide(1, 1);
		previewCanvas.draw(htemp);
		previewCanvas.update();
	}

}

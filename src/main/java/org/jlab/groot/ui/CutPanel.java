package org.jlab.groot.ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.ScrollPane;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.jlab.groot.data.DataVector;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.tree.Tree;
import org.jlab.groot.tree.TreeCut;
import org.jlab.groot.tree.TreeExpression;
import org.jlab.groot.tree.TreeSelector;

public class CutPanel extends JPanel {
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
	private int iconSizeX = 15;
	private int iconSizeY = 15;
	EmbeddedCanvas previewCanvas = new EmbeddedCanvas();
	ScrollPane globalCuts = new ScrollPane();

	ImageIcon checkIcon = new ImageIcon();
	ImageIcon xIcon = new ImageIcon();
	JLabel validationPlaceHolder = null;
	JComboBox branchComboBox = new JComboBox();

	JTextField cutNameTextField = new JTextField();
	JTextField cutTextArea = new JTextField();
	JTextField cutValueTextField = new JTextField();
	JComboBox cutOperator = new JComboBox();
	String[] operators = {"<", ">", ">=", "<=", "=="};
	JComboBox branchVariableSelector = new JComboBox();
	JTextArea textArea = new JTextArea();
	ArrayList<String> cutStrings;
	ArrayList<JCheckBox> cutBoxes = new ArrayList<JCheckBox>();
	Map<String, TreeCut> cutMap;
	JPanel cutOptions = new JPanel();
	JPanel leftPanel = new JPanel();
	boolean editMode = false;
	
	private static enum Mode {
		INSERT, COMPLETION
	};
	private Mode mode = Mode.INSERT;
	private static final String COMMIT_ACTION = "commit";

	public CutPanel(Tree tree) {
		this.tree = tree;
		this.selector = tree.getSelector();
		this.name = "New Cut";
		this.cutString = "";
		this.branches = (ArrayList<String>) tree.getListOfBranches();
		init();
	}

	public CutPanel(Tree tree, TreeCut cut) {
		this.tree = tree;
		this.selector = tree.getSelector();
		this.name = cut.getName();
		this.cutString = cut.getExpression();
		this.branches = (ArrayList<String>) tree.getListOfBranches();
		this.cut = cut;
		editMode = true;
		init();
		this.validateExpression();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		try {
			Image checkImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/general/checkmark.png"));
			Image xImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/general/xmark.png"));
			checkIcon = new ImageIcon(checkImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
			xIcon = new ImageIcon(xImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
		} catch (Exception e) {

		}
		
		cutMap = tree.getSelector().getSelectorCuts();
		cutStrings = new ArrayList<String>();
		Object[] keys = cutMap.keySet().toArray();
		Object[] treeCuts = cutMap.values().toArray();
		for (int i = 0; i < cutMap.keySet().size(); i++) {
			cutStrings.add((String) keys[i]);
		}
		validationPlaceHolder = new JLabel(xIcon);
		cutNameTextField.setText(name);
		for (int i = 0; i < branches.size(); i++) {
			branchVariableSelector.addItem(branches.get(i));
		}
		for (int i = 0; i < operators.length; i++) {
			cutOperator.addItem(operators[i]);
		}
		JButton addCutButton = new JButton("Add to Cut");

		addCutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		//cutTextArea.setPreferredSize(new Dimension(200, 100));
		cutTextArea.setColumns(20);
		cutTextArea.setText(cutString);
		JButton saveButton = new JButton("Apply");
		leftPanel.setLayout(new GridBagLayout());
		//this.setLayout(new GridBagLayout());
		JPanel nameExpressionPanel = new JPanel(new GridBagLayout());
		nameExpressionPanel.setBorder(new TitledBorder("Cut Definition"));
		GridBagConstraints c4 = new GridBagConstraints();
		c4.fill = GridBagConstraints.HORIZONTAL;
		c4.weightx = 1.0;
		c4.weighty = 1.0;
		cutValueTextField.setColumns(6);

		int gridInt = 0;
		c4.gridwidth = 2;
		c4.gridy = gridInt++;
		nameExpressionPanel.add(new JLabel("Cut Name:"), c4);
		c4.gridwidth = 4;
		nameExpressionPanel.add(cutNameTextField, c4);
		branchComboBox = new JComboBox(tree.getListOfBranches().toArray());
		branchComboBox.setMaximumSize(new Dimension(52, branchComboBox.getPreferredSize().height));
		branchComboBox.setPreferredSize(new Dimension(52, branchComboBox.getPreferredSize().height));
		branchComboBox.setMinimumSize(new Dimension(52, branchComboBox.getPreferredSize().height));
		branchComboBox.addActionListener((e) -> {
			cutTextArea.setText(cutTextArea.getText() + branchComboBox.getSelectedItem());
			this.validateExpression();
		});

		c4.gridy = gridInt++;
		c4.fill = GridBagConstraints.HORIZONTAL;
		c4.gridwidth = 2;
		nameExpressionPanel.add(new JLabel("Cut Expression:"), c4);
		c4.gridwidth = 3;
		// c.anchor = GridBagConstraints.HORIZONTAL;
		nameExpressionPanel.add(cutTextArea, c4);
		c4.gridwidth = 1;
		nameExpressionPanel.add(branchComboBox, c4);
		nameExpressionPanel.add(validationPlaceHolder, c4);
		cutTextArea.addKeyListener(new KeyListener() {

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
				validateExpression();

			}

		});
		
		
		JPanel previewOptions = new JPanel(new GridBagLayout());
		GridBagConstraints cPreview = new GridBagConstraints();
		cPreview.fill = GridBagConstraints.BOTH;
		cPreview.weightx = 1.0;
		cPreview.weighty = 1.0;
		previewOptions.setBorder(new TitledBorder("Preview Options"));
		JCheckBox previewCheckBox = new JCheckBox("Show Preview");
		String[] previewOptionsList = {"Cut Preview","Lines","Blue/Red"};
		JComboBox previewOptionBox = new JComboBox(previewOptionsList);
		previewOptions.add(previewCheckBox,cPreview);
		previewOptions.add(previewOptionBox, cPreview);
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridy=0;
		c.gridx=0;
		leftPanel.add(nameExpressionPanel, c);
		initCutOptions();
		if (cutStrings != null && cutStrings.size() > 0) {
			c.gridy++;
			leftPanel.add(this.cutOptions, c);
		}
		c.gridy++;
		//leftPanel.add(previewOptions, c);
		// c.anchor = GridBagConstraints.EAST;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx = 1.0;
		c2.weighty = 1.0;
		c2.gridwidth=2;
		c2.gridy=0;
		//c2.gridx=0;
		this.add(leftPanel, c2);
		//c2.gridx++;
		//this.add(this.previewCanvas, c2);
		c2.gridx=0;
		c2.gridwidth=1;
		c2.gridy++;
		JButton cancelButton = new JButton("Cancel");
		this.add(cancelButton,c2);
		c2.gridx++;
		this.add(saveButton, c2);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cut != null) {
					cut.setName(cutNameTextField.getText());
					cut.setExpression(cutTextArea.getText());
				} else {
					selector.addCut(new TreeCut(cutNameTextField.getText(), cutTextArea.getText(), branches));
				}
				SwingUtilities.getWindowAncestor(cutNameTextField).dispose();
			}
		});

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
					this.validateExpression();
				});
				c.gridy = i;
				cutOptions.add(cutBoxes.get(i), c);
			}
		}
	}
	private void validateExpression() {
		boolean passed = TreeCut.validateExpression(this.cutTextArea.getText(), this.tree.getListOfBranches());
		if (!passed) {
			validationPlaceHolder.setIcon(xIcon);
			validationPlaceHolder.repaint();
		} else {
			validationPlaceHolder.setIcon(checkIcon);
			validationPlaceHolder.repaint();
		}
	}
	
	public void showPreview(){
		
	}

}

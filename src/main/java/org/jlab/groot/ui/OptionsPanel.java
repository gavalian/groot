package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jlab.groot.base.AxisAttributes.AxisAttributesPane;
import org.jlab.groot.base.DatasetAttributes.DatasetAttributesPane;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.Histogram2DPlotter;
import org.jlab.groot.graphics.IDataSetPlotter;

public class OptionsPanel extends JPanel {
	int pad;
	EmbeddedCanvas can = null;
	JTabbedPane tabbedPane = null;
	JTabbedPane datasetPanes = null;
	public OptionsPanel(EmbeddedCanvas can, int pad){
		this.can = can;
		this.pad = pad;
		tabbedPane = new JTabbedPane();
		this.setLayout(new BorderLayout());
		this.add(tabbedPane,BorderLayout.CENTER);
		initMain();
		initAxes();
		initDatasets();
	}
	
	private void initMain() {
		JPanel main = new JPanel();
		main.setLayout(new GridBagLayout());
		main.setMinimumSize(new Dimension(200,300));
		GridBagConstraints c = new GridBagConstraints();
		//main.add(new JLabel("Main Panel"));
		int yGrid = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = yGrid++;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JPanel frameOptions = new JPanel();
		initFrameOptions(frameOptions);
		JPanel generalOptions = new JPanel();
		initAxisOptions(generalOptions);
		main.add(generalOptions,c);
		c.gridy = yGrid++;
		c.weightx = 1.0;
		c.weighty = 1.0;
		main.add(frameOptions,c);
		tabbedPane.add("Main", main);
	}
	private void initAxisOptions(JPanel axisOptions){
		JCheckBox[] applyToAllCheckBoxes;
		RangeSlider xSlider, ySlider;
		String xAxisLabel, yAxisLabel, title;
		String[]   systemFonts = FontProperties.getSystemFontsArray();
		String[]      fontSize = FontProperties.getFontSizeArray();
		axisOptions.setLayout(new BorderLayout());
		JPanel optionsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
		optionsPanel.setBorder(new TitledBorder("Options"));
		String[] lineThickness 	= {"1","2","3","4","5","6","7","8"};
		String[] fillColor 		= {"34","35","36","37","38"};
		String[] colors 		= {"1","2","3","4","5","6","7","8","9"};
		String[] colorNames 	= {"Black","White","Red","Green","Blue","Yellow","Magenta","Cyan","Dark Green","Purple-ish"};
		//String[] fontSize 		= {"6","8","10","12","14","16","18","24","36","48"};
		//int[] fontSizeInts 		= {6,8,10,12,14,16,18,24,36,48};
		String[] translucency = {"100%","80%","60%","40%","20%"};
		
		//JComboBox lineWidthBox = new JComboBox(lineThickness);
		JComboBox fontsBox	= new JComboBox(systemFonts);
		fontsBox.setSelectedIndex(0);
		for(int i=0; i<systemFonts.length; i++){
			if(can.getPad(pad).getAxisX().getTitleFont().getFontName().contains(systemFonts[i])){
				fontsBox.setSelectedIndex(i);
			}
		}
		JComboBox axisFontSizeBox	= new JComboBox(fontSize);
		axisFontSizeBox.setSelectedItem(""+can.getPad(pad).getAxisX().getLabelFontSize());
		JComboBox axisTitleFontSizeBox = new JComboBox(fontSize);
		axisTitleFontSizeBox.setSelectedItem(""+can.getPad(pad).getAxisX().getTitleFontSize());
		JComboBox titleFontSizeBox 	= new JComboBox(fontSize);
		titleFontSizeBox.setSelectedItem(""+can.getPad(pad).getTitleFontSize());
		JComboBox statBoxFontSizeBox 	= new JComboBox(fontSize);
		statBoxFontSizeBox.setSelectedItem(""+can.getPad(pad).getStatBoxFontSize());


		//JComboBox fillColorBox = new JComboBox(fillColor);
		
		//JLabel lineWidthLabel = new JLabel("Line Width");
		JLabel fontLabel 	            = new JLabel("Font:");
		JLabel axisFontSizeLabel     	= new JLabel("Axis Label Font Size:");
		JLabel axisTitleFontSizeLabel 	= new JLabel("Axis Title Font Size:");
		JLabel titleFontSizeLabel 	    = new JLabel("Title Font Size:");
		JLabel statBoxFontSizeLabel 	= new JLabel("Stat Box Font Size:");

		
		JLabel xAxisTitleLabel = new JLabel("X Axis Title:");
		JLabel yAxisTitleLabel = new JLabel("Y Axis Title:");
		JLabel titleLabel      = new JLabel("Title:");

		JTextField xAxisTextField 	= new JTextField(can.getPad(pad).getAxisX().getTitle());
		JTextField yAxisTextField 	= new JTextField(can.getPad(pad).getAxisY().getTitle());
		JTextField titleTextField 	= new JTextField(can.getPad(pad).getTitle());
		/*xAxisTextField.addKeyListener(new KeyListener(){
			 public void keyTyped(KeyEvent e) {
				 canvas.getPad(index).getAxisX().setTitle(xAxisTextField.getText());
					canvas.update();			   
				}
		});*/
		

		
		JPanel gridPanel = new JPanel(new GridLayout(1,2));
		JCheckBox xGridBox = new JCheckBox("Grid X");
		xGridBox.setSelected(can.getPad(pad).getAxisX().getGrid());
	
		JCheckBox yGridBox = new JCheckBox("Grid Y");
		yGridBox.setSelected(can.getPad(pad).getAxisY().getGrid());

		
		gridPanel.add(xGridBox);
		gridPanel.add(yGridBox);
		
		JPanel applyToAllPanel = new JPanel(new BorderLayout());
		String[] applyToAllOptions = {"Font","Title Font Size","Axis Title Font Size","Axis Label Font Size","Stat Box Font Size","Title","X Axis Title","Y Axis Title","Grid X","Grid Y"};
		boolean[] applyToAllDefaults = {true,true,true,true,true,false,false,false,true,true,false,false};
		applyToAllCheckBoxes = new JCheckBox[applyToAllOptions.length];
		for(int i=0; i<applyToAllOptions.length; i++){
			applyToAllCheckBoxes[i] = new JCheckBox(applyToAllOptions[i]);
			applyToAllCheckBoxes[i].setSelected(applyToAllDefaults[i]);
		}
		JComboCheckBox applyToAllComboCheckBox = new JComboCheckBox(applyToAllCheckBoxes);
		JToggleButton applyToAllButton = new JToggleButton("Apply To All");
		ActionListener applyToAllListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(applyToAllButton.isSelected()){
					if(applyToAllCheckBoxes[0].isSelected()){
						can.setFont(systemFonts[fontsBox.getSelectedIndex()]);
					} 
					if(applyToAllCheckBoxes[1].isSelected()){
						can.setTitleSize(Integer.parseInt(fontSize[titleFontSizeBox.getSelectedIndex()]));
					}
					if(applyToAllCheckBoxes[2].isSelected()){
						can.setAxisLabelSize(Integer.parseInt(fontSize[axisFontSizeBox.getSelectedIndex()]));
					} 
					if(applyToAllCheckBoxes[3].isSelected()){
						can.setAxisTitleSize(Integer.parseInt(fontSize[axisTitleFontSizeBox.getSelectedIndex()]));
					}
					if(applyToAllCheckBoxes[4].isSelected()){
						can.setStatBoxFontSize(Integer.parseInt(fontSize[statBoxFontSizeBox.getSelectedIndex()]));
					}
					if(applyToAllCheckBoxes[5].isSelected()){
						can.setPadTitles(titleTextField.getText());
					}
					if(applyToAllCheckBoxes[6].isSelected()){
						can.setPadTitlesX(xAxisTextField.getText());
					}
					if(applyToAllCheckBoxes[7].isSelected()){
						can.setPadTitlesY(yAxisTextField.getText());
					}
					if(applyToAllCheckBoxes[8].isSelected()){
						can.setGridX(xGridBox.isSelected());
					}
					if(applyToAllCheckBoxes[9].isSelected()){
						can.setGridY(yGridBox.isSelected());
					}
					
					/*
					if(applyToAllCheckBoxes[10].isSelected()){
						canvas.getPad(i).setAxisRange("X",xSlider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) +xMin, xSlider.getUpperValue()* (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin);
					}
					if(applyToAllCheckBoxes[11].isSelected()){
						canvas.getPad(i).setAxisRange("Y",ySlider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin) +yMin, ySlider.getUpperValue()* (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin);
					}*/
					can.update();
				}
			}
		};
		ItemListener itemListener = new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));				
			}
			
		};
		for(int i=0; i<applyToAllOptions.length; i++){
			applyToAllCheckBoxes[i].addItemListener(itemListener);
		}
		applyToAllComboCheckBox.addActionListener(applyToAllListener);
		applyToAllButton.addActionListener(applyToAllListener);
		applyToAllPanel.add(applyToAllComboCheckBox,BorderLayout.WEST);
		applyToAllPanel.add(applyToAllButton,BorderLayout.EAST);
		
		fontsBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setFontNameAll(systemFonts[fontsBox.getSelectedIndex()]);
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		
		axisFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setAxisLabelFontSize(Integer.parseInt(fontSize[axisFontSizeBox.getSelectedIndex()]));
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		
		
		axisTitleFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setAxisTitleFontSize(Integer.parseInt(fontSize[axisTitleFontSizeBox.getSelectedIndex()]));
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		
		titleFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setTitleFontSize(Integer.parseInt(fontSize[titleFontSizeBox.getSelectedIndex()]));
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));

			}
		});
		
		statBoxFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setStatBoxFontSize(Integer.parseInt(fontSize[statBoxFontSizeBox.getSelectedIndex()]));
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		xAxisTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).getAxisX().setTitle(xAxisTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		xAxisTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				can.getPad(pad).getAxisX().setTitle(xAxisTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
			
		});
		
		
		yAxisTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).getAxisY().setTitle(yAxisTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		yAxisTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				can.getPad(pad).getAxisY().setTitle(yAxisTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
			
		});
		
		titleTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).setTitle(titleTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		
		titleTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				can.getPad(pad).setTitle(titleTextField.getText());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
			
		});
		
		xGridBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).getAxisX().setGrid(xGridBox.isSelected());
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});
		yGridBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				can.getPad(pad).getAxisY().setGrid(yGridBox.isSelected());
				can.update();
				can.update();
				applyToAllListener.actionPerformed(new ActionEvent("", 0, ""));
			}
		});

		
		int yGrid = 0;
		c.gridy = yGrid++;
		optionsPanel.add(fontLabel,c);
		optionsPanel.add(fontsBox,c);
		c.gridy = yGrid++;
		optionsPanel.add(titleFontSizeLabel,c);
		optionsPanel.add(titleFontSizeBox,c);
		c.gridy = yGrid++;
		optionsPanel.add(axisTitleFontSizeLabel,c);
		optionsPanel.add(axisTitleFontSizeBox,c);
		c.gridy = yGrid++;	
		optionsPanel.add(axisFontSizeLabel,c);
		optionsPanel.add(axisFontSizeBox,c);
		c.gridy = yGrid++;
		optionsPanel.add(statBoxFontSizeLabel,c);
		optionsPanel.add(statBoxFontSizeBox,c);
		c.gridy = yGrid++;
		optionsPanel.add(titleLabel,c);
		optionsPanel.add(titleTextField,c);
		c.gridy = yGrid++;
		optionsPanel.add(titleLabel,c);
		optionsPanel.add(titleTextField,c);
		c.gridy = yGrid++;
		optionsPanel.add(xAxisTitleLabel,c);
		optionsPanel.add(xAxisTextField,c);
		c.gridy = yGrid++;
		optionsPanel.add(yAxisTitleLabel,c);
		optionsPanel.add(yAxisTextField,c);
		c.gridy = yGrid++;
		optionsPanel.add(xGridBox,c);
		optionsPanel.add(yGridBox,c);
		c.gridy = yGrid++;
		optionsPanel.add(applyToAllComboCheckBox,c);
		optionsPanel.add(applyToAllButton,c);
		axisOptions.add(optionsPanel, BorderLayout.PAGE_START);
/*
		JPanel rangePanel = new JPanel(new GridBagLayout());
		GridBagConstraints rangeConstraints = new GridBagConstraints();
		rangeConstraints.fill = GridBagConstraints.HORIZONTAL;
		JPanel xRangePanel =new JPanel(new FlowLayout());
		JPanel yRangePanel =new JPanel(new FlowLayout());

		rangePanel.setBorder(new TitledBorder("Range"));
		JLabel xrangeSliderValue1 = new JLabel(""+String.format("%4.2f",xMin));
		JLabel xrangeSliderValue2 = new JLabel(""+String.format("%4.2f",xMax));
		JLabel xAxisLabel = new JLabel("X:");
		
		//x axis
		xSlider = new RangeSlider();
		xSlider.setMinimum(xSliderMin);
		xSlider.setMaximum(xSliderMax);
		xSlider.setValue(xSliderMin);
		xSlider.setUpperValue(xSliderMax);
		rangeConstraints.gridy=0;
		//rangeConstraints.weightx = 0.5;
		rangePanel.add(xAxisLabel,rangeConstraints);
		rangePanel.add(xrangeSliderValue1,rangeConstraints);
		//rangeConstraints.weightx = 0.0;
		rangePanel.add(xSlider,rangeConstraints);
		//rangeConstraints.weightx = 0.5;
		rangePanel.add(xrangeSliderValue2,rangeConstraints);
		
		xSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				xrangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin)));
				xrangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",slider.getUpperValue() *(xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin)));
				canvas.getPad(index).setAxisRange("X",slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin , slider.getUpperValue()* (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin);
				//canvas.repaint();
				canvas.update();
			}
		});
		
		//y axis
		JLabel yAxisLabel = new JLabel("Y:");
		JLabel yrangeSliderValue1 = new JLabel(""+String.format("%4.2f",yMin));
		JLabel yrangeSliderValue2 = new JLabel(""+String.format("%4.2f",yMax));
		ySlider = new RangeSlider();
		ySlider.setMinimum(ySliderMin);
		ySlider.setMaximum(ySliderMax);
		ySlider.setValue(ySliderMin);
		ySlider.setUpperValue(ySliderMax);
		rangeConstraints.gridy=1;
		//rangeConstraints.weightx = 0.5;
		rangePanel.add(yAxisLabel,rangeConstraints);
		rangePanel.add(yrangeSliderValue1,rangeConstraints);
		//rangeConstraints.weightx = 0.0;
		rangePanel.add(ySlider,rangeConstraints);
		rangePanel.add(yrangeSliderValue2,rangeConstraints);
		
		ySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				yrangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",slider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin)));
				yrangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",slider.getUpperValue() *(yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin)));
				canvas.getPad(index).setAxisRange("Y",slider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin , slider.getUpperValue()* (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin);
				//canvas.repaint();
				canvas.update();
			}
		});
		
		//rangePanel.add(xRangePanel);
		//rangePanel.add(yRangePanel);
		axisOptions.add(rangePanel, BorderLayout.PAGE_END);
		*/
	}
	
	protected void applyToAll() {
		// TODO Auto-generated method stub
		
	}

	private void initFrameOptions(JPanel frameOptions) {
		frameOptions.setBorder(new TitledBorder("Frame Options"));
		frameOptions.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
      
        JTextField xPixels = new JTextField(4);
        JTextField yPixels = new JTextField(4);
        JTextField ratio = new JTextField(4);
        JCheckBox lockRatio = new JCheckBox("Lock Ratio");
        double width = can.getSize().getWidth();
        double height = can.getSize().getHeight();
        xPixels.setText(String.format("%d",(int) width));
        yPixels.setText(String.format("%d",(int) height));
        ratio.setText(String.format("%.02f",width/height));
        c.gridy = 0;
        c.weightx = 1.0;
        frameOptions.add(new JLabel("Width:"), c);
        frameOptions.add(xPixels, c);        
        frameOptions.add(new JLabel("Height:"), c);
        frameOptions.add(yPixels, c);
        xPixels.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		can.setPreferredSize(new Dimension(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText())));
        		can.setSize(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText()));
        		  double width = can.getSize().getWidth();
        	        double height = can.getSize().getHeight();
        	        xPixels.setText(String.format("%d",(int)width));
        	        yPixels.setText(String.format("%d",(int)height));
        	        ratio.setText(String.format("%.02f",width/height));
        	        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(can);
        	        topFrame.pack(); 
        	        can.update();
        	}
        });
        
        yPixels.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		can.setPreferredSize(new Dimension(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText())));
        		can.setSize(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText()));
        		  double width = can.getSize().getWidth();
        	        double height = can.getSize().getHeight();
        	        xPixels.setText(String.format("%d",(int)width));
        	        yPixels.setText(String.format("%d",(int)height));
        	        ratio.setText(String.format("%.02f",width/height));
        	        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(can);
        	        topFrame.pack();  
        	        can.update();
        	}
        });
        c.gridy = 1;
        frameOptions.add(new JLabel("Aspect Ratio:"), c);
        frameOptions.add(ratio, c);
        frameOptions.add(lockRatio, c);
        can.addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e){
        		  	double width = can.getSize().getWidth();
        	        double height = can.getSize().getHeight();
        	        xPixels.setText(String.format("%d",(int)width));
        	        yPixels.setText(String.format("%d",(int)height));
        	        ratio.setText(String.format("%.02f",width/height));
        	       // canvas.setPreferredSize(new Dimension(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText())));
            		//canvas.setSize(Integer.parseInt(xPixels.getText()),Integer.parseInt(yPixels.getText()));
        	        //JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(canvas);
        	        //topFrame.pack(); 
        	        can.update();
        	    
        	}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
        });
        
        //axisOptions.add(frameOptions,BorderLayout.CENTER);
	}

	
	private void initAxes() {
		JTabbedPane axes = new JTabbedPane();
		AxisAttributesPane panex = new AxisAttributesPane(can.getPad(pad).getAxisFrame().getAxisX().getAttributes());
		AxisAttributesPane paney = new AxisAttributesPane(can.getPad(pad).getAxisFrame().getAxisY().getAttributes());
	
		panex.addAttributeListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				can.repaint();
			}
		});
		paney.addAttributeListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				can.repaint();
			}
		});
		axes.add("X Axis", panex);
		axes.add("Y Axis", paney);
		if(can.getPad(pad).getDatasetPlotters().size()>0){
			if(can.getPad(pad).getDatasetPlotters().get(0) instanceof Histogram2DPlotter){
				AxisAttributesPane panez = new AxisAttributesPane(can.getPad(pad).getAxisFrame().getAxisZ().getAttributes());
				panez.addAttributeListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						can.repaint();
					}
				});
				axes.add("Z Axis", panez);
			}
		}
		tabbedPane.add("Axes",axes);
	}
	
	private void initDatasets() {
		List<IDataSetPlotter> datasets = can.getPad(pad).getDatasetPlotters();
		ArrayList<DatasetAttributesPane> datasetPaneList = new ArrayList<DatasetAttributesPane>();
		datasetPanes = new JTabbedPane();
		for(IDataSetPlotter dataset : datasets){
			DatasetAttributesPane tempPane = new DatasetAttributesPane(dataset.getDataSet().getAttributes());
			tempPane.addAttributeListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					can.repaint();
				}
			});
			datasetPaneList.add(tempPane);
			datasetPanes.add(dataset.getName(),tempPane);
			ActionListener removeButtonListener = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0; i<datasetPaneList.size(); i++){
						if(e.getSource().equals(datasetPaneList.get(i).buttonRemove)){
							int tabSelectionIndex = datasetPanes.getSelectedIndex();
							datasetPanes.setSelectedIndex((tabSelectionIndex>=1)?tabSelectionIndex-1:0);
							datasetPanes.remove(i);
							datasetPaneList.remove(i);
							datasetPanes.repaint();
							can.getPad(pad).remove(datasets.get(i).getDataSet());
							break;
						}
					}
					can.update();
				}
				
			};
			
			ActionListener defaultButtonListener  = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0; i<datasetPaneList.size(); i++){
						if(e.getSource().equals(datasetPaneList.get(i).buttonDefault)){
							int tabSelectionIndex = datasetPanes.getSelectedIndex();
							datasets.get(i).getDataSet().getAttributes().setDefault();
							DatasetAttributesPane pane = new DatasetAttributesPane(datasets.get(i).getDataSet().getAttributes());
							pane.buttonRemove.addActionListener(removeButtonListener);
							pane.buttonDefault.addActionListener(this);
							pane.addAttributeListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e) {
									can.repaint();
								}
							});
							//datasetPanes.setSelectedIndex((tabSelectionIndex>=1)?tabSelectionIndex-1:0);
							datasetPaneList.remove(i);
							datasetPanes.setComponentAt(i, pane);
							datasetPaneList.add(i,pane);
							datasetPanes.repaint();
							break;
						}
					}
					can.update();
				}
				
			};
			tempPane.buttonRemove.addActionListener(removeButtonListener);
			tempPane.buttonDefault.addActionListener(defaultButtonListener);
			
		}
		tabbedPane.add("Datasets",datasetPanes);
	}
	
}

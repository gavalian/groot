/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jlab.groot.graphics.EmbeddedCanvas;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author gavalian
 */
public class AxisAttributes {
    
    private String  labelFontName    = "Avenir";
    private int     labelFontSize    = 12;
    private String  titleFontName    = "Avenir";
    private int     titleFontSize    = 12;
    private boolean  axisAutoScale   = true;
    private double   axisMinimum     = 0.0;
    private double   axisMaximum     = 1.0;
    private int     labelOffset      = 4;
    private int     titleOffset      = 5;
    private boolean  axisGrid        = true;
    private int      lineColor       = 1;
    private int      lineWidth       = 1;
    private int      tickSize        = 5;
    private String   axisTitleString = "";
    
    private EmbeddedCanvas can      = null;
    
    public AxisAttributes(){
        
    }
    
    public void setLabelFontName(String fn){this.labelFontName = fn;}
    public void setLabelFontSize(int fs){this.labelFontSize = fs;}
    public void setTitleFontName(String fn){this.titleFontName = fn;}
    public void setTitleFontSize(int fs){this.titleFontSize = fs;}
    public void setAxisAutoScale(boolean autoscale){this.axisAutoScale=autoscale;}
    public void setAxisMinimum(double min) {this.axisMinimum = min;}
    public void setAxisMaximum(double max) {this.axisMaximum = max;}
    public void setAxisGrid(boolean grid){ this.axisGrid = grid;}
    
    
    public void setLineColor(int color) { this.lineColor = color;}
    public void setLineWidth(int width) { this.lineWidth = width;}
    public void setTickSize(int size) { this.tickSize = size;}
    public void setAxisTitle(String title){ this.axisTitleString = title;}
    public void setCanvas(EmbeddedCanvas can){this.can=can;}
    
    public void updateCanvas(){
    	can.update();
    }
    
    public JPanel getPane(){
        return new AxisAttributesPane(this);
    }
    
    public static class AxisAttributesPane extends JPanel implements ActionListener, ChangeListener {
        AxisAttributes attr = null;
        
        JSpinner   axisMinimum = null;
        JSpinner   axisMaximum = null;
        JCheckBox  axisAutoScale = null;
        JCheckBox  axisGrid      = null;
        JComboBox  labelFont     = null;
        JComboBox  titleFont     = null;
        
        JComboBox  labelFontSize = null;
        JComboBox  titleFontSize = null;
        
        JTextField axisTitle     = null;
        
        
        public AxisAttributesPane(AxisAttributes aa){
            attr = aa;
            this.setBorder(BorderFactory.createTitledBorder("Axis Attributes"));
            this.setLayout(new MigLayout());
            initUI();
        }
        
        
        private void initUI(){
            labelFont = new JComboBox(FontProperties.getSystemFontsArray());
            titleFont = new JComboBox(FontProperties.getSystemFontsArray());
            labelFontSize = new JComboBox(FontProperties.getFontSizeArray());
            titleFontSize = new JComboBox(FontProperties.getFontSizeArray());
            
            labelFont.addActionListener(this);
            titleFont.addActionListener(this);
            labelFontSize.addActionListener(this);
            titleFontSize.addActionListener(this);
            
            //Attributes.chooseByString(labelFont, "Avenir");
            //Attributes.chooseByString(titleFont, "Helvetica");
            
            this.add(new JSeparator(),"skip, wrap, growx");
            
            this.add(new JLabel("Label Font:"));
            this.add(labelFont,"wrap, pushx, growx");
            //this.add(labelFont,"wrap, pushx");
            
            this.add(new JLabel("Title Font:"));
            this.add(titleFont,"wrap, pushx, growx");
            
            this.add(new JLabel("Label Font Size:"));
            this.add(labelFontSize,"wrap, pushx, growx");
            
            this.add(new JLabel("Title Font Size:"));
            this.add(titleFontSize,"wrap, pushx, growx");
            

            this.add(new JSeparator(),"skip, wrap, growx");
            this.add(new JLabel("Title:"));
            axisTitle = new JTextField();
            axisTitle.addActionListener(this);
            
            this.add(axisTitle,"wrap, pushx, growx");
            this.add(new JSeparator(),"skip, wrap, growx");
            
            axisMinimum   = new JSpinner();
            axisMaximum   = new JSpinner();
            axisAutoScale = new JCheckBox();
            axisGrid      = new JCheckBox();

            axisMinimum.addChangeListener(this);
            axisMaximum.addChangeListener(this);
            axisAutoScale.addChangeListener(this);
            axisGrid.addChangeListener(this);


            this.add(new JLabel("Axis Min:"));
            this.add(axisMinimum,"wrap, pushx, growx");
            this.add(new JLabel("Axis Max:"));
            this.add(axisMaximum,"wrap, pushx, growx");
            this.add(new JLabel("Auto Scale:"),"skip, split4");
            this.add(axisAutoScale);
            
            this.add(new JLabel("Grid:"),"pushx");
            this.add(axisGrid,"wrap, pushx, growx");
            
            this.add(new JSeparator(),"skip, wrap, growx");
            JButton buttonDefault = new JButton("Default");
            JButton buttonApply   = new JButton("Apply");
            this.add(buttonDefault,"skip, split2, pushy");
            this.add(buttonApply,"wrap, pushy");
            buttonApply.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				//System.out.println("Update the axis!");
    				attr.updateCanvas();
    			}
            });
            
        }
        
        public void updateAttr(AxisAttributes axisAttr){
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("action performed:" + e.getSource().toString());
            if(e.getSource()==labelFont){
            	attr.setLabelFontName(FontProperties.getSystemFontsArray()[labelFont.getSelectedIndex()]);
            }else if(e.getSource()==titleFont){
            	attr.setTitleFontName(FontProperties.getSystemFontsArray()[titleFont.getSelectedIndex()]);
            }else if(e.getSource()==labelFontSize){
            	attr.setLabelFontSize(Integer.parseInt(FontProperties.getFontSizeArray()[labelFontSize.getSelectedIndex()]));
            }else if(e.getSource()==titleFontSize){
            	attr.setLabelFontSize(Integer.parseInt(FontProperties.getFontSizeArray()[titleFontSize.getSelectedIndex()]));
            }else if(e.getSource()==axisTitle){
            	attr.setAxisTitle(axisTitle.getText());
            }
        
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            //System.out.println("stateChanged:" + e.getSource().toString());
            if(e.getSource()==axisMinimum){
            	attr.setAxisMinimum((double) axisMinimum.getValue());
            }else if(e.getSource()==axisMaximum){
            	attr.setAxisMaximum((double) axisMaximum.getValue());
            }else if(e.getSource()==axisAutoScale){
            	attr.setAxisAutoScale(axisAutoScale.isSelected());
            }else if(e.getSource()==axisGrid){
            	attr.setAxisGrid(axisGrid.isSelected());
            }
        }
        
       
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        AxisAttributes  attr = new AxisAttributes();
        frame.add(attr.getPane());
        frame.pack();
        frame.setVisible(true);
    }
    
}


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

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author gavalian
 */
public class AxisAttributes {
    
    private static String labelFontName    = "Avenir";
    private static int    labelFontSize    = 12;
    private static String titleFontName    = "Avenir";
    private static int    titleFontSize    = 12;
    private static boolean axisAutoScale   = true;
    private static double  axisMinimum     = 0.0;
    private static double  axisMaximum     = 1.0;
    private static int    labelOffset      = 4;
    private static int    titleOffset      = 5;
    private static boolean axisGrid        = true;
    private static int     lineColor       = 1;
    private static int     lineWidth       = 1;
    private static int     tickSize        = 5;
    private static String  axisTitleString = "";
    
    public AxisAttributes(){
        
    }
    
    public static void setLabelFontName(String fn){labelFontName = fn;}
    public static void setLabelFontSize(int fs){labelFontSize = fs;}
    public static void setTitleFontName(String fn){titleFontName = fn;}
    public static void setTitleFontSize(int fs){titleFontSize = fs;}
    public static void setAxisAutoScale(boolean autoscale){axisAutoScale=autoscale;}
    public static void setAxisMinimum(double min) {axisMinimum = min;}
    public static void setAxisMaximum(double max) {axisMaximum = max;}
    public static void setAxisGrid(boolean grid){ axisGrid = grid;}
    public static void setLineColor(int color) { lineColor = color;}
    public static void setLineWidth(int width) { lineWidth = width;}
    public static void setTickSize(int size) { tickSize = size;}
    public static void setAxisTitle(String title){ axisTitleString = title;}
    
    
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
    				System.out.println("Update the axis!");
    			}
            });
            
        }
        
        public void updateAttr(AxisAttributes axisAttr){
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("action performed:" + e.getSource().toString());
            if(e.getSource()==labelFont){
            	setLabelFontName(FontProperties.getSystemFontsArray()[labelFont.getSelectedIndex()]);
            }else if(e.getSource()==titleFont){
            	setTitleFontName(FontProperties.getSystemFontsArray()[titleFont.getSelectedIndex()]);
            }else if(e.getSource()==labelFontSize){
            	setLabelFontSize(Integer.parseInt(FontProperties.getFontSizeArray()[labelFontSize.getSelectedIndex()]));
            }else if(e.getSource()==titleFontSize){
            	setLabelFontSize(Integer.parseInt(FontProperties.getFontSizeArray()[titleFontSize.getSelectedIndex()]));
            }else if(e.getSource()==axisTitle){
            	setAxisTitle(axisTitle.getText());
            }
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            //System.out.println("stateChanged:" + e.getSource().toString());
            if(e.getSource()==axisMinimum){
            	setAxisMinimum((double) axisMinimum.getValue());
            }else if(e.getSource()==axisMaximum){
            	setAxisMaximum((double) axisMaximum.getValue());
            }else if(e.getSource()==axisAutoScale){
            	setAxisAutoScale(axisAutoScale.isSelected());
            }else if(e.getSource()==axisGrid){
            	setAxisGrid(axisGrid.isSelected());
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


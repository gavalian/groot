/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author gavalian
 */
public class AxisAttributes {
    
    private String labelFontName    = "Avenir";
    private int    labelFontSize    = 12;
    private String titleFontName    = "Avenir";
    private int    titleFontSize    = 12;
    private boolean axisAutoScale   = true;
    private double  axisMinimum     = 0.0;
    private double  axisMaximum     = 1.0;
    private int    labelOffset      = 4;
    private int    titleOffset      = 5;
    private boolean axisGrid        = true;
    private int     lineColor       = 1;
    private int     lineWidth       = 1;
    private int     tickSize        = 5;
    
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
    
    
    
    public JPanel getPane(){
        return new AxisAttributesPane(this);
    }
    
    public static class AxisAttributesPane extends JPanel implements ActionListener {
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
            
            
            this.add(new JSeparator(),"skip, wrap, growx");
            
            this.add(new JLabel("Label Font:"));
            this.add(labelFont,"wrap, pushx, growx");
            
            this.add(new JLabel("Title Font:"));
            this.add(titleFont,"wrap, pushx, growx");
            
            this.add(new JLabel("Label Font Size:"));
            this.add(labelFontSize,"wrap, pushx, growx");
            
            this.add(new JLabel("Title Font Size:"));
            this.add(titleFontSize,"wrap, pushx, growx");
            

            this.add(new JSeparator(),"skip, wrap, growx");
            this.add(new JLabel("Title:"));
            axisTitle = new JTextField();
            this.add(axisTitle,"wrap, pushx, growx");
            this.add(new JSeparator(),"skip, wrap, growx");
            
            axisMinimum   = new JSpinner();
            axisMaximum   = new JSpinner();
            axisAutoScale = new JCheckBox();
            axisGrid      = new JCheckBox();
            
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
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
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


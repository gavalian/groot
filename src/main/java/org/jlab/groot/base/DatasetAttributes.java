package org.jlab.groot.base;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gavalian
 */
public class DatasetAttributes implements Cloneable {
    
    private int lineColor = 1;
    private int lineWidth = 1;
    private int lineStyle = 1;
    private int markerColor = 1;
    private int markerWidth = 1;
    private int markerStyle = 1;
    private int fillColor   = 0;
    private int fillStyle   = 1;
    
    public DatasetAttributes(){
        
    }
    
    public void setLineColor(int color){ this.lineColor = color;}
    public void setLineWidth(int width){ this.lineWidth = width;}
    public void setLineStyle(int style){ this.lineStyle = style;}
    public void setMarkerColor(int color){ this.markerColor = color;}
    public void setMarkerWidth(int width){ this.markerWidth = width;}
    public void setMarkerStyle(int style){ this.markerStyle = style;}
    
    public int  getLineColor(){return this.lineColor;}
    public int  getLineWidth(){return this.lineWidth;}
    public int  getLineStyle(){return this.lineStyle;}
    public int  getMarkerColor(){return this.markerColor;}
    public int  getMarkerWidth(){return this.markerWidth;}
    public int  getMarkerStyle(){return this.markerStyle;}
    
    
    @Override
    public DatasetAttributes  clone() throws CloneNotSupportedException{
        return (DatasetAttributes) super.clone();
    }
    
    public JPanel getPane(){
        DatasetAttributesPane pane = new DatasetAttributesPane(this);
        return pane;
    }
    
    public static class DatasetAttributesPane extends JPanel implements ActionListener {
        
        private DatasetAttributes attr = null;
        private final static String[] colorChoices  = 
                new String[]{"1","2","3","4","5","6","7","8","9"};
        
        private JComboBox boxLineColor = null;
        private JComboBox boxLineWidth = null;
        private JComboBox boxLineStyle = null;
        
        private JComboBox boxMarkerColor = null;
        private JComboBox boxMarkerWidth = null;
        private JComboBox boxMarkerStyle = null;
        
        private JComboBox boxFillColor = null;
        
        private List<ActionListener> listeners = new ArrayList<ActionListener>();
        
        public DatasetAttributesPane(DatasetAttributes da){
            attr = da;
            this.setBorder(BorderFactory.createTitledBorder("Dataset Attributes"));
            this.setLayout(new MigLayout());
            this.initUI();
        }
        
        private void initUI(){
            JLabel labelLineColor  = new JLabel("Line Color:");
            JLabel labelLineWidth  = new JLabel("Line Width:");
            JLabel labelLineStyle  = new JLabel("Line Style:");
            
            boxLineColor = new JComboBox(colorChoices);
            boxLineWidth = new JComboBox(colorChoices);
            boxLineStyle = new JComboBox(colorChoices);
            
            boxMarkerColor = new JComboBox(colorChoices);
            boxMarkerWidth = new JComboBox(colorChoices);
            boxMarkerStyle = new JComboBox(colorChoices);
            
            this.add(labelLineColor);
            this.add(boxLineColor,"wrap, pushx, growx");
            this.add(labelLineWidth);
            this.add(boxLineWidth,"wrap, growx");
            this.add(labelLineStyle);
            this.add(boxLineStyle,"wrap, growx");
            JButton buttonDefault = new JButton("Default");
            JButton buttonApply = new JButton("Apply");
            this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            
            this.add(new JLabel("Maker Color:"));
            this.add(boxMarkerColor,"wrap, growx");
            
            this.add(new JLabel("Maker Size:"));
            this.add(boxMarkerWidth,"wrap, growx");
            
            this.add(new JLabel("Maker Style:"));
            this.add(boxMarkerStyle,"wrap, growx");
            
            this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            boxFillColor = new JComboBox(colorChoices);
            this.add(new JLabel("Fill Color:"));
            this.add(boxFillColor,"wrap, pushx, growx");
            this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            
            
            this.add(buttonDefault,"skip, pushx, split2");
            this.add(buttonApply,"pushx");
        }

        public void addAttributeListener(ActionListener al){
            this.listeners.add(al);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().compareTo("Apply")==0){
                
            }
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        DatasetAttributes  attr = new DatasetAttributes();
        frame.add(attr.getPane());
        frame.pack();
        frame.setVisible(true);
    }
}

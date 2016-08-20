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
    private int markerSize = 1;
    private int markerStyle = 1;
    private int fillColor   = 0;
    private int fillStyle   = 1;
    
    private String stringXtitle = "";
    private String stringYtitle = "";
    private String stringTitle  = "";
    
    public DatasetAttributes(){
        
    }
    
    public void setXTitle(String title){ this.stringXtitle = title;}
    public void setYTitle(String title){ this.stringYtitle = title;}
    public void setTitle(String title){ this.stringTitle = title;}
    
    
    public void setLineColor(int color){ this.lineColor = color;}
    public void setLineWidth(int width){ this.lineWidth = width;}
    public void setLineStyle(int style){ this.lineStyle = style;}
    public void setMarkerColor(int color){ this.markerColor = color;}
    public void setMarkerSize(int size){ this.markerSize = size;}
    public void setMarkerStyle(int style){ this.markerStyle = style;}
    public void setFillColor(int color){ this.fillColor = color;}
    public void setFillStyle(int style){ this.fillStyle = style;}

    
    public int  getLineColor(){return this.lineColor;}
    public int  getLineWidth(){return this.lineWidth;}
    public int  getLineStyle(){return this.lineStyle;}
    public int  getMarkerColor(){return this.markerColor;}
    public int  getMarkerSize(){return this.markerSize;}
    public int  getMarkerStyle(){return this.markerStyle;}
    public int  getFillColor() {return this.fillColor;}
    public int  getFillStyle(){return this.fillStyle;}
    
    public String getTitle() { return this.stringTitle;}
    public String getXTitle(){ return this.stringXtitle;}
    public String getYTitle(){ return this.stringYtitle;}
    
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
        private JComboBox boxMarkerSize = null;
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
           
            boxLineColor.addActionListener(this);
            boxLineWidth.addActionListener(this);
            boxLineStyle.addActionListener(this);
            
            boxMarkerColor = new JComboBox(colorChoices);
            boxMarkerSize = new JComboBox(colorChoices);
            boxMarkerStyle = new JComboBox(colorChoices);
            
            boxMarkerColor.addActionListener(this);
            boxMarkerSize.addActionListener(this);
            boxMarkerStyle.addActionListener(this);
            boxFillColor = new JComboBox(colorChoices);
            boxFillColor.addActionListener(this);

            
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
            this.add(boxMarkerSize,"wrap, growx");
            
            this.add(new JLabel("Maker Style:"));
            this.add(boxMarkerStyle,"wrap, growx");
            
            this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            this.add(new JLabel("Fill Color:"));
            this.add(boxFillColor,"wrap, pushx, growx");
            this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            
            
            this.add(buttonDefault,"skip, pushx, split2");
            this.add(buttonApply,"pushx");
        }

        public void addAttributeListener(ActionListener al){
            this.listeners.add(al);
        }
        
        public void updateCanvas(){
        	for(ActionListener actionListener:listeners){
        		actionListener.actionPerformed(new ActionEvent(this, 0, ""));
        	}
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().compareTo("Apply")==0){
                
            }
            if(e.getSource()==boxLineColor){
            	attr.setLineColor(Integer.parseInt(colorChoices[boxLineColor.getSelectedIndex()]));
            }else if(e.getSource()==boxLineWidth){
            	attr.setLineWidth(Integer.parseInt(colorChoices[boxLineWidth.getSelectedIndex()]));
            }else if(e.getSource()==boxLineStyle){
            	attr.setLineStyle(Integer.parseInt(colorChoices[boxLineStyle.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerColor){
            	attr.setMarkerColor(Integer.parseInt(colorChoices[boxMarkerColor.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerSize){
            	attr.setMarkerSize(Integer.parseInt(colorChoices[boxMarkerSize.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerStyle){
            	attr.setMarkerStyle(Integer.parseInt(colorChoices[boxMarkerStyle.getSelectedIndex()]));
            }else if(e.getSource()==boxFillColor){
            	attr.setFillColor(Integer.parseInt(colorChoices[boxFillColor.getSelectedIndex()]));
            }
            updateCanvas();
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

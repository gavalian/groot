/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import net.miginfocom.swing.MigLayout;
import org.jlab.groot.ui.RangeSlider;

/**
 *
 * @author gavalian
 */
public class SparseIndexPane extends JPanel {
    
    int indexRank = 0;
    int[] dimensions = null;
    int   numberOrder = 0;
    
    List<RangeSlider>  sliders     = new ArrayList<RangeSlider>();    
    JComboBox       selectionBin = null;
    JComboBox     selectionOrder = null;
    
    JComboBox       selectionBinY = null;
    JComboBox     selectionOrderY = null;
    
    public SparseIndexPane(int[] dims, int vectorSize){
        super();
        dimensions = dims;
        this.setBorder(BorderFactory.createTitledBorder("Grid Index"));        
        this.setLayout(new MigLayout());
        this.numberOrder = vectorSize;
        initUI();
    }
    
    private void initUI(){
        
        for(int i = 0; i < dimensions.length; i++){
            //JSlider sl = new JSlider(JSlider.HORIZONTAL,
            //        0, dimensions[i]-1, 0);
            RangeSlider sl = new RangeSlider(
                    0, dimensions[i]-1);
            sl.setUpperValue(dimensions[i]-1);
            sl.setValue(0);
            
            sl.setMajorTickSpacing(2);
            sl.setMinorTickSpacing(1);
            sl.setPaintTicks(true);
            sl.setPaintLabels(true);
            sl.setSnapToTicks(true);
            sliders.add(sl);
            
            this.add(new JLabel("DIM "+i +" : "));
            this.add(sl,"wrap, pushx, growx");

        }
        
        String[] choises = new String[dimensions.length];
        String[] choisesOrder = new String[this.numberOrder];

        for(int i = 0; i < choises.length; i++) choises[i] = (new Integer(i)).toString();
        this.selectionBin = new JComboBox(choises);


        for(int i = 0; i < this.numberOrder; i++) choisesOrder[i] = (new Integer(i)).toString();
        
        this.selectionOrder = new JComboBox(choisesOrder);
        this.add(new JLabel(""));
        this.add( new JSeparator(),"wrap, pushx, growx");
        this.add(new JLabel("X dimension : "));
        this.add(this.selectionBin,"split 3, growx");
        this.add(new JLabel("Vector : "));
        this.add(this.selectionOrder,"growx");
        
        
/*        this.add(this.selectionBin," wrap, pushx, growx");
        this.add(new JLabel("Vector : "));
        this.add(this.selectionOrder," wrap, pushx, growx");        
*/
    }
    
    public int[] getBins(){
        int[] index = new int[this.sliders.size()];
        for(int i = 0; i < index.length;i++) index[i] = sliders.get(i).getValue();
        return index;
    }
    
    public int[] getBinsMin(){
        int[] index = new int[this.sliders.size()];
        for(int i = 0; i < index.length;i++) index[i] = sliders.get(i).getValue();
        return index;
    }
    
    public int[] getBinsMax(){
        int[] index = new int[this.sliders.size()];
        for(int i = 0; i < index.length;i++) index[i] = sliders.get(i).getUpperValue();
        return index;
    }
    
    
    public int getSelectedBin(){
        String token = (String) this.selectionBin.getSelectedItem();
        return Integer.parseInt(token);
    }
    
    public int getSelectedOrder(){
        String token = (String) this.selectionOrder.getSelectedItem();
        return Integer.parseInt(token);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        SparseIndexPane pane = new SparseIndexPane(new int[]{10,15,20},2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }
    
}

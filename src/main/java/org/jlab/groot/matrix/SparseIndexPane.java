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
import javax.swing.JSlider;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author gavalian
 */
public class SparseIndexPane extends JPanel {
    
    int indexRank = 0;
    int[] dimensions = null;
    
    List<JSlider>  sliders     = new ArrayList<JSlider>();    
    JComboBox     selectionBin = null;
    
    public SparseIndexPane(int[] dims){
        super();
        dimensions = dims;
        this.setBorder(BorderFactory.createTitledBorder("Grid Index"));        
        this.setLayout(new MigLayout());
        initUI();
    }
    
    private void initUI(){
        for(int i = 0; i < dimensions.length; i++){
            JSlider sl = new JSlider(JSlider.HORIZONTAL,
                    0, dimensions[i]-1, 0);
            
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
        for(int i = 0; i < choises.length; i++) choises[i] = (new Integer(i)).toString();
        this.selectionBin = new JComboBox(choises);
        
        this.add(new JLabel("Draw Dimension :"));
        this.add(this.selectionBin,"wrap, pushx, growx");
    }
        
    public int[] getBins(){
        int[] index = new int[this.sliders.size()];
        for(int i = 0; i < index.length;i++) index[i] = sliders.get(i).getValue();
        return index;
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        SparseIndexPane pane = new SparseIndexPane(new int[]{10,15,20});
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }
    
}

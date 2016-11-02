/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedCanvasTabbed;

/**
 *
 * @author gavalian
 */
public class GridStudio extends JFrame implements ActionListener {
    
    JPanel    mainPane = null;
    JPanel  actionPane = null;
    
    EmbeddedCanvasTabbed  canvas = null;
    SparseIndexPane indexPane = null;
    
    SparseVectorGrid  grid = null;
    
    public GridStudio(String filename){
        super();        
        initUI();
        this.add(mainPane);
        this.pack();
        this.setVisible(true);
    }
    
    public GridStudio(){
        super();
        initUI();
        this.add(mainPane);
        this.pack();
        this.setVisible(true);
    }
    
    public void openFile(String filename){
        grid = SparseGridIO.importHipo(filename);
        mainPane.remove(indexPane);
        indexPane = new SparseIndexPane(grid.getIndexer().getBinsPerAxis(),grid.getVectorSize());
        mainPane.add(indexPane,BorderLayout.PAGE_END);
        this.validate();
        
    }
    
    private void initUI(){
        
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        
        canvas = new EmbeddedCanvasTabbed();
        
        indexPane = new SparseIndexPane(new int[]{9,21,31},1);
        
        mainPane.add(canvas,BorderLayout.CENTER);
        mainPane.add(indexPane,BorderLayout.PAGE_END);
        
        actionPane = new JPanel();
        JButton buttonPlot = new JButton("Draw");        
        buttonPlot.addActionListener(this);
        JButton buttonOverView = new JButton("Over View");
        buttonOverView.addActionListener(this);
        actionPane.add(buttonPlot);
        actionPane.add(buttonOverView);
        mainPane.add(actionPane,BorderLayout.PAGE_START);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Draw")==0){
            int[] binsMin = this.indexPane.getBinsMin();
            int[] binsMax = this.indexPane.getBinsMax();
            int   dim  = this.indexPane.getSelectedBin();
            int order  = this.indexPane.getSelectedOrder();
            
            H1F h = grid.slice(dim, order,binsMin,binsMax);
            canvas.getCanvas().drawNext(h);
            canvas.getCanvas().update();
        }
        
        if(e.getActionCommand().compareTo("Over View")==0){
            this.canvas.getCanvas().divide(1, grid.getIndexer().getRank());
            int order = this.indexPane.getSelectedOrder();
            int[] binsMax = grid.getIndexer().getBinsPerAxis();
            int[] binsMin = new int[binsMax.length];
            for(int k = 0; k < binsMin.length; k++) binsMin[k] = 0;
            
            for(int i = 0; i < grid.getIndexer().getRank();i++){
                H1F h = grid.slice(i, order,binsMin,binsMax);
                canvas.getCanvas().drawNext(h);
            }
        }
    }
    
    public static void main(String[] args){
        GridStudio studio = new GridStudio();
        studio.openFile("grid.hipo");
    }

}

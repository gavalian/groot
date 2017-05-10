/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.group.DataGroup;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvasGroup extends JPanel implements ActionListener {

    private EmbeddedCanvas canvas = new EmbeddedCanvas();
    private int     padsPerPage = 12;
    private DataGroup dataGroup = new DataGroup();
    private int     currentPage = 0;
    private int        maxPages = 1;
    private List<IDataSet> canvasDataSets = new ArrayList<IDataSet>();
    JLabel  progressLabel = null;
    
    public EmbeddedCanvasGroup(){
        super();
        setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        JButton buttonPrev = new JButton("<");
        this.progressLabel = new JLabel("0/0");
        JButton buttonNext = new JButton(">");
        buttonPrev.addActionListener(this);
        buttonNext.addActionListener(this);
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(buttonPrev);
        buttonsPanel.add(this.progressLabel);
        buttonsPanel.add(buttonNext);
        add(this.canvas,BorderLayout.CENTER);
        add(buttonsPanel,BorderLayout.PAGE_END);
    }
    
    
    public void setData(List<IDataSet>  datasets){
        this.canvasDataSets.clear();
        this.canvasDataSets.addAll(datasets);
        this.currentPage = 0;
        this.maxPages = this.canvasDataSets.size()/this.padsPerPage;
        if(maxPages*this.padsPerPage<this.canvasDataSets.size()){
            this.maxPages++;
        }
        this.updateCanvas();
        
    }
    
    
    public void updateCanvas(){
        this.canvas.clear();
        this.canvas.divide(3, 4);
        for(int i = 0; i < this.padsPerPage; i++){
            int index = currentPage*this.padsPerPage + i;
            this.canvas.cd(i);
            if(index<this.canvasDataSets.size()){
                this.canvas.draw(this.canvasDataSets.get(index));
            }
        }
        this.progressLabel.setText(String.format("%d/%d", this.currentPage+1,this.maxPages));
    }
    
    public void nextPage(){
        if((currentPage+1)<maxPages){
            currentPage++;
            this.updateCanvas();
        }
    }
    
    public void previousPage(){
        if(currentPage>0){
            currentPage--;
            this.canvas.clear();
            this.updateCanvas();
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("<")==0){
            this.previousPage();
        }
        if(e.getActionCommand().compareTo(">")==0){
            this.nextPage();
        }
    }
     public static void main(String[] args){
        JFrame frame = new JFrame();
        EmbeddedCanvasGroup canvasTab = new EmbeddedCanvasGroup();
        //EmbeddedCanvasTabbed canvasTab = new EmbeddedCanvasTabbed();
        frame.add(canvasTab);
        frame.pack();
        frame.setMinimumSize(new Dimension(300,300));
        frame.setVisible(true);
    }
}

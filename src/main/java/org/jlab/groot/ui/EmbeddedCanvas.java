/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel {
    
    private Timer        updateTimer = null;
    private EmbeddedPad  pad = new EmbeddedPad();
    private Long numberOfPaints  = (long) 0;
    private Long paintingTime    = (long) 0;
    private int  numberOfRows    = 1;
    private int  numberOfColumns = 1;
    private List<EmbeddedPad>    canvasPads = new ArrayList<EmbeddedPad>();
    
    public EmbeddedCanvas(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(1, 1);
    }
    
    public EmbeddedCanvas(int xsize, int ysize, int nc, int nr){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(nc, nr);
    }
    
    public void divide(int nc, int nr){
        this.numberOfColumns = nc;
        this.numberOfRows    = nr;
        this.canvasPads.clear();
        for(int i = 0; i < nc*nr; i++){
            this.canvasPads.add(new EmbeddedPad());
        }
    }
    
    @Override
    public void paint(Graphics g){ 
        try {
            Long st = System.currentTimeMillis();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = this.getSize().width;
            int h = this.getSize().height;
            
            this.updateCanvasPads(w, h);
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, w, h);
            //pad.setDimension(0, 0, w, h);        
            //pad.draw(g2d);
            for(EmbeddedPad pad : canvasPads){
                pad.draw(g2d);
            }
            Long et = System.currentTimeMillis();
            paintingTime += (et-st);
            numberOfPaints++;
            System.out.println(this.getBenchmarkString());
        } catch (Exception e){
            
        }
    }
    
    private void updateCanvasPads(int xsize, int ysize){
        int rowSize = ysize/numberOfRows;
        int colSize = xsize/numberOfColumns;
        int counter = 0;
        for(int ir = 0; ir < numberOfRows; ir++){
            for(int ic = 0; ic < numberOfColumns; ic++){
                int xs = ic*colSize;
                int ys = ir*rowSize;
                //System.out.println(" counter = " + counter + " X "
                //+ xs + " Y " + ys + " C/R " + colSize + " " + rowSize);
                canvasPads.get(counter).setDimension(xs, ys, colSize, rowSize);
                counter++;
            }
        }
    }
    
    public int getNPads(){
        return this.canvasPads.size();
    }
    
    public String getBenchmarkString(){
        StringBuilder str = new StringBuilder();
        int ms = (int) (paintingTime/numberOfPaints);
        str.append(String.format("Time = %d ms",ms));
        return str.toString();
    }
    
    public void update(){
        repaint();
        
    }
    
    public void  initTimer(int interval){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        updateTimer = new Timer("EmbeddeCanvasTimer");
        updateTimer.scheduleAtFixedRate(timerTask, 30, interval);
    }
    
    public EmbeddedPad  getPad(int index){
        return this.canvasPads.get(index);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        EmbeddedCanvas canvas = new EmbeddedCanvas(400,400);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}

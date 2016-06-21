/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.BasicStroke;
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
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Func1D;

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
    private List<EmbeddedPad>    canvasPads  = new ArrayList<EmbeddedPad>();
    private int                  selectedPad = 2;
    private int                  activePad   = 0;
    
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
        activePad   = 0;
    }
    
    public void moveNext(){
        activePad++;
        if(activePad>=this.canvasPads.size()){
            activePad = 0;
        }
    }
    
    public void drawNext(IDataSet ds){
        this.draw(ds, "");
    }
    
    public void drawNext(IDataSet ds, String options){
        draw(ds,options);
        moveNext();
    }
    
    public void draw(IDataSet ds){
        draw(ds,"");
    }
    
    public void draw(IDataSet ds, String options){
        if(options.contains("same")==false){
            canvasPads.get(activePad).reset();
        }
        
        if(ds instanceof H1F){
            canvasPads.get(activePad).addPlotter(new HistogramPlotter(ds));
        }
        
        if(ds instanceof GraphErrors){
            canvasPads.get(activePad).addPlotter(new GraphErrorsPlotter(ds));
        }
        if(ds instanceof Func1D){
            canvasPads.get(activePad).addPlotter(new FunctionPlotter(ds));
        }
    }
    
    public void cd(int pad){
        activePad = pad;
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
            
            if(selectedPad>=0&&selectedPad<canvasPads.size()){
                g2d.setColor(Color.red);
                g2d.setStroke(new BasicStroke(1));
                Dimension2D d = canvasPads.get(selectedPad).padDimensions;
                g2d.drawRect(
                        (int) d.getDimension(0).getMin(), 
                        (int) d.getDimension(1).getMin(), 
                        (int) (d.getDimension(0).getMax()-d.getDimension(0).getMin()),
                        (int) (d.getDimension(1).getMax()-d.getDimension(1).getMin())
                );
            }
            
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

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
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel {
    
    private Timer        updateTimer = null;
    private Long numberOfPaints  = (long) 0;
    private Long paintingTime    = (long) 0;
    
    private List<EmbeddedPad>    canvasPads  = new ArrayList<EmbeddedPad>();
    private int                  ec_COLUMNS  = 1;
    private int                  ec_ROWS     = 1;
        
    public EmbeddedCanvas(){
        super();
        //this.setSize(500, 400);
        this.setPreferredSize(new Dimension(500,400));        
        canvasPads.add(new EmbeddedPad());
        this.divide(1, 1);
    }
    
    public final void divide(int columns, int rows){
        canvasPads.clear();
        ec_COLUMNS = columns;
        ec_ROWS    = rows;
        for(int i = 0; i < columns*rows; i++){
            canvasPads.add(new EmbeddedPad());
        }
    }
    
    private void updateCanvasPads(int w, int h){
        int pcounter = 0;
        for(int ir = 0; ir < ec_ROWS; ir++){
            for(int ic = 0; ic < ec_COLUMNS; ic++){
                double x   = ic * (w/((double) ec_COLUMNS ));
                double xe  = (ic+1) * (w/((double) ec_COLUMNS ));
                double y   = ir * ( h/((double) ec_ROWS) );
                double ye  = (ir+1) * ( h/((double) ec_ROWS));
                //System.out.println("PAD " + pcounter + " " + x + " " + xe );
                canvasPads.get(pcounter).setDimension((int) x, (int) y,
                        (int) (xe-x), (int) (ye-y));

                pcounter++;
            }
        }
    }
    
    /**
     * painting all components on the Graphics2D object.
     * @param g 
     */
    @Override
    public void paint(Graphics g){ 

        Long st = System.currentTimeMillis();
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        updateCanvasPads(w,h);
        
        PadMargins  margins = new PadMargins();
        
        for(int i = 0; i < canvasPads.size(); i ++){
            EmbeddedPad pad = canvasPads.get(i);
            pad.getAxisFrame().updateMargins(g2d);
            //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());
            margins.marginFit(pad.getAxisFrame().getFrameMargins());
        }
        
        for(int i = 0; i < canvasPads.size(); i ++){
            EmbeddedPad pad = canvasPads.get(i);            
            //pad.setDimension(0, 0, w, h);                        
            //System.out.println("PAD " + i + " " + pad.getAxisFrame().getFrameMargins());
            //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());                
            //System.out.println(pad.getAxisFrame().getFrameMargins());
            pad.getAxisFrame().setAxisMargins(margins);
            pad.setMargins(margins);
            pad.draw(g2d);
        }
    }
        
    public EmbeddedPad  getPad(int index){
        return this.canvasPads.get(index);
    }
        
    public void update(){
        Long st = System.currentTimeMillis();
        this.repaint();
        Long et = System.currentTimeMillis();
        paintingTime += (et-st);
        numberOfPaints++;
        System.out.println(this.getBenchmarkString());
    }
    
    public String getBenchmarkString(){
        StringBuilder str = new StringBuilder();
        double time = (double) paintingTime;
        double ms =  (time/numberOfPaints);
        str.append(String.format("Time = %.2f ms",ms));
        return str.toString();
    }
    
    public void setAxisFontSize(int size){
        for(EmbeddedPad pad : canvasPads){
            pad.setAxisFontSize(size);
        }
    }
    
    public void  initTimer(int interval){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
                /*for(int i = 0; i < canvasPads.size();i++){
                    System.out.println("PAD = " + i);
                    canvasPads.get(i).show();
                }*/
            }
        };
        updateTimer = new Timer("EmbeddeCanvasTimer");
        updateTimer.scheduleAtFixedRate(timerTask, 30, interval);
        this.paintingTime   = 0L;
        this.numberOfPaints = 0L;
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        canvas.divide(1, 3);
        canvas.setAxisFontSize(14);
        //canvas.getPad(0).getAxisFrame().getAxisX().setAxisFontSize(18);
        //canvas.getPad(1).getAxisFrame().getAxisY().setAxisFontSize(18);
        //canvas.getPad(0).getAxisFrame().setDrawAxisZ(true);
        
        H1F h1 = FunctionFactory.createDebugH1F(6);
        H1F h2 = FunctionFactory.randomGausian(100, 0.4, 5.6, 200000, 2.3, 0.8);
        H2F h2d = FunctionFactory.randomGausian2D(120, 0.4, 5.6, 800000, 2.3, 0.8);
        
        h1.setFillColor(43);
        h2.setFillColor(44);
        HistogramPlotter  plotter  = new HistogramPlotter(h1);
        HistogramPlotter  plotter2 = new HistogramPlotter(h2);
        Histogram2DPlotter  plotter3 = new Histogram2DPlotter(h2d);
        
        canvas.getPad(0).addPlotter(plotter);
        canvas.getPad(1).addPlotter(plotter2);
        canvas.getPad(2).addPlotter(plotter3);
        
        canvas.divide(4,4);
        for(int i = 0; i < 16; i++){
            canvas.getPad(i).getAxisFrame().setDrawAxisZ(true);
            canvas.getPad(i).addPlotter(plotter3);
        }
        
        
        //canvas.initTimer(300);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}

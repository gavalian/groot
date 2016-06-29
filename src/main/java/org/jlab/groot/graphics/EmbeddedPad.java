/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {
    
     Dimension2D            padDimensions = new Dimension2D();
     GraphicsAxisFrame          axisFrame = new GraphicsAxisFrame();
     Color                backgroundColor = Color.WHITE;
     private  PadMargins  padMargins      = new PadMargins();
     List<IDataSetPlotter> datasetPlotters = new ArrayList<IDataSetPlotter>();
    
     public EmbeddedPad(){
         
     }
    
    public EmbeddedPad(int x, int y, int width, int height){
        this.setDimension(x, y, width, height);        
    }
    
    public final EmbeddedPad setDimension(int x, int y, int width, int height){
        this.padDimensions.getDimension(0).setMinMax(x,x+width);
        this.padDimensions.getDimension(1).setMinMax(y,y+height);
        axisFrame.setFrameDimensions(
                padDimensions.getDimension(0).getMin(),
                padDimensions.getDimension(0).getMax(),
                padDimensions.getDimension(1).getMin(),
                padDimensions.getDimension(1).getMax()
        );
        //System.out.println(padDimensions);
        return this;
    }
    
    public void setMargins(PadMargins margins){
        this.padMargins.copy(margins);
        //System.out.println(" PAD - > " + padMargins);
    }
    
    public GraphicsAxisFrame  getAxisFrame(){
        return this.axisFrame;
    }
    
    public void draw(Graphics2D g2d){                
        //axisFrame.updateMargins(g2d);
        //axisFrame.setAxisMargins(padMargins);
        //axisFrame.updateMargins(g2d);
        g2d.setColor(backgroundColor);
        /*g2d.fillRect( 
                (int) padDimensions.getDimension(0).getMin(),
                (int) padDimensions.getDimension(1).getMin(),
                (int) ( padDimensions.getDimension(0).getMax() 
                        - padDimensions.getDimension(0).getMin()),
                (int) (padDimensions.getDimension(1).getMax()
                        -padDimensions.getDimension(1).getMin())
                );
        */
        Dimension3D  axis = new Dimension3D();
        if(this.datasetPlotters.size()>0){
            axis.copy(datasetPlotters.get(0).getDataRegion());
        
        
            axisFrame.getAxisX().setRange(
                    axis.getDimension(0).getMin(),
                    axis.getDimension(0).getMax()
            );
            
            axisFrame.getAxisY().setRange(
                    axis.getDimension(1).getMin(),
                    axis.getDimension(1).getMax()
            );
            axisFrame.getAxisZ().setRange(
                    axis.getDimension(2).getMin(),
                    axis.getDimension(2).getMax()
            );
            datasetPlotters.get(0).draw(g2d, axisFrame);
        }
        
        //System.out.println("PLOTTERS SIZE = " + this.datasetPlotters.size());
        axisFrame.drawAxis(g2d, padMargins);        
    }
    
    public void setAxisFontSize(int size){
        this.getAxisFrame().getAxisX().setAxisFontSize(size);
        this.getAxisFrame().getAxisY().setAxisFontSize(size);
    }
    
    public void addPlotter(IDataSetPlotter plotter){
        this.datasetPlotters.add(plotter);
    }
    
}

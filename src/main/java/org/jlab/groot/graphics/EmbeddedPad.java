/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.math.Func1D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {
    
     Dimension2D            padDimensions  = new Dimension2D();
     GraphicsAxisFrame          axisFrame  = new GraphicsAxisFrame();
     Color                backgroundColor  = Color.WHITE;
     private  PadMargins  padMargins       = new PadMargins();
     List<IDataSetPlotter> datasetPlotters = new ArrayList<IDataSetPlotter>();
     
     Dimension3D           fixedRange      = new Dimension3D();
     
     private boolean       isAutoScaleX    = true;
     private boolean       isAutoScaleY    = true;
     
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
        axis.set(0.0, 1.0, 0.0, 1.0, 0.0, 1.0);
        if(this.datasetPlotters.size()>0){
            axis.copy(datasetPlotters.get(0).getDataRegion());
            for(IDataSetPlotter plotter : this.datasetPlotters){
                Dimension3D d3d = plotter.getDataRegion();
                axis.combine(d3d);
            }
        }
        
        if(this.isAutoScaleX==false){
            axis.getDimension(0).copy(this.fixedRange.getDimension(0));
        }
        
        if(this.isAutoScaleY==false){
            axis.getDimension(1).copy(this.fixedRange.getDimension(1));
        }
        
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
        
        Rectangle2D rect = new Rectangle2D.Double(
                axisFrame.getFrameDimensions().getDimension(0).getMin() + padMargins.getLeftMargin(),
                axisFrame.getFrameDimensions().getDimension(1).getMin() + padMargins.getTopMargin(),
                axisFrame.getFrameDimensions().getDimension(0).getLength() 
                        - padMargins.getLeftMargin() - padMargins.getRightMargin(),
                axisFrame.getFrameDimensions().getDimension(1).getLength() - 
                        padMargins.getTopMargin() - padMargins.getBottomMargin()
        );
        
        g2d.setClip(rect);
        
        for(IDataSetPlotter plotter : this.datasetPlotters){
            plotter.draw(g2d, axisFrame);
        }
        g2d.setClip(null);
        //System.out.println("PLOTTERS SIZE = " + this.datasetPlotters.size());
        axisFrame.drawAxis(g2d, padMargins);        
    }
    
    public EmbeddedPad setAutoScale(){
        this.isAutoScaleX = true;
        this.isAutoScaleY = true;
        return this;
    }
    
    public EmbeddedPad setAxisRangeX(double xmin, double xmax){
        this.fixedRange.getDimension(0).setMinMax(xmin, xmax);
        this.isAutoScaleX = false;
        return this;
    }
    
    public EmbeddedPad setAxisRangeY(double ymin, double ymax){
        this.fixedRange.getDimension(1).setMinMax(ymin, ymax);
        this.isAutoScaleY = false;
        return this;
    }
    
    public void setAxisFontSize(int size){
        this.getAxisFrame().getAxisX().setAxisFontSize(size);
        this.getAxisFrame().getAxisY().setAxisFontSize(size);
    }
    
    public void addPlotter(IDataSetPlotter plotter){
        this.datasetPlotters.add(plotter);
    }
    
    public void draw(IDataSet ds, String options){
        if(options.contains("same")==false){
            this.datasetPlotters.clear();
        }
        if(ds instanceof Func1D){
            this.addPlotter(new FunctionPlotter(ds));
        }
        
        if(ds instanceof H1F){
            this.addPlotter(new HistogramPlotter(ds,options));
        }
        if(ds instanceof H2F){
            this.addPlotter(new Histogram2DPlotter(ds));
        }
        if(ds instanceof GraphErrors){
            this.addPlotter(new GraphErrorsPlotter(ds));
        }
    }
}

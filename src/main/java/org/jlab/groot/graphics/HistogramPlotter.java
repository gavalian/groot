/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

/**
 *
 * @author gavalian
 */
public class HistogramPlotter implements IDataSetPlotter  {
    
    String       plottingOptions = "";
    IDataSet     dataset = null;
    Dimension3D  dataRegion  = new Dimension3D();
    String       datasetName = "";
    
    public HistogramPlotter(IDataSet ds){
        dataset = ds;
        datasetName = ds.getName();
    }
    
    public HistogramPlotter(IDataSet ds,String options){
        dataset = ds;
        datasetName = ds.getName();
        this.plottingOptions = options;
    }
    
    @Override
    public String getOptions() {
        return this.plottingOptions;
    }

    @Override
    public void setOptions(String opt) {
        this.plottingOptions = opt;
    }

    @Override
    public String getName() {
        return datasetName;
    }

    @Override
    public IDataSet getDataSet() {
        return dataset;
    }

    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {
        
        int npoints = dataset.getDataSize(0);

        double dataX  = dataset.getDataX(0);
        double dataY  = dataset.getDataY(0);
        double errorX = dataset.getDataEX(0);
            
        double xps = frame.getAxisPointX(dataX - errorX*0.5);
        double xpe = frame.getAxisPointX(dataX + errorX*0.5);
        //double yp  = frame.getAxis(1).getDimension().getMax() - 
        //        frame.getAxis(1).getAxisPosition(0.0)                 
        //        + frame.getAxis(1).getDimension().getMin();
        double yp = frame.getAxisPointY(0.0);
        
        GeneralPath path = new GeneralPath();
        //Path2D path = new Path2D.Double();
        
        path.moveTo((int) xps, (int) yp);
        
        for(int p = 0; p < npoints; p++){
            
            dataX  = dataset.getDataX(p);
            dataY  = dataset.getDataY(p);
            errorX = dataset.getDataEX(p);
            
            xps = frame.getAxisPointX(dataX - errorX*0.5);
            xpe = frame.getAxisPointX(dataX + errorX*0.5);
            //System.out.println(" x =  " + dataX + " y = " + dataY + " xps = "
            //        + xps + " yp = " + yp);
            //yp  = frame.getAxis(1).getDimension().getMax() - 
            //        frame.getAxis(1).getAxisPosition(dataY)                    
            //        + frame.getAxis(1).getDimension().getMin(); 
            
            //if(dataY<0.1) dataY = 0.0;
            yp = frame.getAxisPointY(dataY);
            
            //System.out.println("histogram = " + p + " " + dataY + " " + yp);
            path.lineTo((int) xps, (int) yp);
            path.lineTo((int) xpe, (int) yp);
            
        }
        
        //yp  = frame.getAxis(1).getDimension().getMax() - 
        //        frame.getAxis(1).getAxisPosition(0.0)                 
        //        + frame.getAxis(1).getDimension().getMin();
        yp = frame.getAxisPointY(0.0);
        path.lineTo((int) xpe, (int) yp);
        int fillColor = this.dataset.getAttributes().get(AttributeType.FILL_COLOR);
        if(fillColor>=0){
            g2d.setColor(TStyle.getColor(fillColor));
            g2d.fill(path);
        }
        //g2d.fill(path);
        int lineColor = this.dataset.getAttributes().get(AttributeType.LINE_COLOR);
        g2d.setColor(TStyle.getColor(lineColor));
        g2d.draw(path);
    }
    
    @Override
    public Dimension3D getDataRegion() {
        
        this.dataRegion.set(
                dataset.getDataX(0), dataset.getDataX(0), 
                0.0, 0.0,0.0,1.0
                );
        int dataSize = dataset.getDataSize(0);
        for(int p = 0; p < dataSize; p++){
            double x1 = dataset.getDataX(p)-dataset.getDataEX(p)*0.5;
            double x2 = dataset.getDataX(p)+dataset.getDataEX(p)*0.5;
            this.dataRegion.grow(dataset.getDataX(p), dataset.getDataY(p),0.5);
            this.dataRegion.getDimension(0).grow(x1);
            this.dataRegion.getDimension(0).grow(x2);
        }

        this.dataRegion.getDimension(1).addPadding(0.0, 0.2);
        
        return dataRegion;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class HistogramPlotter implements IDataSetPlotter  {
    String       plottingOptions = "";
    IDataSet     dataset = null;
    Dimension2D  dataRegion  = new Dimension2D();
    String       datasetName = "";
    public HistogramPlotter(IDataSet ds){
        dataset = ds;
        datasetName = ds.getName();
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
            
        double xps = frame.getAxis(0).getAxisPosition(dataX - errorX*0.5);
        double xpe = frame.getAxis(0).getAxisPosition(dataX + errorX*0.5);
        double yp  = frame.getAxis(1).getDimension().getMax() - 
                frame.getAxis(1).getAxisPosition(0.0)                 
                + frame.getAxis(1).getDimension().getMin();
            
        GeneralPath path = new GeneralPath();
        path.moveTo((int) xps, (int) yp);
        
        for(int p = 0; p < npoints; p++){
            
            dataX  = dataset.getDataX(p);
            dataY  = dataset.getDataY(p);
            errorX = dataset.getDataEX(p);
            
            xps = frame.getAxis(0).getAxisPosition(dataX - errorX*0.5);
            xpe = frame.getAxis(0).getAxisPosition(dataX + errorX*0.5);
            yp  = frame.getAxis(1).getDimension().getMax() - 
                    frame.getAxis(1).getAxisPosition(dataY)                    
                    + frame.getAxis(1).getDimension().getMin();                    
            path.lineTo((int) xps, (int) yp);
            path.lineTo((int) xpe, (int) yp);
            
        }
        
        yp  = frame.getAxis(1).getDimension().getMax() - 
                frame.getAxis(1).getAxisPosition(0.0)                 
                + frame.getAxis(1).getDimension().getMin();
        path.lineTo((int) xpe, (int) yp);
        
        g2d.setColor(Color.blue);
        g2d.fill(path);
        
        g2d.setColor(Color.red);

        g2d.draw(path);
    }

    @Override
    public Dimension2D getDataRegion() {
        
        this.dataRegion.set(
                dataset.getDataX(0), dataset.getDataX(0), 
                0.0, 0.0
                );
        int dataSize = dataset.getDataSize(0);
        for(int p = 0; p < dataSize; p++){
            double x1 = dataset.getDataX(p)-dataset.getDataEX(p)*0.5;
            double x2 = dataset.getDataX(p)+dataset.getDataEX(p)*0.5;
            this.dataRegion.grow(dataset.getDataX(p), dataset.getDataY(p));
            this.dataRegion.getDimension(0).grow(x1);
            this.dataRegion.getDimension(0).grow(x2);
        }
        double ymin = this.dataRegion.getDimension(1).getMin();
        double ymax = this.dataRegion.getDimension(1).getMax();
        
        this.dataRegion.getDimension(1).setMinMax(ymin,ymax + Math.abs(ymax-ymin)*0.2);
        
        return dataRegion;
    }
    
}

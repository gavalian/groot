/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;


import org.jlab.groot.base.TStyle;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

/**
 *
 * @author gavalian
 */
public class HistogramPlotter  implements IDataSetPlotter {
    
    String       plottingOptions = "";
    IDataSet     dataset = null;
    Dimension3D  dataRegion  = new Dimension3D();
    String       datasetName = "";
    public GeneralPath path = null;
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
        
        path = new GeneralPath();
        //Path2D path = new Path2D.Double();
        int fillColor = this.dataset.getAttributes().getFillColor();
        int lineColor = this.dataset.getAttributes().getLineColor();
        path.moveTo((int) xps, (int) yp);
        if(this.dataset.getAttributes().getDrawOptions().contains("E")==false){
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

            if(fillColor>0){
                g2d.setColor(TStyle.getColor(fillColor));
                g2d.fill(path);
            }
            //g2d.fill(path);
            g2d.setStroke(new BasicStroke(this.dataset.getAttributes().getLineWidth()));            
            g2d.setColor(TStyle.getColor(lineColor));
            g2d.draw(path);
        }
        if(this.dataset.getAttributes().getDrawOptions().contains("E")){
        	for(int p = 0; p < npoints; p++){
                    double xpc = frame.getAxisPointX(this.dataset.getDataX(p));
                    double xpL = frame.getAxisPointX(this.dataset.getDataX(p)-0.5*this.dataset.getDataEX(p));
                    double xpH = frame.getAxisPointX(this.dataset.getDataX(p)+0.5*this.dataset.getDataEX(p));
                    
                    //double yp = frame.getAxis(1).getAxisPosition(graphDataSet.getDataY(p));
                    //double yp1 = frame.getAxisPointY(this.dataset.getDataY(p));
                    //int    yc = (int) (frame.getAxis(1).getDimension().getMax() - yp 
                    //        + frame.getAxis(1).getDimension().getMin());
                    
                    // double xpL = frame.getAxisPointX(this.dataset.getDataX(p) - this.dataset.getDataEX(p));
                    //double xpH = frame.getAxisPointX(this.dataset.getDataX(p) + this.dataset.getDataEX(p));
                    double ypc = frame.getAxisPointY(this.dataset.getDataY(p));
                    double ypL = frame.getAxisPointY(this.dataset.getDataY(p) - this.dataset.getDataEY(p));
                    double ypH = frame.getAxisPointY(this.dataset.getDataY(p) + this.dataset.getDataEY(p));
                    
                    g2d.setColor(TStyle.getColor(lineColor));
                    g2d.setStroke(new BasicStroke(this.dataset.getAttributes().getLineWidth()));
                    //g2d.drawLine((int) xpL, (int) yp, (int) xpH, (int) yp);
                    g2d.drawLine((int) xpc, (int) ypL, (int) xpc, (int) ypH);
                    g2d.drawLine((int) xpL, (int) ypc, (int) xpH, (int) ypc);
                }
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.Color;
import java.awt.Graphics2D;
import org.jlab.groot.base.ColorPalette;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

/**
 *
 * @author gavalian
 */
public class Histogram2DPlotter implements IDataSetPlotter {

    String plotterName = "";
    String plotterOptions = "";
    IDataSet   dataSet = null;
    Dimension3D  dataRegion  = new Dimension3D();
    Dimension1D  dataRegionZ = new Dimension1D();
    ColorPalette palette     = new ColorPalette();
    
    public Histogram2DPlotter(IDataSet ds){
        this.dataSet = ds;
    }
    
    @Override
    public String getOptions() {
        return plotterOptions;
    }

    @Override
    public void setOptions(String opt) {
        this.plotterOptions = opt;
    }

    @Override
    public String getName() {
        return dataSet.getName();
    }

    @Override
    public IDataSet getDataSet() {
        return this.dataSet;
    }

    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {
        
        int npointsX = dataSet.getDataSize(0);
        int npointsY = dataSet.getDataSize(1);
        frame.setDrawAxisZ(true);
        updateRegion();
        
        for(int xd = 0; xd < dataSet.getDataSize(0); xd++){
                for(int yd = 0; yd < dataSet.getDataSize(1); yd++){
                    double dataX  = dataSet.getDataX(xd);
                    double dataY  = dataSet.getDataY(yd);
                    double errorX = dataSet.getDataEX(xd);
                    double errorY = dataSet.getDataEY(yd);
                    
                    double xps = frame.getAxisPointX(dataX - errorX*0.5);
                    double xpe = frame.getAxisPointX(dataX + errorX*0.5);
                    double yps = frame.getAxisPointY(dataY - errorY*0.5);
                    double ype = frame.getAxisPointY(dataY + errorY*0.5);
                    double height = yps - ype;
                    
                    double dataWeight  = this.dataSet.getData(xd, yd);
                    boolean zAxisLog = frame.getAxisZ().getLog();
                    //System.out.println("2D plotter axis Z " + zAxisLog);
                    Color  weightColor;
                    if(frame.getAxisZ().isAutoScale()){
                    	weightColor = palette.getColor3D(dataWeight,dataRegionZ.getMax(), zAxisLog);
                    }else{
                    	dataWeight -= frame.getAxisZ().getRange().getMin();
                    	weightColor = palette.getColor3D(dataWeight,frame.getAxisZ().getRange().getMax()-frame.getAxisZ().getRange().getMin(), zAxisLog);
                    	//System.out.println("h2d: dataweight "+dataWeight+" max: "+frame.getAxisZ().getRange());
                    }
                    /*System.out.println("drawing point " + xd + " " + yd
                            + " xps/xpe = " + (int) xps + "  " + (int) xpe 
                            + " yps/ype = " + (int) yps + "  " + (int) ype
                    );*/
                    g2d.setColor(weightColor);
                    g2d.fillRect( (int) xps, (int) ype , 
                            (int) (xpe-xps) + 1,
                            (int) (yps-ype) + 1 );
                    
                }
        }
        if(this.dataSet.getAttributes().isDrawAxis()){
        	palette.draw(g2d, 50, 50, 50, 50, frame.getAxisZ().getRange().getMin(),dataRegionZ.getMax(), frame.getAxisZ().getLog());
        }
        
    }

    public void updateRegion(){
        this.dataRegionZ.setMinMax(dataSet.getData(0, 0),dataSet.getData(0, 0));
        for(int xd = 0; xd < dataSet.getDataSize(0); xd++){
                for(int yd = 0; yd < dataSet.getDataSize(1); yd++){
                    this.dataRegionZ.grow(dataSet.getData(xd, yd));
                }
        }
    }
    
    @Override
    public Dimension3D getDataRegion() {
        if(this.dataSet.getDataSize(0)>0&&this.dataSet.getDataSize(1)>0){
            this.dataRegion.set(
                    this.dataSet.getDataX(0),
                    this.dataSet.getDataX(0),
                    this.dataSet.getDataY(0),
                    this.dataSet.getDataY(0),
                    this.dataSet.getData(0, 0),
                    this.dataSet.getData(0, 0)                    
                    );
            
            for(int xd = 0; xd < dataSet.getDataSize(0); xd++){
                for(int yd = 0; yd < dataSet.getDataSize(1); yd++){
                    dataRegion.grow(
                            dataSet.getDataX(xd) - dataSet.getDataEX(xd)*0.5,
                            dataSet.getDataY(yd) - dataSet.getDataEY(yd)*0.5,
                            dataSet.getData(xd, yd));
                    dataRegion.grow(
                            dataSet.getDataX(xd) + dataSet.getDataEX(xd)*0.5,
                            dataSet.getDataY(yd) + dataSet.getDataEY(yd)*0.5,
                            dataSet.getData(xd, yd)
                            );
                }
            }
        }
        //System.out.println(this.dataRegion.toString());
        return this.dataRegion;
    }
    
}

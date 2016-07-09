/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.Color;
import java.awt.Graphics2D;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

/**
 *
 * @author gavalian
 */
public class GraphErrorsPlotter implements IDataSetPlotter {

    IDataSet     graphDataSet = null;
    Dimension3D  graphDataRegion = new Dimension3D();
    String       plottingOptions = "";
    
    public GraphErrorsPlotter(IDataSet  dataset){
        graphDataSet = dataset;
    }
    
    @Override
    public String getName() {
        return graphDataSet.getName();
    }

    @Override
    public IDataSet getDataSet() {
        return graphDataSet;
    }

    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {
        int npoints = graphDataSet.getDataSize(0);
        for(int p = 0; p < npoints; p++){
            double xp = frame.getAxisPointX(graphDataSet.getDataX(p));
            //double yp = frame.getAxis(1).getAxisPosition(graphDataSet.getDataY(p));
            double yp = frame.getAxisPointY(graphDataSet.getDataY(p));
            //int    yc = (int) (frame.getAxis(1).getDimension().getMax() - yp 
            //        + frame.getAxis(1).getDimension().getMin());
            
            g2d.setColor(Color.red);
            g2d.drawOval((int) xp, (int) yp, 5, 5);
        }
    }

    @Override
    public Dimension3D getDataRegion() {
        this.graphDataRegion.getDimension(0).setMinMax(
                graphDataSet.getDataX(0),
                graphDataSet.getDataX(0)
                );
        this.graphDataRegion.getDimension(1).setMinMax(
                graphDataSet.getDataY(0),
                graphDataSet.getDataY(0)
        );
        this.graphDataRegion.getDimension(2).setMinMax(0.0, 1.0);
        for(int p = 0; p < graphDataSet.getDataSize(0); p++){
            graphDataRegion.grow(graphDataSet.getDataX(p),graphDataSet.getDataY(p),0.5);
        }
        
        graphDataRegion.getDimension(0).addPadding(0.05);
        graphDataRegion.getDimension(1).addPadding(0.0,0.15);
        return graphDataRegion;
    }

    @Override
    public String getOptions() {
        return plottingOptions;
    }

    @Override
    public void setOptions(String opt) {
        plottingOptions = opt;
    }
    
}

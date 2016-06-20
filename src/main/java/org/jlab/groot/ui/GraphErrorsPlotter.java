/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class GraphErrorsPlotter implements IDataSetPlotter {

    IDataSet     graphDataSet = null;
    Dimension2D  graphDataRegion = new Dimension2D();
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
            double xp = frame.getAxis(0).getAxisPosition(graphDataSet.getDataX(p));
            double yp = frame.getAxis(1).getAxisPosition(graphDataSet.getDataY(p));
            int    yc = (int) (frame.getAxis(1).getDimension().getMax() - yp 
                    + frame.getAxis(1).getDimension().getMin());
            g2d.setColor(Color.red);
            g2d.drawOval((int) xp, (int) yc, 5, 5);
        }
    }

    @Override
    public Dimension2D getDataRegion() {
        this.graphDataRegion.getDimension(0).setMinMax(
                graphDataSet.getDataX(0),
                graphDataSet.getDataX(0)
                );
        this.graphDataRegion.getDimension(1).setMinMax(
                graphDataSet.getDataY(0),
                graphDataSet.getDataY(0)
        );
        for(int p = 0; p < graphDataSet.getDataSize(0); p++){
            graphDataRegion.grow(graphDataSet.getDataX(p),graphDataSet.getDataY(p));
        }
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

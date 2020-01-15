/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BasicStroke;
import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.jlab.groot.base.TStyle;
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
        int markerSize = this.getDataSet().getAttributes().getMarkerSize();
        int lineThickness = this.getDataSet().getAttributes().getLineWidth();
        int lineColor = this.getDataSet().getAttributes().getLineColor();
        int style = this.getDataSet().getAttributes().getMarkerStyle();
        BasicStroke strokePoint = new BasicStroke(lineThickness);
        BasicStroke strokeError = new BasicStroke(lineThickness);
        boolean drawLine = false;
        
        if(this.plottingOptions.contains("L")==true) drawLine = true;
        
        double prevXp = 0.0;
        double prevYp = 0.0;
        
        for(int p = 0; p < npoints; p++){
            double xp = frame.getAxisPointX(graphDataSet.getDataX(p));
            //double yp = frame.getAxis(1).getAxisPosition(graphDataSet.getDataY(p));
            double yp = frame.getAxisPointY(graphDataSet.getDataY(p));
            //int    yc = (int) (frame.getAxis(1).getDimension().getMax() - yp 
            //        + frame.getAxis(1).getDimension().getMin());
            
            if(drawLine==true&&p!=0){
                g2d.drawLine((int) xp, (int) yp, (int) prevXp, (int) prevYp);
                prevXp = xp; prevYp = yp;
            }
            double xpL = frame.getAxisPointX(graphDataSet.getDataX(p) - graphDataSet.getDataEX(p));
            double xpH = frame.getAxisPointX(graphDataSet.getDataX(p) + graphDataSet.getDataEX(p));
            
            double ypL = frame.getAxisPointY(graphDataSet.getDataY(p) - graphDataSet.getDataEY(p));
            double ypH = frame.getAxisPointY(graphDataSet.getDataY(p) + graphDataSet.getDataEY(p));
            
            g2d.setColor(TStyle.getColor(lineColor));
            g2d.setStroke(strokeError);
            g2d.drawLine((int) xpL, (int) yp, (int) xpH, (int) yp);
            g2d.drawLine((int) xp, (int) ypL, (int) xp, (int) ypH);
            if(this.getDataSet().getAttributes().getDrawOptions().contains("L")){
            	if(p<npoints-1){
            		double xpNext = frame.getAxisPointX(graphDataSet.getDataX(p+1));
                    double ypNext = frame.getAxisPointY(graphDataSet.getDataY(p+1));
                    g2d.drawLine((int)xp, (int)yp, (int) xpNext, (int) ypNext);
            	}
            }
            g2d.setColor(TStyle.getColor(this.getDataSet().getAttributes().getMarkerColor()));
            g2d.setStroke(strokePoint);
           
            if(style==0){
            	g2d.fillOval((int) xp - markerSize, (int) yp - markerSize , markerSize*2, markerSize*2);
            }else if(style==1){
            	g2d.fillRect((int) xp - markerSize, (int) yp - markerSize , markerSize*2, markerSize*2);
            }else if(style==2){
             	Polygon invertedTriangle = new Polygon();
             	invertedTriangle.addPoint((int)xp, (int)(yp-markerSize));
             	invertedTriangle.addPoint((int)xp+markerSize, (int)(yp+markerSize));
             	invertedTriangle.addPoint((int)xp-markerSize, (int)(yp+markerSize));
            	g2d.fillPolygon(invertedTriangle); 
            }else if(style==3){
            	Polygon triangle = new Polygon();
            	triangle.addPoint((int)xp, (int)(yp+markerSize));
            	triangle.addPoint((int)xp+markerSize, (int)(yp-markerSize));
            	triangle.addPoint((int)xp-markerSize, (int)(yp-markerSize));
            	g2d.fillPolygon(triangle);
            }else{
            	g2d.fillOval((int) xp - markerSize, (int) yp - markerSize , markerSize*2, markerSize*2);
            }
            
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
            graphDataRegion.grow(graphDataSet.getDataX(p) - graphDataSet.getDataEX(p),
                    graphDataSet.getDataY(p) - graphDataSet.getDataEY(p),0.5);
            graphDataRegion.grow(graphDataSet.getDataX(p) + graphDataSet.getDataEX(p),
                    graphDataSet.getDataY(p) + graphDataSet.getDataEY(p),0.5);
        }
        
        if(graphDataSet.getDataSize(0)==0){
            graphDataRegion.getDimension(0).setMinMax(0, 1.0);
            graphDataRegion.getDimension(1).setMinMax(0, 1.0);
            return graphDataRegion;
        }
        
        if(graphDataRegion.getDimension(0).getLength()<1e-9){
            double value = graphDataRegion.getDimension(0).getMin();            
            double deviation = Math.abs(value)*0.05;
            graphDataRegion.getDimension(0).setMinMax(value-deviation, value+deviation);
        } else {
            graphDataRegion.getDimension(0).addPadding(0.05);
        }
        if(graphDataRegion.getDimension(1).getLength()<1e-9){
            double value = graphDataRegion.getDimension(1).getMin();            
            double deviation = Math.abs(value)*0.10;
            graphDataRegion.getDimension(1).setMinMax(value-deviation, value+deviation);
        } else {
             graphDataRegion.getDimension(1).addPadding(0.10,0.10);
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

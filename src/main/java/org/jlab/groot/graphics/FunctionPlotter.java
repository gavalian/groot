/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.base.WobbleStroke;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.graphics.GraphicsAxisFrame;

/**
 *
 * @author gavalian
 */
public class FunctionPlotter implements IDataSetPlotter {

    private Dimension3D dataRegion  = new Dimension3D();
    private String      drawOptions = "";
    private IDataSet    functionData = null;
    
    public FunctionPlotter(IDataSet func){
        functionData = func;
    }
    
    @Override
    public String getOptions() {
        return this.drawOptions;
    }

    @Override
    public void setOptions(String opt) {
        this.drawOptions = opt;
    }

    @Override
    public String getName() {
        return this.functionData.getName();
    }

    @Override
    public IDataSet getDataSet() {
        return this.functionData;
    }
    
    @Override
    public void draw(Graphics2D g2d, GraphicsAxisFrame frame) {
        int npoints = functionData.getDataSize(0);
        GeneralPath path = new GeneralPath();
        double xp = frame.getAxisPointX(functionData.getDataX(0));
        double yp = frame.getAxisPointY(functionData.getDataY(0));
        path.moveTo(xp, yp);
        for(int p = 0; p < npoints; p++){
            xp = frame.getAxisPointX(functionData.getDataX(p));
            yp = frame.getAxisPointY(functionData.getDataY(p));
            path.lineTo(xp, yp);
        }
        int lineColor = functionData.getAttributes().getLineColor();
        int lineWidth = functionData.getAttributes().getLineWidth();
        int lineStyle = functionData.getAttributes().getLineStyle();
        
        g2d.setColor(TStyle.getColor(lineColor));
        g2d.setStroke(new BasicStroke(lineWidth));
        if(lineStyle==2){
        	final float dash1[] = {20.0f};
            g2d.setStroke(new BasicStroke(lineWidth,BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,20.0f,dash1,0.0f));
        }
        if(lineStyle==3){
        	final float dash1[] = {5.0f};
            g2d.setStroke(new BasicStroke(lineWidth,BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,5.0f,dash1,0.0f));
        }
        if(lineStyle==4){
        	final float dash1[] = {20.0f,5.0f};
            g2d.setStroke(new BasicStroke(lineWidth,BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,20.0f,dash1,0.0f));
        }
        if(lineStyle==5){
            WobbleStroke  stroke = new WobbleStroke(lineWidth);
            g2d.setStroke(stroke);
        }
        g2d.draw(path);
    }

    @Override
    public Dimension3D getDataRegion() {
        double xp = this.functionData.getDataX(0);
        double yp = this.functionData.getDataY(0);
        this.dataRegion.set(xp,xp,yp,yp,0.0,1.0);
        int npoints = functionData.getDataSize(0);
        for(int i = 0; i < npoints; i++){
            this.dataRegion.grow(functionData.getDataX(i), functionData.getDataY(i),0.5);
        }
        double length = this.dataRegion.getDimension(1).getLength();
        this.dataRegion.getDimension(1).setMinMax(
                this.dataRegion.getDimension(1).getMin(), 
                this.dataRegion.getDimension(1).getMin() + length*1.1);
        return dataRegion;
    }
 
}

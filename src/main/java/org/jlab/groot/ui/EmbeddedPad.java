/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import org.jlab.groot.graphics.Histogram2DPlotter;
import org.jlab.groot.graphics.IDataSetPlotter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jlab.groot.base.AttributeType;
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
    Map<String,IDataSetPlotter>   padDataSets = new LinkedHashMap<String,IDataSetPlotter>();
    /**
	 * @return the padDataSets
	 */
	public Map<String, IDataSetPlotter> getPadDataSets() {
		return padDataSets;
	}

	/**
	 * @param padDataSets the padDataSets to set
	 */
	public void setPadDataSets(Map<String, IDataSetPlotter> padDataSets) {
		this.padDataSets = padDataSets;
	}

	List<IDataSetPlotter>         padPlotters = new ArrayList<>();
    
    
    public EmbeddedPad(){
        
    }
    
    public EmbeddedPad(int x, int y, int width, int height){
        this.setDimension(x, y, width, height);        
    }
    
    public EmbeddedPad setDimension(int x, int y, int width, int height){
        this.padDimensions.getDimension(0).setMinMax(x,x+width);
        this.padDimensions.getDimension(1).setMinMax(y, y+height);
        return this;
    }
    
    public void reset(){
        this.padDataSets.clear();
        this.padPlotters.clear();
    }
    
    public void draw(Graphics2D g2d){
        //update(g2d);
        
        
        Dimension3D  dim = new Dimension3D();
        int counter = 0;
        
        for(Map.Entry<String,IDataSetPlotter>  entry : padDataSets.entrySet()){
            if(counter==0){
                dim.copy(entry.getValue().getDataRegion());
            } else {
                dim.combine(entry.getValue().getDataRegion());
            }
            counter++;
            /*
            Dimension2D region = entry.getValue().getDataRegion();
            axisFrame.getAxis(0).setRange(
                    region.getDimension(0).getMin(),
                    region.getDimension(0).getMax()
                    );
            axisFrame.getAxis(1).setRange(
                    region.getDimension(1).getMin(),
                    region.getDimension(1).getMax()
                    );
            System.out.println(entry.getKey() + " : " + region);
            System.out.println(axisFrame.getAxis(0));*/
            //entry.getValue().draw(g2d, axisFrame);
        }
        if(this.padPlotters.size()>0){
            if(this.padPlotters.get(0) instanceof Histogram2DPlotter){
                
            } else {
                if(axisFrame.getAxis(1).getLog()==true){
                    dim.getDimension(1).addPadding(1.0);
                }
            }
        }
        update(g2d);
        g2d.setColor(Color.BLACK);
        axisFrame.getAxis(0).setRange(dim.getDimension(0).getMin(), dim.getDimension(0).getMax());
        axisFrame.getAxis(1).setRange(dim.getDimension(1).getMin(), dim.getDimension(1).getMax());        
        axisFrame.getDimension().copy(padDimensions);
        //axisFrame.update(g2d);

        
        if(this.padPlotters.size()>0){
            if(padPlotters.get(0) instanceof Histogram2DPlotter){
                this.axisFrame.getAttributes().add(AttributeType.AXIS_DRAW_Z, 1);
            } else {
                this.axisFrame.getAttributes().add(AttributeType.AXIS_DRAW_Z, 0);
            }
        }
        
        axisFrame.update(g2d);
        for(Map.Entry<String,IDataSetPlotter>  entry : padDataSets.entrySet()){
          //  entry.getValue().draw(g2d, axisFrame);
        } 
        
        axisFrame.draw(g2d, padDimensions);
    }
    
    public void setAxisDrawZ(boolean flag){
        if(flag==true){
            this.axisFrame.getAttributes().add(AttributeType.AXIS_DRAW_Z, 1);
        } else {
            this.axisFrame.getAttributes().add(AttributeType.AXIS_DRAW_Z, 0);
        }        
    }
    
    public void update(Graphics2D g2d){
        //axisFrame.getAxis(0).setRange(4, 5);
        //axisFrame.getAxis(1).setRange(27,32);
    }
    
    public void addPlotter(IDataSetPlotter ip){        
        this.padDataSets.put(ip.getName(), ip);
        this.padPlotters.add(ip);
    }
    
    public void setAxisFontSize(int size){
        this.axisFrame.getAxis(0).getLabelFont().setFontSize(size);
        this.axisFrame.getAxis(1).getLabelFont().setFontSize(size);
    }
    
    public void show(){
        axisFrame.showAxis();
    }
    
    public void setLogX(boolean flag){
        //this.axisFrame.getAxis(0).setLog(flag);
    }
    
    public void setLogY(boolean flag){
        this.axisFrame.getAxis(1).setLog(flag);
    }
    
    public void setLogZ(boolean flag){
        this.axisFrame.getAxis(2).setLog(flag);
        //System.out.println(" SETTING Z AXIS TO " + this.axisFrame.getAxis(2).getLog());
    }
}

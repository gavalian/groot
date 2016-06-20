/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class GraphicsAxisFrame {

    List<GraphicsAxis>   graphicsAxis   = new ArrayList<GraphicsAxis>();    
    FontProperties       frameTitleFont = new FontProperties();
    LatexText            graphicsFrameTitle = new LatexText("");
    PadMargins           axisFrameMargins   = new PadMargins();
    
    public GraphicsAxisFrame(){
        setAxis(3);
    }
    
    public GraphicsAxisFrame(int naxis){
        setAxis(naxis);
    }
    /**
     * sets the dimension of the graphics axis.
     * @param naxis 
     */
    public final void setAxis(int naxis){
        graphicsAxis.clear();
        for(int i = 0; i < naxis; i++){
            graphicsAxis.add(new GraphicsAxis());            
        }
    }
    /**
     * returns GraphicsAxis from the array with given index.
     * @param index
     * @return 
     */
    public GraphicsAxis   getAxis(int index){
        return this.graphicsAxis.get(index);
    }
    
    public Point2D  getPoint(float x, float y){
        return new Point2D.Double(
                graphicsAxis.get(0).getAxisPosition(x),
                graphicsAxis.get(1).getAxisPosition(y)
        );
    }
    
    public void showAxis(){
        for(int i = 0; i < graphicsAxis.size(); i++){
            System.out.print(String.format("%3d : ", i));
            System.out.println(graphicsAxis.get(i));
        }
    }
    
    public void draw(Graphics2D g2d, Dimension2D frame){
        
        update(g2d);
        
        graphicsAxis.get(0).setDimension(
                this.axisFrameMargins.getLeftMargin(),
                (int) (frame.getDimension(0).getMax()-axisFrameMargins.getRightMargin()));
        
        graphicsAxis.get(1).setDimension(
                axisFrameMargins.getBottomMargin(),
                (int) (frame.getDimension(1).getMax()-axisFrameMargins.getTopMargin()));
        
        /**
         * Drawing X axis Line and Ticks and Labels
         */
        
        int xticks_y = (int) (frame.getDimension(1).getLength()-
                graphicsAxis.get(1).getDimension().getMin());
        
        g2d.drawLine(
                (int) graphicsAxis.get(0).getDimension().getMin(),
                xticks_y,
                (int) graphicsAxis.get(0).getDimension().getMax(),
                xticks_y
                );        
        List<Double>    xTicks = graphicsAxis.get(0).getAxisTicks();
        List<LatexText> xTexts = graphicsAxis.get(0).getAxisLabels();
        
        for(int ix = 0; ix < xTicks.size(); ix++){
            int xtick = (int) graphicsAxis.get(0).getAxisPosition(xTicks.get(ix));
            g2d.drawLine(xtick,xticks_y,xtick,xticks_y-5);
            
            xTexts.get(ix).drawString(g2d, xtick, xticks_y, 1,0);
            /*
            Rectangle2D rect = xTexts.get(ix).getBounds(g2d);
            g2d.drawString(xTexts.get(ix).getText().getIterator(),
                    (int) (xtick - 0.5*rect.getWidth()),
                    (int) (xticks_y + 5 + rect.getHeight()));
            */
        }
        
        /**
         * Draw the Y-axis 
         */
        int yticks_x = (int) graphicsAxis.get(0).getDimension().getMin();
        
        g2d.drawLine( yticks_x,
                (int) (frame.getDimension(1).getLength() - graphicsAxis.get(1).getDimension().getMin()),
                yticks_x,
                (int) (frame.getDimension(1).getLength() -graphicsAxis.get(1).getDimension().getMax())
                );
        
        List<Double>    yTicks = graphicsAxis.get(1).getAxisTicks();
        List<LatexText> yTexts = graphicsAxis.get(1).getAxisLabels();
        for(int iy = 0; iy < yTicks.size(); iy++){
            int ytick = (int) (
                    frame.getDimension(1).getLength()-
                    graphicsAxis.get(1).getAxisPosition(yTicks.get(iy))
                    );
            g2d.drawLine(yticks_x,ytick,yticks_x+5,ytick);
            yTexts.get(iy).drawString(g2d, yticks_x, ytick, 2,1);
            //xTexts.get(ix).drawString(g2d, xtick, xticks_y, 1,0);            
        }
        
        /**
         * if needed the Z-axis will be drawn
         */
    }
    
    public void update(Graphics2D g2d){
        
        this.graphicsAxis.get(0).setAxisDivisions(10);
        this.graphicsAxis.get(1).setAxisDivisions(10);
                
        int yStringWidth  = graphicsAxis.get(1).getSize(g2d, true);
        int xStringHeight = graphicsAxis.get(0).getSize(g2d, false);
        /**
         * Adjust N-divisions to ovoid overlaps of numbers on the screen
         */
        double fractionX  = graphicsAxis.get(0).getLabelFraction(g2d, false);
        int    ndivisionsX = graphicsAxis.get(0).getAxisDivisions();
        while(fractionX>0.6&&ndivisionsX>2){
            ndivisionsX = ndivisionsX - 1;
            graphicsAxis.get(0).setAxisDivisions(ndivisionsX);
            fractionX = graphicsAxis.get(0).getLabelFraction(g2d, false);
        }
        
        
        double fractionY   = graphicsAxis.get(1).getLabelFraction(g2d, true);
        int    ndivisionsY = 10;
        
        while(fractionY>0.6&&ndivisionsY>2){
            ndivisionsY = ndivisionsY - 1;
            graphicsAxis.get(1).setAxisDivisions(ndivisionsY);
            fractionY = graphicsAxis.get(1).getLabelFraction(g2d, true);
        }
        
        //System.out.println(" y width = " + yStringWidth + "  x height = " + xStringHeight);
        this.axisFrameMargins.setLeftMargin(yStringWidth+20);
        this.axisFrameMargins.setRightMargin(15);
        this.axisFrameMargins.setBottomMargin(xStringHeight+20);
        this.axisFrameMargins.setTopMargin(40);
        
    }
    
    public static void main(String[] args){
        GraphicsAxisFrame  frame = new GraphicsAxisFrame();
        frame.showAxis();
    }
}

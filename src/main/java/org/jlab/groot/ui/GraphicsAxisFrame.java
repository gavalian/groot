/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.Attributes;
import org.jlab.groot.base.ColorPalette;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class GraphicsAxisFrame {

    List<GraphicsAxis>   graphicsAxis   = new ArrayList<GraphicsAxis>();    
    FontProperties       frameTitleFont = new FontProperties();
    LatexText            graphicsFrameTitle = new LatexText("");
    Dimension2D          frameDimensions    = new Dimension2D();
    PadMargins           axisFrameMargins   = new PadMargins();
    ColorPalette         zAxisPalette       = new ColorPalette();
    private Attributes   frameAttributes    = new Attributes();
    
    public GraphicsAxisFrame(){
        setAxis(3);
        initAttributes();
    }
    
    public GraphicsAxisFrame(int naxis){
        setAxis(naxis);
        initAttributes();
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
    
    
    public  Attributes getAttributes(){
        return this.frameAttributes;
    }
    
    private void initAttributes(){
        
        frameAttributes.addString(AttributeType.STRING_TITLE, "");
        frameAttributes.addString(AttributeType.STRING_TITLE_X, "");
        frameAttributes.addString(AttributeType.STRING_TITLE_Y, "");
        
        frameAttributes.add(AttributeType.AXIS_LINE_COLOR,  1);
        frameAttributes.add(AttributeType.AXIS_LINE_WIDTH,  1);
        frameAttributes.add(AttributeType.AXIS_LINE_STYLE,  1);
        
        frameAttributes.add(AttributeType.FILL_COLOR, -11);
        
        frameAttributes.add(AttributeType.AXIS_TICKS_SIZE, 15);
        frameAttributes.add(AttributeType.AXIS_TICKS_STYLE, 0);
        frameAttributes.add(AttributeType.AXIS_LABEL_OFFSET, 4);
        frameAttributes.add(AttributeType.AXIS_TITLE_OFFSET, 5);
        frameAttributes.add(AttributeType.AXIS_FRAME_STYLE, 1);
        
        frameAttributes.add(AttributeType.AXIS_GRID_X, 1);
        frameAttributes.add(AttributeType.AXIS_GRID_Y, 1);
        
        frameAttributes.add(AttributeType.AXIS_DRAW_X, 1);
        frameAttributes.add(AttributeType.AXIS_DRAW_Y, 1);
        frameAttributes.add(AttributeType.AXIS_DRAW_Z, 0);
        
        frameAttributes.copyValues(TStyle.getStyle());
    }
    /**
     * returns GraphicsAxis from the array with given index.
     * @param index
     * @return 
     */
    public GraphicsAxis   getAxis(int index){
        return this.graphicsAxis.get(index);
    }
    
    public double getPointX(double value){
        return graphicsAxis.get(0).getAxisPosition(value);
    }
    
    public double getPointY(double value){
        double frac  = graphicsAxis.get(1).getRange().getFraction(value);
        /*int xticks_y = (int) (this.frameDimensions.getDimension(1).getMax()
                graphicsAxis.get(1).getDimension().getMin());
          */
        int xticks_y = (int) (frameDimensions.getDimension(1).getMax() -  
                (graphicsAxis.get(1).getDimension().getMin() - frameDimensions.getDimension(1).getMin()));
        double length = graphicsAxis.get(1).getDimension().getLength();
        /*System.out.println(" X = " + xticks_y + "  data = "  + value + 
                " fraction = " + frac + " length = " + length);
        System.out.println(graphicsAxis.get(1).getRange());*/
        return xticks_y - frac*length;
    }
    
    public Point2D  getPoint(float x, float y){
        return new Point2D.Double(
                graphicsAxis.get(0).getAxisPosition(x),
                graphicsAxis.get(1).getAxisPosition(y)
        );
    }
    
    public Dimension2D getDimension(){
        return this.frameDimensions;
    }
    
    
    public void showAxis(){
        for(int i = 0; i < graphicsAxis.size(); i++){
            System.out.print(String.format("%3d : ", i));
            System.out.println(graphicsAxis.get(i));
        }
    }
    
    public void draw(Graphics2D g2d, Dimension2D frame){
        //System.out.println("FRAME DRAW " + frame);
        update(g2d);
        //System.out.println("GF : Axis X " + this.getAxis(0).getDimension());
        //System.out.println("GF : Axis Y " + this.getAxis(1).getDimension());
        /*
        graphicsAxis.get(0).setDimension(
                (int) (frame.getDimension(0).getMin() + this.axisFrameMargins.getLeftMargin()),
                (int) (frame.getDimension(0).getMax()-axisFrameMargins.getRightMargin()));
        */
        /*
        graphicsAxis.get(1).setDimension(
                (int) ( axisFrameMargins.getBottomMargin()),
                (int) (frame.getDimension(1).getMax()-axisFrameMargins.getTopMargin()));
        */
        /*
        graphicsAxis.get(0).setDimension(
                (int) frame.getDimension(0).getMin(),
                (int) frame.getDimension(0).getMax());
        graphicsAxis.get(1).setDimension(
                (int) frame.getDimension(1).getMin(),
                (int) frame.getDimension(1).getMax());
        */
        //System.out.println(" FRAME SET " + this.frameDimensions);
        int tickSize = frameAttributes.get(AttributeType.AXIS_TICKS_SIZE);
        int axisLabelOffset = frameAttributes.get(AttributeType.AXIS_LABEL_OFFSET);
        int axisColor       = frameAttributes.get(AttributeType.AXIS_LINE_COLOR);
        g2d.setColor(this.zAxisPalette.getColor3D(axisColor));
        g2d.setStroke(new BasicStroke(frameAttributes.get(AttributeType.AXIS_LINE_WIDTH)));
        /**
         * Drawing X axis Line and Ticks and Labels
         */
        
        /*int xticks_y = (int) (frame.getDimension(1).getLength()-
                graphicsAxis.get(1).getDimension().getMin());
        */
        int xticks_y = (int) (frameDimensions.getDimension(1).getMax() -  
                (graphicsAxis.get(1).getDimension().getMin() - frameDimensions.getDimension(1).getMin()));
        //System.out.println(" Y AXIS = " + graphicsAxis.get(1).getDimension());
        //System.out.println("DRAING AXIS X at Y = " + xticks_y);
        
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
            /*System.out.println("drawing xtick  " + xTicks.get(ix)
            + "  X = " + xtick + "  Y = " + xticks_y);
            */
            g2d.drawLine(xtick,xticks_y,xtick,xticks_y-tickSize);
            
            xTexts.get(ix).drawString(g2d, xtick, xticks_y + axisLabelOffset, 1,0);
            /*
            Rectangle2D rect = xTexts.get(ix).getBounds(g2d);
            g2d.drawString(xTexts.get(ix).getText().getIterator(),
                    (int) (xtick - 0.5*rect.getWidth()),
                    (int) (xticks_y + 5 + rect.getHeight()));
            */
        }
        List<Double>  xMinorTicks = graphicsAxis.get(0).getAxisMinorTicks();
        for(int ix = 0; ix < xMinorTicks.size(); ix++){
            int xtick = (int) graphicsAxis.get(0).getAxisPosition(xMinorTicks.get(ix));
            g2d.drawLine(xtick,xticks_y,xtick,xticks_y-(int) (0.5* tickSize));
        }
        /**
         * Draw the Y-axis 
         */
        int yticks_x = (int) graphicsAxis.get(0).getDimension().getMin();
        
        g2d.drawLine( yticks_x,
                (int) (frame.getDimension(1).getLength() - graphicsAxis.get(1).getDimension().getMin()),
                yticks_x,
                (int) (frame.getDimension(1).getLength() - graphicsAxis.get(1).getDimension().getMax())
                );
        
        List<Double>    yTicks = graphicsAxis.get(1).getAxisTicks();
        List<LatexText> yTexts = graphicsAxis.get(1).getAxisLabels();
        for(int iy = 0; iy < yTicks.size(); iy++){
            int ytick = (int) getPointY(yTicks.get(iy));
            //System.out.println(" Draing axis Y "
            //+ " tick = " + yTicks.get(iy) +  "  position = " + ytick);
            /*(int) (
                    frame.getDimension(1).getLength()-
                    graphicsAxis.get(1).getAxisPosition(yTicks.get(iy))
                    );*/
            g2d.drawLine(yticks_x,ytick,yticks_x+tickSize,ytick);
            yTexts.get(iy).drawString(g2d, yticks_x-axisLabelOffset, ytick, 2,1);
            //xTexts.get(ix).drawString(g2d, xtick, xticks_y, 1,0);            
        }
        
        List<Double>  yMinorTicks = graphicsAxis.get(1).getAxisMinorTicks();
        for(int iy = 0; iy < yMinorTicks.size(); iy++){
            int ytick = (int) getPointY(yMinorTicks.get(iy));
            g2d.drawLine(yticks_x,ytick, yticks_x+ (int) (0.5*tickSize),ytick);
        }
        if(this.frameAttributes.get(AttributeType.AXIS_FRAME_STYLE)>0){
            
            g2d.drawRect(yticks_x, 
                    (int) ( xticks_y-this.graphicsAxis.get(1).getDimension().getLength()),
                    (int) this.graphicsAxis.get(0).getDimension().getLength(),
                    (int) this.graphicsAxis.get(1).getDimension().getLength()
                    );
        }
        /**
         * if needed the Z-axis will be drawn
         */
        
        //System.out.println("Drawing Z axis " + this.getAxis(2).getLog());
        if(this.frameAttributes.get(AttributeType.AXIS_DRAW_Z)>0){
            int x = (int) this.graphicsAxis.get(0).getDimension().getMax();
            int y = (int) ( this.frameDimensions.getDimension(1).getMin()
                    + this.axisFrameMargins.getTopMargin());
            int height = (int) this.graphicsAxis.get(1).getDimension().getLength();
            zAxisPalette.draw(g2d, x+4, y, 15, 
                    height,
                    0.0,400.0, this.getAxis(2).getLog());
        }
    }
    
    public void update(Graphics2D g2d){
        
        this.graphicsAxis.get(0).setDimension(
                (int) this.frameDimensions.getDimension(0).getMin(),
                (int) this.frameDimensions.getDimension(0).getMax());
        this.graphicsAxis.get(1).setDimension( 
                (int) this.frameDimensions.getDimension(1).getMin(),
                (int) this.frameDimensions.getDimension(1).getMax()
                );
        
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
        
        while(fractionY>0.8&&ndivisionsY>2){
            ndivisionsY = ndivisionsY - 1;
            graphicsAxis.get(1).setAxisDivisions(ndivisionsY);
            fractionY = graphicsAxis.get(1).getLabelFraction(g2d, true);
        }
        
        //System.out.println(" y width = " + yStringWidth + "  x height = " + xStringHeight);
        this.axisFrameMargins.setLeftMargin(yStringWidth+5);
        this.axisFrameMargins.setRightMargin(15);
        this.axisFrameMargins.setBottomMargin(xStringHeight+5);
        this.axisFrameMargins.setTopMargin(10); 
        
        
        if(this.frameAttributes.get(AttributeType.AXIS_DRAW_Z)>0){
            int zAxisWidth = zAxisPalette.getAxisWidth(g2d, 0, 0, 15, 
                   (int) this.frameDimensions.getDimension(1).getLength()
                    , 0.0,400.0,this.getAxis(2).getLog());
            //System.out.println(" MARGINS RIGHT moved by " + zAxisWidth);
            this.axisFrameMargins.setRightMargin(15+zAxisWidth);
        }
        
        this.graphicsAxis.get(1).setDimension(
                (int) frameDimensions.getDimension(1).getMin()+axisFrameMargins.getBottomMargin(),
                (int) frameDimensions.getDimension(1).getMax()-axisFrameMargins.getTopMargin() );
        this.graphicsAxis.get(0).setDimension(
                (int) frameDimensions.getDimension(0).getMin()+axisFrameMargins.getLeftMargin(),
                (int) frameDimensions.getDimension(0).getMax()-axisFrameMargins.getRightMargin()
        );
               
    }
    
    public static void main(String[] args){
        GraphicsAxisFrame  frame = new GraphicsAxisFrame();
        frame.showAxis();
    }
}

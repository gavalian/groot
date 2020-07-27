/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.attr.LineStyles;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeInsets;
import org.jlab.jnp.graphics.base.NodeRegion2D;

/**
 * Graphics Axis class to draw axis inside a pad
 * and host data plotters.
 * @author gavalian
 */
public class GraphicsAxis extends Node2D {
    
    private Color           axisColor = Color.BLACK;
    private BasicStroke        stroke = new BasicStroke(1);
    private NodeInsets     axisInsets = new NodeInsets();
    
    private final AxisNode2D     axisX = new AxisNode2D(AxisNode2D.AXIS_TYPE_HORIZONTAL);
    private final AxisNode2D     axisY = new AxisNode2D(AxisNode2D.AXIS_TYPE_VERTICAL);
    
    private final AttributeCollection axisFrameAttributes = 
            new AttributeCollection(
                    new AttributeType[]{AttributeType.PAD_TITLE},
                    new String[]{"Graph Title"});
    
    private Rectangle2D  axisFrameLimits = null;
    private List<DataNode2D> dataNodes = new ArrayList<DataNode2D>();
    
    public GraphicsAxis(){
        super(20,20);
        //axisInsets.set(10,10,10,10);
        axisInsets.set(0,0,0,0);
        //this.getInsets().set(5,20,20,20);
        getInsets().right(20).left(50).top(20).bottom(40);
        //System.out.println("AXIS X \n" + axisX.getAttributes());
        axisFrameAttributes.append(axisX.getAttributes());
        axisFrameAttributes.addListener(axisX.getAttributes());
        axisFrameAttributes.addListener(axisY.getAttributes());
        //System.out.println("FRAME  \n" + axisFrameAttributes);
        setName("axis");
    }
    
    public AxisNode2D getAxisX(){ return axisX;}
    public AxisNode2D getAxisY(){ return axisY;}
    
    public AttributeCollection getAttributes(){
        return this.axisFrameAttributes;
    }
    
    public GraphicsAxis setStroke(BasicStroke __str){
        stroke = __str; return this;
    }
    
    public GraphicsAxis setAxisTicks(int ticks, String axis){
        if(axis.contains("X")==true){
            this.axisX.setAxisTicks(ticks);
        }
        if(axis.contains("Y")==true){
            this.axisY.setAxisTicks(ticks);
        }
        return this;
    }
    
    public void setAxisLimits(Rectangle2D rect){
        this.axisFrameLimits = rect;
    }
    
    public void setAxisLimits(double xmin, double xmax, double ymin, double ymax){
        this.axisFrameLimits = new Rectangle2D.Double(xmin,ymin,xmax-xmin,ymax-ymin);
    }
    
    public GraphicsAxis setAxisTitleOffset(Integer offset, String axis){
        if(axis.contains("X")==true){
            this.axisX.getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, offset.toString());
        }
        if(axis.contains("Y")==true){
            Integer xoffset = axisX.getAttributes().getInt(AttributeType.AXISTITLEOFFSET);
            this.axisY.getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, xoffset.toString());
            Integer voffset = offset - xoffset;
            this.axisY.getAttributes().changeValue(AttributeType.AXISTITLEOFFSETVERTICAL, voffset.toString());
        }
        return this;
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        
        axisX.setParent(this);
        axisY.setParent(this);
        
        NodeRegion2D bounds = getBounds();
        NodeInsets   insets =  this.getInsets();
        
        axisX.getBounds().copyFrom(bounds);
        axisX.getInsets().copyFrom(insets);
        
        axisY.getBounds().copyFrom(bounds);
        axisY.getInsets().copyFrom(insets);
        
        Rectangle2D trans = getTranslation();
        List<Node2D>  children = getChildren();
        if(children.size()>0){
            DataNode2D node = (DataNode2D) children.get(0);
            node.getDataBounds(trans);
        }
        //System.out.println(trans);
        
        if(this.axisFrameLimits==null) {
            axisX.setAxisRegion(trans);
            axisY.setAxisRegion(trans);
        } else {
            trans.setRect(axisFrameLimits.getX(), 
                    axisFrameLimits.getY(),
                    axisFrameLimits.getWidth(),
                    axisFrameLimits.getHeight());
            axisX.setAxisRegion(axisFrameLimits);
            axisY.setAxisRegion(axisFrameLimits);
        }
        
        axisX.drawLayer(g2d, 0);
        axisY.drawLayer(g2d, 0);
        
        /*axisX.setTitle("Q^2[ Gev^2/c]");
        axisY.setTitle("#phi^2 (#gamma) title [GeV]");
        */
        //if(this.getChildren().size()>0){
            
        //}
        for(DataNode2D node : this.dataNodes){
            node.drawLayer(g2d, layer);
        }
        //drawChildren(g2d,layer);
        //System.out.println("drawing axis ----");
        axisX.drawLayer(g2d, 1);
        axisY.drawLayer(g2d, 1);
        
       /* g2d.setFont(  new Font("Symbol",Font.PLAIN,28));
        g2d.drawString("a", (int) ( bounds.getX() + 20), (int) (bounds.getY() + 20));
        g2d.drawString("b", (int) ( bounds.getX() + 40), (int) (bounds.getY() + 20));*/
       
        //g2d.drawString("helvetica", (int) ( bounds.getX() + 20), (int) (bounds.getY() + 80));
    }
    
    public void addDataNode(DataNode2D node){
        if(dataNodes.size()==0){
            axisX.setTitle(node.getDataSet().getAttributes().getTitleX());
            axisY.setTitle(node.getDataSet().getAttributes().getTitleY());
        }
        node.setParent(this);
        dataNodes.add(node);
    }
    
    @Override
    public double transformX(double x){
        double xf = super.transformX(x);
        return getBounds().getX() - getInsets().getLeft() + xf;
    }
    
    @Override
    public double transformY(double y){
        double yf = super.transformY(y);
        //return yf;
        return getBounds().getHeight() + getBounds().getY() + getInsets().getTop() - yf;
    }
    
    
}

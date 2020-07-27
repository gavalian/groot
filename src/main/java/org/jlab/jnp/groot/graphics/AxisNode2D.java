/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.attr.AxisAttributes;
import org.jlab.jnp.graphics.attr.LineStyles;
import org.jlab.jnp.graphics.attr.Theme;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeInsets;
import org.jlab.jnp.graphics.base.NodeRegion2D;

/**
 *
 * @author gavalian
 */
public class AxisNode2D extends Node2D {
    
    public static int   AXIS_TYPE_VERTICAL = 1;
    public static int AXIS_TYPE_HORIZONTAL = 2;
    public static int      AXIS_TYPE_COLOR = 3;    
    
    private          int   AXIS_TYPE = AxisNode2D.AXIS_TYPE_HORIZONTAL;
    
    private final NodeRegion2D  axisLimits   = new NodeRegion2D();
    private final List<Double> axisMarkers   = new ArrayList<Double>(); 
    private final  NiceScale   axisScale     = new NiceScale(0.0,1.0);
    private final  LatexText   axisText      = new LatexText("1.0");
    private final  LatexText   axisTitleText = new LatexText("title");
    private int    maxAxisTicks  = 10;
    
    private String axisTitle = "";
    
    private final AttributeCollection axisAttributes = 
            new AttributeCollection(
                    new AttributeType[]{AttributeType.AXISLINECOLOR,
                        AttributeType.AXISLINEWIDTH,AttributeType.AXISLINESTYLE,
                        AttributeType.AXISTICKSIZE,AttributeType.AXISLABELOFFSET,
                        AttributeType.AXISTITLEOFFSET,AttributeType.AXISTITLEOFFSETVERTICAL,
                        AttributeType.AXISDRAWBOX,AttributeType.AXISDRAWTICKS,
                        AttributeType.AXISDRAWLINE,AttributeType.AXISDRAWGRID,                        
                    },                    
                    new String[]{"0",
                        "1","1",
                        "8","5","10","10",
                        "true","true","true","fasle"});
    
//    private final  AxisAttributes  axisAttributes = new AxisAttributes();
    
    public AxisNode2D(){
        super(20,20);
    }
    
    public AxisNode2D(int type){
        super(20,20);
        this.AXIS_TYPE = type;
        if(this.AXIS_TYPE == AxisNode2D.AXIS_TYPE_HORIZONTAL){
            axisAttributes.setName("X axis");
        }
        if(this.AXIS_TYPE == AxisNode2D.AXIS_TYPE_VERTICAL){
            axisAttributes.setName("Y axis");
        }
    }    
    
    public AxisNode2D setAxisTicks(int ticks){ this.maxAxisTicks = ticks;return this;}
    
    public AxisNode2D setTitle(String title) { axisTitle = title; return this;}
    
    public String     getTitle(){ return axisTitle;}
    
    public AttributeCollection getAttributes(){
        return this.axisAttributes;
    }
    
    public void setAxisFont(String fontname, int fontsize, int fontFace){
        this.axisText.setFontSize(fontsize);
        this.axisText.setFont(fontname, fontFace);
    }
    
    public void setAxisTitleFont(String fontname, int fontsize, int fontFace){
        this.axisTitleText.setFontSize(fontsize);
        this.axisTitleText.setFont(fontname, fontFace);
    }
    
    public void setAxisRegion(NodeRegion2D node){
        axisLimits.copyFrom(node);
        if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_HORIZONTAL){
            axisScale.setMaxTicks(this.maxAxisTicks);
            axisScale.setMinMaxPoints(axisLimits.getX(), axisLimits.getX()+axisLimits.getWidth());
            axisScale.getTicks(axisMarkers);
            axisScale.setOrderString();
             /*System.out.printf(" AXIS TYPE HORIZONTAL : %8.4f %8.4f\n",
                    axisLimits.getX(),axisLimits.getX() + axisLimits.getWidth());*/
        } else if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_VERTICAL){
            axisScale.setMaxTicks(this.maxAxisTicks);
            axisScale.setMinMaxPoints(axisLimits.getY(), axisLimits.getY()+axisLimits.getHeight());
            axisScale.getTicks(axisMarkers);
            axisScale.setOrderString();
           /* System.out.printf(" AXIS TYPE VERTICAL : %8.4f %8.4f\n",
                    axisLimits.getY(),axisLimits.getY() + axisLimits.getHeight());
            */
        }
    }
    
    public void setAxisRegion(Rectangle2D node){
        axisLimits.set(node.getX(),node.getY(),node.getWidth(),node.getHeight() );
        if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_HORIZONTAL){
            axisScale.setMaxTicks(this.maxAxisTicks);
            axisScale.setMinMaxPoints(axisLimits.getX(), axisLimits.getX()+axisLimits.getWidth());
            axisScale.getTicks(axisMarkers);
            axisScale.setOrderString();
            /*System.out.printf(" AXIS TYPE HORIZONTAL : %8.4f %8.4f : %5d\n",
                    axisLimits.getX(),axisLimits.getX() + axisLimits.getWidth(),
                    axisMarkers.size());*/
        } else if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_VERTICAL){
            axisScale.setMaxTicks(this.maxAxisTicks);
            axisScale.setMinMaxPoints(axisLimits.getY(), axisLimits.getY()+axisLimits.getHeight());
            
            axisScale.getTicks(axisMarkers);
            axisScale.setOrderString();
            System.out.printf("Setting axis : %f %f \n",axisLimits.getY(), axisLimits.getY()+axisLimits.getHeight());
            System.out.printf(" SPACING = %f , order = %s\n",axisScale.getSpacing(), axisScale.getOrderString());
            /*System.out.printf(" AXIS TYPE VERTICAL : %8.4f %8.4f : %5d\n",
                    axisLimits.getY(),axisLimits.getY() + axisLimits.getHeight(),
                    axisMarkers.size());*/

        }
    }
    
    public void drawAxisGrid(Graphics2D g2d){
        
        if(this.axisAttributes.getBoolean(AttributeType.AXISDRAWBOX)==false) return;
        
        Node2D       parent = this.getParent();
        
        NodeRegion2D bounds =  getBounds();
        NodeInsets   insets =  getInsets(); 
        g2d.setColor(new Color(210,210,210));
        g2d.setStroke(LineStyles.getStroke(3));
        //this.showAxis();
        if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_HORIZONTAL){
            double x1 = bounds.getX();
            double x2 = bounds.getX();
            double y1 = bounds.getY();
            double y2 = bounds.getY() + bounds.getHeight();
            //g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            //System.out.println("axis ticks = " + this.axisMarkers.size());
            for(int i = 0; i < this.axisMarkers.size(); i++){
                double xcoord = parent.transformX(axisMarkers.get(i));
                g2d.drawLine( (int) xcoord, (int) y2 , (int) xcoord, (int) (y1));
                /*axisText.setText(String.format("%.2f", axisMarkers.get(i)));
                axisText.drawString(g2d, (int) xcoord, (int) (y2+4), 
                        LatexText.ALIGN_CENTER, LatexText.ALIGN_TOP);*/
            }
        } else if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_VERTICAL){
            double x1 = bounds.getX();
            double x2 = bounds.getX() + bounds.getWidth();
            double y1 = bounds.getY();
            double y2 = bounds.getY() + bounds.getHeight();
            //g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            //System.out.printf(" DRAWING AXIS VERTICAL : %.2f %.2f %.2f %.2f\n",x1,y1,x2,y2);
            //System.out.println(bounds);
            
            for(int i = 0; i < this.axisMarkers.size(); i++){
                double ycoord = parent.transformY(axisMarkers.get(i));
                g2d.drawLine( (int) x1 , (int) ycoord , (int) (x2), (int) ycoord);
                /* axisText.setText(String.format("%.2f", axisMarkers.get(i)));
                axisText.drawString(g2d, (int) (x1-4), (int) ycoord, 
                        LatexText.ALIGN_RIGTH, LatexText.ALIGN_CENTER);*/
            }
        }
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
                
        if(layer == 0 ) { 
            this.drawAxisGrid(g2d);
            return;
        }
        Node2D       parent = this.getParent();
        Theme        theme  = Theme.getInstance();
        
        NodeRegion2D bounds =  getBounds();
        NodeInsets   insets =  getInsets();                       
        int strokeWidth = this.axisAttributes.getInt(AttributeType.AXISLINEWIDTH);
        int axisColor   = this.axisAttributes.getInt(AttributeType.AXISLINECOLOR);
        g2d.setColor(theme.getPalette().getColor(axisColor));
        this.axisText.setColor(theme.getPalette().getColor(axisColor));
        g2d.setStroke(LineStyles.getStrokeWidth(strokeWidth));
        
        if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_HORIZONTAL){
            double x1 = bounds.getX();
            double x2 = bounds.getX() + bounds.getWidth();
            double y1 = bounds.getY() + bounds.getHeight();
            double y2 = bounds.getY() + bounds.getHeight();
            double y3 = bounds.getY();
            
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            if(this.axisAttributes.getBoolean(AttributeType.AXISDRAWBOX)==true){
                g2d.drawLine((int) x1, (int) y3, (int) x2, (int) y3);
            }
            //System.out.println("axis ticks = " + this.axisMarkers.size());
            int  tickOffset = axisAttributes.getInt(AttributeType.AXISTICKSIZE);
            int labelOffset = axisAttributes.getInt(AttributeType.AXISLABELOFFSET);
            int titleOffset = axisAttributes.getInt(AttributeType.AXISTITLEOFFSET);
            
            if(tickOffset<0){ labelOffset = labelOffset + Math.abs(tickOffset);}
            int maxOffset = 0;
            for(int i = 0; i < this.axisMarkers.size(); i++){
                double xcoord = parent.transformX(axisMarkers.get(i));
                g2d.drawLine( (int) xcoord, (int) y2 , (int) xcoord, (int) (y2-tickOffset));                                
                //axisText.setText(String.format(axisScale.getOrderString(), axisMarkers.get(i)));
                String label = String.format(axisScale.getOrderString(), axisMarkers.get(i));
                int offset = axisText.drawString(label,g2d, (int) xcoord, (int) (y2 + labelOffset), 
                        LatexText.ALIGN_CENTER, LatexText.ALIGN_TOP,0);
                if(offset>maxOffset) maxOffset = offset;
            }
            
            int xtitle = (int) (bounds.getX() + bounds.getWidth()/2.0);
            int ytitle = (int) (bounds.getY()+bounds.getHeight());
            //axisTitleText.setFont(new Font("Symbol",Font.PLAIN,18));
            axisTitleText.setText(axisTitle);                  
            axisTitleText.drawString(g2d, xtitle, ytitle + titleOffset + maxOffset, LatexText.ALIGN_CENTER, LatexText.ALIGN_TOP);
            
        } else if(this.AXIS_TYPE==AxisNode2D.AXIS_TYPE_VERTICAL){
            
            double x1 = bounds.getX();
            double x2 = bounds.getX();
            double x3 = bounds.getX() + bounds.getWidth();
            double y1 = bounds.getY();
            double y2 = bounds.getY() + bounds.getHeight();
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            if(this.axisAttributes.getBoolean(AttributeType.AXISDRAWBOX)==true){
                g2d.drawLine((int) x3, (int) y1, (int) x3, (int) y2);
            }
            //System.out.printf(" DRAWING AXIS VERTICAL : %.2f %.2f %.2f %.2f\n",x1,y1,x2,y2);
            //System.out.println(bounds);          
            int titleOffset = axisAttributes.getInt(AttributeType.AXISTITLEOFFSET);
            int titleOffsetVert = axisAttributes.getInt(AttributeType.AXISTITLEOFFSETVERTICAL);
            int tickOffset = axisAttributes.getInt(AttributeType.AXISTICKSIZE);
            int labelOffset = axisAttributes.getInt(AttributeType.AXISLABELOFFSET);
            
            System.out.println(" AXIS = " + axisScale.getSpacing());
            System.out.println(" ORDERGIN = " + axisScale.getOrderString());
            
            
            if(tickOffset<0){ labelOffset = labelOffset + Math.abs(tickOffset);}
            int maxOffset = 0;
            for(int i = 0; i < this.axisMarkers.size(); i++){
                double ycoord = parent.transformY(axisMarkers.get(i));
                g2d.drawLine( (int) x1 , (int) ycoord , (int) (x1+tickOffset), (int) ycoord);
                //axisText.setText(String.format(axisScale.getOrderString(), axisMarkers.get(i)));
                String label = String.format(axisScale.getOrderString(), axisMarkers.get(i));
                int offset = axisText.drawString(label,g2d, (int) (x1 - labelOffset), (int) ycoord, 
                        LatexText.ALIGN_RIGTH, LatexText.ALIGN_CENTER,1);
                if(offset>maxOffset) maxOffset = offset;
            }
            
            int xtitle = (int) (bounds.getX());
            int ytitle = (int) (bounds.getY()+bounds.getHeight()/2.0);
            axisTitleText.setText(axisTitle);
            axisTitleText.drawString(g2d, 
                    xtitle - titleOffset - maxOffset, ytitle , 
                    LatexText.ALIGN_CENTER, LatexText.ALIGN_CENTER,LatexText.ROTATE_LEFT);
        }
        
    }
    
    private void showAxis(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("TYPE : %2d : ", this.AXIS_TYPE));
        for(int i = 0; i < this.axisMarkers.size(); i++){
            str.append(String.format("%6.2f ", axisMarkers.get(i)));
        }
        System.out.println(str.toString());
    }
}

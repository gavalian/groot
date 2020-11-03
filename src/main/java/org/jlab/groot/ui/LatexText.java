/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import org.jlab.groot.base.ColorPalette;

import org.jlab.groot.base.TStyle;

/**
 *
 * @author gavalian
 */
public class LatexText {
    
    public  static int  ALIGN_LEFT    = 0;
    public  static int  ALIGN_CENTER  = 1;
    public  static int  ALIGN_RIGTH   = 2;
    public  static int  ALIGN_TOP     = 3;
    public  static int  ALIGN_BOTTOM  = 4;
    
    public  static int  ROTATE_NONE   = 11;
    public  static int  ROTATE_LEFT   = 12;
    public  static int  ROTATE_RIGHT  = 13;
    
    private String  textFamily   = "Times New Roman";//"Avenir";
    private int     textFontSize = 12;
    private Double  relativeX  = 0.0;
    private Double  relativeY  = 0.0;
    private AttributedString  latexString = null;
    private String            asciiString = "";
    private Integer           textColor   = 1;
    private Color             latexTextColor = Color.BLACK;
    
    
    public LatexText(String text, double xc, double yc){
        this.setText(text);
        this.setLocation(xc, yc);
        this.setFont(textFamily);
        this.setFontSize(textFontSize);
    }
    
     public LatexText(String text){
        this.setText(text);
        this.setLocation(0.0,0.0);
        this.setFont(textFamily);
        this.setFontSize(textFontSize);
    }
     
    public final void setText(String text){
    	asciiString = text;
        String ltx  = LatexTextTools.convertUnicode(text);
        latexString = LatexTextTools.convertSubAndSuperscript(ltx);
        this.setFont(this.textFamily);
        this.setFontSize(this.textFontSize);
    }
    
    public final String getTextString(){
        return asciiString;
    }
    
    public final void setLocation(double xr, double yr){
        this.relativeX = xr;
        this.relativeY = yr;
    }
    
    public int    getColor(){return this.textColor;}
    public void   setColor(int color){ 
        this.textColor = color;
        this.latexTextColor = ColorPalette.getColor(textColor);
    }
    
    public double getX(){ return this.relativeX;}
    public double getY(){ return this.relativeY;}
    
    public AttributedString getText(){ return this.latexString;}
    
    public final void setFont(String fontname){
        this.textFamily = fontname;
        if(this.latexString.getIterator().getEndIndex()>0){
        //System.out.println("INDEX = " + this.latexString.getIterator().getEndIndex());
            latexString.addAttribute(TextAttribute.FAMILY, fontname);
            latexString.addAttribute(TextAttribute.WEIGHT,TextAttribute.WEIGHT_EXTRABOLD);
        }
    }
    
    public final void setFontSize(int size){
        this.textFontSize = size;
        if(this.latexString.getIterator().getEndIndex()>0){
            latexString.addAttribute(TextAttribute.SIZE, (float) size);
        }
    }
    
    
public void drawString(Graphics2D  g2d, int x, int y, int alignX, int alignY, int rotate){
        
        if(rotate==LatexText.ROTATE_NONE){
            this.drawString(g2d, x, y, alignX, alignY);
            return;
        }
        
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        
        int posX = y;
        int posY = x;
        
        if(alignX==LatexText.ALIGN_CENTER) posX = posX + (int) (0.5*rect.getWidth());
        //if(alignX==LatexText.ALIGN_LEFT)   posX = posX + (int) rect.getWidth();
        if(alignY==LatexText.ALIGN_TOP)    posY = posY + (int) (rect.getHeight());
        //if(alignY==LatexText.ALIGN_CENTER)    posY = posY + (int) (0.5*rect.getHeight());
        AffineTransform orig = g2d.getTransform();
        g2d.rotate(-Math.PI/2);
        g2d.setColor(this.latexTextColor);
        g2d.drawString(latexString.getIterator(),-posX,posY);
        g2d.setTransform(orig);
    }
    
    public void drawString(Graphics2D  g2d, int x, int y, int alignX, int alignY){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.BOLD,this.textFontSize));
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        int  ascend   = fmg.getAscent();
        int leading   = fmg.getLeading();
        //System.out.println("ascend = " + ascend + " leading = " + leading);
        int  xp     = x;
        int  yp     = y + ascend;
        if(alignX==1) xp = (int) (xp-0.5*rect.getWidth());
        if(alignX==2) xp = (int) (xp-rect.getWidth());
        if(alignY==1) yp = (int) (y + 0.5*(ascend));
        if(alignY==2) yp = (int)  y;
        g2d.setColor(latexTextColor);
        g2d.drawString(latexString.getIterator(), xp, yp);        
    }
    
    public  Rectangle2D  getBoundsNumber(Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.BOLD,this.textFontSize));
        //System.out.println(" ACCEND = " + fmg.getAscent() + " LEAD " + fmg.getLeading()
        //+ "  DESCENT " + fmg.getDescent() + "  HEIGHT = " + fmg.getHeight());
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        //fmg.getHeight();
        return new Rectangle2D.Double(0,0,rect.getWidth(),fmg.getAscent());
    }
    
    public  Rectangle2D getBounds( Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.BOLD,this.textFontSize));
        //System.out.println(" ACCEND = " + fmg.getAscent() + " LEAD " + fmg.getLeading()
        //+ "  DESCENT " + fmg.getDescent() + "  HEIGHT = " + fmg.getHeight());
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        //fmg.getHeight();
        //return new Rectangle2D.Double(0,0,rect.getWidth(),fmg.getAscent());
        return rect;
    }
    
    public  Rectangle2D getBounds(FontMetrics  fm, Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(this.textFamily,Font.PLAIN,this.textFontSize));
        Rectangle2D rect = fmg.getStringBounds(this.latexString.getIterator(), 0,
                this.latexString.getIterator().getEndIndex(),g2d);
        return rect;
    }
    
    public static LatexText createFromDouble(double number, int order){
        //System.out.println("LatexUtils: number = " + number + "  order = " + order);
        String format = "%." + String.format("%df", order);
        String numString = String.format(format, number);
        //System.out.println("[opt] --> released string : " + numString);
        return new LatexText(numString);
    }
    
    public static void main(String[] args){
        LatexText lt = LatexText.createFromDouble(0.56789, 2);
    }
}

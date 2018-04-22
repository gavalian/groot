/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jlab.groot.base.FontProperties;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class PaveText {
    
    private int N_COLUMNS = 1;
    List< List<LatexText> >  paveTexts = new ArrayList< List<LatexText> >();
    private Dimension2D      paveDimension = new Dimension2D();
    private FontProperties   paveFont      = new FontProperties();
    private int              yPadding      = 5;
    private int              xPadding      = 5;
    private int              ySpacing      = 2;
    private int              xSpacing      = 10;
    private int              xLeftOffset   = 0;
    private Color            backgroundColor = Color.WHITE;
    private List<Color>      colors          = new ArrayList<Color>();
    
    public PaveText(int cols){
        this.N_COLUMNS = cols;
        this.paveDimension.set(100, 120, 100, 120);
        this.paveFont.setFontName("Avenir");
        this.paveFont.setFontSize(10);
    }
    
    public void addText(String... texts){
        if(texts.length!=this.N_COLUMNS){
            System.out.println(" ERROR : number of columns = " + this.N_COLUMNS);
            return;
        }
        List<LatexText> lt = new ArrayList<LatexText>();
        for(String item : texts){
            LatexText latex = new LatexText(item);
            latex.setFont(paveFont.getFontName());
            latex.setFontSize(paveFont.getFontSize());
            lt.add(latex);
        }
        this.paveTexts.add(lt);
    }
    
    public void addColor(Color col){
        colors.add(col);
    }
    
    public void setBackground(int r, int g, int b){
        this.backgroundColor = new Color(r,g,b);
    }
    
    public void setBackground(int r, int g, int b, int alpha){
        this.backgroundColor = new Color(r,g,b,alpha);
    }
    
    public void setXOffset(int offset){
        this.xLeftOffset = offset;
    }
    
    public void drawPave(Graphics2D g2d, int x , int y){
        
        if(this.paveTexts.isEmpty()==true) return;
        
        this.updateDimensions(g2d);
        g2d.setColor(this.backgroundColor);
        g2d.fillRect(
                (int) this.paveDimension.getDimension(0).getMin() - xPadding,
                (int) this.paveDimension.getDimension(1).getMin() - yPadding,
                (int) this.paveDimension.getDimension(0).getLength() + 2*xPadding,
                (int) this.paveDimension.getDimension(1).getLength() + 2*yPadding
        );
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(  
                (int) this.paveDimension.getDimension(0).getMin() - xPadding,
                (int) this.paveDimension.getDimension(1).getMin() - yPadding,
                (int) this.paveDimension.getDimension(0).getLength() + 2*xPadding,
                (int) this.paveDimension.getDimension(1).getLength() + 2*yPadding
                );
        /*g2d.drawRoundRect(
                (int) this.paveDimension.getDimension(0).getMin() - xPadding,
                (int) this.paveDimension.getDimension(1).getMin() - yPadding,
                (int) this.paveDimension.getDimension(0).getLength() + 2*xPadding,
                (int) this.paveDimension.getDimension(1).getLength() + 2*yPadding,
                20,20
        );*/
        int ystart = (int) this.paveDimension.getDimension(1).getMin();
        int xstart = (int) this.paveDimension.getDimension(0).getMin() + this.xLeftOffset;
        //System.out.println("drawing....");
        int counter = 0;
        for(List<LatexText> list : this.paveTexts){

            if(list.size()==2){
                list.get(0).drawString(g2d, xstart, ystart,0,0);
                list.get(1).drawString(g2d, 
                        (int) (xstart+paveDimension.getDimension(0).getLength()), ystart,2,0);
            }
            int size = this.paveFont.getFontSize();
            
            if(this.colors.size()>counter){
               g2d.setColor(this.colors.get(counter));
               g2d.setStroke(new BasicStroke(4));
               g2d.drawLine(xstart-this.xLeftOffset+10,ystart+size/2,xstart-5,ystart+size/2);
               g2d.setColor(Color.BLACK);
            }
            ystart += ySpacing;
            counter++;
        }
    }
    
    public void setPosition(int x, int y){
        this.paveDimension.getDimension(0).setMinMax(x,x + paveDimension.getDimension(0).getLength());
        this.paveDimension.getDimension(1).setMinMax(y,y + paveDimension.getDimension(1).getLength());
    }
    
    public Dimension2D  getBounds(){
        return this.paveDimension;
    }
    
    public void updateDimensions(Graphics2D g2d){
        FontMetrics fmg = g2d.getFontMetrics(new Font(paveFont.getFontName(),
                Font.PLAIN,paveFont.getFontSize()));
        double[] widths = new double[this.N_COLUMNS];
        double   height = 0; 
        for(List<LatexText> list : this.paveTexts){
            for(int i = 0; i < this.N_COLUMNS; i++){
                Rectangle2D rect = fmg.getStringBounds(list.get(i).getText().getIterator(), 0,
                        list.get(i).getText().getIterator().getEndIndex(),g2d);
                if(rect.getWidth()>widths[i]) widths[i] = rect.getWidth();
                height += rect.getHeight()/this.N_COLUMNS;
            }
        }
        
        double width = 0.0;
        for(int i = 0; i < widths.length; i++) width+= widths[i];
        ySpacing = (int) (1.1*height/this.paveTexts.size());
        
        this.paveDimension.getDimension(0).setMinMax(
                 paveDimension.getDimension(0).getMin(), 
                this.xLeftOffset + paveDimension.getDimension(0).getMin() + width + xSpacing
                );
        this.paveDimension.getDimension(1).setMinMax(
                paveDimension.getDimension(1).getMin(), 
                paveDimension.getDimension(1).getMin() + ySpacing*this.paveTexts.size()
        );
        //ySpacing = (int) (1.2*height/this.paveTexts.size());
        /*System.out.println("HEIGHT = " + height + "  spacing = " + ySpacing);
        for(int i = 0; i < widths.length; i++)
            System.out.println("WIDTHS = " + widths[i]);
        */
    }
    
    public void setFont(String name){
        this.paveFont.setFontName(name);
        for(List<LatexText> texts : this.paveTexts){
            for(LatexText text : texts){
                text.setFont(name);
            }
        }
    }
    
    public void setFontSize(int size){
        this.paveFont.setFontSize(size);
        for(List<LatexText> texts : this.paveTexts){
            for(LatexText text : texts){
                text.setFontSize(size);
            }
        }
    }
    
    public List<List<LatexText>> getPaveTexts(){
        return this.paveTexts;
    }
    
    public void  setPaveTexts(List<List<LatexText>> paveTexts){
        this.paveTexts = paveTexts;
    }
    
    public void copy(PaveText pave){
        this.paveTexts.clear();
        for(List<LatexText>  texts : pave.getPaveTexts()){
            
        }
    }

	public void addRow(List<LatexText> element) {
		paveTexts.add(element);
	}
}

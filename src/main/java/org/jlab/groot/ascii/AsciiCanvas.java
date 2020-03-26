/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ascii;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.H1F;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class AsciiCanvas {
    
    private List<StringBuilder> canvasStrings = new ArrayList<StringBuilder>();
    private int    XSIZE = 0;
    private int    YSIZE = 0;
    
    private char[] BINS  = new char[]{'\u2581', '\u2582','\u2583',
        '\u2584','\u2585', '\u2586','\u2587' , '\u2590'};     
    
    private Dimension2D canvasDimension = new Dimension2D();
    
    public AsciiCanvas(){
        
    }
    
    protected void showBars(){
        System.out.print( "BINS : -> ");
        for(int i = 0; i < BINS.length; i++){
            System.out.print(BINS[i]);
        }
        System.out.println();
    }
    protected void init(int xSize, int ySize){
        canvasStrings.clear();
        for(int i = 0; i < ySize ; i++){
            StringBuilder str = new StringBuilder();
            for(int k = 0; k < xSize; k++) str.append(" ");
            canvasStrings.add(str);
            //System.out.println(" adding ->" + str.toString() + "<-");
        }
        XSIZE = xSize; YSIZE = ySize;
    }
    
    protected void initAxis(){
       for(int i = 0; i < YSIZE ; i++) setChar(0,i,'|');
       for(int i = 1; i < XSIZE ; i++) setChar(i,YSIZE-1,'-');
    }
    
    protected void setBinContentNormalized(int bin, double value){
        int  yWidth = YSIZE - 2;
        int howMany = (int) (yWidth*value);
        for(int i = 0; i < howMany; i++){
            this.setChar(bin+1, YSIZE-2-i, BINS[7]);
        }
    }
    
    public void setChar(int xpos, int ypos, char ch){
        canvasStrings.get(ypos).setCharAt(xpos, ch);
    }
    
    public String getCanvasString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < canvasStrings.size(); i++){
            str.append(canvasStrings.get(i).toString()).append("\n");

        }
        return str.toString();
    }
    
    public static void main(String[] arg){
        AsciiCanvas canvas = new AsciiCanvas();
        canvas.showBars();
        canvas.showBars();
        canvas.init(120, 20);
        canvas.initAxis();
        H1F h1 = FunctionFactory.randomGausian(118, 0.0, 6.0, 184000, 3.2, 0.6);
        System.out.println(h1.toString());
        for(int i = 0; i < h1.getxAxis().getNBins(); i++){
            //int bin = (int) (Math.random()*78);
            //double value =  (Math.random());
            int bin = i+1;
            double value = h1.getBinContent(i)/h1.getMax();
            canvas.setBinContentNormalized(bin+1, value);
            //canvas.setBinContentNormalized(11, 0.7);
            //canvas.setBinContentNormalized(12, 0.4);
        }
        System.out.println(canvas.getCanvasString());
    }
}

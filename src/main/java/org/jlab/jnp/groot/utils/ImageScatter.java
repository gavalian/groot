/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jlab.groot.data.GraphErrors;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.graphics.TDataCanvas;

/**
 *
 * @author gavalian
 */
public class ImageScatter {
    public int getRed(int color){
        return (color>>16)&0x000000FF;
    }
    
    public int getGreen(int color){
        return (color>>8)&0x000000FF;       
    }
    
    public int getBlue(int color){
        return (color)&0x000000FF;  
    }
    
    public int brightness(int value){
        int br = getRed(value) + getGreen(value) + getBlue(value);
        return br/3;
    }
    
    public GraphErrors fromImage(String filename){
        BufferedImage img = null;
        GraphErrors graph = new GraphErrors();
        //graph.setTitleX("wire");
        //graph.setTitleX("layer");
        
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
        }
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int value = img.getRGB(x, y);
                if(brightness(value)>150){
                    graph.addPoint(x+1, y+1, 0.0,0.0);
                }
                //System.out.printf("x = %4d, y = %4d , color = %4d, %4d, %4d, brightness = %5d\n",
                //        x,y, getRed(value),getGreen(value),getBlue(value), brightness(value));
            }
            
        }
        
        graph.setMarkerColor(42);
        graph.setMarkerSize(4);
        graph.setMarkerStyle(1);
        graph.setLineColor(2);
        graph.setLineThickness(1);
        return graph;
    }
    
    public void createExamplePlots(){
         String[] noisy = new String[]{ 
            "intro_noisy0.png","intro_noisy1.png",
            "intro_noisy2.png","intro_noisy3.png" };
        String[] correct = new String[]{ 
            "intro_correct0.png","intro_correct1.png",
            "intro_correct2.png","intro_correct3.png"
        };
        
        TDataCanvas c1 = new TDataCanvas(900,400);
        /*c1.divide(new double[][]{
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45}
        });*/
        c1.getDataCanvas().divide(0.03,0.08, 4,1);
        int pad = 2;
        c1.getDataCanvas().left(pad).right(pad).top(pad).bottom(pad);
        
        for(int i = 0; i < 4; i++){
            GraphErrors gr = fromImage("imagestemp/" + noisy[i]);
            gr.setMarkerColor(71);
            gr.setLineColor(21);
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTickLabels(new double[]{6,12,18,24,30,36});            
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});

            
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            if(i!=0) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            
            
        }
        
        for(int i = 0; i < 4; i++){
            GraphErrors gr = fromImage("imagestemp/" + correct[i]);
            c1.cd(i);
            c1.draw(gr, "same");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            if(i!=0) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        }
    }
    public void createLuminocityPlots(){
         String[] noisy = new String[]{ 
            "noisy0_45nA.png",
             "noisy0_50nA.png",
             "noisy0_55nA.png",
             "noisy0_90nA.png",
             "noisy4_100nA.png",
             "noisy1_110nA.png",
             
         };
          String[] correct = new String[]{ 
            "correct0_45nA.png",
             "correct0_50nA.png",
             "correct0_55nA.png",
             "correct0_90nA.png",
             "correct4_100nA.png",
             "correct1_110nA.png",
             
         };
           String[] denoise = new String[]{ 
            "denoised0_45nA.png",
             "denoised0_50nA.png",
             "denoised0_55nA.png",
             "denoised0_90nA.png",
             "denoised4_100nA.png",
             "denoised1_110nA.png",
             
         };
        
        
        TDataCanvas c1 = new TDataCanvas(900,400);
        /*c1.divide(new double[][]{
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45}
        });*/
        c1.getDataCanvas().divide(0.03,0.08, 3, 6);
        int pad = 2;
        c1.getDataCanvas().left(pad).right(pad).top(pad).bottom(pad);
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("imagestemp/"+noisy[k]);
            gr.setMarkerColor(71);
            gr.setLineColor(21);
            int i = k*3;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTickLabels(new double[]{6,12,18,24,30,36});            
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});

            
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            
            
        }
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("imagestemp/"+correct[k]);
            int i = k*3 + 1;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        }
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("imagestemp/"+denoise[k]);
            gr.setMarkerColor(77);
            gr.setLineColor(27);
            int i = k*3+2;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        }
    }
    
    public void createTrainingPlots(){
         String[] noisy = new String[]{ 
            "noisy0.png",
             "noisy1.png",
             "noisy2.png",
             "noisy3.png",
             "noisy4.png",
             "noisy5.png",
             
         };
          String[] correct = new String[]{ 
            "correct0.png",
             "correct1.png",
             "correct2.png",
             "correct3.png",
             "correct4.png",
             "correct5.png",
             
         };
           String[] denoise = new String[]{ 
            "denoised0.png",
             "denoised1.png",
             "denoised2.png",
             "denoised3.png",
             "denoised4.png",
             "denoised5.png",             
         };
        
        
        TDataCanvas c1 = new TDataCanvas(900,400);
        /*c1.divide(new double[][]{
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45},
            {0.45,0.45}
        });*/
        c1.getDataCanvas().divide(0.03,0.08, 3, 6);
        int pad = 2;
        c1.getDataCanvas().left(pad).right(pad).top(pad).bottom(pad);
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("Figures/"+noisy[k]);
            gr.setMarkerColor(71);
            gr.setLineColor(21);
            int i = k*3;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTickLabels(new double[]{6,12,18,24,30,36});            
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});

            
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            
            
        }
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("Figures/"+correct[k]);
            int i = k*3 + 1;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        }
        
        for(int k = 0; k < 6; k++){
            GraphErrors gr = fromImage("Figures/"+denoise[k]);
            gr.setMarkerColor(77);
            gr.setLineColor(27);
            int i = k*3+2;
            c1.cd(i);
            c1.draw(gr, "");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().setAxisLimits(0.0, 113, 0.0, 37);
            //c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().setAxisTicks(new double[]{6,12,18,24,30,36});
            if(k!=5) c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            c1.getDataCanvas().getRegion(i).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
            //c1.getDataCanvas().getRegion(i+4).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        }
    }

    public static void main(String[] args){
        ImageScatter scatter = new ImageScatter();

        scatter.createExamplePlots();
        //scatter.createLuminocityPlots();
        //scatter.createTrainingPlots();;
        
    }
}

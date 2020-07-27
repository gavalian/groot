/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class GRootColorPalette {
    
    
    private List<Color> colorPalette3D = new ArrayList<Color>();
    private List<Color> colorPalette   = new ArrayList<Color>();
    public static GRootColorPalette GROOTPalette = new GRootColorPalette();
    
    double[] red   = {0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.17,0.33,0.50,0.67,0.83,1.00,1.00,
                             1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,
                             1.,1.,1.,1.,1.,1.,1.};
    double[] green = {0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.08,0.15,0.23,0.31,0.38,0.46,0.53,0.59,0.66,
                             0.73,0.80,0.87,0.72,0.58,0.43,0.29,0.14,0.00,0.08,
                             0.17,0.25,0.33,0.42,0.50,0.58,0.67,0.75,0.83,0.92,
                             1.,1.,1.,1.,1.,1.,1.};
    double[] blue  = {0.30,0.33,0.36,0.39,0.42,0.45,0.48,0.52,0.56,0.60,
                             0.64,0.68,0.68,0.70,0.70,0.70,0.70,0.64,0.56,0.48,
                             0.40,0.33,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.,0.,0.,0.,0.,0.,0.,0.,0.,
                             0.,0.17,0.33,0.50,0.67,0.83,1.};
    
    public GRootColorPalette(){
        init("default");
    }
    
    public void init(String type){
        if(type.contains("default")==true){
            colorPalette.clear();
            colorPalette3D.clear();
            for(int loop = 0; loop < red.length; loop++){
                int pred   = (int) (255.0*red[loop]);
                int pgreen = (int) (255.0*green[loop]);
                int pblue  = (int) (255.0*blue[loop]);
                colorPalette3D.add(new Color(pred,pgreen,pblue));
            }
            colorPalette.add(Color.WHITE);
            colorPalette.add(Color.BLACK);            
            colorPalette.add(new Color(246,67,55));
            colorPalette.add(new Color(73,175,87));
            colorPalette.add(new Color(60,82,178));
            colorPalette.add(new Color(255,234,79));
            colorPalette.add(new Color(157,42,173));
            colorPalette.add(new Color(0,188,211));
            colorPalette.add(new Color(138,194,84));
            colorPalette.add(new Color(102,60,180));            
        }
    }
    
    public Color getColor(int color){
        if(color<colorPalette.size()){
            return this.colorPalette.get(color);
        } 
        int alfa = 255 - 25*(color/10);
        int base = color%10;
        Color col = colorPalette.get(base);
        return new Color(col.getRed(),col.getGreen(),col.getBlue(),alfa);
    }
    
    public static GRootColorPalette getInstance(){ return GROOTPalette;}
    
    public Color getColor3D(double min, double max, double value){
        int         nbins = colorPalette3D.size();
        double  fraction = (value-min)/(max-min);

        int         binC = (int) (fraction*nbins);
        //System.out.printf(" fraction = %12.6f, bin = %d, size = %d\n",fraction,binC, nbins);
        if(binC<1) return new Color(200,200,200);//Color.WHITE;
        return this.colorPalette3D.get(binC-1);
        //return Color.WHITE;
    }
}

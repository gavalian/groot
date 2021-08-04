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
    private Color paletteColors[];
    private Color bgColor = new Color(200, 200, 200);
    private double stops[] = {0.0000, 0.1250, 0.2500, 0.3750, 0.5000, 0.6250, 0.7500, 0.8750, 1.0000};
    
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
    
    public void setColorScheme(String scheme){
        if(scheme.compareTo("tab10")==0){
            colorPalette.clear();
            colorPalette.add(Color.WHITE);
            colorPalette.add(Color.BLACK);
            colorPalette.add(new Color( 20,120,178)); // BLUE - 2
            colorPalette.add(new Color(255,127, 30)); // ORANGE - 3
            colorPalette.add(new Color( 39,160, 56));  // GREEN - 4
            colorPalette.add(new Color(216, 39, 40));  // RED - 5
            colorPalette.add(new Color(148,104,186));  // PURPLE - 6
            colorPalette.add(new Color(141, 86, 76));  // BROWN - 7
            colorPalette.add(new Color(227,119,190));  // PINK - 8
            colorPalette.add(new Color(127,127,127));  // GRAY - 9
            colorPalette.add(new Color(186,185, 53));  // OLIVE - 10
            colorPalette.add(new Color(  0,188,203));  // CYAN  - 11
            colorPalette.add(new Color(  168,182,196));  // GRAY - 12
            //colorPalette.add(new Color());
        }
        
        if(scheme.compareTo("gold10")==0){
            colorPalette.clear();
            colorPalette.add(Color.WHITE); // white - 0
            colorPalette.add(Color.BLACK); // black - 1
            colorPalette.add(new Color( 37,75,135)); // NAVY - 2
            colorPalette.add(new Color( 37,118,117)); // TEAL - 3
            colorPalette.add(new Color( 255,195,114)); // GOLD 70 - 4
            colorPalette.add(new Color( 220,115,28)); // DARK GOLD - 5
            colorPalette.add(new Color( 186,164,150)); // Neutral60 - 6
            colorPalette.add(new Color( 116,87,69)); // Dark Neutral - 7
            colorPalette.add(new Color( 220,156,191)); // Purple 50 - 8
            colorPalette.add(new Color( 160,27,104)); // Dark Purple - 9
            colorPalette.add(new Color( 231,158,142)); // Red 60 - 10
            colorPalette.add(new Color( 209,65,36)); // Red - 11
            
            
        }
    }
    
    
    public void setColorPalette(){
        
        /*this.colorPalette3D.clear();
        for(int i = 0; i < 200; i++){
            this.colorPalette3D.add(new Color(0,0,i+55));
        }*/
        //double red[] = {19., 42., 64., 88., 118., 147., 175., 187., 205.};
        //double green[] = {19., 55., 89., 125., 154., 169., 161., 129., 70.};
        //double blue[] = {19., 32., 47., 70., 100., 128., 145., 130., 75.};
        double red[] = {53.0, 15.0, 19.0, 5.0, 45.0, 135.0, 208.0, 253.0, 248.0};
        double green[] = {42.0, 91.0, 128.0, 163.0, 183.0, 191.0, 186.0, 200.0, 250.0};
        double blue[] = {134.0, 221.0, 213.0, 201.0, 163.0, 118.0, 89.0, 50.0, 13.0};
        CreateGradientColorTable(red, green, blue);
        
    }
    
    
    private void CreateGradientColorTable(double[] red, double[] green, double[] blue) {
        if (red.length == 47) {
            paletteColors = new Color[red.length];
            for (int icol = 0; icol < red.length; icol++) {
                paletteColors[icol] = new Color((float) red[icol], (float) green[icol], (float) blue[icol]);
            }
        } else {
            paletteColors = new Color[255];
            int nstops = stops.length;
            int icolor = 0;
            for (int istop = 1; istop < nstops; istop++) {
                int ncolors = (int) (Math.floor(255.0 * stops[istop]) - Math.floor(255.0 * stops[istop - 1]));
                for (int ic = 0; ic < ncolors; ic++) {
                    double rr = red[istop - 1] + ic * (red[istop] - red[istop - 1]) / ncolors;
                    double gg = green[istop - 1] + ic * (green[istop] - green[istop - 1]) / ncolors;
                    double bb = blue[istop - 1] + ic * (blue[istop] - blue[istop - 1]) / ncolors;
                    paletteColors[icolor++] = new Color((int) rr, (int) gg, (int) bb);
                }
            }
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
       /* int         nbins = colorPalette3D.size();
        double  fraction = (value-min)/(max-min);
        int         binC = (int) (fraction*nbins);
        //System.out.printf(" fraction = %12.6f, bin = %d, size = %d\n",fraction,binC, nbins);
        if(binC<1) return Color.WHITE;//new Color(200,200,200);//Color.WHITE;
        return this.colorPalette3D.get(binC-1);*/
        //return Color.WHITE;
        return getColor3D(value,min,max,false);
    }
    
    public Color getColor3D(double value, double min, double max, boolean islog) {
        double fraction = 0.0;
        if (max < min) {
            throw new UnsupportedOperationException("axis range is wrong: Maximum < Minimum");
        }
        if (value == 0 && min >= 0) {
            return bgColor;
        }
        if (islog == true) {
            if (value <= 0) {
                throw new UnsupportedOperationException("Logarithmic scale can't be enabled for negative values");
            }
            if (min == 0) {
                min = 0.1;
            }
            value = Math.log10(value);
            min = Math.log10(min);
            max = Math.log10(max);
        }
        fraction = (value - min) / (max - min);

        if (fraction > 1) {
            fraction = 1.0;
        } else if (fraction < 0) {
            fraction = 0;
        }
        return paletteColors[(int) (fraction * (paletteColors.length - 1))];
    }
}

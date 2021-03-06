/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.settings;

import java.awt.BasicStroke;

/**
 *
 * @author gavalian
 */
public class GRootTheme {
    public static GRootTheme GROOTTheme = new GRootTheme();
    
    public static GRootTheme getInstance(){ return GROOTTheme;}
    
    public static float[]  dashPattern1 = new float[]{10.0f,5.0f};
    public static float[]  dashPattern2 = new float[]{10.0f,5.0f,2.0f,5.0f};
    public static float[]  dashPattern3 = new float[]{2.0f,5.0f,2.0f,5.0f};
    public static float[]  dashPattern4 = new float[]{2.0f,8.0f,2.0f,4.0f};
    public static float[]  dashPattern5 = new float[]{2.0f,6.0f,2.0f,2.0f};
    
        
    public GRootTheme(){
        
    }
    
    public BasicStroke getLineStroke(int style, int width){
        
         switch (style){                
                case 1 : return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f);
                case 2: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern2, 0.0f);
                case 3: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern3, 0.0f);
                case 4: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern4, 0.0f);
                case 5: return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern5, 0.0f);
                default : return new BasicStroke(width);

            }
    }
}

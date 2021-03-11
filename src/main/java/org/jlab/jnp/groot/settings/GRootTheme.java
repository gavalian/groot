/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.settings;

import java.awt.BasicStroke;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;

/**
 *
 * @author gavalian
 */
public class GRootTheme {
    
    public enum ThemeFontType {
        AXIS_TICKS_FONT, AXIS_TITLE_FONT, REGION_TITLE_FONT
    };
    
    public static GRootTheme GROOTTheme = new GRootTheme();
    public static GRootTheme getInstance(){ return GROOTTheme;}
    
    public static List<float[]> dashPatterns = Arrays.asList(
            new float[]{10.0f,0.0f},
            new float[]{10.0f,5.0f},
            new float[]{10.0f,5.0f,2.0f,5.0f},
            new float[]{2.0f,5.0f,2.0f,5.0f},
            new float[]{2.0f,8.0f,2.0f,4.0f},
            new float[]{2.0f,6.0f,2.0f,2.0f},
            new float[]{1.0f,10.0f},
            new float[]{1.0f,1.0f},
            new float[]{5.0f,10.0f},
            new float[]{5.0f,5.0f},
            new float[]{5.0f,1.0f},            
            new float[]{3.0f,10.0f,1.0f,10.0f},
            new float[]{3.0f,5.0f,1.0f,5.0f},
            new float[]{3.0f,10.0f,1.0f,10.0f,1.0f,10.0f},
            new float[]{3.0f,1.0f,1.0f,1.0f,1.0f,1.0f}                                    
    );
    
    private  GRootColorPalette  themePalette = new GRootColorPalette();        
        
    private AttributeCollection dataSetAttributes = null;
    private AttributeCollection    axisAttributes = null;
    private AttributeCollection  regionAttributes = null;
    
    private Map<ThemeFontType, Font> themeFonts = new HashMap<>();
    
    public GRootTheme(){
        initAttributes();
        initFonts();
        themePalette.setColorScheme("tab10");
    }
    
    public GRootColorPalette getPalette(){ return themePalette;}
    
    protected final void initAttributes(){
        
        axisAttributes = new AttributeCollection(
                new AttributeType[]{
                    AttributeType.AXISLINECOLOR,
                    AttributeType.AXISLINEWIDTH,AttributeType.AXISLINESTYLE,
                    AttributeType.AXISTICKSIZE,AttributeType.AXISLABELOFFSET,
                    AttributeType.AXISTITLEOFFSET,AttributeType.AXISTITLEOFFSETVERTICAL,
                    AttributeType.AXISDRAWBOX,AttributeType.AXISDRAWTICKS,
                    AttributeType.AXISDRAWLINE,AttributeType.AXISDRAWGRID, 
                    AttributeType.AXIS_DRAW_LABELS,AttributeType.AXIS_DRAW_TITLE,
                    AttributeType.AXIS_DRAW_TICKS
                },                    
                new String[]{"0",
                    "1","1",
                    "5","5","10","10",
                    "true","true","true","fasle","true","true","true"});
        
    }
    
    
    public final void initFonts(){
        themeFonts.put(ThemeFontType.AXIS_TICKS_FONT, new Font("Avenir",Font.PLAIN,14));
        themeFonts.put(ThemeFontType.AXIS_TITLE_FONT, new Font("Avenir",Font.PLAIN,18));
        themeFonts.put(ThemeFontType.REGION_TITLE_FONT, new Font("Avenir",Font.PLAIN,18));
    }
    
    public final void initFont(String theme){
        if(theme.compareTo("PAW")==0){
            themeFonts.put(ThemeFontType.AXIS_TICKS_FONT, new Font("Times",Font.PLAIN,14));
            themeFonts.put(ThemeFontType.AXIS_TITLE_FONT, new Font("Times",Font.PLAIN,18));
            themeFonts.put(ThemeFontType.REGION_TITLE_FONT, new Font("Times",Font.PLAIN,18));
        }
    }
    public Font getFont(ThemeFontType type){
        return themeFonts.get(type);
    }
    
    public void setFont(ThemeFontType type, Font font){
        themeFonts.put(type, font);
    }
    
    public BasicStroke getLineStroke(int style, int width){
        
        if(style<2) return new BasicStroke(width);
        int strokeStyle = style;
        if(style>=dashPatterns.size())
            strokeStyle= style%dashPatterns.size();
            return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPatterns.get(strokeStyle), 0.0f);

            /*
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

            }*/
    }
}

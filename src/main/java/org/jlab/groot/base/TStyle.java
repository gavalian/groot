/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import java.awt.Color;

/**
 *
 * @author gavalian
 */
public class TStyle {
    
    public static Attributes  grootStyle = TStyle.createAttributes();

    
    public static Attributes createAttributes(){
        Attributes attr = new Attributes();
        attr.add(AttributeType.LINE_COLOR, 0);
        attr.add(AttributeType.LINE_WIDTH, 1);
        attr.add(AttributeType.LINE_STYLE, 1);        
        attr.add(AttributeType.FILL_COLOR, 4);
        attr.add(AttributeType.AXIS_LINE_WIDTH, 1);
        //attr.add(AttributeType., 1);
        
        return attr;
    }
    
    
    public static Attributes getStyle(){
        return grootStyle;
    }
    
    public static Color getColor(int index){
        return ColorPalette.getColor(index);
    }
    
    
}

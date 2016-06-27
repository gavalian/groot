/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

/**
 *
 * @author gavalian
 */
public enum AttributeType {
      
    UNDEFINED        ( 0, "undefined"),
    LINE_COLOR       ( 1, "LINE_COLOR"),
    LINE_STYLE       ( 2, "LINE_STYLE"),
    LINE_WIDTH       ( 3, "LINE_WIDTH"),
    FILL_COLOR       ( 4, "FILL_COLOR"),
    FILL_STYLE       ( 5, "FILL_STYLE"),
    MARKER_COLOR     ( 6, "MARKER_COLOR"),
    MARKER_STYLE     ( 7, "MARKER_STYLE"),
    MARKER_SIZE      ( 8, "MARKER_SIZE"),
    MARING_TOP       ( 10, "MARGIN_TOP"),
    MARING_BOTTOM    ( 11, "MARGIN_TOP"),
    MARING_LEFT      ( 12, "MARGIN_TOP"),
    MARING_RIGHT     ( 13, "MARGIN_TOP"),     
    AXIS_LINE_WIDTH    ( 14, "AXIS_LINE_WIDTH"),
    AXIS_LINE_COLOR    ( 15, "AXIS_LINE_COLOR"),
    AXIS_LINE_STYLE    ( 16, "AXIS_LINE_STYLE"),
    AXIS_TICKMARK_SIZE (17,"AXIS_TICKMARK_SIZE"),
    AXIS_BACKGROUND_COLOR (18,"AXIS_BACKGROUND_COLOR"),
    STRING_TITLE       (19,"STRING_TITLE"),
    STRING_TITLE_X   (20,"STRING_TITLE_X"),
    STRING_TITLE_Y   (21,"STRING_TITLE_Y"),
    AXIS_GRID_X      (22,"AXIS_GRID_X"),
    AXIS_GRID_Y      (23,"AXIS_GRID_Y"),
    AXIS_TICKS_SIZE  (24,"AXIS_TICKS_SIZE"),
    AXIS_TICKS_STYLE (25,"AXIS_TICKS_STYLE"),
    AXIS_LABEL_OFFSET (26,"AXIS_LABEL_OFFSET"),
    AXIS_TITLE_OFFSET (27,"AXIS_TITLE_OFFSET"),
    AXIS_FRAME_STYLE (28,"AXIS_FRAME_STYLE"),
    AXIS_DRAW_X      (29,"AXIS_DRAW_X"),
    AXIS_DRAW_Y      (30,"AXIS_DRAW_Y"),
    AXIS_DRAW_Z      (31,"AXIS_DRAW_Z")
    
    
    ;
    
    private int attributeId ;
    private String attributeName;
    //private String attributeDescription;
    
    AttributeType(){
        attributeId   = 0;
        attributeName = "undefined"; 
    }
    
    AttributeType(int id, String name){
        attributeId   = id;
        attributeName = name;
    }
    
    public int    getType(){ return attributeId; }
    public String getName() { return attributeName; }
    
    public static AttributeType getType(String name) {
        name = name.trim();
        for(AttributeType id: AttributeType.values())
            if (id.getName().equalsIgnoreCase(name)) 
                return id;
        return UNDEFINED;
    }
    
    public static AttributeType getType(Integer detId) {
        
        for(AttributeType id: AttributeType.values())
            if (id.getType() == detId) 
                return id;
        return UNDEFINED;
    }
}

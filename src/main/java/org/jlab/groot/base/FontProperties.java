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
public class FontProperties {
    private String fontName = "Avenir";
    private int    fontSize = 12;
    
    public FontProperties(){
        
    }
    
    public FontProperties setFontName(String name){
        this.fontName = name;
        return this;
    }
    
    public FontProperties setFontSize(int size){
        this.fontSize = size;
        return this;
    }
    
    public int getFontSize(){
        return this.fontSize;
    }
    
    public String getFontName(){
        return this.fontName;
    }
}

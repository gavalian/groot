/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import java.awt.*;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gavalian
 */
public class FontProperties {
    
    private static List<String>   systemAvailableFonts = null;//FontProperties.initSystemFonts();
    
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
    
    public static List<String>  getSystemFonts(){
        if(systemAvailableFonts==null){
            systemAvailableFonts = FontProperties.initSystemFonts();
        }
        return systemAvailableFonts;
    }
    
    public static String[]  getSystemFontsArray(){
        if(systemAvailableFonts==null){
            systemAvailableFonts = FontProperties.initSystemFonts();
        }
        String[] fonts = new String[systemAvailableFonts.size()];
        int icounter = 0;
        for(String fn : systemAvailableFonts){
            fonts[icounter] = fn;
            icounter++;
        }
        return fonts;
    }
    
    public static List<String>  initSystemFonts(){
        Set<String>  fontSet = new HashSet<String>();
        List<String> fontList = new ArrayList<String>();

        fontSet.add("Avenir");
        fontSet.add("Arial");
        fontSet.add("American Typewriter");
        fontSet.add("Bradley Hand");
        fontSet.add("Chalkduster");
        fontSet.add("Charter");
        fontSet.add("Courier");
        fontSet.add("HanziPen TC");
        fontSet.add("Hannotate TC");
        fontSet.add("Helvetica");
        fontSet.add("Helvetica Neue");
        fontSet.add("Kaiti TC");
        fontSet.add("Kefa");
        fontSet.add("Krungthep");
        fontSet.add("Menlo");
        fontSet.add("Monaco");
        fontSet.add("Noteworthy");
        fontSet.add("Monospaced");
        fontSet.add("PT Mono");
        fontSet.add("PT Sans Narrow");
        fontSet.add("SansSerif");
        fontSet.add("Snell Roundhand");
        fontSet.add("Times");
        fontSet.add("Times New Roman");
        fontSet.add("Veranda");
        // Check if the system contains the fonts that we want the user
        // to use. If so, add them to available list
        String[] fonts = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        if(fonts!=null){
            for(String fontname : fonts){
                if(fontSet.contains(fontname)==true){
                    fontList.add(fontname);
                }
            }
        }

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            File directory = new File(FontProperties.class.getClassLoader().getResource("fonts").getFile());

            try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory.toPath(), "*.{ttf,otf}")) {
                for (Path entry: dirStream) {
                    Font tmp = Font.createFont(Font.TRUETYPE_FONT, entry.toFile());
                    fontList.add(tmp.getFamily());
                    ge.registerFont(tmp);
                }
            }

        } catch (Exception e) {
            System.err.println("Whoops!");
            System.err.println(e);
        }

        System.out.println("[SystemFonts] ---> set size = " + fontSet.size()
        + ", available " + fontList.size());
        return fontList;
    }
    
    public static String[] getFontSizeArray(){
        String[] array = new String[]{"6","8","10","12","14","18","24","28","32","48"};
        return array;
    }
    
    public int getFontSize(){
        return this.fontSize;
    }
    
    public String getFontName(){
        return this.fontName;
    }
}

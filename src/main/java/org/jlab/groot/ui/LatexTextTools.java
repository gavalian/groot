/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class LatexTextTools {
    
    private static Map<String,String>  greekTranslation = getTable();
            
    public static Map<String,String> getTable(){
        Map<String,String> table = new LinkedHashMap<String,String>();
        table.put("#alpha"  , "\u03B1");
        table.put("#Alpha"  , "\u0391");
        table.put("#beta"   , "\u03B2");
        table.put("#Beta"   , "\u0392");
        table.put("#gamma"  , "\u03B3");
        table.put("#Gamma"  , "\u0393");
        table.put("#delta"  , "\u03B4");
        table.put("#Delta"  , "\u0394");
        table.put("#eps"    , "\u03B5");
        table.put("#Eps"    , "\u0395");
        table.put("#zeta"   , "\u03B6");
        table.put("#Zeta"   , "\u0396");
        table.put("#eta"    , "\u03B7");
        table.put("#Eta"    , "\u0397");
        table.put("#theta"  , "\u03B8");
        table.put("#Theta"  , "\u0398");
        table.put("#iota"   , "\u03B9");
        table.put("#Iota"   , "\u0399");
        table.put("#kappa"  , "\u03BA");
        table.put("#Kappa"  , "\u039A");
        table.put("#lambda" , "\u03BB");
        table.put("#Lambda" , "\u039B");
        table.put("#mu"     , "\u03BC");
        table.put("#Mu"     , "\u039C");
        table.put("#nu"     , "\u03BD");
        table.put("#Nu"     , "\u039D");
        table.put("#xi"     , "\u03BE");
        table.put("#Xi"     , "\u039E");
        table.put("#omicron", "\u03BF");
        table.put("#Omicron", "\u039F");
        table.put("#pi"     , "\u03C0");
        table.put("#Pi"     , "\u03A0");
        table.put("#rho"    , "\u03C1");
        table.put("#Rho"    , "\u03A1");
        table.put("#sigma"  , "\u03C3");
        table.put("#Sigma"  , "\u03A3");
        table.put("#tau"    , "\u03C4");
        table.put("#Tau"    , "\u03A4");
        table.put("#phi"    , "\u03C6");
        table.put("#Phi"    , "\u03A6");        
        table.put("#chi"    , "\u03C7");
        table.put("#Chi"    , "\u03A7");        
        table.put("#psi"    , "\u03C8");
        table.put("#Psi"    , "\u03A8");        
        table.put("#omega"  , "\u03C9");
        table.put("#Omega"  , "\u03A9");
        
        
        table.put("#degree"  , "\u00B0");
        table.put("#rarrow"  , "\u2192");
        table.put("#larrow"  , "\u2190");
        table.put("#uarrow"  , "\u2191");
        table.put("#darrow"  , "\u2193");
        table.put("#overline", "\u0305");
        table.put("#bar"     , "\u0304");
        table.put("#pm"     , "\u00B1");
        return table;
    }
    
    public static String convertUnicode(String original){
        
        if(original.contains("#")==false&&original.contains("^")==false) return original;
        String newString = original;
        
        for(Map.Entry<String,String> entry : LatexTextTools.greekTranslation.entrySet()){
            newString = newString.replaceAll(entry.getKey(), entry.getValue());
        }
        return newString;
    }
    
    
    public static AttributedString convertSubAndSuperscript(String line){
        ArrayList<Integer> supindex = new ArrayList<Integer>();
        ArrayList<Integer> subindex = new ArrayList<Integer>();

        int index = line.indexOf("^");
        String newString = line;
        while(index>=0){
            //System.out.println("adding index = " + index);
            supindex.add(index);
            //newString = newString.replaceFirst("^", "");
            newString = newString.substring(0, index) + newString.substring(index+1);
            index = newString.indexOf("^");
            //System.out.println(newString);
        }
        
        index = newString.indexOf("_");
        while(index>=0){
            //System.out.println("adding index = " + index);
            subindex.add(index);
            //newString = newString.replaceFirst("^", "");
            newString = newString.substring(0, index) + newString.substring(index+1);
            index = newString.indexOf("_");
            //System.out.println(newString);
        }
        
        AttributedString  string = new AttributedString(newString);
        for(Integer indx : subindex){
            if(indx>0){
//                System.out.println(" Subscript = " + indx);
                string.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, indx,indx+1);
            }
        } 
        
        for(Integer indx : supindex){
            if(indx>0){
//                System.out.println(" Superscript = " + indx);
                string.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, indx,indx+1);
            }
        }  
        return string;

    }
    
    public static void main(String[] args){
        String text = "Value #theta (#alpha + #beta) = #gamma^2 = 5.0  #pi^3 = 3.14^5";
        String latex = LatexTextTools.convertUnicode(text);
        System.out.println(latex);
        
        AttributedString str = LatexTextTools.convertSubAndSuperscript(latex);
    }
}

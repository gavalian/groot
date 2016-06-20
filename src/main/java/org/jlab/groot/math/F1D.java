/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.math;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author gavalian
 */
public class F1D {
    
    public F1D(){
        
    }
    
    public void parse(String str){
        
        System.out.println("---> starting parsing....");
        int pos_o = str.indexOf("[");
        Set<String>  params = new LinkedHashSet<String>();
        
        System.out.println(" position = " + pos_o);
        while(pos_o>=0){
            int pos_c = str.indexOf("]", pos_o);
            System.out.println("brackets from " + pos_o 
                    + " to " + pos_c);
            String param = str.substring(pos_o+1, pos_c);
            params.add(param);
            pos_o = str.indexOf("[", pos_c);            
        }
        System.out.println(params);
        String funcString = str;
        //funcString.replaceAll("x", "p");
        String newString = funcString.replace("[0]", "p0");
        System.out.println(newString);
    }
    
    public static void main(String[] args){
        System.out.println("---> starting program");
        F1D func = new F1D();
        func.parse("[0]+[1]*x+[2]*x*x");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import org.jlab.groot.data.GraphErrors;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class DataStudiesNew {
    public static void draw1(){
       GraphErrors  graph = GraphErrors.readFile("denoise_nuevo.data", 0, 6, new int[]{0,4});
       GraphErrors  graph2 = GraphErrors.readFile("denoise_nuevo.data", 6, 6, new int[]{0,4});
       GraphErrors  graph3 = GraphErrors.readFile("denoise_nuevo.data", 6, 6, new int[]{0,6});
       TDataCanvas c1 = new TDataCanvas(450,350);
       //graph.addPoint(5, 2328, 0, 0);
       graph.addPoint(5, 2842, 0, 0);
       graph.statErrors();
       graph2.statErrors();
       
       GraphErrors gr1 = graph.divide(2900);
       GraphErrors gr2 = graph2.divide(2900);
       GraphErrors gr3 = graph3.divide(2900);
       
       gr1.setMarkerColor(2);
       gr1.setMarkerSize(8);
       gr1.setLineThickness(1);
       
       gr2.setMarkerColor(3);
       gr2.setMarkerSize(8);
       gr2.setLineThickness(1);
       
       c1.draw(gr1).draw(gr2, "same").draw(gr3, "same").setAxisLimits(0, 120, 0.4, 1.22);
    }
    
    public static void draw2(){
       GraphErrors  graph = GraphErrors.readFile("denoise_nuevo.data", 0, 6, new int[]{0,5});
       GraphErrors  graph2 = GraphErrors.readFile("denoise_nuevo.data", 6, 6, new int[]{0,5});
       TDataCanvas c1 = new TDataCanvas(450,350);
       //graph.addPoint(5, 2328, 0, 0);
       graph.addPoint(5, 2328, 0, 0);
       graph.statErrors();
       graph2.statErrors();
       
       GraphErrors gr1 = graph.divide(2300);
       GraphErrors gr2 = graph2.divide(2300);
       gr1.setMarkerColor(2);
       gr1.setMarkerSize(8);
       gr1.setLineThickness(1);
       
       gr2.setMarkerColor(3);
       gr2.setMarkerSize(8);
       gr2.setLineThickness(1);
       c1.draw(gr1).draw(gr2, "same").setAxisLimits(0, 120, 0.0, 1.2);
    }
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorScheme("tab10");
        DataStudiesNew.draw1();
        DataStudiesNew.draw2();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import org.jlab.groot.data.GraphErrors;
import org.jlab.jnp.groot.graphics.Legend;
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
    
     public static void draw3(){
       GraphErrors  graph1 = GraphErrors.readFile("event_by_event_efficiency.data", 0, 6, new int[]{0,3});
       GraphErrors  graph2 = GraphErrors.readFile("event_by_event_efficiency.data", 6, 6, new int[]{0,3});
       TDataCanvas c1 = new TDataCanvas(450,350);
       //graph.addPoint(5, 2328, 0, 0);
       
       graph1.statErrors();
       graph2.statErrors();
       
       //GraphErrors gr1 = graph1.divide(16056);
       //GraphErrors gr2 = graph2.divide(16056);
       GraphErrors gr1 = graph1.divide(15000);
       GraphErrors gr2 = graph2.divide(15000);
       gr1.setTitleX("Beam Current (nA)");
       gr1.setTitleY("Fraction of Reconstructed Tracks");
       gr1.setMarkerColor(2);
       gr1.setMarkerSize(12);
       gr1.setLineThickness(1);
       
       gr2.setMarkerColor(3);
       gr2.setMarkerSize(12);
       gr2.setLineThickness(1);
       c1.draw(gr1).draw(gr2, "same").setAxisLimits(40, 120, 0.4, 1.05);
    }
    
     public static void draw4(){
       GraphErrors  graph1 = GraphErrors.readFile("event_by_event_efficiency.data",  0, 6, new int[]{0,3});
       GraphErrors  graph2 = GraphErrors.readFile("event_by_event_efficiency.data",  6, 6, new int[]{0,3});
       GraphErrors  graph3 = GraphErrors.readFile("event_by_event_efficiency.data", 12, 6, new int[]{0,3});
       GraphErrors  graph4 = GraphErrors.readFile("event_by_event_efficiency.data", 18, 6, new int[]{0,3});
       TDataCanvas c1 = new TDataCanvas(450,350);
       //graph.addPoint(5, 2328, 0, 0);
       
       graph1.statErrors();
       graph2.statErrors();
       
       //GraphErrors gr1 = graph1.divide(16056);
       //GraphErrors gr2 = graph2.divide(16056);
       GraphErrors gr1 = graph1.divide(16056);
       GraphErrors gr2 = graph2.divide(16056);
       GraphErrors gr3 = graph3.divide(16152);
       GraphErrors gr4 = graph4.divide(16152);
       
       gr1.setTitleX("Beam Current (nA)");
       gr1.setTitleY("Fraction of Reconstructed Tracks");
       gr1.setMarkerColor(3);
       gr1.setMarkerSize(12);
       gr1.setLineThickness(1);
       
       gr2.setMarkerColor(7);
       gr2.setMarkerSize(12);
       gr2.setMarkerStyle(5);
       gr2.setLineThickness(1);
       
       gr3.setMarkerColor(4);
       gr3.setMarkerSize(12);
       gr3.setMarkerStyle(3);
       gr3.setLineThickness(1);
       
       
       Legend leg = new Legend(60,170);
       leg.add(gr1, "Conventional (raw)");
       leg.add(gr2, "Denoised");
       leg.add(gr3, "Denoised (AI assisted)");
       
       c1.draw(gr1,"PL")
               .draw(gr2, "samePL")
               .draw(gr3, "samePL")
               //.draw(gr4, "same")
               .setAxisLimits(35, 120, 0.4, 1.05);
       leg.right(10);
       leg.bottom(8);
       c1.addLegend(leg);
    }
     
     public static void draw5(){
       GraphErrors  graph1 = GraphErrors.readFile("event_by_event_efficiency.data",  0, 6, new int[]{0,3});
       GraphErrors  graph2 = GraphErrors.readFile("event_by_event_efficiency.data",  6, 6, new int[]{0,3});
       GraphErrors  graph3 = GraphErrors.readFile("event_by_event_efficiency.data", 12, 6, new int[]{0,3});
       GraphErrors  graph4 = GraphErrors.readFile("event_by_event_efficiency.data", 18, 6, new int[]{0,3});
       TDataCanvas c1 = new TDataCanvas(450,350);
       //graph.addPoint(5, 2328, 0, 0);
       
       graph1.statErrors();
       graph2.statErrors();
       
       //GraphErrors gr1 = graph1.divide(16056);
       //GraphErrors gr2 = graph2.divide(16056);
       GraphErrors gr1 = graph1.divide(16056);
       GraphErrors gr2 = graph2.divide(16056);
       GraphErrors gr3 = graph3.divide(16152);
       GraphErrors gr4 = graph4.divide(16152);
       
       gr1.setTitleX("Beam Current (nA)");
       gr1.setTitleY("Fraction of Reconstructed Tracks");
       gr1.setMarkerColor(2);
       gr1.setMarkerSize(12);
       gr1.setLineThickness(1);
       
       gr2.setMarkerColor(3);
       gr2.setMarkerSize(12);
       gr2.setMarkerStyle(2);
       gr2.setLineThickness(1);
       
       gr3.setMarkerColor(4);
       gr3.setMarkerSize(12);
       gr3.setMarkerStyle(3);
       gr3.setLineThickness(1);
       
       GraphErrors result = new GraphErrors("gr",new double[]{40,52},new double[]{1.119,1.135});
       
       
       GraphErrors gr5 = graph3.divide(graph1);
       gr5.setMarkerColor(2);
       gr5.setMarkerSize(12);
       gr5.setLineThickness(1);
       gr5.setTitleX("Beam Current (nA)");
       gr5.setTitleY("Track Multiplicity Ratio");
       gr5.show();
       result.setMarkerColor(3);
       result.setMarkerStyle(2);
       result.setMarkerSize(12);
       result.setLineThickness(1);
       c1.draw(gr5,"PL").draw(result,"PEsame")
               //.draw(gr2, "same")               
               //.draw(gr4, "same")
               .setAxisLimits(35, 120, 0.9, 2.05);
       Legend leg = new Legend(60,30);
       leg.right(10);
       leg.bottom(8);
       leg.add(gr5, "Background Merged");
       //leg.add(gr2, "Denoised");
       leg.add(result, "Experimental Data");
       c1.addLegend(leg);
    }
     
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorScheme("tab10");
        //DataStudiesNew.draw1();
        //DataStudiesNew.draw2();
        //DataStudiesNew.draw3();
        DataStudiesNew.draw4();
        DataStudiesNew.draw5();
        
    }
}

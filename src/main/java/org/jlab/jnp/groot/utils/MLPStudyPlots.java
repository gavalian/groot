/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class MLPStudyPlots {
    
    
    public static H1F convert(GraphErrors gr, double min, double max){
        H1F h = new H1F("h",gr.getDataSize(0),min,max);
        for(int i = 0; i < h.getxAxis().getNBins(); i++){
            h.setBinContent(i, gr.getDataY(i));
        }
        return h;
    }
    
    public static void dist_p(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,1});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,3});
        TDataCanvas c1 = new TDataCanvas(450,350);
        H1F h = MLPStudyPlots.convert(pos_p, 0.0, 10.0);
        h.setFillColor(42);
        h.setLineWidth(1);
        h.setTitleX("Particle Momentum [GeV]");
        c1.draw(h);
        System.out.println(h.toString());
        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
    }
    
    public static void dist_p_not(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,10});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,9});
        TDataCanvas c1 = new TDataCanvas(450,350);
        H1F h = MLPStudyPlots.convert(pos_p, 0.0, 10.0);
        h.setFillColor(45);
        h.setLineWidth(1);
        h.setTitleX("Particle Momentum [GeV]");
        c1.draw(h);
        System.out.println(h.toString());
        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
    }
    
    public static void dist_th(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,2});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,4});
        TDataCanvas c1 = new TDataCanvas(450,350);
        H1F h = MLPStudyPlots.convert(pos_p, 4.0, 45.0);
        h.setFillColor(43);
        h.setLineWidth(1);
        h.setTitleX("Particle Polar Angle [deg]");
        c1.draw(h);
        System.out.println(h.toString());
        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
    }
    
    public static void dist_th_not(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,10});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,9});
        TDataCanvas c1 = new TDataCanvas(450,350);
        H1F h = MLPStudyPlots.convert(pos_p, 4.0, 45.0);
        h.setFillColor(45);
        h.setLineWidth(1);
        h.setTitleX("Particle Polar Angle [deg]");
        c1.draw(h);
        System.out.println(h.toString());
        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
    }
    
    
    public static void eff_p(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,1});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_p.data", 0, 20, new int[]{0,3});
        TDataCanvas c1 = new TDataCanvas(450,350);

        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
        //pos_p.statErrors();
        //pos_t.statErrors();

        GraphErrors pos_eff = pos_p.divide(pos_t);
        pos_eff.setMarkerStyle(1);
        pos_eff.setMarkerSize(12);
        pos_eff.setMarkerColor(2);
        //pos_eff.setLineColor(2);
        pos_eff.setLineThickness(1);
        
        pos_eff.setTitleX("Particle Momentum [GeV]");
        pos_eff.setTitleY("Prediction Efficiency");        
        c1.draw(pos_eff).setAxisLimits(0., 9.5, 0.8, 1.11);
    }
    
    public static void eff_th(){
        
        GraphErrors  pos_t = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,1});
        GraphErrors  pos_p = GraphErrors.readFile("mlp_efficiency_th.data", 0, 20, new int[]{0,3});
        TDataCanvas c1 = new TDataCanvas(450,350);
        c1.getDataCanvas().bottom(45);
        c1.getDataCanvas().left(55);
        //pos_p.statErrors();
        //pos_t.statErrors();

        GraphErrors pos_eff = pos_p.divide(pos_t);
        pos_eff.setMarkerStyle(3);
        pos_eff.setMarkerSize(12);
        pos_eff.setMarkerColor(3);
        //pos_eff.setLineColor(3);
        pos_eff.setLineThickness(1);

        
        pos_eff.setTitleX("Particle Polar Angle [deg]");
        pos_eff.setTitleY("Prediction Efficiency");        
        c1.draw(pos_eff).setAxisLimits(3., 45., 0.8, 1.11);
    }
    
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorScheme("tab10");
        MLPStudyPlots.eff_p();
        MLPStudyPlots.eff_th();
        MLPStudyPlots.dist_p();
        MLPStudyPlots.dist_th();
        
        MLPStudyPlots.dist_p_not();
        MLPStudyPlots.dist_th_not();
    }
}

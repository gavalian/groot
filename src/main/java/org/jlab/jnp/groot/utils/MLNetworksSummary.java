/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.util.List;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.io.CSVReader;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.graphics.AxisNode2D;
import org.jlab.jnp.groot.graphics.Legend;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class MLNetworksSummary {
    
    public static void noise(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        CSVReader reader = new CSVReader();
        String file = "noise_eff_results/t-0.50/model_comparison_results_with_threshold.csv";
        List<double[]> columns = reader.readColumn(file, new int[]{19,20,3}, 1);
        GraphErrors graph = new GraphErrors();
        GraphErrors eff = new GraphErrors();
        
        for(int i = 0; i < columns.size(); i++){
            double  y = columns.get(i)[0];
            double ye = columns.get(i)[1];
            double  r = columns.get(i)[2];
            System.out.printf("x = %8.4f , %8.4f %8.4f\n",(double) (i+1),y,ye);
            graph.addPoint(i+1,y,0.0,0.0);
            eff.addPoint(i+1,r/100.0,0.0,0.0);
            
        }
        graph.setTitleX("Model");
        graph.setTitleY("Efficiency");
        
        graph.setLineColor(1);
        graph.setLineThickness(1);
        graph.setMarkerColor(3);
        graph.setMarkerSize(12);
        
        eff.setLineColor(1);
        eff.setLineThickness(1);
        eff.setMarkerColor(2);
        eff.setMarkerSize(12);
        eff.setMarkerStyle(2);
        
        c1.draw(graph);
        c1.draw(eff,"same");
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISLABELOFFSET, "20");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, "25");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0, 10, 0.0, 1.05);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisTickLabels(
                new String[]{
                    "0","0a","0b","0c","0d","0e","0f","1","2"});
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        Legend legend = new Legend(100,200);
        legend.add(eff, "Track hits fraction");
        legend.add(graph,  "Noise fraction");
        c1.addLegend(legend);
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis()
        c1.repaint();
    }
    
    public static void noisePower(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        CSVReader reader = new CSVReader();
        String file = "noise_eff_results/t-0.50/model_comparison_results_with_threshold.csv";
        List<double[]> columns = reader.readColumn(file, new int[]{21}, 1);
        GraphErrors graph = new GraphErrors();
        GraphErrors eff = new GraphErrors();
        
        for(int i = 0; i < columns.size(); i++){
            double  y = columns.get(i)[0];
            graph.addPoint(i+1,y,0.0,0.0);            
        }
        
        graph.setLineColor(1);
        graph.setLineThickness(1);
        graph.setMarkerColor(4);
        graph.setMarkerSize(12);
        graph.setMarkerStyle(4);
        graph.setTitleX("Model");
        graph.setTitleY("De-Noising Power");
        
        c1.draw(graph);

        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0, 10, 5.0, 30);
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis()
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, "25");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisTickLabels(
                new String[]{
                    "0","0a","0b","0c","0d","0e","0f","1","2"});
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        c1.repaint();
    }
    
    
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorPalette();
        GRootColorPalette.getInstance().setColorScheme("tab10");
        MLNetworksSummary.noise();
        MLNetworksSummary.noisePower();
        
    }
}

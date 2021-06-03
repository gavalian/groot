/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.util.List;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.io.CSVReader;
import org.jlab.groot.io.TextFileReader;
import org.jlab.jnp.groot.graphics.Legend;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class LuminocityAnalyzer {
    public static void analyzeFile(String filename, double lumi, double threshold){
        TextFileReader reader = new TextFileReader(",");
        reader.openFile(filename);
        H1F hn  = new H1F("hn",1200,0.0,30.0);
        H1F hnr = new H1F("hnr",1200,0.0,30.0);
        H1F rt = new H1F("rt",120,0.0,1.0);
        
        reader.readNext();
        while(reader.readNext()==true){
            double[] values = reader.getAsDouble(0,5);
            double noise = values[5]/values[1];
            double noise_left = values[4]/values[2];
            double eff = values[3]/values[1];
            hn.fill(noise);
            hnr.fill(noise_left);
            rt.fill(eff);

        }
        System.out.printf("%8.2f %8.4f %8.5f %8.5f %8.5f %8.5f %8.5f %8.5f\n",lumi,threshold,
                hn.getMean(),hn.getRMS(),hnr.getMean(),hnr.getRMS(),rt.getMean(), rt.getRMS());
    }
    
    public static void createData(){
        Integer[] beam = new Integer[]{45,50,55,90,100,110};
        String[]  tr = new String[]{
            "0.05","0.10","0.15","0.20","0.25","0.30","0.35","0.40","0.45","0.50"};
        

        for(int b = 0; b < beam.length; b++){
            for(int t = 0; t < tr.length; t++){
                String filename = "luminosity_threshold_experiments/t-"+tr[t]+"/" + beam[b].toString() + "/all_hits_data.csv";
                LuminocityAnalyzer.analyzeFile(filename,beam[b],Double.parseDouble(tr[t]));
            }
        }
    }
    
    public static void graphStudy1(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        
        Legend legend = new Legend(370,30);
        String[]  labels = new String[]{"45 nA", "50 nA", "55 nA", "90 nA", "100 nA", "110 nA"};
        for(int i = 0; i < 6; i++){
            System.out.println("adding graph ");
            GraphErrors gr = GraphErrors.readFile("treshold_study_n.data", i*10, 10, new int[]{1,4});
            gr.setTitleX("Threshold");
            gr.setTitleY("Noise Fraction");
            gr.show();
            gr.setLineColor(1);
            gr.setLineThickness(1);
            gr.setMarkerSize(10);
            gr.setMarkerStyle(i+1);
            gr.setMarkerColor(2+i);
            c1.draw(gr,"same");
            legend.add(gr, labels[i]);
        }
        c1.getDataCanvas().getRegion(0).addNode(legend);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0, 0.55, 0.05, 0.4);
        c1.repaint();
    }
    
    public static void graphStudy3(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        Legend legend = new Legend(370,30);
        String[]  labels = new String[]{"45 nA", "50 nA", "55 nA", "90 nA", "100 nA", "110 nA"};
        for(int i = 0; i < 6; i++){
            System.out.println("adding graph ");
            GraphErrors gr = GraphErrors.readFile("treshold_study_n.data", i*10, 10, new int[]{1,6});
            gr.show();
            gr.setTitleX("Threshold");
            gr.setTitleY("Hit Reconstruction Efficiency");
            gr.setLineColor(1);
            gr.setLineThickness(1);
            gr.setMarkerSize(10);
            gr.setMarkerStyle(i+1);
            gr.setMarkerColor(2+i);
            legend.add(gr, labels[i]);
            c1.draw(gr,"same");
        }
        c1.getDataCanvas().getRegion(0).addNode(legend);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0, 0.55, 0.75, 1.0);
        c1.repaint();
    }
    public static void graphStudy2(){
        TDataCanvas c1 = new TDataCanvas(850,750);
        
        for(int i = 0; i < 10; i++){
            //System.out.println("adding graph ");
            GraphErrors gr = GraphErrors.readFile("treshold_study.data", i*6, 6, new int[]{0,4});
            gr.show();
            gr.setLineColor(1);
            gr.setLineThickness(1);
            gr.setMarkerSize(10);
            gr.setMarkerStyle(i+1);
            gr.setMarkerColor(2+i);
            c1.draw(gr,"same");
        }
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(40, 115, 0.05, 0.4);
        c1.repaint();
    }
    
    
    public static void graphStudy4(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        Legend legend = new Legend(370,30);
        String[]  labels = new String[]{"45 nA", "50 nA", "55 nA", "90 nA", "100 nA", "110 nA"};
        //for(int i = 0; i < 6; i++){
            System.out.println("adding graph ");
            GraphErrors gr = GraphErrors.readFile("treshold_study.data", 0, 6, new int[]{0,2});
            gr.show();
            gr.setTitleX("Beam Current (nA)");
            gr.setTitleY("All Hits/Track Hits");
            gr.setLineColor(1);
            gr.setLineThickness(1);
            gr.setMarkerSize(10);
            gr.setMarkerStyle(2);
            gr.setMarkerColor(2);
            //legend.add(gr, labels[i]);
            c1.draw(gr);
        //}
        //c1.getDataCanvas().getRegion(0).addNode(legend);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(40, 115, 0.75, 8.0);
        c1.repaint();
    }
    
    public static void graphStudy5(){
        TDataCanvas c1 = new TDataCanvas(480,450);
        c1.getDataCanvas().left(65).bottom(50);
        // String[]  tr = new String[]{
         //   "0.05","0.10","0.15","0.20","0.25","0.30","0.35","0.40","0.45","0.50"};
         String[]  tr = new String[]{
            "0.05","0.10","0.20","0.30","0.40","0.50"};
        CSVReader reader = new CSVReader();
        Legend legend = new Legend(80,210);
        for(int t = 0; t < tr.length; t++){
            List<double[]> data = reader.readColumn("luminosity_threshold_experiments_summary/"+ tr[t]+"/model_comparison_results_with_threshold.csv", new int[]{0,13,14,15,16}, 1);
            GraphErrors gr = new GraphErrors();
            for(int i = 0; i < 6; i++){
                double   x  = data.get(i)[0];
                double tot  = data.get(i)[1];
                double tot4 = data.get(i)[2];
                double tot5 = data.get(i)[3];
                double tot6 = data.get(i)[4];
                
                gr.addPoint(x,(tot4+tot5+tot6)/tot,0.0,0.0);
                System.out.printf("%8.1f \n",data.get(i)[1]);                
            }
            gr.setTitleX("Beam Current [nA]");
            gr.setTitleY("Track Reconstruction Efficiency");
            gr.setLineColor(1);
            gr.setLineThickness(1);
            gr.setMarkerColor(2+t);
            gr.setMarkerStyle(1+t);
            gr.setMarkerSize(12);
            if(t<2) gr.show();
            legend.add(gr, tr[t]);
            c1.draw(gr,"same");
        }
        c1.getDataCanvas().getRegion(0).addNode(legend);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(40, 115, 0.72, 1.0);
        c1.repaint();
    }
    
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorPalette();
        GRootColorPalette.getInstance().setColorScheme("tab10");
        //LuminocityAnalyzer.graphStudy3();
        LuminocityAnalyzer.graphStudy4();
        //LuminocityAnalyzer.graphStudy5();
        //LuminocityAnalyzer.graphStudy2();
        //LuminocityAnalyzer.createData();
    }
}

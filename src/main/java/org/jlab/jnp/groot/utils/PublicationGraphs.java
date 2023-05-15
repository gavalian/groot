/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.BarGraph;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.io.CSVReader;
import org.jlab.groot.io.DataIO;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.graphics.AxisNode2D;
import org.jlab.jnp.groot.graphics.BarGraphNode2D;
import org.jlab.jnp.groot.graphics.Legend;
import org.jlab.jnp.groot.graphics.LegendNode2D;
import org.jlab.jnp.groot.graphics.PaveText;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class PublicationGraphs {
    public static void set1(){
        String file = "different_noise_results0b.csv";
        TDataCanvas c1 = new TDataCanvas(850,350);
        c1.divide(2, 1);
        GraphErrors lg = GraphErrors.csvGraphXYEY(file, 0, 3, 4, 1);
        GraphErrors ln = GraphErrors.csvGraphXYEY(file, 0, 7, 8, 1);
        
        lg.getVectorEY().divide(2.0);
        ln.getVectorEY().divide(2.0);
        
        lg.setMarkerSize(8);
        lg.setLineColor(2);
        lg.setMarkerColor(2);
        lg.setLineThickness(2);
        
        ln.setMarkerSize(8);
        ln.setLineColor(5);
        ln.setMarkerColor(5);
        ln.setLineThickness(2);
        lg.setTitleX("Beam Current (nA)");
        lg.setTitleY("Efficiency");
        c1.cd(0).draw(lg, "PLS");
        c1.cd(0).draw(ln, "samePLS");
        
        c1.getDataCanvas().bottom(55);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(40.0, 115, 0.0, 105.0);
        
        LegendNode2D legend = new LegendNode2D(80,100);
        legend.add(lg, "Hit reconstruction efficiency");
        legend.add(ln, "Noise reduction efficiency");
        c1.getDataCanvas().getRegion(0).addNode(legend);
        
        
        BarGraph bar = new BarGraph(new String[]{"45nA","50nA","55nA","90nA","100nA","110nA"},
                new double[][]{
                    {(12140.0+1479.0+72.0)/14770,(12140.0+1479.0)/14770,12140.0/14770},
                    {(12046.0+1456.0+82.0)/14770,(12046.0+1456.0)/14770,12046.0/14770},
                    {(11820.0+1596.0+87.0)/14770,(11820.0+1596.0)/14770,11820.0/14770},
                    {(9464.0+2358.0+174.0)/14770,(9464.0+2358.0)/14770,9464.0/14770},
                    {(9127.0+2431.0+201.0)/14770,(9127.0+2431.0)/14770,9127.0/14770},
                    {(8636.0+2509.0+245.0)/14770,(8636.0+2509.0)/14770,8636.0/14770}
                });
        
        bar.setPadding(8);
        bar.setBarColor(0, 2);
        bar.setBarColor(1, 3);
        bar.setBarColor(2, 5);
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        
                
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().addDataNode(new BarGraphNode2D(bar));
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().setAxisTickLabels(
                new String[]{
                    "45nA","50nA","55nA","90nA","100nA","110nA"});
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().setAxisLimits(0.0, 7, 0.0, 1.15);
        Legend barLegend = bar.getLegend(new String[]{"4 segments","5 segments","6 segment"});
        c1.getDataCanvas().getRegion(1).addNode(barLegend);
    }
    
     public static void set2(){
         
         GRootColorPalette palette = GRootColorPalette.getInstance();
        String file = "different_noise_results0b.csv";
        TDataCanvas c1 = new TDataCanvas(550,400);
        c1.divide(1, 1);
        c1.getDataCanvas().bottom(55);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        
        GraphErrors pos = new GraphErrors("pos",
                new double[]{5.0,20,40.0,50,70.0},
                new double[]{0.97841,0.93775,0.87062,0.83524,0.77406},
                new double[]{0.0,0.0,0.0,0.0,0.0},
                new double[]{0.0,0.0,0.0,0.0,0.0}
        );
        pos.setTitleX("Beam Current (nA)");
        pos.setTitleY("Efficiency");
        
        pos.setMarkerColor(3);
        pos.setMarkerSize(12);
        pos.setMarkerStyle(3);
        pos.setLineColor(3);
        
        GraphErrors neg = new GraphErrors("neg",
                new double[]{5.0,20,40.0,50,70.0},
                new double[]{0.97931,0.92643,0.85251,0.81307,0.74548},
                new double[]{0.0,0.0,0.0,0.0,0.0},
                new double[]{0.0,0.0,0.0,0.0,0.0}
        );
        neg.setMarkerColor(2);
        neg.setMarkerSize(12);
        neg.setMarkerStyle(4);
        neg.setLineColor(2);
        
        
        GraphErrors denoise = new GraphErrors("neg",
                new double[]{45.0,50.0,55.0,90.0,100.0,110.0},
                new double[]{0.98307,0.98009,0.97874,0.95200,0.94841,0.93927},
                new double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new double[]{0.0,0.0,0.0,0.0,0.0,0.0}
        );
        
        denoise.setMarkerColor(4);
        denoise.setMarkerSize(12);
        denoise.setMarkerStyle(1);
        denoise.setLineColor(4);
        
        c1.draw(pos, "PL");
        c1.draw(neg, "samePL");
        c1.draw(denoise, "samePL");
        
        F1D func = new F1D("func","[a]+[b]*x",4.5,75.0);
        func.setParameter(0, 1.0);
        func.setParameter(1, -0.35);
        func.setLineColor(7);
        func.setLineWidth(2);
        
        
        F1D dfunc = new F1D("func","[a]+[b]*x",40,115.0);
        dfunc.setParameter(0, 1.0);
        dfunc.setParameter(1, -0.35);
        dfunc.setLineColor(2);
        dfunc.setLineWidth(2);
        dfunc.setLineStyle(5);
        
        DataFitter.fit(func, pos, "N");
        DataFitter.fit(dfunc, denoise, "N");
        func.show();
        dfunc.show();
        c1.draw(func,"same");
        c1.draw(dfunc,"same");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0.0, 115, 0.4, 1.05);
        LegendNode2D legend = new LegendNode2D(60,200);
        legend.add(pos, "Positive track efficiency");
        legend.add(neg, "Negative track efficiency");
        legend.add(denoise, "De-noiser efficiency");
        c1.getDataCanvas().getRegion(0).addNode(legend);
        
        String fitString = String.format("f = %.2f%.5fx", func.getParameter(0),func.getParameter(1));
        String dfitString = String.format("f = %.2f%.5fx", dfunc.getParameter(0),dfunc.getParameter(1));
        
        PaveText funcText = new PaveText(fitString,350,200);
        funcText.setTextColor(palette.getColor(7));
        funcText.setBackgroundColor(Color.WHITE).setBorderColor(Color.WHITE);
        funcText.setFont(new Font("Avenir", Font.PLAIN, 18));
        
        PaveText dfuncText = new PaveText(dfitString,350,230);        
        
        dfuncText.setTextColor(palette.getColor(2));
        dfuncText.setBackgroundColor(Color.WHITE).setBorderColor(Color.WHITE);
        dfuncText.setFont(new Font("Avenir", Font.PLAIN, 18));
        
        c1.getDataCanvas().getRegion(0).addNode(funcText);
        c1.getDataCanvas().getRegion(0).addNode(dfuncText);
        c1.repaint();
     }
     
     public static void set3(){
         String file = "model_comparison_results.csv";
        TDataCanvas c1 = new TDataCanvas(850,350);
        c1.divide(2, 1);
        GraphErrors lg = GraphErrors.csvGraphXYEY(file, 0,  4,  5, 1);
        GraphErrors ln = GraphErrors.csvGraphXYEY(file, 0, 12, 13, 1);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
    
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
    
//lg.getVectorEY().divide(2.0);
        //ln.getVectorEY().divide(2.0);
        lg.setTitleX("Model");
        lg.setTitleY("Hit Reconstruction Efficiency (%)");
        lg.setMarkerSize(12);
        lg.setMarkerColor(2);
        lg.setLineColor(2);
        lg.setLineThickness(2);
        c1.cd(0).draw(lg, "PL");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0.0, 10, 0, 120.0);
        ln.setTitleX("Model");
        ln.setTitleY("Noise Removal Efficiency (%)");
        ln.setMarkerSize(12);
        ln.setMarkerStyle(2);
        ln.setMarkerColor(3);
        ln.setLineColor(3);
        ln.setLineThickness(2);
        c1.cd(1).draw(ln, "PL");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().setAxisLimits(0.0, 10, 0, 120.0);
        
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().setAxisTickLabels(
                new String[]{
                    "0","0a","0b","0c","0d","0e","0f","1","2"});
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisTickLabels(
                new String[]{
                    "0","0a","0b","0c","0d","0e","0f","1","2"});
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, "25");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, "25");
     }
     
     public static List<H1F> read(String directory){
         List<H1F> hList = new ArrayList<>();
         String[] dataFiles = new String[]{
             "hits_hist_data.csv","noise_hist_data.csv",
             "noise_red_hist_data.csv"
         };
         CSVReader reader = new CSVReader();
         for(int i = 0; i < dataFiles.length; i++){
             List<double[]> hdata       = reader.readColumn(directory+"/"+dataFiles[i], new int[]{0,1}, 0);
             H1F h = new H1F("h","",40,0.0,100.0);
             h.setTitleX("Efficiency");
             h.setTitleY("Count");
             h.setFillColor(52);
             h.setLineColor(1);
             h.setLineWidth(1);
             for(int j = 0; j < hdata.size(); j++){
             h.fill(hdata.get(j)[0], hdata.get(j)[1]);
             }
             hList.add(h);
         }
         return hList;
     }
     public static void set4(){
        
        TDataCanvas c1 = new TDataCanvas(850,750);
        c1.divide(2, 3);
         CSVReader reader = new CSVReader();
         
         String[] dirs = new String[]{"OneDrive/model_0a",
             "OneDrive/model_0b"
                 ,"OneDrive/model_0c","OneDrive/model_0d"
                 ,"OneDrive/model_0e","OneDrive/model_0f"
         };
        for(int i = 0; i < dirs.length; i++){
            List<H1F> hList = PublicationGraphs.read(dirs[i]);
            hList.get(1).setFillColor(55);
            hList.get(0).setFillColor(59);
            c1.cd(i).draw(hList.get(1)).
                    draw(hList.get(0),"same").draw(hList.get(2), "same");
        }
         
         c1.repaint();
     }
     
     public static void set5(){
         TDataCanvas c1 = new TDataCanvas(850,750);
         H1F h = new H1F("h1",100,0.0,1.0);
         H1F h2 = new H1F("h1",100,0.0,1.0);
         
         h.setFillColor(44);
         h2.setFillColor(42);
         DataIO.csvH1(h, "all_hits_data_005.csv", 7, 1);
         DataIO.csvH1(h2, "all_hits_data_005.csv", 6, 1);
         c1.draw(h2);
         c1.draw(h,"same");
         
         System.out.printf("H ==> mean = %.6f, rms = %.6f\n",h.getMean(),h.getRMS());
         System.out.printf("H2 => mean = %.6f, rms = %.6f\n",h2.getMean(),h2.getRMS());
     }
     
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorPalette();
        GRootColorPalette.getInstance().setColorScheme("tab10");
        //PublicationGraphs.set1();
        //PublicationGraphs.set2();
        //PublicationGraphs.set3();
        PublicationGraphs.set4();
        PublicationGraphs.set5();
    }
}

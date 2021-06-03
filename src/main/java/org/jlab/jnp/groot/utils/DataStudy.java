/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Arrays;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.groot.graphics.Legend;
import org.jlab.jnp.groot.graphics.PaveText;
import org.jlab.jnp.groot.graphics.TDataCanvas;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class DataStudy {
    
    
    public static List<GraphErrors>  readLuminocityGraphs(){
        GraphErrors  graph = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,2});
        GraphErrors  graph2 = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,3});
        
        return Arrays.asList(graph,graph2);
    }
    
    public static List<GraphErrors>  getStandardGraphs(){
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
        return Arrays.asList(neg,pos);
    }
    
    public static void drawDenoising(){
        TDataCanvas c1 = new TDataCanvas(450,350);
        GraphErrors denoise = new GraphErrors("neg",
                new double[]{45.0,50.0,55.0,90.0,100.0,110.0},
                new double[]{0.98307,0.98009,0.97874,0.95200,0.94841,0.93927},
                new double[]{0.0,0.0,0.0,0.0,0.0,0.0},
                new double[]{0.0,0.0,0.0,0.0,0.0,0.0}
        );
        
        
        List<GraphErrors>  graphs2 = DataStudy.readLuminocityGraphs();
        List<GraphErrors>  graphs = Arrays.asList(graphs2.get(0).divide(5.358978e+04),
                graphs2.get(1).divide(2.248018e+04));
        
        graphs.get(0).setMarkerColor(5);
        //graphs.get(0).divide(denoise)
        denoise.setMarkerColor(4);
        denoise.setMarkerSize(12);
        denoise.setMarkerStyle(1);
        denoise.setLineColor(1);
        denoise.setLineThickness(1);
        
        F1D func = new F1D("func","[a]+[b]*x",42,110);
        func.setParameter(0, 1.0);
        func.setParLimits(0, 0.99, 1.01);
        func.setParameter(1, 0.03);
        F1D funcR = new F1D("func","[a]+[b]*x",0.,110);
        F1D funcN = new F1D("func","[a]+[b]*x",0.,110);
        
        func.setLineStyle(2);
        func.setLineWidth(2);
        denoise.setTitleX("Beam Current (nA)");
        denoise.setTitleY("Segments Retention Efficiency");
        DataFitter.fit(func, denoise, "N");
        c1.draw(denoise).draw(func,"same");
        
        //c1.draw(graphs.get(0),"same").draw(graphs.get(1),"same");
        DataFitter.fit(funcR, graphs.get(0), "N");
        DataFitter.fit(funcN, graphs.get(1), "N");
        //c1.draw(funcR,"same").draw(funcN,"same");
        
        //funcR.show();
        //funcN.show();
        c1.setAxisLimits(35, 120, 0.8, 1.05);
        
        
    }
    public static void drawMerging(){
        
        GraphErrors  graph = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,3});
        GraphErrors  graph2 = GraphErrors.readFile("denoise_stats.data", 6, 6, new int[]{0,3});
        
        
        GraphErrors  datagraph = GraphErrors.readFile("data_denoise_stats.data", 0, 2, new int[]{0,3});
        GraphErrors  datagraph2 = GraphErrors.readFile("data_denoise_stats.data", 2, 2, new int[]{0,3});
        
        
        graph.statErrors();
        graph.show();
        
        graph2.statErrors();
        
        TDataCanvas c1 = new TDataCanvas(450,350);
        GraphErrors result = graph2.divide(graph);
        result.show();
        
        GraphErrors dataresult = datagraph2.divide(datagraph);
        
        result.setMarkerSize(12);
        result.setMarkerColor(2);
        result.setLineThickness(1);
        dataresult.setMarkerColor(3);
        dataresult.setMarkerStyle(2);
        dataresult.setMarkerSize(12);
        dataresult.setLineThickness(1);
        
        dataresult.show();
        
        List<GraphErrors> clas12 = DataStudy.getStandardGraphs();
        
        F1D func = new F1D("func","[a]+[b]*x",0.,56);
        func.setParameter(0, 1.0);
        func.setParLimits(0, 0.99, 1.01);
        func.setParameter(1, 0.03);
        func.setLineStyle(2);
        func.setLineWidth(2);
        DataFitter.fit(func, result, "N");
        func.show();
        c1.divide(1, 1);
        result.setTitleX("Beam Current (nA)");
        result.setTitleY("Track Multiplicity Ratio");
        
        c1.cd(0).draw(result)//.draw(func, "same")
                .draw(dataresult, "same");
                //.draw(clas12.get(0),"same")
                //.draw(clas12.get(1), "same");
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(35,120.,0.75, 2.0);
        String fitString = String.format("f = %.2f + %.5fx", func.getParameter(0),func.getParameter(1));
        
        PaveText funcText = new PaveText(fitString,60,25);
        funcText.setBorderColor(Color.white);
        funcText.setFont(new Font("Avenir",Font.PLAIN,18));
        //c1.getDataCanvas().getRegion(0).addNode(funcText);
        Legend legend = new Legend(60,30);
        legend.add(result, "Background Merged Data");
        legend.add(dataresult,  "Experimental Data");
        c1.addLegend(legend);
        
        c1.repaint();
        //funcText.setTextColor(palette.getColor(7));
        
    }
    
    
    public static void drawML(){
        
        GraphErrors  graph = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,2});
        GraphErrors  graph2 = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,4});
                
        GraphErrors  datagraph = GraphErrors.readFile("data_denoise_stats.data", 0, 2, new int[]{0,3});
        GraphErrors  datagraph2 = GraphErrors.readFile("data_denoise_stats.data", 0, 2, new int[]{0,5});
        
        graph.statErrors();
        graph.show();
        
        graph2.statErrors();
        
        TDataCanvas c1 = new TDataCanvas();
        GraphErrors result = graph2.divide(graph);
        result.show();
        GraphErrors dataresult = datagraph2.divide(datagraph);
        
        result.setMarkerSize(12);
        result.setMarkerColor(2);
        result.setLineThickness(1);
        dataresult.setMarkerColor(3);
        dataresult.setMarkerStyle(2);
        dataresult.setMarkerSize(12);
        dataresult.setLineThickness(1);
        
        List<GraphErrors> clas12 = DataStudy.getStandardGraphs();
        
        F1D func = new F1D("func","[a]+[b]*x",0.,56);
        func.setParameter(0, 1.0);
        func.setParLimits(0, 0.99, 1.01);
        func.setParameter(1, 0.03);
        func.setLineStyle(2);
        func.setLineWidth(2);
        DataFitter.fit(func, result, "N");
        func.show();
        c1.divide(1, 1);
        c1.cd(0).draw(result).draw(func, "same")
                .draw(dataresult, "same")
                .draw(clas12.get(0),"same")
                .draw(clas12.get(1), "same");
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0.0,120.,0.95, 1.25);
        String fitString = String.format("f = %.2f + %.5fx", func.getParameter(0),func.getParameter(1));
        
        PaveText funcText = new PaveText(fitString,350,200);
        funcText.setFont(new Font("Avenir",Font.PLAIN,24));
        c1.getDataCanvas().getRegion(0).addNode(funcText);
        
        c1.repaint();
        //funcText.setTextColor(palette.getColor(7));
        
    }
    public static void test(){
        
        GraphErrors  graph = GraphErrors.readFile("denoise_stats.data", 0, 6, new int[]{0,3});
        GraphErrors  graph2 = GraphErrors.readFile("denoise_stats.data", 0, 3, new int[]{0,3});
        TDataCanvas c1 = new TDataCanvas(450,350);

        F1D func2 = new F1D("func","[a]+[b]*x",0.,56);
        F1D func = new F1D("func","[a]+[b]*x",0.,115);
        
        DataFitter.fit(func, graph, "N");
        DataFitter.fit(func2, graph2, "N");
        
        func2.show();
        func.show();
        
        F1D f2 = new F1D("func","[a]+[b]*x",0.,56);
        F1D f = new F1D("func","[a]+[b]*x",0.,115);
        GraphErrors g = graph.divide(func.getParameter(0));
        GraphErrors g2 = graph2.divide(func2.getParameter(0));
        
        DataFitter.fit(f, g, "N");
        DataFitter.fit(f2, g2, "N");
        
        c1.divide(2,1);
        c1.cd(0).draw(graph).draw(func,"same");
        c1.cd(1).draw(graph2).draw(func2,"same");
        
        c1.cd(0).draw(g).draw(f, "same").setAxisLimits(0., 120, 0., 1.2);
        c1.cd(1).draw(g2).draw(f2, "same").setAxisLimits(0., 120, 0., 1.2);
        
        PaveText funcText = new PaveText(String.format("f=%.2f+%.5fx", f.getParameter(0),f.getParameter(1)),60,25);
        funcText.setBorderColor(Color.white);
        funcText.setFont(new Font("Avenir",Font.PLAIN,18));
        c1.getDataCanvas().getRegion(0).addNode(funcText);
        
        PaveText funcText2 = new PaveText(String.format("f=%.2f+%.5fx", f2.getParameter(0),f2.getParameter(1)),60,25);
        funcText2.setBorderColor(Color.white);
        funcText2.setFont(new Font("Avenir",Font.PLAIN,18));
        c1.getDataCanvas().getRegion(1).addNode(funcText2);
               
    }
    
    public static GraphErrors getGraphNormalized(GraphErrors graph){
        F1D func = new F1D("func","[a]+[b]*x",0.0,115.0);
        func.setParameter(0, 10);
        func.setParameter(1, 20);
        DataFitter.fit(func, graph, "N");
        func.show();
        return graph.divide(func.getParameter(0));
    }
    
    public static void test2(){
        GraphErrors gr = new GraphErrors("graph", new double[]{5,50,55,60}, 
                new double[]{16082,10659,10573,10254});
        
        GraphErrors gr2 = new GraphErrors("graph", new double[]{95,105,115}, 
                new double[]{6384,5731,4672});
        
        GraphErrors graph2 = gr2.divide(1.654776e+04);
        graph2.setMarkerColor(3);
        graph2.setMarkerSize(8);
        graph2.setLineThickness(1);
        
        
        
        
        TDataCanvas c1 = new TDataCanvas(450,350);
        F1D func = new F1D("func","[a]+[b]*x",0.0,120.0);
        func.setParameter(0, 1.0);
        func.setParameter(1, 0.004);
        
        GraphErrors graph = DataStudy.getGraphNormalized(gr);
        graph.setMarkerColor(2);
        graph.setMarkerSize(8);
        graph.setLineThickness(1);
        
        DataFitter.fit(func, graph, "N");
        c1.draw(graph).draw(func,"same").setAxisLimits(0, 120, 0, 1.25);
        c1.draw(graph2,"same");
        
        func.show();
        
        PaveText funcText2 = new PaveText(String.format("f=%.2f+%.5fx", func.getParameter(0),func.getParameter(1)),60,25);
        funcText2.setBorderColor(Color.white);
        funcText2.setFont(new Font("Avenir",Font.PLAIN,18));
        c1.getDataCanvas().getRegion(0).addNode(funcText2);
    }
    
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorPalette();
        GRootColorPalette.getInstance().setColorScheme("tab10");
        DataStudy.drawMerging();
        //DataStudy.drawDenoising();
        //DataStudy.drawML();
        
        //DataStudy.test2();
    }
    
}

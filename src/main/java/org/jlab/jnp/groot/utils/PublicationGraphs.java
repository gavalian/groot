/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import java.awt.Color;
import java.awt.Font;
import org.jlab.groot.data.BarGraph;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.graphics.AxisNode2D;
import org.jlab.jnp.groot.graphics.BarGraphNode2D;
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
                new double[]{0.9269,0.9197,0.9142,0.8121,0.7961,0.7711},
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
     
    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorPalette();
        GRootColorPalette.getInstance().setColorScheme("tab10");
        //PublicationGraphs.set1();
        PublicationGraphs.set2();
    }
}

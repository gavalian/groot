/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.utils;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.jnp.groot.graphics.PaveText;
import org.jlab.jnp.groot.graphics.TDataCanvas;

/**
 *
 * @author gavalian
 */
public class MLPExamplePlots {
    
        public static void datasample2(){
        double[] y = new double[]{1,2,3,4,5,6};
        double[] xt = new double[]{0.47,0.52,0.44,0.46,0.31, 0.34};
        
        double[] xtr = new double[]{0.47,0.3,0.35,0.48,0.55, 0.34};
        double[] xll = new double[]{0.15,0.3,0.35,0.41,0.55, 0.6};
        
        double[] xcn = new double[]{0.47,0.52,0.44,0.41,0.31, 0.34};
        
        GraphErrors  h = new GraphErrors("true",xt,y);
        GraphErrors  hr = new GraphErrors("true",xtr,y);
        GraphErrors  hl = new GraphErrors("true",xll,y);
        GraphErrors  hcn = new GraphErrors("true",xcn,y);
        
        TDataCanvas c1 = new TDataCanvas(850,550);
        c1.divide(new double[][]{{0.33,0.33,0.33},{0.33,0.33,0.33}});
        //h.setFillColor(45);
        //h.setLineWidth(1);
        //h.setTitleX("Particle Momentum [GeV]");
        h.setTitleX("Wire");
        h.setTitleY("Layer");
        h.setMarkerSize(10);
        
        hr.setTitle("Random");
        hr.setTitleX("Wire");
        hr.setTitleY("Layer");
        hr.setMarkerSize(10);
        
        hl.setTitle("Random");
        hl.setTitleX("Wire");
        hl.setTitleY("Layer");
        hl.setMarkerSize(10);
                
        hcn.setTitle("Random");
        hcn.setTitleX("Wire");
        hcn.setTitleY("Layer");
        hcn.setMarkerSize(10);
        c1.cd(0).draw(h);
        c1.setAxisLimits(0., 1.0, 0.5, 6.5);
        
        c1.cd(0).draw(h).cd(1).draw(h).cd(2).draw(h);
        c1.cd(3).draw(hl);
        c1.cd(4).draw(hr);
        c1.cd(5).draw(hcn);
        
        for(int i =0; i < 8; i++){
            c1.cd(i).setAxisLimits(0., 1.0, 0.5, 6.5);
        }
        //PaveText paveTrue = new PaveText("True Track",58,30);
        
        
        for(int i = 0 ; i < 3; i++){
            PaveText paveTrue = new PaveText("True Track",58,30);
            c1.getDataCanvas().getRegion(i).addNode(paveTrue);
        }
        
        PaveText paveLL = new PaveText("Least Likely",58,30);
        PaveText paveRR = new PaveText("Random",58,30);
        PaveText paveCN = new PaveText("Closest Neighbor",58,30);
        c1.getDataCanvas().getRegion(3).addNode(paveLL);
        c1.getDataCanvas().getRegion(4).addNode(paveRR);
        c1.getDataCanvas().getRegion(5).addNode(paveCN);
        
        //System.out.println(h.toString());
        //c1.getDataCanvas().bottom(45);
        //c1.getDataCanvas().left(55);
    }
    public static void datasample(){
        double[] y = new double[]{1,2,3,4,5,6};
        double[] xt = new double[]{0.47,0.52,0.44,0.46,0.31, 0.34};
        
        double[] xtr = new double[]{0.47,0.3,0.35,0.48,0.55, 0.34};
        double[] xll = new double[]{0.15,0.3,0.35,0.41,0.55, 0.6};
        
        double[] xcn = new double[]{0.47,0.52,0.44,0.41,0.31, 0.34};
        
        GraphErrors  h = new GraphErrors("true",xt,y);
        GraphErrors  hr = new GraphErrors("true",xtr,y);
        GraphErrors  hl = new GraphErrors("true",xll,y);
        GraphErrors  hcn = new GraphErrors("true",xcn,y);
        
        TDataCanvas c1 = new TDataCanvas(850,550);
        c1.divide(new double[][]{{1.0},{0.33,0.33,0.33}});
        //h.setFillColor(45);
        //h.setLineWidth(1);
        //h.setTitleX("Particle Momentum [GeV]");
        h.setTitleX("Wire");
        h.setTitleY("Layer");
        h.setMarkerSize(10);
        
        hr.setTitle("Random");
        hr.setTitleX("Wire");
        hr.setTitleY("Layer");
        hr.setMarkerSize(10);
        
        hl.setTitle("Random");
        hl.setTitleX("Wire");
        hl.setTitleY("Layer");
        hl.setMarkerSize(10);
                
        hcn.setTitle("Random");
        hcn.setTitleX("Wire");
        hcn.setTitleY("Layer");
        hcn.setMarkerSize(10);
        c1.cd(0).draw(h);
        c1.setAxisLimits(0., 1.0, 0.5, 6.5);
        
        c1.cd(1).draw(hl);
        c1.cd(2).draw(hr);
        c1.cd(3).draw(hcn);
        
        for(int i =1; i < 4; i++){
            c1.cd(i).setAxisLimits(0., 1.0, 0.5, 6.5);
        }
        
        //System.out.println(h.toString());
        //c1.getDataCanvas().bottom(45);
        //c1.getDataCanvas().left(55);
    }
    
    public static void main(String[] args){
        MLPExamplePlots.datasample2();
    }
}

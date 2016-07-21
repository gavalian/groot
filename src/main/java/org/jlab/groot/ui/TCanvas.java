/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import org.jlab.groot.graphics.FunctionPlotter;
import org.jlab.groot.graphics.HistogramPlotter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.math.RandomFunc;

/**
 *
 * @author gavalian
 */
public class TCanvas extends JFrame {
    
    JPanel  framePane = null;
    EmbeddedCanvas  canvas = null; 
    JPanel  statusPane  = null;
    JLabel  statusLabel = null;
    
    public TCanvas(String name, int xsize, int ysize){
        super();
        this.setTitle(name);
        this.setSize(xsize, ysize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new EmbeddedCanvas();
    
        framePane= new JPanel();
        framePane.setLayout(new BorderLayout());
        framePane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 4, 4, 4), new EtchedBorder()));
        //framePane.setBorder(new BevelBorder(BevelBorder.LOWERED));    
        
        statusPane = new JPanel();
        statusPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Avenir",Font.PLAIN,12));
        statusPane.setBorder(new BevelBorder(BevelBorder.LOWERED));        
        statusPane.add(statusLabel);
        
        framePane.add(canvas, BorderLayout.CENTER);
        framePane.add(statusPane,BorderLayout.PAGE_END);
        
        this.add(framePane);
        //this.pack();
        this.setVisible(true);
    }
    
    public EmbeddedCanvas  getCanvas(){
        return this.canvas;
    }
    
    public void divide(int xsize, int ysize){
        this.canvas.divide(xsize, ysize);
    }
    
    public void cd(int pad){
        this.canvas.cd(pad);
    }
    
    public void draw(IDataSet ds, String options){
        this.canvas.draw(ds, options);
        this.getCanvas().update();
    }
    
    public void draw(IDataSet ds){
        this.canvas.draw(ds);
        this.getCanvas().update();
    }
    
    public static void main(String[] args){
        
        
        TCanvas c1 = new TCanvas("c1",900,1200);
        
        GraphErrors  graph = new GraphErrors();
        graph.addPoint(1.0, 1.0, 0.0, 0.0);
        graph.addPoint(2.0, 8.0, 0.0, 0.0);
        graph.addPoint(3.0, 12.0, 0.0, 0.0);
        graph.addPoint(4.0, 9.0, 0.0, 0.0);
        
        H1F  h1 = FunctionFactory.randomGausian(80, 0.1, 5.0, 8000, 2.2, 0.4);
        H1F  h2 = FunctionFactory.randomGausian(80, 0.1, 5.0, 20000, 3.3, 0.2);
        
        H2F  h3 = FunctionFactory.randomGausian2D(120, 0.1, 5.0, 2000000, 2.4, 0.55);
        
        h1.setName("h100");
        h2.setName("h200");
        h2.setLineColor(1);
        h2.setFillColor(44);
        h1.setFillColor(33);
        GraphErrors  gr = h1.getGraph();
        c1.getCanvas().divide(2,2);
        //c1.getCanvas().initTimer(600);
        h1.add(h2);
        //c1.getCanvas().getPad(0).setAxisFontSize(14);
        //c1.getCanvas().getPad(0).addPlotter(new GraphErrorsPlotter(gr));
        
        
        F1D func = new F1D("func","[amp]*gaus(x,[mean],[sigma])",0.1,0.8);
        func.setParameters(new double[]{120,0.4,0.05});
        
        F1D func2 = new F1D("func2","[p0]+[p1]*x+[amp]*gaus(x,[mean],[sigma])",0.1,0.8);
        func2.setParameters(new double[]{1.0,1.0,120,0.4,0.05});
        func2.setLineColor(4);
        DataFitter.fit(func, h2, "E");
        DataFitter.fit(func2, h1, "E");
        
        F1D func3 = new F1D("func3","[p0]+[p1]*x+[amp]*gaus(x,[mean],[sigma])+[amp2]*gaus(x,[mean2],[sigma2])",0.1,5.0);
        
        
        func3.setParameters(new double[]{1.0,0.0,10.0,2.2,0.3,10.0,3.3,0.25});
        func3.setParLimits(0, 0.0, 1000);
        func3.setParLimits(1, 0.0, 1000);
        func3.setParLimits(2, 0, 1000);
        func3.setParLimits(3, 0, 1000);
        func3.setParLimits(4, 0, 1000);
        func3.setParLimits(5, 0, 1000);
        func3.setParLimits(6, 0, 1000);
        func3.setParLimits(7, 0, 1000);
        
        func3.setParStep(0, 0.0);
        func3.setLineWidth(3);


        h1.setLineColor(4);
        func3.show();

        c1.divide(2,3);
        //h1.divide(40.0);
        DataFitter.fit(func3, h1, "E");
        //c1.draw(h1);

        RandomFunc  random = new RandomFunc(func3,1000);
        c1.cd(0);
        c1.draw(func3);        
        c1.cd(1);
        c1.draw(random.getGraph());
        c1.getCanvas().update();
        
        
        Func1D func4 = FunctionFactory.createFunction(0);
        Func1D func5 = FunctionFactory.createFunction(1);
        
        H1F h4 = FunctionFactory.createH1F(0, 120, 5000);
        H1F h5 = FunctionFactory.createH1F(1, 120, 5000);
        h4.setXTitle("Random Gaussian");
        h5.setXTitle("Random Gaussian");
        func5.setRange(0.1, 5.0);
        //func5.setParStep(3, 0.0);
        //func5.setParameter(3, 2.0);
        DataFitter.fit(func5, h5, "N");
        
        func5.show();
        c1.cd(0);
        c1.getCanvas().getPad(0).getAxisFrame().getAxisX().setTitle("Random Gaussian");
        c1.getCanvas().getPad(1).getAxisFrame().getAxisX().setTitle("Random Gaussian");
        c1.draw(h4);
        c1.cd(1);
        c1.draw(h5);
        for(int i = 2; i <6; i++){
            c1.cd(i);
            c1.getCanvas().getPad(i).setStatBoxFontSize(14);
            //c1.getCanvas().getPad(i).setAxisRangeY(0, 50);
            c1.draw(h5);
        }
        /*
        c1.getCanvas().divide(2, 2);
        for(int i = 0; i < 4; i++){
            c1.getCanvas().cd(i);
            c1.getCanvas().draw(h3,"colz");
        }*/
        


    }
}

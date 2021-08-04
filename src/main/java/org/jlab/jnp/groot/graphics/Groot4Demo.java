/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.Color;
import java.util.Random;
import org.jlab.groot.data.BarGraph;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.graphics.LegendNode2D.LegendStyle;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 * Demo showing some of important features of the GROOT
 * package.
 * @author gavalian
 */
public class Groot4Demo {

    public static H1F createHistogram(int sample, int maxStat){
        H1F h = new H1F("h100","Random Histogram",100,0.0,1.0);
        Random rand = new Random();
        for(int i = 0; i < maxStat; i++){
            h.fill(rand.nextFloat());
            h.fill(rand.nextGaussian()*0.2+0.5);
        }
        return h;
    }
    
    public static H2F createJuliaSet(){
        H2F h2 = new H2F();
        //Complex a = new Complex();
        return h2;
    }
    
    public static void demo1(){
        
        TDataCanvas c1 = new TDataCanvas(900,800);
        c1.getDataCanvas().divide(new double[][]{{0.7,0.3},{0.4,0.6}});
        
        LegendNode2D legend = new LegendNode2D(200,30);
        
        for(int i = 0; i <= 9; i++){
            GraphErrors graph = new GraphErrors();
            //for(int p = 0; p <= 10; p++){
            graph.addPoint(1.0, 3.0 + i*2.2 , 0.0, 0.8);
            graph.addPoint(2.0, 2.0 + i*2.2 , 0.0, 0.6);
            graph.addPoint(3.0, 1.5 + i*2.2 , 0.0, 0.3);
            graph.addPoint(4.0, 1.2 + i*2.2 , 0.0, 0.4);
            graph.addPoint(5.0, 1.0 + i*2.2 , 0.0, 0.5);
            //}
            
            graph.setMarkerColor(i+2);
            graph.setLineColor(i+2);
            graph.setMarkerSize(12);
            int style = i%7 + 1;
            if(i<7){
                int color = i+2;
                legend.add(graph, "color=" + color + ",style="+style);
            }
            graph.setMarkerStyle(style);
            c1.getDataCanvas().cd(0).draw(graph, "samePLS");
            //c1.getDataCanvas().cd(2).draw(graph, "samePLS");
            //c1.getDataCanvas().cd(3).draw(graph, "samePLS");
            
        }
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0.0,6.0,0.0,25);
        c1.getDataCanvas().getRegion(0).addNode(legend);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().setAxisLimits(0.0,1.0,0.0,350);
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().setAxisLimits(0.0,6.0,0.0,25);
        
        
        c1.getDataCanvas().getRegion(1).getGraphicsAxis()
                .getAxisX().getAttributes()
                .changeValue(AttributeType.AXISDRAWBOX, "false");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis()
                .getAxisY().getAttributes()
                .changeValue(AttributeType.AXISDRAWBOX, "false");
        
        c1.getDataCanvas().getRegion(2).getGraphicsAxis()
                .getAxisX().getAttributes()
                .changeValue(AttributeType.AXISDRAWBOX, "false");
        c1.getDataCanvas().getRegion(2).getGraphicsAxis()
                .getAxisY().getAttributes()
                .changeValue(AttributeType.AXISDRAWBOX, "false");
    
        H1F h10 = Groot4Demo.createHistogram(0,10000);
        H1F h11 = Groot4Demo.createHistogram(0,6000);
        h10.setLineColor(1).setFillColor(34);
        h11.setLineColor(1).setFillColor(32);
        c1.getDataCanvas().cd(1).draw(h10, "");
        c1.getDataCanvas().cd(1).draw(h11, "same");
        
        
        F1D f1 = new F1D("f1","[a]*sin(x)",0.0,Math.PI*4);
        f1.setParameter(0, 2);
        f1.setLineColor(7);
        f1.setLineStyle(1);
        f1.setLineWidth(2);
        F1D f2 = new F1D("f1","[a]*sin(x)",0.0,Math.PI*4);
        f2.setParameter(0, 1);
        f2.setLineColor(3);
        f2.setLineStyle(2);
        f2.setLineWidth(2);
        
        F1D f3 = new F1D("f1","[a]*sin(x)",0.0,Math.PI*4);
        f3.setParameter(0, 0.5);
        f3.setLineColor(5);
        f3.setLineStyle(3);
        f3.setLineWidth(2);
        F1D f4 = new F1D("f1","[a]*cos(x)",0.0,Math.PI*4);
        f4.setParameter(0, 1.5);
        f4.setLineColor(2);
        f4.setLineStyle(0);
        f4.setLineWidth(2);
        
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().addDataNode(new FunctionNode1D(f1));
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().addDataNode(new FunctionNode1D(f2));
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().addDataNode(new FunctionNode1D(f3));
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().addDataNode(new FunctionNode1D(f4));
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().setAxisLimits(0,Math.PI*4,-2.5,2.5);
        
        H2F h2 = Julia.getJulia();
        c1.getDataCanvas().getRegion(3).getGraphicsAxis().addDataNode(new HistogramNode2D(h2));
        c1.getDataCanvas().getRegion(3).getGraphicsAxis().setAxisLimits(-1.5,1.5,-1.5,1.5);
        
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");;
        c1.getDataCanvas().getRegion(2).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");;
        
        c1.getDataCanvas().getRegion(3).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");;
        c1.getDataCanvas().getRegion(3).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");;
        
        c1.repaint();
    }
    
    public static class Complex {
        
        private final double re;   // the real part
        private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(re, im);
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
        return Math.atan2(im, re);
    }

    // return a new Complex object whose value is (this + b)
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // return a new object whose value is (this * alpha)
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    // return a new Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() { return re; }
    public double im() { return im; }

    // return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }
    


    // a static version of plus
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    // See Section 3.3.
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    // See Section 3.3.    
    }
    public static class Julia {
        static int julia(Complex c, Complex z, int maximumIterations) {
            for (int t = 0; t < maximumIterations; t++) {
                if (z.abs() > 2.0) return t;
                z = z.times(z).plus(c);
            }
            return maximumIterations - 1;
        }
        
        public static H2F getJulia(){
             // % java ColorJulia -0.75 0.1 < mandel.txt
             //  % java ColorJulia -1.25 0 < mandel.txt
             //  % java ColorJulia 0.1 0.7 < mandel.txt
             
            double xmin   = -1.5;
            double ymin   = -1.5;
            double width  =  3.0;
            double height =  3.0;
            int n = 512;
            
            //int ITERS  = 256;
            int ITERS  = 1024;
            
            //Complex c = new Complex(-0.4,0.6);
            Complex c = new Complex(-0.8, 0.156);
            
            H2F h2 = new H2F("h200","Julia",n,xmin,xmin+width,n, ymin,ymin+height);
            for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                double x = h2.getXAxis().getBinCenter(col);
                double y = h2.getYAxis().getBinCenter(row);
                Complex z = new Complex(x, y);
                int t = 10*julia(c, z, ITERS)+10;
                //picture.set(col, n - 1 - row, colors[t]);
                double value = t;// Math.log(t);
                h2.setBinContent(col, row, value);
            }
        }
            return h2;
        }
    }
    
    public static void demo2(){
        TDataCanvas c1 = new TDataCanvas(600,400);
        c1.getDataCanvas().divide(1, 1);
        //c1.getDataCanvas().divide(new double[][]{ {0.65,0.35},{1.0} });
        BarGraph bar = new BarGraph(new String[]{"ROOT","HIPO","AVRO","EVIO","HDF5"},
                new double[][]{
                    {0.4,0.5,0.8,0.5},
                    {0.2,0.7,0.6,0.8},
                    {0.4,0.8,0.7,0.8},
                    {0.45,0.9,0.2,0.8},
                    {0.75,0.15,0.65,0.8}
                });
        
        bar.setPadding(8);
        bar.setBarColor(0, 27);
        bar.setBarColor(1, 23);
        bar.setBarColor(2, 29);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addDataNode(new BarGraphNode2D(bar));
        GraphicsAxis axis = c1.getDataCanvas().getRegion(0).getGraphicsAxis();
        axis.getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        axis.getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        axis.getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        axis.getAxisX().setAxisTickLabels(
                new String[]{"ROOT","HIPO","AVRO","EVIO","HDF5"});
        
        axis.setAxisLimits(0.5, 5.5, 0.0, 1.05);
        
        GraphErrors gr1 = new GraphErrors();
        gr1.setLineColor(1);
        gr1.setMarkerColor(27);
        gr1.setMarkerStyle(2);
        gr1.setLineThickness(1);
        
        GraphErrors gr2 = new GraphErrors();
        gr2.setLineColor(1);
        gr2.setMarkerColor(23);
        gr2.setMarkerStyle(2);
        gr2.setLineThickness(1);
        
        GraphErrors gr3 = new GraphErrors();
        gr3.setLineColor(1);
        gr3.setMarkerColor(29);
        gr3.setMarkerStyle(2);
        gr3.setLineThickness(1);
        
        LegendNode2D legend = new LegendNode2D(60,25,LegendStyle.ONELINE);
        legend.add(gr1, "4 super layer");
        legend.add(gr2, "5 super layer");
        legend.add(gr3, "6 super layer");
        
        c1.getDataCanvas().getRegion(0).addNode(legend);
    }
    
    public static void demo3(){
        TDataCanvas c1 = new TDataCanvas(600,400);
        c1.getDataCanvas().divide(1, 1);
        
        GraphicsAxis axis = c1.getDataCanvas().getRegion(0).getGraphicsAxis();
        
        axis.getAxisX().setAxisLabelType(AxisNode2D.AXIS_LABELS_FORCED);
        axis.getAxisX().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        axis.getAxisY().getAttributes().changeValue(AttributeType.AXISTICKSIZE, "-5");
        axis.getAxisX().setAxisTickLabels(new String[]{"CUDA","OpenCL","CPU","GPU"});
        axis.setAxisLimits(0.5, 4.5, 0.0, 2.5);
    }
    
    
    public static void demo4(){
        TDataCanvas c1 = new TDataCanvas(600,400);
        c1.getDataCanvas().divide(1, 1);
        
        //c1.getDataCanvas().left(100).right(100).top(100).bottom(120);
        c1.getDataCanvas().bottom(45);
        PaveText text = new PaveText(60,120);
        text.addLines(new String[]{"negative",
            "positive","de-noising",
            "ai assisted"}).setSpacing(0.0);
        
        PaveText text2 = new PaveText(60,150);
        text2.addLines(new String[]{"mean = 0.54x10^2, ",
            "rms = 0.02346, ","overflow = 12456, ",
            "underflow = 6789"}).setSpacing(0.2);
        
        PaveText text3 = new PaveText(60,30);
        text3.addLines(new String[]{
            "mean=0.5456,",
            "rms=0.023x10^3,","over=12456,",
            "under=6789"
        }).setSpacing(0.4);
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_LABELS, "false");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TICKS, "false");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXIS_DRAW_TITLE, "false");
        
        c1.getDataCanvas().getRegion(0).addNode(text);
        c1.getDataCanvas().getRegion(0).addNode(text2);
        c1.getDataCanvas().getRegion(0).addNode(text3);
    }
    
    public static void demo5(DataCanvas canvas, int pad, boolean dark){
        int size = 22;
        canvas.cd(pad);
        String options = "same";
        for(int i = 0; i < size; i++){
            F1D func = new F1D("func","[a]",0.5,9.5);
            func.setParameter(0, i+1);
            func.setLineWidth(2);
            func.setLineStyle(i+1);
            if(dark==true) func.setLineColor(0);
            if(i!=0){
                canvas.draw(func, "same");
            } else {                
                canvas.draw(func);
            }
        }
        canvas.getRegion(pad).getGraphicsAxis().setAxisLimits(0, 10, 0, size+1);
        canvas.getRegion(pad).getGraphicsAxis().getAxisY().setTitle("Line Style");
    }
    
    public static void demo6(DataCanvas canvas, int pad, boolean dark){
        LegendNode2D legend = new LegendNode2D(200,30);
        if(dark==true){
            legend.setBackgroundColor(69, 90, 101);
            legend.setTextColor(Color.WHITE);
        }
        int yoffset = 1;
        for(int i = 0; i <= 17; i++){
            GraphErrors graph = new GraphErrors();
            //for(int p = 0; p <= 10; p++){
            graph.addPoint(1.0, yoffset + 3.0 + i*2.2 , 0.0, 0.8);
            graph.addPoint(2.0, yoffset + 2.0 + i*2.2 , 0.0, 0.6);
            graph.addPoint(3.0, yoffset + 1.5 + i*2.2 , 0.0, 0.3);
            graph.addPoint(4.0, yoffset + 1.2 + i*2.2 , 0.0, 0.4);
            graph.addPoint(5.0, yoffset + 1.0 + i*2.2 , 0.0, 0.5);
            
            graph.setMarkerColor(i+1);
            graph.setLineColor(i+1);
            graph.setMarkerSize(12);
            int style = i%7 + 1;
            if(i<7){
                int color = i+1;
                legend.add(graph, "color=" + color + ",style="+style);
            }
            graph.setMarkerStyle(style);
            canvas.cd(pad).draw(graph, "samePLS");
        }
        
        canvas.getRegion(pad).getGraphicsAxis().setAxisLimits(0.0,6.0,0.0,45);
        canvas.getRegion(pad).addNode(legend);
    }
    
    public static void main(String[] args){
        
        GRootColorPalette.getInstance().setColorPalette();
        //GRootColorPalette.getInstance().setColorScheme("tab10");
        GRootColorPalette.getInstance().setColorScheme("gold10");
        
        Groot4Demo.demo1();
        //Groot4Demo.demo2();
        //Groot4Demo.demo3();
        //Groot4Demo.demo4();
        
        TDataCanvas c = new TDataCanvas(600,400);
        c.divide(1, 1);
        Groot4Demo.demo5(c.getDataCanvas(), 0,false);
        
        c.repaint();
    }
}

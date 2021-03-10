/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class TDataCanvas extends JFrame implements ActionListener {
    
    private int CANVAS_DEFAULT_WIDTH  = 600;
    private int CANVAS_DEFAULT_HEIGHT = 400;
    
    private JPanel     canvasPane = null;
    private DataCanvas dataCanvas = null;
    private StatusPane statusPane = null;
    private String     dataCanvasTitle = "c1";
    
    public TDataCanvas(){
        initUI();
    }
    
    public TDataCanvas(int xsize, int ysize){
        this.CANVAS_DEFAULT_WIDTH = xsize;
        this.CANVAS_DEFAULT_HEIGHT = ysize;
        initUI();
    }
    
    public TDataCanvas(String title){
        this.dataCanvasTitle = title;
        initUI();        
    }
    
    public TDataCanvas(String title, int width, int height){
        this.CANVAS_DEFAULT_WIDTH  = width; this.CANVAS_DEFAULT_HEIGHT = height;
        initUI();
    }
    
    public TDataCanvas setMargins(int left, int top, int right, int bottom){
        getDataCanvas().left(left).top(top).right(right).bottom(bottom);
        return this;
    }
    
    public TDataCanvas setAxisFontSize(int labelSize, int titleSize){
        getDataCanvas().setAxisFontSize(labelSize).setAxisTitleFontSize(titleSize);
        return this;
    }
    
    private void initUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = this.createMenuBar();
        setJMenuBar(menuBar);
        
        canvasPane = new JPanel();
        canvasPane.setLayout(new BorderLayout());
        dataCanvas = new DataCanvas();
        dataCanvas.setSize(this.CANVAS_DEFAULT_WIDTH, this.CANVAS_DEFAULT_HEIGHT);
        dataCanvas.divide(1, 1);
        statusPane = new StatusPane();
        canvasPane.add(dataCanvas,BorderLayout.CENTER);
        canvasPane.add(statusPane,BorderLayout.PAGE_END);
        
        this.add(canvasPane);
        
        setSize(this.CANVAS_DEFAULT_WIDTH, this.CANVAS_DEFAULT_HEIGHT);
        
        this.pack();
        setSize(this.CANVAS_DEFAULT_WIDTH, this.CANVAS_DEFAULT_HEIGHT);
        this.setVisible(true);
    }
    
    public TDataCanvas cd(int pad){
        getDataCanvas().cd(pad); return this;
    }
    
    public TDataCanvas draw(IDataSet ds){
        getDataCanvas().draw(ds); return this;
    }
    
    public TDataCanvas draw(IDataSet ds, String options){
        getDataCanvas().draw(ds,options); return this;
    }
    
    public TDataCanvas divide(int x, int y){
        getDataCanvas().divide(x, y); return this;
    }
    
     public TDataCanvas divide(double[][] pads){
        getDataCanvas().divide(pads); return this;
    }
     
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenu saveMenu = new JMenu("Save...");
        
        JMenuItem savePDF = new JMenuItem("Export PDF");
        JMenuItem saveEPS = new JMenuItem("Export EPS");
        JMenuItem saveSVG = new JMenuItem("Export SVG");
        JMenuItem savePDFFree = new JMenuItem("Export PDF free Hep");
        saveMenu.add(savePDF);
        saveMenu.add(saveEPS);
        saveMenu.add(saveSVG);
        saveMenu.add(savePDFFree);
        savePDF.addActionListener(this);
        savePDFFree.addActionListener(this);
        saveEPS.addActionListener(this);
        saveSVG.addActionListener(this);
        fileMenu.add(saveMenu);
        menuBar.add(fileMenu);
        return menuBar;
    }
    
    public DataCanvas getDataCanvas(){
        return this.dataCanvas;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Export PDF")==0){
            this.getDataCanvas().save(this.dataCanvasTitle + ".pdf");
        }
        
        if(e.getActionCommand().compareTo("Export PDF free Hep")==0){
            this.getDataCanvas().export("test_free" + ".pdf");
        }
        
        if(e.getActionCommand().compareTo("Export EPS")==0){
            this.getDataCanvas().saveEPS(this.dataCanvasTitle + ".eps");
        }
        if(e.getActionCommand().compareTo("Export SVG")==0){
            this.getDataCanvas().saveSVG(this.dataCanvasTitle + ".svg");
        }
    }
    
    public static void main(String[] args){
        TDataCanvas c1 = new TDataCanvas("T",900,600);
        //c1.getDataCanvas().left(120).setAxisTitleFont("Avenir", 18, 0);
        c1.getDataCanvas().divide(2,1);
        c1.getDataCanvas().left(80).setAxisTitleFont("Avenir", 18, 0);        
        c1.getDataCanvas().left(80).setAxisFont("Avenir", 18, 0);
        
        H1F h1 = FunctionFactory.randomGausian(100, 0.1, 2.5, 1000000, 1.25, 0.3);
        HistogramNode1D node = new HistogramNode1D(h1);
        
        H1F h11 = FunctionFactory.randomGausian(100, 0.0, 2.5, 1000000, 0.5, 0.5);
        HistogramNode1D node11 = new HistogramNode1D(h11);
        
        /*H2F h2 = new H2F("h2",2,0.0,1.0,2,0.0,1.0);
        h2.setBinContent(0, 0, 0.8);
        h2.setBinContent(1, 1, 0.9);
        h2.setBinContent(1, 0, 0.5);
        h2.setBinContent(0, 1, 0.4);
     */
        
        
        h1.setLineColor(3);
        h1.setLineWidth(1);
        h1.setFillColor(73);
        
        h11.setLineColor(2);
        h11.setLineWidth(1);
        h11.setFillColor(72);
        
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(node2);
        PaveText text = new PaveText("trying helvetica 0.1 20.7 80",0,0);
        PaveText textb = new PaveText("Bold Helvetica 0.1 20.7 80",0,120);
        textb.setFont(new Font("Times",Font.PLAIN,18));
        //for(int i = 0; i < 6; i++){
        F1D function = new F1D("f1","x/sqrt(x*x+[m]*[m])",0.05,1.5);
        function.setParameter(0, 0.938);
        function.setLineColor(2);
       // c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(new HistogramNode2D(h2));
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(new FunctionNode1D(function));
        
        //c1.getDataCanvas().getRegion(1).getGraphicsAxis().addNode(new HistogramNode1D(h1));
        //c1.getDataCanvas().getRegion(1).addNode(text);
        //c1.getDataCanvas().getRegion(1).addNode(textb);
        //}
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(node);
        //c1.getDataCanvas().getRegion(1).getGraphicsAxis().addNode(node2);
        c1.getDataCanvas().repaint();
        GraphErrors graph = new GraphErrors();
        GraphErrors graph2 = new GraphErrors();
        GraphErrors graph3 = new GraphErrors();
        
        graph.addPoint(1, 0.3, 0.0, 0.1);
        graph.addPoint(2, 0.5, 0.0, 0.2);
        graph.addPoint(3, 0.7, 0.0, 0.3);
        
        graph2.addPoint(3.1, 0.3, 0.0, 0.1);
        graph2.addPoint(4.1, 0.5, 0.0, 0.2);
        graph2.addPoint(5.1, 0.7, 0.0, 0.3);
        
        graph3.addPoint(2.2, 1.2, 0.0, 0.1);
        graph3.addPoint(5.2, 0.9, 0.0, 0.2);
        graph3.addPoint(8.2, 0.8, 0.0, 0.3);
        graph.setTitleX("P [GeV] & #chi^2");
        graph.setTitleY("Reconstruction Efficiency");
        //graph.readFile("demo_graph.data");
        System.out.println("size = " + graph.getDataSize(0));
        graph.setLineColor(1);
        graph.setMarkerColor(4);
        graph.setLineThickness(1);
        graph.setMarkerSize(15);
        graph.setMarkerStyle(4);
        
        graph2.setLineColor(1);
        graph2.setMarkerColor(2);
        graph2.setLineThickness(1);
        graph2.setMarkerSize(15);
        graph2.setMarkerStyle(3);
        
        graph3.setLineColor(3);
        graph3.setMarkerColor(3);
        graph3.setLineThickness(1);
        graph3.setMarkerSize(12);
        graph3.setMarkerStyle(2);
        
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().setAxisLimits(0.5,9.5,0.0,1.4005);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addDataNode(new GraphNode2D(graph));
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addDataNode(new GraphNode2D(graph2,"PLS"));
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addDataNode(new GraphNode2D(graph3,"PLS"));
        
        PaveText text2 = new PaveText("reconstructed efficiency",500,300);
        LegendNode2D  legend = new LegendNode2D(300,350);
        legend.add(graph, "ML tracking efficiency");
        legend.add(graph2, "negative tracking efficiency");
        legend.add(graph3, "positive tracking efficiency");
        c1.getDataCanvas().getRegion(0).addNode(legend);
        //c1.getDataCanvas().repaint();
        
        
        H2F h2 = FunctionFactory.randomGausian2D(200, 0.0, 2.5, 10000000, 1.25, 0.55);
        HistogramNode2D node2 = new HistogramNode2D(h2);
        
        StatisticsNode2D stats = new StatisticsNode2D(0,0);
        
        stats.add("name", "H100");
        stats.add("mean", "1.2456");
        stats.add("rms", "0.3458");

        
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().setAxisLimits(0.0,2.5,0.0,12500);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().addDataNode(node);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().addDataNode(node11);
        c1.getDataCanvas().getRegion(1).addNode(stats);
    }
}

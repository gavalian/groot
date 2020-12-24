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
    
    public TDataCanvas(String title){
        this.dataCanvasTitle = title;
        initUI();        
    }
    
    public TDataCanvas(String title, int width, int height){
        this.CANVAS_DEFAULT_WIDTH  = width; this.CANVAS_DEFAULT_HEIGHT = height;
        initUI();
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
    
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenu saveMenu = new JMenu("Save...");
        
        JMenuItem savePDF = new JMenuItem("Export PDF");
        JMenuItem saveEPS = new JMenuItem("Export EPS");
        JMenuItem saveSVG = new JMenuItem("Export SVG");
        saveMenu.add(savePDF);
        saveMenu.add(saveEPS);
        saveMenu.add(saveSVG);
        savePDF.addActionListener(this);
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
        c1.getDataCanvas().left(80).setAxisFont("Helvetica", 18, 0);
        H1F h1 = FunctionFactory.randomGausian(100, 0.1, 2.5, 1000000, 1.25, 0.3);
        HistogramNode1D node = new HistogramNode1D(h1);
        
        /*H2F h2 = new H2F("h2",2,0.0,1.0,2,0.0,1.0);
        h2.setBinContent(0, 0, 0.8);
        h2.setBinContent(1, 1, 0.9);
        h2.setBinContent(1, 0, 0.5);
        h2.setBinContent(0, 1, 0.4);
     */
        H2F h2 = FunctionFactory.randomGausian2D(200, 0.0, 2.5, 10000000, 1.25, 0.55);
        HistogramNode2D node2 = new HistogramNode2D(h2);
        
        h1.setLineColor(4);
        //h1.setLineStyle(1);
        h1.setLineWidth(2);
        h1.setFillColor(53);
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(node2);
        PaveText text = new PaveText("trying helvetica 0.1 20.7 80",0,0);
        PaveText textb = new PaveText("Bold Helvetica 0.1 20.7 80",0,120);
        textb.setFont(new Font("Times",Font.PLAIN,18));
        //for(int i = 0; i < 6; i++){
        F1D function = new F1D("f1","x/sqrt(x*x+[m]*[m])",0.05,1.5);
        function.setParameter(0, 0.938);
        function.setLineColor(2);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(new HistogramNode2D(h2));
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(new FunctionNode1D(function));
        
        //c1.getDataCanvas().getRegion(1).getGraphicsAxis().addNode(new HistogramNode1D(h1));
        //c1.getDataCanvas().getRegion(1).addNode(text);
        //c1.getDataCanvas().getRegion(1).addNode(textb);
        //}
        //c1.getDataCanvas().getRegion(0).getGraphicsAxis().addNode(node);
        //c1.getDataCanvas().getRegion(1).getGraphicsAxis().addNode(node2);
        c1.getDataCanvas().repaint();
        GraphErrors graph = new GraphErrors();
        
        graph.setTitleX("P [GeV]");
        graph.setTitleY("AI Rec Efficiency");
        graph.readFile("demo_graph.data");
        System.out.println("size = " + graph.getDataSize(0));
        graph.setLineColor(1);
        graph.setMarkerColor(4);
        graph.setLineThickness(3);
        graph.setMarkerSize(8);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().setAxisLimits(0.5,9.5,0.0,1.4005);
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().addDataNode(new GraphNode2D(graph));
        
    }    
}

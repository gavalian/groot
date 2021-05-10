/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
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
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.groot.settings.GRootColorPalette;

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
        initUI(true);
    }
    
    public TDataCanvas(int xsize, int ysize){
        this.CANVAS_DEFAULT_WIDTH = xsize;
        this.CANVAS_DEFAULT_HEIGHT = ysize;
        initUI(true);
    }
    
    public TDataCanvas(int xsize, int ysize, boolean closeOnExit){
        this.CANVAS_DEFAULT_WIDTH = xsize;
        this.CANVAS_DEFAULT_HEIGHT = ysize;
        initUI(closeOnExit);
    }
    
    public TDataCanvas(String title){
        this.dataCanvasTitle = title;
        initUI(true);        
    }
    
    public TDataCanvas(String title, int width, int height){
        this.CANVAS_DEFAULT_WIDTH  = width; this.CANVAS_DEFAULT_HEIGHT = height;
        initUI(true);
    }
    
    public TDataCanvas setMargins(int left, int top, int right, int bottom){
        getDataCanvas().left(left).top(top).right(right).bottom(bottom);
        return this;
    }
    
    public TDataCanvas setAxisFontSize(int labelSize, int titleSize){
        getDataCanvas().setAxisFontSize(labelSize).setAxisTitleFontSize(titleSize);
        return this;
    }
    
    
    public void setAxisLimits(double xmin, double xmax, double ymin, double ymax){
        this.dataCanvas.setAxisLimits(xmin, xmax, ymin, ymax);
    }
    
    public void setAxisLimits(boolean automatic){
        if(automatic==true){
            this.getDataCanvas().setAxisLimits(automatic);
        }
    }
    
    private void initUI(boolean closeOnExit){
        
        if(closeOnExit==true) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
        JMenu fileHelp = new JMenu("Help");
        
        JMenu saveMenu = new JMenu("Save...");
        
        JMenuItem savePDF = new JMenuItem("Export PDF");
        JMenuItem saveEPS = new JMenuItem("Export EPS");
        JMenuItem saveSVG = new JMenuItem("Export SVG");
        JMenuItem savePDFFree = new JMenuItem("Export PDF free Hep");
        
        JMenuItem colAndLine = new JMenuItem("Colors and Lines");
        JMenuItem colAndLineDark = new JMenuItem("Colors and Lines (dark)");
        
        colAndLine.addActionListener(this);
        colAndLineDark.addActionListener(this);
        fileHelp.add(colAndLine);
        fileHelp.add(colAndLineDark);
        
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
        menuBar.add(fileHelp);
        return menuBar;
    }
    
    public DataCanvas getDataCanvas(){
        return this.dataCanvas;
    }
    
    public TDataCanvas addLegend(Legend leg){
        this.dataCanvas.addLegend(leg);
        return this;
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
        
        if(e.getActionCommand().compareTo("Colors and Lines")==0){
            TDataCanvas c = new TDataCanvas(800,600,false);
            c.divide(2, 1);
            Groot4Demo.demo6(c.getDataCanvas(), 0, false);
            Groot4Demo.demo5(c.getDataCanvas(), 1, false);
        }
        
        if(e.getActionCommand().compareTo("Colors and Lines (dark)")==0){
            TDataCanvas c = new TDataCanvas(800,600,false);
            c.divide(2, 1);
            //c.getDataCanvas().initBackground(38, 49, 55);
            c.getDataCanvas().initBackground(69, 90, 101);
            c.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "12");
            c.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "12");
            c.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "12");
            c.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "12");
            Groot4Demo.demo6(c.getDataCanvas(), 0,true);
            Groot4Demo.demo5(c.getDataCanvas(), 1,true);
        } 
    }
    

    public static void main(String[] args){
        GRootColorPalette.getInstance().setColorScheme("tab10");        
        TDataCanvas c1 = new TDataCanvas("T",900,600);
        //c1.getDataCanvas().left(120).setAxisTitleFont("Avenir", 18, 0);
        c1.getDataCanvas().divide(2,1);
        
        /*c1.getDataCanvas().initBackground(69, 90, 101);
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "0");
        c1.getDataCanvas().getRegion(0).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "0");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "0");
        c1.getDataCanvas().getRegion(1).getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISLINECOLOR, "0");
        */
    }
}

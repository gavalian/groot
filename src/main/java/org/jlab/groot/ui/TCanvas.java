/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import org.jlab.groot.data.DataLine;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H1FC;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedPad;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.math.RandomFunc;
import org.jlab.groot.studio.DataStudio;
import org.jlab.groot.studio.DataStudioFrame;

/**
 *
 * @author gavalian
 */
public class TCanvas extends JFrame implements ActionListener {
    
    JPanel  framePane = null;
    EmbeddedCanvas  canvas = null; 
    JPanel  statusPane  = null;
    JLabel  statusLabel = null;

    JMenuBar menuBar = null;
    
    public TCanvas(String name, int xsize, int ysize){
        
        super();
        this.setTitle(name);
        this.setSize(xsize, ysize);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.initMenu();
        this.setJMenuBar(menuBar);

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
    
    public EmbeddedPad getPad(){
        return this.canvas.getPad();
    }
    
    public TCanvas divide(int xsize, int ysize){
        this.canvas.divide(xsize, ysize);
        return this;
    }
    
    
    public TCanvas next(){
        this.getCanvas().next();
        return this;
    }
    
    public TCanvas cd(int pad){
        canvas.cd(pad);
        return this;
    }
    
    public TCanvas draw(DataLine line) { 
        canvas.draw(line);
        this.getCanvas().update();
        return this;
    } 
    
    public TCanvas draw(List<H1F> data, String options){
        this.getCanvas().draw(data, options);
        return this;
    }
    
    public TCanvas draw(H1FC data){
        List<H1F> list = data.getDataSets();
        draw(list.get(0));
        for(int i = 1; i < list.size(); i++){
            draw(list.get(i),"same");
        }
        return this;
    }
    
    public TCanvas draw(IDataSet ds, String options){
        this.canvas.draw(ds, options);
        this.getCanvas().update();
        return this;
    }
    
    public TCanvas draw(LatexText text){
        this.canvas.draw(text);
        this.getCanvas().update();
        return this;
    }
    
    public TCanvas draw(IDataSet ds){
        this.canvas.draw(ds);
        this.getCanvas().update();
        return this;
    }
    
    public void save(String filename){
    	this.canvas.save(filename);
    }
    
    private void initMenu(){
        menuBar = new JMenuBar();
        JMenu menuFile   = new JMenu("File");
        JMenuItem mfNew = new JMenuItem("New");
        mfNew.addActionListener(this);
        
        menuFile.add(mfNew);
        
        JMenu menuView   = new JMenu("View");
        JMenuItem mvDivide = new JMenuItem("Divide");
        mvDivide.addActionListener(this);
        menuView.add(mvDivide);
        
        JMenu menuStudio = new JMenu("Studio");
        JMenuItem msInspector = new JMenuItem("Inspector");
        msInspector.addActionListener(this);
        JMenuItem msSaveAll = new JMenuItem("Save All");
        msSaveAll.addActionListener(this);
        
        menuStudio.add(msInspector);
        menuStudio.add(msSaveAll);
        
        menuBar.add(menuFile);
        menuBar.add(menuView);
        menuBar.add(menuStudio);
    }
     @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().compareTo("New")==0){
            TCanvas c2 = new TCanvas("c2",600,600);
        }
        
        if(e.getActionCommand().compareTo("Save All")==0){
            List<EmbeddedPad> pads = this.getCanvas().getCanvasPads();
            for(EmbeddedPad p : pads){
                Map<String,IDataSet> datasets = p.getObjectMap();
                for(Map.Entry<String,IDataSet> entry : datasets.entrySet()){
                    DataStudio.getInstance().addDataSet(entry.getValue());
                }
            }
        }
        
        if(e.getActionCommand().compareTo("Inspector")==0){
            DataStudioFrame frame = new DataStudioFrame();
            frame.setCanvas(this);
            frame.setVisible(true);
        }
        
        if(e.getActionCommand().compareTo("Divide")==0){
            String[] options = new String[]{"1","2","3","4","5","6","7"};
            JComboBox columns = new JComboBox(options);
            JComboBox    rows = new JComboBox(options);
            columns.setSelectedIndex(this.getCanvas().getNColumns()-1);
            rows.setSelectedIndex(this.getCanvas().getNRows()-1);
            
            Object[] message = {
                "Columns:", columns,
                "Rows:", rows
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Divide Canvas", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String stringCOLS = (String) columns.getSelectedItem();
                String stringROWS = (String) rows.getSelectedItem();
                this.getCanvas().divide(Integer.parseInt(stringCOLS), Integer.parseInt(stringROWS));
                //System.out.println("----> Splitting " + columns.getSelectedItem() + " " + rows.getSelectedItem());
            }
        }
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
        h4.setTitleX("Random Gaussian");
        h5.setTitleX("Random Gaussian");
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

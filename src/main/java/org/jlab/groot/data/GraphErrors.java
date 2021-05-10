/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.io.TextFileReader;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.ui.PaveText;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.io.CSVReader;
import org.jlab.groot.math.StatNumber;

/**
 *
 * @author gavalian
 */
public class GraphErrors implements IDataSet {

    public static final String[] MARKERNAME = {"Circle", "Square", "Triangle", "Inverted Triangle"};

    private final DataVector dataX = new DataVector();
    private final DataVector dataY = new DataVector();
    private final DataVector dataEX = new DataVector();
    private final DataVector dataEY = new DataVector();

    private String graphName = "graphErrors";
    private DatasetAttributes graphAttr = null;

    private Func1D fitFunction = null;

    public GraphErrors() {
        initAttributes();
    }

    public GraphErrors(String name, DataVector grX, DataVector grY) {
        setName(name);
        for (int i = 0; i < grX.getSize(); i++) {
            this.addPoint(grX.getValue(i), grY.getValue(i), 0.0, 0.0);
        }
        initAttributes();
    }

    public GraphErrors(String name, DataVector grX, DataVector grY, DataVector erX, DataVector erY) {
        setName(name);
        for (int i = 0; i < grX.getSize(); i++) {
            double errX = 0.0;
            if (erX != null) errX = erX.getValue(i);
            this.addPoint(grX.getValue(i), grY.getValue(i), errX, erY.getValue(i));
        }
        initAttributes();
    }

    public GraphErrors(String name, DataVector grY) {
        setName(name);
        for (int i = 0; i < grY.getSize(); i++) {
            this.addPoint((double) (i + 1), grY.getValue(i), 0.0, 0.0);
        }
        initAttributes();
    }

    public GraphErrors(String name, double[] x, double y[], double[] ex, double[] ey) {
        setName(name);
        for (int i = 0; i < x.length; i++) {
            this.addPoint(x[i], y[i], ex[i], ey[i]);
        }
        initAttributes();
    }

    public GraphErrors(String name, double[] x, double y[], double[] ey) {
        setName(name);
        for (int i = 0; i < x.length; i++) {
            this.addPoint(x[i], y[i], 0.0, ey[i]);
        }
        initAttributes();
    }

    public GraphErrors(String name, double[] x, double y[]) {
        setName(name);
        for (int i = 0; i < x.length; i++) {
            this.addPoint(x[i], y[i], 0.0, 0.0);
        }
        initAttributes();
    }

    public GraphErrors(String name) {
        graphName = name;
        initAttributes();
    }

    public static GraphErrors createGraph(H1F h, String options, boolean supressZeros) {
        GraphErrors gr = new GraphErrors();
        for (int i = 0; i < h.getXaxis().getNBins(); i++) {
            double y = h.getBinContent(i);
            if (supressZeros == true) {
                if (y > 10e-12) {
                    if (options.contains("E") == true) {
                        gr.addPoint(h.getXaxis().getBinCenter(i),
                                h.getBinContent(i), 0.0, h.getBinError(i));
                    } else {
                        gr.addPoint(h.getXaxis().getBinCenter(i),
                                h.getBinContent(i), 0.0, 0.0);
                    }
                }
            } else {
                double xerror = h.getXaxis().getBinWidth(i);
                double yerror = h.getBinError(i);
                if (options.contains("E") == true) {
                    gr.addPoint(h.getXaxis().getBinCenter(i),
                            h.getBinContent(i), 0.0, h.getBinError(i));
                } else {
                    gr.addPoint(h.getXaxis().getBinCenter(i),
                            h.getBinContent(i), 0.0, 0.0);
                }
            }
        }
        return gr;
    }

    public static GraphErrors createGraph(H1F h, boolean supressZeros) {
        GraphErrors gr = new GraphErrors();
        for (int i = 0; i < h.getXaxis().getNBins(); i++) {
            double y = h.getBinContent(i);
            if (supressZeros == true) {
                if (y > 10e-12) {
                    gr.addPoint(h.getXaxis().getBinCenter(i),
                            h.getBinContent(i), 0.0, h.getBinError(i));
                }
            } else {
                gr.addPoint(h.getXaxis().getBinCenter(i),
                        h.getBinContent(i), 0.0, h.getBinError(i));
            }
        }
        return gr;
    }


    private void initAttributes() {
        try {
            graphAttr = GStyle.getGraphErrorsAttributes().clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Func1D getFunction() {
        return fitFunction;
    }

    public void setFunction(Func1D func) {
        fitFunction = func;
        func.getAttributes().setOptStat(this.getAttributes().getOptStat());
    }

    public final void addPoint(double x, double y, double ex, double ey) {
        dataX.add(x);
        dataY.add(y);
        dataEX.add(ex);
        dataEY.add(ey);
    }

    public void setPoint(int point, double x, double y) {
        dataX.set(point, x);
        dataY.set(point, y);
    }

    public void shiftX(double xshift){
        int npoints = this.dataX.getSize();
        for (int i = 0; i < npoints; i++){
            double xvalue = this.dataX.getValue(i);
            this.dataX.set(i, xvalue+xshift);
        }
    }
    
    public void setError(int point, double ex, double ey) {
        dataEX.set(point, ex);
        dataEY.set(point, ey);
    }

    @Override
    public void setName(String name) {
        graphName = name;
    }

    @Override
    public String getName() {
        return graphName;
    }

    @Override
    public int getDataSize(int axis) {
        return dataX.getSize();
    }

    @Override
    public DatasetAttributes getAttributes() {
        return graphAttr;
    }

    @Override
    public double getDataX(int bin) {
        return dataX.getValue(bin);
    }

    @Override
    public double getDataY(int bin) {
        return dataY.getValue(bin);
    }

    @Override
    public double getDataEX(int bin) {
        return dataEX.getValue(bin);
    }

    @Override
    public double getDataEY(int bin) {
        return dataEY.getValue(bin);
    }

    @Override
    public double getData(int xbin, int ybin) {
        return 0.0;
    }

    @Override
    public PaveText getStatBox() {
        return new PaveText(2);
    }

    public DataVector getVectorX() {
        return this.dataX;
    }

    public DataVector getVectorY() {
        return this.dataY;
    }

    public DataVector getVectorEX() {
        return this.dataEX;
    }

    public DataVector getVectorEY() {
        return this.dataEY;
    }
    
    public void setMarkerSize(int size) {
        this.graphAttr.setMarkerSize(size);
    }

    public int getMarkerSize() {
        return this.graphAttr.getMarkerSize();
    }

    public void setMarkerStyle(int style) {
        this.graphAttr.setMarkerStyle(style);
    }

    public int getMarkerStyle() {
        return this.graphAttr.getMarkerStyle();
    }

    public int getMarkerColor() {
        return this.graphAttr.getMarkerColor();
    }

    public void setMarkerColor(int color) {
        this.graphAttr.setMarkerColor(color);
    }

    public void setLineColor(int color) {
        this.graphAttr.setLineColor(color);
    }

    public int getLineColor() {
        return this.graphAttr.getLineColor();
    }

    public void setLineThickness(int thickness) {
        this.graphAttr.setLineWidth(thickness);
    }

    public int getLineThickness() {
        return this.graphAttr.getLineWidth();
    }

    public String getTitle() {
        return this.graphAttr.getTitle();
    }

    public String getTitleX() {
        return this.graphAttr.getTitleX();
    }

    public String getTitleY() {
        return this.graphAttr.getTitleY();
    }

    
    public void fit(Func1D func, String options){
        DataFitter.fit(func, this, options);
    }
    
    public void fit(Func1D func){
        this.fit(func, "");
    }
    
    /**
     * set Title of the histogram
     *
     * @param title new title
     */
    public final void setTitle(String title) {

        if (title.contains(";") == true) {
            String[] tokens = title.split(";");
            if (tokens.length > 0)
                this.getAttributes().setTitle(tokens[0]);
            if (tokens.length > 1)
                this.getAttributes().setTitleX(tokens[1]);
            if (tokens.length > 2)
                this.getAttributes().setTitleY(tokens[2]);
        } else {
            this.getAttributes().setTitle(title);
        }
    }

    public void setTitleX(String title) {
        this.graphAttr.setTitleX(title);
    }

    public void setTitleY(String title) {
        this.graphAttr.setTitleY(title);
    }


    public void copy(GraphErrors gr) {
        this.dataEX.clear();
        this.dataEY.clear();
        this.dataX.clear();
        this.dataY.clear();
        for (int i = 0; i < gr.getDataSize(0); i++) {
            this.addPoint(gr.getDataX(i), gr.getDataY(i),
                    gr.getDataEX(i), gr.getDataEY(i)
            );
        }
    }

    
    public static GraphErrors  csvGraphXY(String filename,int columnX, int columnY, int skip){
        GraphErrors graph = new GraphErrors();
        CSVReader reader = new CSVReader();
        List<double[]> data = reader.readColumn(filename, new int[]{columnX,columnY}, skip);
        for(int i = 0; i < data.size(); i++){
            graph.addPoint(data.get(i)[0], data.get(i)[1], 0.0, 0.0);
        }
        return graph;
    }
    
    public static GraphErrors  csvGraphXYEY(String filename,int columnX, int columnY, int columnEY, int skip){
        GraphErrors graph = new GraphErrors();
        CSVReader reader = new CSVReader();
        List<double[]> data = reader.readColumn(filename, new int[]{columnX,columnY,columnEY}, skip);
        for(int i = 0; i < data.size(); i++){
            graph.addPoint(data.get(i)[0], data.get(i)[1], 0.0, data.get(i)[2]);
        }
        return graph;
    }
    
    public void readFile(String filename) {
        TextFileReader reader = new TextFileReader();
        reader.openFile(filename);
        this.reset();
        while (reader.readNext() == true) {
            if (reader.getDataSize() == 2) {
                int[] index = new int[]{0, 1};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0], points[1], 0.0, 0.0);
            }
            if (reader.getDataSize() == 3) {
                int[] index = new int[]{0, 1, 2};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0], points[1], 0.0, points[2]);
            }
            if (reader.getDataSize() > 3) {
                int[] index = new int[]{0, 1, 2, 3};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0], points[1], points[2], points[3]);
            }
        }
    }

    public GraphErrors divide(double number){
        
        StatNumber denom = new StatNumber(number,0.0);
        StatNumber   nom = new StatNumber(number,0.0);
        GraphErrors gr = new GraphErrors();
        
        for(int i = 0; i < this.dataY.size(); i++){
            double value = dataY.getValue(i);
            nom.set(value, dataEY.getValue(i));
            nom.divide(denom);
            //dataY.setValue(i, nom.number());
            //dataEY.setValue(i, nom.error());
            gr.addPoint(dataX.getValue(i), nom.number(), dataEX.getValue(i), nom.error());
        }
        return gr;
    }
    public GraphErrors  divide(GraphErrors gr){
        if(this.getDataSize(0)!=gr.getDataSize(0)){
            System.out.println("[graph:divide] error , graphs have different sizes");
            return new GraphErrors();
        }
        
        GraphErrors result = new GraphErrors();
        StatNumber nom = new StatNumber();
        StatNumber denom = new StatNumber();
        
        for(int i = 0; i < this.getDataSize(0); i++){
            
            nom.set(this.getDataY(i),this.getDataEY(i));
            denom.set(gr.getDataY(i),gr.getDataEY(i));
            nom.divide(denom);
            result.addPoint(this.getDataX(i), nom.number(), 0.0, nom.error());
        }
        return result;
    }
    
    
    public void statErrors(){
        int ndata = dataX.getSize();
        for(int i = 0; i < ndata; i++){
            double ye = this.dataY.getValue(i);
            this.dataEY.set(i, Math.sqrt(Math.abs(ye)));
        }
    }
    
    public static GraphErrors readFile(String filename, int start, int npoints, int[] columns) {
        
        TextFileReader reader = new TextFileReader();
        reader.openFile(filename);
        GraphErrors gr = new GraphErrors();
        
        for(int n = 0; n < start; n++) reader.readNext();
        
        int counter = 0;
        while (reader.readNext() == true&& counter<npoints) {
            double[] data = reader.getAsDouble(columns);
            //System.out.println("COLUMNS = " + columns[0] + " " + columns[1]);
            if(columns.length==2){
                gr.addPoint(data[0], data[1], 0.0, 0.0);
                //System.out.println("adding point " + data[0] + " " + data[1]);
            }
            if (columns.length==3) {
                gr.addPoint(data[0],data[1],0.0,data[2]);
            }
            if (columns.length>3) {
                gr.addPoint(data[0],data[1],data[2],data[3]);
            }
            counter++;
        }
        
        return gr;
    }
    @Override
    public void reset() {
        this.dataX.clear();
        this.dataY.clear();
        this.dataEX.clear();
        this.dataEY.clear();
    }

    @Override
    public double getMin() {
        return dataY.getMin();
    }

    @Override
    public double getMax() {
        return dataY.getMax();
    }
    public void show(){
        System.out.println(">>> graph");
        for(int i = 0; i < dataX.getSize(); i++){
            System.out.printf("%9.5f %9.5f %9.5f %9.5f \n",
                    dataX.getValue(i),dataY.getValue(i),
                    dataEX.getValue(i),dataEY.getValue(i)
                    );
        }
    }
    public void save(String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write("#GraphErrors: " + this.graphName + ", npoints: " + getDataSize(0) + "\n");
            file.write("#x,y,xerror,yerror\n");
            for (int i = 0; i < getDataSize(0); i++) {
                file.write(String.format("%f,%f,%f,%f",
                        getDataX(i), getDataY(i), getDataEX(i), getDataEY(i)));
                file.write('\n');
            }
            file.close();
        } catch (IOException e) {
        }
    }
}

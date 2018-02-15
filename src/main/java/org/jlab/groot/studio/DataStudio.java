/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.util.HashMap;
import java.util.Map;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.tree.TreeFile;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class DataStudio {
    
    public static DataStudio studio = new DataStudio();
    
    private Map<String,DataVector>  vectorStore = new HashMap<String,DataVector>();
    private Map<String,TCanvas>     canvasStore = new HashMap<String,TCanvas>();
    private Map<Integer,TreeFile>   ntupleStore = new HashMap<Integer,TreeFile>();
    private Map<Integer,IDataSet>  datasetStore = new HashMap<Integer,IDataSet>();
    private Integer                dataSetNumbering = 100;
    public DataStudio(){
        
    }
    
    public Map<String,TCanvas> getCanvasStore()   { return this.canvasStore;}
    public Map<String,DataVector> getVectorStore(){ return this.vectorStore;}
    public Map<Integer,IDataSet> getDataSetStore(){ return this.datasetStore;}
    public Map<Integer,TreeFile> getNtupleStore() { return this.ntupleStore;}
    
    public void addDataSet(Integer id, IDataSet ds){
        if(this.datasetStore.containsKey(id)==true){
            System.out.println("** DATA STUDIO ** reaplcing data set id = " + id);
        }        
        this.datasetStore.put(id, ds);
        this.dataSetNumbering = id;
    }
    
    public void addDataSet(IDataSet ds){
        this.dataSetNumbering++;
        if(this.datasetStore.containsKey(dataSetNumbering)==true){
            System.out.println("** DATA STUDIO ** reaplcing data set id = " + dataSetNumbering);
        }        
        this.datasetStore.put(dataSetNumbering, ds);
    }
    
    public static DataStudio getInstance(){ return studio;}
}

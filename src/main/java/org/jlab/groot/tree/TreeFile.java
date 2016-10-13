/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.DataVector;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoNode;
import org.jlab.hipo.data.HipoNodeType;
import org.jlab.hipo.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class TreeFile extends Tree {

    List<DataVector>  dataVectors  = new ArrayList<DataVector>();
    HipoWriter             writer  = new HipoWriter();
    private  int     autoSaveCount = 300;
    
    public TreeFile(String name) {
        super(name);
    }
    
    public TreeFile(String name, String format) {
        super(name);
    }
    
    public  void openFile(String filename){
        this.writer.open(filename);
    }
    
    public void close(){
        this.writer.close();
    }
    
    private void parseFormat(String format){
        String[] tokens = format.split(":");
        getBranches().clear();
        for(int i = 0; i < tokens.length; i++){
            addBranch(tokens[i],"","");
        }
    }
    
    public void addRow(double[] row){
        DataVector vec = new DataVector();
        for(int i = 0; i < row.length; i++) vec.add(row[i]);
    }
    
    private void checkToAutoSave(){
        if(dataVectors.size()>=this.autoSaveCount){
           this.flushData();
        }
    }
    
    private void flushData(){
        if(dataVectors.size()>0){
             int nbranches = getBranches().size();
            int nvecs     = dataVectors.size();
            int bufferLength = nbranches*nvecs;
            HipoNode  node = new HipoNode(400,1,HipoNodeType.FLOAT,bufferLength);
            int offset = 0;
            for(int row = 0; row < nvecs; row++){
                for(int col = 0; col < nbranches; col++){
                    node.setFloat(offset, (float) dataVectors.get(row).getValue(col));
                    offset++;
                }
            }
            HipoEvent event = new HipoEvent();
            event.addNode(node);
            writer.writeEvent(event.getDataBuffer());
            dataVectors.clear();
        }
    }
}

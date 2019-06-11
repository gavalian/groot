/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.jlab.coda.hipo.Record;
import org.jlab.coda.hipo.RecordInputStream;
import org.jlab.coda.hipo.RecordOutputStream;
import org.jlab.groot.data.DataVector;
import org.jlab.jnp.hipo.data.HipoEvent;
import org.jlab.jnp.hipo.data.HipoNode;
import org.jlab.jnp.hipo.data.HipoNodeType;
import org.jlab.jnp.hipo.io.HipoReader;
import org.jlab.jnp.hipo.io.HipoWriter;


/**
 *
 * @author gavalian
 */
public class TreeFile extends Tree {

    public static final int WRITE_MODE = 1;
    public static final int  READ_MODE = 2;
    
    List<DataVector>  dataVectors  = new ArrayList<DataVector>();
    HipoWriter             writer  = null;
    HipoReader             reader  = null;
    private  int     autoSaveCount = 300;
    private  int     TREE_MODE     = TreeFile.READ_MODE;
    
    //HipoRecord       headerRecord  = new HipoRecord();
    
    public TreeFile(String filename) {
        super("UNDEFINED");
        TREE_MODE = TreeFile.READ_MODE;
        this.openFileForReading(filename);
    }
    
    public TreeFile(String filename,String name, String format) {
        super(name);
        this.parseFormat(format);
        this.TREE_MODE = TreeFile.WRITE_MODE;
        this.openFileForWriting(filename);
    }

    private void openFileForReading(String filename){
        reader = new HipoReader();
        reader.open(filename);
        
        RecordInputStream record = reader.getUserHeaderRecord();
        int entries = record.getEntries();
        System.out.println(" Entries = " + entries);
        if(entries>0){
            byte[] userEvent = record.getEvent(0);
            HipoEvent event = new HipoEvent(userEvent);
            if(event.hasNode(32000, 1)==true){
                System.out.println("Description is there ");
                HipoNode node = event.getNode(32000, 1);
                String description = node.getString();
                System.out.println(" FORMAT : " + description);
                String[] tokens = description.split(":");
                for(int i = 1; i < tokens.length; i++){
                    this.addBranch(tokens[i], "", "");
                }
            }
        }
    }
    
    @Override
    public int readEntry(int entry){
        if(this.TREE_MODE==TreeFile.READ_MODE){
            HipoEvent event = reader.readEvent(entry);
            HipoNode  node  = event.getNode(32001, 1);
            if(node.getType()==HipoNodeType.FLOAT){
                float[] data = node.getFloat();
                this.setBranchData(data);
            }
        }
        return 1;
    }
    
    private void openFileForWriting(String filename){
        this.writer = new HipoWriter();        
        writer.setCompressionType(2);
        List<String> tokens = this.getListOfBranches();
        
        StringBuilder str = new StringBuilder();
        //str.append(this.getName());
        str.append(getName());
        for(String item : tokens){
            str.append(":");
            str.append(item);
        }
        
        System.out.println(" FORMAT : " + str.toString());
        HipoNode  branchNode = new HipoNode(32000,1,str.toString());
        RecordOutputStream recDictionary = new RecordOutputStream();
        HipoEvent event = new HipoEvent();
        event.addNode(branchNode);
        recDictionary.addEvent(event.getDataBuffer());
        recDictionary.build();
        ByteBuffer buffer = recDictionary.getBinaryBuffer();
        System.out.println("record size = " + buffer.array().length + " limit = " + buffer.limit());
        int size = buffer.limit();
        int sizeWords = buffer.getInt(0);
        //System.out.println(" The encoded bytes = " + buffer.limit() + " size = " + size 
        //        + "  words = " + sizeWords);
        byte[] userHeader = new byte[sizeWords*4];
        System.arraycopy(buffer.array(), 0, userHeader, 0, userHeader.length);
        writer.open(filename, userHeader);
    }
        
    public  void openFile(String filename){
        this.writer.open(filename);
        
        List<String> tokens = this.getListOfBranches();
        
        StringBuilder str = new StringBuilder();
        str.append(getName());
        for(String item : tokens){
            str.append(":");
            str.append(item);
        }
/*        
        HipoNode   node = new HipoNode(200,1,str.toString());
        HipoEvent event = new HipoEvent();
        event.addNode(node);
        headerRecord.addEvent(event.getDataBuffer());
        
        writer = new HipoWriter();
        writer.addHeader(str.toString());
        
  */      
    }
    
    public void close(){
        if(TREE_MODE==TreeFile.WRITE_MODE){
            this.writer.close();
        }
    }
    
    private void parseFormat(String format){
        String[] tokens = format.split(":");
        getBranches().clear();
        for(int i = 0; i < tokens.length; i++){
            addBranch(tokens[i],"","");
        }
    }
    
    public void addRow(float[] row){
        if(TREE_MODE==TreeFile.WRITE_MODE){
            HipoEvent event = writer.createEvent();
            HipoNode  node  = new HipoNode(32001,1,row);
            event.addNode(node);
            writer.writeEvent(event);
        } else {
            System.out.println("addRow: ** error ** : tree is opened in read mode.");
        }
    }
    
    public void addRow(double[] row){
        if(TREE_MODE==TreeFile.WRITE_MODE){
            HipoEvent event = writer.createEvent();
            HipoNode  node  = new HipoNode(32001,1,row);
            event.addNode(node);
            writer.writeEvent(event);
        } else {
            System.out.println("addRow: ** error ** : tree is opened in read mode.");
        }
        //DataVector vec = new DataVector();
        //for(int i = 0; i < row.length; i++) vec.add(row[i]);
    }
    
    @Override
    public int getEntries(){
        return reader.getEventCount();
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
            writer.writeEvent(event);
            dataVectors.clear();
        }
    }
    
    
    public static void main(String[] args){
        TreeFile tree = new TreeFile("test.hipo","T","a:b:c:d:f");
        double[]  row = new double[5];
        float[]  rowf = new float[5];
        
        for(int i = 0; i < 4800000; i++){
            for(int k = 0; k < row.length; k++) rowf[k] = (float) (Math.random()*10.0);
            tree.addRow(rowf);
        }
        tree.close();
        
        TreeFile treeIn = new TreeFile("test.hipo");
        long st = System.currentTimeMillis();
        int entries = treeIn.getEntries();
        System.out.println(" ENTRIES = " + entries);
        for(int i = 0; i < entries; i++){
            treeIn.readEntry(i);
            //treeIn.print();
        }
        long et = System.currentTimeMillis();
        long time = et-st;
        System.out.println(" time = " + (time/1000.0));
        
        DataVector vec = treeIn.getDataVector("a", "a>0&&a<10.0");
        System.out.println("SIZE = " + vec.size() + "  MAX = " + vec.getMax());        
    }
}

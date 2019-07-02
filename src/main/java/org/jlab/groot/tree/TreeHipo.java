/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.List;
import org.jlab.groot.data.DataVector;
import org.jlab.jnp.hipo4.io.HipoReader;
import org.jlab.jnp.hipo4.data.Bank;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Schema;

/**
 *
 * @author gavalian
 */
public class TreeHipo extends Tree {
    
    private Schema      treeSchema = null;
    private HipoReader  treeReader = null;
    private Bank        treeBank   = null;
    private int         currentRow = -1;
    private Event        treeEvent = new Event();
    
    public TreeHipo(Schema bankSchema){
        super("HIPO");
        initSchema(bankSchema);
    }
    
    public TreeHipo(String filename, String bankname){
        super("HIPO");
        treeReader = new HipoReader();
        treeReader.open(filename);
        initSchema(treeReader.getSchemaFactory().getSchema(bankname));
    }
    
    private void initSchema(Schema schema){
        treeSchema = schema.getCopy();
        List<String> list = treeSchema.getEntryList();
        this.addBranch("i", "", "");
        for(String item : list){
            this.addBranch(item, "", "");
        }
        treeBank = new Bank(treeSchema);
    }
    
    public TreeHipo setReader(HipoReader reader){
        treeReader = reader;
        return this;
    }
    
    @Override
    public int getEntries(){
        return treeReader.getEventCount();
    }
    
    @Override
    public boolean readNext(){
        
        if(currentRow<0){
            //ystem.out.println(" read entry " + i);
            if(treeReader.hasNext()==false) return false;
            treeReader.nextEvent(treeEvent);
            treeEvent.read(treeBank);
            currentRow = treeBank.getRows() - 1;
            while(currentRow<0){
                if(treeReader.hasNext()==false) return false;
                treeReader.nextEvent(treeEvent);
                treeEvent.read(treeBank);
                currentRow = treeBank.getRows() - 1;
            }
        }
        //System.out.println(" row = " + currentRow);
        getBranch("i").setValue(currentRow);
        int nBranches = treeSchema.getElements();
        for(int b = 0; b < nBranches; b++){
            int     type = treeSchema.getType(b);
            String  name = treeSchema.getElementName(b);
            if(type==1||type==2||type==3){
                getBranch(name).setValue(treeBank.getInt(name, currentRow));
            }
            if(type==4){
                getBranch(name).setValue(treeBank.getFloat(name, currentRow));
            }
            if(type==5){
                getBranch(name).setValue(treeBank.getDouble(name, currentRow));
            }
            if(type==8){
                getBranch(name).setValue(treeBank.getLong(name, currentRow));
            }
        }
        currentRow--;
        return true;
    }
    
    @Override
    public void reset(){
        currentRow = -1;
        treeReader.getEvent(treeEvent,0);
    }
    
    @Override
    public int readEntry(int i){
        
        //System.out.println(" reading event " + i + "  current row = " + currentRow);
        if(currentRow<0){
            System.out.println(" read entry " + i);
            if(i >= getEntries()) return -1;
            while(currentRow<=0){
                System.out.println(" read event " + i);
                treeReader.getEvent(treeEvent, i);
                treeEvent.read(treeBank);
                currentRow = treeBank.getRows() - 1;
            }
        }
        int nBranches = treeSchema.getElements();
        for(int b = 0; b < nBranches; b++){
            int     type = treeSchema.getType(b);
            String  name = treeSchema.getElementName(b);
            if(type==1||type==2||type==3){
                getBranch(name).setValue(treeBank.getInt(name, currentRow));
            }
            if(type==4){
                getBranch(name).setValue(treeBank.getFloat(name, currentRow));
            }
            if(type==5){
                getBranch(name).setValue(treeBank.getDouble(name, currentRow));
            }
            if(type==8){
                getBranch(name).setValue(treeBank.getLong(name, currentRow));
            }
        }
        currentRow--;
        return 1;
    }
    
    public static void main(String[] args){
        //String filename = "/Users/gavalian/Work/DataSpace/clas12/4013/out_clas_004013_FILTERED.hipo";
        String filename = "/Users/gavalian/Work/DataSpace/clas12/decoded/clas_004013.evio.00000-00009.hipo";
        HipoReader reader = new HipoReader();
        reader.open(filename);
        TreeHipo tree = new TreeHipo(reader.getSchemaFactory().getSchema("ECAL::adc"));
        tree.setReader(reader);
        
        //tree.scanTree("pid:px", "pid>12", 40000, false);
        
        //List<DataVector> vec = tree.getScanResults();
        List<DataVector> vec2 = tree.getDataVectors("time", "", 500);
        //System.out.println(vec.toString() + "  size = " + vec.get(1).getSize() + " vectors = " + vec.size());
        System.out.println(vec2.get(0).getSize());
        /*
        for(int i = 0; i < 200; i++){
            tree.readEntry(i);
            //tree.show();
            System.out.println(tree.getBranch("pid").getValue());
        }*/
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import org.jlab.jnp.hipo4.data.Schema;
import org.jlab.jnp.hipo4.data.Bank;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class TreeFileWriter {
    private int treeSchemaGroup = 2200;
    private int treeSchemaItem  = 1;
    private int maxTupleRows    = 120;
    private int currentTupleRow = 0;
    private Bank tupleBank      = null;
    private int tupleBankRows      = 0;
    private HipoWriter  writer = null;
    private Event event = new Event();
    
    public TreeFileWriter(String filename, String name, String format){
        Schema schema = parseFormat(name,format);
        writer = new HipoWriter();
        writer.getSchemaFactory().addSchema(schema);
        writer.open(filename);
        tupleBank = new Bank(schema,maxTupleRows);
        tupleBankRows = schema.getElements();
        currentTupleRow = 0;
    }
    
    public void addRow(float[] row){
        if(tupleBankRows!=row.length){
            System.out.println("ERROR : rows size = " + row.length + " entries = " + tupleBankRows);
            return;
        }
        
        for(int i = 0; i < row.length; i++){
            tupleBank.putFloat(i, currentTupleRow, row[i]);
        }
        currentTupleRow++;
        
        if(currentTupleRow>=maxTupleRows){
            event.reset();
            event.write(tupleBank);
            writer.addEvent(event);
            currentTupleRow = 0;
        }
    }
    
    public void close(){
        if(currentTupleRow>0){
            Bank bank = new Bank(tupleBank.getSchema(),currentTupleRow);
            int entries = tupleBank.getSchema().getElements();
            for(int i = 0; i < currentTupleRow; i++){
                for(int e = 0; e < entries; e++){
                    bank.putFloat(e, i, tupleBank.getFloat(e, i));
                }
            }
            event.reset();
            event.write(bank);
            writer.addEvent(event);
        }
        writer.close();
    }
    
    protected Schema parseFormat(String name, String format){
        String[] tokens = format.split(":");
        Schema.SchemaBuilder schema = new Schema.SchemaBuilder(name+"::tree",treeSchemaGroup,treeSchemaItem);
        for(int i = 0; i < tokens.length; i++)
            schema.addEntry(tokens[i], "F", "");
        return schema.build();
    }
    
}

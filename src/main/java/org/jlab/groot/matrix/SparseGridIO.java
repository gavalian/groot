/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoNode;
import org.jlab.hipo.data.HipoNodeBuilder;
import org.jlab.hipo.data.HipoNodeType;
import org.jlab.hipo.io.HipoReader;
import org.jlab.hipo.io.HipoRecord;
import org.jlab.hipo.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class SparseGridIO {
    
    public static SparseVectorGrid importHipo(String filename){
        HipoReader      reader = new HipoReader();
        
        reader.open(filename);
        HipoRecord headerRecord = reader.getHeaderRecord();
        
        byte[] eventArray = headerRecord.getEvent(0);
        HipoEvent headerEvent = new HipoEvent(eventArray);
        
        System.out.println(headerEvent.toString());
        HipoNode index = headerEvent.getNode(100, 1);
        HipoNode vsize = headerEvent.getNode(100, 2);
        
        int[] sparseIndex = new int[index.getDataSize()];
        for(int i = 0; i < index.getDataSize();i++) sparseIndex[i] = index.getInt(i);
        SparseVectorGrid  grid = new SparseVectorGrid(vsize.getInt(0),sparseIndex);
        
        int vecSize = vsize.getInt(0);
        
        int nentries = reader.getEventCount();
        for(int loop = 0; loop < nentries; loop++){
            byte[] entry = reader.readEvent(loop);
            HipoEvent event = new HipoEvent(entry);
            HipoNode indexNode = event.getNode(100,1);
            HipoNode  dataNode = event.getNode(100,2);
            for(int i = 0; i < indexNode.getDataSize(); i++){
                Long key = indexNode.getLong(i);
                DataVector vec = new DataVector();
                for(int k = 0; k < vecSize; k++){
                    vec.add(dataNode.getDouble(i*vecSize+k));
                }
                grid.binMap.put(key, vec);
            }
        }
        return grid;
    }
    
    public static void exportHipo(SparseVectorGrid grid, String filename){
        
        int  entriesPerNode = 300;
        int      vectorSize = grid.getVectorSize();
        
        HipoWriter writer = new HipoWriter();
        
        HipoEvent headerEvent = new HipoEvent();
        
        HipoNode  index = new HipoNode(100,1,HipoNodeType.INT,grid.getIndexer().getRank());
        HipoNode  vsize = new HipoNode(100,2,HipoNodeType.INT,1);
        
        vsize.setInt(0, grid.getVectorSize());
        
        for(int i = 0; i < index.getDataSize(); i++){
            index.setInt(i, grid.getIndexer().getBinsPerAxis()[i]);
        }
        
        headerEvent.addNode(index);
        headerEvent.addNode(vsize);
        HipoRecord headerRecord = new HipoRecord();
        headerRecord.addEvent(headerEvent.getDataBuffer());
        
        writer.open(filename,headerRecord.build().array());
        
        writer.setCompressionType(1);
        HipoNodeBuilder<Long> nodeIndex   = new HipoNodeBuilder<Long>(300);
        HipoNodeBuilder<Double> nodeData  = new HipoNodeBuilder<Double>(vectorSize*entriesPerNode);
        
        for(Map.Entry<Long,DataVector> vector : grid.getGrid().entrySet()){
            
            if(nodeIndex.isFull()==true){
                //System.out.println("----> flashing the event");
                HipoEvent event = new HipoEvent();
                HipoNode ni = nodeIndex.buildNode(100, 1);
                HipoNode nd = nodeData.buildNode(100, 2);
                event.addNode(ni);
                event.addNode(nd);
                writer.writeEvent(event.getDataBuffer());
                nodeIndex.reset();
                nodeData.reset();                
            }
            
            nodeIndex.push(vector.getKey());
            for(int i = 0; i < vectorSize; i++) nodeData.push(vector.getValue().getValue(i));
        }

        HipoEvent event = new HipoEvent();
        HipoNode ni = nodeIndex.buildNode(100, 1);
        HipoNode nd = nodeData.buildNode(100, 2);
        System.out.println("Writing trailing nodes with size = " + ni.getDataSize());
        event.addNode(ni);
        event.addNode(nd);
        writer.writeEvent(event.getDataBuffer());
        
        writer.close();
    }
    
    public static SparseVectorGrid importText(int[] lengths, String filename, int[] columns){

        SparseVectorGrid  grid = new SparseVectorGrid(columns.length,lengths);
        
        String line = null;
        int[] index = new int[lengths.length];
        int[] row   = new int[columns.length];
        
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =  new FileReader(filename);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =  new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                //lines.add(line);
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < lengths.length;i++) index[i] = Integer.parseInt(tokens[i]);
                
                for(int i = 0; i < columns.length; i++){
                    double value = Double.parseDouble(tokens[columns[i]]);
                    grid.addBinContent(i, value, index);
                }
            }   
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
           ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return grid;
    }
    
    public static void main(String[] args){
        int[] bins = new int[]{5,2,18,20,36};
        SparseVectorGrid grid = SparseGridIO.importText(bins, 
                "/Users/gavalian/Work/Software/Release-9.0/Distribution/datasets/pim_datatables_5D.txt", 
                new int[]{11,12,13});
        grid.show();
        
        SparseGridIO.exportHipo(grid, "grid.hipo");
        
        SparseVectorGrid gridIn = SparseGridIO.importHipo( "grid.hipo");

        H1F h = grid.projection(4, 0);
        TCanvas c1 = new TCanvas("grid",500,500);
        c1.draw(h);
        System.out.println("Comparison Output");
        grid.show();
        System.out.println("Comparison Input");
        gridIn.show();
    }
}

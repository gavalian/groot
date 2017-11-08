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
import org.jlab.groot.io.ArgumentParser;
import org.jlab.groot.io.TextFileReader;
import org.jlab.groot.ui.TCanvas;
import org.jlab.jnp.hipo.io.HipoReader;


/**
 *
 * @author gavalian
 */
public class SparseGridIO {
    
    public static SparseVectorGrid importHipo(String filename){
        /*
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
        return grid;*/
        return null;
    }
    
    public static void exportHipo(SparseVectorGrid grid, String filename){
        /*
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
        
        if(nodeIndex.getSize()!=0){
            HipoEvent event = new HipoEvent();
            HipoNode ni = nodeIndex.buildNode(100, 1);
            HipoNode nd = nodeData.buildNode(100, 2);
            System.out.println("Writing trailing nodes with size = " + ni.getDataSize());
            event.addNode(ni);
            event.addNode(nd);
            writer.writeEvent(event.getDataBuffer());
        }
        
        writer.close();
    }
    
    public static void importFileToGrid(String filename, SparseVectorGrid grid, int order){
        TextFileReader reader = new TextFileReader();
        reader.openFile(filename);
        int indexRank = grid.getIndexer().getRank();
        
        while(reader.readNext()){
          double[] values = reader.getAsDouble(0,indexRank-1);
          grid.fill(values, order);
        }*/
    }
    
    public static SparseVectorGrid createGrid(String[] names, int[] bins,
            double[] axisMin, double[] axisMax, int vecsize){
        
        SparseGridBuilder builder = new SparseGridBuilder(vecsize);
        for(int i = 0; i < names.length; i++){
            builder.axis(names[i], bins[i],axisMin[i],axisMax[i]);
        }
        SparseVectorGrid  grid = builder.build();
        return grid;
    }
    
    public static SparseVectorGrid createGrid(String names, String bins, 
            String limits,int vecsize){
        
        String[]  tokens     = names.split(":");
        int[]     arr_bins   = SparseGridIO.getVectorInt(bins);
        double[]  arr_limits = SparseGridIO.getVectorDouble(limits);
        if(tokens.length!=arr_bins.length){
            System.out.println("[createGrid] error : number of names is not consistent with number of bins");
            return null;
        }
        
        if( (2*arr_bins.length)!=arr_limits.length){
            System.out.println("[createGrid] error : number of bins is not consistent with number of limits");
            return null;
        }
        
        double[] axisMin = new double[arr_bins.length];
        double[] axisMax = new double[arr_bins.length];
        for(int i = 0; i < arr_bins.length; i++){
            axisMin[i] = arr_limits[i*2];
            axisMax[i] = arr_limits[i*2+1];
        }
        return SparseGridIO.createGrid(tokens, arr_bins, axisMin, axisMax, vecsize);
    }
    
    
    
    public static SparseVectorGrid inportTextFill(String[] names, int[] bins, 
            double[] axisMin, double[] axisMax, String filename, int vsize){
        
        SparseGridBuilder builder = new SparseGridBuilder(vsize);
        for(int i = 0; i < names.length; i++){
            builder.axis(names[i], bins[i],axisMin[i],axisMax[i]);
        }
        
        SparseVectorGrid grid = builder.build();
        
        //SparseGridIO.importFileToGrid(filename, grid, 0);
        
        return grid;
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
    
    public static void printUsage(){
        System.out.println("grid -create -file file.txt -index \"2:12:24\" -vec \"4,6,8\" -out file.hipo");
    }
    
    public static int[] getVectorInt(String items){
        String[] tokens = items.split(":");
        int[] vec = new int[tokens.length];
        for(int i = 0; i < tokens.length;i++) vec[i] = Integer.parseInt(tokens[i]);
        return vec;
    }
    
    public static double[] getVectorDouble(String items){
        String[] tokens = items.split(":");
        double[] vec = new double[tokens.length];
        for(int i = 0; i < tokens.length;i++) vec[i] = Double.parseDouble(tokens[i]);
        return vec;
    }
    
    public static void main(String[] args){
        
        ArgumentParser parser = new ArgumentParser();
        
        parser.addCommand("-create");        
        parser.addCommand("-fill");
        parser.addCommand("-insert");
        parser.addCommand("-show");
        //parser.addCommand("-show");
        
        parser.getCommand("-show").addRequiredParameter("-f", "Grid file name to display");
        
        parser.getCommand("-create").addRequiredParameter("-o", "Output file to save the grid");
        parser.getCommand("-create").addRequiredParameter("-d", 
                "Dimension names as a string list separated by \":\"");
        parser.getCommand("-create").addRequiredParameter("-b", 
                "bins for each dimension integer separated by \":\"");
        parser.getCommand("-create").addRequiredParameter("-a", 
                "axis definitions min and max double numbers separated by \":\"");
        
        parser.getCommand("-create").addOptionalParameter("-v", "1",
                "size of the vector for each bin ");        
        
        parser.getCommand("-fill").addRequiredParameter("-grid", "grid file to fill from text file");
        parser.getCommand("-fill").addRequiredParameter("-o", "output file name to save the grid");
        parser.getCommand("-fill").addRequiredParameter("-c", "column of the grid to fill");
        parser.getCommand("-fill").addRequiredParameter("-i", "input text file");
        parser.getCommand("-fill").addOptionalParameter("-s", "space",
                "separator of the columns in the text file");
        
        
        parser.getCommand("-insert").addRequiredParameter("-i", "input text file");
        parser.getCommand("-insert").addRequiredParameter("-b", 
                "bins for each dimension integer separated by \":\"");
        parser.getCommand("-insert").addRequiredParameter("-a", 
                "axis definitions min and max double numbers separated by \":\"");
        parser.getCommand("-insert").addRequiredParameter("-c", 
                "columns numbers for vector content \":\"");
        parser.getCommand("-insert").addRequiredParameter("-o", 
                "output grid file name");
        
        
        //parser.show();

        parser.parse(args);
        //parser.getCommand().show();
        
        if(parser.getCommand().getCommand().compareTo("-create")==0){
            if(parser.getCommand().containsRequired()==false){
                parser.getCommand().explainMissing();
                parser.getCommand().printUsage("grid");
            } else {
                int vecsize = parser.getCommand().getAsInt("-v");
                String bins = parser.getCommand().getAsString("-b");
                String dims = parser.getCommand().getAsString("-d");
                String axis = parser.getCommand().getAsString("-a");
                String out  = parser.getCommand().getAsString("-o");
                
                SparseVectorGrid  grid = SparseGridIO.createGrid(dims, bins, axis, vecsize);
                SparseGridIO.exportHipo(grid, out);
            }
        }
        
        if(parser.getCommand().getCommand().compareTo("-fill")==0){
             if(parser.getCommand().containsRequired()==false){
                parser.getCommand().explainMissing();
                parser.getCommand().printUsage("grid");
            } else {
                 String  inputGrid = parser.getCommand().getAsString("-grid");
                 int        column = parser.getCommand().getAsInt("-c");
                 String  inputFile = parser.getCommand().getAsString("-i");
                 String outputFile = parser.getCommand().getAsString("-o");
                 
                 SparseVectorGrid  grid = SparseGridIO.importHipo(inputGrid);
                 
                 grid.show();
                 //SparseGridIO.importFileToGrid(inputFile, grid, column);
                 SparseGridIO.exportHipo(grid, outputFile);            
             }
        }
        
        /**
         * Show COMMAND - displays the grid
         */
        if(parser.getCommand().getCommand().compareTo("-show")==0){
             if(parser.getCommand().containsRequired()==false){
                parser.getCommand().explainMissing();
                parser.getCommand().printUsage("grid");
                System.exit(0);
            } else {
                 String file = parser.getCommand().getAsString("-f");
                 GridStudio studio = new GridStudio();
                 studio.openFile(file);
             }
        }
        //System.out.println(parser.getCommand());
        
        /*
        
        if(args.length==1){
            GridStudio studio = new GridStudio();
            studio.openFile(args[0]);
        } else {
        
            if(args.length!=9){
                SparseGridIO.printUsage();
                System.exit(0);
            }
        
            //int[] bins    = new int[]{5,2,18,20,36};
            //int[] columns = new int[]{11,12,13}
            // Command line for GRID:
            // grid -crate -file pim_datatables_5D.txt -index "5:2:18:20:36" -vec "11:12:13" -out grid.hipo
            int[] bins    = SparseGridIO.getVectorInt(args[4]);
            int[] columns = SparseGridIO.getVectorInt(args[6]);
            
            String inputFile = args[2];
            String   outFile = args[8];
            
            SparseVectorGrid grid = SparseGridIO.importText(bins, 
                    inputFile, columns);
            
            grid.show();
            
            SparseGridIO.exportHipo(grid, outFile);
        }
        */
        
        /*
        SparseVectorGrid gridIn = SparseGridIO.importHipo( "grid.hipo");

        H1F h = grid.projection(4, 0);
        TCanvas c1 = new TCanvas("grid",500,500);
        c1.draw(h);
        System.out.println("Comparison Output");
        grid.show();
        System.out.println("Comparison Input");
        gridIn.show();*/
    }
}

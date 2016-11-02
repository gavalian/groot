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

/**
 *
 * @author gavalian
 */
public class SparseGridImport {
    
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
}

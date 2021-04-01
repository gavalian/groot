/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class TextFileReader {
    
    private BufferedReader bufferedReader = null;
    private List<String>   readLineTokens = new ArrayList<String>();
    private String              tokenizer = "\\s+";
    
    public TextFileReader(){
        
    }
    
    public TextFileReader(String delim){
        this.tokenizer = delim;
    }
    /**
     * Open a text file for reading.
     * @param name name of the file
     * @return 
     */
    public boolean openFile(String name){
        try {
            FileReader fileReader =  new FileReader(name);
            bufferedReader =  new BufferedReader(fileReader);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TextFileReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("[TextFileReader] ---> error openning file : " + name);
            this.bufferedReader = null;
            return false;
        }
        return true;
    }
    
    public final void setTokenizer(String t){
        this.tokenizer = t;
    }
    /**
     * reads the next line in the file and populates the internal list of tokens
     * @return true if read was successful, false otherwise.
     */
    public boolean readNext(){
        
        if(this.bufferedReader==null){
            this.readLineTokens.clear();
            return false;
        }
        
        try {
            String line = null;
            
            while( (line = bufferedReader.readLine())!=null ){
                if(line.startsWith("#")==false) break;
            }
            
            readLineTokens.clear();
            
            if(line==null){
                return false;
            }
            
            String[] tokens = line.trim().split(this.tokenizer);
            for(String token : tokens){
                this.readLineTokens.add(token);
            }
        } catch (IOException ex) {
            Logger.getLogger(TextFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public int       getDataSize(){ return this.readLineTokens.size();}
    
    public double[] getAsDouble(int min, int max){
        int size  = max-min+1;
        int[] index = new int[size];
        for(int i = 0; i < index.length;i++) index[i] = i + min;
        return this.getAsDouble(index);
    }
    /**
     * Returns specified columns as array of double
     * @param index array containing columns indices
     * @return
     */    
    public double[]  getAsDouble(int[] index){        
        double[] result = new double[index.length];        
        for(int i = 0; i < index.length; i++){
            int id = index[i];
            try {
                double value = Double.parseDouble(this.readLineTokens.get(id));
                result[i]    = value;
            } catch (Exception e) {
                System.out.println("[getAsDouble] error : parsing " + this.readLineTokens.get(id)
                        + " as double has failed");
                result[i] = 0.0;
            }
        }
        return result;
    }
    /**
     * returns array of ints for given range of columns.
     * @param min min number of column
     * @param max max number of column
     * @return 
     */
    public int[] getAsInt(int min, int max){
        int size  = max-min+1;
        int[] index = new int[size];
        for(int i = 0; i < index.length;i++) index[i] = i + min;
        return this.getAsInt(index);
    }    
    /**
     * returns specified columns as array of ints 
     * @param index array containing columns indices
     * @return 
     */
    public int[]  getAsInt(int[] index){        
        int[] result = new int[index.length];        
        for(int i = 0; i < index.length; i++){
            int id = index[i];
            try {
                int value = Integer.parseInt(this.readLineTokens.get(id));
                result[i]    = value;
            } catch (Exception e) {
                System.out.println("[getAsDouble] error : parsing " + this.readLineTokens.get(id)
                        + " as double has failed");
                result[i] = 0;
            }
        }
        return result;
    }
    /**
     * returns string representation of the array that was read.
     * @return 
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("LINE :");
        for(String item : this.readLineTokens){
            str.append("   ");
            str.append(item);
        }
        return str.toString();
    }
    
    public void show(){
        System.out.println(toString());
    }
    
    public static void main(String[] args){
        TextFileReader reader = new TextFileReader();
        reader.openFile("/Users/gavalian/Work/Software/Release-9.0/COATJAVA/coatjava/readertest.txt");
        while(reader.readNext()==true){
            reader.show();
            //int[] array = reader.getAsInt(0, 4);
            double[] array = reader.getAsDouble(10, 13);
            for(int i = 0; i < array.length; i++){
                System.out.print(" " + array[i]);
            }
            System.out.println();
        }
    }
}

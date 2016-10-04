/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gavalian
 */
public class DataGroupDescriptor {
    
    private int numberOfColumns = 1;
    private int numberOfRows    = 1;
    private String groupName    = "";
    
    private Map<Integer, List<String>>  groupContent = new LinkedHashMap<>();
    
    public DataGroupDescriptor(String name, int cols, int rows){
        this.groupName = name;
        this.numberOfColumns = cols;
        this.numberOfRows = rows;
    }
    
    public String getName(){ return this.groupName;}
    public int    getCols(){return this.numberOfColumns;}
    public int    getRows(){return this.numberOfRows;}
    
    public void add(int order, String link){
        if(groupContent.containsKey(order)==false){
            groupContent.put(order, new ArrayList<String>());
        }
        groupContent.get(order).add(link);
    }
    
    public List<String>  getList(int order){
        if(groupContent.containsKey(order)==false) return new ArrayList<String>();
        return this.groupContent.get(order);
    }
    
    public void   addEncoded(int order, String encodedString){
        String[] tokens = encodedString.split(":");
        List<String> elements = Arrays.asList(tokens);
        groupContent.put(order, elements);
    }
    
    public String getEncodedString(int order){
        if(groupContent.containsKey(order)==false) return "";
        List<String>  elements = groupContent.get(order);
        StringBuilder str = new StringBuilder();
        int icounter = 0;
        for(String item : elements){
            if(icounter!=0) str.append(":");
            str.append(item);
            icounter++;
        }
        return str.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(getName());
        str.append("\n");
        int icounter = 0;
        for(Map.Entry<Integer,List<String>> entry : groupContent.entrySet()){
            str.append(String.format("\t --> %5d\n", entry.getKey()));
            for(int i = 0; i < entry.getValue().size();i++){
                str.append(String.format("\t\t ==> %5d : %s\n", i,entry.getValue().get(i)));
            }
        }
        return str.toString();
    }
}

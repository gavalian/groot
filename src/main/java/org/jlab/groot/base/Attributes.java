/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComboBox;

/**
 *
 * @author gavalian
 */
public class Attributes {
    
    Map<AttributeType,Integer>  attributesMap = new LinkedHashMap<AttributeType,Integer>();
    Map<AttributeType,String>   attributesString = new LinkedHashMap<AttributeType,String>();
 
    public Attributes(){
        
    }
    
    public void add(AttributeType type, int value){
        this.attributesMap.put(type, value);
    }
    
    public boolean hasAttribute(AttributeType type){
        return this.attributesMap.containsKey(type);
    }
    
    public int get(AttributeType type){
        return this.attributesMap.get(type);
    }
    
    public String getString(AttributeType type){
        return this.attributesString.get(type);
    }
    
    public void addString(AttributeType type, String value){
        this.attributesString.put(type, value);
    }
    
    public Map<AttributeType,Integer>  getMap(){return this.attributesMap;}
    /**
     * makes a copy of an attributes.
     * @param attr 
     */
    public void copy(Attributes attr){
        this.attributesMap.clear();
        for(Map.Entry<AttributeType,Integer> entry : attr.getMap().entrySet()){
            this.attributesMap.put(entry.getKey(), entry.getValue());
        }
    }
    /**
     * from given attributes copies values of entries that exist in this class
     * @param attr 
     */
    public void copyValues(Attributes attr){
        for(Map.Entry<AttributeType,Integer> entry : getMap().entrySet()){
            if(attr.getMap().containsKey(entry.getKey())==true){
                this.attributesMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public static void  chooseByString(JComboBox comboBox, String value){
        int nentries = comboBox.getItemCount();
        for(int index = 0; index < nentries; index++){
            String item = (String) comboBox.getItemAt(index);
            if(value.compareTo(item)==0){
                comboBox.setSelectedIndex(index);
                return;
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<AttributeType,Integer> entry : getMap().entrySet()){
            str.append(String.format("* %-24s * %14d *\n",entry.getKey().getName(),
                    entry.getValue()));
        }
        return str.toString();
    }
}

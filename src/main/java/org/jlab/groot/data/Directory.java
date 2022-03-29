/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author gavalian
 * @param <T> Type of objects to keep in the directory.
 */
public class Directory<T> {
    
    String            directoryName  = "/";
    Directory      parentDirectory  = null;
    Directory      currentDirectory = null;
    
    Map<String,Directory> directoryMap = new LinkedHashMap<>();
    Map<String,T>     directoryObjects = new LinkedHashMap<>();
    /**
     * Default constructor creates a ROOT directory. The parent is
     * set to NULL and name is set to "/"
     */
    public Directory(){
        //currentDirectory = this;
    }
    /**
     * constructor for subdirectory
     * @param parent parent directory
     * @param name newly created directory name
     */
    public Directory(Directory parent, String name){
        this.parentDirectory = parent;
        this.directoryName   = name;
    }
    
    public String stringDirectoryFromPath(String path){
        int index = path.lastIndexOf("/");
        return path.substring(0, index);
    }
    
    public String stringObjectFromPath(String path){
        int index = path.lastIndexOf("/");
        return path.substring(index+1, path.length());
    }
    
    private void mkdir_s(String name){
        //System.out.println(" creating directory " + name + " in directory " + getDir().getDirectoryPath());
        if(currentDirectory==null){
            if(directoryMap.containsKey(name)==false){
                Directory dir = new Directory(this,name);
                directoryMap.put(name, dir);
            }
        } else {
            if(currentDirectory.getDirectoryMap().containsKey(name)==false){
                //System.out.println(" creating : " + name);
                Directory dir = new Directory(currentDirectory,name);
                currentDirectory.getDirectoryMap().put(name, dir);
            } else {
                //System.out.println(" There is no need to create : " + name);
            }
        }
    }
    /**
     * create a directory in current directory.
     * @param name directory name to create
     */
    public synchronized void mkdir(String name){
        
        if(name.contains("/")==false){ mkdir_s(name);return;}
        
        //System.out.println("aa");
        
        String  path    = null;
        boolean rootRef = false;
        
        if(name.startsWith("/")==false){
            path    = name;
        } else {
            path    = name.substring(1, name.length());
            rootRef = true;
        }
        //System.out.println("path = " + );
        Directory saveDir = currentDirectory;
        
        String[] tokens = path.split("/");
        
        if(rootRef==true) cd();
            
        for(String token : tokens){       
            mkdir_s(token);
            cd(token);
        }
        
        currentDirectory = saveDir;
    }
    /**
     * returns map of subdirectories.
     * @return Map<String,Directory> of sub directories
     */
    public Map<String,Directory>  getDirectoryMap(){
        return this.directoryMap;
    }
    /**
     * returns map of the objects in the directory
     * @return Map<String,T> map of objects.
     */
    public Map<String,T>  getObjectMap(){
        return this.directoryObjects;
    }
    /**
     * sets current directory to ROOT.
     */
    public void cd(){
        currentDirectory = null;
    }
    
    public Directory getRoot(){
        
        if(getParent()==null) return this;
        
        Directory parent = getParent();
        while(parent.getParent()!=null){
            parent = parent.getParent();
        }
        return parent;
    }
    
    public Directory getDirectoryByPath(String path){
        String[] tokens = path.split("/");
        Directory  root = getRoot();
        for(String item : tokens){
            if(root.cd(item)==false) return null;
        }
        return root.getDir();
    }
    /**
     * Changes current working directory to given directory
     * @param name directory name to change current one.
     * @return returns true if the directory exists and current directory was set.
     */
    public boolean cd(String name){        
        
        Directory saveDir = currentDirectory;        
        Directory startDir = currentDirectory;
        
        String relativePath = name;
        
        if(    currentDirectory==null) startDir = this;
        if(name.startsWith("/")==true){
            startDir = this;
            relativePath = name.substring(1, name.length());
        }
                
        boolean success = true;
        
        if(name.contains("/")==false){
            if(startDir.getDirectoryMap().containsKey(name)==true){
                currentDirectory = (Directory) startDir.getDirectoryMap().get(name);
            } else {
                System.out.println("[Directory::cd] error -> directory " + name 
                + " is not found in : " + startDir.getDirectoryPath(startDir));
                success = false;
            }
            return success;
        } else {
            String[] tokens = relativePath.split("/");
            /*System.out.print(" TOKENS = " );
            for(int i = 0; i < tokens.length; i++) System.out.print(" [" + tokens[i] + "] ");
            System.out.println();*/
            int index = 0;
            
            while(success==true&&index<tokens.length){
                //System.out.println(" start = " + startDir.getName());
                //System.out.println(tokens[index] + "  " + startDir.getDirectoryMap().containsKey(tokens[index]));
                if(startDir.getDirectoryMap().containsKey(tokens[index])==true){
                    startDir = (Directory) startDir.getDirectoryMap().get(tokens[index]);
                } else {
                    System.out.println("[Directory::cd] error -> directory " + tokens[index]
                            + " is not found in : " + startDir.getDirectoryPath());
                    success = false;
                }
                index++;
            }            
            if(success==false) {
                currentDirectory = saveDir;
            } else {
                currentDirectory = startDir;
            }
        }
        /*
        if(name.compareTo("/")==0){
            currentDirectory = null;
            System.out.println("chdir : " + getDirectoryPath());
            return true;
        } 
        
        if(currentDirectory != null){
            if(name.contains("/")==false){
                if(currentDirectory.getDirectoryMap().containsKey(name)==true){
                    this.currentDirectory = currentDirectory.getDir(name);
                } else {
                    System.out.println("chdir : direcory not found " + name);
                    return false;
                }
            }
        } else {
            currentDirectory = this.directoryMap.get(name);
        }
        System.out.println("chdir : " + getDirectoryPath());*/
        return success;
    }
    
    

    /**
     * Returns pointer to parent directory. For root directory it's NULL
     * @return pointer to parent.
     */
    public Directory getParent(){
        return this.parentDirectory;
    }
    /**
     * returns Directory name
     * @return directory name
     */
    public String getName(){return this.directoryName;}
    
    /**
     * returns list of 
     * @param dir
     * @return 
     */
    public List<String>  getDirectoryPath(Directory dir){
        
        List<String> pathElements = new ArrayList<>();
        //System.out.println(" dir = " + dir.getName() + " parent = " + );
        if(dir.getParent()==null){
            pathElements.add("/");
            return pathElements;
        }
        
        String  lastName = dir.getName();
        
        pathElements.add(lastName);
        Directory   parent = dir.getParent();
        
        while(parent!=null){
            pathElements.add(0, parent.getName());
            parent = parent.getParent();
        }
        return pathElements;        
    }
    /**
     * return a String representing full path to current directory.
     * @return 
     */
    public String getDirectoryPath(){
        
        if(currentDirectory==null&&parentDirectory==null){
            //System.out.println(" current directory = null");
            return "/";
        }                
        
        List<String> path = this.getDirectoryPath(this);
        
        StringBuilder str = new StringBuilder();
        for(String element : path){
            str.append(element);
            str.append("/");
        }
        //str.deleteCharAt(0);
        str.deleteCharAt(str.length()-1);
        str.deleteCharAt(0);
        return str.toString();
    }
    /**
     * Add an object to current directory.
     * @param name
     * @param object 
     */
    public void add(String name, T object){
        Directory dir = getDir();
        if(dir.getObjectMap().containsKey(name)==true){
            System.out.println("[TDirectory] [" + 
                    dir.getDirectoryPath() +
                    "]---> replacing object " + name);
        }
        //System.out.println("BEFORE " + dir.getObjectMap().size());
        dir.getObjectMap().put(name, object);
        //System.out.println("AFTER " + dir.getObjectMap().size());
        
        //currentDirectory.g.put(name, object);
        
    }
    
    public void pwd(){
        System.out.println("pwd : " + getDir().getDirectoryPath());
    }
    
    private String getDirectoryList(Directory dir){
        StringBuilder str = new StringBuilder();
        
        
        for(Object entry : dir.getDirectoryMap().entrySet()){
            Map.Entry<String,Directory> object = (Map.Entry<String,Directory>) entry;
            str.append("\t <dir> ");
            str.append(object.getKey());
            str.append("\n");
        }
        /*
        for(String entry : ){
            str.append("\t\t");
            str.append(entry.getName());
            str.append("\n");
        }*/
        
        return str.toString();
    }
    /**
     * list the content of current directory
     */
    public void ls(){
        System.out.println("dir (ls) :  " + this.getDirectoryPath());
        Directory dir = getDir();
        System.out.println(this.getDirectoryList(dir));        
    }
    /**
     * list the tree structure starting with current directory.
     */
    public void tree(){
        Directory dir = getDir();
        List<String> objectList = dir.getCompositeObjectList(dir);
        int counter = 0;
        for(String item : objectList){
            System.out.println( counter + " : " + item);
            counter++;
        }
    }
    /**
     * returns object 
     * @param directory
     * @param name
     * @return 
     */
    
    public T getObject(String directory, String name){
        String[] path = directory.split("/");
        cd();
        for(String element : path){
            //System.out.println("debug : trying " + element);
            if(cd(element)==false){
                return null;
            }
        }
        if(getDir().getObjectMap().containsKey(name)==false) return null;
        return (T) getDir().getObjectMap().get(name);
    }
    
    public void clear(){
        this.directoryMap.clear();
        this.directoryObjects.clear();
        this.currentDirectory = null;
    }
    
    public T getObject(String fullPath){
        
        int index = fullPath.lastIndexOf("/");
        String path   = fullPath.substring(0, index);
        String object = fullPath.substring(index+1, fullPath.length());
        
        Directory saveDir = currentDirectory;
        //System.out.println("PATH ["+path+"]");
        boolean status = cd(path);
        if(status == true){
            T item = (T) getDir().getObjectMap().get(object);
            currentDirectory = saveDir;
            return item;
        }
        
        return null;
        /*
        System.out.println("[Get Object]  [" + path + "]  [" + object + "]");
        System.out.println(" getting directory ["+path+"]");
        Directory  dir = getDirectoryByPath(path);
        if(dir==null){
            System.out.println("[Directory] --> directory does not exist with name : " + path );
            return null;
        }
        
        if(dir.getObjectMap().containsKey(object)==false){
            System.out.println("[Directory] --> object "  + object + "  does not exist in " + path );
            return null;
        }
        return (T) dir.getObjectMap().get(object);
        */
    }
    /*
    private List<String>  getRecursiveLeafs(Directory dir){
        
    }*/
    
    public Directory getDir(){
        if(currentDirectory==null) return this;
        return currentDirectory;
    }
    
    public Directory getDir(String dirName){
        return this.directoryMap.get(dirName);
    }
    
    public List<String>  getDirectoryList(){
        List<String>  keys = new ArrayList<String>();
        keys.addAll(directoryMap.keySet());
        return keys;
    }
    
    public List<T>  getObjectList(){        
        List<T> dsList = new ArrayList<>();
        dsList.addAll(directoryObjects.values());
        return dsList;
    }
    
    public List<String> getCompositeObjectList(Directory dir){
        List<String> objects = new ArrayList<>();
        
        List<String> dirList = dir.getDirectoryList();
        for(Object entry : dir.getObjectMap().entrySet()){
            Map.Entry<String,T> item = (Map.Entry<String,T>) entry;
            objects.add(dir.getDirectoryPath() +  "/" +item.getKey());
        }
        
        for(String list : dirList){
            Directory subDir = dir.getDir(list);
            List<String> dir_o = getCompositeObjectList(subDir);
            objects.addAll(dir_o);
        }
        return objects;
    }
    
    public boolean exists(String name){
        Directory saveDir = currentDirectory;
        boolean status = cd(name);
        currentDirectory = saveDir;
        return status;
    }
    
    public DefaultMutableTreeNode getTreeNode(){
        DefaultMutableTreeNode  root = new DefaultMutableTreeNode("Root");
        List<String>            objects = this.getCompositeObjectList(this);
        /*for(String obj : objects){
            System.out.println("---> " + obj);
        }*/
        Collections.sort(objects);
        /*for(String obj : objects){
            System.out.println("sorted ---> " + obj);
        }*/
        //System.out.println("Get Tree Nodes : Size = " + objects.size());
        DefaultMutableTreeNode node = getNodes(root,objects,1);
        return node;
    }
    
    public DefaultMutableTreeNode  getNodes(DefaultMutableTreeNode node,List<String> objects, int depth){        
        String name = node.toString();
        //System.out.println(" level = " + depth);
        Set<String> keys = this.getChildrenList(name, objects, depth);
        for(String key : keys){
            //System.out.println("---> adding : " + key);
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(key);
            node.add(child);
            getNodes(child,objects,depth+1);
        }
        return node;
    }
    
    public Set<String>  getChildrenList(String parent, List<String> list, int depth){
        
        SortedSet<String>  children = new TreeSet<String>();
        //Set<String>  children = new HashSet<String>();
        
        if(depth<1) return children;
        
        for(String object : list){
            
            String[] tokens = object.split("/");
            if(depth==1){
                if(tokens.length>0){
                    children.add(tokens[1]);
                }
            } else {
                if(tokens.length>depth){
                    if(tokens[depth-1].compareTo(parent)==0){
                        children.add(tokens[depth]);
                    }
                }
            }
        }
        return children;
    }
    
    public static void main(String[] args){
        
        Directory<H1F> dir = new Directory<>();
        
        dir.mkdir("/a");
        dir.cd("/a");
        dir.pwd();
        
        System.out.println("SIZE = " + dir.getObjectList().size());
        dir.add("TATA", new H1F());
        System.out.println("SIZE = " + dir.getObjectList().size());
        
        /*
        dir.mkdir("a");
        dir.cd("a");
        dir.pwd();
        dir.mkdir("b");
        dir.cd("b");
        dir.mkdir("c");
        dir.cd("c");
        dir.pwd();
        
        dir.cd();
        dir.pwd();
        dir.cd("a/b/c");
        dir.getDir().add("h1",new H1F("h1",100,0.3,0.4));
        dir.pwd();
        List<String> listing = dir.getCompositeObjectList(dir);
        for(String item : listing){
            System.out.println(item);
        }
        dir.cd("/a");
        dir.pwd();
        H1F h1 = dir.getObject("/a/b/c/h1");
        dir.pwd();*/
        //System.out.println(h1);
        /*
        dir.mkdir("a");
        dir.mkdir("b");
        dir.cd("a");
        
        dir.getDir().add("h1",new H1F("h1",100,0.3,0.4));
        dir.mkdir("c");
        dir.cd("c");
        dir.getDir().add("h1c",new H1F("h1",100,0.3,0.4));
        dir.mkdir("d");
        dir.cd("d");
        System.out.println("  name -> " + dir.getDir().getName() +  "  path = " + dir.getDir().getDirectoryPath());
        dir.getDir().add("h1cd",new H1F("h1",100,0.3,0.4));
        dir.cd();
        dir.cd("b");
        dir.getDir().add("h2",new H1F("h2",100,0.3,0.4));
        dir.cd();
        List<String> listing = dir.getCompositeObjectList(dir);
        for(String item : listing){
            System.out.println(item);
        }
        
        Directory dird = dir.getDirectoryByPath("/a/c/d");
        System.out.println(" name " + dird.getName() + "  " + dird.getDirectoryPath());
        
        H1F htest = dir.getObject("/a/c/d/h1cd");
        System.out.println("htest = " + htest.getName());
        */
    }
}

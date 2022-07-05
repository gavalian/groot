/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jlab.groot.data.DataSetSerializer4.DataSetType;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.group.DataGroupDescriptor;
import org.jlab.groot.math.F1D;
import org.jlab.groot.ui.TBrowser;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Node;
import org.jlab.jnp.utils.options.OptionParser;

/**
 *
 * @author gavalian
 */
public class TDirectory extends Directory<IDataSet> {
    
    Map<String,DataGroupDescriptor>  groupDescriptors = new LinkedHashMap<>();
    
    public TDirectory(){
        
    }
    
    public void addGroupDescriptor(String name, int cols, int rows){
        groupDescriptors.put(name, new DataGroupDescriptor(name,cols,rows));
    }
    
    public void addGroup(String name, int order, String dataset){
        if(groupDescriptors.containsKey(name)==true){
            groupDescriptors.get(name).add(order, dataset);
        } else {
            System.out.println("[TDirectory] --> error : data group descriptos does not exist ["+name+"]");
        }
        //System.out.println(groupDescriptors.get(name));
    }
    
    public DataGroup getDataGroup(String name){
        
        DataGroupDescriptor desc = groupDescriptors.get(name);
        DataGroup          group = new DataGroup(name,desc.getCols(),desc.getRows());
        
        int groupSize = desc.getCols()*desc.getRows();
        
        for(int i = 0; i < groupSize; i++){
            List<String>  items = desc.getList(i);
            String encoded = desc.getEncodedString(i);
            //System.out.println(i + " : " + encoded);
            for(String item : items){
                //System.out.println("getting object : " + item + "  for pad " + i);
                IDataSet ds = this.getObject(item);
                if(ds!=null){
                    group.addDataSet(ds, i);
                }
            }
        }
        return group;
    }
    
    
    public void addDataSet(IDataSet... dataSets){
        for(IDataSet ds : dataSets){
            add(ds.getName(),ds);
        }
    }
    
    public void load(String filename){
        org.jlab.jnp.hipo4.io.HipoReader reader = new  org.jlab.jnp.hipo4.io.HipoReader();
        Event event = new Event();
        reader.open(filename);
        
        while(reader.hasNext()==true){
            reader.nextEvent(event);
            Node type = event.read(21, 7);
            //System.out.println("--------- FOUND " + type.getInt(0));
            if(type.getInt(0) == DataSetType.H1F.getType()){
                H1F h = DataSetSerializer4.deserializeH1F(event);
                String fullName = h.getName();
                String dirname = this.stringDirectoryFromPath(fullName);
                String objname = stringObjectFromPath(fullName);
                
                h.setName(objname);
                mkdir(dirname);
                cd(dirname);
                addDataSet(h);
            }
            
            if(type.getInt(0) == DataSetType.H2F.getType()){
                H2F h = DataSetSerializer4.deserializeH2F(event);
                String fullName = h.getName();
                String dirname = this.stringDirectoryFromPath(fullName);
                String objname = stringObjectFromPath(fullName);
                
                h.setName(objname);
                mkdir(dirname);
                cd(dirname);
                addDataSet(h);
            }
            
             if(type.getInt(0) == DataSetType.FUNC.getType()){
                F1D f = DataSetSerializer4.deserializeF1D(event);
                String fullName = f.getName();
                String dirname = this.stringDirectoryFromPath(fullName);
                String objname = stringObjectFromPath(fullName);
                
                f.setName(objname);
                mkdir(dirname);
                cd(dirname);
                addDataSet(f);
            }
            
             if(type.getInt(0) == DataSetType.GRAPH.getType()){
                GraphErrors g = DataSetSerializer4.deserializeGraphErrors(event);
                String fullName = g.getName();
                String dirname = this.stringDirectoryFromPath(fullName);
                String objname = stringObjectFromPath(fullName);
                
                g.setName(objname);
                mkdir(dirname);
                cd(dirname);
                addDataSet(g);
            }
        }
        
    }
    
    private int getReqiredSize(List<Node> list){
        int size = 0; 
        for(Node n : list) size += n.getBufferSize();
        return size + 1024;
    }
    
    public void save(String filename){
        org.jlab.jnp.hipo4.io.HipoWriter writer = new  org.jlab.jnp.hipo4.io.HipoWriter();
        Event event = new Event(1024*1024);
        writer.open(filename);
        List<String>  objectList = this.getCompositeObjectList(this);
        for(String object : objectList){
            //System.out.println("writing object ---> " + object);
            IDataSet ds = getObject(object);
            if(ds!=null){
                if(ds instanceof H1F){
                    String dataName = ds.getName();                    
                    ds.setName(object);
                    List<Node> nodes = DataSetSerializer4.serializeH1F((H1F) ds);
                    event.reset();
                    int required = this.getReqiredSize(nodes);
                    event.require(required);
                    for(Node node: nodes) event.write(node);
                    writer.addEvent(event);
                    ds.setName(dataName);
                }
                
                if(ds instanceof H2F){
                    String dataName = ds.getName();
                    ds.setName(object);
                    List<Node> nodes = DataSetSerializer4.serializeH2F((H2F) ds);
                    event.reset();
                    int required = this.getReqiredSize(nodes);
                    event.require(required);
                    for(Node node: nodes) event.write(node);
                    writer.addEvent(event);
                    ds.setName(dataName);
                }
                
                if(ds instanceof F1D){
                    String dataName = ds.getName();
                    ds.setName(object);
                    List<Node> nodes = DataSetSerializer4.serializeF1D((F1D) ds);
                    event.reset();
                    int required = this.getReqiredSize(nodes);
                    event.require(required);
                    for(Node node: nodes) event.write(node);
                    writer.addEvent(event);
                    ds.setName(dataName);
                }
                if(ds instanceof GraphErrors){
                    String dataName = ds.getName();
                    ds.setName(object);
                    List<Node> nodes = DataSetSerializer4.serializeGraphErrors((GraphErrors) ds);
                    event.reset();
                    int required = this.getReqiredSize(nodes);
                    event.require(required);
                    for(Node node: nodes) event.write(node);
                    writer.addEvent(event);
                    ds.setName(dataName);
                }
            }
        }
        writer.close();        
    }
    
    public void writeFile(String filename){
        save(filename);
        /*
        HipoWriter  writer = new HipoWriter();
        writer.setCompressionType(1);
        writer.open(filename);
        
        List<String>  objectList = this.getCompositeObjectList(this);
        for(String object : objectList){
            //System.out.println("writing object ---> " + object);
            IDataSet ds = getObject(object);
            if(ds!=null){
                String dataset_name = ds.getName();
                
                ds.setName(object);
                List<HipoNode>  nodes = DataSetSerializer.serializeDataSet(ds);
                HipoEvent       event = new HipoEvent();
                event.addNodes(nodes);
                writer.writeEvent(event);
                ds.setName(dataset_name);
            } else {
                System.out.println("[TDirectory::writeFile] error getting object : " + object);
            }
        }
        System.out.println("---> writing data group descriptors, size = " + groupDescriptors.size());
        for(Map.Entry<String,DataGroupDescriptor> entry : groupDescriptors.entrySet()){
            List<HipoNode>  nodes = DataSetSerializer.serializeDataGroupDescriptor(entry.getValue());
            HipoEvent       event = new HipoEvent();
            event.addNodes(nodes);
            event.updateNodeIndex();
            System.out.println(event);
            writer.writeEvent(event);
        }
        writer.close();*/
    }
    
    public static void addFiles(String outputFile, List<String> inputFiles){
        TDirectory dir = new TDirectory();
        
        dir.readFile(inputFiles.get(0));
        
        List<String>  objectList = dir.getCompositeObjectList(dir);

        System.out.println("********* OBJECT LIST ***********");
        for(String object : objectList){
            System.out.println("-> " + object);
        }        
        System.out.println("******* END OBJECT LIST *********");
        for(int loop = 1; loop < inputFiles.size(); loop++){
            TDirectory inDir = new TDirectory();
            inDir.readFile(inputFiles.get(loop));
            for(String object : objectList){
                try {
                    IDataSet dataset = (IDataSet) dir.getObject(object);
                    if(dataset instanceof H1F){
                        H1F h1 = (H1F) dataset;
                        H1F h2 = (H1F) inDir.getObject(object);
                        if(h2!=null){
                            h1.add(h2);
                        }
                    }
                    
                    if(dataset instanceof H2F){
                        H2F h1 = (H2F) dataset;
                        H2F h2 = (H2F) inDir.getObject(object);
                        if(h2!=null){
                            h1.add(h2);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("--> error with dataset : " + object);
                }
            }
        }
        
        dir.writeFile(outputFile);
    }
    
    public void readFile(String filename){
        load(filename);
        
        System.out.println("\n\n[4.0.1] READING FILE COMPLETED : " + filename);
        /*
        HipoReader reader = new HipoReader();
        reader.open(filename);
        
        clear();
        this.groupDescriptors.clear();
        
        int nevents = reader.getEventCount();
        System.out.println(" DEBUG : event count = " + nevents);
        int counter = 0;
        int groupCounter = 0;
        
        int nrecords = reader.getRecordCount();
        
        //for(int i = 0; i < nevents; i++){
        //while(reader.hasNext()==true){
            //byte[] eventBuffer = reader.readEvent(i);
            //System.out.println(" EVENT # " + i + "  SIZE = " + eventBuffer.length);
        for(int r = 0 ; r < nrecords; r++){
            boolean status = reader.readRecord(r);
            int nrevents = reader.getRecordEventCount();
            for(int e = 0; e < nrevents; e++){
                //HipoEvent    event = reader.readNextEvent();
                HipoEvent    event = reader.readRecordEvent(e);
                if(event.hasGroup(1200)==true){
                groupCounter++;
                System.out.println("--> reading data group descriptor");
                System.out.println(event);
                DataGroupDescriptor desc = DataSetSerializer.deserializeDataGroupDescriptor(event);
                this.groupDescriptors.put(desc.getName(), desc);
            } else {            
                //System.out.println(event.toString());
                IDataSet h1 = DataSetSerializer.deserializeDataSet(event);
                String h1name = h1.getName();
                //System.out.println(" NAME = [" +  h1name + "]");
                //System.out.println("name -> : " + h1name + " -> " + this.stringDirectoryFromPath(h1name)
                //+ " obj ---> " + this.stringObjectFromPath(h1name));
                String dirname = this.stringDirectoryFromPath(h1name);
                //System.out.println("[DIRNAME] = [" + dirname + "]");
                mkdir(dirname);
                cd(dirname);
                //pwd();
                //System.out.println("OBJECTS = " + this.getObjectList().size());
                h1.setName(this.stringObjectFromPath(h1name));
                //System.out.println(h1.toString());
                addDataSet(h1);
                //this.ls();
                }
                counter++;
            }
            
        }
        System.out.println(" EVENT # " + nevents + " : counter = " +
                counter + " groups = " + groupCounter);*/
    }
    
    
    public static void main(String[] args){
        
        OptionParser p = new OptionParser("hadd");
        
        p.addRequired("-o", "output file name");
        
        p.parse(args);
        String outputFile = p.getOption("-o").stringValue();
        TDirectory.addFiles(outputFile, p.getInputList());
        
        //TDirectory dir = new TDirectory();
        //dir.readFile("/Users/gavalian/Desktop/CLAS12Mon_run_5038_03-28-2022_11.45.06_PM.hipo");
        
        
        /*
        TDirectory dir = new TDirectory();
        
        dir.ls();
        dir.mkdir("/first");
        dir.cd("/first");
        H1F h = new H1F("h100","data sample",100,0.0,1.0);
        h.setTitleX("x axis");
        h.setTitleY("y axis");
        
        
        H2F h2 = new H2F("h200","2d gaussian",20,0.0,1.0,30,0.0,1.0);
        h2.setTitleX("gaus X axis");
        h2.setTitleY("gaus Y axis");
        Random r = new Random();
        
        for(int i = 0; i < 10000; i++){
            h.fill(r.nextGaussian()+0.6);
            h2.fill(r.nextGaussian()+0.6, r.nextGaussian()+0.3);
        }
        dir.addDataSet(h);
        dir.addDataSet(h2);
        dir.cd("/");
        dir.mkdir("/second");
        dir.cd("/second");
        
        F1D func = new F1D("func","[a]+[b]*x",0.0,1.0);
        func.setParameter(0,5.0);
        func.setParameter(1,2.0);
        
        func.setLineColor(5);
        func.setLineStyle(4);
        func.setLineWidth(2);
        
        dir.addDataSet(func);

        GraphErrors gr = new GraphErrors("graph_100",new double[]{0.1,0.2,0.3,0.4}, new double[]{2.4,3,6,1.2});
        
        dir.addDataSet(gr);
        
        dir.save("newDir.hipo");
        
        
        TDirectory rdir = new TDirectory();
        
        rdir.load("newDir.hipo");
        
        rdir.ls();
        
        TBrowser t = new TBrowser(rdir);*/
       /* dir.mkdir("/aa");
        dir.cd("/aa");
        dir.add("TEST", new H1F());
        dir.cd();
        System.out.println(" ADDING " + dir.getObjectList().size());*/
       //dir.readFile("/Users/gavalian/Desktop/out_monitor.hipo");
       //TBrowser browser = new TBrowser(dir);
        
        /*
        if(args.length<3){
            System.out.println("error: \n\n Usage : hadd [outputfile] [input1] [input2] ....\n\n");
            String filename = "ftCalCosmic_0_04-04-2018_05.59.18_PM.hipo";
            dir.readFile(filename);
            System.exit(0);
        }
        
        String outputFile = args[0];
        
        List<String> inputFiles = new ArrayList<>();
        for(int i = 1; i < args.length;i++) inputFiles.add(args[i]);        
        TDirectory.addFiles(outputFile, inputFiles);
        */        
        
    }
}

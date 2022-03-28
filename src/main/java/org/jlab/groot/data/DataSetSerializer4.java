/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.group.DataGroupDescriptor;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.Func1D;

import org.jlab.jnp.hipo4.data.DataType;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Node;


/**
 *
 * @author gavalian
 */
public class DataSetSerializer4 {
    
    public enum DataSetType {
    
        H1F     (11,100),
        H2F     (12,200),
        GRAPH   (13,300),
        FUNC    (14,400);
    
        private final int type;
        private final int group;
        
        DataSetType(){
            type = 1; group = 100;
        }
        
        DataSetType(int t, int g){ type = t;group = g;}
        public int getType(){return type;}
        public int getGroup(){ return group;}
    }
    /**
     * serialization of H1F into HipoNodes
     * @param h1
     * @return 
     */
    public static List<Node>  serializeH1F(H1F h1){
        
        List<Node> nodes = new ArrayList<>();
        Node   storeDataType = new Node(21,7, new int[]{DataSetType.H1F.getType()});
        
        Node   dataType   = new Node(100,1, new short[]{(short) DataSetType.H1F.getType()});
        Node    dataUID   = new Node(100,2, new long[]{h1.getUniqueID()});        
        Node   dataName   = new Node(100,3, h1.getName());
        Node dataTitles   = new Node(100,4, h1.getTitle()+":"+h1.getTitleX()+":"+h1.getTitleY());
        
        //HipoNode dataAttr   = DataSetSerializer.serializeDataAttributes(100, 3, h1.getAttributes());
        Node axisBins   = new Node(100,5, new int[]{h1.getXaxis().getNBins()});
        Node axisLimits = new Node(100,6, new double[]{ h1.getXaxis().min() , h1.getXaxis().max()});
        Node overUnder  = new Node(100,7, new long[]{ h1.getEntries(), h1.getUnderflow(), h1.getOverflow()});
        
        Node axisData   = new Node(100,8, DataType.DOUBLE, h1.getxAxis().getNBins());        
        for(int i = 0; i < h1.getXaxis().getNBins(); i++) axisData.setDouble(i, h1.getBinContent(i));                
        
        Collections.addAll(nodes, 
                dataType,dataUID, dataName, dataTitles,
                axisBins,axisLimits, overUnder,axisData);
        
        List<Node>  attrNodes = DataSetSerializer4.serializeAttributes(100, h1.getAttributes());
        nodes.addAll(attrNodes);
        nodes.add(storeDataType);
        
        //------- HAS TO BE UNCOMMENTED -----
        /*
        if(h1.getFunction()!=null){
            List<HipoNode> funcNodes = DataSetSerializer.serializeF1D(h1.getFunction());
            nodes.addAll(funcNodes);
        }*/                
        return nodes;
    }
    
    public static List<Node>  serializeH2F(H2F h2){
        
        List<Node> nodes = new ArrayList<>();        
        Node   storeDataType = new Node(21,7, new int[]{DataSetType.H2F.getType()});
        Node   dataType   = new Node(200,1, new short[]{(short) DataSetType.H2F.getType()});
        Node    dataUID   = new Node(200,2, new long[]{h2.getUniqueID()});
        Node   dataName   = new Node(200,3, h2.getName());
        Node dataTitles   = new Node(200,4, h2.getTitle()+":"+h2.getTitleX()+":"+h2.getTitleY());
        
        //HipoNode dataAttr   = DataSetSerializer.serializeDataAttributes(100, 3, h1.getAttributes());
        Node axisBins   = new Node(200,5, new int[]{h2.getXAxis().getNBins(),h2.getYAxis().getNBins()});
        Node axisLimits = new Node(200,6, new double[]{ 
            h2.getXAxis().min() , h2.getXAxis().max(),
            h2.getYAxis().min() , h2.getYAxis().max()
        });
        
        Node overUnder  = new Node(200,7, new long[]{ h2.getEntries(),0L,0L,0L,0L});
        
        int bufferSize  =  h2.getDataBufferSize();
        Node axisData   = new Node(200,8, DataType.DOUBLE, bufferSize);        
        for(int i = 0; i < bufferSize; i++) axisData.setDouble(i, h2.getDataBufferBinAsDouble(i));                
        
        Collections.addAll(nodes, 
                dataType,dataUID, dataName, dataTitles,
                axisBins,axisLimits, overUnder,axisData);
        
        List<Node>  attrNodes = DataSetSerializer4.serializeAttributes(200, h2.getAttributes());
        nodes.addAll(attrNodes);
        nodes.add(storeDataType);
        
        //------- HAS TO BE UNCOMMENTED -----
        /*
        if(h1.getFunction()!=null){
            List<HipoNode> funcNodes = DataSetSerializer.serializeF1D(h1.getFunction());
            nodes.addAll(funcNodes);
        }*/                
        return nodes;        
    }
    
    public static List<Node>  serializeF1D(Func1D func){
        
        List<Node>  nodes = new ArrayList<>();
        Node   storeDataType = new Node(21,7, new int[]{DataSetType.FUNC.getType()});
        
        Node dataType    = new Node(400,1,new short[]{(short) DataSetType.FUNC.getType()});
        Node dataName    = new Node(400,2,func.getName());
        Node dataExpr    = new Node(400,3,func.getExpression());
        Node dataLimits  = new Node(400,4,new double[]{func.getMin(),func.getMax()});
        Node fitResults  = new Node(400,5, new double[]{func.getChiSquare(), func.getNDF()});
        //Node dataClass   = new Node(500,4,func.getClass().getName());        
//        Node dataLimits  = new Node(500,6,HipoNodeType.FLOAT,3);
        
        Node dataPars    = new Node(400,6,DataType.DOUBLE,func.getNPars());
        Node dataParsErr = new Node(400,7,DataType.DOUBLE,func.getNPars());                
        
        //dataType.setInt(0, 5);
        //dataType.setInt(1, func.getNDF());
        
        for(int i = 0; i < func.getNPars(); i++){
            dataPars.setDouble(i, func.getParameter(i));
            dataParsErr.setDouble(i, func.parameter(i).error());
        }
        
        List<Node> dataAttr = DataSetSerializer4.serializeAttributes(400, func.getAttributes());
        nodes.add(storeDataType);        
        Collections.addAll(nodes, dataType,dataName,dataExpr,fitResults,dataLimits,dataPars,dataParsErr);
        nodes.addAll(dataAttr);
        
        return nodes;
    }
    
    public static List<Node>  serializeGraphErrors(GraphErrors gr){        
        List<Node> nodes = new ArrayList<Node>();
        Node   storeDataType = new Node(21,7, new int[]{DataSetType.GRAPH.getType()});
        
        Node dataType   = new Node(300,1,new int[]{1});
        Node dataName   = new Node(300,2,gr.getName());
        
        Node axisDataX  = new Node(300,3,DataType.DOUBLE,gr.getDataSize(0));
        Node axisDataY  = new Node(300,4,DataType.DOUBLE,gr.getDataSize(0));
        Node axisDataXE = new Node(300,5,DataType.DOUBLE,gr.getDataSize(0));
        Node axisDataYE = new Node(300,6,DataType.DOUBLE,gr.getDataSize(0));

        for(int i = 0; i < gr.getDataSize(0); i++){
            axisDataX.setDouble(i, gr.getDataX(i));
            axisDataY.setDouble(i, gr.getDataY(i));
            axisDataXE.setDouble(i, gr.getDataEX(i));
            axisDataYE.setDouble(i, gr.getDataEY(i));
        }
        
        List<Node> attrNodes = DataSetSerializer4.serializeAttributes(300, gr.getAttributes());
        nodes.add(storeDataType);
        nodes.add(dataType);
        nodes.add(dataName);
        nodes.add(axisDataX);
        nodes.add(axisDataY);
        nodes.add(axisDataXE);
        nodes.add(axisDataYE);
        nodes.addAll(attrNodes);        
        return nodes;
    }
    
    
    
    public static List<Node>  serializeAttributes(int group, DatasetAttributes attr){
        
        List<Node>  attrNodes = new ArrayList<Node>();
        
        Node attrNode   = new Node(group,31,DataType.INT,8);
        
        attrNode.setInt(0, attr.getLineColor());
        attrNode.setInt(1, attr.getLineWidth());
        attrNode.setInt(2, attr.getLineStyle());
        attrNode.setInt(3, attr.getMarkerColor());
        attrNode.setInt(4, attr.getMarkerSize());
        attrNode.setInt(5, attr.getMarkerStyle());
        attrNode.setInt(6, attr.getFillColor());
        attrNode.setInt(7, attr.getFillStyle());
        
        Node attrTitle   = new Node(group,32,attr.getTitle());
        Node attrTitleX  = new Node(group,33,attr.getTitleX());
        Node attrTitleY  = new Node(group,34,attr.getTitleY());
        Node attrOptStat = new Node(group,35,attr.getOptStat());
        
        Collections.addAll(attrNodes, attrNode,attrTitle,attrTitleX,attrTitleY,attrOptStat);
        //Collections.addAll(attrNodes);
        return attrNodes;
    }
    
    public static Node  serializeDataAttributes(int group, int item, DatasetAttributes attr){
        
        Node attrNode   = new Node(group,item,DataType.BYTE,8);
        
        attrNode.setByte(0, (byte) attr.getLineColor());
        attrNode.setByte(1, (byte) attr.getLineWidth());
        attrNode.setByte(2, (byte) attr.getLineStyle());
        attrNode.setByte(3, (byte) attr.getMarkerColor());
        attrNode.setByte(4, (byte) attr.getMarkerSize());
        attrNode.setByte(5, (byte) attr.getMarkerStyle());
        attrNode.setByte(6, (byte) attr.getFillColor());
        attrNode.setByte(7, (byte) attr.getFillStyle());
        
        return attrNode;
    }
    
    public static void copyAttributes(DatasetAttributes attr, IDataSet ds){
        ds.getAttributes().setLineColor(attr.getLineColor());
        ds.getAttributes().setLineWidth(attr.getLineWidth());
        ds.getAttributes().setLineStyle(attr.getLineStyle()); 
        ds.getAttributes().setMarkerColor(attr.getMarkerColor());
        ds.getAttributes().setMarkerSize(attr.getMarkerSize());
        ds.getAttributes().setMarkerStyle(attr.getMarkerStyle());
        ds.getAttributes().setFillColor(attr.getFillColor());
        ds.getAttributes().setFillStyle(attr.getFillStyle());
    }
    
    public static DatasetAttributes deserializeAttributes(Map<Integer,Node> dataNodes){
        DatasetAttributes  attr = new DatasetAttributes();
        Node node = dataNodes.get(31);
        
        attr.setLineColor(node.getInt(0));
        attr.setLineWidth(node.getInt(1));
        attr.setLineStyle(node.getInt(2));
        attr.setMarkerColor(node.getInt(3));
        attr.setMarkerSize(node.getInt(4));
        attr.setMarkerStyle(node.getInt(5));
        attr.setFillColor(node.getInt(6));
        attr.setFillStyle(node.getInt(7));
        if(dataNodes.containsKey(32)==true)
            attr.setTitle(dataNodes.get(32).getString());
        if(dataNodes.containsKey(33)==true)
            attr.setTitleX(dataNodes.get(33).getString());
        if(dataNodes.containsKey(34)==true)
            attr.setTitleY(dataNodes.get(34).getString());
        if(dataNodes.containsKey(35)==true)
            attr.setOptStat(dataNodes.get(35).getString());
        
        return attr;
    }
    

    
    public static F1D deserializeF1D(Event event){
        //Map<Integer,HipoNode> nodes = event.getGroup(500);
        //System.out.println(" F1D DESERIALIZER NODES SIZE = " + nodes.size());
        Map<Integer,Node> nodes = event.readNodes(400);
        //System.out.println("READING SIZE (F1D) = " + nodes.size());
        
        //event.scanShow();
        //DataSetSerializer4.printMap(nodes);
        //if(nodes.size()!=8) return null;
        String funcname   = nodes.get(2).getString();
        String expression = nodes.get(3).getString();
        F1D func = new F1D(funcname,expression,nodes.get(4).getDouble(0),nodes.get(4).getDouble(1));
        

        func.setChiSquare(nodes.get(5).getDouble(0));
        func.setNDF((int) nodes.get(5).getDouble(1));
        
        for(int i = 0; i < func.getNPars(); i++){ 
            func.setParameter(i, nodes.get(6).getDouble(i));
            func.parameter(i).setError(nodes.get(7).getDouble(i));
        }
        //System.out.println(func);
        return func;
    }
    
    public static GraphErrors deserializeGraphErrors(Event event){
        //Map<Integer,HipoNode> nodes = event.getGroup(300);
        Map<Integer,Node> nodes = event.readNodes(300);
        //if(nodes.size()!=6) return null;
        //System.out.println("READING SIZE (GRAPH) = " + nodes.size());
        //event.scanShow();
        //event.scan();
        //DataSetSerializer4.printMap(nodes);
        
        String   grname = nodes.get(2).getString();
        GraphErrors graph = new GraphErrors(grname);
        
        if(nodes.containsKey(3)&&nodes.containsKey(4)&&
                nodes.containsKey(5)&&nodes.containsKey(6)){                    
            int     npoints = nodes.get(3).getDataSize();                        
            for(int i = 0; i < npoints; i++){
                graph.addPoint(
                        nodes.get(3).getDouble(i),
                        nodes.get(4).getDouble(i),
                        nodes.get(5).getDouble(i),
                        nodes.get(6).getDouble(i)
                );
            }
        } else {
            if(nodes.containsKey(3)&&nodes.containsKey(4)){
                int     npoints = nodes.get(3).getDataSize();                        
                for(int i = 0; i < npoints; i++){
                    graph.addPoint(
                            nodes.get(3).getDouble(i),
                            nodes.get(4).getDouble(i),
                            0.0,0.0
                    );
                }
            }
        }
        DatasetAttributes attr = DataSetSerializer4.deserializeAttributes(nodes);
        graph.getAttributes().setAttributes(attr);        
        //F1D func = DataSetSerializer.deserializeF1D(event);
        //if(func!=null) graph.setFunction(func);        
        return graph;
    }
    
    public static void printMap(Map<Integer,Node> nodes){
        for(Map.Entry<Integer,Node> entry : nodes.entrySet()){
            System.out.printf("key = %5d : (%6d, %3d), type = %8s\n",entry.getKey(),
                    entry.getValue().getGroup(),entry.getValue().getItem(), entry.getValue().getType().getName());
        }
    }
    public static H1F deserializeH1F(Event event){
        
        Map<Integer,Node> nodes = event.readNodes(100);
        //DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes.get(3));
        //System.out.println("READING SIZE (H1F) = " + nodes.size());
        //event.scanShow();
        //DataSetSerializer4.printMap(nodes);
        
        H1F h1 = new H1F();
        DatasetAttributes attr = DataSetSerializer4.deserializeAttributes(nodes);
        h1.getAttributes().setAttributes(attr);
        //DataSetSerializer.copyAttributes(attr, h1);
        
        h1.set(nodes.get(5).getInt(0),nodes.get(6).getDouble(0),nodes.get(6).getDouble(1));
        h1.setEntries(nodes.get(7).getLong(0));
        h1.setUnderflow(nodes.get(7).getLong(1));
        h1.setOverflow(nodes.get(7).getLong(2));
        
        h1.setName(nodes.get(3).getString());
        for(int i = 0; i < h1.getXaxis().getNBins();i++) h1.setBinContent(i, nodes.get(8).getDouble(i));
        
        //F1D func = DataSetSerializer.deserializeF1D(event);
        //if(func!=null) h1.setFunction(func);
        return h1;
    }
    
    public static H2F deserializeH2F(Event event){
        
        Map<Integer,Node> nodes = event.readNodes(200);
        //DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes.get(3));
                
        H2F h2 = new H2F();
        DatasetAttributes attr = DataSetSerializer4.deserializeAttributes(nodes);
        h2.getAttributes().setAttributes(attr);
        //DataSetSerializer.copyAttributes(attr, h1);        
        //h2.set(nodes.get(4).getInt(0),nodes.get(5).getFloat(0),nodes.get(5).getFloat(1));
        
        h2.setName(nodes.get(3).getString());
        h2.set(
                nodes.get(5).getInt(0), 
                nodes.get(6).getDouble(0),nodes.get(6).getDouble(1),
                nodes.get(5).getInt(1), 
                nodes.get(6).getDouble(2),nodes.get(6).getDouble(3)
        );
        
        for(int i = 0; i < nodes.get(8).getDataSize();i++) h2.setDataBufferBin(i, nodes.get(8).getDouble(i));
        
        return h2;
    }
    
    public static List<Node>  serializeDataSet(IDataSet ds){
        //List<HipoNode>  attributes = DataSetSerializer.serializeDataAttributes(ds.getAttributes());
        
        if(ds instanceof H1F){
            List<Node>  dsNodes = DataSetSerializer4.serializeH1F( (H1F) ds);
            //attributes.addAll(dsNodes);
            return dsNodes;
        }
        
        if(ds instanceof H2F){
            List<Node>  dsNodes = DataSetSerializer4.serializeH2F( (H2F) ds);
            return dsNodes;
        }
        
        if(ds instanceof GraphErrors){
            List<Node>  dsNodes = DataSetSerializer4.serializeGraphErrors( (GraphErrors) ds);
            return dsNodes;
        }
        
        if(ds instanceof F1D){
            List<Node>  dsNodes = DataSetSerializer4.serializeF1D( (F1D) ds);
            return dsNodes;
        }
        
        return null;
    }
    
    public static IDataSet deserializeDataSet(Event event){        
       
        //H1F h1 = DataSetSerializer.deserializeH1F(event);
        return null;
    }
        
    public static void main(String[] args){
        
        GraphErrors gr = new GraphErrors("gr",new double[]{1,2,3,4},new double[]{3,4,5,6});
        List<Node> nodes = DataSetSerializer4.serializeGraphErrors(gr);
        System.out.println(" size = " + nodes.size());
        Event event = new Event();
        gr.setTitle("normal graph");
        gr.setTitleX("graph X");
        gr.setTitleY("graph Y");

        for(Node n : nodes) {
            System.out.println(" n " + n.getGroup() + " " + n.getItem());
            event.write(n);
        }
        
        event.show();        
        event.scan();
        
        GraphErrors g = DataSetSerializer4.deserializeGraphErrors(event);
        
        TDirectory dir = new TDirectory();
        
        dir.mkdir("/ec");
        dir.cd("/ec");
        dir.add("graph", gr);
        dir.cd();
        dir.ls();
        dir.save("dir.hipo");
        
        TDirectory dir2 = new TDirectory();        
        dir2.readFile("dir.hipo");        
        dir2.ls();
        
    }
}

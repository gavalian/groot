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
import org.jlab.jnp.hipo.data.HipoEvent;
import org.jlab.jnp.hipo.data.HipoNode;
import org.jlab.jnp.hipo.data.HipoNodeType;


/**
 *
 * @author gavalian
 */
public class DataSetSerializer {

    /**
     * serialization of H1F into HipoNodes
     * @param h1
     * @return 
     */
    
    public static List<HipoNode>  serializeH1F(H1F h1){
        
        List<HipoNode> nodes = new ArrayList<>();

        HipoNode dataType   = new HipoNode(100,1, HipoNodeType.INT,1);
        HipoNode dataName   = new HipoNode(100,2, h1.getName());
        //HipoNode dataAttr   = DataSetSerializer.serializeDataAttributes(100, 3, h1.getAttributes());
        HipoNode axisBins   = new HipoNode(100,4, HipoNodeType.INT,1);
        HipoNode axisLimits = new HipoNode(100,5, HipoNodeType.FLOAT,2);
        HipoNode axisData   = new HipoNode(100,6, HipoNodeType.FLOAT, h1.getxAxis().getNBins());        
        
        axisBins.setInt(0, h1.getXaxis().getNBins());
        axisLimits.setFloat(0, (float) h1.getXaxis().min());
        axisLimits.setFloat(1, (float) h1.getXaxis().max());
        
        for(int i = 0; i < h1.getXaxis().getNBins(); i++) axisData.setFloat(i, (float) h1.getBinContent(i));
        Collections.addAll(nodes, dataType,dataName,axisBins,axisLimits,axisData);
        
        List<HipoNode>  attrNodes = DataSetSerializer.serializeAttributes(100, h1.getAttributes());
        nodes.addAll(attrNodes);
        
        if(h1.getFunction()!=null){
            List<HipoNode> funcNodes = DataSetSerializer.serializeF1D(h1.getFunction());
            nodes.addAll(funcNodes);
        }
        return nodes;
    }
    
    public static List<HipoNode>  serializeH2F(H2F h2){
        
        List<HipoNode> nodes = new ArrayList<>();

        HipoNode dataType   = new HipoNode(200,1,HipoNodeType.INT,1);
        HipoNode dataName   = new HipoNode(200,2,h2.getName());
        HipoNode axisBins   = new HipoNode(200,3,HipoNodeType.INT,2);
        HipoNode axisLimits = new HipoNode(200,4,HipoNodeType.FLOAT,4);
        HipoNode dataBuffer = new HipoNode(200,5,HipoNodeType.FLOAT,h2.getDataBufferSize());
        
        axisBins.setInt(0, h2.getXAxis().getNBins());
        axisBins.setInt(1, h2.getYAxis().getNBins());
        axisLimits.setFloat(0, (float) h2.getXAxis().min());
        axisLimits.setFloat(1, (float) h2.getXAxis().max());
        axisLimits.setFloat(2, (float) h2.getYAxis().min());
        axisLimits.setFloat(3, (float) h2.getYAxis().max());
        
        for(int i = 0; i < h2.getDataBufferSize();i++){
            dataBuffer.setFloat(i, h2.getDataBufferBin(i));
        }
        
        Collections.addAll(nodes, dataType,dataName,axisBins,axisLimits,dataBuffer);
        
        List<HipoNode>  attrNodes = DataSetSerializer.serializeAttributes(200, h2.getAttributes());
        nodes.addAll(attrNodes);
        
        /*HipoNode axisData   = new HipoNode(800,5,HipoNodeType.FLOAT, h2data.length);
        
        axisBins.setInt(0, h2.getXaxis().getNBins());
        axisBins.setInt(1, h2.getYaxis().getNBins());
        axisLimits.setFloat(0, (float) h2.getXaxis().min());
        axisLimits.setFloat(1, (float) h2.getXaxis().max());
        axisLimits.setFloat(2, (float) h2.getYaxis().min());
        axisLimits.setFloat(3, (float) h2.getYaxis().max());
        
        for(int i = 0; i < h2data.length; i++) axisData.setFloat(i, h2data[i]);
        */
        return nodes;
    }
    
    public static List<HipoNode>  serializeF1D(Func1D func){
        
        List<HipoNode>  nodes = new ArrayList<>();
        
        HipoNode dataType    = new HipoNode(500,1,HipoNodeType.INT,2);
        HipoNode dataName    = new HipoNode(500,2,func.getName());
        HipoNode dataAttr    = DataSetSerializer.serializeDataAttributes(500, 3, func.getAttributes());
        HipoNode dataClass   = new HipoNode(500,4,func.getClass().getName());
        HipoNode dataExpr    = new HipoNode(500,5,func.getExpression());
        HipoNode dataLimits  = new HipoNode(500,6,HipoNodeType.FLOAT,3);
        HipoNode dataPars    = new HipoNode(500,7,HipoNodeType.FLOAT,func.getNPars());
        HipoNode dataParsErr = new HipoNode(500,8,HipoNodeType.FLOAT,func.getNPars());
        
        
        dataLimits.setFloat(0, (float) func.getMin());
        dataLimits.setFloat(1, (float) func.getMax());
        dataLimits.setFloat(2, (float) func.getChiSquare());
        
        dataType.setInt(0, 5);
        dataType.setInt(1, func.getNDF());
        
        for(int i = 0; i < func.getNPars(); i++){
            dataPars.setFloat(i, (float) func.getParameter(i));
            dataParsErr.setFloat(i, (float) func.parameter(i).error());
        }
        
        Collections.addAll(nodes, dataType,dataName,dataAttr,dataClass,dataExpr,dataLimits,dataPars,dataParsErr);
        return nodes;
    }
    
    public static List<HipoNode>  serializeGraphErrors(GraphErrors gr){
        
        List<HipoNode> nodes = new ArrayList<HipoNode>();
        
        HipoNode dataType   = new HipoNode(300,1,HipoNodeType.INT,1);
        HipoNode dataName   = new HipoNode(300,2,gr.getName());                
        HipoNode axisDataX  = new HipoNode(300,3,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataY  = new HipoNode(300,4,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataXE = new HipoNode(300,5,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataYE = new HipoNode(300,6,HipoNodeType.FLOAT,gr.getDataSize(0));

        for(int i = 0; i < gr.getDataSize(0); i++){
            axisDataX.setFloat(i, (float) gr.getDataX(i));
            axisDataY.setFloat(i, (float) gr.getDataY(i));
            axisDataXE.setFloat(i, (float) gr.getDataEX(i));
            axisDataYE.setFloat(i, (float) gr.getDataEY(i));
        }
        
        List<HipoNode> attrNodes = DataSetSerializer.serializeAttributes(300, gr.getAttributes());
        
        nodes.add(dataType);
        nodes.add(dataName);
        nodes.add(axisDataX);
        nodes.add(axisDataY);
        nodes.add(axisDataXE);
        nodes.add(axisDataYE);
        nodes.addAll(attrNodes);
        
        if(gr.getFunction()!=null){
            List<HipoNode> funcNodes = DataSetSerializer.serializeF1D(gr.getFunction());
            nodes.addAll(funcNodes);
        }
        
        return nodes;
    }
    
    
    public static List<HipoNode>  serializeDataGroupDescriptor(DataGroupDescriptor desc){
        List<HipoNode>  nodes = new ArrayList<>();
        
        HipoNode  header   = new HipoNode(1200,1,HipoNodeType.INT,3);
        HipoNode  descName = new HipoNode(1200,2,desc.getName());
        header.setInt(0, 120);
        header.setInt(1, desc.getCols());
        header.setInt(2, desc.getRows());
        int npads = desc.getCols()*desc.getRows();
        nodes.add(header);
        nodes.add(descName);
        
        for(int i = 0; i < npads; i++){
            String encoded = desc.getEncodedString(i);
            HipoNode node = new HipoNode(1200,20+i,encoded);
            nodes.add(node);
        }
        return nodes;
    }
    
    public static DataGroupDescriptor  deserializeDataGroupDescriptor(HipoEvent event){
        Map<Integer,HipoNode> nodes = event.getGroup(1200);
        String name = nodes.get(2).getString();
        int   ncols = nodes.get(1).getInt(1);
        int   nrows = nodes.get(1).getInt(2);
        DataGroupDescriptor desc = new DataGroupDescriptor(name,ncols,nrows);
        for(Map.Entry<Integer,HipoNode> entry : nodes.entrySet()){
            if(entry.getKey()>=20){
                int order = entry.getKey() - 20;
                desc.addEncoded(order, entry.getValue().getString());
            }
        }
        return desc;
    }
    
    public static List<HipoNode>  serializeAttributes(int group, DatasetAttributes attr){
        
        List<HipoNode>  attrNodes = new ArrayList<HipoNode>();
        
        HipoNode attrNode   = new HipoNode(group,31,HipoNodeType.BYTE,8);
        
        attrNode.setByte(0, (byte) attr.getLineColor());
        attrNode.setByte(1, (byte) attr.getLineWidth());
        attrNode.setByte(2, (byte) attr.getLineStyle());
        attrNode.setByte(3, (byte) attr.getMarkerColor());
        attrNode.setByte(4, (byte) attr.getMarkerSize());
        attrNode.setByte(5, (byte) attr.getMarkerStyle());
        attrNode.setByte(6, (byte) attr.getFillColor());
        attrNode.setByte(7, (byte) attr.getFillStyle());
        
        HipoNode attrTitle   = new HipoNode(group,32,attr.getTitle());
        HipoNode attrTitleX  = new HipoNode(group,33,attr.getTitleX());
        HipoNode attrTitleY  = new HipoNode(group,34,attr.getTitleY());
        HipoNode attrOptStat = new HipoNode(group,35,attr.getOptStat());
        
        Collections.addAll(attrNodes, attrNode,attrTitle,attrTitleX,attrTitleY,attrOptStat);
        return attrNodes;
    }
    
    public static HipoNode  serializeDataAttributes(int group, int item, DatasetAttributes attr){
        
        HipoNode attrNode   = new HipoNode(group,item,HipoNodeType.BYTE,8);
        
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
    
    public static DatasetAttributes deserializeAttributes(Map<Integer,HipoNode> dataNodes){
        DatasetAttributes  attr = new DatasetAttributes();
        HipoNode node = dataNodes.get(31);
        attr.setLineColor(node.getByte(0));
        attr.setLineWidth(node.getByte(1));
        attr.setLineStyle(node.getByte(2));
        attr.setMarkerColor(node.getByte(3));
        attr.setMarkerSize(node.getByte(4));
        attr.setMarkerStyle(node.getByte(5));
        attr.setFillColor(node.getByte(6));
        attr.setFillStyle(node.getByte(7));
        
        attr.setTitle(dataNodes.get(32).getString());
        attr.setTitleX(dataNodes.get(33).getString());
        attr.setTitleY(dataNodes.get(34).getString());
        attr.setOptStat(dataNodes.get(35).getString());
        
        return attr;
    }
    public static DatasetAttributes deserializeDataAttributes(HipoNode node){
        DatasetAttributes  attr = new DatasetAttributes();        
        attr.setLineColor(node.getByte(0));
        attr.setLineWidth(node.getByte(1));
        attr.setLineStyle(node.getByte(2));
        attr.setMarkerColor(node.getByte(3));
        attr.setMarkerSize(node.getByte(4));
        attr.setMarkerStyle(node.getByte(5));
        attr.setFillColor(node.getByte(6));
        attr.setFillStyle(node.getByte(7));
        return attr;
    }
    
    public static F1D deserializeF1D(HipoEvent event){
        Map<Integer,HipoNode> nodes = event.getGroup(500);
        //System.out.println(" F1D DESERIALIZER NODES SIZE = " + nodes.size());
        if(nodes.size()!=8) return null;
        String funcname   = nodes.get(2).getString();
        String expression = nodes.get(5).getString();
        F1D func = new F1D(funcname,expression,nodes.get(6).getFloat(0),nodes.get(6).getFloat(1));
        func.setNDF(nodes.get(1).getInt(1));
        func.setChiSquare(nodes.get(6).getFloat(2));
        for(int i = 0; i < func.getNPars(); i++){ 
            func.setParameter(i, nodes.get(7).getFloat(i));
            func.parameter(i).setError(nodes.get(8).getFloat(i));
        }
        System.out.println(func);
        return func;
    }
    
    public static GraphErrors deserializeGraphErrors(HipoEvent event){
        Map<Integer,HipoNode> nodes = event.getGroup(300);
        //if(nodes.size()!=6) return null;
        String grname   = nodes.get(2).getString();
        int npoints = nodes.get(3).getDataSize();
        GraphErrors graph = new GraphErrors(grname);
        
        for(int i = 0; i < npoints; i++){
            graph.addPoint(
                    nodes.get(3).getFloat(i),
                    nodes.get(4).getFloat(i),
                    nodes.get(5).getFloat(i),
                    nodes.get(6).getFloat(i)
                    );
        }
        
        DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes);
        graph.getAttributes().setAttributes(attr);
        
        F1D func = DataSetSerializer.deserializeF1D(event);
        if(func!=null) graph.setFunction(func);
        
        return graph;
    }
    
    public static H1F deserializeH1F(HipoEvent event){
        
        Map<Integer,HipoNode> nodes = event.getGroup(100);        
        //DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes.get(3));
                
        H1F h1 = new H1F();
        DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes);
        h1.getAttributes().setAttributes(attr);
        //DataSetSerializer.copyAttributes(attr, h1);
        
        h1.set(nodes.get(4).getInt(0),nodes.get(5).getFloat(0),nodes.get(5).getFloat(1));
        h1.setName(nodes.get(2).getString());
        for(int i = 0; i < h1.getXaxis().getNBins();i++) h1.setBinContent(i, nodes.get(6).getFloat(i));
        
        F1D func = DataSetSerializer.deserializeF1D(event);
        if(func!=null) h1.setFunction(func);
        return h1;
    }
    
    public static H2F deserializeH2F(HipoEvent event){
        
        Map<Integer,HipoNode> nodes = event.getGroup(200); 
        //DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes.get(3));
                
        H2F h2 = new H2F();
        DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes);
        h2.getAttributes().setAttributes(attr);
        //DataSetSerializer.copyAttributes(attr, h1);        
        //h2.set(nodes.get(4).getInt(0),nodes.get(5).getFloat(0),nodes.get(5).getFloat(1));
        
        h2.setName(nodes.get(2).getString());
        h2.set(
                nodes.get(3).getInt(0), 
                nodes.get(4).getFloat(0),nodes.get(4).getFloat(1),
                nodes.get(3).getInt(1), 
                nodes.get(4).getFloat(2),nodes.get(4).getFloat(3)
        );
        
        for(int i = 0; i < nodes.get(5).getDataSize();i++) h2.setDataBufferBin(i, nodes.get(5).getFloat(i));
        
        return h2;
    }
    
    public static List<HipoNode>  serializeDataSet(IDataSet ds){
        //List<HipoNode>  attributes = DataSetSerializer.serializeDataAttributes(ds.getAttributes());
        
        if(ds instanceof H1F){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeH1F( (H1F) ds);
            //attributes.addAll(dsNodes);
            return dsNodes;
        }
        
        if(ds instanceof H2F){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeH2F( (H2F) ds);
            return dsNodes;
        }
        
        if(ds instanceof GraphErrors){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeGraphErrors( (GraphErrors) ds);
            return dsNodes;
        }
        
        if(ds instanceof F1D){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeF1D( (F1D) ds);
            return dsNodes;
        }
        
        return null;
    }
    
    public static IDataSet deserializeDataSet(HipoEvent event){
        
        Map<Integer,HipoNode>  nodesGroup_h1 = event.getGroup(100);
        if(nodesGroup_h1.containsKey(1)==true){
            H1F h1 = DataSetSerializer.deserializeH1F(event);
            return h1;
        }
        Map<Integer,HipoNode>  nodesGroup_h2 = event.getGroup(200);
        if(nodesGroup_h2.containsKey(1)==true){
            H2F h2 = DataSetSerializer.deserializeH2F(event);
            return h2;
        }
        
        Map<Integer,HipoNode>  nodesGroup_gr = event.getGroup(300);
        if(nodesGroup_gr.containsKey(1)==true){
            GraphErrors gr = DataSetSerializer.deserializeGraphErrors(event);
            return gr;
        }
        
        Map<Integer,HipoNode>  nodesGroup_f1 = event.getGroup(500);
        if(nodesGroup_f1.containsKey(1)==true){
            Func1D f1 = DataSetSerializer.deserializeF1D(event);
            return f1;
        }
        
        //H1F h1 = DataSetSerializer.deserializeH1F(event);
        return null;
    }
}

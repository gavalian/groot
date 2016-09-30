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
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.Func1D;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoNode;
import org.jlab.hipo.data.HipoNodeType;

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
        HipoNode dataAttr   = DataSetSerializer.serializeDataAttributes(100, 3, h1.getAttributes());
        HipoNode axisBins   = new HipoNode(100,4, HipoNodeType.INT,1);
        HipoNode axisLimits = new HipoNode(100,5, HipoNodeType.FLOAT,2);
        HipoNode axisData   = new HipoNode(100,6, HipoNodeType.FLOAT, h1.getxAxis().getNBins());        
        
        axisBins.setInt(0, h1.getXaxis().getNBins());
        axisLimits.setFloat(0, (float) h1.getXaxis().min());
        axisLimits.setFloat(1, (float) h1.getXaxis().max());
        
        for(int i = 0; i < h1.getXaxis().getNBins(); i++) axisData.setFloat(i, (float) h1.getBinContent(i));
        Collections.addAll(nodes, dataType,dataName,dataAttr,axisBins,axisLimits,axisData);
        
        if(h1.getFunction()!=null){
            List<HipoNode> funcNodes = DataSetSerializer.serializeF1D(h1.getFunction());
            nodes.addAll(funcNodes);
        }
        return nodes;
    }
    
    public static List<HipoNode>  serializeH2F(H2F h2){
        
        List<HipoNode> nodes = new ArrayList<HipoNode>();

        HipoNode dataType   = new HipoNode(800,1,HipoNodeType.INT,2);
        //HipoNode dataType   = new HipoNode(800,2,h1.getName());
        HipoNode axisBins   = new HipoNode(800,3,HipoNodeType.INT,2);
        HipoNode axisLimits = new HipoNode(800,4,HipoNodeType.FLOAT,4);
        
        
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
        
        HipoNode dataType    = new HipoNode(500,1,HipoNodeType.INT,4);
        HipoNode dataName    = new HipoNode(500,2,func.getName());
        HipoNode dataAttr    = DataSetSerializer.serializeDataAttributes(500, 3, func.getAttributes());
        HipoNode dataClass   = new HipoNode(500,4,func.getClass().getName());
        HipoNode dataExpr    = new HipoNode(500,5,func.getExpression());
        HipoNode dataLimits  = new HipoNode(500,6,HipoNodeType.FLOAT,2);
        HipoNode dataPars    = new HipoNode(500,7,HipoNodeType.FLOAT,func.getNPars());
        
        dataLimits.setFloat(0, (float) func.getMin());
        dataLimits.setFloat(1, (float) func.getMax());
        
        for(int i = 0; i < func.getNPars(); i++) dataPars.setFloat(i, (float) func.getParameter(i));
        
        Collections.addAll(nodes, dataType,dataName,dataAttr,dataClass,dataExpr,dataLimits,dataPars);
        return nodes;
    }
    
    public static List<HipoNode>  serializeGraphErrors(GraphErrors gr){
        
        List<HipoNode> nodes = new ArrayList<HipoNode>();
        
        HipoNode dataType   = new HipoNode(800,1,HipoNodeType.INT,3);
        HipoNode dataName   = new HipoNode(800,2,gr.getName());
        
        
        HipoNode axisDataX  = new HipoNode(800,5,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataY  = new HipoNode(800,6,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataXE = new HipoNode(800,7,HipoNodeType.FLOAT,gr.getDataSize(0));
        HipoNode axisDataYE = new HipoNode(800,8,HipoNodeType.FLOAT,gr.getDataSize(0));

        for(int i = 0; i < gr.getDataSize(0); i++){
            axisDataX.setFloat(i, (float) gr.getDataX(i));
            axisDataY.setFloat(i, (float) gr.getDataY(i));
            axisDataXE.setFloat(i, (float) gr.getDataEX(i));
            axisDataYE.setFloat(i, (float) gr.getDataEY(i));
        }
        
        nodes.add(dataType);
        nodes.add(dataName);
        nodes.add(axisDataX);
        nodes.add(axisDataY);
        nodes.add(axisDataXE);
        nodes.add(axisDataYE);
        
        return nodes;
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
    
    public static DatasetAttributes deserializeAttributes(HipoNode node){
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
        if(nodes.size()!=7) return null;
        String funcname   = nodes.get(2).getString();
        String expression = nodes.get(5).getString();
        F1D func = new F1D(funcname,expression,nodes.get(6).getFloat(0),nodes.get(6).getFloat(1));
        for(int i = 0; i < func.getNPars(); i++) func.setParameter(i, nodes.get(7).getFloat(i));
        System.out.println(func);
        return func;
    }
    
    public static H1F deserializeH1F(HipoEvent event){
        
        Map<Integer,HipoNode> nodes = event.getGroup(100);        
        DatasetAttributes attr = DataSetSerializer.deserializeAttributes(nodes.get(3));
                
        H1F h1 = new H1F();
        
        DataSetSerializer.copyAttributes(attr, h1);
        
        h1.set(nodes.get(4).getInt(0),nodes.get(5).getFloat(0),nodes.get(5).getFloat(1));
        h1.setName(nodes.get(2).getString());
        for(int i = 0; i < h1.getXaxis().getNBins();i++) h1.setBinContent(i, nodes.get(6).getFloat(i));
        
        F1D func = DataSetSerializer.deserializeF1D(event);
        if(func!=null) h1.setFunction(func);
        return h1;
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

        }
        
        if(ds instanceof GraphErrors){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeGraphErrors( (GraphErrors) ds);

        }
        
        if(ds instanceof F1D){
            List<HipoNode>  dsNodes = DataSetSerializer.serializeF1D( (F1D) ds);
            
        }
        
        return null;
    }
    
    public static IDataSet deserializeDataSet(HipoEvent event){
        H1F h1 = DataSetSerializer.deserializeH1F(event);
        return h1;
    }
}

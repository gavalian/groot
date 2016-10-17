package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;

import org.jlab.groot.data.IDataSet;

public class DatasetDescriptor {
    
    public static int DESCRIPTOR_H1 = 1;
    public static int DESCRIPTOR_H2 = 2;
    public static int DESCRIPTOR_GRXY_XY = 3;
    public static int DESCRIPTOR_GRXY_XY_EY = 4;
    public static int DESCRIPTOR_GRXY_XY_EX_EY = 5;
    
    
    String                      descName = "somename";
    List<IDataSet>              descDataset = new ArrayList<IDataSet>();
    List<TreeCut>               treeCuts = new ArrayList<TreeCut>();
    List<TreeExpression> treeExpressions = new ArrayList<TreeExpression>();
    private int           descriptorType = 0;
    
    public DatasetDescriptor(String name, int type){
        this.descName = name;
        
    }
        
    public DatasetDescriptor(String name, int nbins, double min, double max, String exp, ITree tree){
        this.descName = name;
        this.setExpression(exp, tree);
        if(treeExpressions.size()==1){
            H1F h1 = new H1F(name,nbins,min,max);
            this.descDataset.add(h1);
        }
    }
    
    public DatasetDescriptor(String name, int nbinsX, double minX, double maxX, 
            int nbinsY, double minY, double maxY, String exp, ITree tree){
        
        this.descName = name;
        this.setExpression(exp, tree);
        if(treeExpressions.size()==2){
            H2F h2 = new H2F(name,nbinsX,minX,maxX,nbinsY,minY,maxY);
            this.descDataset.add(h2);
        }
    }
    
    public DatasetDescriptor(String name, String exp, ITree tree){
        
        this.descName = name;
        this.setExpression(exp, tree);        
        if(treeExpressions.size()==2){
            
        }
    }
    
    public final void setExpression(String expressions, ITree tree){
        
        String[] tokens = expressions.split(":");
        treeExpressions.clear();
        for(String item : tokens){
            try {
                TreeExpression exp = new TreeExpression(item,tree.getListOfBranches());
                treeExpressions.add(exp);
            } catch (Exception e){
                System.out.println("ERROR : parsing expression ["+item+"]");
            }
        }
        int nexp = treeExpressions.size();
        switch (nexp){
            case 1 : descriptorType = DatasetDescriptor.DESCRIPTOR_H1; break;
            case 2 : descriptorType = DatasetDescriptor.DESCRIPTOR_H2; break;
            case 3 : descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY; break;
            case 4 : descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY; break;
            default: descriptorType = 0;
        }        
    }
    
    public void addCut(String name, String cutExp, ITree tree){
        TreeCut cut = new TreeCut(name,cutExp,tree.getListOfBranches());
    }
    
    public void addCut(TreeCut cut){
        this.treeCuts.add(cut);
    }
    
    public void fill(ITree tree){
        
        boolean cutsPassed = true;
        for(TreeCut cut : this.treeCuts){
            if(cut.isValid(tree)==false) return;
        }
        
        if(descriptorType==DatasetDescriptor.DESCRIPTOR_H1){
            double value = treeExpressions.get(0).getValue(tree);
            H1F h1 = (H1F) this.descDataset;
            h1.fill(value);
            return;
        }
        
        if(descriptorType==DatasetDescriptor.DESCRIPTOR_H2){
            double valueX = treeExpressions.get(0).getValue(tree);
            double valueY = treeExpressions.get(1).getValue(tree);
            H2F h2 = (H2F) this.descDataset;
            h2.fill(valueX,valueY);
            return;
        }
        
        if(descriptorType==DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY){
            double x  = treeExpressions.get(0).getValue(tree);
            double y  = treeExpressions.get(1).getValue(tree);
            double ey = treeExpressions.get(2).getValue(tree);
            GraphErrors graph = (GraphErrors) this.descDataset;
            graph.addPoint(x, y, 0.0, ey);
        }
        
        if(descriptorType==DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY){
            double x  = treeExpressions.get(0).getValue(tree);
            double y  = treeExpressions.get(1).getValue(tree);
            double ex = treeExpressions.get(2).getValue(tree);
            double ey = treeExpressions.get(3).getValue(tree);
            GraphErrors graph = (GraphErrors) this.descDataset;
            graph.addPoint(x, y, ex, ey);
        }        
         
    }
    
    public IDataSet getDataSet(){return descDataset.get(0);}
    
}

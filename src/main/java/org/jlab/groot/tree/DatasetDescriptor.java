package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;

import org.jlab.groot.data.IDataSet;

public class DatasetDescriptor extends AbstractDescriptor {

	public static int DESCRIPTOR_H1 = 1;
	public static int DESCRIPTOR_H2 = 2;
	public static int DESCRIPTOR_GRXY_XY = 3;
	public static int DESCRIPTOR_GRXY_XY_EY = 4;
	public static int DESCRIPTOR_GRXY_XY_EX_EY = 5;

	String descName = "Dataset Name";
	List<IDataSet> descDataset = new ArrayList<IDataSet>();
	List<TreeCut> treeCuts = new ArrayList<TreeCut>();
	List<TreeExpression> treeExpressions = new ArrayList<TreeExpression>();
	private int descriptorType = 0;
	String expressionX = "";
	String expressionY = "";
	String expressionXerr = "";
	String expressionYerr = "";
	int nbinsX = 100;
	double minX = 0.0;
	double maxX = 1.0;

	int nbinsY = 100;
	double minY = 0.0;
	double maxY = 1.0;

	public DatasetDescriptor(String name, int type) {
		this.descName = name;
		this.descriptorType = type;
		GraphErrors graph = new GraphErrors(name);
		this.descDataset.add(graph);

	}

	public DatasetDescriptor(String name, int nbins, double min, double max, String exp, ITree tree) {
		this.descName = name;
		this.setExpression(exp, tree);
		this.nbinsX = nbins;
		this.minX = min;
		this.maxX = max;
		this.expressionX = exp;
		this.initDatasets();
	}

	public DatasetDescriptor(String name, int nbinsX, double minX, double maxX, int nbinsY, double minY, double maxY,
			String exp, ITree tree) {

		this.descName = name;
		this.setExpression(exp, tree);
		this.nbinsX = nbinsX;
		this.minX = minX;
		this.maxX = maxX;
		this.nbinsY = nbinsY;
		this.minY = minY;
		this.maxY = maxY;
		if (treeExpressions.size() == 2) {
			this.expressionX = treeExpressions.get(0).treeExpression;
			this.expressionY = treeExpressions.get(1).treeExpression;
		} else {
			this.expressionX = exp;
		}
		this.initDatasets();
	}

	public DatasetDescriptor(String name, String exp, ITree tree) {

		this.descName = name;
		this.setExpression(exp, tree);
		if (treeExpressions.size() == 2) {
			this.descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY;
			this.expressionX = treeExpressions.get(0).treeExpression;
			this.expressionY = treeExpressions.get(1).treeExpression;
		}
		if (treeExpressions.size() == 3) {
			this.descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY;
			this.expressionX = treeExpressions.get(0).treeExpression;
			this.expressionY = treeExpressions.get(1).treeExpression;
			this.expressionYerr = treeExpressions.get(2).treeExpression;

		}
		if (treeExpressions.size() == 4) {
			this.descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY;
			this.expressionX = treeExpressions.get(0).treeExpression;
			this.expressionY = treeExpressions.get(1).treeExpression;
			this.expressionXerr = treeExpressions.get(2).treeExpression;
			this.expressionYerr = treeExpressions.get(3).treeExpression;
		}
		GraphErrors graph = new GraphErrors(name);
		this.descDataset.add(graph);
	}

	public final void setExpression(String expressions, ITree tree) {
		String[] tokens = expressions.split(":");
		treeExpressions.clear();
		for (String item : tokens) {
			try {
				TreeExpression exp = new TreeExpression(item, tree.getListOfBranches());
				treeExpressions.add(exp);
			} catch (Exception e) {
				System.out.println("ERROR : parsing expression [" + item + "]");
			}
		}
		int nexp = treeExpressions.size();
		switch (nexp) {
			case 1 :
				descriptorType = DatasetDescriptor.DESCRIPTOR_H1;
				expressionX = treeExpressions.get(0).treeExpression;
				break;
			case 2 :
				descriptorType = DatasetDescriptor.DESCRIPTOR_H2;
				expressionX = treeExpressions.get(0).treeExpression;
				expressionY = treeExpressions.get(1).treeExpression;
				break;
			case 3 :
				descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY;
				expressionX = treeExpressions.get(0).treeExpression;
				expressionY = treeExpressions.get(1).treeExpression;
				expressionYerr = treeExpressions.get(2).treeExpression;

				break;
			case 4 :
				descriptorType = DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY;
				expressionX = treeExpressions.get(0).treeExpression;
				expressionY = treeExpressions.get(1).treeExpression;
				expressionXerr = treeExpressions.get(2).treeExpression;
				expressionYerr = treeExpressions.get(3).treeExpression;
				break;
			default :
				descriptorType = 0;
		}
	}

	public void addCut(String name, String cutExp, ITree tree) {
		TreeCut cut = new TreeCut(name, cutExp, tree.getListOfBranches());
	}

	public void addCut(TreeCut cut) {
		this.treeCuts.add(cut);
	}

	public void fill(ITree tree) {
		double eventWeight = 1.0;
		boolean cutsPassed = true;
		//System.out.println("Number of TreeCuts"+this.treeCuts.size());
		for (TreeCut cut : this.treeCuts) {
			eventWeight*=cut.isValid(tree);
			//System.out.println("Cut"+cut.cutExpression+" "+cut.isValid(tree)+" "+eventWeight);
			/*if (cut.isValid(tree) == false)
				return;*/
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H1) {
			double value = treeExpressions.get(0).getValue(tree);
			H1F h1 = (H1F) this.descDataset.get(0);
			h1.fill(value,eventWeight);
			return;
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H2) {
			double valueX = treeExpressions.get(0).getValue(tree);
			double valueY = treeExpressions.get(1).getValue(tree);
			H2F h2 = (H2F) this.descDataset.get(0);
			h2.fill(valueX, valueY,eventWeight);
			return;
		}
		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, 0.0, 0.0);
			}
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ey = treeExpressions.get(2).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, 0.0, ey);
			}
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ex = treeExpressions.get(2).getValue(tree);
			double ey = treeExpressions.get(3).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, ex, ey);
			}
		}
	}

	public void initDatasets() {
		this.descDataset.clear();
		System.out.println("initDatasets Descriptortype "+descriptorType + " "+ treeExpressions.size());
		if (this.descriptorType == DatasetDescriptor.DESCRIPTOR_H1) {
			if (treeExpressions.size() == 1) {
				H1F h1 = new H1F(descName, nbinsX, minX, maxX);
				this.descDataset.add(h1);
			}
		}
		if (this.descriptorType == DatasetDescriptor.DESCRIPTOR_H2) {
			if (treeExpressions.size() == 2) {
				H2F h2 = new H2F(descName, nbinsX, minX, maxX, nbinsY, minY, maxY);
				this.descDataset.add(h2);
			}
		}
		System.out.println("Initialized datasets:"+descDataset.size());
	}

	public IDataSet getDataSet() {
		return descDataset.get(0);
	}

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}

	public List<IDataSet> getDescDataset() {
		return descDataset;
	}

	public void setDescDataset(List<IDataSet> descDataset) {
		this.descDataset = descDataset;
	}

	public List<TreeCut> getTreeCuts() {
		return treeCuts;
	}

	public void setTreeCuts(List<TreeCut> treeCuts) {
		this.treeCuts = treeCuts;
	}

	public List<TreeExpression> getTreeExpressions() {
		return treeExpressions;
	}

	public void setTreeExpressions(List<TreeExpression> treeExpressions) {
		this.treeExpressions = treeExpressions;
	}

	public int getDescriptorType() {
		return descriptorType;
	}

	public void setDescriptorType(int descriptorType) {
		this.descriptorType = descriptorType;
	}

	public String getExpressionX() {
		return expressionX;
	}

	public void setExpressionX(String expressionX) {
		this.expressionX = expressionX;
	}

	public String getExpressionY() {
		return expressionY;
	}

	public void setExpressionY(String expressionY) {
		this.expressionY = expressionY;
	}

	public String getExpressionXerr() {
		return expressionXerr;
	}

	public void setExpressionXerr(String expressionXerr) {
		this.expressionXerr = expressionXerr;
	}

	public String getExpressionYerr() {
		return expressionYerr;
	}

	public void setExpressionYerr(String expressionYerr) {
		this.expressionYerr = expressionYerr;
	}

	public int getNbinsX() {
		return nbinsX;
	}

	public void setNbinsX(int nbinsX) {
		this.nbinsX = nbinsX;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public int getNbinsY() {
		return nbinsY;
	}

	public void setNbinsY(int nbinsY) {
		this.nbinsY = nbinsY;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	@Override
	public void processTreeEvent(Tree tree) {
		/*
		boolean cutsPassed = true;
		for (TreeCut cut : this.treeCuts) {
			if (cut.isValid(tree) == false)
				return;
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H1) {
			double value = treeExpressions.get(0).getValue(tree);
			H1F h1 = (H1F) this.descDataset.get(0);
			h1.fill(value);
			return;
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H2) {
			double valueX = treeExpressions.get(0).getValue(tree);
			double valueY = treeExpressions.get(1).getValue(tree);
			H2F h2 = (H2F) this.descDataset.get(0);
			h2.fill(valueX, valueY);
			return;
		}
		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			graph.addPoint(x, y, 0.0, 0.0);
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ey = treeExpressions.get(2).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			graph.addPoint(x, y, 0.0, ey);
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ex = treeExpressions.get(2).getValue(tree);
			double ey = treeExpressions.get(3).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			graph.addPoint(x, y, ex, ey);
		}*/
		
		double eventWeight = 1.0;
		boolean cutsPassed = true;
		for (TreeCut cut : this.treeCuts) {
			eventWeight*=cut.isValid(tree);
			/*if (cut.isValid(tree) == false)
				return;*/
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H1) {
			double value = treeExpressions.get(0).getValue(tree);
			H1F h1 = (H1F) this.descDataset.get(0);
			h1.fill(value,eventWeight);
			return;
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_H2) {
			double valueX = treeExpressions.get(0).getValue(tree);
			double valueY = treeExpressions.get(1).getValue(tree);
			H2F h2 = (H2F) this.descDataset.get(0);
			h2.fill(valueX, valueY,eventWeight);
			return;
		}
		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, 0.0, 0.0);
			}
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ey = treeExpressions.get(2).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, 0.0, ey);
			}
		}

		if (descriptorType == DatasetDescriptor.DESCRIPTOR_GRXY_XY_EX_EY) {
			double x = treeExpressions.get(0).getValue(tree);
			double y = treeExpressions.get(1).getValue(tree);
			double ex = treeExpressions.get(2).getValue(tree);
			double ey = treeExpressions.get(3).getValue(tree);
			GraphErrors graph = (GraphErrors) this.descDataset.get(0);
			if(eventWeight>0.0){
				graph.addPoint(x, y, ex, ey);
			}
		}
	}

	@Override
	public JDialog edit(Tree tree) {
		return null;
	}

	public void clearCuts() {
		this.treeCuts.clear();
	}

}

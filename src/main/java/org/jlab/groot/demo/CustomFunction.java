package org.jlab.groot.demo;

import org.jlab.groot.math.Func1D;

public class CustomFunction extends Func1D{

	public CustomFunction(String name, double min, double max) {
		super(name, min, max);
	}
	
	//Simple polynomial function of any order
	@Override
	public double evaluate(double x){
		double sum = 0.0;
		for(int i=0; i<this.getNPars(); i++){
			sum += this.getParameter(i)*Math.pow(x,i);
		}
		return sum;
	}
}

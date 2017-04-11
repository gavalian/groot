package org.jlab.groot.fitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.Axis;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.ui.ProgressBar;

public class ParallelSliceFitter {
	H2F histogram = null;
	int nthreads = Runtime.getRuntime().availableProcessors();
	ArrayList<H1F> slices = null;
	ArrayList<FitThread> threads = new ArrayList<FitThread>();
	F1D fitsResults[];
	
	public H2F getHistogram() {
		return histogram;
	}

	public void setHistogram(H2F histogram) {
		this.histogram = histogram;
	}

	public int getNthreads() {
		return nthreads;
	}

	public void setNthreads(int nthreads) {
		this.nthreads = nthreads;
	}

	Axis axis = null;

	public ParallelSliceFitter() {
	}

	public ParallelSliceFitter(H2F histogram) {
		this.histogram = histogram;
	}

	public void fitSlicesY() {
		slices = histogram.getSlicesY();
		axis = histogram.getYAxis();
		fit();
	}

	public void fitSlicesX() {
		slices = histogram.getSlicesX();
		axis = histogram.getXAxis();
		fit();
	}
	
	public GraphErrors getMeanSlices(){
		GraphErrors graph = new GraphErrors();
		for(int i=0; i<slices.size(); i++){
			graph.addPoint(axis.getBinCenter(i),fitsResults[i].getParameter(1),0,fitsResults[i].parameter(1).error());
		}
		return graph;
	}
	
	public GraphErrors getSigmaSlices(){
		GraphErrors graph = new GraphErrors();
		for(int i=0; i<slices.size(); i++){
			graph.addPoint(axis.getBinCenter(i),fitsResults[i].getParameter(2),0,fitsResults[i].parameter(2).error());
		}
		return graph;
	}
	
	public GraphErrors getAmpSlices(){
		GraphErrors graph = new GraphErrors();
		for(int i=0; i<slices.size(); i++){
			graph.addPoint(axis.getBinCenter(i),fitsResults[i].getParameter(0),0,fitsResults[i].parameter(0).error());
		}
		return graph;
	}

	private void fit() {
		List<Callable<Object>> taskList = new ArrayList<Callable<Object>>(slices.size());
		ArrayList<Future<Object>> tasks = new ArrayList<Future<Object>>();
		fitsResults = new F1D[slices.size()];
		for (int i=0; i<slices.size(); i++) {
			H1F slice = slices.get(i);
			ResultSetter setter = new ResultSetter() {
				public void setResult(H1F histogram, F1D fit,int fitNumber) {
					fitsResults[fitNumber] = fit;
				}
			};
			FitThread temp = new FitThread();
			temp.setN(i);
			temp.setHistogram(slice);
			temp.setResultSetter(setter);
			temp.setFitter(new F1D("f1", "[amp]*gaus(x,[mean],[sigma])", axis.min(), axis.max()));
			threads.add(temp);
		}

		ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);
		for (FitThread thread : threads) {
			taskList.add(Executors.callable(thread));
			@SuppressWarnings("unchecked")
			Future<Object> x = (Future<Object>) threadPool.submit(thread);
			tasks.add(x);
		}
		int ncomplete = 0;
		int ntotal = 0;
		System.out.println("In parallel fitter");
		if (tasks != null) {
			ProgressBar bar = new ProgressBar();
			ntotal = tasks.size();
			bar.update(ncomplete, ntotal);
			while (ncomplete < ntotal) {
				ncomplete = 0;
				for (int i = 0; i < ntotal; i++) {
					if (tasks.get(i).isDone()) {
						ncomplete++;
					}
				}
				bar.update(ncomplete, ntotal);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for (FitThread thread : threads) {
			try {
				thread.join();
				//System.out.println("Thread " + thread.getN() + " has been joined successfully.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threadPool.shutdown();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		canvas.divide(2, 2);
		frame.setSize(800, 500);
		Random rand = new Random();
		H2F histogram2d = new H2F("h2","",100,0,4,100,0,4);
		for(int i=0; i<8000000; i++){
			double x = 1.5+rand.nextGaussian();
			double y = 2.5+2*rand.nextGaussian();
			histogram2d.fill(x, y);
		}
		//H2F histogram2d = FunctionFactory.randomGausian2D(200, 0.4, 7.6, 800000, 3.3, 0.8);
		histogram2d.setTitleX("Randomly Generated Function");
		histogram2d.setTitleY("Randomly Generated Function");
		canvas.getPad(0).setTitle("Histogram2D Demo");
		canvas.draw(histogram2d);
		canvas.setFont("HanziPen TC");
		canvas.setTitleSize(32);
		canvas.setAxisTitleSize(24);
		canvas.setAxisLabelSize(18);
		canvas.setStatBoxFontSize(18);
		
		ParallelSliceFitter fitter = new ParallelSliceFitter(histogram2d);
		fitter.fitSlicesY();
		GraphErrors meanY = fitter.getMeanSlices();
		fitter.fitSlicesX();
		GraphErrors meanX = fitter.getMeanSlices();
		canvas.cd(1);
		canvas.draw(meanY);
		canvas.cd(2);
		canvas.draw(meanX);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private class FitThread extends Thread {
		F1D fitFunction = null;
		private ResultSetter setter;
		private H1F histogram;
		private int n = 0;
		private String options = "NRQ";
		private double[] parameters;


		public void setFitter(F1D fitFunction) {
			this.fitFunction = fitFunction;
		}
		public void setResultSetter(ResultSetter setter) {
			this.setter = setter;
		}

		public void setParameters(double[] params) {
			this.fitFunction.setParameters(params);
		}
		public void setN(int n) {
			this.n = n;
		}
		public void setOptions(String options) {
			this.options = options;
		}
		public void setHistogram(H1F histogram) {
			this.histogram = histogram;
		}

	public void run() {
		IDataSet currentDataset = histogram;
		double currentRangeMin = fitFunction.getMin();
		double currentRangeMax = fitFunction.getMax();
		
		for (int i = 0; i < fitFunction.getNPars(); i++) {
			if (i == 0) {
				fitFunction.setParameter(0,
						getMaxYIDataSet(currentDataset, currentRangeMin, currentRangeMax));
			} else if (i == 1) {
				fitFunction.setParameter(1,
						getMeanIDataSet(currentDataset, currentRangeMin, currentRangeMax));
			} else if (i == 2) {
				fitFunction.setParameter(2,
						getRMSIDataSet(currentDataset, currentRangeMin, currentRangeMax));
			} else if (i == 3) {
				fitFunction.setParameter(3,
						getAverageHeightIDataSet(currentDataset, currentRangeMin, currentRangeMax));
			} else if (i > 3) {
				fitFunction.setParameter(i, 1.0);
			}
			//System.out.println("Paramter " + i + " =" + fitFunction.getParameter(i));
		}
			DataFitter.fit(fitFunction, currentDataset, options);
			setter.setResult(histogram, fitFunction, n);
	}

		public int getN() {
			return n;
		}
		private double getMeanIDataSet(IDataSet data, double min, double max) {
			int nsamples = 0;
			double sum = 0;
			double nEntries = 0;
			for (int i = 0; i < data.getDataSize(0); i++) {
				double x = data.getDataX(i);
				double y = data.getDataY(i);
				if (x > min && x < max && y != 0) {
					nsamples++;
					sum += x * y;
					nEntries += y;
				}
			}
			return sum / (double) nEntries;
		}

		private double getRMSIDataSet(IDataSet data, double min, double max) {
			int nsamples = 0;
			double mean = getMeanIDataSet(data, min, max);
			double sum = 0;
			double nEntries = 0;

			for (int i = 0; i < data.getDataSize(0); i++) {
				double x = data.getDataX(i);
				double y = data.getDataY(i);
				if (x > min && x < max && y != 0) {
					nsamples++;
					sum += Math.pow(x - mean, 2) * y;
					nEntries += y;
				}
			}
			return Math.sqrt(sum / (double) nEntries);
		}

		private double getAverageHeightIDataSet(IDataSet data, double min, double max) {
			int nsamples = 0;
			double sum = 0;
			for (int i = 0; i < data.getDataSize(0); i++) {
				double x = data.getDataX(i);
				double y = data.getDataY(i);
				if (x > min && x < max && y != 0) {
					nsamples++;
					sum += y;
				}
			}
			return sum / (double) nsamples;
		}

		private double getMaxXIDataSet(IDataSet data, double min, double max) {
			double max1 = 0;
			double xMax = 0;
			for (int i = 0; i < data.getDataSize(0); i++) {
				double x = data.getDataX(i);
				double y = data.getDataY(i);
				if (x > min && x < max && y != 0) {
					if (y > max1) {
						max1 = y;
						xMax = x;
					}
				}
			}
			return xMax;
		}
		private double getMaxYIDataSet(IDataSet data, double min, double max) {
			double max1 = 0;
			double xMax = 0;
			for (int i = 0; i < data.getDataSize(0); i++) {
				double x = data.getDataX(i);
				double y = data.getDataY(i);
				if (x > min && x < max && y != 0) {
					if (y > max1) {
						max1 = y;
						xMax = x;
					}
				}
			}
			return max1;
		}
	}

	public interface ResultSetter {
		public void setResult(H1F histogram, F1D fit, int fitNumber);
	}
}

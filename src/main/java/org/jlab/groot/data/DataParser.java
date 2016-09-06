package org.jlab.groot.data;

import java.util.Scanner;

public class DataParser {
	String data = "";
	int nCols = 0;
	public int DATAY = 0;
	public int DATAX = 1;
	public int DATAYERR = 2;
	public int DATAXERR = 3;

	public DataParser(String data) {
		this.data = data;
	}

	public GraphErrors getGraphErrors() {
		GraphErrors graph = new GraphErrors();
		Scanner scanner = new Scanner(data);
		int lineCounter = 0;
		int numElementsForEachLine = 0;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			int numElements = 0;
			while (lineScanner.hasNext()) {
				double d = 0.0;
				if (lineScanner.hasNextInt()) {
					d = (double) lineScanner.nextInt();
					numElements++;
				} else if (lineScanner.hasNextDouble()) {
					d = lineScanner.nextDouble();
					numElements++;
				} else {
					d = 0.0;
					String idk = lineScanner.next();
				}

			}
			if (lineCounter == 0) {
				numElementsForEachLine = numElements;
			}

			Scanner lineScanner2 = new Scanner(line);
			double x = 0.0;
			double y = 0.0;
			double xerr = 0.0;
			double yerr = 0.0;
			int counter = 0;
			while (lineScanner2.hasNext()) {
				double d = 0.0;
				if (lineScanner2.hasNextInt()) {
					d = (double) lineScanner2.nextInt();
					numElements++;
				} else if (lineScanner2.hasNextDouble()) {
					d = lineScanner2.nextDouble();
					numElements++;
				} else {
					d = 0.0;
					String idk = lineScanner2.next();
				}
				if (counter == 0) {
					x = d;
				} else if (counter == 1) {
					y = d;
				} else if (counter == 2){
					if(numElementsForEachLine==3){
						yerr = d;
					}else{
						xerr = d;
					}
				}else if(counter == 3){
					yerr = d;
				}
				counter++;
			}
			graph.addPoint(x, y, xerr, yerr);

			lineCounter++;

		}
		return graph;
	}
}

package org.jlab.groot.base;

public class GStyle {
	static DatasetAttributes H1FAttributes 			= new DatasetAttributes(DatasetAttributes.HISTOGRAM);
	static DatasetAttributes H2FAttributes 			= new DatasetAttributes(DatasetAttributes.HISTOGRAM2D);
	static DatasetAttributes GraphErrorsAttributes 	= new DatasetAttributes(DatasetAttributes.GRAPHERRORS);
	static DatasetAttributes FunctionAttributes 	= new DatasetAttributes(DatasetAttributes.FUNCTION);
	static AxisAttributes AxisAttributesX 			= new AxisAttributes();
	static AxisAttributes AxisAttributesY 			= new AxisAttributes();
	static AxisAttributes AxisAttributesZ 			= new AxisAttributes();

	public static AxisAttributes getAxisAttributesZ() {
		return AxisAttributesZ;
	}

	public static void setAxisAttributesZ(AxisAttributes axisAttributesZ) {
		AxisAttributesZ = axisAttributesZ;
	}

	public static AxisAttributes getAxisAttributesX() {
		return AxisAttributesX;
	}

	public static void setAxisAttributesX(AxisAttributes axisAttributesX) {
		AxisAttributesX = axisAttributesX;
	}

	public static AxisAttributes getAxisAttributesY() {
		return AxisAttributesY;
	}

	public static void setAxisAttributesY(AxisAttributes axisAttributesY) {
		AxisAttributesY = axisAttributesY;
	}

	public static void setH1FAttributes(DatasetAttributes h1fAttributes) {
		H1FAttributes = h1fAttributes;
	}

	public static void setH2FAttributes(DatasetAttributes h2fAttributes) {
		H2FAttributes = h2fAttributes;
	}

	public static void setGraphErrorsAttributes(DatasetAttributes graphErrorsAttributes) {
		GraphErrorsAttributes = graphErrorsAttributes;
	}

	public static void setFunctionAttributes(DatasetAttributes functionAttributes) {
		FunctionAttributes = functionAttributes;
	}

	public static DatasetAttributes getH1FAttributes() {
		return H1FAttributes;
	}
	
	public static DatasetAttributes getH2FAttributes() {
		return H2FAttributes;
	}
	
	public static DatasetAttributes getGraphErrorsAttributes() {
		return GraphErrorsAttributes;
	}
	
	public static DatasetAttributes getFunctionAttributes() {
		return FunctionAttributes;
	}
}

package org.jlab.groot.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;

public class GStyle {
    
	static DatasetAttributes H1FAttributes 			= new DatasetAttributes(DatasetAttributes.HISTOGRAM);
	static DatasetAttributes H2FAttributes 			= new DatasetAttributes(DatasetAttributes.HISTOGRAM2D);
	static DatasetAttributes GraphErrorsAttributes 	= new DatasetAttributes(DatasetAttributes.GRAPHERRORS);
	static DatasetAttributes FunctionAttributes 	= new DatasetAttributes(DatasetAttributes.FUNCTION);
	static AxisAttributes AxisAttributesX 			= new AxisAttributes(AxisAttributes.X);
	static AxisAttributes AxisAttributesY 			= new AxisAttributes(AxisAttributes.Y);
	static AxisAttributes AxisAttributesZ 			= new AxisAttributes(AxisAttributes.Z);
        private static String workingDirectory     = new File(System.getProperty("user.home"), "Desktop").getAbsolutePath();
        
        public static float[]  dashPattern1 = new float[]{10.0f,5.0f};
        public static float[]  dashPattern2 = new float[]{10.0f,5.0f,2.0f,5.0f};
        public static float[]  dashPattern3 = new float[]{2.0f,5.0f,2.0f,5.0f};
        static TColorPalette palette = new TColorPalette();

        public static Integer    graphicsFrameLineWidth = 2;
        public static Color      canvasBackgroundColor = Color.WHITE;
        
        public static void setPalette(String palName) {
            palette.setPalette(palName);
        }

        public static TColorPalette getPalette(){
            return new TColorPalette(palette);
        }

        
        public static void setCanvasBackgroundColor(Color c){
            canvasBackgroundColor = c;
        }
        
        public static Color getCanvasBackgroundColor(){
            return canvasBackgroundColor;
        }
        
        public static BasicStroke getStroke(int type){
            switch (type){
                
                case 0 : return new BasicStroke(1, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f);
                case 1: return new BasicStroke(1, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern2, 0.0f);
                case 2: return new BasicStroke(1, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern3, 0.0f);
                default : return new BasicStroke(1, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 20.0f, dashPattern1, 0.0f);
            }
        }
        
        public static void setGraphicsFrameLineWidth(int lineWidth){
            graphicsFrameLineWidth = lineWidth;
        }
        
        public static int getGraphicsFrameLineWidth(){
            return graphicsFrameLineWidth;
        }
        
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

	public static String getWorkingDirectory() {
		return workingDirectory;
	}

	public static void setWorkingDirectory(String workingDirectory) {
		GStyle.workingDirectory = workingDirectory;
	}

        public static void setBackgroundColor(Color bgColor) {
            palette.setBackgroundColor(bgColor);
        }
}

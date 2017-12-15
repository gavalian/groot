package org.jlab.groot.base;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.ui.JComboCheckBox;

import net.miginfocom.swing.MigLayout;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gavalian
 */
public class DatasetAttributes implements Cloneable {
    private int lineColor = 1;
    private int lineWidth = 3;
    private int lineStyle = 1;
    private int markerColor = 1;
    private int markerSize = 1;
    private int markerStyle = 0;
    private int fillColor   = 0;
    private int fillStyle   = 1;
    private int datasetType  = 0;
    
    public final static int HISTOGRAM = 0;
    public final static int HISTOGRAM2D = 1;
    public final static int GRAPHERRORS = 2;
    public final static int FUNCTION = 3;
    
    private boolean drawAxis = false;
    public String optStat = "0";

    
    private String stringTitleX = "";
    private String stringTitleY = "";
    private String stringTitle  = "";
    private String drawOptions = "";
    
    public DatasetAttributes(int datasetType){
        this.datasetType = datasetType;
        init(datasetType);
    }
    
    public void setAttributes(DatasetAttributes attr){
    	datasetType = attr.getDatasetType();
    	lineColor = attr.getLineColor();
    	lineWidth = attr.getLineWidth();
    	lineStyle = attr.getLineStyle();
    	markerColor = attr.getMarkerColor();
    	markerStyle = attr.getMarkerStyle();
    	markerSize = attr.getMarkerSize();
    	fillColor = attr.getFillColor();
    	fillStyle = attr.getFillStyle();
    	optStat = attr.getOptStat();
    	drawAxis = attr.isDrawAxis();
    	stringTitleX = attr.getTitleX();
    	stringTitleY = attr.getTitleY();
    	stringTitle = attr.getTitle();
    }
    
    private void init(int datasetType2) {
    	if(datasetType2 == DatasetAttributes.HISTOGRAM){
            this.setLineWidth(2);
            this.setLineColor(1);
            this.setLineStyle(1);
            this.setFillColor(-1);
            this.setFillStyle(0);
    	}
    	if(datasetType2 == DatasetAttributes.HISTOGRAM2D){
    		this.setLineWidth(1);
            this.setLineColor(1);
            this.setLineStyle(1);
            this.setFillColor(-1);
            this.setFillStyle(0);
    	}
    	if(datasetType2 == DatasetAttributes.FUNCTION){
    		this.setLineWidth(1);
            this.setLineColor(1);
            this.setLineStyle(1);
            this.setFillColor(-1);
            this.setFillStyle(0);
    	}
    	if(datasetType2 == DatasetAttributes.GRAPHERRORS){
    		this.setLineColor(1);
    		this.setLineWidth(2);
    		this.setLineStyle(1);
    		this.setMarkerColor(1);
    		this.setMarkerSize(6);
    		this.setMarkerStyle(0);
    	}
	}
	public DatasetAttributes(){}
    
	public void setTitleX(String title){ this.stringTitleX = title;}
    public void setTitleY(String title){ this.stringTitleY = title;}
    public void setTitle(String title){ this.stringTitle = title;}
    
    
    public void setLineColor(int color){ this.lineColor = color;}
    public void setLineWidth(int width){ this.lineWidth = width;}
    public void setLineStyle(int style){ this.lineStyle = style;}
    public void setMarkerColor(int color){ this.markerColor = color;}
    public void setMarkerSize(int size){ this.markerSize = size;}
    public void setMarkerStyle(int style){ this.markerStyle = style;}
    public void setFillColor(int color){ this.fillColor = color;}
    public void setFillStyle(int style){ this.fillStyle = style;}

    
    public boolean isDrawAxis() {
		return drawAxis;
	}

	public void setDrawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}

        public int  getLineColor(){return this.lineColor;}
        public int  getLineWidth(){return this.lineWidth;}
        public int  getLineStyle(){return this.lineStyle;}
        public int  getMarkerColor(){return this.markerColor;}
        public int  getMarkerSize(){return this.markerSize;}
        public int  getMarkerStyle(){return this.markerStyle;}
        public int  getFillColor() {return this.fillColor;}
        public int  getFillStyle(){return this.fillStyle;}
        
        public String getTitle() { return this.stringTitle;}
        public String getTitleX(){ return this.stringTitleX;}
        public String getTitleY(){ return this.stringTitleY;}
        
        public String getDrawOptions() {
			return drawOptions;
		}

		public void setDrawOptions(String drawOptions) {
			this.drawOptions = drawOptions;
		}

		@Override
        public DatasetAttributes  clone() throws CloneNotSupportedException{
            return (DatasetAttributes) super.clone();
        }
        
        public JPanel getPane(){
            DatasetAttributesPane pane = new DatasetAttributesPane(this);
            return pane;
        }
        
        public static class DatasetAttributesPane extends JPanel implements ActionListener, KeyListener {
    	public JButton buttonDefault;
    	public JButton buttonSetDefault;
        public JButton buttonRemove;
        /**
		 * 
		 */
		private static final long serialVersionUID = -8413019611235714087L;
		private DatasetAttributes attr = null;
        private  String[] colorChoices    = new String[50];
        private  int[] colorChoicesInts   = new int[50];
        private  String[] sizeChoices     = new String[10];
        private  int[] sizeChoicesInts    = new int[10];
        private  String[] markerChoices   = new String[4];
        private  int[] markerChoicesInts  = new int[4];
        private  String[] lineStyleChoices   = new String[5];
        private  int[] lineStyleChoicesInts  = new int[5];
        
        private JComboBox boxLineColor = null;
        private JComboBox boxLineWidth = null;
        private JComboBox boxLineStyle = null;
        
        private JComboBox boxMarkerColor = null;
        private JComboBox boxMarkerSize = null;
        private JComboBox boxMarkerStyle = null;
        
        private JComboBox boxFillColor = null;
        private JTextField optStatTextField = null;
        private JTextField drawOptionsTextField = null;
        private JComboCheckBox optStatCheckBox = null;
        private List<ActionListener> listeners = new ArrayList<ActionListener>();
        
        public DatasetAttributesPane(DatasetAttributes da){
            attr = da;
            this.setBorder(BorderFactory.createTitledBorder("Dataset Attributes"));
            this.setLayout(new MigLayout());
            this.initUI();
        }
        
        @SuppressWarnings({"unchecked", "rawtypes"})
		private void initUI(){
        	
        	 for(int i=0; i<colorChoices.length; i++){
             	colorChoices[i] = ""+i;
             	colorChoicesInts[i] = i;
             	if(i<sizeChoices.length){
             		sizeChoices[i] = ""+i;
             		sizeChoicesInts[i] = i;
             	}
             	if(i<markerChoices.length){
             		if(attr.getDatasetType() == DatasetAttributes.GRAPHERRORS){
                 		markerChoices[i] = GraphErrors.MARKERNAME[i];
             		}else{
                 		markerChoices[i] = ""+i;
             		}
             		markerChoicesInts[i] = i;
             	}
             	if(i<lineStyleChoices.length){
             		lineStyleChoices[i] = ""+(i+1);
             		lineStyleChoicesInts[i] = i+1;
             	}
             }
        	
            JLabel labelLineColor  = new JLabel("Line Color:");
            JLabel labelLineWidth  = new JLabel("Line Width:");
            JLabel labelLineStyle  = new JLabel("Line Style:");
            
            boxLineColor = new JComboBox(colorChoices);
            boxLineWidth = new JComboBox(sizeChoices);
            boxLineStyle = new JComboBox(lineStyleChoices);
            boxLineColor.setSelectedIndex(returnIndex(colorChoicesInts,attr.getLineColor()));
            boxLineWidth.setSelectedIndex(returnIndex(sizeChoicesInts,attr.getLineWidth()));
            boxLineStyle.setSelectedIndex(returnIndex(lineStyleChoicesInts,attr.getLineStyle()));

            boxLineColor.addActionListener(this);
            boxLineWidth.addActionListener(this);
            boxLineStyle.addActionListener(this);
            
            boxMarkerColor = new JComboBox(colorChoices);
            boxMarkerSize = new JComboBox(sizeChoices);
            boxMarkerStyle = new JComboBox(markerChoices);
            boxMarkerColor.setSelectedIndex(returnIndex(colorChoicesInts,attr.getMarkerColor()));
            boxMarkerSize.setSelectedIndex(returnIndex(sizeChoicesInts,attr.getMarkerSize()));
            boxMarkerStyle.setSelectedIndex(returnIndex(markerChoicesInts,attr.getMarkerStyle()));
            
            boxMarkerColor.addActionListener(this);
            boxMarkerSize.addActionListener(this);
            boxMarkerStyle.addActionListener(this);
            boxFillColor = new JComboBox(colorChoices);
            boxFillColor.addActionListener(this);
            boxFillColor.setSelectedIndex(returnIndex(colorChoicesInts,attr.getFillColor()));
            JLabel optStatLabel = new JLabel("StatBox Options:");
            optStatTextField = new JTextField(10);
            optStatTextField.setText(""+attr.getOptStat());
            drawOptionsTextField = new JTextField(10);
            drawOptionsTextField.setText(""+attr.getDrawOptions());
            
            
            if(attr.getDatasetType()==DatasetAttributes.HISTOGRAM|| attr.getDatasetType()==DatasetAttributes.FUNCTION){
	            this.add(labelLineColor);
	            this.add(boxLineColor,"wrap, pushx, growx");
	            this.add(labelLineWidth);
	            this.add(boxLineWidth,"wrap, growx");
	            this.add(labelLineStyle);
	            this.add(boxLineStyle,"wrap, growx");
            }
           
            
            if(attr.getDatasetType()==DatasetAttributes.GRAPHERRORS){
            	this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            	this.add(new JLabel("Marker Color:"));
                this.add(boxMarkerColor,"wrap, growx");
                this.add(new JLabel("Marker Size:"));
                this.add(boxMarkerSize,"wrap, growx");
                this.add(new JLabel("Marker Style:"));
                this.add(boxMarkerStyle,"wrap, growx");
	            this.add(labelLineColor);
	            this.add(boxLineColor,"wrap, pushx, growx");
	            this.add(labelLineWidth);
	            this.add(boxLineWidth,"wrap, growx");
            }
           
            if(attr.getDatasetType()==DatasetAttributes.HISTOGRAM){
                this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
                this.add(new JLabel("Fill Color:"));
                this.add(boxFillColor,"wrap, pushx, growx");
            }
            this.add(optStatLabel);
            this.add(optStatTextField,"wrap, pushx, growx");
            this.add(new JLabel("Draw Options:"));
            this.add(drawOptionsTextField,"wrap, pushx, growx");
            optStatTextField.addActionListener(this);
            drawOptionsTextField.addActionListener(this);
            optStatTextField.addKeyListener(this);
            drawOptionsTextField.addKeyListener(this);


            //this.add(new JSeparator(SwingConstants.HORIZONTAL),"skip, wrap, pushx, growx");
            
            buttonDefault = new JButton("Default");
            buttonRemove = new JButton("Delete");
            buttonSetDefault = new JButton("Set Default");
            buttonSetDefault.addActionListener(e -> {
            	 if(this.attr.datasetType == DatasetAttributes.FUNCTION){
                     GStyle.getFunctionAttributes().setAttributes(this.attr);
                 }
                 if(this.attr.datasetType == DatasetAttributes.HISTOGRAM){
                     GStyle.getH1FAttributes().setAttributes(this.attr);
                 }
                 if(this.attr.datasetType == DatasetAttributes.HISTOGRAM2D){
                     GStyle.getH2FAttributes().setAttributes(this.attr);
                 }
                 if(this.attr.datasetType == DatasetAttributes.GRAPHERRORS){
                     GStyle.getGraphErrorsAttributes().setAttributes(this.attr);
                 }
            });
            this.add(buttonSetDefault,"skip,pushx,split3");
            this.add(buttonDefault,"pushx");
            this.add(buttonRemove,"pushx");
        }

        public void addAttributeListener(ActionListener al){
            this.listeners.add(al);
        }
        
        public void updateCanvas(){
        	for(ActionListener actionListener:listeners){
        		actionListener.actionPerformed(new ActionEvent(this, 0, ""));
        	}
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==boxLineColor){
            	attr.setLineColor(Integer.parseInt(colorChoices[boxLineColor.getSelectedIndex()]));
            }else if(e.getSource()==boxLineWidth){
            	attr.setLineWidth(Integer.parseInt(sizeChoices[boxLineWidth.getSelectedIndex()]));
            }else if(e.getSource()==boxLineStyle){
            	attr.setLineStyle(Integer.parseInt(lineStyleChoices[boxLineStyle.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerColor){
            	attr.setMarkerColor(Integer.parseInt(colorChoices[boxMarkerColor.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerSize){
            	attr.setMarkerSize(Integer.parseInt(sizeChoices[boxMarkerSize.getSelectedIndex()]));
            }else if(e.getSource()==boxMarkerStyle){
            	attr.setMarkerStyle(boxMarkerStyle.getSelectedIndex());
            }else if(e.getSource()==boxFillColor){
            	attr.setFillColor(Integer.parseInt(colorChoices[boxFillColor.getSelectedIndex()]));
            }else if(e.getSource()==optStatTextField){
            	attr.setOptStat(optStatTextField.getText());
            }else if(e.getSource()==drawOptionsTextField){
            	attr.setDrawOptions(drawOptionsTextField.getText());
            }
            updateCanvas();
        }

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getSource()==optStatTextField){
            	attr.setOptStat(optStatTextField.getText());
                updateCanvas();
            }else if(e.getSource()==drawOptionsTextField){
            	attr.setDrawOptions(drawOptionsTextField.getText());
                updateCanvas();
            }
		}
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        DatasetAttributes  attr = new DatasetAttributes();
        frame.add(attr.getPane());
        frame.pack();
        frame.setVisible(true);
    }

	public int getDatasetType() {
		return datasetType;
	}
	
	private static int returnIndex(int[] array, int number){
		for(int i=0; i<array.length; i++){
			if(array[i] == number){
				return i;
			}
		}
		return 0;
	}

	public void setDatasetType(int datasetType) {
		this.datasetType = datasetType;
	}

	public void setOptStat(String optStat) {
		this.optStat = optStat;
	}

	public String getOptStat() {
		return this.optStat;
	}
	public void setDefault() {
		if(this.datasetType == DatasetAttributes.FUNCTION){
			this.setAttributes(GStyle.getFunctionAttributes());
		}else if(this.datasetType == DatasetAttributes.HISTOGRAM){
			this.setAttributes(GStyle.getH1FAttributes());
		}else if(this.datasetType == DatasetAttributes.HISTOGRAM2D){
			this.setAttributes(GStyle.getH2FAttributes());
		}else if(this.datasetType == DatasetAttributes.GRAPHERRORS){
			this.setAttributes(GStyle.getGraphErrorsAttributes());
		}
	}

}

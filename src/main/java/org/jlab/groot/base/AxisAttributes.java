/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.ui.DoubleSpinner;
import org.jlab.groot.ui.LatexText;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author gavalian
 */
public class AxisAttributes implements Cloneable {

	private Dimension1D axisDimension = new Dimension1D();
	// private Dimension1D axisDimension = new Dimension1D();

	Dimension1D range = new Dimension1D();

	public final static int X = 1;
	public final static int Y = 2;
	public final static int Z = 3;
	private String labelFontName = "Times New Roman";// "Avenir";
	private int labelFontSize = 12;
	private String titleFontName = "Times New Roman";//"Avenir";
	private int titleFontSize = 12;
	private boolean axisAutoScale = true;
	private double axisMinimum = 0.0;
	private double axisMaximum = 2.0;
	private int labelOffset = 5;
	private int titleOffset = 5;
	private boolean axisGrid = true;
	private int lineColor = 1;
	private int lineWidth = 1;
	private int tickSize = 10; // was 5
	private boolean log = false;
	private boolean showAxis = true;
	private int lineStyle = 1;
	private int axisType = 1;

	private String axisTitleString = "";
	private LatexText axisTitle = new LatexText(axisTitleString);
	private FontProperties labelFont = new FontProperties();
	private FontProperties titleFont = new FontProperties();
	private EmbeddedCanvas can = null;

	public AxisAttributes(int axisType) {
		this.axisType = axisType;
		if(this.axisType== AxisAttributes.Z){
                    showAxis = false;
                    labelFontSize = 8;
                    tickSize = 5;
                }
	}

	public Dimension1D getAxisDimension() {
		return axisDimension;
	}

	/**
	 * @return the labelOffset
	 */
	public int getLabelOffset() {
		return labelOffset;
	}

	/**
	 * @param labelOffset
	 *            the labelOffset to set
	 */
	public void setLabelOffset(int labelOffset) {
		this.labelOffset = labelOffset;
	}

	/**
	 * @return the titleOffset
	 */
	public int getTitleOffset() {
		return titleOffset;
	}

	/**
	 * @param titleOffset
	 *            the titleOffset to set
	 */
	public void setTitleOffset(int titleOffset) {
		this.titleOffset = titleOffset;
	}

	/**
	 * @return the axisTitleString
	 */
	public String getAxisTitleString() {
		return axisTitleString;
	}

	/**
	 * @param axisTitleString
	 *            the axisTitleString to set
	 */
	public void setAxisTitleString(String axisTitleString) {
		this.axisTitleString = axisTitleString;
	}

	/**
	 * @return the can
	 */
	public EmbeddedCanvas getCan() {
		return can;
	}

	/**
	 * @param can
	 *            the can to set
	 */
	public void setCan(EmbeddedCanvas can) {
		this.can = can;
	}

	/**
	 * @return the labelFontName
	 */
	public String getLabelFontName() {
		return labelFontName;
	}

	/**
	 * @return the labelFontSize
	 */
	public int getLabelFontSize() {
		return labelFontSize;
	}

	/**
	 * @return the titleFontName
	 */
	public String getTitleFontName() {
		return titleFontName;
	}

	/**
	 * @return the titleFontSize
	 */
	public int getTitleFontSize() {
		return titleFontSize;
	}

	/**
	 * @return the axisAutoScale
	 */
	public boolean isAxisAutoScale() {
		return axisAutoScale;
	}

	/**
	 * @return the axisMinimum
	 */
	public double getAxisMinimum() {
		return axisMinimum;
	}

	/**
	 * @return the axisMaximum
	 */
	public double getAxisMaximum() {
		return axisMaximum;
	}

	public int getAxisType() {
		return axisType;
	}

	public void setAxisType(int axisType) {
		this.axisType = axisType;
	}

	/**
	 * @return the axisGrid
	 */
	public boolean isAxisGrid() {
		return axisGrid;
	}

	/**
	 * @return the lineColor
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * @return the lineWidth
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * @return the tickSize
	 */
	public int getTickSize() {
		return tickSize;
	}
	@Override
	public AxisAttributes clone() throws CloneNotSupportedException {
			AxisAttributes copy = (AxisAttributes) super.clone();
			copy.range = (Dimension1D) range.clone();
			copy.axisDimension = (Dimension1D) this.axisDimension.clone();
		return  copy;
	}

	public void setLabelFontName(String fn) {
		this.labelFontName = fn;
	}
	public void setLabelFontSize(int fs) {
		this.labelFontSize = fs;
	}
	public void setTitleFontName(String fn) {
		this.titleFontName = fn;
	}
	public void setTitleFontSize(int fs) {
		this.titleFontSize = fs;
	}
	public void setAxisAutoScale(boolean autoscale) {
		this.axisAutoScale = autoscale;
	}
	public void setAxisMinimum(double min) {
		this.axisMinimum = min;
		this.range.setMinMax(this.axisMinimum, this.range.getMax());
	}
	public void setAxisMaximum(double max) {
		this.axisMaximum = max;
		this.range.setMinMax(this.range.getMin(), this.axisMaximum);
	}
	public void setAxisGrid(boolean grid) {
		this.axisGrid = grid;
	}

	public void setLineColor(int color) {
		this.lineColor = color;
	}
	public void setLineWidth(int width) {
		this.lineWidth = width;
	}
	public void setTickSize(int size) {
		this.tickSize = size;
	}
	public void setAxisTitle(String title) {
		this.axisTitleString = title;
	}
	public void setCanvas(EmbeddedCanvas can) {
		this.can = can;
	}

	public void updateCanvas() {
		can.update();
	}

	public JPanel getPane() {
		return new AxisAttributesPane(this);
	}

	public static class AxisAttributesPane extends JPanel implements ActionListener, ChangeListener {
		AxisAttributes attr = null;
		DoubleSpinner axisMinimum = null;
		DoubleSpinner axisMaximum = null;
		JCheckBox axisAutoScale = null;
		JCheckBox axisGrid = null;
		JCheckBox axisLog = null;
		JCheckBox axisShow = null;
		JComboBox labelFont = null;
		JComboBox titleFont = null;

		JComboBox labelFontSize = null;
		JComboBox titleFontSize = null;

		JTextField axisTitle = null;
		private List<ActionListener> listeners = new ArrayList<ActionListener>();

		public void addAttributeListener(ActionListener al) {
			this.listeners.add(al);
		}

		public AxisAttributesPane(AxisAttributes aa) {
			attr = aa;
			this.setBorder(BorderFactory.createTitledBorder("Axis Attributes"));
			this.setLayout(new MigLayout());
			initUI();
		}

		private void initUI() {
			labelFont = new JComboBox(FontProperties.getSystemFontsArray());
			titleFont = new JComboBox(FontProperties.getSystemFontsArray());
			labelFontSize = new JComboBox(FontProperties.getFontSizeArray());
			titleFontSize = new JComboBox(FontProperties.getFontSizeArray());

			labelFont.setSelectedItem(attr.getLabelFontName());
			titleFont.setSelectedItem(attr.getTitleFontName());
			labelFontSize.setSelectedIndex(returnIndex(FontProperties.getFontSizeArray(), attr.getLabelFontSize()));
			titleFontSize.setSelectedIndex(returnIndex(FontProperties.getFontSizeArray(), attr.getTitleFontSize()));

			labelFont.addActionListener(this);
			titleFont.addActionListener(this);
			labelFontSize.addActionListener(this);
			titleFontSize.addActionListener(this);

			// Attributes.chooseByString(labelFont, "Avenir");
			// Attributes.chooseByString(titleFont, "Helvetica");

			this.add(new JSeparator(), "skip, wrap, growx");

			this.add(new JLabel("Label Font:"));
			this.add(labelFont, "wrap, pushx, growx");
			// this.add(labelFont,"wrap, pushx");

			this.add(new JLabel("Title Font:"));
			this.add(titleFont, "wrap, pushx, growx");

			this.add(new JLabel("Label Font Size:"));
			this.add(labelFontSize, "wrap, pushx, growx");

			this.add(new JLabel("Title Font Size:"));
			this.add(titleFontSize, "wrap, pushx, growx");

			this.add(new JSeparator(), "skip, wrap, growx");
			this.add(new JLabel("Title:"));
			axisTitle = new JTextField();
			axisTitle.addActionListener(this);

			this.add(axisTitle, "wrap, pushx, growx");
			axisTitle.setText(attr.getAxisTitleString());
			this.add(new JSeparator(), "skip, wrap, growx");

			axisMinimum = new DoubleSpinner();
			axisMaximum = new DoubleSpinner();
			axisAutoScale = new JCheckBox();
			axisGrid = new JCheckBox();
			axisShow = new JCheckBox();
			axisLog = new JCheckBox();

			axisMinimum.setValue(attr.getAxisMinimum());
			axisMaximum.setValue(attr.getAxisMaximum());
			axisAutoScale.setSelected(attr.isAxisAutoScale());
			axisGrid.setSelected(attr.isAxisGrid());
			axisLog.setSelected(attr.isLog());
			axisShow.setSelected(attr.showAxis());

			axisMinimum.addChangeListener(this);
			axisMaximum.addChangeListener(this);
			axisAutoScale.addChangeListener(this);
			axisGrid.addChangeListener(this);
			axisLog.addChangeListener(this);
			axisShow.addChangeListener(this);

			this.add(new JLabel("Axis Min:"));
			this.add(axisMinimum, "wrap, pushx, growx");
			this.add(new JLabel("Axis Max:"));
			this.add(axisMaximum, "wrap, pushx, growx");
			this.add(new JLabel("Auto Scale:"), "skip, split4");
			this.add(axisAutoScale);

			this.add(new JLabel("Grid:"), "pushx");
			this.add(axisGrid, "wrap, pushx, growx");
			this.add(new JLabel("Show Axis:"), "skip,split4");
			this.add(axisShow);

			this.add(new JLabel("Log:"), "skip, split4");
			this.add(axisLog,"wrap");

			// this.add(new JSeparator(),"skip, wrap, growx");
			JButton buttonDefault = new JButton("Default");
			JButton buttonSetDefault = new JButton("Set Default");
			this.add(buttonSetDefault, "skip");
			buttonSetDefault.addActionListener(e -> {
				if (this.attr.axisType == AxisAttributes.X) {
					GStyle.getAxisAttributesX().setAttributes(this.attr);
				}
				if (this.attr.axisType == AxisAttributes.Y) {
					GStyle.getAxisAttributesY().setAttributes(this.attr);
				}
				if (this.attr.axisType == AxisAttributes.Z) {
					GStyle.getAxisAttributesZ().setAttributes(this.attr);
				}

			});
			JButton buttonApply = new JButton("Apply");
			// this.add(buttonDefault,"skip, split2, pushy");
			// this.add(buttonApply,"wrap, pushy");

		}

		public void updateCanvas() {
			for (ActionListener actionListener : listeners) {
				actionListener.actionPerformed(new ActionEvent(this, 0, ""));
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("action performed:" +
			// e.getSource().toString());
			if (e.getSource() == labelFont) {
				attr.setLabelFontName(FontProperties.getSystemFontsArray()[labelFont.getSelectedIndex()]);
			} else if (e.getSource() == titleFont) {
				attr.setTitleFontName(FontProperties.getSystemFontsArray()[titleFont.getSelectedIndex()]);
			} else if (e.getSource() == labelFontSize) {
				attr.setLabelFontSize(
						Integer.parseInt(FontProperties.getFontSizeArray()[labelFontSize.getSelectedIndex()]));
			} else if (e.getSource() == titleFontSize) {
				attr.setTitleFontSize(
						Integer.parseInt(FontProperties.getFontSizeArray()[titleFontSize.getSelectedIndex()]));
			} else if (e.getSource() == axisTitle) {
				attr.setAxisTitle(axisTitle.getText());
			}
			updateCanvas();
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// System.out.println("stateChanged:" + e.getSource().toString());
			if (e.getSource() == axisMinimum) {
				axisAutoScale.setSelected(false);
				attr.setAxisAutoScale(false);
				attr.setAxisMinimum((double) axisMinimum.getDouble());
			} else if (e.getSource() == axisMaximum) {
				axisAutoScale.setSelected(false);
				attr.setAxisAutoScale(false);
				attr.setAxisMaximum((double) axisMaximum.getDouble());
				// System.out.println("Axis Maximum:"+(double)
				// axisMaximum.getDouble());
			} else if (e.getSource() == axisAutoScale) {
				attr.setAxisAutoScale(axisAutoScale.isSelected());
				attr.setAxisMinimum((double) axisMinimum.getDouble());
				attr.setAxisMaximum((double) axisMaximum.getDouble());
			} else if (e.getSource() == axisGrid) {
				attr.setAxisGrid(axisGrid.isSelected());
			} else if (e.getSource() == axisLog) {
				attr.setLog(axisLog.isSelected());
			} else if (e.getSource() == axisShow) {
				attr.setShowAxis(axisShow.isSelected());
			}
			updateCanvas();
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		AxisAttributes attr = new AxisAttributes(AxisAttributes.X);
		frame.add(attr.getPane());
		frame.pack();
		frame.setVisible(true);
	}

	public void setAttributes(AxisAttributes attr) {
		labelFontName = attr.getLabelFontName();
		labelFontSize = attr.getLabelFontSize();
		titleFontName = attr.getTitleFontName();
		titleFontSize = attr.getTitleFontSize();
		axisAutoScale = attr.isAxisAutoScale();
		axisMinimum = attr.getAxisMinimum();
		axisMaximum = attr.getAxisMaximum();
		labelOffset = attr.getLabelOffset();
		titleOffset = attr.getTitleOffset();
		axisGrid = attr.getGrid();
		lineColor = attr.getLineColor();
		lineWidth = attr.getLineWidth();
		tickSize = attr.getTickSize();
		log = attr.isLog();
		showAxis = attr.showAxis();
		lineStyle = attr.getLineStyle();
		axisType = attr.getAxisType();    
	}

	public LatexText getTitle() {
		axisTitle.setText(axisTitleString);
		axisTitle.setFont(this.titleFontName);
		axisTitle.setFontSize(this.titleFontSize);
		return axisTitle;
	}

	public FontProperties getTitleFont() {
		titleFont.setFontName(titleFontName);
		titleFont.setFontSize(titleFontSize);
		return titleFont;
	}

	public FontProperties getLabelFont() {
		labelFont.setFontName(labelFontName);
		labelFont.setFontSize(labelFontSize);
		return labelFont;
	}

	public boolean isLog() {
		return this.log;
	}

	public void setLog(boolean log) {
		this.log = log;
		this.axisDimension.setLog(log);
		this.getRange().setLog(log);
	}
	private static int returnIndex(String[] strings, int number) {
		for (int i = 0; i < strings.length; i++) {
			if (Integer.parseInt(strings[i]) == number) {
				return i;
			}
		}
		return 0;
	}

	public Dimension1D getRange() {
		range.setMinMax(axisMinimum, axisMaximum);
		return range;
	}

	public boolean getGrid() {
		return axisGrid;
	}

	public void setGrid(boolean axisGrid) {
		this.axisGrid = axisGrid;
	}

	public boolean showAxis() {
		return showAxis;
	}
	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
	}

	public int getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}

}

package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;

import org.jlab.groot.base.AxisAttributes.AxisAttributesPane;
import org.jlab.groot.base.DatasetAttributes.DatasetAttributesPane;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.IDataSetPlotter;

public class OptionsPanel extends JPanel {
	int pad;
	EmbeddedCanvas can = null;
	JTabbedPane tabbedPane = null;
	public OptionsPanel(EmbeddedCanvas can, int pad){
		this.can = can;
		this.pad = pad;
		tabbedPane = new JTabbedPane();
		this.setLayout(new BorderLayout());
		this.add(tabbedPane,BorderLayout.CENTER);
		initMain();
		initAxes();
		initDatasets();
	}
	
	private void initMain() {
		JPanel main = new JPanel();
		main.setMinimumSize(new Dimension(200,300));
		main.add(new JLabel("Main Panel"));
		tabbedPane.add("Main", main);
	}
	
	private void initAxes() {
		AxisAttributesPane panex = new AxisAttributesPane(can.getPad(pad).getAxisFrame().getAxisX().attr);
		AxisAttributesPane paney = new AxisAttributesPane(can.getPad(pad).getAxisFrame().getAxisY().attr);
		tabbedPane.add("X", panex);
		tabbedPane.add("Y", paney);
	}
	
	private void initDatasets() {
		List<IDataSetPlotter> datasets = can.getPad(pad).getDatasetPlotters();
		for(IDataSetPlotter dataset : datasets){
			DatasetAttributesPane tempPane  = new DatasetAttributesPane(dataset.getDataSet().getAttributes());
			tempPane.addAttributeListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					can.repaint();
				}
			});
			tabbedPane.add(dataset.getName(),tempPane);
		}
	}

}

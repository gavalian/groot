/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class DatasetOperations extends JPanel {
    
    Map<String,IDataSet>  datasetObjects = null;
    JComboBox firstOperand = null;
    JComboBox secondOperand = null;
    List<IDataSet>  results = new ArrayList<IDataSet>();
    
    public DatasetOperations(Map<String,IDataSet> datasets){
        super();
        datasetObjects = datasets;
        initUI();
    }
    
    private void initUI(){
        Set<String> objectKeys = datasetObjects.keySet();
        String[] choices = new String[objectKeys.size()];
        int counter = 0;
        for(String key : objectKeys){
            choices[counter] = key;
            counter++;
        }
        firstOperand  = new JComboBox(choices);
        secondOperand = new JComboBox(choices);
        this.setLayout(new FlowLayout());
        this.add(firstOperand);
        this.add(secondOperand);
        
        JButton buttonDivide = new JButton("Divide");
        
        buttonDivide.addActionListener(
                new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
                            operationDivide();
			}
                }
        );
        
        JButton buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(
                new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
                            operationAdd();
			}
                }
        );
        
        JButton buttonNormalize = new JButton("Normalize");
        buttonNormalize.addActionListener(
                new ActionListener() {
                        @Override
			public void actionPerformed(ActionEvent event) {
                            operationNormalize();
			}
                }
        );
        
        this.add(buttonDivide);
        this.add(buttonAdd);
        this.add(buttonNormalize);
        
    }
    
    private void operationDivide(){
        String op1 = (String) firstOperand.getSelectedItem();
        String op2 = (String) secondOperand.getSelectedItem();
        results.clear();
        
        H1F h1 = (H1F) datasetObjects.get(op1);
        H1F h2 = (H1F) datasetObjects.get(op2);
        
        H1F h3 = H1F.divide(h1, h2);
        h3.setFillColor(45);
        h3.setTitleX(h1.getName()+"_divided_"+h2.getName());
        //h3.setName(h1.getName()+"_divided_"+h2.getName());
        results.add(h3);
    }
    
    private void operationAdd(){
        String op1 = (String) firstOperand.getSelectedItem();
        String op2 = (String) secondOperand.getSelectedItem();

        results.clear();
        
        H1F h1 = (H1F) datasetObjects.get(op1);
        H1F h2 = (H1F) datasetObjects.get(op2);
        
        H1F h3 = H1F.add(h1, h2);
        h3.setFillColor(45);
        h3.setTitleX(h1.getName()+"_add_"+h2.getName());
        //h3.setName(h1.getName()+"_divided_"+h2.getName());
        results.add(h3);
    }
    private void operationNormalize(){
        
        String op1 = (String) firstOperand.getSelectedItem();
        String op2 = (String) secondOperand.getSelectedItem();
        
        results.clear();
        
        H1F h1 = (H1F) datasetObjects.get(op1);
        H1F h2 = (H1F) datasetObjects.get(op2);
        
        H1F h3 = H1F.normalized(h1, h2);
        h3.setFillColor(45);
        h3.setTitleX(h1.getName()+"_normalized_"+h2.getName());
        //h3.setName(h1.getName()+"_divided_"+h2.getName());
        results.add(h3);
    }
    
    public List<IDataSet>  getResults(){
        return this.results;
    }
    
    public static JDialog createOperations(JFrame parent,Map<String,IDataSet> datasets){
        JDialog dialog = new JDialog(parent,
                                                 "Click a button",
                                                 true);
        DatasetOperations oper = new DatasetOperations(datasets);
        dialog.setContentPane(oper);
        dialog.setSize(300, 300);
        return dialog;
    }
    
}

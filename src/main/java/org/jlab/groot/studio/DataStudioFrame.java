/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class DataStudioFrame extends JFrame {

    private JTable dTable = null;
    private DataStudio  studio = DataStudio.getInstance();
    private TCanvas     canvas = null;
    
    public DataStudioFrame(){
        super();
        this.setSize(500, 500);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initViewer();
        JScrollPane pane = new JScrollPane(dTable);
        this.add(pane);
        this.pack();
    }
    
    public final void setCanvas(TCanvas c){
        this.canvas = c;
    }
    
    private void initViewer(){
        AbstractTableModel model = new AbstractTableModel(){
            @Override
            public int getRowCount() {
               return studio.getDataSetStore().size();
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                int row = 0;
                for(Map.Entry<Integer,IDataSet> data : studio.getDataSetStore().entrySet()){
                    if(row==rowIndex){
                        if(columnIndex==0) return data.getKey();
                        if(columnIndex==1) return data.getValue().getName();
                        if(columnIndex==2) return new Double(data.getValue().getMin());
                        if(columnIndex==3) return data.getValue().getMax();
                    }
                    row++;
                }
                return "UNKNOWN";
            }
            
        };
        this.dTable = new JTable(model);
        
        dTable.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
           
            if (e.getClickCount() == 2) {
                int[] rows = dTable.getSelectedRows();
                if(rows.length>0){
                    Integer id = (Integer) dTable.getModel().getValueAt(rows[0], 0);
                    System.out.println(" SELECTED ROWS " + rows.length  + " "  + id);
                    if(canvas!=null){
                        canvas.getCanvas().drawNext(DataStudio.getInstance().getDataSetStore().get(id));
                    }
                }

            } else {
            }

        }
        });
        
        ListSelectionModel cellSelectionModel = dTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /*
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] rows = dTable.getSelectedRows();
                if(rows.length>0){
                    Integer id = (Integer) dTable.getModel().getValueAt(rows[0], 0);
                    System.out.println(" SELECTED ROWS " + rows.length  + " "  + id);
                    if(canvas!=null){
                        canvas.getCanvas().drawNext(DataStudio.getInstance().getDataSetStore().get(id));
                    }
                }
                //System.out.println(" SELECTED ROWS " + rows.length  );
            }
        
        });*/
       
        
    }
    
    
    
    public static void main(String[] args){
        DataStudio.getInstance().addDataSet(100, new H1F("DATA SET",100,0.0,1.0));
        DataStudio.getInstance().addDataSet(101, new H1F("DATA SET 2",100,0.0,1.0));
        DataStudioFrame frame = new DataStudioFrame();
        TCanvas c1 = new TCanvas("c1",500,500);
        frame.setCanvas(c1);
        frame.setVisible(true);
    }
    
}

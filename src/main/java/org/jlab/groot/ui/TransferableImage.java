package org.jlab.groot.ui;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.TransferHandler.TransferSupport;

import org.jlab.groot.graphics.EmbeddedPad;

/*import org.root.base.DataSetCollection;
import org.root.base.IDataSet;
*/
public class TransferableImage implements Transferable {
    private static DataFlavor dmselFlavor = new DataFlavor(EmbeddedPad.class, "EmbeddedPad");

    Image i;
    EmbeddedPad pad;
     
    public TransferableImage( Image i ) {
        this.i = i;
    }
   
    public void setPad(EmbeddedPad pad){
    	this.pad = pad;
    }
  

    public Object getTransferData( DataFlavor flavor ) 
    throws UnsupportedFlavorException, IOException {
        if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
            return i;
        }
       if ( flavor.getHumanPresentableName().equals( dmselFlavor.getHumanPresentableName() ) && pad != null ) {
            return pad;
        }
        else {
            throw new UnsupportedFlavorException( flavor );
        }
    }
     
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[2];
        DataFlavor imageFlavor = new DataFlavor("image/x-java-image; class=java.awt.Image", "Image");
        flavors[0] = imageFlavor;
        flavors[1] = dmselFlavor;
        return flavors;
    }
     
    public boolean isDataFlavorSupported( DataFlavor flavor ) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for ( int i = 0; i < flavors.length; i++ ) {
            if (  flavor.getHumanPresentableName().equals( flavors[i].getHumanPresentableName() ) ) {
                return true;
            }
        }
         
        return false;
    }
}

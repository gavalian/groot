/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.math;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class Dimension3D {
    private List<Dimension1D>  dimensions = new ArrayList<Dimension1D>();
    public Dimension3D(){
         this.dimensions.add(new Dimension1D());
         this.dimensions.add(new Dimension1D());
         this.dimensions.add(new Dimension1D());         
    }
    
    public final void set(double min1, double max1, double min2, double max2,
            double min3, double max3){
        dimensions.get(0).setMinMax(min1, max1);
        dimensions.get(1).setMinMax(min2, max2);
        dimensions.get(2).setMinMax(min3, max3);
    }

    public Dimension1D getDimension(int index){
        return this.dimensions.get(index);
    }
    
    public void grow(double x, double y, double z){
        dimensions.get(0).grow(x);
        dimensions.get(1).grow(y);
        dimensions.get(2).grow(z);
    }
    
    public void grow(Dimension3D dim){
        getDimension(0).grow(dim.getDimension(0).getMin());
        getDimension(0).grow(dim.getDimension(0).getMax());
        getDimension(1).grow(dim.getDimension(1).getMin());
        getDimension(1).grow(dim.getDimension(1).getMax());
        getDimension(2).grow(dim.getDimension(2).getMin());
        getDimension(2).grow(dim.getDimension(2).getMax());                
    }
    
    public void  copy(Dimension3D object){
        this.set(object.getDimension(0).getMin(),
                object.getDimension(0).getMax(),
                object.getDimension(1).getMin(),
                object.getDimension(1).getMax(),
                object.getDimension(2).getMin(),
                object.getDimension(2).getMax()
        );       
    }
    public void combine(Dimension3D dim){
        this.getDimension(0).grow(dim.getDimension(0).getMin());
        this.getDimension(0).grow(dim.getDimension(0).getMax());
        this.getDimension(1).grow(dim.getDimension(1).getMin());
        this.getDimension(1).grow(dim.getDimension(1).getMax());
        this.getDimension(2).grow(dim.getDimension(2).getMin());
        this.getDimension(2).grow(dim.getDimension(2).getMax());
    }
    @Override
    public String toString(){
        return String.format("Dimension2D : (%8.3f - %8.3f) (%8.3f - %8.3f) (%8.3f - %8.3f)",
                dimensions.get(0).getMin(),
                dimensions.get(0).getMax(),
                dimensions.get(1).getMin(),
                dimensions.get(1).getMax(),
                dimensions.get(2).getMin(),
                dimensions.get(2).getMax()
                
                );
    }
}

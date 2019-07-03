/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import java.util.ArrayList;
import java.util.List;
//import javafx.geometry.Point2D;

/**
 *
 * @author gavalian
 */
public class Dimension2D {
    
    private List<Dimension1D>  dimensions = new ArrayList<Dimension1D>();
    
    public     Dimension2D(){
        this.dimensions.add(new Dimension1D());
        this.dimensions.add(new Dimension1D());
    }
    
    public     Dimension2D(double min1, double max1, double min2, double max2){
        this.dimensions.add(new Dimension1D(min1,max1));
        this.dimensions.add(new Dimension1D(min2,max2));
    }
    
    public final void set(double min1, double max1, double min2, double max2){
        dimensions.get(0).setMinMax(min1, max1);
        dimensions.get(1).setMinMax(min2, max2);
    }
    
    public Dimension1D  getDimension(int index){
        return this.dimensions.get(index);
    }
    
    public boolean contains(double x, double y){
        return (dimensions.get(0).contains(x)&&dimensions.get(1).contains(y));
    }
    
    public void grow(double x, double y){
        dimensions.get(0).grow(x);
        dimensions.get(1).grow(y);
    }
    
    public void combine(Dimension2D dim){
        this.getDimension(0).grow(dim.getDimension(0).getMin());
        this.getDimension(0).grow(dim.getDimension(0).getMax());
        this.getDimension(1).grow(dim.getDimension(1).getMin());
        this.getDimension(1).grow(dim.getDimension(1).getMax());
    }
    
    public void  copy(Dimension2D object){
        this.set(object.getDimension(0).getMin(),
                object.getDimension(0).getMax(),
                object.getDimension(1).getMin(),
                object.getDimension(1).getMax()
        );
        /*return new Dimension2D(
                object.getDimension(0).getMin(),
                object.getDimension(0).getMax(),
                object.getDimension(1).getMin(),
                object.getDimension(1).getMax()
        );*/
    }
    
    public void grow(Dimension2D dim){
        grow(dim.getDimension(0).getMin(),dim.getDimension(1).getMin());
        grow(dim.getDimension(0).getMax(),dim.getDimension(1).getMax());
    }
    
    @Override
    public String toString(){
        return String.format("Dimension2D : (%8.3f - %8.3f) (%8.3f - %8.3f)",
                dimensions.get(0).getMin(),
                dimensions.get(0).getMax(),
                dimensions.get(1).getMin(),
                dimensions.get(1).getMax()
                );
    }
    
    /*public Point2D getPoint( double x, double y, Dimension2D dim){
        double xc = this.dimensions.get(0).convert(x, dim.getDimension(0));
        double yc = this.dimensions.get(1).convert(y, dim.getDimension(1));
        return new Point2D( xc, yc);
    }*/
    
    public static void main(String[] args){
        Dimension2D dim = new Dimension2D(0.0,100.0,0.,50.0);
        Dimension2D dim2 = new Dimension2D(-0.5,0.7,0.0,5.0);
        System.out.println(dim);
        dim.grow(dim2);
        System.out.println(dim);
        //Point2D  point = dim.getPoint(0.0,1.0, dim2);
        //System.out.println(point);
    }
}

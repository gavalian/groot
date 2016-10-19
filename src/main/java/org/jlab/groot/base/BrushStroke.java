/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.base;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.Random;

/**
 *
 * @author gavalian
 */
public class BrushStroke implements Stroke {

    float thickness = 0.5f;
    
    public BrushStroke(double th){
        thickness = (float) th;
    }
    
    @Override
    public Shape createStrokedShape(Shape p) {
         GeneralPath returnValue = new GeneralPath();
  /*
 Random r = new Random(randomSeed);
 
 float h = thickness*thickness;
 int thicknessMax = (int) Math.min(100, 100*h+100*.2f);
 int thicknessMin = (int) (100*h/2f);
  
 GeneralPath thisLayer = new GeneralPath(Path2D.WIND_NON_ZERO);
 GeneralPathWriter writer = new GeneralPathWriter(thisLayer);  
 for(int a = 0; a<layers; a++) {
  writer.reset();
  float k1 = a*width/(layers-1f);
  float k2 = k1-width/2;
  InsetPathWriter insetWriter;
  if(k2>0) {
   insetWriter = new InsetPathWriter(writer,Math.abs(k2),theta);
  } else {
   insetWriter = new InsetPathWriter(writer,Math.abs(k2),(float)(Math.PI+theta));
  }
  insetWriter.write(p);
  MeasuredShape[] measuredLayers = MeasuredShape.getSubpaths(thisLayer);

  float minStreakDistance = (4+10*thickness)/1f;
  float maxStreakDistance = (40+10*thickness)/1f;
  float k3 = Math.abs(k2);
  float minGapDistance = (4+10*k3)/1f;
  float maxGapDistance = (40+10*k3)/1f;
   
  for(int b = 0; b<measuredLayers.length; b++) {
   r.setSeed(randomSeed+1000*a+10000*b);
   
   float x = 0;
   if(a!=layers/2) {
    float k4 = Math.abs(k2/width);
    x = (maxGapDistance-minGapDistance)*r.nextFloat()+k4*(.3f*r.nextFloat()+.7f)*minGapDistance;
   }
    
   boolean first = true;
   while(x<measuredLayers[b].getOriginalDistance()) {
    float streakDistance = minStreakDistance+(maxStreakDistance-minStreakDistance)*r.nextFloat();
    float gapDistance;
    if(first) {
     first = false;
     gapDistance = (.2f+.8f*r.nextFloat())*minGapDistance+(maxGapDistance-minGapDistance)*r.nextFloat();
    } else {
     gapDistance = minGapDistance+(maxGapDistance-minGapDistance)*r.nextFloat();
    }
     
    if(x+streakDistance>measuredLayers[b].getOriginalDistance()) {
     float z = 0;
     if(a!=layers/2)
      z = (maxGapDistance-minGapDistance)*r.nextFloat();
     streakDistance = measuredLayers[b].getOriginalDistance()-x-z;
    }
    if(streakDistance>0) {
     GeneralPath p2 = measuredLayers[b].getShape(x/measuredLayers[b].getDistance(),
       streakDistance/measuredLayers[b].getDistance());
     float z = r.nextInt(thicknessMax-thicknessMin)+thicknessMin
     float width = .15f+(2.05f-.15f)*z/100;
     BasicStroke thinStroke = new BasicStroke(width);
     returnValue.append(thinStroke.createStrokedShape(p2),false);
    }
    
    x = x+(streakDistance+gapDistance);
   }
  }
 }*/
 return returnValue;
    }
    
}

package org.jlab.groot.math;

import java.io.Serializable;

/**
 * Handles the indexing of multidimensional arrays. 
 *
 * @author Erin Kirby
 * @version 061714
 */
public class MultiIndex implements Serializable {
    
	private Integer indexDim;
	private Integer[] dimSizes;
	private Integer[] currentIndex;
	
	/**
	 * Initializes the arrays to size 0.
	 */
	public MultiIndex() {
		indexDim = 0;
		dimSizes = new Integer[0];
		currentIndex = new Integer[0];
	}
	
	/**
	 * Initializes the variables based on the number of arguments
	 * 
	 * @param d the list of dimension sizes. Used to determine the array sizes.
	 */
	public MultiIndex(int... d) {
		setDimensions(d);
	}
	
	/**
	 * Sets the dimension sizes at each index and initializes every element
	 * of the current index to -1.
	 * 
	 * @param d			the list of dimension sizes
	 */
	public final void setDimensions(int... d) {
		indexDim = d.length;
		dimSizes = new Integer[d.length];
		currentIndex = new Integer[d.length];		
		for (int i = 0; i < d.length; i++) {
			dimSizes[i] = d[i];
			currentIndex[i] = -1;
		}
	}
	
	/**
	 * Gets the size of the resulting 1D array from the dimension sizes
	 * 
	 * @return 		the size of the 1D array from storing everything in the multi-dimensional
	 * arrays
	 */
	public int getArraySize() {
		int aSize = 1;
		for (int i = 0; i < dimSizes.length; i++) {
			aSize *= dimSizes[i];
		}
		return aSize;
	}
	
	/**
	 *	Gets the needed size for 1D, 2D, and 3D arrays to be stored in a 1D array
	 *
	 * @param ind		The size of each dimension
	 * @return			The size of the 1D array from storing all the elements
	 */
	public int getArrayIndex(int... ind) {
		if(ind.length == indexDim) {
            if(ind.length == 1) { 
                return ind[0];
            }
            
            if(ind.length == 2) { 
                if(this.checkIndex(0, ind[0]) == false ||
                        this.checkIndex(1, ind[1]) == false) {
                	return -1; 
                }
                return ind[0] + ind[1]*dimSizes[0];
            }

            if(ind.length == 3) { 
                if(this.checkIndex(0, ind[0])==false||
                        this.checkIndex(1, ind[1])==false||
                        this.checkIndex(2, ind[2])==false) {
                	return -1;
                }
                return (ind[2]*dimSizes[1]*dimSizes[0])+(ind[1]*dimSizes[0]) + ind[0];
            }
        }
		
        return -1;
	}
	
	/**
	 * Checks if the index is valid at that dimension
	 * 
	 * @param dim 			Which dimension's index to be checked
	 * @param index			The index to be checked if valid
	 * @return				Whether the index at that dimension is valid
	 */
	private boolean checkIndex(int dim, int index) {
        if((index >= 0) && (index<dimSizes[dim])) {
        	return true; 
        }
        return false;
    }
}

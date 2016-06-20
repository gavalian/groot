package org.jlab.groot.math;


/**
 * Creates a StatNumber object with a number and an error.
 * @author Erin Kirby
 * @version 061714
 */
public class StatNumber {

	private double number;
	private double error;
	
	/**
	 * Creates a default StatNumber object with a number and error set to 0.0.
	 */
	public StatNumber() {
		number = 0.0;
		error = 0.0;
	}
	
	/**
	 * Creates a StatNumber object with the specified number and error.
	 * 
	 * @param num		The number
	 * @param err		The error
	 */
	public StatNumber(double num, double err) {
		number = num;
		error = err;
	}
	
	/**
	 * Creates a StatNumber object with the specified number and calculates its error.
	 * 
	 * @param num		What number to store in the StatNumber object.
	 */
	public StatNumber(double num) {
		number = num;
		error = Math.sqrt(Math.abs(num));
	}
	
	/**
	 * Sets the number and error of the StatNumber object
	 * 
	 * @param num		What to set the number to
	 * @param err		What to set the error to
	 */
	public void set(double num, double err) {
		number = num;
		error = err;
	}
	
	/**
	 * 
	 * @return the number stored in the StatNumber object.
	 */
	public double number() {
		return number;
	}
	
	/**
	 * 
	 * @return the error stored in the StatNumber object.
	 */
	public double error() {
		return error;
	}
	
	/**
	 * Adds two StatNumber objects.
	 * 
	 * @param num 		The StatNumber to add to this object.
	 */
	public void add(StatNumber num) {
		number += num.number();
		error = Math.sqrt((error * error) + (num.error() * num.error()));
	}
	
	/**
	 * Divides the current object by the parameter StatNumber
	 * 
	 * @param num		The StatNumber to divide this object by
	 */
	public void divide(StatNumber num) {
        double  _fnum = 0;
        if(num.number()!=0) _fnum = this.number()/num.number();
        double  _n1   = this.number();
        double  _n2   = num.number();
        double  _e1   = this.error();
        double  _e2   = num.error();
        double  _r12   = 0;
        double  _r22   = 0;
        if(_n1!=0) _r12 = (_e1/_n1)*(_e1/_n1);
        if(_n2!=0) _r22 = (_e2/_n2)*(_e2/_n2);
        number = _fnum;
        error  = _fnum*Math.sqrt(_r12+_r22);
	}
	
	/**
	 * Multiplies the parameter and the current StatNumber
	 * 
	 * @param num		The StatNumber object to multiply this object by
	 */
	public void multiply(StatNumber num) {
		double  _fnum = this.number()*num.number();
        double  _n1   = this.number();
        double  _n2   = num.number();
        double  _e1   = this.error();
        double  _e2   = num.error();
        double  _r12   = 0;
        double  _r22   = 0;
        if(_n1!=0) _r12 = (_e1/_n1)*(_e1/_n1);
        if(_n2!=0) _r22 = (_e2/_n2)*(_e2/_n2);
        number = _fnum;
        error  = _fnum * Math.sqrt(_r12+_r22);
	}
	
	/**
	 * Subtracts the parameter from the current object
	 * 
	 * @param num		The StatNumber to subtract from the current StatNumber object
	 */
	public void subtract(StatNumber num) {
		number -= num.number();
		error = Math.sqrt((error * error) 
                + (num.error()*num.error()));
	}
}

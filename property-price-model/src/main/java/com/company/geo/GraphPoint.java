/**
 * 
 */
package com.company.geo;

import com.company.utils.PriceUtils;

/**
 * @author smcardle
 *
 */
public class GraphPoint {

    private double a;
    private double t;
    private double q;
    private String d;
    
    
    /**
     * @return the a
     */
    public double getA() {
        return a;
    }
    /**
     * @param a the a to set
     */
    public void setA(double a) {
        this.a = PriceUtils.truncate(a);
    }
    /**
     * @return the t
     */
    public double getT() {
        return t;
    }
    /**
     * @param t the t to set
     */
    public void setT(double t) {
        this.t = PriceUtils.truncate(t);;
    }
    /**
     * @return the q
     */
    public double getQ() {
        return q;
    }
    /**
     * @param q the q to set
     */
    public void setQ(double q) {
        this.q = PriceUtils.truncate(q);
    }
    /**
     * @return the d
     */
    public String getD() {
        return d;
    }
    /**
     * @param d the d to set
     */
    public void setD(String d) {
        this.d = d;
    }
}

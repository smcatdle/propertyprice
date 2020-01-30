/**
 * 
 */
package com.company.propertyprice.model.mdo;

import java.util.Date;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.utils.PriceUtils;

/**
 * @author smcardle
 *
 */
public class PropertyMDO {

    private int id;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;
    private double p;
    private Date d;
    private int f;
    private int v;
    private int s;
    private double l;
    private double o;
  
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }



    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }



    /**
     * @return the a1
     */
    public String getA1() {
        return a1;
    }



    /**
     * @param a1 the a1 to set
     */
    public void setA1(String a1) {
        this.a1 = a1;
    }



    /**
     * @return the a2
     */
    public String getA2() {
        return a2;
    }



    /**
     * @param a2 the a2 to set
     */
    public void setA2(String a2) {
        this.a2 = a2;
    }



    /**
     * @return the a3
     */
    public String getA3() {
        return a3;
    }



    /**
     * @param a3 the a3 to set
     */
    public void setA3(String a3) {
        this.a3 = a3;
    }



    /**
     * @return the a4
     */
    public String getA4() {
        return a4;
    }



    /**
     * @param a4 the a4 to set
     */
    public void setA4(String a4) {
        this.a4 = a4;
    }



    /**
     * @return the a5
     */
    public String getA5() {
        return a5;
    }



    /**
     * @param a5 the a5 to set
     */
    public void setA5(String a5) {
        this.a5 = a5;
    }



    /**
     * @return the p
     */
    public double getP() {
        return p;
    }



    /**
     * @param p the p to set
     */
    public void setP(double p) {
        this.p = p;
    }



    /**
     * @return the d
     */
    public Date getD() {
        return d;
    }



    /**
     * @param d the d to set
     */
    public void setD(Date d) {
        this.d = d;
    }



    /**
     * @return the f
     */
    public boolean isF() {
        return (f==1) ? true : false;
    }



    /**
     * @param f the f to set
     */
    public void setF(boolean f) {
        this.f = (f ? 1 : 0);
    }



    /**
     * @return the v
     */
    public boolean isV() {
	return (v==1) ? true : false;
    }



    /**
     * @param v the v to set
     */
    public void setV(boolean v) {
        this.v = (v ? 1 : 0);
    }



    /**
     * @return the s
     */
    public String getS() {
	return (s==1) ? "Second-Hand Dwelling house /Apartment" : "";
    }



    /**
     * @param s the s to set
     */
    public void setS(String s) {
        this.s = "Second-Hand Dwelling house /Apartment".equals(s) ? 1 : 0;
    }



    /**
     * @return the l
     */
    public double getL() {
        return l;
    }



    /**
     * @param l the l to set
     */
    public void setL(double l) {
       this.l = PriceUtils.truncate(l,8);
    }



    /**
     * @return the o
     */
    public double getO() {
        return o;
    }



    /**
     * @param o the o to set
     */
    public void setO(double o) {
        this.o = PriceUtils.truncate(o,8);
    }



	public Address getAddress() {
		
		Address address = new Address();
		address.setAddressLine1(a1);
		address.setAddressLine2(a2);
		address.setAddressLine3(a3);
		address.setAddressLine4(a4);
		address.setAddressLine5(a5);
		
		return address;
	}

}

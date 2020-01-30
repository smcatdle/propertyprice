/** Property Price Engine   **/

package com.company.propertyprice.model;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class PropertyElement {

    private String productAsString;
    private HtmlElement productAsHtml;
    private String category;
    private String subCategory;
    private String pageLink;
    private String htmlPage;
    
    
    public PropertyElement(String productAsString) {
        this.productAsString = null;
        productAsHtml = null;
        category = null;
        subCategory = null;
        pageLink = null;
        htmlPage = null;
        this.productAsString = productAsString;
    }

    public PropertyElement(HtmlElement productAsHtml) {
        productAsString = null;
        this.productAsHtml = null;
        category = null;
        subCategory = null;
        pageLink = null;
        htmlPage = null;
        this.productAsHtml = productAsHtml;
    }

    public String getProductAsString() {
        return productAsString;
    }

    public void setProductAsString(String productAsString) {
        this.productAsString = productAsString;
    }

    public HtmlElement getProductAsHtml() {
        return productAsHtml;
    }

    public void setProductAsHtml(HtmlElement productAsHtml) {
        this.productAsHtml = productAsHtml;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getHtmlPage() {
        return htmlPage;
    }

    public void setHtmlPage(String htmlPage) {
        this.htmlPage = htmlPage;
    }


}

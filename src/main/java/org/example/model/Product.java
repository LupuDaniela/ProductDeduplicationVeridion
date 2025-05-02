package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String unspsc;
    private String rootDomain;
    private String pageUrl;
    private String productTitle;
    private String productSummary;
    private String productName;
    private List<String> productIdentifier;
    private String brand;

    public Product() {}

    public String getUnspsc()                  { return unspsc; }
    public void   setUnspsc(String u)          { this.unspsc = u; }

    public String getRootDomain()              { return rootDomain; }
    public void   setRootDomain(String r)      { this.rootDomain = r; }

    public String getPageUrl()                 { return pageUrl; }
    public void   setPageUrl(String p)         { this.pageUrl = p; }

    public String getProductTitle()            { return productTitle; }
    public void   setProductTitle(String t)    { this.productTitle = t; }

    public String getProductSummary()          { return productSummary; }
    public void   setProductSummary(String s)  { this.productSummary = s; }

    public String getProductName()             { return productName; }
    public void   setProductName(String n)     { this.productName = n; }

    public List<String> getProductIdentifier() { return productIdentifier; }
    public void   setProductIdentifier(List<String> pi) {
        this.productIdentifier = pi;
    }

    public String getBrand()                   { return brand; }
    public void   setBrand(String b)           { this.brand = b; }



    @Override
    public String toString() {
        return "Product{" +
                "unspsc='" + unspsc + '\'' +
                ", rootDomain='" + rootDomain + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", productSummary='" + productSummary + '\'' +
                ", productName='" + productName + '\'' +
                ", productIdentifier=" + productIdentifier +
                ", brand='" + brand + '\'' +
                '}';
    }
}

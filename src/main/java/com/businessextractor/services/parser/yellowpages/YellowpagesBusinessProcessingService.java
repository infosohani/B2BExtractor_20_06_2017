/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.businessextractor.services.parser.yellowpages;

import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
import com.businessextractor.services.parser.tpage.TPageBusinessProcessingService;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;


import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

/**
 *
 * @author nxsol -1
 */
public class YellowpagesBusinessProcessingService extends HierarchicalBusinessProcessingService{
    	
	private static final Logger logger = Logger.getLogger(TPageBusinessProcessingService.class);

    @Override
    protected List<Business> getBusinessesFromDirectory(Directory directory) {
      
            try {
                String url=directory.getSourceURL();
               
              Document directorySource=Jsoup.connect(url).timeout(60*1000).get();
           
        
       List<Business> businessList = new ArrayList<Business>();
	
		this.getBusinesses(directorySource, businessList,url);
        //System.out.println("yellowpages Source+"+directorySource);
        
        return businessList;
       } catch (IOException ex) {
                System.out.println("com.businessextractor.services.parser.yellowpages.YellowpagesBusinessProcessingService.getBusinessesFromDirectory()"+ ex);
            }
            return null;
    }

    @Override
    protected List<Directory> getSubdirectories(Directory directory) {
         List<Directory> directories = new ArrayList<Directory>();
       Document document;
            try {
                document = Jsoup.connect(directory.getSourceURL()).get();
           
          int j=0;
        Elements ek;
        List<Element> url=document.select("STRONG");
        for(Element urldata:url){
            if(!(urldata.text().equals(""))){
      //      System.out.println("displayempdata.DisplayEmpData.main()"+urldata.childNode(0).nodeName());
            if(!(urldata.childNode(0).childNode(0).nodeName().equals("font"))){
                if(!(urldata.childNode(0).nodeName().equals("font"))){
                     Business business=new Business();
                     
                            
                          
                    ek=urldata.getAllElements();
                    
                    ek=ek.get(0).select("a[href]");
                    String name=ek.get(0).getAllElements().text();
                    
                    
                    String linkHref =ek.attr("href");
            //        System.out.println("displayempdata.DisplayEmpData.main()"+name+linkHref);
                directory=new Directory();                                                        //this was comment because not want to display in a screen 04-05-2017   ypathak
                //  directory.setSourceName(name);
                //  directory.setSourceURL(linkHref);
               //   directory.setActivated(false);
                //  directories.add(directory);
                  j++;
                    
                }
                }
                
            }
            
            
        }
       } catch (Exception ex) {
                System.out.println("Exception  accured bcoz into company no find any directory"+ex);
            }
       return  directories;
    }

    private void getBusinesses(Document doc, List<Business> businessList,String uString) {
        
        try{

        int k=0;

        
        //address city state zip tel url importdate
        List<Element> br=doc.select("br");
        Elements bre;
        for(int i=0; i<br.size();i++){
            if(i<30){
               Business business=new Business();
                
                if(br.get(i).parentNode().nodeName().equals("td")){
                    
                    String ffr=br.get(i).parentNode().toString();
                    
                    String[] n=ffr.split("\\<.*?\\>");
                    String Description=n[1];
                    business.setBusinessDescription(Description);
                    String[] addressdate=n[2].split(",");
                    String address=addressdate[0];
                    String city=addressdate[1];
                    business.setAddress(address);
                    business.setCity(city);
                    
                    
                    String[] StateZipcode=addressdate[2].split(" ");
                    String State=StateZipcode[1];
                    String zipcode=StateZipcode[2];
                    business.setState(State);
                    business.setZipCode(zipcode);
                    
                    
                    String[] split=n[3].split("TEL:");
                    String[] split1=split[1].split("\\|");
                    String Tel=split1[0].toString();
                    business.setPhone(Tel);
                    
                    if(n.length>4){
                        String check=split1[1].substring(1,4);
                        if(check.equals("FAX")){
                            
                            int l=n[3].length();
                            business.setFax(n[4].toString());
                            
                          //  System.out.println("fax++"+n[4]);
                            
                        }
                        else{
                            business.setContactEmail(n[4].toString());
                           // System.out.println("url++"+n[4]);
                            
                            
                        }
                    }
                     business.setImportDate(new Date());
                     businessList.add(k,business);
                     k++;
                }
               
                i++;
            }
        }
        
        //company name ane url
        int j=0;
        Elements ek;
        List<Element> url=doc.select("STRONG");
        for(Element urldata:url){
            if(!(urldata.text().equals(""))){
        //    System.out.println("displayempdata.DisplayEmpData.main()"+urldata.childNode(0).nodeName());
            if(!(urldata.childNode(0).childNode(0).nodeName().equals("font"))){
                if(!(urldata.childNode(0).nodeName().equals("font"))){
                     Business business=new Business();
                     
                            
                          
                    ek=urldata.getAllElements();
                    
                    ek=ek.get(0).select("a[href]");
                    String name=ek.get(0).getAllElements().text();
                    
                    
                    String linkHref =ek.attr("href");
            //        System.out.println("displayempdata.DisplayEmpData.main()"+name+linkHref);
                
                  
                  
                   
                     business=businessList.get(j);
                     businessList.remove(j);
                      business.setBusinessTitle(name);
                    business.setBusinessDomain(linkHref);
                     businessList.add(j,business);
                     j++;
                    
                }
                }
                
            }
            
        }
     //   businessList.add(business);
        //next page
        Elements elw;
        elw=doc.getElementsByTag("p");
        elw=elw.get(0).getElementsByTag("Strong");
        
         int number=Integer.parseInt(elw.text());
                    int ratio=number%5;
                      if(ratio==0)
                      {
                              Elements imageurl=elw.get(0).parent().parent().parent().getAllElements();
                                  Elements data=imageurl.select("img").get(1).parent().getElementsByTag("a");
                                  String nextpagelink=data.attr("href");
                          
                                  
            try {
                doc=Jsoup.connect("http://yp.koreadaily.com"+nextpagelink).referrer(uString).timeout(60*1000).get();
               
                this.getBusinesses(doc, businessList, uString);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(YellowpagesBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
            }
                       
                                
                      
                      } else{
        
        
        Element da=elw.get(0).nextElementSibling();
        if(da!=null){
        
        if(da.getAllElements().get(0).nodeName().equals("a")){
            elw=da.getAllElements();
            
            elw=elw.get(0).select("a[href]");
            String nextpagelink =elw.attr("href");
            try {
                doc=Jsoup.connect("http://yp.koreadaily.com"+nextpagelink).referrer(uString).timeout(60*1000).get();
                
                this.getBusinesses(doc, businessList, uString);
                
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(YellowpagesBusinessProcessingService.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
                      }
        
        }
    }catch(Exception e){
            System.out.println("exception occured bcoz into the company not able to scripting"+e);
  }
    }
      
    }

  
    


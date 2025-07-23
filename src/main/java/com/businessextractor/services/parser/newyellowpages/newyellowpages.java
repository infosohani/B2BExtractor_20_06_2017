/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.businessextractor.services.parser.newyellowpages;

import com.businessextractor.datascraper.YellowPagesScraper;
import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author nxsol -1
 */
public class newyellowpages extends HierarchicalBusinessProcessingService{
    @Override
    protected List<Business> getBusinessesFromDirectory(Directory directory) {
        try {
//            Document document=Jsoup.connect(directory.getSourceURL()).timeout(60*1000).get();
//            List<Business> lbBusinesses=new ArrayList<Business>();
//
//             getbusinessdetail(document,lbBusinesses);
            YellowPagesScraper scraper = new YellowPagesScraper();
            List<Business> lbBusinesses=scraper.getData(directory.getSourceURL());
            return  lbBusinesses;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("exception in new yellopages.com");
        }
        
         return  null;
    }

    @Override
    protected List<Directory> getSubdirectories(Directory directory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Business> getbusinessdetail(Document document, List<Business> lbBusinesses) {
        
        Elements listalldata=document.getElementsByClass("search-results organic");
        Elements allcompany=listalldata.get(0).getElementsByClass("result");
        for(Element companyoenbyone :allcompany){
            Elements number=companyoenbyone.getElementsByClass("n");
            System.out.println("com.businessextractor.services.parser.newyellowpages.newyellowpages.getbusinessdetail()"+number);
            
              Business business=new Business();
                  String comapnyname=companyoenbyone.getElementsByClass("business-name").text();
                
                  business.setBusinessTitle(comapnyname);
                  
                   String businessdomain="https://www.yellowpages.com"+companyoenbyone.getElementsByClass("business-name").select("a").attr("href");
                   business.setBusinessDomain(businessdomain);
                  try{
                  String address=companyoenbyone.getElementsByClass("street-address").text();
                 
                  business.setAddress(address);
                  }catch(Exception e){System.out.println("address not availble++++++++");}
                  
                  try{
                  String city=companyoenbyone.getElementsByClass("locality").text();
                   
                  business.setCity(city);
                  }catch(Exception e){
                      System.out.println("city not availbble+++++++++++++++");
                  }
                  try{
                  String coun=companyoenbyone.getElementsByClass("locality").get(0).nextElementSibling().text();
                  
                  business.setCountry(coun);
                  }catch(Exception e){
                      System.out.println("country not availble++++++++++++++");
                  }
                  try{
                  String zipo=companyoenbyone.getElementsByClass("locality").get(0).nextElementSibling().nextElementSibling().text();
                  business.setZipCode(zipo);
                  }catch(Exception e){
                      System.out.println("Zip code not availble++++++++++++++++++");
                  }
                  try{
                   String phonenumber=companyoenbyone.getElementsByClass("phones phone primary").text();
                   business.setPhone(phonenumber);
                  }catch(Exception e){
                      System.out.println(" phone number not availble++++++");
                  
                  }
                  
                  try{
                       Elements websiteurl=companyoenbyone.getElementsByClass("track-visit-website");
                       String url=websiteurl.get(0).select("a[href]").attr("href");
                   business.setBusinessEmail(url);
                       System.out.println("com.businessextractor.services.parser.newyellowpages.newyellowpages.getbusinessdetail()"+url);
                   }
                   catch(Exception e){
                       System.out.println("web site not available++++++++++++++++++");
                   }
                   
                   lbBusinesses.add(business);
        }  
        try{ 
        if(document.getElementsByClass("next ajax-page").size()!=0){
        String nextpagelink =document.getElementsByClass("next ajax-page").select("a").attr("href");
        String finallink=nextpagelink.replace("%25", "%");
        
            document=Jsoup.connect("https://www.yellowpages.com"+finallink).timeout(80*1000).get();
            this.getbusinessdetail(document, lbBusinesses);
        } 
    }catch (IOException ex) {
            System.out.println("++++++++ no new page availble+++++++++++++++");
        }
        
        
        return lbBusinesses;
}
    
}

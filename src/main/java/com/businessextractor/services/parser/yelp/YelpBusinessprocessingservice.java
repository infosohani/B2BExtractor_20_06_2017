/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.businessextractor.services.parser.yelp;

import com.businessextractor.datascraper.YelpScraper;
import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.directory.Directory;
import com.businessextractor.services.parser.HierarchicalBusinessProcessingService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 *
 * @author nxsol -1
 */
public class YelpBusinessprocessingservice extends HierarchicalBusinessProcessingService{

    @Override
    protected List<Business> getBusinessesFromDirectory(Directory directory) {
        try {
//            Document document=Jsoup.connect(directory.getSourceURL()).timeout(60*1000).get();
//
              List<Business> lbBusinesses=new ArrayList<Business>();
//              getBusinessdata(document,lbBusinesses);
                YelpScraper yelpScraper = new YelpScraper();
                lbBusinesses = yelpScraper.findAllData(directory.getSourceURL());
               return  lbBusinesses;
              } catch (Exception ex) {
                System.out.println("exception in a yelp page++++++++++++++++");
                ex.printStackTrace();
        }
          return  null;
    }
    

    @Override
    protected List<Directory> getSubdirectories(Directory directory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Business> getBusinessdata(Document source, List<Business> lbBusinesses) {
        
        
     
        Elements listall=source.getElementsByClass("regular-search-result");
        for(Element listonebyoen:listall){
            Business business=new Business();
            Elements namefetch=listonebyoen.getElementsByTag("span");
                            String BusinessDescription =namefetch.select("a[href]").text();
                            business.setBusinessDescription(BusinessDescription);
                           
                String businessdomain=namefetch.get(0).select("a").attr("href");
                
                      businessdomain.replace("Spas", "spac");
                
                business.setBusinessDomain("https://www.yelp.com"+businessdomain);
                
            try {
                Document interpage=Jsoup.connect("https://www.yelp.com"+businessdomain).get();
                Elements inner=interpage.getElementsByClass("mapbox-container");
                Elements classd=inner.get(0).getElementsByClass("offscreen");
             
               if(classd.size()==2){
                
                Elements siteurl=classd.get(1).parent().getAllElements();
                
                 
                String businessurl=siteurl.get(0).select("a[href]").text();
                
              business.setBusinessEmail(businessurl);
                
                }
            } catch (IOException ex) {
                System.out.println("business url not availble+++++++++++++++");
            }
                 
                 
            System.out.println("comapny name "+namefetch);
              String companyname=namefetch.get(1).text();
              
              business.setBusinessTitle(companyname);
              
              
              Elements category=listonebyoen.getElementsByClass("category-str-list");
              Elements elementcategory=category.get(0).getElementsByTag("a");
              for(Element onebyonecategory:elementcategory){
                    String categoryname=onebyonecategory.text();
                    System.out.println("com.businessextractor.services.parser.yelp.YelpBusinessprocessingservice.getBusinessdata()"+categoryname);
        }
             Elements getaddressorphone=listonebyoen.getElementsByClass("secondary-attributes");
         //     Elements city=getaddressorphone.get(0).getElementsByClass("neighborhood-str-list");
          //    String cityname=city.get(0).text();
              String phonenumber=getaddressorphone.get(0).getElementsByClass("biz-phone").get(0).text();          //phone number
              
              business.setPhone(phonenumber);
              try{
              Element adress=getaddressorphone.get(0).getElementsByTag("address").get(0);
                     String[] add=adress.getAllElements().toString().split("<br>");
                                String[] addr=add[0].split("<address>");
                                String finaladdress=addr[1].trim();               //address
                               
              business.setAddress(finaladdress);
                                String[] citynameand=add[1].split("</address>");
                                String finalcitynameandpin=citynameand[0].trim();
                                String[] citywithpin=finalcitynameandpin.split(",");
                                String City=citywithpin[0];             //city
                                
             business.setCity(City);
                                String[] countrywithpin=citywithpin[1].split(" ");
                                String state=countrywithpin[1];               //country
                                
             business.setState(state);
             
                                String pin=countrywithpin[2];                   //pin
                                
            business.setZipCode(pin);
              }catch(Exception e){
                  System.out.println("com.businessextractor.services.parser.yelp.YelpBusinessprocessingservice.getBusinessdata()++++++++exception+++++");
              }
                                
              lbBusinesses.add(business);
              
    }
       Elements currentpagenumber=source.getElementsByClass("arrange_unit page-option current");
       if(currentpagenumber.get(0).nextElementSibling()!=null){
        Element nxtpageDiv=currentpagenumber.get(0).nextElementSibling();
        Elements findnextpagelink=nxtpageDiv.getElementsByTag("a");
        String nextpage=findnextpagelink.get(0).attr("href");
       String nextpagelink="https://www.yelp.com"+nextpage;
        String newlonio=nextpagelink.replace("%2C", ",");
        try {
            source=Jsoup.connect(newlonio).timeout(60*1000).get();

            this.getBusinessdata(source, lbBusinesses);
        } catch (IOException ex) {
            System.out.println("next page not availble+++++++++++++++++++++============");
        }
      
    }
       
       return lbBusinesses;
}
}
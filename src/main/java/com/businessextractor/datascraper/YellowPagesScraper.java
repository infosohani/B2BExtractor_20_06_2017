package com.businessextractor.datascraper;

import com.businessextractor.entity.business.Business;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YellowPagesScraper {
    public  List<Business> getData(String url){
        List<Business> businesses = new ArrayList<>();
        try {
            InputStream exeStream = RunPythonEXE.class.getClassLoader().getResourceAsStream("yellowpages_scraper.exe");
            if (exeStream == null) {
                throw new FileNotFoundException("EXE file not found in resources!");
            }
            File tempExe = File.createTempFile("yellowpages_scraper", ".exe");
            tempExe.deleteOnExit(); // Delete after execution
            Files.copy(exeStream, tempExe.toPath(), StandardCopyOption.REPLACE_EXISTING);
            ProcessBuilder pb = new ProcessBuilder(tempExe.getAbsolutePath(), url);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Business currentBusiness = null;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Business Name:")) {
                    if (currentBusiness != null) businesses.add(currentBusiness);
                    currentBusiness = new Business();
                    currentBusiness.setBusinessTitle(line.replace("Business Name:", "").trim());
                } else if (line.startsWith("Phone:")) {
                    if (currentBusiness != null) currentBusiness.setPhone(line.replace("Phone:", "").trim());
                } else if (line.startsWith("Street Address:")) {
                    if (currentBusiness != null) currentBusiness.setAddress(line.replace("Street Address:", "").trim());
                } else if (line.startsWith("Locality:")) {
                    if (currentBusiness != null) {
                        String locality = line.replace("Locality:", "").trim();

                        // Regex to extract city, state, and ZIP
                        Pattern pattern = Pattern.compile("^(.*?),[\\s]*([A-Z]{2})[\\s]*(\\d{5})$");
                        Matcher matcher = pattern.matcher(locality);

                        if (matcher.matches()) {
                            currentBusiness.setCity(matcher.group(1).trim());
                            currentBusiness.setState(matcher.group(2).trim());
                            currentBusiness.setZipCode(matcher.group(3).trim());
                            currentBusiness.setCountry("United States");
                        }
                    }
                } else if (line.startsWith("Website:")) {
                    if (currentBusiness != null) currentBusiness.setSourceUrl(line.replace("Website:", "").trim());
                    if (currentBusiness != null) currentBusiness.setBusinessDomain(line.replace("Website:", "").trim());
                } else if (line.startsWith("Description:")) {
                    if (currentBusiness != null) currentBusiness.setBusinessDescription(line.replace("Description:", "").trim());
                } else if (line.startsWith("--------------------------------------------------")) {
                    if (currentBusiness != null) {
                        businesses.add(currentBusiness);
                        currentBusiness = null;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return businesses;
    }
}

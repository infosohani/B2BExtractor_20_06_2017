package com.businessextractor.datascraper;

import com.businessextractor.entity.business.Business;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class RunPythonEXE {
    public static void main(String[] args) {
        try {
            // Load EXE from resources
            InputStream exeStream = RunPythonEXE.class.getClassLoader().getResourceAsStream("yellowpages_scraper_old.exe");

            if (exeStream == null) {
                throw new FileNotFoundException("EXE file not found in resources!");
            }

            // Create a temporary file
            File tempExe = File.createTempFile("yellowpages_scraper_provide_url", ".exe");
            tempExe.deleteOnExit(); // Delete after execution

            // Copy EXE from resources to temp file
            Files.copy(exeStream, tempExe.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // URL to scrape
            String url = "https://www.yellowpages.com/search?search_terms=spa&geo_location_terms=Los%20Angeles%2C%20CA";

            // Run the extracted EXE with the URL argument
            ProcessBuilder pb = new ProcessBuilder(tempExe.getAbsolutePath(), url);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output from EXE
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<Business> businesses = new ArrayList<>();
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
                } else if (line.startsWith("Address:")) {
                    if (currentBusiness != null) currentBusiness.setAddress(line.replace("Address:", "").trim());
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

            // Wait for process completion
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Print all businesses
            System.out.println("Scraped Businesses:");
            for (Business business : businesses) {
                System.out.println(business);
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

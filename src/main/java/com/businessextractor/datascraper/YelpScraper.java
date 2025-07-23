package com.businessextractor.datascraper;

import com.businessextractor.entity.business.Business;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YelpScraper {
    private static final String API_URL = "https://api.zenrows.com/v1/";
    private static final String API_KEY = "aecd1fa07831dde32218b1ed8fb9ccd5d143c954";
    private static final String BASE_URL = "https://www.yelp.com";

    public List<Business> findAllData(String bizUrl) throws IOException {
        List<Business> businesses = new ArrayList<>();
        String jsonResponse = fetchPageContent(bizUrl);
        if (jsonResponse == null) {
            System.out.println("❌ Failed to fetch details for " + bizUrl);
        }

        Document bizDoc = Jsoup.parse(jsonResponse);

        // 1. Find the heading node
        Element heading = bizDoc.selectFirst("h2.y-css-o6ctds:contains(All \"general contractor\")");

        if (heading != null) {
            // 2. Traverse up to the nearest <li> containing this heading
            Element parentLi = heading.closest("li.y-css-mhg9c5");

            // 3. Collect all <li> siblings AFTER this one
            Elements allLi = parentLi.parent().select("li.y-css-mhg9c5");
            List<Element> resultList = new ArrayList<>();

            boolean startCollecting = false;
            int co = 1;
            for (Element li : allLi) {
                if (startCollecting && resultList.size() < 2) {
                    resultList.add(li);
                }
                if (li.equals(parentLi)) {
                    startCollecting = true;
                }
                co++;
            }

            List<String> businessLinks = new ArrayList<>();

            for (Element result : resultList) {
                Element linkElements = result.selectFirst("div.businessName__09f24__HG_pC.y-css-mhg9c5");
                Element linkElement = linkElements.selectFirst("a[href]");
                if (linkElement != null) {
                    String href = linkElement.attr("href");
                    String fullUrl = "https://www.yelp.com" + href; // if it's a relative URL
                    businessLinks.add(fullUrl);
                    System.out.println("🔗 Business Link: " + fullUrl);
                }
            }
            for (String url : businessLinks) {
                Business business = fetchBusinessDetails(url);
                businesses.add(business);
            }
            Business business11 = fetchBusinessDetails("http://yelp.com/biz/ummas-k-bbq-lake-forest-2?osq=ummas");
            businesses.add(business11);
        } else {
            System.out.println("❌ Heading not found.");
        }
        return businesses;
    }
    private static Business fetchBusinessDetails(String bizUrl) throws IOException {
        Business business = new Business();
        String jsonResponse = fetchPageContent(bizUrl);
        if (jsonResponse == null) {
            System.out.println("❌ Failed to fetch details for " + bizUrl);
            return null;
        }

        Document bizDoc = Jsoup.parse(jsonResponse);

        String fullAddress = "";
        Element addressElement = bizDoc.selectFirst("address");
        if (addressElement != null) {
            fullAddress = addressElement.text();
            System.out.println("📍 Address: " + fullAddress);
        } else {
            System.out.println("⚠️ Address not found");
        }

        // Select the table containing hours
        Elements hourTables = bizDoc.select("table.hours-table__09f24__KR8wh");
        StringBuilder workingHoursString = new StringBuilder();
        for (Element table : hourTables) {
            Elements uls = table.select("ul.list__09f24__ynIEd");

            List<String> workingHoursList = new ArrayList<>();

            for (Element ul : uls) {
                // You can also extract the weekday label if present
                Elements lis = ul.select("li");

                for (Element li : lis) {
                    Elements timeParagraphs = li.select("p");

                    for (Element p : timeParagraphs) {
                        String timeRange = p.text();
                        System.out.println(timeRange);
                        String time = p.text();
                        workingHoursList.add(time);
                        workingHoursString.append(time).append("\n");
                    }
                }
            }
        }


        Element table = bizDoc.selectFirst("table[class=y-css-16hyog2]");
        StringBuilder upcomingSpecialHours = new StringBuilder();

        if (table == null) {
            System.out.println("❌ Table not found.");
        }else {

            Elements rows = table.select("tr");

            for (Element row : rows) {
                Element dateElement = row.selectFirst("th > p");
                Element timeElement = row.selectFirst("td ul li p");

                if (dateElement != null && timeElement != null) {
                    String date = dateElement.text();
                    String time = timeElement.text();

                    upcomingSpecialHours.append(date).append(": ").append(time).append("\n");
                }
            }
        }

        String businessName = bizDoc.select("h1").text();
        String sidebarText = bizDoc.select("div[data-testid=sidebar-content]").text();

        Elements pElements = bizDoc.select("div[data-testid=sidebar-content] p.y-css-qn4gww");
        Elements addressElements = bizDoc.select("div[data-testid=sidebar-content] p.y-css-p0gpmm");

        List<String> extractedTexts = new ArrayList<>();
        for (Element p : pElements) {
            extractedTexts.add(p.text());  // Add each paragraph's text to the list
        }

        // Initialize website and phone with default values
        String website = "N/A";
        String phoneNumber = "N/A";
        String openCloseHours = bizDoc.select("div.photo-header-content__09f24__q7rNO span.y-css-qavbuq").text();
        String address = addressElements.text();
        // Verify if elements exist in expected positions
        if (extractedTexts.size() > 0) {
            String firstElement = extractedTexts.get(0);
            if (firstElement.contains(".com") || firstElement.contains(".net") || firstElement.contains(".org")) {
                website = firstElement; // Assume first index contains website
            }
        }

        if (extractedTexts.size() > 1) {
            String secondElement = extractedTexts.get(1);
            if (secondElement.matches(".*\\(\\d{3}\\) \\d{3}-\\d{4}.*")) {
                phoneNumber = secondElement; // Assume second index contains phone number
            }
        }


        Elements imageElements = bizDoc.select("div.carousel__09f24__HJrqN img");
        List<String> imageUrls = new ArrayList<>();
        for (Element img : imageElements) {
            String imgUrl = img.attr("src");
            if (imgUrl.matches("(?i).+\\.(png|jpe?g)$")) {
                imageUrls.add(imgUrl);
            }
        }

        business.setBusinessTitle(businessName);
        business.setBusinessDescription(businessName);
        business.setBusinessDomain(website);
        business.setPhone(phoneNumber);
        business.setOpenCloseHours(openCloseHours);
        business.setImages(String.join(",", imageUrls));
        business.setLocation(fullAddress);
        business.setHours(workingHoursString.toString());
        business.setUpcomingSpecialHours(upcomingSpecialHours.toString());
        // Regular expression pattern
        Pattern pattern = Pattern.compile("^(.*) ([A-Za-z ]+), ([A-Z]{2}) (\\d{5})$");
        Matcher matcher = pattern.matcher(address);

        if (matcher.matches()) {
            String street = matcher.group(1).trim();
            String city = matcher.group(2).trim();
            String state = matcher.group(3).trim();
            String zipCode = matcher.group(4).trim();
            business.setAddress(street);
            business.setCity(city);
            business.setState(state);
            business.setZipCode(zipCode);
        } else {
            System.out.println("Invalid address format.");
        }

        return business;
    }
    private static String fetchPageContent(String targetUrl) throws IOException {
        String apiUrl = API_URL + "?url=" + URLEncoder.encode(targetUrl, String.valueOf(StandardCharsets.UTF_8)) +
                "&apikey=" + API_KEY + "&premium_proxy=true&js_render=true";

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        return null;
    }

}

from selenium import webdriver
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
import time

url = "https://www.yellowpages.com/search?search_terms=spa&geo_location_terms=Los%2520Angeles%252C%2520CA"

# Set up Chrome driver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

options = webdriver.ChromeOptions()
options.add_argument("--headless")
options.add_argument("--no-sandbox")
options.add_argument("--disable-dev-shm-usage")
options.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=options)

try:
    driver.get(url)
    time.sleep(5)  # Wait for the page to load

    soup = BeautifulSoup(driver.page_source, "html.parser")
    listings = soup.find_all("div", class_="result")

    for listing in listings:
        title = listing.find("a", class_="business-name")
        address = listing.find("p", class_="adr")
        description = listing.find("div", class_="snippet")

        if title:
            print(f"Business Name: {title.text.strip()}")

        if address:
            print(f"Address: {address.text.strip()}")

        if description:
            print(f"Description: {description.text.strip()}")

        # Email scraping would typically require visiting each business page, as it's not in the main listing
        print("-" * 50)

finally:
    driver.quit()

# Let me know if you want me to navigate into individual business pages to try scraping emails! 🚀

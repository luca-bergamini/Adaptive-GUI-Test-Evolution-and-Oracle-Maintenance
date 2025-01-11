from appium import webdriver
import unittest
import time

class ThunderBirdTests(unittest.TestCase):
    
    # Set up the Appium driver
    def setUp(self):
        # Desired capabilities for the Appium server
        self.desired_caps = {
            'platformName': 'Android',
            'platformVersion': '11.0',  # Change as per your device/emulator
            'deviceName': 'Android Emulator',  # Change as per your device/emulator
            'appPackage': 'net.thunderbird.android.debug',
            'appActivity': 'net.thunderbird.android.debug.MainActivity',  # Change as needed
            'automationName': 'UiAutomator2',
        }
        # Starting Appium session
        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', self.desired_caps)
    
    # Tear down the Appium driver
    def tearDown(self):
        # Quit the Appium session
        self.driver.quit()

    # Test: Verify the presence of UI components
    def test_ui_components_presence(self):
        # Wait for the main view to load
        time.sleep(3)
        # Verify presence of "Thunderbird" title
        self.assertTrue(self.driver.find_element_by_xpath("//android.widget.TextView[@text='Thunderbird']"), "Thunderbird title not found")
        # Verify presence of "Migliora l'esperienza" subtitle
        self.assertTrue(self.driver.find_element_by_xpath("//android.widget.TextView[@text='Migliora l'esperienza']"), "Migliora l'esperienza subtitle not found")
        # Verify presence of "Contatti" text view
        self.assertTrue(self.driver.find_element_by_xpath("//android.widget.TextView[@text='Contatti']"), "Contatti text view not found")
    
    # Test: Test button click for 'Consenti' (contacts access)
    def test_consenti_button_click(self):
        # Wait for the view to load
        time.sleep(3)
        # Locate 'Consenti' button and click
        consenti_button = self.driver.find_element_by_xpath("//android.view.View[@text='Consenti']")
        consenti_button.click()
        # Additional assertion can be done here to verify the change in UI/state
    
    # Test: Test button click for 'Consenti' (notifications)
    def test_enable_notifications(self):
        # Wait for the view to load
        time.sleep(3)
        # Locate notification consent button
        enable_notifications_button = self.driver.find_element_by_xpath("//android.view.View[@text='Consenti']")
        enable_notifications_button.click()
        # Additional assertions can be made here to ensure notifications are enabled

    # Test: Validate navigation to the 'Salta' button
    def test_navigation_to_salva(self):
        # Wait for the view to load
        time.sleep(3)
        # Locate 'Salta' button
        salta_button = self.driver.find_element_by_xpath("//android.view.View[@text='Salta']")
        salta_button.click()
        # Additional assertions can be made here to verify navigation to the next screen

if __name__ == '__main__':
    # Run the tests
    unittest.main()
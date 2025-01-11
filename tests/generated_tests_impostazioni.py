from appium import webdriver
from appium.webdriver.common.touch_action import TouchAction
import unittest

class ThunderBirdUITests(unittest.TestCase):

    def setUp(self):
        # Setup the desired capabilities for the Appium driver
        self.desired_caps = {
            'platformName': 'Android',
            'deviceName': 'Android Emulator',
            'appPackage': 'net.thunderbird.android.debug',
            'appActivity': 'net.thunderbird.android.debug.MainActivity',  # Replace with the correct main activity
            'noReset': True
        }
        # Initialize the Appium driver
        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', self.desired_caps)

    def tearDown(self):
        # Close the Appium session
        self.driver.quit()

    def test_verify_ui_components(self):
        # Verify the presence of the toolbar
        toolbar = self.driver.find_element_by_id('net.thunderbird.android.debug:id/toolbar')
        self.assertIsNotNone(toolbar, "Toolbar is not present")
        
        # Verify the presence of the 'Impostazioni' text
        impostazioni_text = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Impostazioni']")
        self.assertIsNotNone(impostazioni_text, "'Impostazioni' text is not present")
        
        # Verify the presence of 'Account' option
        account_option = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Account']")
        self.assertIsNotNone(account_option, "'Account' option is not present")
        
        # Verify the presence of 'Backup' option
        backup_option = self.driver.find_element_by_id('net.thunderbird.android.debug:id/text')
        self.assertIsNotNone(backup_option, "'Backup' option is not present")
        
        # Verify the presence of the 'Supporto' text
        supporto_text = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Supporto']")
        self.assertIsNotNone(supporto_text, "'Supporto' text is not present")

    def test_basic_interactions(self):
        # Click on the 'Impostazioni' option
        impostazioni_text = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Impostazioni']")
        impostazioni_text.click()
        
        # Verify we navigate to settings page
        self.assertIn('Settings', self.driver.page_source)
        
        # Click on 'Account' option
        account_option = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Account']")
        account_option.click()
        
        # Verify we navigate to account settings
        self.assertIn('Account Settings', self.driver.page_source)

    def test_validate_navigation(self):
        # Navigate to settings
        impostazioni_text = self.driver.find_element_by_xpath("//android.widget.TextView[@text='Impostazioni']")
        impostazioni_text.click()
        
        # Assert we are on the correct screen
        self.assertTrue(self.driver.find_element_by_xpath("//android.widget.TextView[@text='Impostazioni']"), "Failed to navigate to Settings Screen")
        
        # Navigate back
        back_button = self.driver.find_element_by_accessibility_id("Torna indietro")
        back_button.click()
        
        # Assert we are back on the main screen
        self.assertTrue(self.driver.find_element_by_xpath("//android.widget.TextView[@text='Impostazioni']"), "Failed to navigate back to main screen")

if __name__ == '__main__':
    # Execute the tests
    unittest.main()
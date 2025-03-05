import time
import os
from appium import webdriver
from appium.options.android import UiAutomator2Options

# Crea le cartelle se non esistono già
os.makedirs("jpgScreen", exist_ok=True)
os.makedirs("xmlScreen", exist_ok=True)

options = UiAutomator2Options()
options.platform_name = "Android"
options.device_name = "BPN0218A19004767"  # Nome del dispositivo da `adb devices`
options.app = "v.6.3.0.apk"  # Percorso assoluto corretto all'APK
options.automation_name = "UiAutomator2"

# Connessione al server Appium
driver = webdriver.Remote("http://127.0.0.1:4723", options=options)

# Aspetta l'avvio dell'app
time.sleep(5)

# Imposta un contatore per i nomi dei file
counter = 1

try:
    while True:
        # Screenshot XML
        xml_source = driver.page_source
        xml_filename = f"xmlScreen/screen{counter}.xml"  # Salva in xmlScreen
        with open(xml_filename, "w", encoding="utf-8") as f:
            f.write(xml_source)
        print(f"✅ Screenshot XML salvato come {xml_filename}")

        # Screenshot JPG
        screenshot_filename = f"jpgScreen/screen{counter}.jpg"  # Salva in jpgScreen
        driver.get_screenshot_as_file(screenshot_filename)
        print(f"✅ Screenshot JPG salvato come {screenshot_filename}")

        # Incrementa il contatore per il prossimo file
        counter += 1

        # Aspetta 5 secondi prima di acquisire il prossimo screenshot
        time.sleep(5)

except KeyboardInterrupt:
    print("🔴 Acquisizione interrotta dall'utente.")

# Chiudi Appium
driver.quit()
print("🔴 Fine della sessione Appium")

import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import matplotlib.patches as patches

def parse_bounds(bounds):
    """Estrae le coordinate dai bounds in formato [x1,y1][x2,y2]."""
    bounds = bounds.strip("[]").split("][")
    x1, y1 = map(int, bounds[0].split(","))
    x2, y2 = map(int, bounds[1].split(","))
    return x1, y1, x2, y2

def visualize_ui_dump(xml_path):
    """
    Visualizza il layout di un file XML di uiautomator.
    """
    tree = ET.parse(xml_path)
    root = tree.getroot()

    # Configura il layout della figura
    fig, ax = plt.subplots(figsize=(10, 15))
    ax.set_xlim(0, 1080)  # Risoluzione tipica di uno schermo Android
    ax.set_ylim(1920, 0)  # L'origine è in alto a sinistra
    ax.set_title("Anteprima Layout UI")
    ax.set_xlabel("X")
    ax.set_ylabel("Y")

    # Itera su tutti i nodi e disegna i rettangoli
    for node in root.iter("node"):
        bounds = node.attrib.get("bounds")
        if bounds:
            x1, y1, x2, y2 = parse_bounds(bounds)
            width = x2 - x1
            height = y2 - y1
            
            # Disegna il rettangolo per il nodo
            rect = patches.Rectangle((x1, y1), width, height, linewidth=1, edgecolor='blue', facecolor='none')
            ax.add_patch(rect)

            # Aggiungi il testo associato al nodo (se presente)
            text = node.attrib.get("text", "").strip()
            if text:
                ax.text(x1 + 5, y1 + 15, text, fontsize=8, color="red")

    plt.gca().invert_yaxis()  # L'interfaccia Android ha l'origine in alto a sinistra
    plt.show()

# Percorso del file XML
xml_path = "layouts/old/new-note.xml"  # Cambia con il percorso del tuo file XML
visualize_ui_dump(xml_path)

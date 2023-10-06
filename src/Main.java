import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            File sales = new File("sales.xml"); //ruta del archivo de ventas XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(sales);
            document.getDocumentElement().normalize();

            Scanner sc = new Scanner(System.in); //scanner para los porcentajes y departamento
            System.out.println("Ingrese el porcentaje del 5% al 15% al que desea hacer el aumento de ventas");
            double percentage = sc.nextDouble();    //porcentaje del 5 al 15
            if (percentage < 5 || percentage > 15) {
                System.out.println("El porcentaje debe estar entre 5% y 15%.");
                return;
            }
            System.out.println("Ingrese el departamento al que desea aumentar las ventas");
            String department = sc.next();
            sc.close();

            NodeList list = document.getElementsByTagName("sale_record");   //tag sale_record del xml
            boolean departamentoEncontrado = false;

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String departmentName = element.getElementsByTagName("department").item(0).getTextContent();    //tag del departamento del xml
                    if (departmentName.equals(department)) {
                        departamentoEncontrado = true;
                        NodeList salesList = element.getElementsByTagName("sales");
                        for (int j = 0; j < salesList.getLength(); j++) {
                            Element elementSales = (Element) salesList.item(j);
                            double actualSales = Double.parseDouble(elementSales.getTextContent());

                            double newSales = actualSales * (1 + (percentage / 100));
                            elementSales.setTextContent(String.format("%.2f", newSales));       //decimales de dos caracteres de longitud
                        }
                    }
                }
            }

            if (departamentoEncontrado) {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer trans = tFactory.newTransformer();
                DOMSource domSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(new File("new_sales.xml"));
                trans.transform(domSource, streamResult);          //transforma el XML original al nuevo "new_sales.xml"
                System.out.println("Se generó el nuevo archivo 'new_sales.xml'");
            } else {
                System.out.println("El departamento no se encontró en el archivo XML.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
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

        try{
            File inputFile = new File("src/sales.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document =  builder.parse(inputFile);
            document.getDocumentElement().normalize();

            Scanner sc = new Scanner(System.in);
            System.out.println("Ingrese el porcentaje del 5% al 15%");
            double percentaje = sc.nextDouble();
            System.out.println("Ingrese el departamento");
            String department = sc.next();
            sc.close();

            NodeList list = document.getElementsByTagName("department");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String departmentName = element.getAttribute("first_name");
                    if (departmentName.equals(department)){
                        NodeList salesList = element.getElementsByTagName("sales");
                        for (int j = 0; j < salesList.getLength(); j++) {
                            Element elementSales = (Element) salesList.item(j);
                            double actualSales = Double.parseDouble(elementSales.getTextContent());
                            double newSales = actualSales * (1 + (percentaje / 100));
                            elementSales.setTextContent(String.valueOf(newSales));
                        }
                    }
                }
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("new_sales.xml"));
            transformer.transform(source, result);

            System.out.println("Se genero el nuevo archivo");


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
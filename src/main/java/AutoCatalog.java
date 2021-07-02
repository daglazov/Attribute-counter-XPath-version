import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AutoCatalog {
    public static void main(String[] args) {
        String xmlName;

        if (args.length == 1) {
            xmlName = args[0];
        }else {
            System.out.println("Specify XML file name in this directory");
            return;
        }

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        Document doc;

        try (InputStream is = new FileInputStream(xmlName)){
            docBuilder = builderFactory.newDocumentBuilder();
            doc = docBuilder.parse(is);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            System.out.println("The number of modification tags with unique name attribute is " +
                countAttribute(doc, xpath));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String countAttribute(Document doc, XPath xpath){
        Integer ret = null;
        try{
            XPathExpression expr = xpath.compile(
                    "//modification[not(@name = preceding::modification/@name)]"
            );
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            ret = nodes.getLength();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }
}

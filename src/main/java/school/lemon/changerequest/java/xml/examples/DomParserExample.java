package school.lemon.changerequest.java.xml.examples;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class DomParserExample {

    private static final String TEST_XML = "complexTypesTnsExample.xml";
    private static final String TEST_XSD = "complexTypesTnsExample.xsd";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(TEST_XSD));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setSchema(schema);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException e) throws SAXException {
                throw e; // <-- throw exception if XML document is NOT valid
            }
        });
        Document document = db.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_XML));
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
        Element rootElement = document.getDocumentElement();
        System.out.println("Root tag is: " + rootElement.getTagName());
        printNodes("", rootElement.getChildNodes());
    }

    private static void printNodes(String indent, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);
            if (child.hasChildNodes()) {
                System.out.println(String.format("%s %s:", indent, child.getNodeName()));
                printNodes(indent + " ", child.getChildNodes());
            } else {
                System.out.println(String.format("%s %s", indent, child.getNodeValue()));
            }
        }
    }

}

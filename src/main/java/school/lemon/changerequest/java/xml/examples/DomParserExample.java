package school.lemon.changerequest.java.xml.examples;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;

public class DomParserExample {
    private static final String TEST_XML = "complexTypesTnsExample.xml";
    private static final String TEST_XSD = "complexTypesTnsExample.xsd";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        db.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException e) throws SAXException {
                throw e; // <-- throw exception if XML document is NOT valid
            }
        });
        Document document = db.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_XML));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(TEST_XSD));
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
        Element rootElement = document.getDocumentElement();
        System.out.println("Root tag is: " + rootElement.getTagName());
        NodeList childNodes = rootElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            System.out.println(String.format("%d child tag name: %s", i + 1, child.getNodeName()));
        }
    }
}

package ua.eurosoftware.jaxbmavenproject2.filters;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

public class EmployeeFilter extends XMLFilterImpl {

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        super.endElement(null, localName, qName);
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {

        super.startElement(null, localName, qName, atts);

    }

}

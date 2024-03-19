package org.hlumak.connector;

import org.hlumak.dto.DOMDTO;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.XMLConstants;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DOMConnector implements Connector<DOMDTO> {
    @Override
    public DOMDTO read(String path) {
        SAXBuilder sax = new SAXBuilder();

        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Document doc = null;
        try {
            doc = sax.build(new File(path));
        } catch (JDOMException | IOException e) {
            System.out.println(e.getMessage());
        }

        return doc != null ? new DOMDTO(doc) : null;
    }

    @Override
    public void write(String path, DOMDTO dto) {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        try (FileWriter fileWriter = new FileWriter(path)) {
            xmlOutputter.output(dto.getDocument(), fileWriter);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

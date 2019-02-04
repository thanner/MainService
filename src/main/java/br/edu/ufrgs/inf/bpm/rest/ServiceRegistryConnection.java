package br.edu.ufrgs.inf.bpm.rest;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class ServiceRegistryConnection {

    public static String getServiceInstanceURL(String serviceInstanceName) throws Exception {
        String serviceInstances = ClientConnection.makeConnection(EurekaServices.GetServiceInstances + serviceInstanceName, "", MediaType.APPLICATION_XML, "GET");

        String serviceInstanceURL = "";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document xmlDocument = builder.parse(new InputSource(new StringReader(serviceInstances)));

            NodeList nodes = xmlDocument.getElementsByTagName("homePageUrl");
            String instanceURL = nodes.item(0).getTextContent();

            nodes = xmlDocument.getElementsByTagName("servletPath");
            String servletPath = nodes.item(0).getTextContent().substring(1);

            serviceInstanceURL = instanceURL + servletPath;
        } catch (Exception e) {
            throw new Exception("Service Instance not found");
        }

        return serviceInstanceURL;
    }

}

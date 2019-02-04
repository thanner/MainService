package br.edu.ufrgs.inf.bpm.rest.textReader;

import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.rest.ClientConnection;
import br.edu.ufrgs.inf.bpm.rest.EurekaServices;
import br.edu.ufrgs.inf.bpm.rest.ServiceRegistryConnection;
import br.edu.ufrgs.inf.bpm.wrapper.JsonWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TextReaderClient {

    /*
    public boolean hasConnected() {
        return ClientConnection.makeConnectionTest(TextReaderURLs.HasConnectedURL);
    }

    public String getProcess(String text) {
        return ClientConnection.makeConnectionPlainText(TextReaderURLs.GetProcessURL, text);
    }

    public TText getMetaText(String text) {
        String json = ClientConnection.makeConnectionJson(TextReaderURLs.GetMetaTextURL, text);
        return JsonWrapper.getObject(json, TText.class);
    }
    */

    public String getProcess(String text) throws Exception {
        String serviceInstanceurl = ServiceRegistryConnection.getServiceInstanceURL(EurekaServices.TextReaderServiceInstances);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("text", text));
        return ClientConnection.makeConnectionForm(serviceInstanceurl + TextReaderURLs.GetProcess, params, "POST");
    }

    public TTextMetadata getMetaText(String text) throws Exception {
        String serviceInstanceUrl = ServiceRegistryConnection.getServiceInstanceURL(EurekaServices.TextReaderServiceInstances);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("text", text));
        String json = ClientConnection.makeConnectionForm(serviceInstanceUrl + TextReaderURLs.GetMetaText, params, "POST");
        return JsonWrapper.getObject(json, TTextMetadata.class);
    }

    /*
    public boolean hasConnected() {
        return RestApplication.getConsumerController().makeRequestTestConnection(TextReaderURLs.ServiceName, TextReaderURLs.HasConnected);
    }

    public String getProcess(String text) {
        return RestApplication.getConsumerController().makeRequest(TextReaderURLs.ServiceName, TextReaderURLs.GetProcess, text, HttpMethod.POST, MediaType.APPLICATION_JSON);
    }

    public Representation getMetaText(String text) {
        String json = RestApplication.getConsumerController().makeRequest(TextReaderURLs.ServiceName, TextReaderURLs.GetMetaText, text, HttpMethod.POST, MediaType.APPLICATION_JSON);
        return JsonWrapper.getObject(json, Representation.class);
    }
    */

    /*
    public boolean hasConnected() {
        return false;
    }

    public String getProcess(String text) {
        return "";
    }

    public Representation getMetaText(String text) {
        return new Representation();
    }
    */

}

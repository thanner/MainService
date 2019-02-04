package br.edu.ufrgs.inf.bpm.rest.textWriter;

import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.rest.ClientConnection;
import br.edu.ufrgs.inf.bpm.rest.EurekaServices;
import br.edu.ufrgs.inf.bpm.rest.ServiceRegistryConnection;
import br.edu.ufrgs.inf.bpm.wrapper.JsonWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TextWriterClient {

    public TTextMetadata getText(String text) throws Exception {
        String serviceInstanceUrl = ServiceRegistryConnection.getServiceInstanceURL(EurekaServices.TextWriterServiceInstances);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("bpmnString", text));
        String json = ClientConnection.makeConnectionForm(serviceInstanceUrl + TextWriterURLs.GetText, params, "POST");
        return JsonWrapper.getObject(json, TTextMetadata.class);
    }

    /*
    public boolean hasConnected() {
        return RestApplication.getConsumerController().makeRequestTestConnection(TextWriterURLs.ServiceName, TextWriterURLs.HasConnected);
    }

    public Representation getText(String bpmnProcess) {
        String json = RestApplication.getConsumerController().makeRequest(TextWriterURLs.ServiceName, TextWriterURLs.GetText, bpmnProcess, HttpMethod.POST, MediaType.APPLICATION_JSON);
        return JsonWrapper.getObject(json, Representation.class);
    }
    */

    /*
    public boolean hasConnected() {
        return false;
    }

    public Representation getText(String bpmnProcess) {
        return new Representation();
    }
    */

}
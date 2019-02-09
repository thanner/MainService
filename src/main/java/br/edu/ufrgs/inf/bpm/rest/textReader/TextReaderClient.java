package br.edu.ufrgs.inf.bpm.rest.textReader;

import br.edu.ufrgs.inf.bpm.rest.ClientConnection;
import br.edu.ufrgs.inf.bpm.rest.EurekaServices;
import br.edu.ufrgs.inf.bpm.rest.ServiceRegistryConnection;
import br.edu.ufrgs.inf.bpm.textmetadata.TTextMetadata;
import br.edu.ufrgs.inf.bpm.wrapper.JsonWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TextReaderClient {

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

}

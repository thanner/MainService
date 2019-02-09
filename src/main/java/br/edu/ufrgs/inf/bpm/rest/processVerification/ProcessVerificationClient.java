package br.edu.ufrgs.inf.bpm.rest.processVerification;

import br.edu.ufrgs.inf.bpm.rest.ClientConnection;
import br.edu.ufrgs.inf.bpm.rest.EurekaServices;
import br.edu.ufrgs.inf.bpm.rest.ServiceRegistryConnection;
import br.edu.ufrgs.inf.bpm.verificationmessages.TBpmnVerification;
import br.edu.ufrgs.inf.bpm.wrapper.JsonWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ProcessVerificationClient {

    public TBpmnVerification getVerification(String text) throws Exception {
        String serviceInstanceUrl = ServiceRegistryConnection.getServiceInstanceURL(EurekaServices.BpmnVerificationServiceInstances);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("bpmnString", text));
        String json = ClientConnection.makeConnectionForm(serviceInstanceUrl + ProcessVerificationUrls.GetVerification, params, "POST");
        return JsonWrapper.getObject(json, TBpmnVerification.class);
    }

}

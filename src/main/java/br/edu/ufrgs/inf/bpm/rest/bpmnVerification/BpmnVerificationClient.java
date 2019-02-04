package br.edu.ufrgs.inf.bpm.rest.bpmnVerification;

import br.edu.ufrgs.inf.bpm.rest.ClientConnection;
import br.edu.ufrgs.inf.bpm.rest.EurekaServices;
import br.edu.ufrgs.inf.bpm.rest.ServiceRegistryConnection;
import br.edu.ufrgs.inf.bpm.verificationmessages.TBpmnVerification;
import br.edu.ufrgs.inf.bpm.wrapper.JsonWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BpmnVerificationClient {

    public TBpmnVerification getVerification(String text) throws Exception {
        String serviceInstanceUrl = ServiceRegistryConnection.getServiceInstanceURL(EurekaServices.BpmnVerificationServiceInstances);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("bpmnString", text));
        String json = ClientConnection.makeConnectionForm(serviceInstanceUrl + BpmnVerificationUrls.GetVerification, params, "POST");
        return JsonWrapper.getObject(json, TBpmnVerification.class);
    }

    /*
    public TBpmnVerification getVerification(String text) {
        String json = ClientConnection.makeConnectionXml(BpmnVerificationUrls.GetVerificationURL, text);
        return JsonWrapper.getObject(json, TBpmnVerification.class);
    }
    */

    /*
    public boolean hasConnected() {
        return RestApplication.getConsumerController().makeRequestTestConnection(BpmnVerificationUrls.ServiceName, BpmnVerificationUrls.HasConnected);
    }

    public List<VerificationElement> getVerification(String bpmnProcess) {
        String json = RestApplication.getConsumerController().makeRequest(BpmnVerificationUrls.ServiceName, BpmnVerificationUrls.GetVerification, bpmnProcess, HttpMethod.POST, MediaType.APPLICATION_JSON);
        return JsonWrapper.getObjectList(json, VerificationElement.class);
    }
    */

    /*
    public boolean hasConnected() {
        return false;
    }

    public List<VerificationElement> getVerification(String bpmnProcess) {
        return null;
    }
    */

}

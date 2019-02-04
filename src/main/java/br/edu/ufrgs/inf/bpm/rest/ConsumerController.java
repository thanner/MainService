/*
package br.edu.ufrgs.inf.bpm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ConsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;

    public boolean makeRequestTestConnection(String serviceName, String serviceMethodName) {
        String response = makeRequest(serviceName, serviceMethodName, "", HttpMethod.GET, MediaType.APPLICATION_JSON);
        return response.equalsIgnoreCase("true");
    }

    public String makeRequest(String serviceInstanceName, String serviceMethodName, String requestBody, HttpMethod httpMethod, MediaType responseMediaType) {
        try {
            ServiceInstance serviceInstance = getServiceInstance(serviceInstanceName);

            String baseUrl = serviceInstance.getUri().toString();
            baseUrl = baseUrl + "/" + serviceMethodName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(responseMediaType);
            HttpEntity<?> httpEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(baseUrl, httpMethod, httpEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private ServiceInstance getServiceInstance(String serviceInstanceName){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceInstanceName);
        return instances.get(0);
    }

}
*/
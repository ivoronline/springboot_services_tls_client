package com.ivoronline.springboot_services_tls_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping(value = "/nt-gw")
public class MyController {

  @Autowired Environment environment;
  @Autowired RestTemplate restTemplate;

  @RequestMapping(value = "/ms-data", method = RequestMethod.GET)
  public String getMsData() {
    System.out.println("getMsData()");
    try {
      String msEndpoint = environment.getProperty("endpoint.ms-service");
      System.out.println(msEndpoint);
      return restTemplate.getForObject(new URI(msEndpoint), String.class);
    }
    catch(Exception e) {
      e.printStackTrace();;
    }
    return "Hello from Controller";
  }

}

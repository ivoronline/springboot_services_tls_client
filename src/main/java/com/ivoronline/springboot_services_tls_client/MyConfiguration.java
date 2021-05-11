package com.ivoronline.springboot_services_tls_client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class MyConfiguration {

  @Bean
  public RestTemplate restTemplate() {

    RestTemplate restTemplate = new RestTemplate();

    try {

      //LOAD KEY STORE
      ClassPathResource classPathResource = new ClassPathResource("nt-gateway.jks");
      InputStream       inputStream       = classPathResource.getInputStream();
      KeyStore          keyStore = KeyStore.getInstance("JKS");
                        keyStore.load(inputStream, "mypassword".toCharArray());

      //CREATE SSL CONTEXT
      SSLContext sslContext = new SSLContextBuilder()
        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
        .loadKeyMaterial(keyStore, "mypassword".toCharArray())
        .build();

      //CREATE SOCKET FACTORY
      SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
        sslContext,
        NoopHostnameVerifier.INSTANCE
      );

      //CREATE HTTP CLIENT
      CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLSocketFactory(socketFactory)
        .setMaxConnTotal(Integer.valueOf(5))
        .setMaxConnPerRoute(Integer.valueOf(5))
        .build();

      //CREATE REQUEST FACTORY
      HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
                                             requestFactory.setReadTimeout(Integer.valueOf(10000));
                                             requestFactory.setConnectTimeout(Integer.valueOf(10000));

      //CREATE REST TEMPLATE
      restTemplate.setRequestFactory(requestFactory);

    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
    }

    return restTemplate;

  }

}

package org.smooth.systems.ec.prestashop17.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductsCreationResponseFilterInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);

        if(request.getMethod() == HttpMethod.POST && response.getStatusCode() == HttpStatus.CREATED && request.getURI().toString().endsWith("/api/products")) {
          String filteredBody = extractFilteredResponseBody(response);
          traceResponse(response, filteredBody);
          return new HttpClientCustomeResponseWrapper(response, filteredBody);
        }
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        log.debug("===========================request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Headers     : {}", request.getHeaders() );
        log.debug("Request body: {}", new String(body, "UTF-8"));
        log.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response, String filteredBody) throws IOException {
        log.debug("============================response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        log.debug("Response body: {}", filteredBody);
        log.debug("=======================response end=================================================");
    }

    private String extractFilteredResponseBody(ClientHttpResponse response) throws UnsupportedEncodingException, IOException {
      StringBuilder inputStringBuilder = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
      String line = bufferedReader.readLine();
      while (line != null) {
          inputStringBuilder.append(line);
          inputStringBuilder.append('\n');
          line = bufferedReader.readLine();
      }
      String filteredResponse = filterXmlResult(inputStringBuilder.toString());
      return filteredResponse;
    }

    private String filterXmlResult(String mixedString) {
      int index = mixedString.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      if(index < 0) {
        index = 0;
      }
      return mixedString.substring(index);
    }
}
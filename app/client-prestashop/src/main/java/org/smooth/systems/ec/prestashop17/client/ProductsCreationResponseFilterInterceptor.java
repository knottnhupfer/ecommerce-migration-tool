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
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
          throws IOException {
    traceRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);

    if (request.getMethod() == HttpMethod.POST && response.getStatusCode() == HttpStatus.CREATED
            && request.getURI().toString().endsWith("/api/products")) {
      String filteredBody = extractFilteredResponseBody(response);
      traceResponse(response, filteredBody);
      return new HttpClientCustomeResponseWrapper(response, filteredBody);
    } else if (request.getMethod() == HttpMethod.GET && response.getStatusCode() == HttpStatus.OK
            && request.getURI().toString().contains("/api/tags/")) {
      String filteredBody = filteredTagResponseBody(response);
      traceResponse(response, filteredBody);
      return new HttpClientCustomeResponseWrapper(response, filteredBody);
    } else if (((request.getMethod() == HttpMethod.POST && response.getStatusCode() == HttpStatus.CREATED)
            || (request.getMethod() == HttpMethod.GET && response.getStatusCode() == HttpStatus.OK))
            && request.getURI().toString().contains("/api/specific_prices")) {
      String filteredBody = filteredTagResponseBody(response);
      filteredBody = removeSimpleElementInResponse(filteredBody, "id_shop_group");
      filteredBody = removeAttributesFromSimpleTagInResponse(filteredBody, "id_shop");
      filteredBody = removeAttributesFromSimpleTagInResponse(filteredBody, "id_product");
      traceResponse(response, filteredBody);
      return new HttpClientCustomeResponseWrapper(response, filteredBody);
    }
    return response;
  }

  private void traceRequest(HttpRequest request, byte[] body) throws IOException {
    log.debug("===========================request begin================================================");
    log.debug("URI         : {}", request.getURI());
    log.debug("Method      : {}", request.getMethod());
    log.debug("Headers     : {}", request.getHeaders());
    if (request.getURI().toString().startsWith("http://prestashop.local/api/images/products/")) {
      log.debug("Request body: <image>");
    } else {
      log.debug("Request body: {}", new String(body, "UTF-8"));
    }
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

  private String extractFilteredResponseBody(ClientHttpResponse response)
          throws UnsupportedEncodingException, IOException {
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

  private String filteredTagResponseBody(ClientHttpResponse response)
          throws UnsupportedEncodingException, IOException {
    StringBuilder inputStringBuilder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
    String line = bufferedReader.readLine();
    while (line != null) {
      inputStringBuilder.append(line);
      inputStringBuilder.append('\n');
      line = bufferedReader.readLine();
    }
    String filteredResponse = removeIdLangHrefFromResponse(inputStringBuilder.toString());
    return filteredResponse;
  }

  private String filterXmlResult(String mixedString) {
    int index = mixedString.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    if (index < 0) {
      index = 0;
    }
    return mixedString.substring(index);
  }

  private String removeIdLangHrefFromResponse(String mixedString) {
    String START_ID_TAG = "<id_lang>";
    String END_ID_TAG = "</id_lang>";
    if (!mixedString.contains(END_ID_TAG)) {
      return mixedString;
    }
    int startIndex = mixedString.indexOf("<id_lang");
    int endIndex = mixedString.indexOf(END_ID_TAG);
    String substring = mixedString.substring(startIndex, endIndex);
    String idLangValue = substring.substring(substring.indexOf(">") + 1);
    return mixedString.replace(substring, START_ID_TAG + idLangValue);
  }

  private String removeAttributesFromSimpleTagInResponse(String mixedString, String tagName) {
    String START_ID_TAG = "<" + tagName + ">";
    String END_ID_TAG = "</" + tagName + ">";
    if (!mixedString.contains(END_ID_TAG)) {
      return mixedString;
    }
    int startIndex = mixedString.indexOf("<" + tagName);
    int endIndex = mixedString.indexOf(END_ID_TAG);
    String substring = mixedString.substring(startIndex, endIndex);
    String idLangValue = substring.substring(substring.indexOf(">") + 1);
    return mixedString.replace(substring, START_ID_TAG + idLangValue);
  }

  private String removeSimpleElementInResponse(String mixedString, String tagName) {
    String START_ID_TAG = "<" + tagName + ">";
    String END_ID_TAG = "</" + tagName + ">";
    if (!mixedString.contains(END_ID_TAG)) {
      return mixedString;
    }
    int startIndex = mixedString.indexOf(START_ID_TAG);
    int endIndex = mixedString.indexOf(END_ID_TAG) + END_ID_TAG.length();
    String substring = mixedString.substring(startIndex, endIndex);
    return mixedString.replace(substring, "");
  }
}
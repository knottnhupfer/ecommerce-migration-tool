package org.smooth.systems.ec.prestashop17.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

public class HttpClientCustomeResponseWrapper implements ClientHttpResponse {

  private final ClientHttpResponse response;

  private final String filteredResponse;

  public HttpClientCustomeResponseWrapper(ClientHttpResponse response, String filteredResponse) {
    this.response = response;
    this.filteredResponse = filteredResponse;
  }

  @Override
  public InputStream getBody() throws IOException {
    return new ByteArrayInputStream(filteredResponse.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public HttpHeaders getHeaders() {
    return response.getHeaders();
  }

  @Override
  public HttpStatus getStatusCode() throws IOException {
    return response.getStatusCode();
  }

  @Override
  public int getRawStatusCode() throws IOException {
    return response.getRawStatusCode();
  }

  @Override
  public String getStatusText() throws IOException {
    return response.getStatusText();
  }

  @Override
  public void close() {
    response.close();
  }
}
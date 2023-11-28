package com.tpe.microservicioparada;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

public class ParadaTest {

    public final HttpClient client = HttpClientBuilder.create().build();
    public final String BASE_URL = "http://localhost:8004";

    public ParadaTest(){}
    @Test
    @DisplayName(" buscar Paradas ")
    public void testGetParadas() throws IOException {
        String url = BASE_URL + "/p/paradas/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWl0YW1hQHlhaG9vLmNvbSIsImF1dGgiOiJBRE1JTixVU0VSIiwiZXhwIjoxNzAwMjg4MjgwfQ.xduOpTirzKprMO3AKbWkDAZtpThfTZ-7_a-7xF5WO3n24Hs7xtU6PdZHM1SYE4iSobW536vO-qCQAskivOudbQ");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==200);
    }

    @Test
    @DisplayName("buscar los paradas sin autorizacion")
    public void testGetParadasSinAuth() throws  IOException {
        String url = BASE_URL + "/p/paradas/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()!=200);
    }
}

package com.tpe.microservicioadministracion;

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

public class AdministrcionTest {
    public final HttpClient client = HttpClientBuilder.create().build();
    public final String BASE_URL = "http://localhost:8001";
    public AdministrcionTest(){}
    @Test
    @DisplayName(" buscar usuarios ")
    public void testGetUsuarios() throws IOException {
        String url = BASE_URL + "/uc/usuarios/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWl0YW1hQHlhaG9vLmNvbSIsImF1dGgiOiJBRE1JTixVU0VSIiwiZXhwIjoxNzAwMjg4MjgwfQ.xduOpTirzKprMO3AKbWkDAZtpThfTZ-7_a-7xF5WO3n24Hs7xtU6PdZHM1SYE4iSobW536vO-qCQAskivOudbQ");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==200);
    }

    @Test
    @DisplayName("buscar las cuentas")
    public void testGetCuentas() throws  IOException {
        String url = BASE_URL + "/uc/cuentas/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWl0YW1hQHlhaG9vLmNvbSIsImF1dGgiOiJBRE1JTixVU0VSIiwiZXhwIjoxNzAwMjg4MjgwfQ.xduOpTirzKprMO3AKbWkDAZtpThfTZ-7_a-7xF5WO3n24Hs7xtU6PdZHM1SYE4iSobW536vO-qCQAskivOudbQ");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==200);
    }
    @Test
    @DisplayName("buscar los usuarios sin autorizacion")
    public void testGetUsuariosSinAuth() throws  IOException {
        String url = BASE_URL + "/uc/usuarios/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==403);
    }

    @Test
    @DisplayName("buscar las cuentas sin autorizacion")
    public void testGetCuentasSinAuth() throws  IOException {
        String url = BASE_URL + "/uc/cuentas/";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==403);
    }

}

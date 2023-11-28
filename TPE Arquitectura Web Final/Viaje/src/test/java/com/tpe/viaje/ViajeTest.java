package com.tpe.viaje;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

public class ViajeTest {
    public final HttpClient client = HttpClientBuilder.create().build();
    public final String BASE_URL = "http://localhost:8080";
    public ViajeTest() {}
    @Test
    @DisplayName(" buscar los viajes ")
    public void testGetViajes() throws  IOException {
        String url = BASE_URL + "/viajes/";
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String contetn = EntityUtils.toString(entity);
        Assertions.assertFalse(contetn==null);

    }

    @Test
    @DisplayName("Intenta buscar un viajes que no existe")
    public void testGetViajesPorId() throws  IOException {
        String url = BASE_URL + "/viajes//obtener/id/900";
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String contetn = EntityUtils.toString(entity);
        Assertions.assertEquals(contetn,"No se encontró el viaje con id: 900","el viaje si fue encontrado ");

    }
}

package microservice.MonoMante;

import microservice.Service.MantenimientoService;
import microservice.Service.MonopatinService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class MonopatinMantenimentoTest {

    public final HttpClient client = HttpClientBuilder.create().build();
    public final String BASE_URL = "http://localhost:8005";
    public MonopatinMantenimentoTest(){}
    @Test
    @DisplayName(" buscar monopatines ")
    public void testGetViajes() throws IOException {
        String url = BASE_URL + "/mm/monopatines";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWl0YW1hQHlhaG9vLmNvbSIsImF1dGgiOiJBRE1JTixVU0VSIiwiZXhwIjoxNzAwMjg4MjgwfQ.xduOpTirzKprMO3AKbWkDAZtpThfTZ-7_a-7xF5WO3n24Hs7xtU6PdZHM1SYE4iSobW536vO-qCQAskivOudbQ");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==200);
    }

    @Test
    @DisplayName("buscar los mantenimientos")
    public void testGetViajesPorId() throws  IOException {
        String url = BASE_URL + "/mm/mantenimientos";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWl0YW1hQHlhaG9vLmNvbSIsImF1dGgiOiJBRE1JTixVU0VSIiwiZXhwIjoxNzAwMjg4MjgwfQ.xduOpTirzKprMO3AKbWkDAZtpThfTZ-7_a-7xF5WO3n24Hs7xtU6PdZHM1SYE4iSobW536vO-qCQAskivOudbQ");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()==200);
    }
    @Test
    @DisplayName("buscar los mantenimientos sin autorizacion")
    public void testGetMantenimietoSinAuth() throws  IOException {
        String url = BASE_URL + "/mm/mantenimientos";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Assertions.assertTrue(response.getStatusLine().getStatusCode()!=200);
    }

    @Test
    @DisplayName("buscar los mantenimientos sin autorizacion")
    public void testGetMonopatinesSinAuth() throws  IOException {
        String url = BASE_URL + "/mm/monopatines";
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        Assertions.assertTrue(response.getStatusLine().getStatusCode()!=200);
    }
    @Test
    @DisplayName("buscar los mantenimientos sin autorizacion")
    public void testGetMonopatinesPorAnio() throws  IOException {
        String url = BASE_URL + "/cantViajes/"+2+"/anio/"+2023;
        HttpGet request = new HttpGet(url);
        Header header = new BasicHeader("Authorization","");
        request.addHeader(header);
        HttpResponse response = client.execute(request);
        Assertions.assertFalse(response.getStatusLine().getStatusCode()==200);
    }
}

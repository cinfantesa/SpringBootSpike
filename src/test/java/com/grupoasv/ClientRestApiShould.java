package com.grupoasv;

import com.grupoasv.domain.Client;
import com.grupoasv.repository.ClientRepository;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientRestApiShould {

    public static final int SIP = 1;
    public static final String CLIENT_NAME = "alfredo";
    public static final String CLIENT_PHONE = "12345";

    @Autowired ClientRepository clientRepository;
    @Value("${local.server.port}") int port;

    @Before
    public void setUp() {
        clientRepository.deleteAll();
        RestAssured.port = port;
    }

    @Test
    public void get_a_client() {
        clientRepository.save(new Client(SIP, CLIENT_NAME, CLIENT_PHONE));

        when()
          .get("/clients/{id}", SIP).
        then()
          .statusCode(HttpStatus.SC_OK)
          .body("name", equalTo(CLIENT_NAME))
          .body("phone", equalTo(CLIENT_PHONE));
    }
}

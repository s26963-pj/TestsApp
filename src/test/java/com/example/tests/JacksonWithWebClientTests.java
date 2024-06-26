package com.example.tests;

import com.example.tests.model.Exchange;
import com.example.tests.model.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JacksonWithWebClientTests {
    @Autowired
    private JacksonTester<Exchange> json;
    private WebClient webClient = WebClient.builder().build();
    private List<Rate> list = new LinkedList<>();
    private static final String uri = "https://api.nbp.pl/api/exchangerates/rates/a/eur?format=json";
    private static final String filePath = "src/test/resources/com/example/tests/expected.json";

    @BeforeEach
    void downloadJsonFile() throws IOException{
        Exchange exchange = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        JsonContent<Exchange> jsonContent = json.write(exchange);
        File file = new File(filePath);
        file.createNewFile();
        if (file.exists()) {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonContent.getJson());
            fileWriter.close();
        }

    }


    @Test
    void shouldSerializeObjectToJson() throws IOException {
        Exchange exchange = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        JsonContent<Exchange> jsonContent = json.write(exchange);

        assertThat(jsonContent).hasJsonPathStringValue("@.table");
        assertThat(jsonContent).extractingJsonPathStringValue("@.table").isEqualTo(exchange.getTable());

        assertThat(jsonContent).hasJsonPathStringValue("@.currency");
        assertThat(jsonContent).extractingJsonPathStringValue("@.currency").isEqualTo(exchange.getCurrency());

        assertThat(jsonContent).hasJsonPathStringValue("@.code");
        assertThat(jsonContent).extractingJsonPathStringValue("@.code").isEqualTo(exchange.getCode());

        assertThat(jsonContent).hasJsonPathValue("@.rates");

        assertThat(jsonContent).hasJsonPathStringValue("@.rates[0].no");
        assertThat(jsonContent).extractingJsonPathStringValue("@.rates[0].no").isEqualTo(exchange.getRates().getFirst().getNo());

        assertThat(jsonContent).hasJsonPathStringValue("@.rates[0].effectiveDate");
        assertThat(jsonContent).extractingJsonPathStringValue("@.rates[0].effectiveDate").isEqualTo(exchange.getRates().getFirst().getEffectiveDate());

        assertThat(jsonContent).hasJsonPathNumberValue("@.rates[0].mid");
        assertThat(jsonContent).extractingJsonPathNumberValue("@.rates[0].mid").isEqualTo(exchange.getRates().getFirst().getMid());
    }

    @Test
    void shouldDeserializeJsonToExchange() throws IOException{
        Exchange exchange = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        ObjectContent<Exchange> objectContent = json.read("expected.json");
        Exchange exchangeContent = objectContent.getObject();

        assertThat(exchangeContent.getTable()).isEqualTo(exchange.getTable());
        assertThat(exchangeContent.getCurrency()).isEqualTo(exchange.getCurrency());
        assertThat(exchangeContent.getCode()).isEqualTo(exchange.getCode());
        assertThat(exchangeContent.getRates().getFirst().getNo()).isEqualTo(exchange.getRates().getFirst().getNo());
        assertThat(exchangeContent.getRates().getFirst().getEffectiveDate()).isEqualTo(exchange.getRates().getFirst().getEffectiveDate());
        assertThat(exchangeContent.getRates().getFirst().getMid()).isEqualTo(exchange.getRates().getFirst().getMid());
    }
}

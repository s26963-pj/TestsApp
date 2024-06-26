package com.example.tests;

import com.example.tests.model.Exchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExchangeServiceTests {
    @Autowired
    private ExchangeService exchangeService;
    private final WebClient webClient = WebClient.builder().build();
    private static final String uri = "https://api.nbp.pl/api/exchangerates/rates/a/eur?format=json";

    @Test
    void shouldGetEuroExchange() {
        Exchange euroExchange = exchangeService.getEuroExchange();
        Exchange exchangeFromWebClient = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        assertThat(euroExchange.getTable()).isEqualTo(exchangeFromWebClient.getTable());
        assertThat(euroExchange.getCurrency()).isEqualTo(exchangeFromWebClient.getCurrency());
        assertThat(euroExchange.getCode()).isEqualTo(exchangeFromWebClient.getCode());
        assertThat(euroExchange.getRates().getFirst().getMid()).isEqualTo(exchangeFromWebClient.getRates().getFirst().getMid());
        assertThat(euroExchange.getRates().getFirst().getEffectiveDate()).isEqualTo(exchangeFromWebClient.getRates().getFirst().getEffectiveDate());
        assertThat(euroExchange.getRates().getFirst().getNo()).isEqualTo(exchangeFromWebClient.getRates().getFirst().getNo());
    }

}

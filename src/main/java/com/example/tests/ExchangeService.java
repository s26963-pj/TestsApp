package com.example.tests;

import com.example.tests.model.Exchange;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExchangeService {
    private final WebClient webClient;
    private final ExchangeRepository exchangeRepository;
    private static final String urlEur = "https://api.nbp.pl/api/exchangerates/rates/a/eur?format=json";
    private static final String urlGbp = "https://api.nbp.pl/api/exchangerates/rates/a/gbp?format=json";
    private static final String urlUsd = "https://api.nbp.pl/api/exchangerates/rates/a/usd?format=json";

    public ExchangeService(WebClient webClient, ExchangeRepository exchangeRepository) {
        this.webClient = webClient;
        this.exchangeRepository = exchangeRepository;
    }

    public Exchange getEuroExchange() {
        Exchange exchange = webClient.get()
                .uri(urlEur)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        return exchange;
    }

    public Exchange getGbpExchange() {
        Exchange exchange = webClient.get()
                .uri(urlGbp)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        return exchange;
    }

    public Exchange getUsdExchange() {
        Exchange exchange = webClient.get()
                .uri(urlUsd)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        return exchange;
    }
}

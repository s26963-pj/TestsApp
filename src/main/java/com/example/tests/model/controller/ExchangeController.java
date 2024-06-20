package com.example.tests.model.controller;

import com.example.tests.model.Exchange;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ExchangeController {

    private WebClient webClient = WebClient.create();
    private WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.method(HttpMethod.GET);

    @GetMapping("/test")
    public ResponseEntity<Exchange> getExchange() {
        Exchange exchange = webClient.get()
                .uri("https://api.nbp.pl/api/exchangerates/rates/a/eur?format=json")
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
        return ResponseEntity.ok(exchange);
    }
}

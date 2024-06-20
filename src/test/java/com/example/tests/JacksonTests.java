package com.example.tests;

import com.example.tests.model.Exchange;
import com.example.tests.model.Rate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JacksonTests {
    @Autowired
    private JacksonTester<Exchange> json;
    private final List<Rate> list = new LinkedList<>();


    @Test
    public void shouldSerializeExchangeToJson() throws IOException {
        list.addFirst(new Rate("119/A/NBP/2024", "2024-06-20", 4.3238));
        Exchange exchange = new Exchange("A", "euro", "EUR", list);
        JsonContent<Exchange> writeContent = json.write(exchange);

        assertThat(writeContent).isStrictlyEqualToJson("expected.json");

        assertThat(writeContent).hasJsonPathStringValue("@.table");
        assertThat(writeContent).extractingJsonPathStringValue("@.table").isEqualTo(exchange.getTable());

        assertThat(writeContent).hasJsonPathStringValue("@.currency");
        assertThat(writeContent).extractingJsonPathStringValue("@.currency").isEqualTo(exchange.getCurrency());

        assertThat(writeContent).hasJsonPathStringValue("@.code");
        assertThat(writeContent).extractingJsonPathStringValue("@.code").isEqualTo(exchange.getCode());

        assertThat(writeContent).hasJsonPathValue("@.rates");

        assertThat(writeContent).hasJsonPathStringValue("@.rates[0].no");
        assertThat(writeContent).extractingJsonPathStringValue("@.rates[0].no").isEqualTo(exchange.getRates().getFirst().getNo());

        assertThat(writeContent).hasJsonPathStringValue("@.rates[0].effectiveDate");
        assertThat(writeContent).extractingJsonPathStringValue("@.rates[0].effectiveDate").isEqualTo(exchange.getRates().getFirst().getEffectiveDate());

        assertThat(writeContent).hasJsonPathNumberValue("@.rates[0].mid");
        assertThat(writeContent).extractingJsonPathNumberValue("@.rates[0].mid").isEqualTo(exchange.getRates().getFirst().getMid());

    }

    @Test
    void shouldDeserializeFromJson() throws IOException{
        list.addFirst(new Rate("119/A/NBP/2024", "2024-06-20", 4.3238));
        Exchange exchange = new Exchange("A", "euro", "EUR", list);

        ObjectContent<Exchange> objectContent = json.read("expected.json");
        Exchange exchangeContent = objectContent.getObject();

        assertThat(exchange.getTable()).isEqualTo(exchangeContent.getTable());
        assertThat(exchange.getCurrency()).isEqualTo(exchangeContent.getCurrency());
        assertThat(exchange.getCode()).isEqualTo(exchangeContent.getCode());
        assertThat(exchange.getRates().getFirst().getNo()).isEqualTo(exchangeContent.getRates().getFirst().getNo());
        assertThat(exchange.getRates().getFirst().getEffectiveDate()).isEqualTo(exchangeContent.getRates().getFirst().getEffectiveDate());
        assertThat(exchange.getRates().getFirst().getMid()).isEqualTo(exchangeContent.getRates().getFirst().getMid());
    }
}

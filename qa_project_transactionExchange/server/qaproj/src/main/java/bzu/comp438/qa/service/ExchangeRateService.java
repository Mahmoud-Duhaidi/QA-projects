package bzu.comp438.qa.service;


import bzu.comp438.qa.model.ConvertedRate;
import bzu.comp438.qa.model.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class ExchangeRateService {


    private final WebClient webClient;

    public ExchangeRateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://v6.exchangerate-api.com/v6/417cbd09b81467346b0506a5/latest").build();
    }

    public Mono<ConvertedRate> fetchConversionRate(String fromCurrency, String toCurrency) {
        return webClient.get()
                .uri("/{fromCurrency}", fromCurrency)
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .map(response -> new ConvertedRate(response.getConversionRates().get(toCurrency)) );
    }

}

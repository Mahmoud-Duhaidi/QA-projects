package bzu.comp438.qa.service;


import bzu.comp438.qa.model.ConversionRequest;
import bzu.comp438.qa.model.ConversionResult;
import bzu.comp438.qa.model.ConvertedRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ConversionService {

    @Autowired
    private ExchangeRateService exchangeRateService;

    public ConversionResult convert(ConversionRequest request) {
        // Fetch the rate and block until the value is available
        Double rate = exchangeRateService.fetchConversionRate(request.getFromCurrency(), request.getToCurrency())
                .map(ConvertedRate::getConvertedRate) // Assuming ConvertedRate has a getRate method returning Double
                .block(); // This blocks the thread until the rate is available
        System.out.println(rate); // Now 'rate' is a Double and you can use it directly
        // Calculate the converted amount using the rate
        double convertedAmount = request.getAmount() * rate; // Use the rate here

        return new ConversionResult(convertedAmount);
    }

}

package bzu.comp438.qa.junitMockito;

import bzu.comp438.qa.model.ConversionRequest;
import bzu.comp438.qa.model.ConvertedRate;
import bzu.comp438.qa.model.ConversionResult;
import bzu.comp438.qa.service.ConversionService;
import bzu.comp438.qa.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ConversionService conversionService; // Your actual ConversionService class


    // in-memory database
    private final Map<String, Double> conversionRates = new HashMap<>();

    @BeforeEach
    void setUp() {
        // Initialize your in-memory database with dummy data
        conversionRates.put("USD_EUR", 0.92110);
        conversionRates.put("EUR_USD", 1.08540);
        conversionRates.put("ILS_USD", 0.27030);
        conversionRates.put("USD_ILS", 3.69990);

    }

    @Test
    public void testConvert() {
        // Given
        ConversionRequest request = new ConversionRequest(100.0, "USD", "EUR");
        ConvertedRate convertedRate = new ConvertedRate(conversionRates.get("USD_EUR"));
        when(exchangeRateService.fetchConversionRate(request.getFromCurrency(), request.getToCurrency()))
                .thenReturn(Mono.just(convertedRate)); // Return the rate from the in-memory database

        // When
        ConversionResult result = conversionService.convert(request);

        // Then
        assertEquals(92.11, result.getConvertedAmount()); // Verify the conversion is as expected
    }

    @Test
    public void testConvert2() {
        // Given
        ConversionRequest request = new ConversionRequest(100.0, "USD", "ILS");
        ConvertedRate convertedRate = new ConvertedRate(conversionRates.get("USD_ILS"));
        when(exchangeRateService.fetchConversionRate(request.getFromCurrency(), request.getToCurrency()))
                .thenReturn(Mono.just(convertedRate)); // Return the rate from the in-memory database

        // When
        ConversionResult result = conversionService.convert(request);

        // Then
        assertEquals(369.99, result.getConvertedAmount()); // Verify the conversion is as expected
    }
}

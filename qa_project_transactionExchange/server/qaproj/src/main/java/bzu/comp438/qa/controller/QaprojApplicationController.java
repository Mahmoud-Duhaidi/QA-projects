package bzu.comp438.qa.controller;

import bzu.comp438.qa.model.ConversionRequest;
import bzu.comp438.qa.model.ConversionResult;
import bzu.comp438.qa.model.ConvertedRate;
import bzu.comp438.qa.service.ConversionService;
import bzu.comp438.qa.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/qa")
public class QaprojApplicationController {

    @Autowired
    private ConversionService currencyService;
    @Autowired
    private ExchangeRateService exchangeRateService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionResult> convert(@RequestBody ConversionRequest request) {
        ConversionResult result = currencyService.convert(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getRate")
    public Mono<ResponseEntity<ConvertedRate>> getRate(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        return exchangeRateService.fetchConversionRate(fromCurrency, toCurrency)
                .map(rate -> ResponseEntity.ok(rate));
    }
}

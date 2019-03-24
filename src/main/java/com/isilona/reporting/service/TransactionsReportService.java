package com.isilona.reporting.service;

import com.isilona.reporting.dao.psp.TransactionsReportResponse;
import com.isilona.reporting.dto.TransactionsReportRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@Service
public class TransactionsReportService {

    private final RestTemplate restTemplate;
    private final AuthenticationService authenticationService;

    @Value("${external-api.psp.url.transactions.report}")
    private String resourceUrl;

    public TransactionsReportService(RestTemplateBuilder restTemplateBuilder, AuthenticationService authenticationService) {
        this.restTemplate = restTemplateBuilder.build();
        this.authenticationService = authenticationService;
    }

    public void getTransactionsReport(TransactionsReportRequest jsonRequest, DeferredResult<String> deferredResult, Model model) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, authenticationService.getToken());

        HttpEntity<TransactionsReportRequest> request = new HttpEntity(jsonRequest, headers);

        CompletableFuture.supplyAsync(() -> this.restTemplate.postForObject(resourceUrl, request, TransactionsReportResponse.class))
                .whenCompleteAsync((result, throwable) -> {
                    deferredResult.setResult("transactionsReportResult");
                    model.addAttribute("transactions_report_result", result);
                });
    }
}

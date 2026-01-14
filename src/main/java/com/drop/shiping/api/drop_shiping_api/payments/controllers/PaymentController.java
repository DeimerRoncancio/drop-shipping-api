package com.drop.shiping.api.drop_shiping_api.payments.controllers;

import com.drop.shiping.api.drop_shiping_api.payments.dtos.EpaycoWebhookDTO;
import com.drop.shiping.api.drop_shiping_api.payments.dtos.SessionDataDTO;
import com.drop.shiping.api.drop_shiping_api.payments.services.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/payments")
@CrossOrigin(originPatterns = "*")
public class PaymentController {
    private final PaymentService paymentService;
    private final RestClient restClient;

    @Value("${epayco.api.username}")
    private String username;

    @Value("${epayco.api.password}")
    private String password;

    public PaymentController(RestClient restClient, PaymentService paymentService) {
        this.restClient = restClient;
        this.paymentService = paymentService;
    }

    @GetMapping
    public Map<String, String> getToken() {
        return restClient.post()
                .uri("/login")
                .headers(headers -> headers.setBasicAuth(username, password))
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {});
    }

    @PostMapping("/get-session")
    public String getSession(@RequestBody SessionDataDTO data) {
        Map<String, Object> body = new HashMap<>();

        body.put("checkout_version", "2");
        body.put("name", data.name());
        body.put("description", data.description());
        body.put("currency", "COP");
        body.put("amount",  data.amount());
        body.put("country", "CO");
        body.put("lang", "ES");
        body.put("ip", "201.245.254.45");
        body.put("method", "POST");
        body.put("test", true);

        Map<String, Object> extras = new HashMap<>();
        extras.put("extra1", data.transactionId());
        body.put("extras", extras);

//        body.put("confirmation", "https://charriest-semiclinically-julienne.ngrok-free.dev/app/payments/confirmation");
        body.put("response", "https://charriest-semiclinically-julienne.ngrok-free.dev/payments/invoice");

        return restClient.post()
                .uri("/payment/session/create")
                .headers(headers -> headers.setBearerAuth(getToken().get("token")))
                .body(body)
                .retrieve()
                .body(String.class);
    }

    @PostMapping(
    value = "/confirmation",
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handleWebhook(@ModelAttribute EpaycoWebhookDTO request) {
        System.out.println(
                "Webhook recibido:" +
                "cod=" + request.x_cod_response() +
                "response=" + request.x_response() +
                "tx=" + request.x_extra1()
        );
        try {
            if (!paymentService.validateSignature(request))
                return ResponseEntity.badRequest().body("Firma invalida");

            paymentService.processWebhook(request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error procesando webhook");
        }
    }
}

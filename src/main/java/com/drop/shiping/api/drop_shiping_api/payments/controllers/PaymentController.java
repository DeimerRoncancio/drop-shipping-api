package com.drop.shiping.api.drop_shiping_api.payments.controllers;

import com.drop.shiping.api.drop_shiping_api.payments.services.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/payments")
@CrossOrigin(originPatterns = "*")
public class PaymentController {
    private final RestClient restClient;

    private final PaymentService paymentService;

    @Value("${epayco.url.payments}")
    private String url;

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

    @GetMapping("/get-session")
    public String getSession() {
        Map<String, String> body = new HashMap<>();

        body.put("checkout_version", "2");
        body.put("name", "QA Shops Online S.A.S");
        body.put("description", "Buzo con capucha color negro unisex");
        body.put("currency", "COP");
        body.put("amount", "20000");
        body.put("country", "CO");
        body.put("lang", "ES");
        body.put("ip", "201.245.254.45");
        body.put("test", "true");
        body.put("confirmation", "https://webhook.site/4d6ebe96-4e79-4768-bdee-09b8fde0e6c7");
        body.put("response", "https://hkdk.events/ha64xsf25bn8lv");
        body.put("methodconfirmation", "POST");

        return restClient.post()
                .uri("/payment/session/create")
                .headers(headers -> headers.setBearerAuth(getToken().get("token")))
                .body(body)
                .retrieve()
                .body(String.class);
    }

    @PostMapping("/confirmation")
    public ResponseEntity<String> handleWebhook() {
//        try {
//            System.out.println("Llega aquí");
//            if (!paymentService.validateSignature(request))
//                return ResponseEntity.badRequest().body("Firma invalida");
//
//            paymentService.processWebhook(request);
//
//            return ResponseEntity.ok().build();
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error procesando webhook");
//        }

        System.out.println("Funciona");
        return ResponseEntity.ok().build();
    }
}

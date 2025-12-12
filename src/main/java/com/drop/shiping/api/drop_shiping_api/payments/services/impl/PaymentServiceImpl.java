package com.drop.shiping.api.drop_shiping_api.payments.services.impl;

import com.drop.shiping.api.drop_shiping_api.payments.dtos.EpaycoWebhookDTO;
import com.drop.shiping.api.drop_shiping_api.payments.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${epayco.customer.id}")
    private String epaycoCustomerId;

    @Value("${epayco.key}")
    private String epaycoKey;

    @Override
    public boolean validateSignature(EpaycoWebhookDTO request) {
        try {
            String signatureString = String.format("%s^%s^%s^%s^%s^%s",
                epaycoCustomerId,
                epaycoKey,
                request.xRefPayco(),
                request.xTransactionId(),
                request.xAmount(),
                request.xCurrencyCode()
            );

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signatureString.getBytes(StandardCharsets.UTF_8));

            String calculatedSignature = bytesToHex(hash);

            return calculatedSignature.equals(request.xSignature());

        } catch(NoSuchElementException | NoSuchAlgorithmException err) {
            log.debug("Error calculando firma SHA-256");
            return false;
        }
    }

    @Override
    @Transactional
    public void processWebhook(EpaycoWebhookDTO request) {
        switch (request.xResponse()) {
            case "Aceptada": processApprovedTransaction(request);
                break;
            case "Rechazada": processRejectTransaction(request);
                break;
            case "Pendiente": processPendingTransaction(request);
                break;
            case "Fallida": processFailedTransaction(request);
                break;
        }
    }

    @Override
    public boolean existsByTransactionId() {
        return true;
    }

    private void processApprovedTransaction(EpaycoWebhookDTO request) {
        System.out.println("Información de compra: " + request);
    }

    private void processRejectTransaction(EpaycoWebhookDTO request) {
        System.out.println("Información de compra: " + request);
    }

    private void processPendingTransaction(EpaycoWebhookDTO request) {
        System.out.println("Información de compra: " + request);
    }

    private void processFailedTransaction(EpaycoWebhookDTO request) {
        System.out.println("Información de compra: " + request);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

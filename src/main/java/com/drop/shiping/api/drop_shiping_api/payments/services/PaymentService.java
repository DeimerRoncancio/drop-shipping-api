package com.drop.shiping.api.drop_shiping_api.payments.services;

import com.drop.shiping.api.drop_shiping_api.payments.dtos.EpaycoWebhookDTO;

public interface PaymentService {
    boolean validateSignature(EpaycoWebhookDTO request);

    void processWebhook(EpaycoWebhookDTO request);

    boolean existsByTransactionId();
}

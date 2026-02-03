package com.drop.shiping.api.drop_shiping_api.emails.services;

import com.drop.shiping.api.drop_shiping_api.emails.dtos.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO email);
}

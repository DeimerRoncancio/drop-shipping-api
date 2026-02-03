package com.drop.shiping.api.drop_shiping_api.emails.controllers;

import com.drop.shiping.api.drop_shiping_api.emails.dtos.EmailDTO;
import com.drop.shiping.api.drop_shiping_api.emails.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/send-email")
@CrossOrigin(originPatterns = "*")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDTO email) {
        emailService.sendEmail(email);
        return ResponseEntity.ok().build();
    }
}

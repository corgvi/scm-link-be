package com.cvv.scm_link.controller;

import com.cvv.scm_link.dto.request.ReceivingNoteRequest;
import com.cvv.scm_link.dto.response.ReceivingNoteResponse;
import com.cvv.scm_link.service.BaseService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingNotes")
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ReceivingNoteController extends BaseController<ReceivingNoteRequest, ReceivingNoteRequest, ReceivingNoteResponse, String>{
    public ReceivingNoteController(BaseService<ReceivingNoteRequest, ReceivingNoteRequest, ReceivingNoteResponse, String> baseService) {
        super(baseService);
    }
}

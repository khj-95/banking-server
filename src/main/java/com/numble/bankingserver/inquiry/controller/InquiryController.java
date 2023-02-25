package com.numble.bankingserver.inquiry.controller;

import com.numble.bankingserver.inquiry.service.InquiryService;
import com.numble.bankingserver.inquiry.vo.AccountVO;
import com.numble.bankingserver.inquiry.vo.InquiryInfoVO;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry/account-list")
    public ResponseEntity<List<AccountVO>> inquiryAccountList(Principal principal) {
        String userId = principal.getName();
        List<AccountVO> accountList = inquiryService.inquiryAccountList(userId);
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/inquiry")
    public ResponseEntity<AccountVO> inquiry(@RequestBody InquiryInfoVO info) throws Exception {
        AccountVO account = inquiryService.inquiryAccount(info);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}

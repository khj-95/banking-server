package com.numble.bankingserver.inquiry.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.security.Principal;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryInfoVO {

    private String id;
    private String accountNumber;

    @JsonCreator
    @Builder
    public InquiryInfoVO(Principal principal,
        @JsonProperty("account-number") String accountNumber) {
        this.id = principal.getName();
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InquiryInfoVO) {
            InquiryInfoVO tmp = (InquiryInfoVO) obj;
            return id.equals(tmp.getId()) && accountNumber.equals(tmp.getAccountNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber);
    }
}

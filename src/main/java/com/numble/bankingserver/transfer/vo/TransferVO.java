package com.numble.bankingserver.transfer.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.security.Principal;
import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TransferVO {

    private final long MIN_AMOUNT = 1;
    private final long MAX_AMOUNT = 5_000_000;
    @NotBlank
    private String fromUser;
    @NotBlank
    private String toUser;
    @NotBlank
    private String fromAccountNumber;
    @NotBlank
    private String toAccountNumber;
    @Min(MIN_AMOUNT)
    @Max(MAX_AMOUNT)
    private long amount;

    @JsonCreator
    @Builder
    private TransferVO(Principal principal,
        @JsonProperty("receiver") String toUser,
        @JsonProperty("senderAccountNumber") String fromAccountNumber,
        @JsonProperty("receiverAccountNumber") String toAccountNumber,
        @JsonProperty("amount") long amount) {
        this.fromUser = principal.getName();
        this.toUser = toUser;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

    public static TransferVO createTransferVO(Principal principal, String toUser,
        String fromAccountNumber, String toAccountNumber, long amount) {
        return TransferVO.builder()
            .principal(principal)
            .toUser(toUser)
            .fromAccountNumber(fromAccountNumber)
            .toAccountNumber(toAccountNumber)
            .amount(amount)
            .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TransferVO) {
            TransferVO tmp = (TransferVO) obj;
            return fromUser.equals(tmp.getFromUser())
                && fromAccountNumber.equals(tmp.getFromAccountNumber())
                && toUser.equals(tmp.getToUser())
                && toAccountNumber.equals(tmp.getToAccountNumber())
                && amount == tmp.amount;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, fromAccountNumber, toUser, toAccountNumber, amount);
    }
}

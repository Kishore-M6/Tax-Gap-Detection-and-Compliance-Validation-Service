package com.taxgapdetection.response;

import com.taxgapdetection.helper.ValidationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
public class TransactionResponse {
    private String transactionId;
    private ValidationStatus validationStatus;
    private String failureReasons;
}

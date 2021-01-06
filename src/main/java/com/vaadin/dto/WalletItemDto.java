package com.vaadin.dto;

import com.vaadin.domain.Currency;
import com.vaadin.domain.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletItemDto {

    private Long id;
    private Long walletId;
    private Currency currency;
    private Double quantity;

    public WalletItemDto(Long walletId, Currency currency, Double quantity) {
        this.walletId = walletId;
        this.currency = currency;
        this.quantity = quantity;
    }
}

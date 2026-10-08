package application.infrastructure.adapter.web.dto;

import application.domain.valueobject.Money;

import java.math.BigDecimal;

/** Monetary amount accepted by the API (converted to a domain {@link Money} inside the controller). */
public record MoneyRequest(BigDecimal amount, String currency) {

    public Money toMoney() {
        return Money.of(amount, currency);
    }
}

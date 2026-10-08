package application.infrastructure.adapter.web.dto;

import application.domain.valueobject.Money;

import java.math.BigDecimal;

/** Monetary amount view returned by the API (primitives only — no domain types). */
public record MoneyResponse(BigDecimal amount, String currency) {

    public static MoneyResponse from(Money money) {
        if (money == null) {
            return null;
        }
        return new MoneyResponse(money.getAmount(), money.getCurrency());
    }
}

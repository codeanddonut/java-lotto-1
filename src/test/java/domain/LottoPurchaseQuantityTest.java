package domain;

import domain.lotto.LottoPurchaseQuantity;
import domain.lotto.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoPurchaseQuantityTest {
    @Test
    void validationTest() {
        assertThatThrownBy(() -> new LottoPurchaseQuantity(new Money(10000), 12));
    }
}

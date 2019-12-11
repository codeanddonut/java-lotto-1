package domain;

import domain.lotto.*;
import org.junit.jupiter.api.Test;
import service.LottoWinningNumbersFactory;
import testUtils.TestLottoGenerator;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class LottoResultTest {
    private final Lotto a = TestLottoGenerator.make("1, 2, 3, 4, 5, 6");
    private final Lotto b = TestLottoGenerator.make("10, 34, 38, 30, 31, 33");
    private final Lotto c = TestLottoGenerator.make("10, 34, 38, 40, 42, 32");
    private final LottoResult result = new LottoResult(
            new Lottos(
                    Arrays.asList(this.a, this.b, this.c),
                    new LottoPurchaseQuantity(new Money(3000), 3)
            ),
            LottoWinningNumbersFactory.of(new LottoRound(862)));

    @Test
    void purchasedAmountTest() {
        assertThat(this.result.purchasedAmount().amount()).isEqualTo(3_000);
    }

    @Test
    void totalAmountTest() {
        assertThat(this.result.totalEarned().amount()).isEqualTo(30_005_000);
    }

    @Test
    void earningRateTest() {
        final double ANSWER = ((LottoRank.SECOND.prize().amount() + LottoRank.FIFTH.prize().amount()) / (3.0 * Lotto.PRICE) - 1.0) * 100.0;
        assertThat(this.result.earningRate()).isCloseTo(ANSWER, offset(0.0000001));
    }
}

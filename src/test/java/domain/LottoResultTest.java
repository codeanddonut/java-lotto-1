package domain;

import domain.lotto.*;
import org.junit.jupiter.api.Test;
import service.LottoWinningNumbersFactory;
import test.TestLottoGenerator;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class LottoResultTest {
    final Lotto a = TestLottoGenerator.make("1, 2, 3, 4, 5, 6");
    final Lotto b = TestLottoGenerator.make("10, 34, 38, 30, 31, 33");
    final Lotto c = TestLottoGenerator.make("10, 34, 38, 40, 42, 32");
    final LottoResult result = new LottoResult(
            new Lottos(
                    Arrays.asList(a, b, c),
                    new LottoPurchaseQuantity(new Money(3000), 3)
            ),
            LottoWinningNumbersFactory.of(new LottoRound(862)));

    @Test
    void purchasedAmountTest() {
        assertThat(result.purchasedAmount().amount()).isEqualTo(3_000);
    }

    @Test
    void totalAmountTest() {
        assertThat(result.totalEarned().amount()).isEqualTo(30_005_000);
    }

    @Test
    void earningRateTest() {
        assertThat(
                result.earningRate()
        ).isCloseTo(
                ((LottoRank.SECOND.prize().amount() + LottoRank.FIFTH.prize().amount()) / (3.0 * Lotto.PRICE) - 1.0) * 100.0,
                offset(0.0000001)
        );
    }
}
package domain;

import domain.lotto.LottoRank;
import domain.lotto.LottoRound;
import org.junit.jupiter.api.Test;
import service.LottoWinningNumbersFactory;
import test.TestLottoGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    @Test
    void overflowTest() {
        assertThatThrownBy(() -> TestLottoGenerator.make("1,3,7,10,15,99"));
    }

    @Test
    void underflowTest() {
        assertThatThrownBy(() -> TestLottoGenerator.make("1,3,7,10,15,-1"));
    }

    @Test
    void matchingTest() {
        assertThat(
                TestLottoGenerator.make("10,34 , 38 ,40,,42    ,32").match(LottoWinningNumbersFactory.of(new LottoRound(862))).get()
        ).isEqualTo(LottoRank.SECOND);
    }
}
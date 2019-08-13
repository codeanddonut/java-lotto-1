package service;

import test.TestLottoGenerator;
import domain.lotto.LottoRank;
import domain.lotto.LottoRound;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LottoWinningNumbersFactoryTest {
    @Test
    void factoryTestA() {
        assertThat(
                TestLottoGenerator.make("10, 34, 38, 40, 42, 43").match(LottoWinningNumbersFactory.of(new LottoRound(862))).get()
                ).isEqualTo(LottoRank.FIRST);
    }

    @Test
    void factoryTestB() {
        assertThat(
                TestLottoGenerator.make("11, 17, 19, 21, 22, 25").match(LottoWinningNumbersFactory.of(new LottoRound(861))).get()
        ).isEqualTo(LottoRank.FIRST);
    }

    @Test
    void factoryTestC() {
        assertThat(
                TestLottoGenerator.make("8, 10, 18, 23, 27, 33").match(LottoWinningNumbersFactory.of(new LottoRound(457))).get()
        ).isEqualTo(LottoRank.SECOND);
    }
}
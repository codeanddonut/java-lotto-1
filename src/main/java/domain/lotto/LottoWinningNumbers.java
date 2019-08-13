package domain.lotto;

import java.util.Collections;
import java.util.List;

public class LottoWinningNumbers {
    private final List<LottoNumber> winningNumbers;
    private final LottoRound round;

    public LottoWinningNumbers(List<LottoNumber> winningNumbers, LottoRound round) {
        this.winningNumbers = Collections.unmodifiableList(winningNumbers);
        this.round = round;
    }

    public List<LottoNumber> mains() {
        return this.winningNumbers.subList(0, Lotto.NUMBER_OF_PICKS);
    }

    public LottoNumber bonus() {
        return this.winningNumbers.get(Lotto.NUMBER_OF_PICKS);
    }

    public LottoRound round() {
        return this.round;
    }
}
package domain.lotto;

import java.util.Collections;
import java.util.List;

public class WinningNumbers {
    private final List<LottoNumber> winningNumbers;
    private final Round round;

    public WinningNumbers(List<LottoNumber> winningNumbers, Round round) {
        this.winningNumbers = Collections.unmodifiableList(winningNumbers);
        this.round = round;
    }

    public List<LottoNumber> mains() {
        return this.winningNumbers.subList(0, Lotto.NUMBER_OF_PICKS);
    }

    public LottoNumber bonus() {
        return this.winningNumbers.get(Lotto.NUMBER_OF_PICKS);
    }

    public Round round() {
        return this.round;
    }
}
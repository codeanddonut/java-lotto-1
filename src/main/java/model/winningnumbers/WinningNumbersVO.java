package model.winningnumbers;

import model.lotto.Lotto;
import model.lotto.LottoNumber;

import java.util.Collections;
import java.util.List;

public class WinningNumbersVO implements WinningNumbers {
    private final List<LottoNumber> winningNumbers;
    private final int round;

    public WinningNumbersVO(List<LottoNumber> winningNumbers, int round) {
        this.winningNumbers = Collections.unmodifiableList(winningNumbers);
        this.round = round;
    }

    @Override
    public List<LottoNumber> mains() {
        return this.winningNumbers.subList(0, Lotto.NUMBER_OF_PICKS);
    }

    @Override
    public LottoNumber bonus() {
        return this.winningNumbers.get(Lotto.NUMBER_OF_PICKS);
    }

    @Override
    public int round() {
        return this.round;
    }
}
package domain.lotto;

public class LottoPurchaseBill {
    private final Lottos lottos;
    private final LottoWinningNumbers winningNumbers;

    public LottoPurchaseBill(Lottos lottos, LottoWinningNumbers winningNumbers) {
        this.lottos = lottos;
        this.winningNumbers = winningNumbers;
    }

    public Lottos lottos() {
        return this.lottos;
    }

    public LottoWinningNumbers winningNumbers() {
        return this.winningNumbers;
    }

    public LottoRound round() {
        return this.winningNumbers.round();
    }

    public LottoResult result() {
        return this.lottos.result(this.winningNumbers);
    }
}
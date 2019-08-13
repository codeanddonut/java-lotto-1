package domain.lotto;

public class LottoResultPair {
    private final LottoRank rank;
    private final int number;

    public LottoResultPair(LottoRank rank, int number) {
        this.rank = rank;
        this.number = number;
    }

    public LottoRank rank() {
        return this.rank;
    }

    public int number() {
        return this.number;
    }
}
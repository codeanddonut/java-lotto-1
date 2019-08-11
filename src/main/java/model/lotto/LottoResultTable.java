package model.lotto;

public class LottoResultTable {
    private final LottoRank rank;
    private final int number;

    public LottoResultTable(LottoRank rank, int number) {
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
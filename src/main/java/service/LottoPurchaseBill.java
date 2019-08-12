package service;

import domain.lotto.Lottos;
import domain.lotto.Round;

public class LottoPurchaseBill {
    private final Round round;
    private final Lottos lottos;

    public LottoPurchaseBill(Round round, Lottos lottos) {
        this.round = round;
        this.lottos = lottos;
    }

    public Round round() {
        return this.round;
    }

    public Lottos lottos() {
        return this.lottos;
    }
}
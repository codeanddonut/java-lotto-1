package model.lotto;

import model.winningnumbers.WinningNumbers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LottoResult implements Iterable<LottoResultTable> {
    private final List<LottoResultTable> table;
    private final Money purchasedAmount;
    private final Money totalEarned;

    public LottoResult(Lottos lottos, WinningNumbers winningNumbers) {
        this.table = lottos.asList().stream()
                                    .map(l -> l.match(winningNumbers))
                                    .flatMap(r -> r.map(Stream::of).orElseGet(Stream::empty))
                                    .collect(Collectors.groupingBy(LottoRank::prize))
                                    .values().stream()
                                    .map(l -> new LottoResultTable(l.get(0), l.size()))
                                    .collect(
                                            Collectors.collectingAndThen(
                                                    Collectors.toList(),
                                                    Collections::unmodifiableList
                                            )
                                    );
        this.purchasedAmount = new Money(Lotto.PRICE * lottos.quantity());
        this.totalEarned = new Money(
                this.table.stream().map(x -> x.rank().prize().amount() * x.number()).reduce(0, Integer::sum)
        );
    }

    public Money purchasedAmount() {
        return this.purchasedAmount;
    }

    public Money totalEarned() {
        return this.totalEarned;
    }

    public double earningRate() {
        return this.totalEarned.earningRate(this.purchasedAmount);
    }

    @Override
    public Iterator<LottoResultTable> iterator() {
        return table.iterator();
    }
}
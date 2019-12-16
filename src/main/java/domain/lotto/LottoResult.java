package domain.lotto;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LottoResult implements Iterable<Map.Entry<LottoRank, Integer>> {
    private final Map<LottoRank, Integer> results;
    private final Money purchasedAmount;
    private final Money totalEarned;

    public LottoResult(Lottos lottos, LottoWinningNumbers winningNumbers) {
        this.results = lottos.stream().map(l -> l.match(winningNumbers))
                                        .flatMap(r -> r.map(Stream::of).orElseGet(Stream::empty))
                                        .collect(
                                                Collectors.groupingBy(
                                                        Function.identity(),
                                                        Collectors.reducing(0, x -> 1, Integer::sum)
                                                )
                                        );
        this.purchasedAmount = new Money(Lotto.PRICE * lottos.quantity());
        this.totalEarned = new Money(
                this.results.entrySet().stream().map(x ->
                    x.getKey().prize().amount() * x.getValue()
                ).reduce(0, Integer::sum)
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
    public Iterator<Map.Entry<LottoRank, Integer>> iterator() {
        return results.entrySet().iterator();
    }
}
package domain.lotto;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lottos implements Iterable<Lotto> {
    private final List<Lotto> lottos;

    public Lottos(List<Lotto> manualLottos, LottoPurchaseQuantity purchaseAmount) {
        final List<Lotto> lottos = IntStream.range(0, purchaseAmount.auto())
                                            .mapToObj(i -> Lotto.autoGenerate())
                                            .collect(Collectors.toList());
        lottos.addAll(manualLottos);
        if (lottos.isEmpty() || (manualLottos.size() != purchaseAmount.manual())) {
            throw new IllegalArgumentException();
        }
        this.lottos = Collections.unmodifiableList(lottos);
    }

    public LottoResult result(LottoWinningNumbers winningNumbers) {
        return new LottoResult(this, winningNumbers);
    }

    public int quantity() {
        return this.lottos.size();
    }

    public Stream<Lotto> stream() {
        return this.lottos.stream();
    }

    @Override
    public Iterator<Lotto> iterator() {
        return this.lottos.iterator();
    }

    @Override
    public String toString() {
        return this.lottos.toString();
    }
}
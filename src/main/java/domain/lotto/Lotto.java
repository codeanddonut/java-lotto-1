package domain.lotto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lotto {
    public static final int PRICE = 1000;
    public static final int NUMBER_OF_PICKS = 6;

    private static final List<LottoNumber> balls = IntStream.rangeClosed(LottoNumber.MIN, LottoNumber.MAX)
                                                            .mapToObj(LottoNumber::of)
                                                            .collect(Collectors.toList());

    private final List<LottoNumber> numbers;

    public static Lotto autoGenerate() {
        Collections.shuffle(balls);
        return new Lotto(new HashSet<>(balls.subList(0, NUMBER_OF_PICKS)));
    }

    public Lotto(Set<LottoNumber> numbers) {
        if (numbers.size() != NUMBER_OF_PICKS) {
            throw new IllegalArgumentException();
        }
        this.numbers = (new ArrayList<>(numbers)).stream()
                                                .sorted()
                                                .collect(
                                                        Collectors.collectingAndThen(
                                                                Collectors.toList(),
                                                                Collections::unmodifiableList
                                                        )
                                                );
    }

    public Optional<LottoRank> match(LottoWinningNumbers winningNumbers) {
        final Set<LottoNumber> complement = new HashSet<>(this.numbers);
        complement.removeAll(winningNumbers.mains());
        return LottoRank.valueOf(
                NUMBER_OF_PICKS - complement.size(),
                complement.contains(winningNumbers.bonus())
        );
    }

    public List<LottoNumber> numbers() {
        return this.numbers;
    }

    @Override
    public String toString() {
        return String.valueOf(this.numbers);
    }
}
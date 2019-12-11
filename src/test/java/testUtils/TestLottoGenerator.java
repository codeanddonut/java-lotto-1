package testUtils;

import domain.lotto.Lotto;
import domain.lotto.LottoNumber;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestLottoGenerator {
    public static Lotto make(String input) {
        return new Lotto(
                Stream.of(input.split(",+")).map(String::trim)
                                            .map(Integer::parseInt)
                                            .map(LottoNumber::of)
                                            .collect(Collectors.toSet())
        );
    }
}

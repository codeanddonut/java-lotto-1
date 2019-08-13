package service;

import service.exception.NoWinningNumbersInDBException;
import domain.lotto.Lotto;
import domain.lotto.LottoNumber;
import domain.lotto.LottoRound;
import domain.lotto.LottoWinningNumbers;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class LottoWinningNumbersFactoryDB {
    public static LottoWinningNumbers of(LottoRound round) {
        return fetch(round);
    }

    private static LottoWinningNumbers fetch(LottoRound round) {
        try {
            final List<LottoNumber> numbers = LottoWinningNumbersDAO.fetch(round)
                                                                .stream()
                                                                .map(LottoNumber::of)
                                                                .collect(Collectors.toList());
            if (numbers.size() != Lotto.NUMBER_OF_PICKS + 1) {
                throw new NoWinningNumbersInDBException();
            }
            return new LottoWinningNumbers(numbers, round);
        } catch (SQLException e) {
            throw new NoWinningNumbersInDBException();
        }
    }

    private LottoWinningNumbersFactoryDB() {}
}
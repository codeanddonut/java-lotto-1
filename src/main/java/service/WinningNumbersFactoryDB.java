package service;

import service.exception.NoWinningNumbersInDBException;
import domain.lotto.Lotto;
import domain.lotto.LottoNumber;
import domain.lotto.Round;
import domain.lotto.WinningNumbers;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WinningNumbersFactoryDB {
    public static WinningNumbers of(Round round) {
        return fetch(round);
    }

    private static WinningNumbers fetch(Round round) {
        try {
            final List<LottoNumber> numbers = WinningNumbersDAO.fetch(round)
                                                                .stream()
                                                                .map(LottoNumber::of)
                                                                .collect(Collectors.toList());
            if (numbers.size() != Lotto.NUMBER_OF_PICKS + 1) {
                throw new NoWinningNumbersInDBException();
            }
            return new WinningNumbers(numbers, round);
        } catch (SQLException e) {
            throw new NoWinningNumbersInDBException();
        }
    }

    private WinningNumbersFactoryDB() {}
}
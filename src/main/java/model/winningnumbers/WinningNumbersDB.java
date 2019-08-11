package model.winningnumbers;

import model.lotto.Lotto;
import model.lotto.LottoNumber;
import model.exception.NoWinningNumbersInDBException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WinningNumbersDB implements WinningNumbers {
    private final CompletableFuture<WinningNumbers> winningNumbers;

    protected WinningNumbersDB(int round) {
        this.winningNumbers = CompletableFuture.supplyAsync(() -> fetchWinningNumbers(round));
    }

    private WinningNumbersVO fetchWinningNumbers(int round) {
        try {
            final List<LottoNumber> numbers = WinningNumbersDAO.fetchWinningNumbers(round)
                                                                .stream()
                                                                .map(LottoNumber::of)
                                                                .collect(Collectors.toList());
            if (numbers.size() != Lotto.NUMBER_OF_PICKS + 1) {
                throw new NoWinningNumbersInDBException();
            }
            return new WinningNumbersVO(numbers, round);
        } catch (SQLException e) {
            throw new NoWinningNumbersInDBException();
        }
    }

    @Override
    public List<LottoNumber> mains() {
        return this.winningNumbers.join().mains();
    }

    @Override
    public LottoNumber bonus() {
        return this.winningNumbers.join().bonus();
    }

    @Override
    public int round() {
        return this.winningNumbers.join().round();
    }
}
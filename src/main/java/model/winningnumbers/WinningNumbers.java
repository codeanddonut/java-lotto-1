package model.winningnumbers;

import model.lotto.LottoNumber;

import java.util.List;

public interface WinningNumbers {
    List<LottoNumber> mains();
    LottoNumber bonus();
    int round();
}
package controller;

import model.lotto.Lotto;
import model.lotto.LottoPurchaseQuantity;
import model.lotto.Lottos;
import model.winningnumbers.WinningNumbers;
import model.winningnumbers.WinningNumbersFactory;

import java.util.List;

import static view.InputView.*;
import static view.OutputView.*;

public class App {
    public static void main(String[] argc) {
        final LottoPurchaseQuantity purchaseQuantity = inputAmountOfManualPicks(inputAmountOfMoney());
        final List<Lotto> manualLottos = inputManualLottoNumbers(purchaseQuantity);
        printPurchaseQuantity(purchaseQuantity);
        final Lottos lottos = new Lottos(manualLottos, purchaseQuantity);
        printLottos(lottos);
        final WinningNumbers winningNumbers = WinningNumbersFactory.ofRecent();
        printWinningNumbers(winningNumbers);
        printResult(lottos, winningNumbers);
    }
}
package view;

import model.lotto.Lotto;
import model.lotto.LottoNumber;
import model.lotto.LottoPurchaseQuantity;
import model.lotto.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InputView {
    private static final String NUMBER_DELIMITER = ",+";
    private static final Scanner input = new Scanner(System.in);

    public static Money inputAmountOfMoney() {
        System.out.println("구입금액을 입력해 주세요.");
        return TryUntilSuccess.run(() ->
                new Money(Integer.parseInt(input.nextLine().trim()))
        );
    }

    public static LottoPurchaseQuantity inputAmountOfManualPicks(Money investment) {
        System.out.println("\n수동으로 구매할 로또 수를 입력해 주세요.");
        return TryUntilSuccess.run(() ->
            new LottoPurchaseQuantity(investment, Integer.parseInt(input.nextLine().trim()))
        );
    }

    public static List<Lotto> inputManualLottoNumbers(LottoPurchaseQuantity purchaseAmount) {
        if (purchaseAmount.manual() == 0) {
            return new ArrayList<>();
        }
        System.out.println("\n수동으로 구매할 번호를 입력해 주세요(쉼표로 구분).");
        return IntStream.range(0, purchaseAmount.manual())
                        .mapToObj(i -> TryUntilSuccess.run(() ->
                                new Lotto(
                                        Stream.of(input.nextLine().split(NUMBER_DELIMITER))
                                                .map(String::trim)
                                                .map(Integer::parseInt)
                                                .map(LottoNumber::of)
                                                .collect(Collectors.toSet())
                                )
                        )).collect(Collectors.toList());

    }
}
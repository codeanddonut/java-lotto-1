package view;

import domain.lotto.*;

import java.text.NumberFormat;

public class OutputView {
    public static void printPurchaseQuantity(LottoPurchaseQuantity purchaseQuantity) {
        System.out.println(
                "\n수동으로 " + purchaseQuantity.manual() + "장, 자동으로 " + purchaseQuantity.auto() + "개를 구매했습니다."
        );
    }

    public static void printLottos(Lottos lottos) {
        lottos.forEach(System.out::println);
    }

    public static void printWinningNumbers(WinningNumbers winningNumbers) {
        System.out.println(
            "\n금주의 당첨 번호 : "
            + winningNumbers.mains()
            + " + 보너스 번호 "
            + winningNumbers.bonus()
        );
    }

    public static void printResult(Lottos lottos, WinningNumbers winningNumbers) {
        final LottoResult result = lottos.result(winningNumbers);
        System.out.println("\n당첨 통계\n---------");
        result.forEach(x -> {
            System.out.println(
                    x.rank().numberOfMatches()
                    + ((x.rank().equals(LottoRank.SECOND)) ? "개 일치, 보너스 볼 일치 (" : "개 일치 (")
                    + NumberFormat.getInstance().format(x.rank().prize())
                    + "원) - "
                    + x.number()
                    + "개"
            );
        });
        System.out.format("총 수익률은 %d%%입니다.", Math.round(result.earningRate()));
    }
}

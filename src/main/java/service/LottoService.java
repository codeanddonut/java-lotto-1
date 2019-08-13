package service;

import domain.lotto.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class LottoService {
    public static LottoPurchaseBill purchase(Money investment, LottoRound round, List<Lotto> manualLottos)
            throws SQLException {
        final Lottos lottos = new Lottos(
                manualLottos,
                new LottoPurchaseQuantity(investment, manualLottos.size())
        );
        LottoPurchaseHistoryDAO.save(round, lottos);
        return new LottoPurchaseBill(lottos, LottoWinningNumbersFactory.of(round));
    }

    public static LottoPurchaseBill history(Timestamp date) throws SQLException, IllegalArgumentException {
        final LottoPurchaseBill bill = LottoPurchaseHistoryDAO.findHistoryByDate(date);
        if (bill.round().val() <= 0) {
            throw new IllegalArgumentException();
        }
        return bill;
    }
}
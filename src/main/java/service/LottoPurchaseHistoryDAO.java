package service;

import domain.lotto.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LottoPurchaseHistoryDAO {
    private static final String LOTTO_DELIMITER = "/";
    private static final String LOTTO_NUM_DELIMITER = ",";

    public static void save(Round round, Lottos lottos) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "INSERT INTO purchase_history (round, lottos) VALUES (?, ?)"
        );
        pstmt.setInt(1, round.val());
        pstmt.setString(2, encode(lottos));
        pstmt.executeUpdate();
        dbConnection.close();
    }

    public static LottoPurchaseBill findHistoryByDate(Timestamp date) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "SELECT round, lottos FROM purchase_history WHERE date = ?"
        );
        pstmt.setTimestamp(1, date);
        final ResultSet result = pstmt.executeQuery();
        LottoPurchaseBill bill = null;
        if (result.next()) {
            bill = new LottoPurchaseBill(new Round(result.getInt(1)), decode(result.getString(2)));
        }
        dbConnection.close();
        if (bill == null) {
            throw new IllegalArgumentException();
        }
        return bill;
    }

    public static List<Timestamp> retrieveDatesFromHistory() {
        try {
            final DBConnection dbConnection = DBConnection.getInstance();
            final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                    "SELECT date FROM purchase_history ORDER BY date DESC LIMIT 1000"
            );
            final List<Timestamp> fetched = new ArrayList<>();
            final ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                fetched.add(result.getTimestamp(1));
            }
            dbConnection.close();
            return fetched;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static String encode(Lottos lottos) {
        return lottos.stream().map(Lotto::numbers)
                            .map(List::stream)
                            .map(x -> x.map(LottoNumber::toString))
                            .map(x -> x.reduce("", (a, b) -> a + LOTTO_NUM_DELIMITER + b))
                            .reduce("", (a, b) -> a + LOTTO_DELIMITER + b)
                            .replace(LOTTO_DELIMITER + LOTTO_NUM_DELIMITER, LOTTO_DELIMITER);
    }

    private static Lottos decode(String encoded) {
        final int quantity = countQuantity(encoded);
        return new Lottos(
                Stream.of(encoded.split(LOTTO_DELIMITER))
                        .filter(x -> x.length() > 0)
                        .map(x -> x.split(LOTTO_NUM_DELIMITER))
                        .map(Stream::of)
                        .map(x -> x.map(Integer::parseInt))
                        .map(x -> x.map(LottoNumber::of))
                        .map(x -> x.collect(Collectors.toSet()))
                        .map(Lotto::new)
                        .collect(Collectors.toList()),
                new LottoPurchaseQuantity(
                        new Money(Lotto.PRICE * quantity),
                        quantity
                )
        );
    }

    private static int countQuantity(String encoded) {
        return countQuantity(encoded, 0, 0);
    }

    private static int countQuantity(String encoded, int begin, int acc) {
        return (begin == -1) ? acc : countQuantity(encoded, encoded.indexOf(LOTTO_DELIMITER, begin + 1), acc + 1);
    }
}
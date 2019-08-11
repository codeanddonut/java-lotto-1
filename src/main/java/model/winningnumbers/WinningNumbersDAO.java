package model.winningnumbers;

import model.DBConnection;
import model.lotto.Lotto;
import model.lotto.LottoNumber;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WinningNumbersDAO {
    protected static List<Integer> fetchWinningNumbers(int round) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "SELECT round, first_num, second_num, third_num, fourth_num, fifth_num, sixth_num, bonus_num " +
                "FROM winning_numbers WHERE round = ?"
        );
        pstmt.setInt(1, round);
        final List<Integer> fetched = fetchFromResult(pstmt.executeQuery());
        dbConnection.close();
        return fetched;
    }

    private static List<Integer> fetchFromResult(ResultSet result) throws SQLException {
        final List<Integer> fetched = new ArrayList<>();
        while (result.next()) {
            for (int i = 2; i <= Lotto.NUMBER_OF_PICKS + 2; i++) {
                fetched.add(result.getInt(i));
            }
        }
        return fetched;
    }

    protected static void register(WinningNumbers winningNumbers) {
        CompletableFuture.runAsync(() -> {
            try {
                registerHelper(winningNumbers);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static void registerHelper(WinningNumbers winningNumbers) throws SQLException {
        final List<LottoNumber> main = winningNumbers.mains();
        final LottoNumber bonus = winningNumbers.bonus();
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "INSERT INTO winning_numbers VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        pstmt.setInt(1, winningNumbers.round());
        for (int i = 0; i < Lotto.NUMBER_OF_PICKS; i++) {
            pstmt.setString(i + 2, main.get(i).toString());
        }
        pstmt.setString(8, bonus.toString());
        pstmt.executeUpdate();
        dbConnection.close();
    }
}
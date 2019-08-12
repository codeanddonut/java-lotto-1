package service;

import domain.lotto.Lotto;
import domain.lotto.Round;
import domain.lotto.WinningNumbers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WinningNumbersDAO {
    protected static List<Integer> fetch(Round round) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "SELECT round, first_num, second_num, third_num, fourth_num, fifth_num, sixth_num, bonus_num " +
                "FROM winning_numbers WHERE round = ?"
        );
        pstmt.setInt(1, round.val());
        final List<Integer> fetched = fetchEachNumber(pstmt.executeQuery());
        dbConnection.close();
        return fetched;
    }

    private static List<Integer> fetchEachNumber(ResultSet result) throws SQLException {
        final List<Integer> fetched = new ArrayList<>();
        while (result.next()) {
            for (int i = 2; i <= Lotto.NUMBER_OF_PICKS + 2; i++) {
                fetched.add(result.getInt(i));
            }
        }
        return fetched;
    }

    public static void register(WinningNumbers winningNumbers) {
        try {
            final DBConnection dbConnection = DBConnection.getInstance();
            final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                    "INSERT INTO winning_numbers VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            pstmt.setInt(1, winningNumbers.round().val());
            for (int i = 0; i < Lotto.NUMBER_OF_PICKS; i++) {
                pstmt.setString(i + 2, winningNumbers.mains().get(i).toString());
            }
            pstmt.setString(8, winningNumbers.bonus().toString());
            pstmt.executeUpdate();
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
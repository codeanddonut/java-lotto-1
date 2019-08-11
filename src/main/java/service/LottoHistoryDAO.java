package model;

import service.LottoHistoryVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LottoHistoryDAO {
    public static void writeHistory(String encodedLottos, int round) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "INSERT INTO purchase_history (round, lottos) VALUES (?, ?)"
        );
        pstmt.setInt(1, round);
        pstmt.setString(2, encodedLottos);
        pstmt.executeUpdate();
        dbConnection.close();
    }

    public static LottoHistoryVO getHistoryByDate(String date) throws SQLException {
        final DBConnection dbConnection = DBConnection.getInstance();
        final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                "SELECT round, lottos FROM purchase_history WHERE date = ?"
        );
        pstmt.setTimestamp(1, Timestamp.from(Instant.parse(date)));
        final ResultSet result = pstmt.executeQuery();
        LottoHistoryVO history = null;
        if (result.next()) {
            history = new LottoHistoryVO(result.getInt(1), result.getString(2));
        }
        dbConnection.close();
        if (history == null) {
            throw new IllegalArgumentException();
        }
        return history;
    }

    public static List<Timestamp> retrieveDatesFromHistory() {
        try {
            final DBConnection dbConnection = DBConnection.getInstance();
            final PreparedStatement pstmt = dbConnection.connect().prepareStatement(
                    "SELECT date FROM purchase_history ORDER BY date LIMIT 1000"
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
        }
        return new ArrayList<>();
    }
}
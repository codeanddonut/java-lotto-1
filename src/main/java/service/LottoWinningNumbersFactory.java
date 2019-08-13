package service;

import service.exception.NoWinningNumbersInDBException;
import domain.lotto.LottoRound;
import domain.lotto.LottoWinningNumbers;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class LottoWinningNumbersFactory {
    private static final Map<LottoRound, LottoWinningNumbers> CACHE = new HashMap<>();

    private static LottoWinningNumbers recent;
    static {
        recent = LottoWinningNumbersFactoryWeb.of();
        LottoWinningNumbersDAO.register(recent);
        CACHE.put(recent.round(), recent);
    }
    private static LocalDateTime scheduledFetchDate = nextAnnouncementDate();

    public static LottoWinningNumbers of(LottoRound round) {
        if (round.val() < 0) {
            throw new IllegalArgumentException();
        }
        if (round.val() == LottoRound.RECENT_ROUND) {
            return ofRecent();
        }
        try {
            CACHE.computeIfAbsent(round, LottoWinningNumbersFactoryDB::of);
            return CACHE.get(round);
        } catch (NoWinningNumbersInDBException e) {
            final LottoWinningNumbers fetched = LottoWinningNumbersFactoryWeb.of(round);
            LottoWinningNumbersDAO.register(fetched);
            CACHE.put(round, fetched);
            return fetched;
        }
    }

    public static LottoWinningNumbers ofRecent() {
        if (LocalDateTime.now().isAfter(scheduledFetchDate)) {
            refresh();
        }
        return recent;
    }

    private static void refresh() {
        final LottoWinningNumbers newRecent = LottoWinningNumbersFactoryWeb.of();
        if (newRecent.round().val() > recent.round().val()) {
            recent = newRecent;
            scheduledFetchDate = nextAnnouncementDate();
            LottoWinningNumbersDAO.register(recent);
        }
    }

    private static LocalDateTime nextAnnouncementDate() {
        final LocalDateTime nextSaturdayOrToday = LocalDateTime.now().with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)
        ).plusHours(21).plusMinutes(30);
        final LocalDateTime nextSaturday = LocalDateTime.now().with(
                TemporalAdjusters.next(DayOfWeek.SATURDAY)
        ).plusHours(21).plusMinutes(30);
        return (nextSaturdayOrToday.equals(nextSaturday)) ? nextSaturday : nextSaturdayOrToday;
    }

    private LottoWinningNumbersFactory() {}
}
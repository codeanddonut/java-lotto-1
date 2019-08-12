package service;

import service.exception.NoWinningNumbersInDBException;
import domain.lotto.Round;
import domain.lotto.WinningNumbers;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class WinningNumbersFactory {
    private static final Map<Round, WinningNumbers> CACHE = new HashMap<>();

    private static WinningNumbers recent;
    static {
        recent = WinningNumbersFactoryWeb.of();
        WinningNumbersDAO.register(recent);
        CACHE.put(recent.round(), recent);
    }
    private static LocalDateTime scheduledFetchDate = nextAnnouncementDate();

    public static WinningNumbers of(Round round) {
        if (round.val() < 0) {
            throw new IllegalArgumentException();
        }
        if (round.val() == Round.RECENT_ROUND) {
            return ofRecent();
        }
        try {
            CACHE.computeIfAbsent(round, WinningNumbersFactoryDB::of);
            return CACHE.get(round);
        } catch (NoWinningNumbersInDBException e) {
            final WinningNumbers fetched = WinningNumbersFactoryWeb.of(round);
            WinningNumbersDAO.register(fetched);
            CACHE.put(round, fetched);
            return fetched;
        }
    }

    public static WinningNumbers ofRecent() {
        if (LocalDateTime.now().isAfter(scheduledFetchDate)) {
            refresh();
        }
        return recent;
    }

    private static void refresh() {
        final WinningNumbers newRecent = WinningNumbersFactoryWeb.of();
        if (newRecent.round().val() > recent.round().val()) {
            recent = newRecent;
            scheduledFetchDate = nextAnnouncementDate();
            WinningNumbersDAO.register(recent);
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

    private WinningNumbersFactory() {}
}
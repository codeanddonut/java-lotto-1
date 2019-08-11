package model.winningnumbers;

import model.exception.NoWinningNumbersInDBException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class WinningNumbersFactory {
    private static WinningNumbers recent = new WinningNumbersWeb();
    private static LocalDateTime scheduledFetchDate = nextAnnouncementDate();

    public static WinningNumbers of(int round) {
        if (round < 0) {
            throw new IllegalArgumentException();
        }
        if (round == 0) {
            return ofRecent();
        }
        try {
            return new WinningNumbersDB(round);
        } catch (NoWinningNumbersInDBException e) {
            final WinningNumbersWeb fetched = new WinningNumbersWeb(round);
            WinningNumbersDAO.register(fetched);
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
        final WinningNumbers newRecent = new WinningNumbersWeb();
        if (newRecent.round() > recent.round()) {
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
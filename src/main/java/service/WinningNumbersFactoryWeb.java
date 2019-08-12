package service;

import service.exception.FailedFetchingWinningNumbersFromWebException;
import domain.lotto.Lotto;
import domain.lotto.LottoNumber;
import domain.lotto.Round;
import domain.lotto.WinningNumbers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WinningNumbersFactoryWeb {
    private static final String NANUM_LOTTERY_URL = "https://m.dhlottery.co.kr/gameResult.do?method=byWin";

    public static WinningNumbers of(Round round) {
        return fetch(round);
    }

    public static WinningNumbers of() {
        return fetch(new Round(0));
    }

    private static WinningNumbers fetch(Round round) {
        try {
            final String html = winningNumbersHttpRequest(round);
            final List<LottoNumber> numbers = new ArrayList<>();
            final Matcher numbersMatcher = Pattern.compile(">[0-9]+<").matcher(html);
            while (numbersMatcher.find()) {
                final String token = numbersMatcher.group();
                numbers.add(LottoNumber.of(Integer.parseInt(token.substring(1, token.indexOf("<")))));
            }
            if (numbers.size() != Lotto.NUMBER_OF_PICKS + 1) {
                if (round.val() != 0) {
                    return fetch(new Round());
                }
                throw new FailedFetchingWinningNumbersFromWebException();
            }
            if (round.val() == 0) {
                final Matcher roundMatcher = Pattern.compile("<option value=\"[0-9]+\" >").matcher(html);
                roundMatcher.find();
                final String token = roundMatcher.group();
                return new WinningNumbers(
                        numbers,
                        new Round(Integer.parseInt(token.substring(15, token.lastIndexOf("\""))))
                );
            }
            return new WinningNumbers(numbers, round);
        } catch (IOException e) {
            throw new FailedFetchingWinningNumbersFromWebException();
        }
    }

    private static String winningNumbersHttpRequest(Round round) throws IOException {
        final StringBuilder html = new StringBuilder();
        final String roundAttr = (round.val() == Round.RECENT_ROUND) ? "" : "&drwNo=" + round;
        final HttpURLConnection con = (HttpURLConnection) new URL(NANUM_LOTTERY_URL + roundAttr).openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "euc-kr"));
        for (String inputLine = ""; inputLine != null; inputLine = in.readLine()) {
            html.append(inputLine);
        }
        in.close();
        con.disconnect();
        return html.toString();
    }

    private WinningNumbersFactoryWeb() {}
}
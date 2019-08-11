package model.winningnumbers;

import model.lotto.Lotto;
import model.lotto.LottoNumber;
import model.exception.FailedFetchingWinningNumbersFromWebException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WinningNumbersWeb implements WinningNumbers {
    private static final String NANUM_LOTTERY_URL = "https://m.dhlottery.co.kr/gameResult.do?method=byWin";

    private final CompletableFuture<WinningNumbers> winningNumbers;

    protected WinningNumbersWeb(int round) {
        this.winningNumbers = CompletableFuture.supplyAsync(() -> fetchAndParseWinningNumbers(round));
    }

    protected WinningNumbersWeb() {
        this(0);
    }

    private WinningNumbersVO fetchAndParseWinningNumbers(int round) {
        try {
            final String html = winningNumbersHttpRequest(round);
            final List<LottoNumber> numbers = new ArrayList<>();
            final Matcher numbersMatcher = Pattern.compile(">[0-9]+<").matcher(html);
            while (numbersMatcher.find()) {
                final String token = numbersMatcher.group();
                numbers.add(LottoNumber.of(Integer.parseInt(token.substring(1, token.indexOf("<")))));
            }
            if (numbers.size() != Lotto.NUMBER_OF_PICKS + 1) {
                if (round != 0) {
                    return fetchAndParseWinningNumbers(0);
                }
                throw new FailedFetchingWinningNumbersFromWebException();
            }
            if (round == 0) {
                final Matcher roundMatcher = Pattern.compile("<option value=\"[0-9]+\" >").matcher(html);
                final String token = roundMatcher.group();
                round = Integer.parseInt(token.substring(15, token.lastIndexOf("\"")));
            }
            return new WinningNumbersVO(numbers, round);
        } catch (IOException e) {
            throw new FailedFetchingWinningNumbersFromWebException();
        }
    }

    private String winningNumbersHttpRequest(int round) throws IOException {
        final StringBuilder html = new StringBuilder();
        final String roundAttr = (round == 0) ? "" : "&drwNo=" + round;
        final HttpURLConnection con = (HttpURLConnection) new URL(NANUM_LOTTERY_URL + roundAttr).openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        for (String inputLine = ""; inputLine != null; inputLine = in.readLine()) {
            html.append(inputLine);
        }
        in.close();
        con.disconnect();
        return html.toString();
    }

    @Override
    public List<LottoNumber> mains() {
        return this.winningNumbers.join().mains();
    }

    @Override
    public LottoNumber bonus() {
        return this.winningNumbers.join().bonus();
    }

    @Override
    public int round() {
        return this.winningNumbers.join().round();
    }
}
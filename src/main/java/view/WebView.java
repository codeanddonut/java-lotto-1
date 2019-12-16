package view;

import domain.lotto.*;
import domain.lotto.LottoPurchaseBill;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WebView {
    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public static String error() {
        return "<div class=\"inner\">" +
                "<section>" +
                "<div class=\"content\" align=\"center\">" +
                "<b>잘못된 입력입니다.</b>" +
                "</div>" +
                "</section>" +
                "</div>";
    }

    public static String printIndex(List<Timestamp> dates, LottoRound recentRound) {
        final Map<String, Object> model = new HashMap<String, Object>() {{
            put("price", Lotto.PRICE);
            put("priceFormatted", NumberFormat.getInstance().format(Lotto.PRICE));
            put("history", WebView.historySelect(dates));
            put("recentRoundMenu", WebView.roundSelect(recentRound));
        }};
        return render(model, "app.html");
    }

    private static String roundSelect(LottoRound recentRound) {
        final StringBuilder menu = new StringBuilder();
        menu.append("<option value=\"" + recentRound + "\" selected>" + recentRound + " 회</option>");
        for (int i = recentRound.val() - 1; i > 0; i--) {
            menu.append("<option value=\"" + i + "\">" + i + " 회</option>");
        }
        return menu.toString();
    }

    private static String historySelect(List<Timestamp> timestamps) {
        return timestamps.stream()
                        .map(d -> "<option value=\"" + d.toInstant() + "\">" + d.toInstant() + "</option>")
                        .reduce("", String::concat);
    }

    public static String printPurchase(LottoPurchaseBill bill) {
        final Map<String, Object> model = new HashMap<String, Object>() {{
            put("round", bill.round());
            put("winningNumbers", WebView.formatWinningNumbers(bill.winningNumbers()));
            put("purchasedLottos", WebView.formatLottos(bill.lottos()));
            put("result", WebView.formatResult(bill.result()));
        }};
        return render(model, "result.html");
    }

    public static String printHistory(LottoPurchaseBill bill) {
        final Map<String, Object> model = new HashMap<String, Object>() {{
            put("round", bill.round());
            put("winningNumbers", WebView.formatWinningNumbers(bill.winningNumbers()));
            put("purchasedLottos", WebView.formatLottos(bill.lottos()));
            put("result", WebView.formatResult(bill.result()));
        }};
        return render(model, "result.html");
    }

    public static String formatWinningNumbers(LottoWinningNumbers winningNumbers) {
        final StringBuilder formatted = new StringBuilder();
        winningNumbers.mains().forEach(x -> formatted.append(
                "<span class=\"ball color" + Integer.parseInt(x.toString()) / 10 + "\">" + x.toString() + "</span>"
        ));
        formatted.append("<span> + </span>");
        formatted.append(
                "<span class=\"ball color5\">"
                + winningNumbers.bonus().toString()
                + "</span>"
        );
        return formatted.toString();
    }

    public static String formatLottos(Lottos lottos) {
        return lottos.toString()
                    .replace("[[", "[")
                    .replace("]]", "]")
                    .replace("[", "<span class=\"lotto\">")
                    .replace("]", "</span>")
                    .replace(">, <", "><br /><");
    }

    public static String formatResult(LottoResult result) {
        final Function<Integer, String> numberFormat = NumberFormat.getInstance()::format;
        final StringBuilder formatted = new StringBuilder("<br />");
        result.forEach(x ->
                formatted.append(
                        x.getKey().numberOfMatches()
                        + ((x.getKey().equals(LottoRank.SECOND)) ? "개 일치, 보너스 볼 일치 (" : "개 일치 (")
                        + numberFormat.apply(x.getKey().prize().amount())
                        + "원) - "
                        + x.getValue()
                        + "개<br />"
                )
        );
        formatted.append("투자금 : " + numberFormat.apply(result.purchasedAmount().amount()) + "원<br />");
        formatted.append("수익금 : " + numberFormat.apply(result.totalEarned().amount()) + "원<br />");
        formatted.append("수익률 : " + Math.round(result.earningRate() * 100.0) / 100.0 + "%");
        return formatted.toString();
    }
}
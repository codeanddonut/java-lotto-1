package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.lotto.*;
import service.LottoPurchaseBill;
import service.LottoPurchaseHistoryDAO;
import service.LottoService;
import service.WinningNumbersFactory;
import spark.Request;
import view.WebView;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static spark.Spark.*;

public class WebUILottoApplication {
    private static final String MANUAL_NUM_DELIMITER = ",+";

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/static");

        get("/", (req, res) -> WebView.printIndex(
                LottoPurchaseHistoryDAO.retrieveDatesFromHistory(),
                WinningNumbersFactory.ofRecent().round()
            )
        );

        post("/purchase", "application/json", (req, res) -> Try.orThrow(() -> purchase(req), res));

        post("/history", (req, res) -> Try.orThrow(() -> history(req), res));

        get("*", (req, res) -> {
            res.status(404);
            res.redirect("404.html");
            return 404;
        });
    }

    private static String purchase(Request req) throws SQLException {
        final JsonObject reqParams = (new JsonParser().parse(req.body()).getAsJsonObject());
        final LottoPurchaseBill bill = LottoService.purchase(
                new Money(reqParams.get("investment").getAsInt()),
                new Round(reqParams.get("round").getAsInt()),
                IntStream.range(0, reqParams.entrySet().size() - 2)
                        .mapToObj(i -> reqParams.get("manualNumbers[" + i + "]").getAsString())
                        .map(x -> x.split(MANUAL_NUM_DELIMITER))
                        .map(Stream::of)
                        .map(x -> x.map(String::trim))
                        .map(x -> x.map(Integer::parseInt))
                        .map(x -> x.map(LottoNumber::of))
                        .map(x -> x.collect(Collectors.toSet()))
                        .map(Lotto::new)
                        .collect(Collectors.toList())
        );
        final WinningNumbers winningNumbers = WinningNumbersFactory.of(bill.round());
        return WebView.printPurchase(bill, winningNumbers, bill.lottos().result(winningNumbers));
    }

    private static String history(Request req) throws SQLException {
        final LottoPurchaseBill bill = LottoService.history(
                Timestamp.from(
                        Instant.parse(
                                (new JsonParser()).parse(req.body()).getAsJsonObject().get("date").getAsString()
                        )
                )
        );
        final WinningNumbers winningNumbers = WinningNumbersFactory.of(bill.round());
        return WebView.printHistory(bill, winningNumbers, bill.lottos().result(winningNumbers));
    }
}
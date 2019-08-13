package controller;

import spark.Response;
import view.WebView;

public class Try {
    public static String orThrow(SubroutineWithException<String, Exception> f, Response res) {
        try {
            return f.get();
        } catch (Exception e) {
            res.status(500);
            return WebView.error();
        }
    }
}
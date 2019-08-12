package domain.lotto;

public class LottoPurchaseQuantity {
    private final int auto;
    private final int manual;

    public LottoPurchaseQuantity(Money investment, int manualPurchaseQuantity) {
        final int totalPurchaseQuantity = investment.amount() / Lotto.PRICE;
        if ((totalPurchaseQuantity < manualPurchaseQuantity) || (totalPurchaseQuantity + manualPurchaseQuantity < 0)) {
            throw new IllegalArgumentException();
        }
        this.auto = totalPurchaseQuantity - manualPurchaseQuantity;
        this.manual = manualPurchaseQuantity;
    }

    public int auto() {
        return this.auto;
    }

    public int manual() {
        return this.manual;
    }
}
package model.lotto;

import java.util.Objects;

public class Money {
    private static final int MIN = 0;

    private final int amount;

    public Money(int amount) {
        if (amount < MIN) {
            throw new IllegalArgumentException();
        }
        this.amount = amount;
    }

    public double earningRate(Money investment) {
        return (this.amount / (double) investment.amount - 1.0) * 100.0;
    }

    public int amount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return String.valueOf(this.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money)) {
            return false;
        }
        final Money rhs = (Money) o;
        return this.amount == rhs.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.amount);
    }
}
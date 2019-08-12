package domain.lotto;

import java.util.Objects;

public class Round {
    public static final int RECENT_ROUND = 0;

    private final int round;

    public Round(int number) {
        if (number < 0) {
            throw new IllegalArgumentException();
        }
        this.round = number;
    }

    public Round() {
        this.round = RECENT_ROUND;
    }

    public int val() {
        return this.round;
    }

    @Override
    public String toString() {
        return String.valueOf(this.round);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Round)) {
            return false;
        }
        Round rhs = (Round) o;
        return this.round == rhs.round;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.round);
    }
}
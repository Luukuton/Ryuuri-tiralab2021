package ryuuri.ui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.regex.Pattern;

public class LongField extends TextField {
    private final LongProperty value;
    private final long minValue, maxValue;

    /**
     * Creates a LongField holding a long value with given restrictions.
     *
     * @param minValue Minimum value as long
     * @param maxValue Maximum value as long
     * @param initialValue Starting value as long
     * @param defaultValue Default or a fallback value on error as long
     */
    public LongField(long minValue, long maxValue, long initialValue, long defaultValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException(
                    "LongField min value " + minValue + " > " + maxValue
            );
        }

        if (!((minValue <= initialValue) && (initialValue <= maxValue))) {
            throw new IllegalArgumentException(
                    "LongField initialValue " + initialValue + " not between " + minValue + " and " + maxValue
            );
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
        value = new SimpleLongProperty(initialValue);
        setText(initialValue + "");

        final LongField longField = this;

        value.addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                longField.setText("");
            } else {
                if (newValue.longValue() < longField.minValue) {
                    value.setValue(longField.minValue);
                    return;
                }

                if (newValue.longValue() > longField.maxValue) {
                    value.setValue(longField.maxValue);
                    return;
                }

                if (!(newValue.longValue() == 0 && (textProperty().get() == null || "".equals(textProperty().get())))) {
                    // FIXME: IllegalArgumentException when erasing the first digit from a round number.
                    // Eg. 10000, 20000, 30..
                    // Platform.runLater(() -> longField.setText(newValue.toString())); solves this but there are severe side effects.
                    longField.setText(newValue.toString());
                }
            }
        });

        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"-0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });

        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || "".equals(newValue)) {
                value.setValue(0);
                return;
            } else if (newValue.equals("-")) { // For negative values
                textProperty().setValue("-");
                return;
            } else if (!Pattern.matches("^-?[0-9]\\d*$", newValue)) {
                textProperty().setValue(String.valueOf(defaultValue));
                return;
            }

            long longValue;
            try {
                longValue = Long.parseLong(newValue);
                if (longField.minValue > longValue || longValue > longField.maxValue) {
                    textProperty().setValue(oldValue);
                }

                value.set(Long.parseLong(textProperty().get()));
            } catch (NumberFormatException e) {
                longValue = defaultValue;
                if (longField.minValue > longValue || longValue > longField.maxValue) {
                    textProperty().setValue(oldValue);
                }

                value.set(defaultValue);
            }
        });
    }

    /**
     * Overloads the constructor with default of 1.
     *
     * @param minValue Minimum value as long
     * @param maxValue Maximum value as long
     * @param initialValue Starting value as long
     */
    public LongField(long minValue, long maxValue, long initialValue) {
        this(minValue, maxValue, initialValue, 1);
    }

    /**
     * Gets the value of the LongField.
     *
     * @return Value as long
     */
    public long getValue() {
        return value.getValue();
    }

    /**
     * Sets the value of the LongField.
     *
     * @param newValue New value as long
     */
    public void setValue(long newValue) {
        value.setValue(newValue);
    }


    /**
     * Gets the LongProperty of the class
     *
     * @return LongProperty
    */
    public LongProperty valueProperty() {
        return value;
    }
}

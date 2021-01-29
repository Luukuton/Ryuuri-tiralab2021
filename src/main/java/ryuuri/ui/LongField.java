package ryuuri.ui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LongField extends TextField {
    private final LongProperty value;
    private final long minValue, maxValue;

    public LongField(long minValue, long maxValue, long initialValue) {
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
                    longField.setText(newValue.toString());
                }
            }
        });

        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });

        this.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || "".equals(newValue)) {
                value.setValue(0);
                return;
            }

            final long longValue = Long.parseLong(newValue);

            if (longField.minValue > longValue || longValue > longField.maxValue) {
                textProperty().setValue(oldValue);
            }

            value.set(Long.parseLong(textProperty().get()));
        });
    }

    public long getValue() {
        return value.getValue();
    }

    public void setValue(long newValue) {
        value.setValue(newValue);
    }

    public LongProperty valueProperty() {
        return value;
    }
}

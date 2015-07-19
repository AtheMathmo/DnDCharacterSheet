package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.*;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 16/07/15.
 * DocumentFilter to be used in conjunction to the DnDCharacterSheet project.
 */
public class NumericalFilter extends DocumentFilter {
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private int maxCharacters = Integer.MAX_VALUE;

    private boolean needsSign = false;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public boolean isNeedsSign() {
        return needsSign;
    }

    public void setNeedsSign(boolean needsSign) {
        this.needsSign = needsSign;
    }

    public NumericalFilter() {
        super();
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument()).getLength() + string.length() > maxCharacters) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (!IsValidEntry(fb.getDocument(),string, offset)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        super.insertString(fb, offset, string, attr);

    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

        if ((fb.getDocument()).getLength() + text.length() - length > maxCharacters) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (!IsValidEntry(fb.getDocument(),text, offset)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        // Update if it was entered without a sign
        if (this.needsSign && offset == 0 && !(text.startsWith("+") || text.startsWith("-"))) {
                text = "+".concat(text);
        }

        super.replace(fb, offset, length, text, attrs);
    }

    private boolean IsInteger(String string) {
        if(string.isEmpty()) return false;
        for(int i = 0; i < string.length(); i++) {
            if(i == 0 && string.charAt(i) == '-') {
                if(string.length() == 1) return false;
                else continue;
            }
            if(Character.digit(string.charAt(i), 10) < 0) return false;
        }
        return true;
    }

    private boolean IsValidEntry(Document currentDoc, String newText, int offset) {
        boolean isPositive = true;
        int currentVal = 0;
        int currentLength = currentDoc.getLength();

        boolean changingSign = false;

        // If we are using a signed field
        if (this.needsSign)
        {
            // if we try to start without a sign or a number
            if (offset == 0 && !(newText.startsWith("+") || newText.startsWith("-")) && !IsInteger(newText)) {
                return false;
            }

            // check if we're changing the sign
            changingSign = (offset == 0) && (newText.startsWith("+") || newText.startsWith("-"));

            try {
                // Check if a sign currently exists, and that we're not rewriting it
                if (currentLength > 0 &&  !changingSign) {
                    isPositive = currentDoc.getText(0, 1).equals("+");
                } else {
                    isPositive = newText.startsWith("+");
                }

                // if there is currently a value, get it.
                if (currentLength > 1) {
                    currentVal = Integer.parseInt(currentDoc.getText(1,currentLength-1));
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // if there is currently a value, get it.
                if (currentLength > 0) {
                    currentVal = Integer.parseInt(currentDoc.getText(0,currentLength));
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        // If changing sign ignore first char in integer part.
        String integerPart = changingSign ? newText.substring(1) : newText;
        // If only changing sign, need to set to 0.
        if (integerPart.length() == 0) integerPart = "0";

        if (IsInteger(integerPart)) {
            int newVal = 10 * currentVal + Integer.parseInt(integerPart);
            if (isPositive) {
                if (newVal >= minValue && newVal <= maxValue) {
                    return true;
                }
            } else {
                if (-newVal >= minValue && -newVal <= maxValue) {
                    return true;
                }
            }
        }
        return false;
    }
}


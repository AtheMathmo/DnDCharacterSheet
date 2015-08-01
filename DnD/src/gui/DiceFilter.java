package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 31/07/15.
 *
 * DocumentFilter which forces input to be in dnd dice format.
 */
public class DiceFilter extends DocumentFilter {

    public DiceFilter() {

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        if (IsValidEntry(doc, string, offset)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        if (IsValidEntry(doc, text, offset))
        {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean IsValidEntry(Document currentDoc, String newText, int offset) {
        int currentLength = currentDoc.getLength();
        try {
            String currentText = currentDoc.getText(0, currentLength);
            int dIndex = currentText.indexOf("d");

            if (dIndex == -1 && IsInteger(newText)) { // Integer before a d exists
                return true;
            } else if (dIndex == -1 && IsInteger(currentText)) { // d after an integer exists
                System.out.println(newText);
                if (newText.contains("d") && IsIntegerWithd(newText, newText.indexOf("d"))) {
                    return true;
                }
            } else if (offset <= dIndex && IsInteger(newText)) { // Inserting an integer before an existing d
                return true;
            } else if (offset > dIndex && IsInteger(newText)) { // Inserting an integer after an existing d
                return true;
            }
        } catch(BadLocationException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;

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

    private boolean IsIntegerWithd(String string, int dIndex) {
        if (dIndex == -1) {
            throw new IllegalArgumentException("There was no d in the passed string");
        }

        String withoutD = string.substring(0,dIndex).concat(string.substring(dIndex+1));
        return withoutD.isEmpty() || IsInteger(withoutD);
    }
}

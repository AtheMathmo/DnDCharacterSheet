package gui;

import engine.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 01/08/15.
 *
 * Contains panel instantiated by TopMenu. Allows custom rolls.
 */
public class RollPanel extends JPanel implements ItemListener {

    private static final String[] die = new String[] { "d4", "d6", "d8", "d12", "d20", "d100" };
    private static final Random rand = new Random();

    private static String consoleLog;
    private Font buttonFont;

    private JPanel defaultDicePanel;
    private JSpinner countSpinner;
    private JComboBox<String> diceSelector;
    private JTextField customDiceField;
    private JTextField bonusField;

    private ButtonGroup buttonGroup;
    private JTextArea outputTextArea;


    public RollPanel() {
        this.InitializePanel();
        this.AddComponents();
    }

    private void InitializePanel() {
        Font defaultButtonFont = UIManager.getLookAndFeelDefaults().getFont("Button.font");
        buttonFont = new Font(defaultButtonFont.getFontName(), defaultButtonFont.getStyle(), defaultButtonFont.getSize() * 2);
        this.setLayout(new GridBagLayout());
    }

    private void AddComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; constraints.gridy = 0;
        constraints.weightx = 1; constraints.anchor = GridBagConstraints.LINE_START;

        this.defaultDicePanel = new JPanel();

        JLabel chooseLabel = new JLabel("Choose Roll:");
        chooseLabel.setFont(CharacterSheetGUI.SectionTitleFont);
        constraints.gridwidth = 4;
        this.add(chooseLabel, constraints);

        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridwidth = 2;
        constraints.gridx += 1; constraints.gridy += 1;
        JLabel diceLabel = new JLabel("Dice");
        this.add(diceLabel, constraints);

        constraints.gridwidth = 1; constraints.gridx += 2;
        JLabel bonusLabel = new JLabel("Bonus");
        this.add(bonusLabel, constraints);

        constraints.gridx = 0; constraints.gridy +=1;
        JRadioButton setDiceButton = new JRadioButton();
        setDiceButton.setActionCommand("default");
        setDiceButton.addItemListener(this);
        setDiceButton.setSelected(true);
        this.add(setDiceButton, constraints);

        defaultDicePanel.setLayout(new GridBagLayout());

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1,1,10,1);
        countSpinner = new JSpinner(spinnerModel);
        constraints.gridx = 0; constraints.gridy = 0;
        defaultDicePanel.add(countSpinner, constraints);

        constraints.gridx += 1;
        diceSelector = new JComboBox<>(die);
        defaultDicePanel.add(diceSelector, constraints);

        constraints.gridx = 1; constraints.gridy = 2; constraints.gridwidth = 2;
        this.add(defaultDicePanel, constraints);

        bonusField = new JTextField(5);
        AbstractDocument bonusDoc = (AbstractDocument) bonusField.getDocument();
        NumericalFilter signedFilter = new NumericalFilter();
        signedFilter.setNeedsSign(true); signedFilter.setMaxCharacters(4);
        bonusDoc.setDocumentFilter(signedFilter);

        constraints.gridx += 2; constraints.gridheight = 2;
        constraints.gridwidth = 1;
        this.add(bonusField, constraints);

        constraints.gridheight = 1;
        constraints.gridx = 0; constraints.gridy += 1;

        JRadioButton setCustomButton = new JRadioButton();
        setCustomButton.addItemListener(this);
        setCustomButton.setActionCommand("custom");
        this.add(setCustomButton, constraints);

        customDiceField = new JTextField(12);
        customDiceField.setEnabled(false);

        AbstractDocument customDiceDoc = (AbstractDocument) customDiceField.getDocument();
        customDiceDoc.setDocumentFilter(new DiceFilter());

        constraints.gridx += 1; constraints.gridwidth = 2;
        this.add(customDiceField, constraints);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(setDiceButton);
        buttonGroup.add(setCustomButton);

        JButton rollButton = new JButton(new RollAction());
        rollButton.setFont(buttonFont);
        constraints.gridy += 1; constraints.gridx = 0;
        constraints.gridwidth = 4;
        this.add(rollButton, constraints);

        outputTextArea = new JTextArea(6,25);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(false);
        // use append and suffix with \n.
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        constraints.gridy += 1; constraints.gridx = 0;

        this.add(outputScrollPane, constraints);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // HACK : using action command as no other identifier. But ItemListener gives us info on item status.
        String actionCommand = ((AbstractButton) e.getSource()).getActionCommand();

        if (actionCommand.equals("default")) {
            UpdateStatus(e, this.defaultDicePanel);
        } else if (actionCommand.equals("custom")) {
            UpdateStatus(e, this.customDiceField);
        }
    }

    private void UpdateStatus(ItemEvent e, JComponent rootComp) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // we do a 1 depth search for components
            for (Component comp : rootComp.getComponents()) comp.setEnabled(true);
            rootComp.setEnabled(true);
        }

        if (e.getStateChange() == ItemEvent.DESELECTED) {
            // we do a 1 depth search for components
            for (Component comp : rootComp.getComponents()) comp.setEnabled(false);
            rootComp.setEnabled(false);
        }
    }

    private class RollAction extends AbstractAction {
        //TODO make this action pull values from the panel to perform the roll.
        public RollAction() {
            super("Roll");

            putValue(SHORT_DESCRIPTION, "Roll the selected dice.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] rollValues = GetRoll();

            if (rollValues.length == 3) {
                String outputString = MakeRoll(rollValues[0], rollValues[1], rollValues[2]);
                outputTextArea.append(outputString);
            }
        }

        private String MakeRoll(int diceCount, int diceSize, int bonus) {
            int[] rolls = new int[diceCount];
            int sum = 0;

            String resultString = String.format("Rolling %dd%d + %d: ", diceCount, diceSize, bonus);

            for (int i = 0; i < diceCount; i++) {
                rolls[i] = rand.nextInt(diceSize) + 1;

                resultString += Integer.toString(rolls[i]);
                sum += rolls[i];

                resultString += " + ";
            }

            resultString += Integer.toString(bonus);
            resultString += " = ";
            resultString += Integer.toString(sum + bonus);


            return (resultString + "\n");
        }
        private int[] GetRoll() {
            String actionCommand = buttonGroup.getSelection().getActionCommand();
            int diceCount = 0;
            int diceSize = 0;
            int bonus;

            if (actionCommand.equals("default")) {
                int[] diceValues = RollFromDiceString((String)diceSelector.getSelectedItem());
                if (diceValues.length != 1) {
                    outputTextArea.append("An unexpected error occurred. Please report to the issues page (see Info).\n");
                    return new int[0];
                }
                diceCount = (int)countSpinner.getValue();
                diceSize = diceValues[0];
            } else if (actionCommand.equals("custom")) {
                int[] diceValues = RollFromDiceString(customDiceField.getText());

                if (diceValues.length < 2) {
                    outputTextArea.append("Please input a valid dice in the format XdY.\n");
                    return new int[0];
                }
                diceCount = diceValues[0];
                diceSize = diceValues[1];
            }
            bonus = GetBonus(bonusField.getText());

            return new int[] { diceCount, diceSize, bonus};

        }

        private int[] RollFromDiceString(String diceString) {
            int dIndex = diceString.indexOf("d");
            int stringLength = diceString.length();

            if (dIndex == -1) {
                return new int[0];
            }

            if (dIndex == 0) {
                if (stringLength > 1)
                    return new int[] { Integer.parseInt(diceString.substring(1)) };
                else
                    return new int[0];
            }

            int diceCount = Integer.parseInt(diceString.substring(0, dIndex));
            if (stringLength <= dIndex + 1)
                return new int[0];
            int diceSize = Integer.parseInt(diceString.substring(dIndex+1));

            return new int[] { diceCount, diceSize};
        }

        private int GetBonus(String signedString) {
            if (signedString.length() < 2)
            {
                return 0;
            }

            boolean isPositive = signedString.startsWith("+");
            int integerValue = Integer.parseInt(signedString.substring(1));
            return isPositive ? integerValue : -integerValue;
        }
    }

}

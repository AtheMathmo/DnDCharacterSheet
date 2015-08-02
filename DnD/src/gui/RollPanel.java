package gui;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 01/08/15.
 *
 * Contains panel instantiated by ToolBar. Allows custom rolls.
 */
public class RollPanel extends JPanel implements ItemListener {

    private static final String[] die = new String[] { "d4", "d6", "d8", "d12", "d20", "d100" };
    private Font buttonFont;

    private JPanel defaultDicePanel;
    private JTextField customDiceField;
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
        JSpinner countSpinner = new JSpinner(spinnerModel);

        // Right now this isn't working
        //TODO investigate
        AbstractDocument spinnerDoc = (AbstractDocument) ((JSpinner.DefaultEditor)countSpinner.getEditor()).getTextField().getDocument();
        NumericalFilter numFilter = new NumericalFilter();
        numFilter.setMaxCharacters(2);
        spinnerDoc.setDocumentFilter(numFilter);
        constraints.gridx = 0; constraints.gridy = 0;
        defaultDicePanel.add(countSpinner, constraints);

        constraints.gridx += 1;
        JComboBox<String> rollSelected = new JComboBox<>(die);
        defaultDicePanel.add(rollSelected, constraints);

        constraints.gridx = 1; constraints.gridy = 2; constraints.gridwidth = 2;
        this.add(defaultDicePanel, constraints);

        JTextField bonusField = new JTextField(5);
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
        // use append and suffix with \n.
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
            outputTextArea.append("This test worked\n");
        }
    }

}

package gui;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 01/08/15.
 *
 * Contains panel instantiated by ToolBar. Allows custom rolls.
 */
public class RollPanel extends JPanel {

    private static final String[] die = new String[] { "d4", "d6", "d8", "d12", "d20", "d100" };

    public RollPanel() {
        this.InitializePanel();
        this.AddComponents();
    }

    private void InitializePanel() {
        this.setLayout(new GridBagLayout());
    }

    private void AddComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; constraints.gridy = 0;
        constraints.weightx = 1; constraints.anchor = GridBagConstraints.CENTER;

        JLabel chooseLabel = new JLabel("Choose Roll:");
        constraints.gridwidth = 3;
        this.add(chooseLabel, constraints);

        constraints.gridwidth = 1;


        constraints.gridx += 1; constraints.gridy += 1;

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1,1,10,1);
        JSpinner countSpinner = new JSpinner(spinnerModel);

        // Right now this isn't working
        //TODO investigate
        AbstractDocument spinnerDoc = (AbstractDocument) ((JSpinner.DefaultEditor)countSpinner.getEditor()).getTextField().getDocument();
        NumericalFilter numFilter = new NumericalFilter();
        numFilter.setMaxCharacters(2);
        spinnerDoc.setDocumentFilter(numFilter);
        this.add(countSpinner, constraints);

        constraints.gridx = 2;
        JComboBox<String> rollSelected = new JComboBox<>(die);
        this.add(rollSelected, constraints);


        JButton rollButton = new JButton("Roll");
        constraints.gridy += 1; constraints.gridx = 1;
        this.add(rollButton, constraints);

        JTextArea outputArea = new JTextArea(3,20);
        outputArea.setEditable(false);
        // use append and suffix with \n.
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        constraints.gridy += 1; constraints.gridx = 0;
        constraints.gridwidth = 3;
        this.add(outputScrollPane, constraints);
    }

}

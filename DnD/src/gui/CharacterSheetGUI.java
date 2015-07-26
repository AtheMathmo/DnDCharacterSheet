package gui;

import engine.Character;
import engine.DataHandler;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

public class CharacterSheetGUI extends JFrame {
    public static final Font SectionTitleFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

    public static final Border SectionBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createBevelBorder(BevelBorder.LOWERED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10));

    private final JFileChooser fileChooser = new JFileChooser();

    private JPanel contentPane;
    private HeaderPanel headerPanel;
    private CharacterValuesHolder characterValuesHolder;
    private CombatPanel combatPanel;
    private TraitsPanel traitsPanel;

    private Character character;
    private DataHandler dataHandler;

    public int GetSignedIntValue(String signedString) {
        if (signedString.length() < 2)
        {
            return 0;
        }

        boolean isPositive = signedString.startsWith("+");
        int integerValue = Integer.parseInt(signedString.substring(1));
        return isPositive ? integerValue : -integerValue;
    }


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                CharacterSheetGUI frame = new CharacterSheetGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public CharacterSheetGUI() {
        InitializeData();
        InitializeGUI();
        AddPanels();
        //UpdateAllFields();
    }

    private void InitializeData() {
        this.dataHandler = new DataHandler();

        //TODO automatically check for last used file...
        //if (this.dataHandler.CheckSavedData()) {
            //this.character = this.dataHandler.ReadData();
        //} else {
        this.character = new Character();
        //}
    }

    private void UpdateAllFields() {
        headerPanel.UpdateFields();
        characterValuesHolder.UpdateFields();
        traitsPanel.UpdateFields();
        combatPanel.UpdateFields();
    }

    private void InitializeGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 1600, 900);
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }

    private void AddPanels() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;

        JPanel holdingPanel = new JPanel(new GridBagLayout());

        constraints.weighty = 1;
        constraints.gridwidth = 3;
        headerPanel = new HeaderPanel();
        holdingPanel.add(headerPanel, constraints);

        constraints.weighty = 4;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        characterValuesHolder = new CharacterValuesHolder();
        holdingPanel.add(characterValuesHolder, constraints);

        constraints.gridx = 1;
        combatPanel = new CombatPanel();
        holdingPanel.add(combatPanel, constraints);

        constraints.gridx = 2;
        traitsPanel = new TraitsPanel();
        holdingPanel.add(traitsPanel, constraints);

        // Scrollpane which holds the body of the form.
        JScrollPane scrollPane = new JScrollPane(holdingPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Initialize top toolbar
        contentPane.add(new ToolBar("Tools",  dataHandler), BorderLayout.NORTH);
    }

    private class ToolBar extends JToolBar implements ActionListener {

        private DataHandler dataHandler;

        public ToolBar(String toolBarName, DataHandler dataHandler) {
            super(toolBarName);
            this.dataHandler = dataHandler;

            InitializeToolBar();
            AddButtons();
        }

        private void InitializeToolBar() {
            this.setOrientation(JToolBar.HORIZONTAL);
        }

        private void AddButtons() {
            Button saveButton = new Button("Save");
            saveButton.setPreferredSize(new Dimension(100, 25));
            saveButton.setActionCommand("Save");
            saveButton.addActionListener(this);
            this.add(saveButton);

            Button loadButton = new Button("Load");
            loadButton.setPreferredSize(new Dimension(100, 25));
            loadButton.setActionCommand("Load");
            loadButton.addActionListener(this);
            this.add(loadButton);

            Button importButton = new Button("Import");
            importButton.setPreferredSize(new Dimension(100, 25));
            importButton.setActionCommand("Import");
            importButton.addActionListener(this);
            this.add(importButton);
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("Save")) {
                // Save file chooser
                fileChooser.setSelectedFile(new File(character.getCharacterName().concat("Save.bin")));
                int returnVal = fileChooser.showSaveDialog(contentPane);

                // If selected we save the file
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File saveFile = fileChooser.getSelectedFile();
                    this.dataHandler.WriteData(character, saveFile);
                }
            } else if (ae.getActionCommand().equals("Load")) {
                // Open file chooser
                int returnVal = fileChooser.showOpenDialog(contentPane);

                // If selected we read the file
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    character = this.dataHandler.ReadData(file);
                    UpdateAllFields();
                }

            } else if (ae.getActionCommand().equals("Import")) {
                int returnVal = fileChooser.showOpenDialog(contentPane);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    this.dataHandler.ImportData(file);
                }
            }
        }
    }

    private class HeaderPanel extends JPanel implements DocumentListener {
        private JTextField characterNameTextField;
        private JTextField levelTextField;
        private CharacterDetailsPanel charDetailsPanel;

        public HeaderPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridBagLayout());
        }

        private void AddComponents() {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            JLabel characterNameLabel = new JLabel("Character Name");
            this.add(characterNameLabel, constraints);

            characterNameTextField = new JTextField(15);
            constraints.gridx = 1;
            this.add(characterNameTextField, constraints);

            Font charNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
            characterNameTextField.setFont(charNameFont);
            characterNameTextField.getDocument().putProperty("owner", characterNameTextField);
            characterNameTextField.getDocument().putProperty("charAttr", "CharName");
            characterNameTextField.getDocument().addDocumentListener(this);

            JLabel levelLabel = new JLabel("Level");
            constraints.gridx = 0; constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_END;
            this.add(levelLabel, constraints);

            levelTextField = new JTextField(3);
            constraints.gridx = 1; constraints.anchor = GridBagConstraints.LINE_START;
            this.add(levelTextField, constraints);

            AbstractDocument levelDoc = (AbstractDocument) levelTextField.getDocument();
            levelDoc.addDocumentListener(this);
            levelDoc.putProperty("owner", levelTextField);
            levelDoc.putProperty("charAttr", "Level");

            NumericalFilter numFilter = new NumericalFilter();
            numFilter.setMaxCharacters(3);
            levelDoc.setDocumentFilter(numFilter);

            charDetailsPanel = new CharacterDetailsPanel();
            constraints.gridx = 2; constraints.gridy = 0; constraints.gridheight = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            this.add(charDetailsPanel, constraints);
        }

        public void UpdateFields() {
            this.characterNameTextField.setText(character.getCharacterName());
            this.levelTextField.setText(Integer.toString(character.getLevel()));
            this.charDetailsPanel.UpdateFields();
        }

        private void SetCharacterProperty(AbstractDocument e) {
            JTextComponent owner = (JTextComponent) e.getProperty("owner");
            String charAttr = (String) e.getProperty("charAttr");

            switch (charAttr) {
                case "CharName":
                    character.setCharacterName(owner.getText());
                    break;
                case "Level":
                    if (owner.getText().length() > 0) {
                        character.setLevel(Integer.parseInt(owner.getText()));
                    } else {
                        character.setLevel(0);
                    }
                    break;
            }
        }
        @Override
        public void insertUpdate(DocumentEvent e) {
            SetCharacterProperty((AbstractDocument) e.getDocument());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            SetCharacterProperty((AbstractDocument) e.getDocument());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        private class CharacterDetailsPanel extends JPanel implements DocumentListener {
            // Goes within header - contains character information
            private JComboBox<Character.Classes> classDropDown;
            private JTextField backgroundTextField;
            private JTextField playerNameTextField;
            private JComboBox<Character.Races> raceDropDown;
            private JComboBox<Character.Alignment> alignmentDropDown;
            private JFormattedTextField expPointsTextField;

            public CharacterDetailsPanel() {
                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);

            }

            private void AddComponents() {
                GridBagConstraints constraints = new GridBagConstraints();
                JLabel classLabel = new JLabel("Class");
                this.add(classLabel, constraints);

                classDropDown = new JComboBox<>(Character.Classes.values());
                classDropDown.addActionListener((ActionEvent e) ->
                        character.setDndClass((Character.Classes) classDropDown.getSelectedItem()));

                this.add(classDropDown, constraints);

                JLabel backgroundLabel = new JLabel("Background");
                constraints.gridx = 2;
                this.add(backgroundLabel, constraints);

                backgroundTextField = new JTextField(10);

                // Fix drop down to samuip size as background.
                classDropDown.setPreferredSize(backgroundTextField.getPreferredSize());

                backgroundTextField.getDocument().addDocumentListener(this);
                backgroundTextField.getDocument().putProperty("owner", backgroundTextField);
                backgroundTextField.getDocument().putProperty("charValue", "Background");
                constraints.gridx = 3;
                this.add(backgroundTextField, constraints);

                JLabel playerNameLabel = new JLabel("Player Name");
                constraints.gridx = 4;
                this.add(playerNameLabel, constraints);

                playerNameTextField = new JTextField(10);
                playerNameTextField.getDocument().addDocumentListener(this);
                playerNameTextField.getDocument().putProperty("owner", playerNameTextField);
                playerNameTextField.getDocument().putProperty("charValue", "PlayerName");
                constraints.gridx = 5;
                this.add(playerNameTextField, constraints);

                JLabel raceLabel = new JLabel("Race");
                constraints.gridx = 0;
                constraints.gridy = 1;
                this.add(raceLabel, constraints);

                raceDropDown = new JComboBox<>(Character.Races.values());
                raceDropDown.addActionListener((ActionEvent e) ->
                        character.setRace((Character.Races) raceDropDown.getSelectedItem()));
                raceDropDown.setPreferredSize(backgroundTextField.getPreferredSize());
                constraints.gridx = 1;
                this.add(raceDropDown, constraints);

                JLabel alignmentLabel = new JLabel("Alignment");
                constraints.gridx = 2;
                this.add(alignmentLabel, constraints);

                alignmentDropDown = new JComboBox<>(Character.Alignment.values());
                alignmentDropDown.addActionListener((ActionEvent e) ->
                        character.setAlignment((Character.Alignment) alignmentDropDown.getSelectedItem()));
                alignmentDropDown.setPreferredSize(backgroundTextField.getPreferredSize());

                constraints.gridx = 3;
                this.add(alignmentDropDown, constraints);

                JLabel expPointsLabel = new JLabel("Experience Points");
                constraints.gridx = 4;
                this.add(expPointsLabel, constraints);

                // Create exp text field
                expPointsTextField = new JFormattedTextField();
                expPointsTextField.setPreferredSize(backgroundTextField.getPreferredSize());
                AbstractDocument expPointsDoc = ((AbstractDocument) expPointsTextField.getDocument());
                expPointsDoc.addDocumentListener(this);
                expPointsDoc.putProperty("owner", expPointsTextField);
                expPointsDoc.putProperty("charValue", "ExpPoints");

                NumericalFilter numFilter = new NumericalFilter();
                numFilter.setMinValue(0);
                expPointsDoc.setDocumentFilter(numFilter);

                constraints.gridx = 5;
                this.add(expPointsTextField, constraints);
            }

            public void UpdateFields() {
                this.classDropDown.setSelectedItem(character.getDndClass());
                this.backgroundTextField.setText(character.getBackground());
                this.playerNameTextField.setText(character.getPlayerName());
                this.raceDropDown.setSelectedItem(character.getRace());
                this.alignmentDropDown.setSelectedItem(character.getAlignment());
                this.expPointsTextField.setText(Integer.toString(character.getExperiencePoints()));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                SetCharacterValue(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SetCharacterValue(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private boolean SetCharacterValue(DocumentEvent e) {

                JTextField owner = (JTextField) e.getDocument().getProperty("owner");
                String charValue = (String) e.getDocument().getProperty("charValue");

                switch (charValue) {
                    case "Background":
                        character.setBackground(owner.getText());
                        break;
                    case "PlayerName":
                        character.setPlayerName(owner.getText());
                        break;
                    case "ExpPoints":
                        try {
                            character.setExperiencePoints(Integer.parseInt(owner.getText()));
                        } catch (NumberFormatException ex) {
                            Toolkit.getDefaultToolkit().beep();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }
    }

    private class CharacterValuesHolder extends JPanel implements DocumentListener {
        // Contains panels for attributes, skills, saving throws etc.
        private AttributePanel attrPanel;
        private SavingThrowsPanel savingThrowsPanel;
        private SkillsPanel skillsPanel;

        private InputBox inspirationInputBox;
        private InputBox proficiencyBonus;
        private InputBox passiveWisInputBox;

        private JTextArea profAndLangTextArea;

        private final Font defaultFieldFont = UIManager.getLookAndFeelDefaults().getFont("TextField.font");
        private final Font defaultLabelFont = UIManager.getLookAndFeelDefaults().getFont("Label.font");
        private final Font boldFieldFont = new Font(defaultFieldFont.getFontName(), Font.BOLD, defaultFieldFont.getSize());
        private final Font boldLabelFont = new Font(defaultLabelFont.getFontName(), Font.BOLD, defaultLabelFont.getSize());

        public CharacterValuesHolder() {

            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridBagLayout());
        }

        private void AddComponents() {
            GridBagConstraints constraints = new GridBagConstraints();
            attrPanel = new AttributePanel();

            constraints.gridx = 0;
            constraints.gridy = 0;
            this.add(attrPanel, constraints);

            JPanel rightHoldingPanel = new JPanel();
            rightHoldingPanel.setLayout(new GridBagLayout());

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            this.add(rightHoldingPanel, constraints);

            NumericalFilter numFilter = new NumericalFilter();
            numFilter.setMaxCharacters(2);

            NumericalFilter signedFilter = new NumericalFilter();
            signedFilter.setNeedsSign(true); signedFilter.setMaxCharacters(3);

            inspirationInputBox = new InputBox("Inspiration", numFilter);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_START;
            rightHoldingPanel.add(inspirationInputBox, constraints);

            proficiencyBonus = new InputBox("Proficiency Bonus", signedFilter);
            constraints.gridy += 1;
            rightHoldingPanel.add(proficiencyBonus, constraints);

            savingThrowsPanel = new SavingThrowsPanel();
            constraints.gridy += 1;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            rightHoldingPanel.add(savingThrowsPanel, constraints);

            skillsPanel = new SkillsPanel();
            constraints.gridy += 1;
            constraints.fill = GridBagConstraints.NONE;
            rightHoldingPanel.add(skillsPanel, constraints);

            JPanel bottomHoldingPanel = new JPanel();
            bottomHoldingPanel.setLayout(new BoxLayout(bottomHoldingPanel, BoxLayout.Y_AXIS));

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            this.add(bottomHoldingPanel, constraints);


            passiveWisInputBox = new InputBox("Passive Wisdom (Perception)", numFilter);
            bottomHoldingPanel.add(passiveWisInputBox);

            profAndLangTextArea = new JTextArea(4, 20);
            profAndLangTextArea.setLineWrap(true);
            profAndLangTextArea.setWrapStyleWord(true);
            profAndLangTextArea.setMinimumSize(new Dimension(profAndLangTextArea.getPreferredSize()));

            AbstractDocument langDoc = (AbstractDocument) profAndLangTextArea.getDocument();
            langDoc.addDocumentListener(this);
            langDoc.putProperty("owner", profAndLangTextArea);
            langDoc.putProperty("charAttr","ProfAndLang");

            JScrollPane profAndLangScrollPane = new JScrollPane(profAndLangTextArea);
            profAndLangScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            bottomHoldingPanel.add(profAndLangScrollPane);

            JLabel profAndLangLabel = new JLabel("Other Proficiencies & Languages");
            profAndLangLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            bottomHoldingPanel.add(profAndLangLabel);

        }

        public void UpdateFields() {
            attrPanel.UpdateFields();
            savingThrowsPanel.UpdateFields();
            skillsPanel.UpdateFields();

            inspirationInputBox.inputBox.setText(Integer.toString(character.getInspiration()));
            proficiencyBonus.inputBox.setText(Integer.toString(character.getProficiencyBonus()));
            passiveWisInputBox.inputBox.setText(Integer.toString(character.getPassiveWisdom()));

            profAndLangTextArea.setText(character.getProfAndLang());
        }

        private void SetCharacterPropertyByName(AbstractDocument e) {
            JTextComponent textComp = (JTextComponent) e.getProperty("owner");
            String charAttr = (String) e.getProperty("charAttr");

            String textValue = textComp.getText();
            int signedValue = 0;

            //TODO Quite hacky
            if (!charAttr.equals("ProfAndLang")) {
                signedValue = GetSignedIntValue(textValue);
            }
            switch (charAttr) {
                case "StrengthThrow":
                    character.setThrowBonusByIndex(0,signedValue);
                    break;
                case "DexterityThrow":
                    character.setThrowBonusByIndex(1,signedValue);
                    break;
                case "ConstitutionThrow":
                    character.setThrowBonusByIndex(2,signedValue);
                    break;
                case "IntelligenceThrow":
                    character.setThrowBonusByIndex(3,signedValue);
                    break;
                case "WisdomThrow":
                    character.setThrowBonusByIndex(4,signedValue);
                    break;
                case "CharismaThrow":
                    character.setThrowBonusByIndex(5,signedValue);
                    break;
                case "Inspiration":
                    if (textValue.length() > 0) {
                        character.setInspiration(Integer.parseInt(textValue));
                    } else {
                        character.setInspiration(0);
                    }
                    break;
                case "Proficiency Bonus":
                    character.setProficiencyBonus(signedValue);
                    break;
                case "Passive Wisdom (Perception)":
                    if (textValue.length() > 0) {
                        character.setPassiveWisdom(Integer.parseInt(textValue));
                    } else {
                        character.setPassiveWisdom(0);
                    }
                    break;
                case "ProfAndLang":
                    character.setProfAndLang(textValue);
                    break;
                default:
                    try {
                        int skillIndex = Character.Skills.valueByLabel(charAttr).ordinal();
                        character.setSkillBonusByIndex(skillIndex, signedValue);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            SetCharacterPropertyByName((AbstractDocument) e.getDocument());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            SetCharacterPropertyByName((AbstractDocument) e.getDocument());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            SetCharacterPropertyByName((AbstractDocument) e.getDocument());
        }

        private class InputBox extends JPanel {

            private JTextField inputBox;
            private String labelText;

            public InputBox(String labelText, NumericalFilter filter) {
                this.labelText = labelText;

                InitializePanel();
                AddComponents(filter);
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            }

            private void AddComponents(NumericalFilter filter) {
                inputBox = new JTextField();
                this.add(inputBox);
                inputBox.setColumns(3);
                inputBox.setMaximumSize(new Dimension(inputBox.getPreferredSize()));

                AbstractDocument doc = (AbstractDocument) inputBox.getDocument();
                doc.setDocumentFilter(filter);
                doc.addDocumentListener(CharacterValuesHolder.this);
                doc.putProperty("owner", inputBox);
                doc.putProperty("charAttr", labelText);

                JLabel boxLabel = new JLabel(labelText);
                this.add(boxLabel);
            }
        }

        private class AttributePanel extends JPanel implements DocumentListener {
            private AttributeBox strBox;
            private AttributeBox dexBox;
            private AttributeBox conBox;
            private AttributeBox intBox;
            private AttributeBox wisBox;
            private AttributeBox chaBox;

            public AttributePanel() {
                this.InitializePanel();
                this.AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                strBox = new AttributeBox("Strength", 1, 2);
                this.add(strBox);

                this.add(Box.createRigidArea(new Dimension(1, 40)));

                dexBox = new AttributeBox("Dexterity", 1, 2);
                this.add(dexBox);

                this.add(Box.createRigidArea(new Dimension(1, 40)));

                conBox = new AttributeBox("Constitution", 1, 2);
                this.add(conBox);

                this.add(Box.createRigidArea(new Dimension(1, 40)));

                intBox = new AttributeBox("Intelligence", 1, 2);
                this.add(intBox);

                this.add(Box.createRigidArea(new Dimension(1, 40)));

                wisBox = new AttributeBox("Wisdom", 1, 2);
                this.add(wisBox);

                this.add(Box.createRigidArea(new Dimension(1, 40)));

                chaBox = new AttributeBox("Charisma", 1, 2);
                this.add(chaBox);
            }

            private void SetAttributeByName(String attrName, int newValue) {
                switch(attrName) {
                    case "Strength":
                        character.setStrength(newValue);
                        break;
                    case "StrengthBonus":
                        character.setStrBonus(newValue);
                        break;
                    case "Dexterity":
                        character.setDexterity(newValue);
                        break;
                    case "DexterityBonus":
                        character.setDexBonus(newValue);
                        break;
                    case "Constitution":
                        character.setConstitution(newValue);
                        break;
                    case "ConstitutionBonus":
                        character.setConBonus(newValue);
                        break;
                    case "Intelligence":
                        character.setIntelligence(newValue);
                        break;
                    case "IntelligenceBonus":
                        character.setIntBonus(newValue);
                        break;
                    case "Wisdom":
                        character.setWisdom(newValue);
                        break;
                    case "WisdomBonus":
                        character.setWisBonus(newValue);
                        break;
                    case "Charisma":
                        character.setCharisma(newValue);
                        break;
                    case "CharismaBonus":
                        character.setChaBonus(newValue);
                    default:
                        break;
                }
            }

            public void UpdateFields() {
                strBox.attrField.setText(Integer.toString(character.getStrength()));
                strBox.attrArea.setText(Integer.toString(character.getStrBonus()));

                dexBox.attrField.setText(Integer.toString(character.getDexterity()));
                dexBox.attrArea.setText(Integer.toString(character.getDexBonus()));

                conBox.attrField.setText(Integer.toString(character.getConstitution()));
                conBox.attrArea.setText(Integer.toString(character.getConBonus()));

                intBox.attrField.setText(Integer.toString(character.getIntelligence()));
                intBox.attrArea.setText(Integer.toString(character.getIntBonus()));

                wisBox.attrField.setText(Integer.toString(character.getWisdom()));
                wisBox.attrArea.setText(Integer.toString(character.getWisBonus()));

                chaBox.attrField.setText(Integer.toString(character.getCharisma()));
                chaBox.attrArea.setText(Integer.toString(character.getChaBonus()));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {

                JTextComponent ownerField = (JTextComponent) e.getDocument().getProperty("owner");
                String attributeName = (String) e.getDocument().getProperty("attribute");

                // Bonus fields are signed
                if (attributeName.endsWith("Bonus"))
                {
                    SetAttributeByName(attributeName, GetSignedIntValue(ownerField.getText()));
                }
                else {
                    SetAttributeByName(attributeName, Integer.parseInt(ownerField.getText()));
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private class AttributeBox extends JPanel {
                private String attrString;

                private JTextArea attrArea;
                private JTextField attrField;

                public AttributeBox(String attrString, int rows, int cols) {
                    this.attrString = attrString;
                    this.InitializePanel();
                    this.AddComponents(rows, cols);
                }

                private void InitializePanel() {
                    this.setLayout(new GridBagLayout());
                }

                private void AddComponents(int rows, int cols) {
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.weightx = 1;
                    constraints.anchor = GridBagConstraints.CENTER;

                    JLabel attrLabel = new JLabel(this.attrString);
                    this.add(attrLabel, constraints);

                    attrArea = new JTextArea(rows, cols);
                    attrArea.setOpaque(false);
                    constraints.gridy = 1;

                    AbstractDocument areaDoc = (AbstractDocument) attrArea.getDocument();
                    NumericalFilter signedFilter = new NumericalFilter();
                    signedFilter.setNeedsSign(true); signedFilter.setMaxCharacters(3);
                    areaDoc.setDocumentFilter(signedFilter);

                    areaDoc.putProperty("owner", attrArea);
                    areaDoc.putProperty("attribute", this.attrString.concat("Bonus"));
                    areaDoc.addDocumentListener(AttributePanel.this);

                    Font attrFont = new Font(Font.SANS_SERIF, Font.BOLD, 38);
                    attrArea.setFont(attrFont);
                    this.add(attrArea, constraints);

                    attrField = new JTextField((3));
                    AbstractDocument fieldDoc = (AbstractDocument) attrField.getDocument();
                    NumericalFilter numFilter = new NumericalFilter();
                    numFilter.setMinValue(0);
                    fieldDoc.setDocumentFilter(numFilter);

                    fieldDoc.putProperty("owner", attrField);
                    fieldDoc.putProperty("attribute", this.attrString);
                    fieldDoc.addDocumentListener(AttributePanel.this);

                    constraints.gridy = 2;
                    this.add(attrField, constraints);
                }
            }
        }

        private class SavingThrowsPanel extends JPanel {
            private CheckBoxPanel strCheckBox;
            private CheckBoxPanel dexCheckBox;
            private CheckBoxPanel conCheckBox;
            private CheckBoxPanel intCheckBox;
            private CheckBoxPanel wisCheckBox;
            private CheckBoxPanel chaCheckBox;

            public SavingThrowsPanel() {
                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                GridBagConstraints constraints = new GridBagConstraints();
                strCheckBox = new CheckBoxPanel("Strength", "Throw");
                dexCheckBox = new CheckBoxPanel("Dexterity", "Throw");
                conCheckBox = new CheckBoxPanel("Constitution", "Throw");
                intCheckBox = new CheckBoxPanel("Intelligence", "Throw");
                wisCheckBox = new CheckBoxPanel("Wisdom", "Throw");
                chaCheckBox = new CheckBoxPanel("Charisma", "Throw");

                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.anchor = GridBagConstraints.LINE_START;
                constraints.weightx = 1;
                this.add(strCheckBox, constraints);
                constraints.gridy += 1;
                this.add(dexCheckBox, constraints);
                constraints.gridy += 1;
                this.add(conCheckBox, constraints);
                constraints.gridy += 1;
                this.add(intCheckBox, constraints);
                constraints.gridy += 1;
                this.add(wisCheckBox, constraints);
                constraints.gridy += 1;
                this.add(chaCheckBox, constraints);
                constraints.gridy += 1;

                JLabel savingThrowsLabel = new JLabel("Saving Throws");
                savingThrowsLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.gridy += 1;
                constraints.anchor = GridBagConstraints.CENTER;
                this.add(savingThrowsLabel, constraints);
            }

            public void UpdateFields() {
                int[] throwBonus = character.getThrowBonus();
                strCheckBox.textField.setText(Integer.toString(throwBonus[0]));
                dexCheckBox.textField.setText(Integer.toString(throwBonus[1]));
                conCheckBox.textField.setText(Integer.toString(throwBonus[2]));
                intCheckBox.textField.setText(Integer.toString(throwBonus[3]));
                wisCheckBox.textField.setText(Integer.toString(throwBonus[4]));
                chaCheckBox.textField.setText(Integer.toString(throwBonus[5]));

                boolean[] proficiencies = character.getThrowProficiencies();
                strCheckBox.checkBox.setSelected(proficiencies[0]);
                dexCheckBox.checkBox.setSelected(proficiencies[1]);
                conCheckBox.checkBox.setSelected(proficiencies[2]);
                intCheckBox.checkBox.setSelected(proficiencies[3]);
                wisCheckBox.checkBox.setSelected(proficiencies[4]);
                chaCheckBox.checkBox.setSelected(proficiencies[5]);

            }
        }

        private class SkillsPanel extends JPanel {

            private Map<Character.Skills, CheckBoxPanel> skillPanelMap;

            public SkillsPanel() {
                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                skillPanelMap = new HashMap<>();
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.weightx = 1;
                constraints.anchor = GridBagConstraints.LINE_START;

                for (Character.Skills skill : Character.Skills.values()) {
                    CheckBoxPanel skillPanel = new CheckBoxPanel(skill.toString(),"");
                    skillPanelMap.put(skill, skillPanel);
                    constraints.gridy += 1;
                    this.add(skillPanel, constraints);
                    skillPanel.setAlignmentX(LEFT_ALIGNMENT);
                }

                JLabel skillsLabel = new JLabel("Skills");
                skillsLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.gridy += 1;
                this.add(skillsLabel, constraints);

            }

            public void UpdateFields() {
                boolean[] proficiencies = character.getSkillProficiencies();
                int[] values = character.getSkillBonus();
                for (Character.Skills skill : skillPanelMap.keySet()) {
                    int index = skill.ordinal();
                    skillPanelMap.get(skill).checkBox.setSelected(proficiencies[index]);
                    skillPanelMap.get(skill).textField.setText(Integer.toString(values[index]));
                }
            }
        }

        private class CheckBoxPanel extends JPanel implements ItemListener {
            private JCheckBox checkBox;
            private JTextField textField;
            private JLabel label;
            private String identifier;

            public CheckBoxPanel(String labelText, String suffix) {
                this.InitializePanel();
                this.AddComponents(labelText, suffix);
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            }

            private void AddComponents(String labelText, String suffix) {
                checkBox = new JCheckBox();
                this.add(checkBox);
                checkBox.setSelected(false);
                checkBox.addItemListener(this);

                textField = new JTextField();
                this.add(textField);
                textField.setColumns(3);

                AbstractDocument fieldDoc = (AbstractDocument) textField.getDocument();
                NumericalFilter signedFilter = new NumericalFilter();
                signedFilter.setNeedsSign(true); signedFilter.setMaxCharacters(3);
                fieldDoc.setDocumentFilter(signedFilter);

                this.identifier = labelText.concat(suffix);
                fieldDoc.putProperty("charAttr", this.identifier);
                fieldDoc.putProperty("owner",textField);

                // Has to work with skills and saving throw fields
                fieldDoc.addDocumentListener(CharacterValuesHolder.this);

                label = new JLabel(labelText);
                this.add(label);
            }

            private void SetProfByName(String name, boolean isProficient) {
                switch (name) {
                    case "StrengthThrow":
                        character.setThrowProficienciesByIndex(0, isProficient);
                        break;
                    case "DexterityThrow":
                        character.setThrowProficienciesByIndex(1, isProficient);
                        break;
                    case "ConstitutionThrow":
                        character.setThrowProficienciesByIndex(2, isProficient);
                        break;
                    case "IntelligenceThrow":
                        character.setThrowProficienciesByIndex(3, isProficient);
                        break;
                    case "WisdomThrow":
                        character.setThrowProficienciesByIndex(4, isProficient);
                        break;
                    case "CharismaThrow":
                        character.setThrowProficienciesByIndex(5, isProficient);
                        break;
                    default:
                        try {
                            int skillIndex = Character.Skills.valueByLabel(name).ordinal();
                            character.setSkillProficienciesByIndex(skillIndex, isProficient);
                        } catch(NullPointerException ne) {
                            ne.printStackTrace();
                        }
                        break;
                }
            }
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    textField.setFont(boldFieldFont);
                    label.setFont(boldLabelFont);
                    SetProfByName(this.identifier, true);
                } else {
                    textField.setFont(defaultFieldFont);
                    label.setFont(defaultLabelFont);
                    SetProfByName(this.identifier, false);
                }
            }
        }
    }

    private class CombatPanel extends JPanel implements DocumentListener {
        private InCombatPanel inCombatPanel;
        private AtkSpellsPanel atkSpellsPanel;
        private EquipmentPanel equipmentPanel;

        public CombatPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        private void AddComponents() {
            inCombatPanel = new InCombatPanel();
            this.add(inCombatPanel);

            atkSpellsPanel = new AtkSpellsPanel();
            this.add(atkSpellsPanel);

            equipmentPanel = new EquipmentPanel();
            this.add(equipmentPanel);
        }

        public void UpdateFields() {
            equipmentPanel.UpdateFields();
            inCombatPanel.UpdateFields();
            atkSpellsPanel.UpdateFields();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            SetCharacterProperty((AbstractDocument) e.getDocument());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            SetCharacterProperty((AbstractDocument) e.getDocument());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        private void SetCharacterProperty(AbstractDocument doc) {
            JTextComponent textComponent = (JTextComponent) doc.getProperty("owner");
            String property = (String) doc.getProperty("charProperty");

            String textValue = textComponent.getText();
            int rowNum;
            switch (property) {
                case "Armor Class":
                    if (textValue.length() > 0)
                        character.setArmorClass(Integer.parseInt(textValue));
                    else
                        character.setArmorClass(0);
                    break;
                case "Initiative":
                    if (textValue.length() > 0)
                        character.setInitiative(GetSignedIntValue(textValue));
                    else
                        character.setInitiative(0);
                    break;
                case "Speed":
                    if (textValue.length() > 0)
                        character.setSpeed(Integer.parseInt(textValue));
                    else
                        character.setSpeed(0);
                    break;
                case "HPMax":
                    if (textValue.length() > 0)
                        character.setMaxHitPoints(Integer.parseInt(textValue));
                    else
                        character.setCurrentHitPoints(0);
                    break;
                case "Current Hit Points":
                    if (textValue.length() > 0)
                        character.setCurrentHitPoints(Integer.parseInt(textValue));
                    else
                        character.setCurrentHitPoints(0);
                    break;
                case "Temporary Hit Points":
                    if (textValue.length() > 0)
                        character.setTemporaryHitPoints(Integer.parseInt(textValue));
                    else
                        character.setTemporaryHitPoints(0);
                    break;
                case "Hit Dice":
                    character.setHitDiceCurrent(textValue);
                    break;
                case "HitDiceTotal":
                    character.setHitDiceTotal(textValue);
                    break;
                case "AdditionalSpells":
                    character.setAdditionalSpells(textValue);
                    break;
                case "AttackName":
                    rowNum = (int) doc.getProperty("rowNum");
                    character.getAttacks()[rowNum].setAttackName(textValue);
                    break;
                case "AttackBonus":
                    rowNum = (int) doc.getProperty("rowNum");
                    character.getAttacks()[rowNum].setAtkBonus(GetSignedIntValue(textValue));
                    break;
                case "DamageType":
                    rowNum = (int) doc.getProperty("rowNum");
                    character.getAttacks()[rowNum].setDamageType(textValue);
                    break;

            }
        }

        private class InCombatPanel extends JPanel {
            private TextBoxOverLabel armorClassPanel;
            private TextBoxOverLabel initiativePanel;
            private TextBoxOverLabel speedPanel;
            private JTextField hpMaxTextField;
            private TextBoxOverLabel hpCurrentPanel;
            private TextBoxOverLabel hpTempPanel;
            private JTextField hitDiceTotalTextField;
            private TextBoxOverLabel hitDicePanel;
            private DeathSaveCheckBoxesPanel deathSavesPanel;

            public InCombatPanel() {
                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.ipadx = 1;
                constraints.ipady = 1;

                NumericalFilter numFilter = new NumericalFilter();
                numFilter.setMaxCharacters(3);

                NumericalFilter signedFilter = new NumericalFilter();
                signedFilter.setNeedsSign(true);
                signedFilter.setMaxCharacters(3);

                // Armor, Initiative, Speed
                armorClassPanel = new TextBoxOverLabel("Armor Class", 3, numFilter);
                constraints.gridwidth = 2;
                constraints.weightx = 1;
                constraints.weighty = 1;
                this.add(armorClassPanel, constraints);

                initiativePanel = new TextBoxOverLabel("Initiative", 3, signedFilter);
                constraints.gridx = 2;
                this.add(initiativePanel, constraints);

                speedPanel = new TextBoxOverLabel("Speed", 3, numFilter);
                constraints.gridx = 4;
                this.add(speedPanel, constraints);

                // Current HP
                JPanel currentHPHolder = new JPanel();
                currentHPHolder.setLayout(new GridBagLayout());

                JLabel hpMaxLabel = new JLabel("Hit Point Maximum");
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.gridwidth = 1;
                currentHPHolder.add(hpMaxLabel, constraints);

                hpMaxTextField = new JTextField();
                hpMaxTextField.setColumns(3);

                AbstractDocument hpMaxDoc = (AbstractDocument) hpMaxTextField.getDocument();
                hpMaxDoc.setDocumentFilter(numFilter);
                hpMaxDoc.addDocumentListener(CombatPanel.this);
                hpMaxDoc.putProperty("owner", hpMaxTextField);
                hpMaxDoc.putProperty("charProperty", "HPMax");

                constraints.gridx = 1;
                constraints.weightx = 1;
                currentHPHolder.add(hpMaxTextField, constraints);

                hpCurrentPanel = new TextBoxOverLabel("Current Hit Points", 7, numFilter);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                currentHPHolder.add(hpCurrentPanel, constraints);

                constraints.gridwidth = 6;
                this.add(currentHPHolder, constraints);

                // Temp HP
                hpTempPanel = new TextBoxOverLabel("Temporary Hit Points", 7, numFilter);
                constraints.gridx = 0;
                constraints.gridy = 2;
                this.add(hpTempPanel, constraints);

                // Hit Dice, Death Saves
                JPanel hitDiceHolder = new JPanel();
                hitDiceHolder.setLayout(new GridBagLayout());

                JLabel totalLabel = new JLabel("Total");
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.gridwidth = 1;
                hitDiceHolder.add(totalLabel, constraints);

                hitDiceTotalTextField = new JTextField();
                hitDiceTotalTextField.setColumns(3);

                AbstractDocument hitDiceTotalDoc = (AbstractDocument) hitDiceTotalTextField.getDocument();
                hitDiceTotalDoc.putProperty("owner", hitDiceTotalTextField);
                hitDiceTotalDoc.putProperty("charProperty", "HitDiceTotal");
                hitDiceTotalDoc.addDocumentListener(CombatPanel.this);

                constraints.gridx = 1;
                constraints.weightx = 1;
                hitDiceHolder.add(hitDiceTotalTextField, constraints);

                hitDicePanel = new TextBoxOverLabel("Hit Dice", 7, null);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                hitDiceHolder.add(hitDicePanel, constraints);

                constraints.gridx = 0;
                constraints.gridy = 3;
                constraints.gridwidth = 3;
                this.add(hitDiceHolder, constraints);

                deathSavesPanel = new DeathSaveCheckBoxesPanel();

                constraints.gridx = 3;
                constraints.gridy = 3;
                constraints.gridwidth = 3;
                this.add(deathSavesPanel, constraints);
            }

            public void UpdateFields() {
                armorClassPanel.textField.setText(Integer.toString(character.getArmorClass()));
                initiativePanel.textField.setText(Integer.toString(character.getInitiative()));
                speedPanel.textField.setText(Integer.toString(character.getSpeed()));
                hpCurrentPanel.textField.setText(Integer.toString(character.getCurrentHitPoints()));
                hpTempPanel.textField.setText(Integer.toString(character.getTemporaryHitPoints()));

                hpMaxTextField.setText(Integer.toString(character.getMaxHitPoints()));

                hitDiceTotalTextField.setText(character.getHitDiceTotal());
                hitDicePanel.textField.setText(character.getHitDiceCurrent());

                deathSavesPanel.UpdateFields();
            }

            private class TextBoxOverLabel extends JPanel {
                private JTextField textField;

                public TextBoxOverLabel(String labelText, int cols, NumericalFilter numFilter) {
                    this.InitializePanel();
                    this.AddComponents(labelText, cols, numFilter);
                }

                private void InitializePanel() {

                    this.setLayout(new GridBagLayout());
                }

                private void AddComponents(String labelText, int cols, NumericalFilter numFilter) {
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.anchor = GridBagConstraints.CENTER;
                    this.textField = new JTextField();
                    this.textField.setColumns(cols);

                    AbstractDocument fieldDoc = (AbstractDocument) this.textField.getDocument();
                    fieldDoc.addDocumentListener(CombatPanel.this);
                    if (numFilter != null) {
                        fieldDoc.setDocumentFilter(numFilter);
                    }
                    fieldDoc.putProperty("owner", this.textField);
                    fieldDoc.putProperty("charProperty", labelText);

                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.gridwidth = 1;
                    constraints.weightx = 1;
                    this.add(textField, constraints);

                    JLabel label = new JLabel(labelText, JLabel.CENTER);
                    label.setFont(CharacterSheetGUI.SectionTitleFont);
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.gridwidth = 3;
                    this.add(label, constraints);
                }
            }

            private class DeathSaveCheckBoxesPanel extends JPanel implements ItemListener {
                //TODO link up to character class
                private JCheckBox[][] deathSaveCheckBoxes;

                public DeathSaveCheckBoxesPanel() {
                    InitializePanel();
                    AddComponents();
                }

                private void InitializePanel() {
                    this.setLayout(new GridBagLayout());
                }

                private void AddComponents() {
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.gridx = 0; constraints.gridy = 0;
                    constraints.weightx = 1.0; constraints.anchor = GridBagConstraints.LINE_END;
                    deathSaveCheckBoxes = new JCheckBox[2][3];

                    for (int row = 0; row < 2; row ++) {
                        for (int col = 0; col < 3; col++) {
                            deathSaveCheckBoxes[row][col] = new JCheckBox();
                            deathSaveCheckBoxes[row][col].addItemListener(this);
                        }
                    }

                    JLabel successLabel = new JLabel("Successes");
                    this.add(successLabel, constraints); constraints.gridx += 1;

                    this.add(deathSaveCheckBoxes[0][0], constraints); constraints.gridx += 1;
                    this.add(deathSaveCheckBoxes[0][1], constraints); constraints.gridx += 1;
                    this.add(deathSaveCheckBoxes[0][2], constraints);

                    JLabel failLabel = new JLabel("Failures");
                    constraints.gridx = 0; constraints.gridy = 1;
                    this.add(failLabel, constraints); constraints.gridx += 1;

                    this.add(deathSaveCheckBoxes[1][0], constraints); constraints.gridx += 1;
                    this.add(deathSaveCheckBoxes[1][1], constraints); constraints.gridx += 1;
                    this.add(deathSaveCheckBoxes[1][2], constraints);

                    JLabel deathSavesLabel = new JLabel("Death Saves");
                    deathSavesLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                    constraints.gridy = 2; constraints.gridx = 0; constraints.gridwidth = 4;
                    constraints.anchor = GridBagConstraints.CENTER;
                    this.add(deathSavesLabel, constraints);
                }

                public void UpdateFields() {
                    boolean[][] charDeathSaves = character.getDeathSaves();

                    for (int row = 0; row < 2; row ++) {
                        for (int col = 0; col < 3; col ++) {
                            deathSaveCheckBoxes[row][col].setSelected(charDeathSaves[row][col]);
                        }
                    }
                }

                @Override
                public void itemStateChanged(ItemEvent e) {
                    boolean[][] deathSaves = new boolean[2][3];
                    for (int row = 0; row < 2; row ++) {
                        for (int col = 0; col < 3; col++) {
                            deathSaves[row][col] = deathSaveCheckBoxes[row][col].isSelected();
                        }
                    }

                    character.setDeathSaves(deathSaves);
                }
            }
        }

        private class AtkSpellsPanel extends JPanel {
            private InputFields firstAttack;
            private InputFields secondAttack;
            private InputFields thirdAttack;

            private JTextArea additionalSpellsArea;

            public AtkSpellsPanel() {
                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.weightx = 1;
                constraints.anchor = GridBagConstraints.LINE_START;
                constraints.ipadx = 1;
                constraints.gridx = 0;
                constraints.gridy = 0;

                firstAttack = new InputFields(0);
                secondAttack = new InputFields(1);
                thirdAttack = new InputFields(2);

                constraints.gridwidth = 3;
                this.add(firstAttack, constraints);
                constraints.gridy += 1;
                this.add(secondAttack, constraints);
                constraints.gridy += 1;
                this.add(thirdAttack, constraints);
                constraints.gridy += 1;

                additionalSpellsArea = new JTextArea(8, 38);
                additionalSpellsArea.setLineWrap(true);
                additionalSpellsArea.setWrapStyleWord(true);

                AbstractDocument spellDoc = (AbstractDocument) additionalSpellsArea.getDocument();
                spellDoc.addDocumentListener(CombatPanel.this);
                spellDoc.putProperty("owner", additionalSpellsArea);
                spellDoc.putProperty("charProperty", "AdditionalSpells");

                JScrollPane spellsScrollPane = new JScrollPane(additionalSpellsArea);
                spellsScrollPane.setHorizontalScrollBarPolicy(
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                constraints.anchor = GridBagConstraints.CENTER;
                this.add(spellsScrollPane, constraints);
                constraints.gridy += 1;

                JLabel titleLabel = new JLabel("Attacks & Spellcasting");
                titleLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.anchor = GridBagConstraints.PAGE_END;
                this.add(titleLabel, constraints);

            }

            public void UpdateFields() {
                firstAttack.nameField.setText(character.getAttacks()[0].getAttackName());
                secondAttack.nameField.setText(character.getAttacks()[1].getAttackName());
                thirdAttack.nameField.setText(character.getAttacks()[2].getAttackName());

                firstAttack.attackBonusField.setText(Integer.toString(character.getAttacks()[0].getAtkBonus()));
                secondAttack.attackBonusField.setText(Integer.toString(character.getAttacks()[1].getAtkBonus()));
                thirdAttack.attackBonusField.setText(Integer.toString(character.getAttacks()[2].getAtkBonus()));

                firstAttack.damageField.setText(character.getAttacks()[0].getDamageType());
                secondAttack.damageField.setText(character.getAttacks()[1].getDamageType());
                thirdAttack.damageField.setText(character.getAttacks()[2].getDamageType());

                additionalSpellsArea.setText(character.getAdditionalSpells());
            }

            private class InputFields extends JPanel {
                private JTextField nameField;
                private JTextField attackBonusField;
                private JTextField damageField;

                public InputFields(int rowNumber) {
                    InitializePanel();
                    AddComponents(rowNumber);
                }

                private void InitializePanel() {
                    this.setLayout(new GridBagLayout());
                }

                private void AddComponents(int rowNumber) {
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.anchor = GridBagConstraints.LINE_START;

                    if (rowNumber == 0) {
                        JLabel nameLabel = new JLabel("Name");
                        JLabel dmgBonusLabel = new JLabel("Atk Bonus");
                        JLabel damageTypeLabel = new JLabel("Damage/Type");

                        this.add(nameLabel, constraints);
                        constraints.gridx += 1;
                        this.add(dmgBonusLabel, constraints);
                        constraints.gridx += 1;
                        this.add(damageTypeLabel, constraints);

                        constraints.gridy += 1;
                        constraints.gridx = 0;
                    }

                    nameField = new JTextField(24);
                    AbstractDocument nameDoc = (AbstractDocument) nameField.getDocument();
                    nameDoc.addDocumentListener(CombatPanel.this);
                    nameDoc.putProperty("owner", nameField);
                    nameDoc.putProperty("charProperty", "AttackName");
                    nameDoc.putProperty("rowNum", rowNumber);

                    this.add(nameField, constraints);
                    constraints.gridx += 1;

                    attackBonusField = new JTextField(6);
                    AbstractDocument atkBonusDoc = (AbstractDocument) attackBonusField.getDocument();
                    atkBonusDoc.addDocumentListener(CombatPanel.this);
                    atkBonusDoc.putProperty("owner", attackBonusField);
                    atkBonusDoc.putProperty("charProperty", "AttackBonus");
                    atkBonusDoc.putProperty("rowNum", rowNumber);

                    NumericalFilter signedFilter = new NumericalFilter();
                    signedFilter.setMaxCharacters(3);
                    signedFilter.setNeedsSign(true);
                    atkBonusDoc.setDocumentFilter(signedFilter);

                    this.add(attackBonusField, constraints);
                    constraints.gridx += 1;

                    damageField = new JTextField(8);
                    AbstractDocument dmgDoc = (AbstractDocument) damageField.getDocument();
                    dmgDoc.addDocumentListener(CombatPanel.this);
                    dmgDoc.putProperty("owner", damageField);
                    dmgDoc.putProperty("charProperty", "DamageType");
                    dmgDoc.putProperty("rowNum", rowNumber);

                    this.add(damageField, constraints);

                }
            }
        }

        private class EquipmentPanel extends JPanel implements DocumentListener {

            private JTextField copperPiecesField;
            private JTextField silverPiecesField;
            private JTextField electrumPiecesField;
            private JTextField goldPiecesField;
            private JTextField platinumPiecesField;

            private JTextArea equipmentArea;

            public EquipmentPanel() {
                this.InitializePanel();
                this.AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
                this.setBorder(CharacterSheetGUI.SectionBorder);
            }

            private void AddComponents() {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.anchor = GridBagConstraints.LINE_START;

                NumericalFilter numFilter = new NumericalFilter();
                numFilter.setMaxCharacters(2);
                numFilter.setMinValue(0);

                JLabel cpLabel = new JLabel("CP");
                this.add(cpLabel, constraints);
                copperPiecesField = new JTextField();
                copperPiecesField.setColumns(3);

                AbstractDocument copperDoc = (AbstractDocument) copperPiecesField.getDocument();
                copperDoc.addDocumentListener(this);
                copperDoc.putProperty("owner", copperPiecesField);
                copperDoc.putProperty("equipment", "Copper");
                copperDoc.setDocumentFilter(numFilter);

                constraints.gridx = 1;
                this.add(copperPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel spLabel = new JLabel("SP");
                this.add(spLabel, constraints);
                silverPiecesField = new JTextField();
                silverPiecesField.setColumns(3);

                AbstractDocument silverDoc = (AbstractDocument) silverPiecesField.getDocument();
                silverDoc.addDocumentListener(this);
                silverDoc.putProperty("owner", silverPiecesField);
                silverDoc.putProperty("equipment", "Silver");
                silverDoc.setDocumentFilter(numFilter);

                constraints.gridx = 1;
                this.add(silverPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel epLabel = new JLabel("EP");
                this.add(epLabel, constraints);
                electrumPiecesField = new JTextField();
                electrumPiecesField.setColumns(3);

                AbstractDocument electrumDoc = (AbstractDocument) electrumPiecesField.getDocument();
                electrumDoc.addDocumentListener(this);
                electrumDoc.putProperty("owner", electrumPiecesField);
                electrumDoc.putProperty("equipment", "Electrum");
                electrumDoc.setDocumentFilter(numFilter);

                constraints.gridx = 1;
                this.add(electrumPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel gpLabel = new JLabel("GP");
                this.add(gpLabel, constraints);
                goldPiecesField = new JTextField();
                goldPiecesField.setColumns(3);

                AbstractDocument goldDoc = (AbstractDocument) goldPiecesField.getDocument();
                goldDoc.addDocumentListener(this);
                goldDoc.putProperty("owner", goldPiecesField);
                goldDoc.putProperty("equipment", "Gold");
                goldDoc.setDocumentFilter(numFilter);

                constraints.gridx = 1;
                this.add(goldPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel ppLabel = new JLabel("PP");
                this.add(ppLabel, constraints);

                platinumPiecesField = new JTextField();
                platinumPiecesField.setColumns(3);

                AbstractDocument platinumDoc = (AbstractDocument) platinumPiecesField.getDocument();
                platinumDoc.addDocumentListener(this);
                platinumDoc.putProperty("owner", platinumPiecesField);
                platinumDoc.putProperty("equipment", "Platinum");
                NumericalFilter longNumfilter = new NumericalFilter();
                platinumDoc.setDocumentFilter(longNumfilter);

                constraints.gridx = 1;
                this.add(platinumPiecesField, constraints);

                equipmentArea = new JTextArea(6, 30);
                equipmentArea.setLineWrap(true);
                equipmentArea.setWrapStyleWord(true);
                equipmentArea.setMinimumSize(new Dimension(equipmentArea.getPreferredSize()));

                AbstractDocument equipmentDoc = (AbstractDocument) equipmentArea.getDocument();
                equipmentDoc.addDocumentListener(this);
                equipmentDoc.putProperty("owner", equipmentArea);
                equipmentDoc.putProperty("equipment", "Equipment");

                JScrollPane equipmentScrollPane = new JScrollPane(equipmentArea);
                equipmentScrollPane.setHorizontalScrollBarPolicy(
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                constraints.gridx = 2;
                constraints.gridy = 0;
                constraints.gridheight = 5;
                this.add(equipmentScrollPane, constraints);

                JLabel equipmentLabel = new JLabel("Equipment");
                equipmentLabel.setFont(CharacterSheetGUI.SectionTitleFont);

                constraints.gridx = 0;
                constraints.gridy = 7;
                constraints.gridwidth = 3;
                constraints.anchor = GridBagConstraints.CENTER;
                this.add(equipmentLabel, constraints);
            }

            public void UpdateFields() {
                this.copperPiecesField.setText(Integer.toString(character.getCopper()));
                this.silverPiecesField.setText(Integer.toString(character.getSilver()));
                this.electrumPiecesField.setText(Integer.toString(character.getElectrum()));
                this.goldPiecesField.setText(Integer.toString(character.getGold()));
                this.platinumPiecesField.setText(Integer.toString(character.getPlatinum()));

                this.equipmentArea.setText(character.getEquipment());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                UpdateCharEquipment((AbstractDocument) e.getDocument());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                UpdateCharEquipment((AbstractDocument) e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void UpdateCharEquipment(AbstractDocument doc) {
                JTextComponent textComponent = (JTextComponent) doc.getProperty("owner");
                String equipment = (String) doc.getProperty("equipment");

                String fieldValue = textComponent.getText();

                switch (equipment) {
                    case "Copper":
                        if (fieldValue.length() > 0)
                            character.setCopper(Integer.parseInt(fieldValue));
                        else
                            character.setCopper(0);
                        break;
                    case "Silver":
                        if (fieldValue.length() > 0)
                            character.setSilver(Integer.parseInt(fieldValue));
                        else
                            character.setSilver(0);
                        break;
                    case "Electrum":
                        if (fieldValue.length() > 0)
                            character.setElectrum(Integer.parseInt(fieldValue));
                        else
                            character.setElectrum(0);
                        break;
                    case "Gold":
                        if (fieldValue.length() > 0)
                            character.setGold(Integer.parseInt(fieldValue));
                        else
                            character.setGold(0);
                        break;
                    case "Platinum":
                        if (fieldValue.length() > 0)
                            character.setPlatinum(Integer.parseInt(fieldValue));
                        else
                            character.setPlatinum(0);
                        break;
                    case "Equipment":
                        character.setEquipment(fieldValue);
                        break;
                }
            }
        }
    }

    private class TraitsPanel extends JPanel {

        private TitleUnderTextArea personalityTraitsArea;
        private TitleUnderTextArea idealsArea;
        private TitleUnderTextArea bondsArea;
        private TitleUnderTextArea flawsArea;
        private TitleUnderTextArea featAndTraitsArea;

        public TraitsPanel() {
            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(CharacterSheetGUI.SectionBorder);
        }

        private void AddComponents() {
            personalityTraitsArea = new TitleUnderTextArea("Personality Traits", 6, 25);
            idealsArea = new TitleUnderTextArea("Ideals", 5, 25);
            bondsArea = new TitleUnderTextArea("Bonds", 5, 25);
            flawsArea = new TitleUnderTextArea("Flaws", 5, 25);
            featAndTraitsArea = new TitleUnderTextArea("Features & Traits", 10, 25);

            this.add(personalityTraitsArea);
            this.add(idealsArea);
            this.add(bondsArea);
            this.add(flawsArea);
            this.add(featAndTraitsArea);
        }

        public void UpdateFields() {
            this.personalityTraitsArea.setTextBody(character.getPersonalityTraits());
            this.idealsArea.setTextBody(character.getIdeals());
            this.bondsArea.setTextBody(character.getBonds());
            this.flawsArea.setTextBody(character.getFlaws());
            this.featAndTraitsArea.setTextBody(character.getFeatureTraits());
        }

        private class TitleUnderTextArea extends JPanel implements DocumentListener {

            public void setTextBody(String body) {
                this.textArea.setText(body);
            }

            private JTextArea textArea;

            public TitleUnderTextArea(String labelText, int rows, int cols) {
                this.InitializePanel();
                this.AddComponents(labelText, rows, cols);
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            }

            private void AddComponents(String labelText, int rows, int cols) {
                textArea = new JTextArea(rows, cols);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setMinimumSize(new Dimension(textArea.getPreferredSize()));
                textArea.getDocument().addDocumentListener(this);
                textArea.getDocument().putProperty("owner", textArea);
                textArea.getDocument().putProperty("trait", labelText);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setHorizontalScrollBarPolicy(
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                this.add(scrollPane);

                JLabel label = new JLabel(labelText);
                label.setFont(CharacterSheetGUI.SectionTitleFont);
                this.add(label);
                label.setAlignmentX(CENTER_ALIGNMENT);

                this.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                SetCharTrait(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SetCharTrait(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SetCharTrait(e);
            }

            private void SetCharTrait(DocumentEvent e) {
                JTextArea textArea = (JTextArea) e.getDocument().getProperty("owner");
                String traits = (String) e.getDocument().getProperty("trait");
                try {
                    switch (traits) {
                        case "Personality Traits":
                            character.setPersonalityTraits(textArea.getText());
                            break;
                        case "Ideals":
                            character.setIdeals(textArea.getText());
                            break;
                        case "Bonds":
                            character.setBonds(textArea.getText());
                            break;
                        case "Flaws":
                            character.setFlaws(textArea.getText());
                            break;
                        case "Features & Traits":
                            character.setFeatureTraits(textArea.getText());
                            break;
                        default:
                            throw new Exception("Label not set correctly");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

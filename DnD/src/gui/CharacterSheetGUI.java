package gui;

import engine.Character;
import engine.DataHandler;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.NumberFormatter;

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


        if (this.dataHandler.CheckSavedData()) {
            this.character = this.dataHandler.ReadData();
        } else {
            this.character = new Character();
        }
    }

    private void UpdateAllFields() {
        headerPanel.UpdateFields();
        characterValuesHolder.UpdateFields();
        traitsPanel.UpdateFields();
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
        headerPanel = new HeaderPanel(this);
        holdingPanel.add(headerPanel, constraints);

        constraints.weighty = 4;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        characterValuesHolder = new CharacterValuesHolder(this);
        holdingPanel.add(characterValuesHolder, constraints);

        constraints.gridx = 1;
        combatPanel = new CombatPanel();
        holdingPanel.add(combatPanel, constraints);

        constraints.gridx = 2;
        traitsPanel = new TraitsPanel(this);
        holdingPanel.add(traitsPanel, constraints);

        // Scrollpane which holds the body of the form.
        JScrollPane scrollPane = new JScrollPane(holdingPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Initialize top toolbar
        contentPane.add(new ToolBar("Tools", this, dataHandler), BorderLayout.NORTH);
    }

    private class ToolBar extends JToolBar implements ActionListener {

        private CharacterSheetGUI charGUI;
        private DataHandler dataHandler;

        public ToolBar(String toolBarName, CharacterSheetGUI charGUI, DataHandler dataHandler) {
            super(toolBarName);
            this.charGUI = charGUI;
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
                this.dataHandler.WriteData(character);
            } else if (ae.getActionCommand().equals("Load")) {
                this.charGUI.character = this.dataHandler.ReadData();
                this.charGUI.UpdateAllFields();
            } else if (ae.getActionCommand().equals("Import")) {
                int returnVal = charGUI.fileChooser.showOpenDialog(charGUI.contentPane);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    this.dataHandler.ImportData(file);
                }
            }
        }
    }

    private class HeaderPanel extends JPanel implements DocumentListener {
        // Goes at the top, contains character details panel

        private JTextField characterNameTextField;
        private CharacterDetailsPanel charDetailsPanel;

        private CharacterSheetGUI charGUI;

        public HeaderPanel(CharacterSheetGUI charGUI) {
            this.charGUI = charGUI;
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
            characterNameTextField.getDocument().addDocumentListener(this);

            charDetailsPanel = new CharacterDetailsPanel(this.charGUI);
            constraints.gridx = 2;
            this.add(charDetailsPanel, constraints);
        }

        public void UpdateFields() {
            this.characterNameTextField.setText(this.charGUI.character.getCharacterName());
            this.charDetailsPanel.UpdateFields();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            Object owner = e.getDocument().getProperty("owner");
            this.charGUI.character.setCharacterName(((JTextField) owner).getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            Object owner = e.getDocument().getProperty("owner");
            this.charGUI.character.setCharacterName(((JTextField) owner).getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        private class CharacterDetailsPanel extends JPanel implements DocumentListener {
            // Goes within header - contains character information

            private CharacterSheetGUI charGUI;
            private JComboBox<Character.Classes> classDropDown;
            private JTextField backgroundTextField;
            private JTextField playerNameTextField;
            private JComboBox<Character.Races> raceDropDown;
            private JComboBox<Character.Alignment> alignmentDropDown;
            private JFormattedTextField expPointsTextField;

            public CharacterDetailsPanel(CharacterSheetGUI charGUI) {
                this.charGUI = charGUI;
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
                        charGUI.character.setDndClass((Character.Classes) classDropDown.getSelectedItem()));

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
                        charGUI.character.setRace((Character.Races) raceDropDown.getSelectedItem()));
                raceDropDown.setPreferredSize(backgroundTextField.getPreferredSize());
                constraints.gridx = 1;
                this.add(raceDropDown, constraints);

                JLabel alignmentLabel = new JLabel("Alignment");
                constraints.gridx = 2;
                this.add(alignmentLabel, constraints);

                alignmentDropDown = new JComboBox<>(Character.Alignment.values());
                alignmentDropDown.addActionListener((ActionEvent e) ->
                        charGUI.character.setAlignment((Character.Alignment) alignmentDropDown.getSelectedItem()));
                alignmentDropDown.setPreferredSize(backgroundTextField.getPreferredSize());

                constraints.gridx = 3;
                this.add(alignmentDropDown, constraints);

                JLabel expPointsLabel = new JLabel("Experience Points");
                constraints.gridx = 4;
                this.add(expPointsLabel, constraints);

                // Create formatter for expPoints
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setGroupingUsed(false);
                NumberFormatter formatter = new NumberFormatter(numberFormat);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);

                // Create exp text field
                expPointsTextField = new JFormattedTextField(formatter);
                expPointsTextField.setPreferredSize(backgroundTextField.getPreferredSize());
                expPointsTextField.getDocument().addDocumentListener(this);
                expPointsTextField.getDocument().putProperty("owner", expPointsTextField);
                expPointsTextField.getDocument().putProperty("charValue", "ExpPoints");
                constraints.gridx = 5;
                this.add(expPointsTextField, constraints);
            }

            public void UpdateFields() {
                this.classDropDown.setSelectedItem(this.charGUI.character.getDndClass());
                this.backgroundTextField.setText(this.charGUI.character.getBackground());
                this.playerNameTextField.setText(this.charGUI.character.getPlayerName());
                this.raceDropDown.setSelectedItem(this.charGUI.character.getRace());
                this.alignmentDropDown.setSelectedItem(this.charGUI.character.getAlignment());
                this.expPointsTextField.setText(Integer.toString(this.charGUI.character.getExperiencePoints()));
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
                        this.charGUI.character.setBackground(owner.getText());
                        break;
                    case "PlayerName":
                        this.charGUI.character.setPlayerName(owner.getText());
                        break;
                    case "ExpPoints":
                        try {
                            this.charGUI.character.setExperiencePoints(Integer.parseInt(owner.getText()));
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

    private class CharacterValuesHolder extends JPanel {
        // Contains panels for attributes, skills, saving throws etc.
        private AttributePanel attrPanel;
        private SavingThrowsPanel savingThrowsPanel;
        private SkillsPanel skillsPanel;

        private InputBox inspirationInputBox;
        private InputBox proficiencyBonus;
        private InputBox passiveWisInputBox;

        private JTextArea profAndLangTextArea;

        private CharacterSheetGUI charGUI;

        public CharacterValuesHolder(CharacterSheetGUI charGUI) {
            this.charGUI = charGUI;

            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridBagLayout());
        }

        private void AddComponents() {
            GridBagConstraints constraints = new GridBagConstraints();
            attrPanel = new AttributePanel(this.charGUI);

            constraints.gridx = 0;
            constraints.gridy = 0;
            this.add(attrPanel, constraints);

            JPanel rightHoldingPanel = new JPanel();
            rightHoldingPanel.setLayout(new GridBagLayout());

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 1;
            this.add(rightHoldingPanel, constraints);

            inspirationInputBox = new InputBox("Inspiration");
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_START;
            rightHoldingPanel.add(inspirationInputBox, constraints);

            proficiencyBonus = new InputBox("Proficiency Bonus");
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

            passiveWisInputBox = new InputBox("Passive Wisdom (Perception)");
            bottomHoldingPanel.add(passiveWisInputBox);

            profAndLangTextArea = new JTextArea(4, 20);
            profAndLangTextArea.setLineWrap(true);
            profAndLangTextArea.setWrapStyleWord(true);
            profAndLangTextArea.setMinimumSize(new Dimension(profAndLangTextArea.getPreferredSize()));

            JScrollPane profAndLangScrollPane = new JScrollPane(profAndLangTextArea);
            profAndLangScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            bottomHoldingPanel.add(profAndLangScrollPane);

            JLabel profAndLangLabel = new JLabel("Other Proficiencies & Languages");
            profAndLangLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            bottomHoldingPanel.add(profAndLangLabel);

        }

        public void UpdateFields() {
            attrPanel.UpdateFields();
        }

        private class InputBox extends JPanel {

            private JTextField inputBox;
            private String labelText;

            public InputBox(String labelText) {
                this.labelText = labelText;

                InitializePanel();
                AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            }

            private void AddComponents() {
                inputBox = new JTextField();
                this.add(inputBox);
                inputBox.setColumns(3);
                inputBox.setMaximumSize(new Dimension(inputBox.getPreferredSize()));

                JLabel boxLabel = new JLabel(labelText);
                this.add(boxLabel);
            }
        }

        private class AttributePanel extends JPanel {
            private AttributeBox strBox;
            private AttributeBox dexBox;
            private AttributeBox conBox;
            private AttributeBox intBox;
            private AttributeBox wisBox;
            private AttributeBox chaBox;

            private CharacterSheetGUI charGUI;

            public AttributePanel(CharacterSheetGUI charGUI) {
                this.charGUI = charGUI;

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

            public void UpdateFields() {

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

                    Font attrFont = new Font(Font.SANS_SERIF, Font.BOLD, 38);
                    attrArea.setFont(attrFont);
                    this.add(attrArea, constraints);

                    attrField = new JTextField((3));
                    constraints.gridy = 2;
                    this.add(attrField, constraints);
                }

                public void UpdateField(String area, String field) {
                    this.attrArea.setText(area);
                    this.attrField.setText(field);

                }

            }
        }

        private class CheckBoxPanel extends JPanel {

            private String labelText;
            private JCheckBox checkBox;
            private JTextField textField;

            public CheckBoxPanel(String labelText) {
                this.labelText = labelText;
                this.InitializePanel();
                this.AddComponents();
            }

            private void InitializePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            }

            private void AddComponents() {
                checkBox = new JCheckBox();
                this.add(checkBox);
                checkBox.setSelected(false);

                textField = new JTextField();
                this.add(textField);
                textField.setColumns(3);
                textField.setMaximumSize(textField.getPreferredSize());
                textField.setMinimumSize(textField.getPreferredSize());

                JLabel label = new JLabel(labelText);
                this.add(label);
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
                strCheckBox = new CheckBoxPanel("Strength");
                dexCheckBox = new CheckBoxPanel("Dexterity");
                conCheckBox = new CheckBoxPanel("Constitution");
                intCheckBox = new CheckBoxPanel("Intelligence");
                wisCheckBox = new CheckBoxPanel("Wisdom");
                chaCheckBox = new CheckBoxPanel("Charisma");

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
                    CheckBoxPanel skillPanel = new CheckBoxPanel(skill.toString());
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
        }
    }

    private class CombatPanel extends JPanel {
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

        private class InCombatPanel extends JPanel {
            private TextBoxOverLabel armorClassPanel;
            private TextBoxOverLabel initiativePanel;
            private TextBoxOverLabel speedPanel;
            private JTextField hpMaxTextField;
            private TextBoxOverLabel hpCurrentPanel;
            private TextBoxOverLabel hpTempPanel;
            private JTextField hitDiceTotalTextField;
            private TextBoxOverLabel hitDicePanel;
            private DeathSaveCheckBoxesPanel successDeathSaves;
            private DeathSaveCheckBoxesPanel failureDeathSaves;

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

                // Armor, Initiative, Speed
                armorClassPanel = new TextBoxOverLabel("Armor Class", 3);
                constraints.gridwidth = 2;
                constraints.weightx = 1;
                constraints.weighty = 1;
                this.add(armorClassPanel, constraints);

                initiativePanel = new TextBoxOverLabel("Initiative", 3);
                constraints.gridx = 2;
                this.add(initiativePanel, constraints);

                speedPanel = new TextBoxOverLabel("Speed", 3);
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
                constraints.gridx = 1;
                constraints.weightx = 1;
                currentHPHolder.add(hpMaxTextField, constraints);

                hpCurrentPanel = new TextBoxOverLabel("Current Hit Points", 7);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                currentHPHolder.add(hpCurrentPanel, constraints);

                constraints.gridwidth = 6;
                this.add(currentHPHolder, constraints);

                // Temp HP
                hpTempPanel = new TextBoxOverLabel("Temporary Hit Points", 7);
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
                constraints.gridx = 1;
                constraints.weightx = 1;
                hitDiceHolder.add(hitDiceTotalTextField, constraints);

                hitDicePanel = new TextBoxOverLabel("Hit Dice", 7);
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.gridwidth = 2;
                hitDiceHolder.add(hitDicePanel, constraints);

                constraints.gridx = 0;
                constraints.gridy = 3;
                constraints.gridwidth = 3;
                this.add(hitDiceHolder, constraints);

                JPanel deathSaveHolder = new JPanel();
                deathSaveHolder.setLayout(new GridBagLayout());

                successDeathSaves = new DeathSaveCheckBoxesPanel("Successes");
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.anchor = GridBagConstraints.LINE_END;
                deathSaveHolder.add(successDeathSaves, constraints);

                failureDeathSaves = new DeathSaveCheckBoxesPanel("Failures");
                constraints.gridy = 1;
                deathSaveHolder.add(failureDeathSaves, constraints);

                JLabel deathSavesLabel = new JLabel("Death Saves");
                deathSavesLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.gridy = 2;
                constraints.anchor = GridBagConstraints.CENTER;
                deathSaveHolder.add(deathSavesLabel, constraints);

                constraints.gridx = 3;
                constraints.gridy = 3;
                constraints.gridwidth = 3;
                this.add(deathSaveHolder, constraints);
            }

            private class TextBoxOverLabel extends JPanel {
                private JTextField textField;

                public TextBoxOverLabel(String labelText, int cols) {
                    this.InitializePanel();
                    this.AddComponents(labelText, cols);
                }

                private void InitializePanel() {

                    this.setLayout(new GridBagLayout());
                }

                private void AddComponents(String labelText, int cols) {
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.fill = GridBagConstraints.NONE;
                    constraints.anchor = GridBagConstraints.CENTER;
                    this.textField = new JTextField();
                    this.textField.setColumns(cols);

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

            private class DeathSaveCheckBoxesPanel extends JPanel {

                private JCheckBox firstCheckBox;
                private JCheckBox secondCheckBox;
                private JCheckBox thirdCheckBox;

                private final int boxSize = 5;

                public DeathSaveCheckBoxesPanel(String labelText) {
                    InitializePanel();
                    AddComponents(labelText);
                }

                private void InitializePanel() {
                    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                }

                private void AddComponents(String labelText) {
                    JLabel label = new JLabel(labelText);
                    this.add(label);

                    firstCheckBox = new JCheckBox();
                    secondCheckBox = new JCheckBox();
                    thirdCheckBox = new JCheckBox();

                    this.add(firstCheckBox);
                    this.add(Box.createRigidArea(new Dimension(boxSize, 0)));
                    this.add(secondCheckBox);
                    this.add(Box.createRigidArea(new Dimension(boxSize, 0)));
                    this.add(thirdCheckBox);
                }
            }
        }

        private class AtkSpellsPanel extends JPanel {
            private InputFields nameFields;
            private InputFields atkBonusFields;
            private InputFields dmgTypeFields;

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

                nameFields = new InputFields("Name", 3, 20);
                constraints.gridx = 0;
                constraints.gridy = 0;
                this.add(nameFields, constraints);

                atkBonusFields = new InputFields("Atk Bonus", 3, 3);
                constraints.gridx = 1;
                this.add(atkBonusFields, constraints);

                dmgTypeFields = new InputFields("Damage/Type", 3, 5);
                constraints.gridx = 2;
                this.add(dmgTypeFields, constraints);

                additionalSpellsArea = new JTextArea(4, 38);
                additionalSpellsArea.setLineWrap(true);
                additionalSpellsArea.setWrapStyleWord(true);

                JScrollPane spellsScrollPane = new JScrollPane(additionalSpellsArea);
                spellsScrollPane.setHorizontalScrollBarPolicy(
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                constraints.gridx = 0;
                constraints.gridy = 2;
                constraints.gridwidth = 3;
                constraints.anchor = GridBagConstraints.CENTER;
                this.add(spellsScrollPane, constraints);

                JLabel titleLabel = new JLabel("Attacks & Spellcasting");
                titleLabel.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.gridx = 0;
                constraints.gridy = 3;
                constraints.anchor = GridBagConstraints.PAGE_END;
                constraints.gridwidth = 3;
                this.add(titleLabel, constraints);

            }

            private class InputFields extends JPanel {
                List<JTextField> inputFields;

                public InputFields(String labelText, int rowCount, int fieldColumns) {
                    InitializePanel();
                    AddComponents(labelText, rowCount, fieldColumns);
                }

                private void InitializePanel() {
                    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                }

                private void AddComponents(String labelText, int rowCount, int fieldColumns) {
                    JLabel headerLabel = new JLabel(labelText);
                    this.add(headerLabel);

                    inputFields = new ArrayList<>();

                    for (int row = 0; row < rowCount; row++) {
                        JTextField inputField = new JTextField(fieldColumns);
                        this.add(inputField);
                        inputFields.add(inputField);
                    }
                }
            }
        }

        private class EquipmentPanel extends JPanel {

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

                JLabel cpLabel = new JLabel("CP");
                copperPiecesField = new JTextField();
                copperPiecesField.setColumns(3);
                this.add(cpLabel, constraints);
                constraints.gridx = 1;
                this.add(copperPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel spLabel = new JLabel("SP");
                silverPiecesField = new JTextField();
                silverPiecesField.setColumns(3);
                this.add(spLabel, constraints);
                constraints.gridx = 1;
                this.add(silverPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel epLabel = new JLabel("EP");
                electrumPiecesField = new JTextField();
                electrumPiecesField.setColumns(3);
                this.add(epLabel, constraints);
                constraints.gridx = 1;
                this.add(electrumPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel gpLabel = new JLabel("GP");
                goldPiecesField = new JTextField();
                goldPiecesField.setColumns(3);
                this.add(gpLabel, constraints);
                constraints.gridx = 1;
                this.add(goldPiecesField, constraints);

                constraints.gridx = 0;
                constraints.gridy += 1;
                JLabel ppLabel = new JLabel("PP");
                platinumPiecesField = new JTextField();
                platinumPiecesField.setColumns(3);
                this.add(ppLabel, constraints);
                constraints.gridx = 1;
                this.add(platinumPiecesField, constraints);

                equipmentArea = new JTextArea(6, 30);
                equipmentArea.setLineWrap(true);
                equipmentArea.setWrapStyleWord(true);
                equipmentArea.setMinimumSize(new Dimension(equipmentArea.getPreferredSize()));

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


        }
    }

    private class TraitsPanel extends JPanel {

        private TitleUnderTextArea personalityTraitsArea;
        private TitleUnderTextArea idealsArea;
        private TitleUnderTextArea bondsArea;
        private TitleUnderTextArea flawsArea;
        private TitleUnderTextArea featAndTraitsArea;

        private CharacterSheetGUI charGUI;

        public TraitsPanel(CharacterSheetGUI charGUI) {
            this.charGUI = charGUI;

            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(CharacterSheetGUI.SectionBorder);
        }

        private void AddComponents() {
            personalityTraitsArea = new TitleUnderTextArea(this.charGUI, "Personality Traits", 6, 25);
            idealsArea = new TitleUnderTextArea(this.charGUI, "Ideals", 5, 25);
            bondsArea = new TitleUnderTextArea(this.charGUI, "Bonds", 5, 25);
            flawsArea = new TitleUnderTextArea(this.charGUI, "Flaws", 5, 25);
            featAndTraitsArea = new TitleUnderTextArea(this.charGUI, "Features & Traits", 10, 25);

            this.add(personalityTraitsArea);
            this.add(idealsArea);
            this.add(bondsArea);
            this.add(flawsArea);
            this.add(featAndTraitsArea);
        }

        public void UpdateFields() {
            this.personalityTraitsArea.setTextBody(this.charGUI.character.getPersonalityTraits());
            this.idealsArea.setTextBody(this.charGUI.character.getIdeals());
            this.bondsArea.setTextBody(this.charGUI.character.getBonds());
            this.flawsArea.setTextBody(this.charGUI.character.getFlaws());
            this.featAndTraitsArea.setTextBody(this.charGUI.character.getFeatureTraits());
        }

        private class TitleUnderTextArea extends JPanel implements DocumentListener {

            public void setTextBody(String body) {
                this.textArea.setText(body);
            }

            private JTextArea textArea;
            private CharacterSheetGUI charGUI;

            public TitleUnderTextArea(CharacterSheetGUI charGUI, String labelText, int rows, int cols) {
                this.charGUI = charGUI;

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
                textArea.getDocument().putProperty("attribute", labelText);

                JScrollPane profAndLangScrollPane = new JScrollPane(textArea);
                profAndLangScrollPane.setHorizontalScrollBarPolicy(
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                this.add(profAndLangScrollPane);

                JLabel label = new JLabel(labelText);
                label.setFont(CharacterSheetGUI.SectionTitleFont);
                this.add(label);
                label.setAlignmentX(CENTER_ALIGNMENT);

                this.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                SetCharAttribute(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SetCharAttribute(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SetCharAttribute(e);
            }

            private void SetCharAttribute(DocumentEvent e) {
                JTextArea textArea = (JTextArea) e.getDocument().getProperty("owner");
                String attribute = (String) e.getDocument().getProperty("attribute");
                try {
                    switch (attribute) {
                        case "Personality Traits":
                            this.charGUI.character.setPersonalityTraits(textArea.getText());
                            break;
                        case "Ideals":
                            this.charGUI.character.setIdeals(textArea.getText());
                            break;
                        case "Bonds":
                            this.charGUI.character.setBonds(textArea.getText());
                            break;
                        case "Flaws":
                            this.charGUI.character.setFlaws(textArea.getText());
                            break;
                        case "Features & Traits":
                            this.charGUI.character.setFeatureTraits(textArea.getText());
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

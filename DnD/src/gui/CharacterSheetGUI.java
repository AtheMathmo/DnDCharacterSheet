package gui;

import engine.Character;
import jdk.internal.util.xml.impl.Input;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import java.util.List;
import javax.swing.border.BevelBorder;

public class CharacterSheetGUI extends JFrame {
    public static final Font SectionTitleFont = new Font(Font.SANS_SERIF,Font.BOLD, 18);

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CharacterSheetGUI frame = new CharacterSheetGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public CharacterSheetGUI() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		HeaderPanel headerPanel = new HeaderPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);

		CharacterValuesHolder charValueHolder = new CharacterValuesHolder();
		contentPane.add(charValueHolder, BorderLayout.WEST);

        CombatPanel combatPanel = new CombatPanel();
        contentPane.add(combatPanel, BorderLayout.CENTER);
	}

    private class HeaderPanel extends JPanel {
        // Goes at the top, contains character details panel

        private JTextField characterNameTextField;
        private CharacterDetailsPanel charDetailsPanel;

        public HeaderPanel() {
            InitializePanel();
            AddComponents();
        }

        void InitializePanel() {

        }

        void AddComponents() {
            JLabel characterNameLabel = new JLabel("Character Name");
            this.add(characterNameLabel);

            characterNameTextField = new JTextField();
            this.add(characterNameTextField);
            Font charNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
            characterNameTextField.setFont(charNameFont);
            characterNameTextField.setColumns(7);


            charDetailsPanel = new CharacterDetailsPanel();
            this.add(charDetailsPanel);
        }
    }

	private class CharacterDetailsPanel extends JPanel {
		// Goes within header - contains character information

        public JTextField getClassTextField() {
            return classTextField;
        }

        public JTextField getBackgroundTextField() {
            return backgroundTextField;
        }

        public JTextField getPlayerNameTextField() {
            return playerNameTextField;
        }

        public JTextField getRaceTextField() {
            return raceTextField;
        }

        public JTextField getAlignmentTextField() {
            return alignmentTextField;
        }

        public JTextField getExpPointsTextFields() {
            return expPointsTextFields;
        }

        private JTextField classTextField;
        private JTextField backgroundTextField;
        private JTextField playerNameTextField;
        private JTextField raceTextField;
        private JTextField alignmentTextField;
        private JTextField expPointsTextFields;

        public CharacterDetailsPanel(){
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridLayout(2,6));
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

        }

        private void AddComponents() {
            JLabel classLabel = new JLabel("Class");
            this.add(classLabel);

            classTextField = new JTextField();
            this.add(classTextField);
            classTextField.setColumns(10);

            JLabel backgroundLabel = new JLabel("Background");
            this.add(backgroundLabel);

            backgroundTextField = new JTextField();
            this.add(backgroundTextField);
            backgroundTextField.setColumns(10);

            JLabel playerNameLabel = new JLabel("Player Name");
            this.add(playerNameLabel);

            playerNameTextField = new JTextField();
            this.add(playerNameTextField);
            playerNameTextField.setColumns(10);

            JLabel raceLabel = new JLabel("Race");
            this.add(raceLabel);

            raceTextField = new JTextField();
            this.add(raceTextField);
            raceTextField.setColumns(10);

            JLabel alignmentLabel = new JLabel("Alignment");
            this.add(alignmentLabel);

            alignmentTextField = new JTextField();
            this.add(alignmentTextField);
            alignmentTextField.setColumns(10);

            JLabel expPointsLabel = new JLabel("Experience Points");
            this.add(expPointsLabel);

            expPointsTextFields = new JTextField();
            this.add(expPointsTextFields);
            expPointsTextFields.setColumns(10);
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

            constraints.gridx = 0; constraints.gridy  = 0;
            this.add(attrPanel, constraints);

            JPanel rightHoldingPanel = new JPanel();
            rightHoldingPanel.setLayout(new BoxLayout(rightHoldingPanel, BoxLayout.Y_AXIS));

            constraints.gridx = 1; constraints.gridy = 0;
            this.add(rightHoldingPanel, constraints);

            inspirationInputBox =  new InputBox("Inspiration");
            rightHoldingPanel.add(inspirationInputBox);
            inspirationInputBox.setAlignmentX(LEFT_ALIGNMENT);

            proficiencyBonus = new InputBox("Proficiency Bonus");
            rightHoldingPanel.add(proficiencyBonus);
            proficiencyBonus.setAlignmentX(LEFT_ALIGNMENT);

            savingThrowsPanel = new SavingThrowsPanel();
            rightHoldingPanel.add(savingThrowsPanel);

            skillsPanel = new SkillsPanel();
            rightHoldingPanel.add(skillsPanel);

            JPanel bottomHoldingPanel = new JPanel();
            bottomHoldingPanel.setLayout(new BoxLayout(bottomHoldingPanel, BoxLayout.Y_AXIS));

            constraints.gridx = 0; constraints.gridy = 1; constraints.gridwidth = 2;
            this.add(bottomHoldingPanel, constraints);

            passiveWisInputBox = new InputBox("Passive Wisdom (Perception)");
            bottomHoldingPanel.add(passiveWisInputBox);

            profAndLangTextArea = new JTextArea(6,20);
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
    }

    private class AttributePanel extends JPanel {

        public JTextField getStrTextField() {
            return strTextField;
        }

        public JTextField getDexTextField() {
            return dexTextField;
        }

        public JTextField getConTextField() {
            return conTextField;
        }

        public JTextField getIntTextField() {
            return intTextField;
        }

        public JTextField getWisTextField() {
            return wisTextField;
        }

        public JTextField getChaTextfield() {
            return chaTextfield;
        }

        private JTextField strTextField;
        private JTextField dexTextField;
        private JTextField conTextField;
        private JTextField intTextField;
        private JTextField wisTextField;
        private JTextField chaTextfield;

        public AttributePanel() {
            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }

        private void AddComponents() {
            JLabel strengthLabel = new JLabel("Strength");
            this.add(strengthLabel);

            strTextField = new JTextField();
            this.add(strTextField);
            strTextField.setColumns(3);

            JLabel dexterityLabel = new JLabel("Dexterity");
            this.add(dexterityLabel);

            dexTextField = new JTextField();
            this.add(dexTextField);
            dexTextField.setColumns(3);

            JLabel constitutionLabel = new JLabel("Constitution");
            this.add(constitutionLabel);

            conTextField = new JTextField();
            this.add(conTextField);
            conTextField.setColumns(3);

            JLabel intelligenceLabel = new JLabel("Intelligence");
            this.add(intelligenceLabel);

            intTextField = new JTextField();
            this.add(intTextField);
            intTextField.setColumns(3);

            JLabel wisdomLabel = new JLabel("Wisdom");
            this.add(wisdomLabel);

            wisTextField = new JTextField();
            this.add(wisTextField);
            wisTextField.setColumns(3);

            JLabel charismaLabel = new JLabel("Charisma");
            this.add(charismaLabel);

            chaTextfield = new JTextField();
            this.add(chaTextfield);
            chaTextfield.setColumns(3);
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
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }

        private void AddComponents() {
            strCheckBox = new CheckBoxPanel("Strength");
            dexCheckBox = new CheckBoxPanel("Dexterity");
            conCheckBox = new CheckBoxPanel("Constitution");
            intCheckBox = new CheckBoxPanel("Intelligence");
            wisCheckBox = new CheckBoxPanel("Wisdom");
            chaCheckBox = new CheckBoxPanel("Charisma");

            this.add(strCheckBox);
            this.add(dexCheckBox);
            this.add(conCheckBox);
            this.add(intCheckBox);
            this.add(wisCheckBox);
            this.add(chaCheckBox);

            strCheckBox.setAlignmentX(LEFT_ALIGNMENT);
            dexCheckBox.setAlignmentX(LEFT_ALIGNMENT);
            conCheckBox.setAlignmentX(LEFT_ALIGNMENT);
            intCheckBox.setAlignmentX(LEFT_ALIGNMENT);
            wisCheckBox.setAlignmentX(LEFT_ALIGNMENT);
            chaCheckBox.setAlignmentX(LEFT_ALIGNMENT);

            JLabel savingThrowsLabel = new JLabel("Saving Throws");
            savingThrowsLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            this.add(savingThrowsLabel);

        }
    }

    private class SkillsPanel extends JPanel {

        private Map<Character.Skills, CheckBoxPanel> skillPanelMap;

        public SkillsPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }

        private void AddComponents() {
            skillPanelMap = new HashMap<>();

            for (Character.Skills skill : Character.Skills.values()) {
                CheckBoxPanel skillPanel = new CheckBoxPanel(skill.toString());
                skillPanelMap.put(skill, skillPanel);
                this.add(skillPanel);
                skillPanel.setAlignmentX(LEFT_ALIGNMENT);
            }

            JLabel skillsLabel = new JLabel("Skills");
            skillsLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            this.add(skillsLabel);

        }
    }

    private class CombatPanel extends JPanel {
        private InCombatPanel inCombatPanel;
        private AtkSpellsPanel atkSpellsPanel;
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
        private DeathSaveCheckBoxesPanel successDeathSaves;
        private DeathSaveCheckBoxesPanel failureDeathSaves;

        public InCombatPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridBagLayout());
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }

        private void AddComponents() {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0; constraints.gridy = 0; constraints.ipadx =1; constraints.ipady = 1;

            // Armor, Initiative, Speed
            armorClassPanel = new TextBoxOverLabel("Armor Class", 3);
            constraints.weightx = 1; constraints.weighty = 1;
            this.add(armorClassPanel, constraints);

            initiativePanel = new TextBoxOverLabel("Initiative", 3);
            constraints.gridx = 1;
            this.add(initiativePanel, constraints);

            speedPanel = new TextBoxOverLabel("Speed", 3);
            constraints.gridx = 2;
            this.add(speedPanel, constraints);

            // Current HP
            JPanel currentHPHolder = new JPanel();
            currentHPHolder.setLayout(new GridBagLayout());

            JLabel hpMaxLabel = new JLabel("Hit Point Maximum");
            constraints.gridx = 0; constraints.gridy = 0;
            currentHPHolder.add(hpMaxLabel, constraints);

            hpMaxTextField = new JTextField();
            hpMaxTextField.setColumns(3);
            constraints.gridx = 1; constraints.weightx = 1;
            currentHPHolder.add(hpMaxTextField, constraints);

            hpCurrentPanel = new TextBoxOverLabel("Current Hit Points", 7);
            constraints.gridx = 0; constraints.gridy = 1; constraints.gridwidth = 2;
            currentHPHolder.add(hpCurrentPanel, constraints);

            constraints.gridwidth = 3;
            this.add(currentHPHolder, constraints);

            // Temp HP
            hpTempPanel = new TextBoxOverLabel("Temporary Hit Points", 7);
            constraints.gridx = 0; constraints.gridy = 2; constraints.gridwidth = 3;
            this.add(hpTempPanel, constraints);

            // Hit Dice, Death Saves
            JPanel hitDiceHolder = new JPanel();
            hitDiceHolder.setLayout(new GridBagLayout());

            JLabel totalLabel = new JLabel("Total");
            constraints.gridx = 0; constraints.gridy = 0; constraints.gridwidth = 1;
            hitDiceHolder.add(totalLabel, constraints);

            hitDiceTotalTextField = new JTextField();
            hitDiceTotalTextField.setColumns(3);
            constraints.gridx = 1; constraints.weightx = 1;
            hitDiceHolder.add(hitDiceTotalTextField, constraints);

            hitDicePanel = new TextBoxOverLabel("Hit Dice", 7);
            constraints.gridx = 0; constraints.gridy = 1; constraints.gridwidth = 2;
            hitDiceHolder.add(hitDicePanel, constraints);

            constraints.gridx = 0; constraints.gridy = 3; constraints.gridwidth = 1;
            this.add(hitDiceHolder, constraints);

            JPanel deathSaveHolder = new JPanel();
            deathSaveHolder.setLayout(new GridBagLayout());

            successDeathSaves = new DeathSaveCheckBoxesPanel("Successes");
            constraints.gridx = 0; constraints.gridy = 0; constraints.anchor = GridBagConstraints.LINE_END;
            deathSaveHolder.add(successDeathSaves, constraints);

            failureDeathSaves  = new DeathSaveCheckBoxesPanel("Failures");
            constraints.gridy = 1;
            deathSaveHolder.add(failureDeathSaves, constraints);

            JLabel deathSavesLabel = new JLabel("Death Saves");
            deathSavesLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            constraints.gridy = 2; constraints.anchor = GridBagConstraints.CENTER;
            deathSaveHolder.add(deathSavesLabel, constraints);

            constraints.gridx = 1; constraints.gridy = 3;
            this.add(deathSaveHolder, constraints);
        }

        private class TextBoxOverLabel extends JPanel {
            private JTextField textField;

            public TextBoxOverLabel(String labelText,int cols) {
                this.InitializePanel();
                this.AddComponents(labelText, cols);
            }

            private void InitializePanel() {
                this.setLayout(new GridBagLayout());
            }

            private void AddComponents(String labelText, int cols) {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.NONE; constraints.anchor = GridBagConstraints.CENTER;
                this.textField = new JTextField();
                this.textField.setColumns(cols);

                constraints.gridx = 0; constraints.gridy = 0; constraints.gridwidth = 1; constraints.weightx = 1;
                this.add(textField, constraints);

                JLabel label = new JLabel(labelText, JLabel.CENTER);
                label.setFont(CharacterSheetGUI.SectionTitleFont);
                constraints.gridx = 0; constraints.gridy = 1; constraints.gridwidth = 3;
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
                this.add(Box.createRigidArea(new Dimension(boxSize,0)));
                this.add(secondCheckBox);
                this.add(Box.createRigidArea(new Dimension(boxSize,0)));
                this.add(thirdCheckBox);
            }
        }
    }

    private class AtkSpellsPanel extends JPanel {
        private InputFields nameFields;
        private InputFields atkBonusFields;
        private InputFields dmgTypeFields;

        public AtkSpellsPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new GridBagLayout());
            this.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }

        private void AddComponents() {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1; constraints.anchor = GridBagConstraints.LINE_START;
            constraints.ipadx = 1;

            nameFields = new InputFields("Name",3,20);
            constraints.gridx = 0; constraints.gridy = 0;
            this.add(nameFields, constraints);

            atkBonusFields = new InputFields("Atk Bonus",3,3);
            constraints.gridx = 1;
            this.add(atkBonusFields, constraints);

            dmgTypeFields = new InputFields("Damage/Type",3,5);
            constraints.gridx = 2;
            this.add(dmgTypeFields, constraints);

            JLabel titleLabel = new JLabel("Attacks & Spellcasting");
            titleLabel.setFont(CharacterSheetGUI.SectionTitleFont);
            constraints.gridx = 0; constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.PAGE_END; constraints.gridwidth = 3;
            this.add(titleLabel, constraints);

        }
        
        private class InputFields extends JPanel{
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

}

package gui;

import engine.*;
import engine.Character;
import org.w3c.dom.Attr;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.BevelBorder;

public class CharacterSheetGUI extends JFrame {

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
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
        panel_1.add(new JLabel("CENTER"));
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.EAST);
		
		CharacterValuesHolder charValueHolder = new CharacterValuesHolder();
		contentPane.add(charValueHolder, BorderLayout.WEST);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4, BorderLayout.SOUTH);
	}

    private class HeaderPanel extends JPanel {
        // Goes at the top, contains character details panel

        private JTextField characterNameTextField;
        private CharacterDetailsPanel charDetailsPanel;

        public HeaderPanel() {
            InitializePanel();
            AddComponents();
        }
        private void InitializePanel() {

        }

        private void AddComponents() {
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

        public CharacterValuesHolder() {

            this.InitializePanel();
            this.AddComponents();
        }

        private void InitializePanel() {

        }

        private void AddComponents() {
            attrPanel = new AttributePanel();
            this.add(attrPanel, BorderLayout.WEST);

            JPanel holdingPanel = new JPanel();
            holdingPanel.setLayout(new BoxLayout(holdingPanel, BoxLayout.Y_AXIS));
            this.add(holdingPanel, BorderLayout.EAST);

            skillsPanel = new SkillsPanel();
            holdingPanel.add(skillsPanel);

            JLabel skillsLabel = new JLabel("Skills");
            Font sectionTitleFont = new Font(Font.SANS_SERIF,Font.BOLD, 22);
            skillsLabel.setFont(sectionTitleFont);
            holdingPanel.add(skillsLabel);
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

    private class SavingThrowsPanel extends JPanel {

    }

    private class SkillsPanel extends JPanel {

        private Map<Character.Skills, SkillPanel> skillPanelMap;

        public SkillsPanel() {
            InitializePanel();
            AddComponents();
        }

        private void InitializePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        private void AddComponents() {
            skillPanelMap = new HashMap<>();

            for (Character.Skills skill : Character.Skills.values()) {
                SkillPanel skillPanel = new SkillPanel(skill);
                skillPanelMap.put(skill, skillPanel);
                this.add(skillPanel);
                skillPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }

        private class SkillPanel extends JPanel {

            private Character.Skills skill;
            private JCheckBox checkBox;
            private JTextField textField;

            public SkillPanel(Character.Skills skill) {
                this.skill = skill;
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
                textField.setMaximumSize(new Dimension(textField.getPreferredSize().width, Integer.MAX_VALUE));

                JLabel label = new JLabel(skill.toString());
                this.add(label);
            }
        }
    }


}

package ru.itmo.p3114.s312198.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.structures.Color;
import ru.itmo.p3114.s312198.structures.Country;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Locale;

public class PersonForm extends UIForm {
    static final Logger logger = LoggerFactory.getLogger(GroupsForm.class);

    public static final String ACTION_SAVE = "actionSave";
    public static final String ACTION_CANCEL = "actionCancel";

    // Form components
    private final JPanel pPerson = new JPanel();
    private final JLabel lId = new JLabel();
    private final JTextField tfId = new JFormattedTextField();
    private final JLabel lName = new JLabel();
    private final JTextField tfName = new JTextField();
    private final JLabel lHeight = new JLabel();
    private final JTextField tfHeight = new JFormattedTextField();
    private final JLabel lHairColor = new JLabel();
    private final JComboBox<Color> cbHairColor = new JComboBox<>();
    private final JLabel lNationality = new JLabel();
    private final JComboBox<Country> cbNationality = new JComboBox<>();
    private final JLabel lLocation = new JLabel();
    private final JLabel lLocationX = new JLabel();
    private final JLabel lLocationY = new JLabel();
    private final JLabel lLocationZ = new JLabel();
    private final JLabel lLocationName = new JLabel();
    private final JTextField tfLocationX = new JFormattedTextField();
    private final JTextField tfLocationY = new JFormattedTextField();
    private final JTextField tfLocationZ = new JFormattedTextField();
    private final JTextField tfLocationName = new JTextField();
    private final JPanel pLocation = new JPanel();
    private final JButton btnSave = new JButton();
    private final JButton btnCancel = new JButton();
    private final JPanel pButtons = new JPanel();

    public PersonForm(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
    }

    @Override
    protected void prepareGUI(Locale locale) {
        instance = new JDialog();

        // Build buttons panel
        btnSave.setActionCommand(ACTION_SAVE);
        btnSave.addActionListener(this);
        pButtons.add(btnSave);
        btnCancel.setActionCommand(ACTION_CANCEL);
        btnCancel.addActionListener(this);
        pButtons.add(btnCancel);
        // Build Location panel
        pLocation.add(tfLocationX);
        pLocation.add(tfLocationY);
        pLocation.add(tfLocationZ);
        pLocation.add(tfLocationName);
        pLocation.setBorder(BorderFactory.createLineBorder(java.awt.Color.lightGray));

        GroupLayout glLocation = new GroupLayout(pLocation);
        pLocation.setLayout(glLocation);
        glLocation.setAutoCreateGaps(true);
        glLocation.setAutoCreateContainerGaps(true);

        glLocation.setVerticalGroup(
                glLocation.createSequentialGroup()
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lLocationX)
                                .addComponent(tfLocationX)
                        )
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lLocationY)
                                .addComponent(tfLocationY)
                        )
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lLocationZ)
                                .addComponent(tfLocationZ)
                        )
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lLocationName)
                                .addComponent(tfLocationName)
                        )
        );

        glLocation.setHorizontalGroup(
                glLocation.createSequentialGroup()
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lLocationX)
                                .addComponent(lLocationY)
                                .addComponent(lLocationZ)
                                .addComponent(lLocationName)
                        )
                        .addGroup(glLocation.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(tfLocationX)
                                .addComponent(tfLocationY)
                                .addComponent(tfLocationZ)
                                .addComponent(tfLocationName)
                        )
        );

        GroupLayout glPerson = new GroupLayout(pPerson);
        pPerson.setLayout(glPerson);
        glPerson.setAutoCreateGaps(true);
        glPerson.setAutoCreateContainerGaps(true);

        glPerson.setVerticalGroup(
                glPerson.createSequentialGroup()
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lId)
                                .addComponent(tfId)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lName)
                                .addComponent(tfName)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lHeight)
                                .addComponent(tfHeight)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lHairColor)
                                .addComponent(cbHairColor)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lNationality)
                                .addComponent(cbNationality)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lLocation)
                                .addComponent(pLocation)
                        )
                        .addComponent(pButtons)
        );

        glPerson.setHorizontalGroup(
                glPerson.createSequentialGroup()
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lId)
                                .addComponent(lName)
                                .addComponent(lHeight)
                                .addComponent(lHairColor)
                                .addComponent(lNationality)
                                .addComponent(lLocation)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(tfId)
                                .addComponent(tfName)
                                .addComponent(tfHeight)
                                .addComponent(cbHairColor)
                                .addComponent(cbNationality)
                                .addComponent(pLocation)
                                .addComponent(pButtons)
                        )
        );
        setLocale(locale);
        instance.add(pPerson);
        instance.setLocationRelativeTo(null);
        instance.pack();
    }

    @Override
    public void setLocale(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        if (instance != null) {
            instance.setTitle(resourceBundle.getString("form.person.title"));
        }
        lId.setText(resourceBundle.getString("form.person.id"));
        lName.setText(resourceBundle.getString("form.person.name"));
        lHeight.setText(resourceBundle.getString("form.person.height"));
        lHairColor.setText(resourceBundle.getString("form.person.hair.color"));
        lNationality.setText(resourceBundle.getString("form.person.nationality"));
        lLocation.setText(resourceBundle.getString("form.person.location"));
        lLocationX.setText(resourceBundle.getString("form.person.location.x"));
        lLocationY.setText(resourceBundle.getString("form.person.location.y"));
        lLocationZ.setText(resourceBundle.getString("form.person.location.z"));
        lLocationName.setText(resourceBundle.getString("form.person.location.name"));
        btnSave.setText(resourceBundle.getString("form.person.buttons.save"));
        btnCancel.setText(resourceBundle.getString("form.person.buttons.cancel"));
        refreshDropdowns(locale);
        instance.pack();
    }

    private void refreshDropdowns(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        cbHairColor.removeAllItems();
        Arrays.stream(Color.values()).sorted().forEach(cbHairColor::addItem);
        cbNationality.removeAllItems();
        Arrays.stream(Country.values()).sorted().forEach(cbNationality::addItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (ACTION_SAVE): {
                logger.info("Saving person...");
                break;
            }
            case (ACTION_CANCEL): {
                logger.info("Person edit has been cancelled");
                hide();
                break;
            }
        }
    }

    @Override
    public void loadById(Long id) {
        // 1. Call Server.getPersonById(id)
        // 2. Fill controls data with returned values
    }
}

package ru.itmo.p3114.s312198.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.structures.FormOfEducation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class GroupForm extends UIForm {
    static final Logger logger = LoggerFactory.getLogger(GroupForm.class);

    public static final String ACTION_SAVE = "actionSave";
    public static final String ACTION_CANCEL = "actionCancel";

    // Form elements
    private final JPanel pGroup = new JPanel();
    private final JLabel lId = new JLabel();
    private final JTextField tfId = new JTextField();
    private final JLabel lName = new JLabel();
    private final JTextField tfName = new JTextField();
    private final JLabel lOwner = new JLabel();
    private final JTextField tfOwner = new JTextField();
    private final JLabel lCoordinates = new JLabel();
    private final JPanel pCoordinates = new JPanel();
    private final JLabel lCoordinateX = new JLabel();
    private final JFormattedTextField tfCoordinateX = new JFormattedTextField(); // Long
    private final JLabel lCoordinateY = new JLabel();
    private final JFormattedTextField tfCoordinateY = new JFormattedTextField(); // Double
    private final JLabel lCreated = new JLabel();
    private final JFormattedTextField tfCreated = new JFormattedTextField(); // YYYY-MM-DD
    private final JLabel lStudentsCount = new JLabel();
    private final JFormattedTextField tfStudentsCount = new JFormattedTextField(); // Int
    private final JLabel lShouldBeExpelled = new JLabel();
    private final JFormattedTextField tfShouldBeExpelled = new JFormattedTextField(); // Int
    private final JLabel lTransferredStudents = new JLabel();
    private final JFormattedTextField tfTransferredStudents = new JFormattedTextField(); // Int
    private final JLabel lFormOfEducation = new JLabel();
    private final JComboBox<FormOfEducation> cbFormOfEducation = new JComboBox<>();
    private final JLabel lGroupAdmin = new JLabel();
    private final JComboBox<String> cbGroupAdmin = new JComboBox<>(); // Each Item is "<Person.name> (Person.id)>"

    // Buttons panel
    private final JPanel pButtons = new JPanel();
    private final JButton btnSave = new JButton();
    private final JButton btnCancel = new JButton();

    public GroupForm(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
    }

    @Override
    protected void prepareGUI(Locale locale) {
        instance = new JDialog();

        // Prepare fields formatters
        tfId.setEnabled(false);
        tfCreated.setEnabled(false);
        tfCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

//        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        DateFormatter df = new DateFormatter(format);
//        DefaultFormatterFactory dff = new DefaultFormatterFactory(df);

        GroupLayout glGroup = new GroupLayout(pGroup);
        pGroup.setLayout(glGroup);
        glGroup.setAutoCreateGaps(true);
        glGroup.setAutoCreateContainerGaps(true);

        // Compose Coordinates panel
        pCoordinates.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        GroupLayout glCoordinates = new GroupLayout(pCoordinates);
        pCoordinates.setLayout(glCoordinates);
        glCoordinates.setAutoCreateGaps(true);
        glCoordinates.setAutoCreateContainerGaps(true);
        glCoordinates.setVerticalGroup(
                glCoordinates.createSequentialGroup()
                        .addGroup(glCoordinates.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lCoordinateX)
                                .addComponent(tfCoordinateX)
                        )
                        .addGroup(glCoordinates.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lCoordinateY)
                                .addComponent(tfCoordinateY)
                        )
        );
        glCoordinates.setHorizontalGroup(
                glCoordinates.createSequentialGroup()
                        .addGroup(glCoordinates.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lCoordinateX)
                                .addComponent(lCoordinateY)
                        )
                        .addGroup(glCoordinates.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(tfCoordinateX)
                                .addComponent(tfCoordinateY)
                        )
        );
        // Compose buttons panel
        btnSave.setActionCommand(ACTION_SAVE);
        btnSave.addActionListener(this);
        pButtons.add(btnSave);
        btnCancel.setActionCommand(ACTION_CANCEL);
        btnCancel.addActionListener(this);
        pButtons.add(btnCancel);

        glGroup.setVerticalGroup(
                glGroup.createSequentialGroup()
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lId)
                                .addComponent(tfId)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lName)
                                .addComponent(tfName)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lOwner)
                                .addComponent(tfOwner)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lCoordinates)
                                .addComponent(pCoordinates)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lCreated)
                                .addComponent(tfCreated)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lStudentsCount)
                                .addComponent(tfStudentsCount)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lShouldBeExpelled)
                                .addComponent(tfShouldBeExpelled)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lTransferredStudents)
                                .addComponent(tfTransferredStudents)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lFormOfEducation)
                                .addComponent(cbFormOfEducation)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lGroupAdmin)
                                .addComponent(cbGroupAdmin)
                        )
                        .addComponent(pButtons)
        );
        glGroup.setHorizontalGroup(
                glGroup.createSequentialGroup()
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lId)
                                .addComponent(lName)
                                .addComponent(lOwner)
                                .addComponent(lCoordinates)
                                .addComponent(lCreated)
                                .addComponent(lStudentsCount)
                                .addComponent(lShouldBeExpelled)
                                .addComponent(lTransferredStudents)
                                .addComponent(lFormOfEducation)
                                .addComponent(lGroupAdmin)
                        )
                        .addGroup(glGroup.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(tfId)
                                .addComponent(tfName)
                                .addComponent(tfOwner)
                                .addComponent(pCoordinates)
                                .addComponent(tfCreated)
                                .addComponent(tfStudentsCount)
                                .addComponent(tfShouldBeExpelled)
                                .addComponent(tfTransferredStudents)
                                .addComponent(cbFormOfEducation)
                                .addComponent(cbGroupAdmin)
                                .addComponent(pButtons)
                        )
        );
        instance.add(pGroup);
        instance.setModal(true);
        setLocale(locale);
        instance.setLocationRelativeTo(null);
        instance.pack();
    }

    private void refreshDropdowns(Locale locale) {
        logger.info("Refreshing dropdowns");
        // Form of Education
        resourceBundle = getResourceBundle(locale);
        cbFormOfEducation.removeAllItems();
        Arrays.stream(FormOfEducation.values()).sorted().forEach(cbFormOfEducation::addItem);
        // Group Admin
        cbGroupAdmin.removeAllItems();
        // 1. Call Server.getPersons()
        // 2. Add dropdown items by template "<Person.name> (Person.id)>"
    }

    @Override
    public void setLocale(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        if (instance != null) {
            instance.setTitle(resourceBundle.getString("form.group.title"));
        }
        lId.setText(resourceBundle.getString("form.group.id"));
        lName.setText(resourceBundle.getString("form.group.name"));
        lOwner.setText(resourceBundle.getString("form.group.owner"));
        lCoordinates.setText(resourceBundle.getString("form.group.coordinates"));
        lCoordinateX.setText(resourceBundle.getString("form.group.coordinate.x"));
        lCoordinateY.setText(resourceBundle.getString("form.group.coordinate.y"));
        lCreated.setText(resourceBundle.getString("form.group.created"));
        lStudentsCount.setText(resourceBundle.getString("form.group.students.count"));
        lShouldBeExpelled.setText(resourceBundle.getString("form.group.students.expell"));
        lTransferredStudents.setText(resourceBundle.getString("form.group.students.transfer"));
        lFormOfEducation.setText(resourceBundle.getString("form.group.form"));
        lGroupAdmin.setText(resourceBundle.getString("form.group.admin"));
        btnSave.setText(resourceBundle.getString("form.group.buttons.save"));
        btnCancel.setText(resourceBundle.getString("form.group.buttons.cancel"));
        refreshDropdowns(locale);
        instance.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (ACTION_SAVE): {
                logger.info("Saving group...");
                break;
            }
            case (ACTION_CANCEL): {
                logger.info("Group edit has been cancelled");
                hide();
                break;
            }
        }
    }

    @Override
    public void loadById(Long id) {
        if (id == -1) {
            clearFields();
        } else {
            tfId.setText(id.toString());
            // 1. Call Server.getGroupById(id)
            // 2. Fill controls data with returned values
        }
    }

    private void clearFields() {
        tfId.setText("");
        tfName.setText("");
        tfCreated.setText("");
        tfCoordinateX.setText("");
        tfCoordinateY.setText("");
        tfOwner.setText("");
        tfStudentsCount.setText("");
        tfShouldBeExpelled.setText("");
        tfTransferredStudents.setText("");
    }
}

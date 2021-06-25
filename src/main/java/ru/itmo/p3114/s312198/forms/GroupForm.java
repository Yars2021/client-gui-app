package ru.itmo.p3114.s312198.forms;

import jdk.nashorn.internal.objects.NativeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.commands.CommandRecord;
import ru.itmo.p3114.s312198.commands.actions.complex.Add;
import ru.itmo.p3114.s312198.commands.actions.simple.Info;
import ru.itmo.p3114.s312198.commands.types.CommandTypes;
import ru.itmo.p3114.s312198.exceptions.InputInterruptedException;
import ru.itmo.p3114.s312198.exceptions.InvalidInputException;
import ru.itmo.p3114.s312198.exceptions.TransmissionException;
import ru.itmo.p3114.s312198.parsers.FieldParser;
import ru.itmo.p3114.s312198.structures.Country;
import ru.itmo.p3114.s312198.structures.FormOfEducation;
import ru.itmo.p3114.s312198.structures.Location;
import ru.itmo.p3114.s312198.structures.Person;
import ru.itmo.p3114.s312198.structures.StudyGroup;
import ru.itmo.p3114.s312198.structures.builders.LocationBuilder;
import ru.itmo.p3114.s312198.structures.builders.PersonBuilder;
import ru.itmo.p3114.s312198.structures.builders.StudyGroupBuilder;
import ru.itmo.p3114.s312198.transmission.CSChannel;
import ru.itmo.p3114.s312198.transmission.PrimaryPack;
import ru.itmo.p3114.s312198.transmission.ResponsePack;
import ru.itmo.p3114.s312198.transmission.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class GroupForm extends UIForm {
    static final Logger logger = LoggerFactory.getLogger(GroupForm.class);

    public static final String ACTION_SAVE = "actionSave";
    public static final String ACTION_CANCEL = "actionCancel";

    private CSChannel channel;
    private User actor;
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

    // Person section
    private final JPanel pLocation = new JPanel();
    private final JPanel pPerson = new JPanel();
    private final JLabel lPerson = new JLabel();
    private final JLabel lPId = new JLabel();
    private final JTextField tfPId = new JFormattedTextField();
    private final JLabel lPName = new JLabel();
    private final JTextField tfPName = new JTextField();
    private final JLabel lHeight = new JLabel();
    private final JTextField tfHeight = new JFormattedTextField();
    private final JLabel lHairColor = new JLabel();
    private final JComboBox<ru.itmo.p3114.s312198.structures.Color> cbHairColor = new JComboBox<>();
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

    // Buttons panel
    private final JPanel pButtons = new JPanel();
    private final JButton btnSave = new JButton();
    private final JButton btnCancel = new JButton();

    public GroupForm(Locale locale, CSChannel channel) {
        this.channel = channel;
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
    }

    public CSChannel getChannel() {
        return channel;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    @Override
    protected void prepareGUI(Locale locale) {
        instance = new JDialog();

        // Prepare fields formatters
        tfId.setEnabled(false);
        tfCreated.setEnabled(false);
        tfCreated.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tfOwner.setEnabled(false);
        tfPId.setEnabled(false);

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

        // Compose Person Panel
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
                                .addComponent(lPId)
                                .addComponent(tfPId)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lPName)
                                .addComponent(tfPName)
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
        );

        glPerson.setHorizontalGroup(
                glPerson.createSequentialGroup()
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lPId)
                                .addComponent(lPName)
                                .addComponent(lHeight)
                                .addComponent(lHairColor)
                                .addComponent(lNationality)
                                .addComponent(lLocation)
                        )
                        .addGroup(glPerson.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(tfPId)
                                .addComponent(tfPName)
                                .addComponent(tfHeight)
                                .addComponent(cbHairColor)
                                .addComponent(cbNationality)
                                .addComponent(pLocation)
                        )
        );
        TitledBorder tbPerson = new TitledBorder("");

        pPerson.setBorder(tbPerson);

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
                                .addComponent(lPerson)
                                .addComponent(pPerson)
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
                                .addComponent(lPerson)
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
                                .addComponent(pPerson)
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
        cbHairColor.removeAllItems();
        Arrays.stream(ru.itmo.p3114.s312198.structures.Color.values()).sorted().forEach(cbHairColor::addItem);
        cbNationality.removeAllItems();
        Arrays.stream(Country.values()).sorted().forEach(cbNationality::addItem);
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
        // Person
        lPerson.setText(resourceBundle.getString("form.groups.table.header.admin"));
        lPId.setText(resourceBundle.getString("form.person.id"));
        lPName.setText(resourceBundle.getString("form.person.name"));
        lHeight.setText(resourceBundle.getString("form.person.height"));
        lHairColor.setText(resourceBundle.getString("form.person.hair.color"));
        lNationality.setText(resourceBundle.getString("form.person.nationality"));
        lLocation.setText(resourceBundle.getString("form.person.location"));
        lLocationX.setText(resourceBundle.getString("form.person.location.x"));
        lLocationY.setText(resourceBundle.getString("form.person.location.y"));
        lLocationZ.setText(resourceBundle.getString("form.person.location.z"));
        lLocationName.setText(resourceBundle.getString("form.person.location.name"));
        refreshDropdowns(locale);
        instance.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (ACTION_SAVE): {
                logger.info("Saving group...");
                Add add = new Add();
                FieldParser fieldParser = new FieldParser();
                System.out.println(cbFormOfEducation.getToolTipText());
                try {
                    Location location = null;

                    if (!(tfLocationX.getText().trim().isEmpty() && tfLocationY.getText().trim().isEmpty()
                            && tfLocationZ.getText().trim().isEmpty())) {
                        location = new LocationBuilder()
                                .addX(fieldParser.parseLocationCoordinate(tfLocationX.getText()))
                                .addY(fieldParser.parseLocationCoordinate(tfLocationY.getText()))
                                .addZ(fieldParser.parseLocationCoordinate(tfLocationZ.getText()))
                                .addName(fieldParser.parseOptionalName(tfLocationName.getText()))
                                .toLocation();
                    }

                    Person person = null;

                    if (!tfPName.getText().trim().isEmpty()) {
                        person = new PersonBuilder()
                                .addName(fieldParser.parseName(tfPName.getText()))
                                .addHeight(fieldParser.parseNaturalNumber(tfHeight.getText()))
                                .addHairColor((ru.itmo.p3114.s312198.structures.Color) cbHairColor.getSelectedItem())
                                .addNationality((Country) cbNationality.getSelectedItem())
                                .addLocation(location)
                                .toPerson();
                    }

                    StudyGroup studyGroup = new StudyGroupBuilder()
                            .addName(fieldParser.parseName(tfName.getText()))
                            .addCoordinates(fieldParser.parseCoordinates(tfCoordinateX.getText() + " " + tfCoordinateY.getText()))
                            .addStudentsCount(fieldParser.parseNaturalNumber(tfStudentsCount.getText()))
                            .addShouldBeExpelled(fieldParser.parseNaturalNumber(tfShouldBeExpelled.getText()))
                            .addTransferredStudents(fieldParser.parseNaturalNumber(tfTransferredStudents.getText()))
                            .addFormOfEducation((FormOfEducation) cbFormOfEducation.getSelectedItem())
                            .addGroupAdmin(person)
                            .toStudyGroup();
                    System.out.println(studyGroup.toReadableString());
                    add.setComplexArgument(studyGroup);
                } catch (InvalidInputException | InputInterruptedException exception) {
                    logger.error(exception.getMessage());
                }
                try {
                    PrimaryPack primaryPack = new PrimaryPack(actor);
                    primaryPack.addCommand(new CommandRecord(add, CommandTypes.COMPLEX_COMMAND));
                    channel.writeObject(primaryPack);
                    ResponsePack responsePack = (ResponsePack) channel.readObject();
                    for (String line : responsePack.getOutput()) {
                        logger.info(line);
                    }
                } catch (IOException ioException) {
                    logger.error(ioException.getMessage());
                }

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
            tfOwner.setText(actor.getUsername());
        } else {
            tfId.setText(id.toString());
            try {
                if (channel != null && !channel.getSocket().isClosed()) {
                    PrimaryPack primaryPack = new PrimaryPack(actor);
                    primaryPack.addCommand(new CommandRecord(new Info(), CommandTypes.SIMPLE_COMMAND));
                    channel.writeObject(primaryPack);
                    ResponsePack responsePack = (ResponsePack) channel.readObject();
                    for (int i = 5; i < responsePack.getOutput().size() - 1; i++) {
                        String[] halves = responsePack.getOutput().get(i).split(": ");
                        if (halves[0].split(", ")[0].trim().equals(id.toString())) {
                            logger.info("Loading into the form: " + responsePack.getOutput().get(i));
                            StudyGroup studyGroup = (StudyGroup) new StudyGroup().fromCSV(halves[1]);
                            tfOwner.setText(halves[0].split(", ")[2].trim());
                            tfName.setText(studyGroup.getName());
                            tfCoordinateX.setText(studyGroup.getCoordinates().getX().toString());
                            tfCoordinateY.setText(studyGroup.getCoordinates().getY().toString());
                            tfCreated.setText(studyGroup.getCreationDate().toString());
                            tfStudentsCount.setText(studyGroup.getStudentsCount().toString());
                            tfShouldBeExpelled.setText(studyGroup.getShouldBeExpelled().toString());
                            tfTransferredStudents.setText(studyGroup.getTransferredStudents().toString());
                            cbFormOfEducation.setSelectedItem(studyGroup.getFormOfEducation());
                            if (studyGroup.getGroupAdmin() != null) {
                                tfPName.setText(studyGroup.getGroupAdmin().getName().toString());
                                tfHeight.setText(studyGroup.getGroupAdmin().getHeight().toString());
                                cbHairColor.setSelectedItem(studyGroup.getGroupAdmin().getHairColor());
                                cbNationality.setSelectedItem(studyGroup.getGroupAdmin().getNationality());
                                if (studyGroup.getGroupAdmin().getLocation() != null) {
                                    tfLocationX.setText(studyGroup.getGroupAdmin().getLocation().getX() + "");
                                    tfLocationY.setText(studyGroup.getGroupAdmin().getLocation().getY() + "");
                                    tfLocationZ.setText(studyGroup.getGroupAdmin().getLocation().getZ() + "");
                                    tfLocationName.setText(studyGroup.getGroupAdmin().getLocation().getName());
                                }
                            }
                        }
                    }
                }
            } catch (TransmissionException transmissionException) {
                logger.error(transmissionException.getMessage());
            }
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
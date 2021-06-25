package ru.itmo.p3114.s312198.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.commands.CommandRecord;
import ru.itmo.p3114.s312198.commands.actions.AbstractCommand;
import ru.itmo.p3114.s312198.commands.actions.simple.Info;
import ru.itmo.p3114.s312198.commands.actions.simple.RemoveById;
import ru.itmo.p3114.s312198.commands.types.CommandTypes;
import ru.itmo.p3114.s312198.exceptions.TransmissionException;
import ru.itmo.p3114.s312198.structures.StudyGroup;
import ru.itmo.p3114.s312198.transmission.CSChannel;
import ru.itmo.p3114.s312198.transmission.PrimaryPack;
import ru.itmo.p3114.s312198.transmission.ResponsePack;
import ru.itmo.p3114.s312198.transmission.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class GroupsForm extends UIForm {
    static final Logger logger = LoggerFactory.getLogger(GroupsForm.class);

    public static final String ACTION_ADD_GROUP = "actionAddNewGroup";
    public static final String ACTION_DELETE_GROUPS = "actionDeleteGroups";
    public static final String ACTION_RELOAD = "actionReloadGroups";

    private CSChannel channel;
    private User actor;

    private ResourceBundle resourceBundle;

    private final JPanel pGroups = new JPanel();
    private final JButton btnAddGroup = new JButton();
    private final JButton btnDeleteGroup = new JButton();
    private final JButton btnReloadGroups = new JButton();
    private final JPanel pButtons = new JPanel();
    private final JPanel pLanguages = new JPanel();
    private final TitledBorder tbButtons = new TitledBorder("");
    private final TitledBorder tbLanguages = new TitledBorder("");
    private JTable tGroups = null;

    private DefaultTableModel tmGroups =
            new DefaultTableModel() {
                @Override
                public Class getColumnClass(int column) {
                    // todo Customize column types if required
                    switch (column) {
                        case 0: {
                            return Boolean.class;
                        }
                        case 1:
                        case 6:
                        case 7:
                        case 8: {
                            return Long.class;
                        }
                        default: {
                            return String.class;
                        }
                    }
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0;
                }
            };

    public GroupsForm(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
    }

    public GroupsForm(Locale locale, CSChannel channel) {
        this.channel = channel;
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public CSChannel getChannel() {
        return channel;
    }

    public void setChannel(CSChannel channel) {
        this.channel = channel;
    }

    private JPanel buildLanguagePanel() {
        HashMap<String, String> languages = new HashMap<>();
        languages.put("\u0420\u0443\u0441\u0441\u043a\u0438\u0439", "ru-RU");
        languages.put("\u0411\u0435\u043b\u0430\u0440\u0443\u0441\u043a\u0430\u044f", "be-BY");
        languages.put("US English", "en-US");
        languages.put("Espa\u00f1ol", "es-ES");

        JComboBox<String> cbLanguages = new JComboBox<>();
        for (String key : languages.keySet()) {
            cbLanguages.addItem(key);
            pLanguages.add(cbLanguages);
        }
        cbLanguages.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selectedLanguage = ((JComboBox<Object>) e.getSource()).getSelectedItem();
                if (selectedLanguage != null) {
                    Locale locale = Locale.forLanguageTag(languages.get(selectedLanguage.toString()));
                    resourceBundle = getResourceBundle(locale);
                    setLocale(locale);
                }
                logger.info("Locale switched to '" + e.getItem().toString() + "'");
            }
        });
        pLanguages.setBorder(tbLanguages);

        String langName = "";
        for (String lang: languages.keySet()) {
            if (languages.get(lang).equals(resourceBundle.getLocale().toLanguageTag())) {
                langName = lang;
            }
        }
        for (int i = 0; i < cbLanguages.getItemCount(); i++) {
            if (cbLanguages.getItemAt(i).equals(langName)) {
                cbLanguages.setSelectedIndex(i);
                break;
            }
        }

        return pLanguages;
    }

    @Override
    protected void prepareGUI(Locale locale) {
        instance = new JDialog();
        instance.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Localize all texts except Table headers
        setLocale(locale);

        // Form elements
        tGroups = new JTable(tmGroups) {
//            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
//                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
//                if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
//                    c.setBackground(Color.cyan);
//                } else {
//                    c.setBackground(getBackground());
//                }
//                return c;
//            }
        };
        JScrollPane spGroups = new JScrollPane(tGroups);

        GroupLayout glGroups = new GroupLayout(pGroups);
        pGroups.setLayout(glGroups);
        glGroups.setAutoCreateGaps(true);
        glGroups.setAutoCreateContainerGaps(true);

        pButtons.add(btnAddGroup);
        btnAddGroup.setActionCommand(ACTION_ADD_GROUP);
        btnAddGroup.addActionListener(this);
        pButtons.add(btnDeleteGroup);
        btnDeleteGroup.setActionCommand(ACTION_DELETE_GROUPS);
        btnDeleteGroup.addActionListener(this);
        pButtons.add(btnReloadGroups);
        btnReloadGroups.setActionCommand(ACTION_RELOAD);
        btnReloadGroups.addActionListener(this);

        JPanel pControls = new JPanel();
        pButtons.setBorder(tbButtons);
        pControls.add(pButtons);
        pControls.add(buildLanguagePanel());

        pControls.setMaximumSize(new Dimension(480, 64));

        glGroups.setVerticalGroup(
                glGroups.createSequentialGroup()
                        .addComponent(pControls)
                        .addComponent(spGroups)
        );
        glGroups.setHorizontalGroup(
                glGroups.createParallelGroup()
                        .addComponent(pControls)
                        .addComponent(spGroups)
        );


        tGroups.setEnabled(true);
        tGroups.setFillsViewportHeight(true);
        tGroups.setFocusable(false);
        tGroups.addMouseListener(new MouseAdapter() {
            // Mouse double click opens Group for editing
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    JTable target = (JTable) mouseEvent.getSource();
                    int row = target.getSelectedRow();
                    try {
                        Integer id = (Integer) tGroups.getValueAt(row, 1);
                        editGroup(Long.valueOf(id));
                    } catch (NumberFormatException e) {
                        logger.error("Impossible to edit group {}...", e.getMessage());
                    }
                }
            }
        });

        // Columns
        tGroups.getColumnModel().getColumn(0).setPreferredWidth(32);
        tGroups.getColumnModel().getColumn(1).setMaxWidth(48);
        tGroups.getColumnModel().getColumn(2).setMaxWidth(128);
        tGroups.getColumnModel().getColumn(6).setPreferredWidth(48);
        tGroups.getColumnModel().getColumn(7).setPreferredWidth(48);
        tGroups.getColumnModel().getColumn(8).setPreferredWidth(48);
        tGroups.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        tGroups.setShowGrid(true);
        tGroups.setShowHorizontalLines(true);
        tGroups.setShowVerticalLines(true);
        tGroups.setGridColor(Color.lightGray);
        tGroups.getTableHeader().setBackground(Color.lightGray);

        spGroups.setVisible(true);
        spGroups.createVerticalScrollBar();
        spGroups.setWheelScrollingEnabled(true);
        instance.add(pGroups);
        instance.setLocationRelativeTo(null);
        instance.pack();

    }

    @Override
    public void setLocale(Locale locale) {
        resourceBundle = getResourceBundle(locale);
        if (instance != null) {
            instance.setTitle(resourceBundle.getString("form.groups.title"));
        }
        btnAddGroup.setText(resourceBundle.getString("form.groups.buttons.add"));
        btnDeleteGroup.setText(resourceBundle.getString("form.groups.buttons.delete"));
        btnReloadGroups.setText(resourceBundle.getString("form.groups.buttons.reload"));

        tbButtons.setTitle(resourceBundle.getString("form.groups.control.actions"));
        tbLanguages.setTitle(resourceBundle.getString("form.groups.control.lang"));
        tmGroups.setDataVector(retrieveGroupsFromServer(), getLocalizedTableHeaders(locale));
        instance.pack();
    }

    private Object[] getLocalizedTableHeaders(Locale locale) {
        ArrayList<String> header = new ArrayList<>();
        header.add("");
        header.add(resourceBundle.getString("form.groups.table.header.id"));
        header.add(resourceBundle.getString("form.groups.table.header.name"));
        header.add(resourceBundle.getString("form.groups.table.header.owner"));
        header.add(resourceBundle.getString("form.groups.table.header.coordinates"));
        header.add(resourceBundle.getString("form.groups.table.header.created"));
        header.add(resourceBundle.getString("form.groups.table.header.students.count"));
        header.add(resourceBundle.getString("form.groups.table.header.students.expell"));
        header.add(resourceBundle.getString("form.groups.table.header.students.transfer"));
        header.add(resourceBundle.getString("form.groups.table.header.form"));
        header.add(resourceBundle.getString("form.groups.table.header.admin"));
        return header.toArray();
    }

    private Object[][] transform(ArrayList<ArrayList<Object>> arrayLists) {
        if (arrayLists == null || arrayLists.size() == 0) {
            return null;
        } else {
            Object[][] arrays = new Object[arrayLists.size()][arrayLists.get(0).size()];
            for (int i = 0; i < arrayLists.size(); i++) {
                for (int j = 0; j < arrayLists.get(0).size(); j++) {
                    arrays[i][j] = arrayLists.get(i).get(j);
                }
            }
            return arrays;
        }
    }

    /**
     * Requests groups data from the server and returns it as a two-dimensional array
     *
     * @return Two-dimensional array with gruops data (each row is a group, each column is a field)
     */
    private Object[][] retrieveGroupsFromServer() {
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        if (channel != null && !channel.getSocket().isClosed()) {
            try {
                PrimaryPack primaryPack = new PrimaryPack(actor);
                primaryPack.addCommand(new CommandRecord(new Info(), CommandTypes.SIMPLE_COMMAND));
                channel.writeObject(primaryPack);
                ResponsePack responsePack = (ResponsePack) channel.readObject();
                for (int i = 5; i < responsePack.getOutput().size() - 1; i++) {
                    ArrayList<Object> line = new ArrayList<>();
                    String[] halves = responsePack.getOutput().get(i).split(": ");
                    logger.info("Loading into the table: " + responsePack.getOutput().get(i));
                    line.add(false);
                    StudyGroup studyGroup = (StudyGroup) new StudyGroup().fromCSV(halves[1]);
                    line.add(Integer.parseInt(halves[0].trim().split(", ")[0].trim()));
                    line.add(studyGroup.getName());
                    line.add(halves[0].trim().split(", ")[2].trim());
                    line.add("(" + studyGroup.getCoordinates().getX() + "; " +
                            studyGroup.getCoordinates().getY() + ")");
                    line.add(studyGroup.getCreationDate());
                    line.add(studyGroup.getStudentsCount());
                    line.add(studyGroup.getShouldBeExpelled());
                    line.add(studyGroup.getTransferredStudents());
                    line.add(studyGroup.getFormOfEducation());
                    line.add(studyGroup.getGroupAdmin() == null ? "-" : studyGroup.getGroupAdmin().getName());

                    data.add(line);
                }
            } catch (TransmissionException transmissionException) {
                logger.error(transmissionException.getMessage());
            }
        }
        return transform(data);
    }

    private void editGroup(Long id) {
        GroupForm groupForm = new GroupForm(resourceBundle.getLocale(), channel);
        groupForm.loadById(id);
        groupForm.show();
        groupForm.hide();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (ACTION_ADD_GROUP): {
                logger.info("Adding a new group...");
                // Show "Group" dialog with empty fields
                GroupForm groupForm = new GroupForm(resourceBundle.getLocale(), channel);
                groupForm.setActor(actor);
                groupForm.loadById(-1L);
                groupForm.show();

                tmGroups.setDataVector(retrieveGroupsFromServer(), getLocalizedTableHeaders(resourceBundle.getLocale()));
                groupForm.hide();
                break;
            }
            case (ACTION_DELETE_GROUPS): {
                logger.info("Deleting group by ID...");
                ArrayList<Integer> rowsToDelete = new ArrayList<>();
                for (int row = 0; row < tGroups.getRowCount(); row++) {
                    if ((Boolean) tGroups.getModel().getValueAt(tGroups.convertRowIndexToModel(row), 0)) {
                        rowsToDelete.add((Integer) tGroups.getModel().getValueAt(tGroups.convertRowIndexToModel(row), 1));
                    }
                }

                // 1.Ask for confirmation (JOptionPane with "yes/no" options)
                int result = JOptionPane.showConfirmDialog(instance,
                        resourceBundle.getString("form.groups.delete"),
                        resourceBundle.getString("form.groups.delete.confirm.title"),
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    PrimaryPack primaryPack = new PrimaryPack(actor);
                    for (Integer row : rowsToDelete) {
                        RemoveById remove = new RemoveById();
                        remove.getArguments().add(row.toString());
                        primaryPack.addCommand(new CommandRecord(remove, CommandTypes.SIMPLE_COMMAND));
                    }
                    try {
                        channel.writeObject(primaryPack);
                        ResponsePack responsePack = (ResponsePack) channel.readObject();
                        logger.info("Execution state: " + responsePack.allowed().toString());
                    } catch (TransmissionException transmissionException) {
                        logger.error(transmissionException.getMessage());
                    }
                    tmGroups.setDataVector(retrieveGroupsFromServer(), getLocalizedTableHeaders(resourceBundle.getLocale()));
                }

                break;
            }
            case (ACTION_RELOAD): {
                logger.info("Reloading groups data from the server...");
                tmGroups.setDataVector(retrieveGroupsFromServer(), getLocalizedTableHeaders(resourceBundle.getLocale()));
                break;
            }
        }

    }
}

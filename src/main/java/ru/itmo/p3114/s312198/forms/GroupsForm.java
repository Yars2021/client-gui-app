package ru.itmo.p3114.s312198.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                    int column = target.getSelectedColumn();
                    try {
                        editGroup(Long.parseLong((String) tGroups.getValueAt(row, 1)));
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
        tmGroups.setDataVector(getTableData(), getLocalizedTableHeaders(locale));
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

    /**
     * Requests groups data from the server and returns it as a two-dimensional array
     *
     * @return Two-dimensional array with gruops data (each row is a group, each column is a field)
     */
    private Object[][] retrieveGroupsFromServer() {
        Object[][] data = getTableData(); // todo replace with actual code to retrieve data from the server
        return data;
    }

    // todo: remove when the method above is implemented
    private Object[][] getTableData() {
        logger.info("Retrieving data from the server");
        Object[][] data = {
                {false, 1, "1", "1", "1", "1", 1, 1, 1, "1", "Admin"},
                {false, 2, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 3, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 4, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 5, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 6, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 7, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 8, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 9, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 10, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 11, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 12, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 13, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 14, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 15, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 16, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 17, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 18, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 19, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 20, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 21, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 22, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 23, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 24, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 25, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 26, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 27, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 28, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 29, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"},
                {false, 30, "2", "2", "2", "2", 2, 2, 2, "2", "Admin"}
        };
        return data;
    }

    private void editGroup(Long id) {
        GroupForm groupForm = new GroupForm(resourceBundle.getLocale());
        groupForm.loadById(id);
        groupForm.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (ACTION_ADD_GROUP): {
                logger.info("Adding a new group...");
                // Show "Group" dialog with empty fields
                GroupForm groupForm = new GroupForm(resourceBundle.getLocale());
                groupForm.loadById(-1L);
                groupForm.show();

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
                    // 2. Confirmed - call "DeleteGroup" command on the server
                    // todo Call Server.deleteGroup(id) for each id from the rowsToDelete collection

                    // 3. Reload groups list and update GroupsTable:
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

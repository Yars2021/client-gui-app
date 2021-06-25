package ru.itmo.p3114.s312198.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.authentication.Login;
import ru.itmo.p3114.s312198.authentication.Register;
import ru.itmo.p3114.s312198.exceptions.TransmissionException;
import ru.itmo.p3114.s312198.io.GUIReaderAdapter;
import ru.itmo.p3114.s312198.transmission.AuthenticationRequest;
import ru.itmo.p3114.s312198.transmission.AuthenticationResponse;
import ru.itmo.p3114.s312198.transmission.CSChannel;
import ru.itmo.p3114.s312198.transmission.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginForm extends UIForm {
    static final Logger logger = LoggerFactory.getLogger(LoginForm.class);

    public static final String ACTION_LOGIN = "actionLogin";
    public static final String ACTION_REGISTER = "actionRegister";
    public static final String ACTION_CANCEL = "actionCancel";
    private static final String EMPTY_STRING = "";

    private CSChannel channel;
    private User actor;

    private ResourceBundle resourceBundle = null;
    // Form elements
    private final JTabbedPane tabs = new JTabbedPane();
    private final JPanel pLogin = new JPanel();
    private final JPanel pRegistration = new JPanel();
    private final JLabel lUser = new JLabel();
    private final JLabel lPassword = new JLabel();

    private final JLabel lRUser = new JLabel();
    private final JLabel lRPassword = new JLabel();
    private final JLabel lRPassword2 = new JLabel();
    private final JTextField tfUser = new JTextField();
    private final JTextField tfPassword = new JPasswordField();
    private final JTextField tfRUser = new JTextField();
    private final JTextField tfRPassword = new JPasswordField();
    private final JTextField tfRPassword2 = new JPasswordField();
    private final JButton btnLogin = new JButton();
    private final JButton btnRegister = new JButton();
    private final JButton btnCancel = new JButton();
    private final JButton btnCancelRegistration = new JButton();

    public LoginForm(Locale locale) {
        try {
        channel = new CSChannel(new Socket("localhost", 7035));
        resourceBundle = getResourceBundle(locale);
        prepareGUI(resourceBundle.getLocale());
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
        }
    }

    public void setChannel(CSChannel channel) {
        this.channel = channel;
    }

    public CSChannel getChannel() {
        return channel;
    }

    public User getActor() {
        return actor;
    }

    @Override
    protected void prepareGUI(Locale locale) {
        instance = new JDialog();
        instance.setModal(true);
        // "Login" tab
        btnLogin.setActionCommand(ACTION_LOGIN);
        btnLogin.addActionListener(this);
        btnCancel.setActionCommand(ACTION_CANCEL);
        btnCancel.addActionListener(this);

        JPanel pLoginButtons = new JPanel();
        pLoginButtons.add(btnLogin);
        pLoginButtons.add(btnCancel);

        GroupLayout glLogin = new GroupLayout(pLogin);
        pLogin.setLayout(glLogin);
        glLogin.setAutoCreateGaps(true);
        glLogin.setAutoCreateContainerGaps(true);
        glLogin.setVerticalGroup(
                glLogin.createSequentialGroup()
                        .addGroup(glLogin.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lUser)
                                .addComponent(tfUser)
                        )
                        .addGroup(glLogin.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lPassword)
                                .addComponent(tfPassword)
                        )
                        .addGroup(glLogin.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pLoginButtons)
                        )
        );
        glLogin.setHorizontalGroup(
                glLogin.createSequentialGroup()
                        .addGroup(glLogin.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lUser)
                                .addComponent(lPassword)
                        )
                        .addGroup(glLogin.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(tfUser)
                                .addComponent(tfPassword)
                                .addComponent(pLoginButtons, GroupLayout.Alignment.TRAILING)
                        )
        );

        tabs.addTab("XXX", pLogin);


        // "Register" tab
        btnRegister.setActionCommand(ACTION_REGISTER);
        btnRegister.addActionListener(this);
        btnCancelRegistration.setActionCommand(ACTION_CANCEL);
        btnCancelRegistration.addActionListener(this);

        JPanel pRegisterButtons = new JPanel();
        pRegisterButtons.add(btnRegister);
        pRegisterButtons.add(btnCancelRegistration);

        GroupLayout glRegistration = new GroupLayout(pRegistration);
        pRegistration.setLayout(glRegistration);
        glRegistration.setAutoCreateGaps(true);
        glRegistration.setAutoCreateContainerGaps(true);
        glRegistration.setVerticalGroup(
                glRegistration.createSequentialGroup()
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lRUser)
                                .addComponent(tfRUser)
                        )
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lRPassword)
                                .addComponent(tfRPassword)
                        )
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lRPassword2)
                                .addComponent(tfRPassword2)
                        )
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pRegisterButtons)
                        )
        );
        glRegistration.setHorizontalGroup(
                glRegistration.createSequentialGroup()
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lRUser)
                                .addComponent(lRPassword)
                                .addComponent(lRPassword2)
                        )
                        .addGroup(glRegistration.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(tfRUser)
                                .addComponent(tfRPassword)
                                .addComponent(tfRPassword2)
                                .addComponent(pRegisterButtons, GroupLayout.Alignment.TRAILING)
                        )
        );

        tabs.addTab("YYY", pRegistration);

        instance.add(tabs);
        setLocale(locale);
        instance.setLocationRelativeTo(null);
        instance.pack();
    }

    @Override
    public void setLocale(Locale locale) {
        resourceBundle = getResourceBundle(locale);

        this.instance.setTitle(resourceBundle.getString("form.login.title"));
        lUser.setText(resourceBundle.getString("form.login.username"));
        lPassword.setText(resourceBundle.getString("form.login.password"));
        btnLogin.setText(resourceBundle.getString("form.login.buttons.enter"));
        btnCancel.setText(resourceBundle.getString("form.login.buttons.cancel"));
        lRUser.setText(resourceBundle.getString("form.login.username"));
        lRPassword.setText(resourceBundle.getString("form.login.password"));
        lRPassword2.setText(resourceBundle.getString("form.login.password"));
        btnRegister.setText(resourceBundle.getString("form.login.buttons.register"));
        btnCancelRegistration.setText(resourceBundle.getString("form.login.buttons.cancel"));


        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (SwingUtilities.isDescendingFrom(btnLogin, tabs.getComponentAt(i))) {
                tabs.setTitleAt(i, resourceBundle.getString("form.login.tab.login"));
            }
            if (SwingUtilities.isDescendingFrom(btnRegister, tabs.getComponentAt(i))) {
                tabs.setTitleAt(i, resourceBundle.getString("form.login.tab.registration"));
            }
        }
        instance.pack();
    }

    private boolean checkLogin(CSChannel channel, String user, String credentials) {
        if (channel != null) {
            logger.info(channel.toString());
            try {
                GUIReaderAdapter guiReaderAdapter = new GUIReaderAdapter();
                guiReaderAdapter.push(user);
                guiReaderAdapter.push(credentials);
                AuthenticationRequest authenticationRequest = new Login().formRequest(guiReaderAdapter, Boolean.TRUE);
                channel.writeObject(authenticationRequest);
                AuthenticationResponse authenticationResponse = (AuthenticationResponse) channel.readObject();
                logger.info(authenticationResponse.getServerMessage());
                actor = authenticationResponse.getUser();
                return authenticationResponse.allowed();
            } catch (TransmissionException transmissionException) {
                logger.error(transmissionException.getMessage());
            }
        }
        return false;
    }

    private boolean register(CSChannel channel, String user, String credentials) {
        if (channel != null) {
            try {
                GUIReaderAdapter guiReaderAdapter = new GUIReaderAdapter();
                guiReaderAdapter.push(user);
                guiReaderAdapter.push(credentials);
                guiReaderAdapter.push(credentials);
                AuthenticationRequest authenticationRequest = new Register().formRequest(guiReaderAdapter, Boolean.TRUE);
                channel.writeObject(authenticationRequest);
                AuthenticationResponse authenticationResponse = (AuthenticationResponse) channel.readObject();
                logger.info(authenticationResponse.getServerMessage());
                actor = authenticationResponse.getUser();
                return authenticationResponse.allowed();
            } catch (TransmissionException transmissionException) {
                logger.error(transmissionException.getMessage());
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UIManager.put("OptionPane.okButtonText", resourceBundle.getString("form.login.buttons.ok"));
        switch (e.getActionCommand()) {
            case (ACTION_LOGIN): {
                // Collect fields data and perform "login" command on the server
                logger.info("Logging in user '{}'...", tfUser.getText());

                // Disable all controls on the form to avoid serial clicks
                instance.setEnabled(false);
                //

                if (checkLogin(channel, tfUser.getText(), tfPassword.getText())) {
                    // Success - clear form data, enable form controls, hide form and open main (Groups) window
                    hide();
                    GroupsForm groupsForm = new GroupsForm(resourceBundle.getLocale(), channel);
                    groupsForm.setActor(actor);
                    groupsForm.show();
                } else {
                    // Fail - show error
                    JOptionPane.showMessageDialog(instance,
                            resourceBundle.getString("form.login.error.incorrect.credentials"),
                            resourceBundle.getString("form.login.error.title"),
                            JOptionPane.ERROR_MESSAGE);
                }
                clearFields();
                instance.setEnabled(true);
                break;
            }
            case (ACTION_REGISTER): {
                // Collect fields data and perform "register" command on the server
                if (!tfRPassword.getText().equals(tfRPassword2.getText())) {
                    JOptionPane.showMessageDialog(instance,
                            resourceBundle.getString("form.login.error.passwords.are.not.identical"),
                            resourceBundle.getString("form.login.error.title"),
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    logger.info("Registering user '{}'...", tfRUser.getText());

                    if (register(channel, tfUser.getText(), tfPassword.getText())) {
                        // Success - clear form data, enable form controls, hide form and open main (Groups) window
                        hide();
                        GroupsForm groupsForm = new GroupsForm(resourceBundle.getLocale(), channel);
                        groupsForm.setActor(actor);
                        groupsForm.show();
                    } else {
                        // Fail - show error
                        JOptionPane.showMessageDialog(instance,
                                resourceBundle.getString("form.login.error.incorrect.credentials"),
                                resourceBundle.getString("form.login.error.title"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                    clearFields();
                    instance.setEnabled(true);
                    // 1. Disable all controls on the form
                    // 2. Send "Registration" action to the server
                    // 3. Process server response
                    // 3.1. Fail - show error and enable form controls
                    // 3.2. Success - clear form data, enable form controls, hide form and open main window
                }
                break;
            }
            case (ACTION_CANCEL): {
                // Clear Login/Register forms data and close dialog
                logger.info("Cancelling...");
                hide();
                clearFields();
                break;
            }
            default: {
                // DO Nothing
            }
        }
    }

    private void clearFields() {
        tfUser.setText(EMPTY_STRING);
        tfRUser.setText(EMPTY_STRING);
        tfPassword.setText(EMPTY_STRING);
        tfRPassword.setText(EMPTY_STRING);
        tfRPassword2.setText(EMPTY_STRING);
    }
}

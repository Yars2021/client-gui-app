package ru.itmo.p3114.s312198;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.p3114.s312198.forms.LoginForm;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Locale;

public class GuiClient {
    static final Logger logger = LoggerFactory.getLogger(GuiClient.class);
    private static LoginForm loginForm;
    private static Locale applicationLocale = Locale.US;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("GUI");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel app = new JPanel();

        JPanel pLanguages = new JPanel();
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
                    applicationLocale = Locale.forLanguageTag(languages.get(selectedLanguage.toString()));
                }
                logger.info("Locale switched to '" + e.getItem().toString() + "'");
            }
        });
        app.add(pLanguages);

        JPanel buttons = new JPanel();
        JButton btnLogin = new JButton("Login");
        btnLogin.setActionCommand("showLoginForm");
        btnLogin.addActionListener(e -> {
            if ("showLoginForm".equals(e.getActionCommand())) {
                btnLogin.setEnabled(false);
                loginForm.setLocale(applicationLocale);
                loginForm.show();
            }
        });

        buttons.add(btnLogin);

        app.add(buttons);
        jFrame.add(app);

        jFrame.setVisible(true);
        jFrame.pack();

        // Language selection bar
        Object selectedLanguage = cbLanguages.getSelectedItem();

        if (selectedLanguage != null) {
            applicationLocale = Locale.forLanguageTag(languages.get(selectedLanguage.toString()));
        }

        if (cbLanguages.getSelectedItem() != null) {
            loginForm = new LoginForm(applicationLocale);
        }
    }
}

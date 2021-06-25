package ru.itmo.p3114.s312198.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class UIForm implements ActionListener {
    protected JDialog instance;
    protected ResourceBundle resourceBundle;

    public UIForm() {
        this.instance = null;
        this.resourceBundle = null;
    }

    public void show() {
        if (this.instance != null) {
            this.instance.setVisible(true);
            this.instance.setEnabled(true);
        }
    }

    public void hide() {
        if (this.instance != null) {
            this.instance.setVisible(false);
            this.instance.setEnabled(false);
        }
    }

    protected abstract void prepareGUI(Locale locale);

    public abstract void setLocale(Locale locale);

    /**
     * Dedicated method where inherited code can implement its data loading from outside (File, DB, Service, etc).
     */
    public void loadById(Long id) {
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    protected ResourceBundle getResourceBundle(Locale locale) {
        Locale _locale;
        if (locale != null) {
            _locale = locale;
        } else {
            _locale = Locale.US;
        }
        return  ResourceBundle.getBundle("s312198", _locale);
    }
}

package com.kota65535.intellij.plugin.keymap.exporter;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;

public class ExportKeymapDialog extends DialogWrapper {
    private JPanel myRootPanel;
    private JComboBox keymapComboBox;
    private TextFieldWithBrowseButton savedDirectoryName;
    private JTextField savedFileName;
    private final Project myProject;
    private KeymapManagerEx manager = KeymapManagerEx.getInstanceEx();

    public ExportKeymapDialog(@Nullable Project project) {
        super(project, true);

        myProject = project;

        // List all keymaps
        Arrays.stream(manager.getAllKeymaps()).forEach( m -> keymapComboBox.addItem(m));

        // Open directory chooser when the button clicked
        savedDirectoryName.addBrowseFolderListener("Save as", "", null,
                FileChooserDescriptorFactory.createSingleFolderDescriptor());

        // If keymap combobox is changed, set filename textbox.
        keymapComboBox.addActionListener( e -> {
            JComboBox comboBox = (JComboBox)e.getSource();
            setFileNameText(comboBox);
        });

        // Init filename textbox
        setFileNameText(keymapComboBox);

        init();
    }

    private void setFileNameText(JComboBox comboBox) {
        String text = String.format("keymap-%s.xlsx", comboBox.getSelectedItem());
        savedFileName.setText(text);
    }

    @Override
    protected JComponent createCenterPanel() {
        return myRootPanel;
    }

    @Override
    protected void doOKAction() {
        if ( StringUtil.isEmpty(getSavedDir()) ) {
            Messages.showErrorDialog("Choose 'To Directory' where the keymap file is saved.", "Export Keymap");
            return;
        }
        if (getOKAction().isEnabled()) {
            close(OK_EXIT_CODE);
        }
    }

    public Keymap getSelectedKeymap() {
        return (Keymap) keymapComboBox.getSelectedItem();
    }

    public String getSavedDir() {
        return savedDirectoryName.getText();
    }

    public String getSavedFileName() {
        return savedFileName.getText();
    }
}

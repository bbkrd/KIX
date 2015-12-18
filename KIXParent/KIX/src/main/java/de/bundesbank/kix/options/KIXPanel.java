/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.kix.options;

import static de.bundesbank.kix.options.KIXOptionsPanelController.KIX2_DEFAULT_METHOD;
import static de.bundesbank.kix.options.KIXOptionsPanelController.KIXE_DEFAULT_METHOD;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.NbPreferences;

final class KIXPanel extends javax.swing.JPanel {

    private final KIXOptionsPanelController controller;

    KIXPanel(KIXOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        defaultModelMainPanel = new javax.swing.JPanel();
        defaultModelPanel = new javax.swing.JPanel();
        kix2DefaultMethodLabel = new javax.swing.JLabel();
        kix2DefaultMethod = new javax.swing.JComboBox();
        kixeDefaultMethodLabel = new javax.swing.JLabel();
        kixeDefaultMethod = new javax.swing.JComboBox();
        defaultModelFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        defaultModelMainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), org.openide.util.NbBundle.getMessage(KIXPanel.class, "KIXPanel.defaultModelMainPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP)); // NOI18N
        defaultModelMainPanel.setLayout(new javax.swing.BoxLayout(defaultModelMainPanel, javax.swing.BoxLayout.LINE_AXIS));

        defaultModelPanel.setMaximumSize(new java.awt.Dimension(300, 100));
        defaultModelPanel.setPreferredSize(new java.awt.Dimension(200, 100));
        defaultModelPanel.setLayout(new java.awt.GridLayout(4, 0));

        org.openide.awt.Mnemonics.setLocalizedText(kix2DefaultMethodLabel, org.openide.util.NbBundle.getMessage(KIXPanel.class, "KIXPanel.kix2DefaultMethodLabel.text")); // NOI18N
        defaultModelPanel.add(kix2DefaultMethodLabel);

        kix2DefaultMethod.setModel(new DefaultComboBoxModel(UnchainingMethod.values()));
        defaultModelPanel.add(kix2DefaultMethod);

        org.openide.awt.Mnemonics.setLocalizedText(kixeDefaultMethodLabel, org.openide.util.NbBundle.getMessage(KIXPanel.class, "KIXPanel.kixeDefaultMethodLabel.text")); // NOI18N
        defaultModelPanel.add(kixeDefaultMethodLabel);

        kixeDefaultMethod.setModel(new DefaultComboBoxModel(UnchainingMethod.values()));
        defaultModelPanel.add(kixeDefaultMethod);

        defaultModelMainPanel.add(defaultModelPanel);
        defaultModelMainPanel.add(defaultModelFiller);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(defaultModelMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(defaultModelMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        String kix2_method = NbPreferences.forModule(KIXOptionsPanelController.class).get(KIX2_DEFAULT_METHOD, UnchainingMethod.PRAGMATIC.toString());
        kix2DefaultMethod.setSelectedItem(UnchainingMethod.valueOf(kix2_method));

        String kixe_method = NbPreferences.forModule(KIXOptionsPanelController.class).get(KIXE_DEFAULT_METHOD, UnchainingMethod.PURISTIC.toString());
        kixeDefaultMethod.setSelectedItem(UnchainingMethod.valueOf(kixe_method));
    }

    void store() {
        NbPreferences.forModule(KIXOptionsPanelController.class).put(KIX2_DEFAULT_METHOD, kix2DefaultMethod.getSelectedItem().toString());
        NbPreferences.forModule(KIXOptionsPanelController.class).put(KIXE_DEFAULT_METHOD, kixeDefaultMethod.getSelectedItem().toString());
    }

    boolean valid() {
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler defaultModelFiller;
    private javax.swing.JPanel defaultModelMainPanel;
    private javax.swing.JPanel defaultModelPanel;
    private javax.swing.JComboBox kix2DefaultMethod;
    private javax.swing.JLabel kix2DefaultMethodLabel;
    private javax.swing.JComboBox kixeDefaultMethod;
    private javax.swing.JLabel kixeDefaultMethodLabel;
    // End of variables declaration//GEN-END:variables
}
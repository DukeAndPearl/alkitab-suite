/* This work has been placed into the public domain. */

package kiyut.alkitab.installer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import kiyut.alkitab.util.ComponentOrientationSupport;
import kiyut.alkitab.util.IOUtilities;
import kiyut.swing.dialog.EscapeDialog;
import org.crosswire.jsword.book.sword.SwordBookPath;

/**
 * BookInstallerPane
 * 
 * @author Tonny Kohar <tonny.kohar@gmail.com>
 */
public class BookInstallerPane extends javax.swing.JPanel {
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());
    
    /** Flag for whether any book is installed during a dialog popup */
    protected boolean bookInstalled = false;
    
    /** Creates new BookInstallerPane */
    public BookInstallerPane() {
        initComponents();
        initCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        srcField = new javax.swing.JTextField();
        srcBrowseButton = new javax.swing.JButton();
        destComboBox = new javax.swing.JComboBox<File>();
        overwriteCheckBox = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        progressLabel = new javax.swing.JLabel();
        installButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel3.setText(bundle.getString("CTL_RawZipInfo.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(bundle.getString("CTL_Source.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText(bundle.getString("CTL_Destination.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 6);
        jPanel1.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(srcField, gridBagConstraints);

        srcBrowseButton.setText(bundle.getString("CTL_Browse.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(srcBrowseButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPanel1.add(destComboBox, gridBagConstraints);

        overwriteCheckBox.setSelected(true);
        overwriteCheckBox.setText(bundle.getString("CTL_Overwrite.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel1.add(overwriteCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jPanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jSeparator1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        progressLabel.setText("   "); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        jPanel2.add(progressLabel, gridBagConstraints);

        installButton.setText(bundle.getString("CTL_Install.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        jPanel2.add(installButton, gridBagConstraints);

        closeButton.setText(bundle.getString("CTL_Close.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel2.add(closeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JComboBox<File> destComboBox;
    private javax.swing.JButton installButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox overwriteCheckBox;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JButton srcBrowseButton;
    private javax.swing.JTextField srcField;
    // End of variables declaration//GEN-END:variables

    protected void initCustom() {
        DefaultComboBoxModel<File> model = (DefaultComboBoxModel<File>)destComboBox.getModel();
        model.removeAllElements();
        
        File[] paths = SwordBookPath.getSwordPath();
        for (int i=0; i<paths.length; i++) {
            model.addElement(paths[i]);
        }
        
        srcBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                File file = showFileChooser();
                if (file == null) { return; }
                srcField.setText(file.toString());
            }
        });
        
        installButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt)  {
               installBook();
           }
        });
        
        closeButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt)  {
               Component comp = SwingUtilities.getWindowAncestor(BookInstallerPane.this);
               if (comp != null) {
                   comp.setVisible(false);
               }
           }
        });
    }
    
    /** Show as Dialog and handle the installation
     * @param owner {@code Component}
     */
    public void showDialog(Component owner) {
        JDialog dialog = null;
        
        if (owner != null) {
            Component comp = owner;
            if (!(comp instanceof Frame || comp instanceof Dialog)) {
                comp = SwingUtilities.getWindowAncestor(owner);
            }
            if (comp instanceof Frame) {
                dialog = new EscapeDialog((Frame)comp, bundle.getString("CTL_Title.Text"), true);
            } else if (comp instanceof Dialog) {
                dialog = new EscapeDialog((Dialog)comp, bundle.getString("CTL_Title.Text"), true);
            } 
        }
        
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(12,12,12,12));
        pane.add(this,BorderLayout.CENTER);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(pane,BorderLayout.CENTER);
        dialog.pack();
        if (owner != null) {
            dialog.setLocationRelativeTo(owner);
        }

        ComponentOrientationSupport.applyComponentOrientation(dialog);

        dialog.setVisible(true);
    }
    
    /** 
     * Return whether any book is installed sucessfully during the dialog shown.
     * @return true or false
     */
    public boolean isBookInstalled() {
        return bookInstalled;
    }
    
    /** Display JFileChooser
     * @return the selected file or null
     */
    protected File showFileChooser() {
        File file = null;
        
        JFileChooser fc = IOUtilities.getFileChooser();
        fc.setFileFilter(IOUtilities.getZipFileFilter());
        int choice = fc.showOpenDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return file;
        }
       
        file = fc.getSelectedFile();
        IOUtilities.setUserDir(file);
        
        return file;
    }
    
    /** Return source file (zip file of raw books)
     * @return file or null
     */
    public File getSourceFile() {
        File file = new File(srcField.getText());
        if (!file.isFile()) {
            file = null;
        }
        return file;
    }
    
    /** Return destination folder (sword path to install the raw zip to)
     * @return file or null
     */
    public File getDestinationFile() {
        return (File)destComboBox.getSelectedItem();
    }
    
    /** Install or extracting the book raw zip file */
    protected void installBook() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                installButton.setEnabled(false);
                closeButton.setEnabled(false);
                updateProgress(null);
                try {
                    boolean valid = validateInstall();
                    if (!valid) {
                        return;
                    }
                    
                    extractZip();
                    JOptionPane.showMessageDialog(BookInstallerPane.this, bundle.getString("MSG_InstallFinish.Text"), bundle.getString("CTL_Title.Text"), JOptionPane.INFORMATION_MESSAGE);
                    srcField.setText(null);
                    bookInstalled = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BookInstallerPane.this, ex.getLocalizedMessage(), bundle.getString("CTL_Title.Text"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    updateProgress(null);
                    installButton.setEnabled(true);
                    closeButton.setEnabled(true);
                    setCursor(cursor);
                }
            }
        });
    }
    
    /** Just some simple validation
     * eg: check the content of zip file for folder called modules and mods.d,
     * and other simple check
     */
    protected boolean validateInstall() {
        File srcFile = getSourceFile();
        if (srcFile == null) {
            JOptionPane.showMessageDialog(BookInstallerPane.this, bundle.getString("MSG_InvalidSourceFile.Text"), bundle.getString("CTL_Title.Text"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // check for modules and mods.d
        ZipFile zipFile = null;
        try {
            boolean modulesDir = false;
            boolean modsDir = false;
            
            zipFile = new ZipFile(srcFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                updateProgress(bundle.getString("MSG_Checking.Text") + " " + entry.getName());
                
                if (!modulesDir) {
                    if (entry.getName().startsWith("modules")) {
                        modulesDir = true;
                    }
                }
                
                if (!modsDir) {
                    if (entry.getName().startsWith("mods.d")) {
                        modsDir = true;
                    }
                }
                
                if (modulesDir && modsDir) {
                    break;
                }
            }
            
            if (!modulesDir || !modsDir) {
                throw new RuntimeException("Invalid Sword Module raw zip file."); // NOI18N
            }
            
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(BookInstallerPane.this, bundle.getString("MSG_InvalidSwordModuleSourceFile.Text"), bundle.getString("CTL_Title.Text"), JOptionPane.ERROR_MESSAGE);
            return false;
        }  finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception ex) {
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.log(Level.WARNING,ex.getMessage());
            }
        }
        return true;
    }
    
    /** Install or extracting the book raw zip file */
    protected void extractZip() throws IOException {
        File srcFile = getSourceFile();
        File dstFile = getDestinationFile();
        
        // for testing only
        //File dstFile = new File("/home/tonny/tmp/test/books/");  
        //System.out.println("src: " + srcFile);
        //System.out.println("dst: " + dstFile);

        // install process or zip extraction
        
        boolean overwrite = overwriteCheckBox.isSelected();
        Enumeration<? extends ZipEntry> entries;
        ZipFile zipFile = null;
        byte[] buffer = new byte[1024];
        int len;

        try {
            zipFile = new ZipFile(srcFile);
            entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                File dir = new File(dstFile,entry.getName()).getParentFile();
                if (dir != null) {
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                }
                
                File dstEntryFile = new File(dstFile,entry.getName());
                //System.out.println("Extracting file: " + entry.getName());
                // use dstEntryFile.getName to get shorter nmae, the zipEntry is very long because it include the parent path
                updateProgress(bundle.getString("MSG_Extracting.Text") + " " + dstEntryFile.getName()); 
                
                if (!overwrite) {
                    if (dstEntryFile.exists()) {
                        Object[] args = {dstEntryFile.getName()};
                        String msg = MessageFormat.format(bundle.getString("MSG_FileExists.Text"), args);
                        int choice = JOptionPane.showConfirmDialog(BookInstallerPane.this, msg, bundle.getString("CTL_Title.Text"), JOptionPane.YES_NO_OPTION);
                        if (choice != JOptionPane.YES_OPTION) {
                            // skip the file
                            continue;
                        }
                    }
                }
                
                
                InputStream in = zipFile.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dstEntryFile));
                
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            }
            
            
            updateProgress(bundle.getString("MSG_Extracting.Text") + " " + bundle.getString("MSG_InstallFinish.Text"));
            
        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
        }
    }
    
    private void updateProgress(String text) {
        if (text == null) {
            text = "   ";
        }
        progressLabel.setText(text);
        progressLabel.paintImmediately(0, 0, progressLabel.getWidth(), progressLabel.getHeight());
    }
}

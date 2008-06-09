/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import kiyut.swing.dialog.DialogESC;

/**
 * Display the OSIS and HTML source for the the book
 * 
 */
public class SourceCodePane extends javax.swing.JPanel {
    
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());    
    
    /** Creates new SourceCodePane */
    public SourceCodePane() {
        initComponents();
        initCustom();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        rawTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        osisTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        htmlTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(rawTextArea);

        jTabbedPane1.addTab(bundle.getString("CTL_Raw.Text"), jScrollPane1); // NOI18N

        jScrollPane2.setViewportView(osisTextArea);

        jTabbedPane1.addTab(bundle.getString("CTL_OSIS.Text"), jScrollPane2); // NOI18N

        jScrollPane3.setViewportView(htmlTextArea);

        jTabbedPane1.addTab(bundle.getString("CTL_HTML.Text"), jScrollPane3); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea htmlTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea osisTextArea;
    private javax.swing.JTextArea rawTextArea;
    // End of variables declaration//GEN-END:variables
    
    protected void initCustom() {
        int fontSize = 10;
        try {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            fontSize = (int)Math.round((double)fontSize * dpi / 72.0);
            
            /*Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            if (size.width >= 1280 || size.height >= 1024) {
                fontSize = 13;
            }*/
        } catch (Exception ex) {
            // do nothing
        }
        
        Font font = new Font("Monospaced", Font.PLAIN, fontSize);
        
        initTextArea(rawTextArea, font);
        initTextArea(osisTextArea, font);
        initTextArea(htmlTextArea, font);
    }
    
    private void initTextArea(JTextArea textArea, Font font) {
        textArea.setFont(font);
        textArea.setColumns(100);
        textArea.setRows(30);
        textArea.setTabSize(4);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }
    
    /** Show as Dialog 
     * @param parentComponent {@code Component}
     */
    public void showDialog(Component parentComponent, boolean modal) {
        Window owner = null;
        JDialog dialog = null;
        String title = bundle.getString("CTL_Title.Text");
        if (parentComponent != null) {
            owner = SwingUtilities.getWindowAncestor(parentComponent);
            if (owner instanceof Frame) {
                dialog = new DialogESC((Frame)owner);
            } else if (owner instanceof Dialog) {
                dialog = new DialogESC((Dialog)owner);
            } 
        }
        if (dialog == null) {
            dialog = new DialogESC();
        }
        dialog.setTitle(title);
        dialog.setModal(modal);
        dialog.setLayout(new BorderLayout());
        dialog.add(this,BorderLayout.CENTER);
        dialog.pack();
        if (owner != null) {
            dialog.setLocationRelativeTo(owner);
        }
        
        dialog.setVisible(true);
    }
    
    public void setText(String raw, String osis, String html) {
        rawTextArea.setText(raw);
        osisTextArea.setText(osis);
        htmlTextArea.setText(html);
        
        rawTextArea.setCaretPosition(0);
        osisTextArea.setCaretPosition(0);
        htmlTextArea.setCaretPosition(0);
    }
}

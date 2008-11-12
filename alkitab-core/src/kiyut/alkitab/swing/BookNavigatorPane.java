/* This work has been placed into the public domain. */

package kiyut.alkitab.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import kiyut.alkitab.api.BookViewer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookCategory;
import org.crosswire.jsword.passage.Key;
import org.crosswire.jsword.passage.KeyUtil;

/**
 * Panel which display book Key aka Table of Content. It display the content using
 * {@link kiyut.alkitab.swing.KeyTree KeyTree}
 * 
 */
public class BookNavigatorPane extends javax.swing.JPanel {
    protected ResourceBundle bundle = ResourceBundle.getBundle(this.getClass().getName());

    protected KeyTree keyTree;
    protected BookViewer bookViewer;
    protected TreeSelectionListener treeSelectionListener;
    protected Book book;
    protected boolean bibleDisplayMode = false;
    protected FilterData[] filters;

    /** Creates new BookNavigatorPane */
    public BookNavigatorPane() {
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
        java.awt.GridBagConstraints gridBagConstraints;

        filterPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filterCombo = new javax.swing.JComboBox();
        scrollPane = new javax.swing.JScrollPane();

        filterPane.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(bundle.getString("CTL_Filter.Text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 12);
        filterPane.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        filterPane.add(filterCombo, gridBagConstraints);

        setLayout(new java.awt.BorderLayout());
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox filterCombo;
    private javax.swing.JPanel filterPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    
    protected void initCustom() {
        treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                keyValueChanged(evt);
            }
        };

        filterCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                filterActionPerformed(evt);
            }
        });

        String[] presets = bundle.getString("Filter.Preset").split("\\|");
        filters = new FilterData[presets.length];
        try {
            for (int i = 0; i < presets.length; i++) {
                String str = presets[i];
                int indexOf = str.indexOf("[");
                presets[i] = str.substring(0, indexOf).trim();
                String[] strFilters = str.substring(indexOf + 1, str.length() - 1).split("\\-");
                filters[i] = new FilterData(Integer.parseInt(strFilters[0]), Integer.parseInt(strFilters[1]));
            }
            filterCombo.setModel(new DefaultComboBoxModel(presets));
        } catch (Exception ex) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.CONFIG, ex.getMessage(),ex);
            filters = null;
        }
    }

    public void setBookViewer(BookViewer bookViewer) {
        this.bookViewer = bookViewer;
    }

    public void setDisplayMode(Book book) {
        bibleDisplayMode = false;
        BookCategory bookCategory = book.getBookCategory();
        if (bookCategory.equals(BookCategory.BIBLE) || bookCategory.equals(BookCategory.COMMENTARY)) {
            keyTree = new KeyTree(new BibleKeyTreeModel(BibleKeyTreeModel.LEVEL_VERSE));
            bibleDisplayMode = true;
        } else {
            Key key = book.getGlobalKeyList();
            keyTree = new KeyTree(new DefaultKeyTreeModel(key));
        }

        if (bibleDisplayMode && filters != null) {
            this.add(BorderLayout.NORTH,filterPane);
        } else {
            this.remove(filterPane);
        }

        scrollPane.setViewportView(keyTree);
        keyTree.addTreeSelectionListener(treeSelectionListener);

        this.revalidate();
    }
    
    protected void keyValueChanged(TreeSelectionEvent evt) {
        TreePath treePath = evt.getPath();
        
        KeyTreeNode node = (KeyTreeNode)treePath.getLastPathComponent();
        Key key = node.getKey();
        if (key == null) {
            return;
        }
        
        // XXX this parts is ugly, rewrite needed
        if (bookViewer != null) {
            if (keyTree.getModel() instanceof BibleKeyTreeModel) {
                if (treePath.getPathCount() <= 2) {
                    // this is book level, too big to be displayed
                    return;
                }
                // convert the key into Passage
                bookViewer.setKey(KeyUtil.getPassage(key));
                bookViewer.refresh();
            } else {
                //System.out.println(obj.toString());
                bookViewer.setKey(key);
                bookViewer.refresh();
            }
        }
    }

    protected void filterActionPerformed(ActionEvent evt) {
        if (!bibleDisplayMode) { return; }
        if (filters == null) { return; }
        
        int i = filterCombo.getSelectedIndex();

        BibleKeyTreeModel model = (BibleKeyTreeModel)keyTree.getModel();
        FilterData filterData = filters[i];
        model.setFilter(filterData.getBeginFilter(), filterData.getEndFilter());
        repaint();
    }

    public class FilterData {
        private int beginFilter;
        private int endFilter;

        public FilterData(int beginFilter, int endFilter) {
            this.beginFilter = beginFilter;
            this.endFilter = endFilter;
        }

        public int getBeginFilter() {
            return beginFilter;
        }

        public int getEndFilter() {
            return endFilter;
        }
    }
}

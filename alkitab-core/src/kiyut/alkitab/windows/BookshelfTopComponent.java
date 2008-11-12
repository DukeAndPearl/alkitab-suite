/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.swing.BookshelfTree;
import kiyut.alkitab.swing.BookshelfTreeModel;
import kiyut.openide.util.NbUtilities;
import org.crosswire.jsword.book.Book;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.Repository;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays available book as {@link kiyut.alkitab.swing.BookshelfTree BookshelfTree}.
 */
public final class BookshelfTopComponent extends TopComponent {

    private static BookshelfTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "BookshelfTopComponent";
    
    private BookshelfTree booksTree;
    
    // popup menu related
    private JPopupMenu popupMenu;
    private boolean popupMenuValid = false;
    private String popupMenuFolderName = "Alkitab/Bookshelf/PopupMenu";
    
    private BookshelfTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(BookshelfTopComponent.class, "CTL_BookshelfTopComponent"));
        setToolTipText(NbBundle.getMessage(BookshelfTopComponent.class, "HINT_BookshelfTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        
        initCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized BookshelfTopComponent getDefault() {
        if (instance == null) {
            instance = new BookshelfTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the BookshelfTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized BookshelfTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(BookshelfTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof BookshelfTopComponent) {
            return (BookshelfTopComponent) win;
        }
        Logger.getLogger(BookshelfTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return BookshelfTopComponent.getDefault();
        }
    }
    
    @Override
    protected void componentActivated() {
        super.componentActivated();
        if (booksTree.getSelectionPath() == null) {
            TreeNode root = (TreeNode) booksTree.getModel().getRoot();
            if (root.getChildCount()>0) {
                booksTree.setSelectionRow(0);
            }
        }
        booksTree.requestFocusInWindow();
    }
    
    private void initCustom() {
        booksTree = new BookshelfTree();
        scrollPane.setViewportView(booksTree);
        
        booksTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.isPopupTrigger()) { return; }
                if (evt.getClickCount() != 2) { return; }
                
                openSelectedBook();
            }
            
            @Override
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    showPopup(evt);
                }
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    showPopup(evt);
                }
            }

            private void showPopup(MouseEvent evt) {
                if (popupMenuValid == false) {
                    createPopupMenu();
                }

                if (popupMenu == null) {
                    return;
                }
                
                TreePath treePath = booksTree.getSelectionPath();
                if (treePath == null) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                if (!node.isLeaf()) {
                    return;
                }

                popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        });
        
        booksTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                openSelectedBook();
            }
        });
        
        FileSystem fs = Repository.getDefault().getDefaultFileSystem();
        FileObject fo = fs.getRoot().getFileObject(popupMenuFolderName);
        
        // add listener to monitor layer.xml popup menu change
        fo.addFileChangeListener(new FileChangeAdapter() {
            @Override
            public void fileFolderCreated(FileEvent fe) { popupMenuValid = false; }
            @Override
            public void fileDataCreated(FileEvent fe) { popupMenuValid = false; }
            @Override
            public void fileChanged(FileEvent fe) { popupMenuValid = false; }
            @Override
            public void fileDeleted(FileEvent fe) { popupMenuValid = false; }
            @Override
            public void fileRenamed(FileRenameEvent fe) { popupMenuValid = false; }
            @Override
            public void fileAttributeChanged(FileAttributeEvent fe) { popupMenuValid = false; }
        });

        if (fo != null) {
            // initialize here to speed up
            createPopupMenu();
        }
    }
    
    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        NbUtilities.createMenu(popupMenu,popupMenuFolderName);
        
        if (popupMenu.getComponentCount() <= 0) {
            this.popupMenu = null;
        }
        
        popupMenuValid = true;
    }
    
    /** Reload the tree */
    public void reload() {
        ((BookshelfTreeModel)booksTree.getModel()).reload();
    }
    
    /** Open the selected book, if the currently selected node 
     * is not a book eg: category, it do nothing
     */
    public void openSelectedBook() {
        TreePath treePath = booksTree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

        Object obj = node.getUserObject();
        if (!(obj instanceof Book)) {
            return;
        }

        Book book = (Book)obj;

        if (book.isLocked()) {
            String title = NbBundle.getMessage(BookshelfTopComponent.class, "MSG_UnlockBook.Title");
            String msg = NbBundle.getMessage(BookshelfTopComponent.class, "MSG_UnlockBook.Text");

            Object[] args = {book.getName()};
            msg = MessageFormat.format(msg, args);

            boolean canceled = false;

            NotifyDescriptor.InputLine d = new NotifyDescriptor.InputLine(msg, title, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);

            String unlockKey = book.getUnlockKey();

            while (!canceled) {
                d.setInputText(unlockKey);
                if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.OK_OPTION) {
                    unlockKey = d.getInputText();
                    if (unlockKey != null && unlockKey.length() > 0) {
                        boolean unlocked = book.unlock(unlockKey);
                        //Books.installed().addBook(book);
                        if (!unlocked) {
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(NbBundle.getMessage(BookshelfTopComponent.class, "MSG_UnlockBookInvalidKey.Text"), NotifyDescriptor.ERROR_MESSAGE));
                        }
                        canceled = unlocked;
                    }
                } else {
                    canceled = true;
                }
            }
            
        }

        if (book.isLocked()) {
            //JOptionPane.showMessageDialog(BookInstallerPane.this, bundle.getString("MSG_InvalidSwordModuleSourceFile.Text"), bundle.getString("CTL_Title.Text"), JOptionPane.ERROR_MESSAGE);
            return;
        }


        SwordURI uri = SwordURI.createURI(book, null);
        if (uri != null) {
            BookViewManager.getInstance().openURI(uri, true);
        }
    }
    
    /** Return the selected book or null
     *  @return the selected book or null
     */
    public Book getSelectedBook() {
        TreePath treePath = booksTree.getSelectionPath();
        if (treePath == null) {
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

        Object obj = node.getUserObject();
        if (!(obj instanceof Book)) {
            return null;
        }
        
        return (Book)obj;
    }
}

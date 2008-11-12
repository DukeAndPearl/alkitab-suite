/* This work has been placed into the public domain. */

package kiyut.alkitab.windows;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import kiyut.alkitab.api.BookToolTipFactory;
import kiyut.alkitab.api.BookViewer;
import kiyut.alkitab.api.BookViewerNode;
import kiyut.alkitab.api.BookViewManager;
import kiyut.alkitab.api.Indexer;
import kiyut.alkitab.api.SwordURI;
import kiyut.alkitab.api.ViewerHints;
import kiyut.alkitab.options.BookViewerOptions;
import kiyut.alkitab.swing.ParallelBookViewerPane;
import org.crosswire.jsword.book.Book;
import org.crosswire.jsword.book.BookFilters;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.passage.Key;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Top component which displays {@link kiyut.alkitab.swing.ParallelBookViewerPane ParallelBookViewerPane}.
 */
public class ParallelBookTopComponent extends BookViewerTopComponent {

    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ParallelBookTopComponent";
    private ParallelBookViewerPane bookViewer;
    private Point linkToolTipLocation;

    public ParallelBookTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ParallelBookTopComponent.class, "CTL_ParallelBookTopComponent"));
        setToolTipText(NbBundle.getMessage(ParallelBookTopComponent.class, "HINT_ParallelBookTopComponent"));
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

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ONLY_OPENED;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() throws ObjectStreamException {
        return new ResolvableHelper(this);
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;
        private List<String> bookNames;
        private Key key;
        private boolean compareView;
        private boolean focused;

        public ResolvableHelper(ParallelBookTopComponent tc) {
            ParallelBookViewerPane bookViewer = tc.bookViewer;
            
            if (bookViewer == null) { return; }

            List<Book> books = bookViewer.getBooks();

            bookNames = new ArrayList<String>(books.size());
            for (int i = 0; i < books.size(); i++) {
                bookNames.add(books.get(i).getInitials());
            }

            key = bookViewer.getKey();
            compareView = bookViewer.isCompareView();
            this.focused = tc.isFocusOwner();
        }

        public Object readResolve() {
            final ParallelBookTopComponent result = new ParallelBookTopComponent();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (!BookViewerOptions.getInstance().isSessionPersistence()) {
                        // just a fallback mechanism in case ModuleInstall fail to handle
                        result.close();
                        return;
                    }

                    try {
                        restoreSession(result);
                    } catch (Exception ex) {
                        Logger logger = Logger.getLogger(ParallelBookTopComponent.class.getName());
                        logger.fine("Unable to restore session.\n" + ex.getMessage());
                        result.close();
                    }
                }
            });

            return result;
        }

        private void restoreSession(ParallelBookTopComponent tc) {
            if (bookNames == null || key == null) {
                throw new IllegalStateException("bookNames or key is null");
            }

            ParallelBookViewerPane bookViewer = tc.bookViewer;

            if (key != null) {
                bookViewer.setKey(key);
            }

            for (int i = 0; i < bookNames.size(); i++) {
                bookViewer.addBook(bookNames.get(i));
            }

            if (bookViewer.getBookCount() == 0) {
                throw new IllegalStateException("bookViewer.getBookCount() == 0");
            }

            bookViewer.compareView(compareView);
            bookViewer.refresh();

            if (focused) {
                tc.requestActive();
            }
        }
    }
    
    @Override
    public void componentClosed() {
        if (bookViewer != null) {
            bookViewer.dispose();
        }
    }

    @Override
    public javax.swing.Action[] getActions() {
        List<Action> actionList = new ArrayList<Action>();
        
        // add 
        actionList.addAll(Arrays.asList(super.getActions()));
        actionList.add(1, new ReindexAction());
        
        return actionList.toArray(new Action[0]);
    }
    
    /** If you override this, please make sure to call super.initCustom() */
    protected void initCustom() {
        bookViewer = new ParallelBookViewerPane();
        add(BorderLayout.CENTER, (JComponent) bookViewer);

        bookViewerNode = new BookViewerNode(bookViewer);

        bookViewer.addPropertyChangeListener(BookViewer.VIEWER_NAME, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String name = (String) evt.getNewValue();
                setName(name);
                setToolTipText(name);
            }
        });
        
        bookViewer.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                ParallelBookTopComponent.this.hyperlinkUpdate(evt);
            }
        });
        
        linkToolTipLocation = new Point();
        
        bookViewer.getViewerComponent().addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent evt) { 
                //do nothing
            }

            public void mouseMoved(MouseEvent evt) {
                linkToolTipLocation.setLocation(evt.getX(), evt.getY());
            }
        });
        
        /*
        // Init action map: cut,copy,delete,paste actions.
        javax.swing.ActionMap tcActionMap = getActionMap();
        javax.swing.ActionMap bookViewerActionMap = ((JComponent)bookViewer).getActionMap();
        tcActionMap.setParent(bookViewerActionMap);
        
        /*
        // this is not needed
        CallbackSystemAction callCopyAction = (CallbackSystemAction)SystemAction.get(CopyAction.class);
        tcActionMap.put(callCopyAction.getActionMapKey(), bookViewerActionMap.get(HTMLEditorKit.copyAction));
        bookViewerActionMap.get(HTMLEditorKit.copyAction).setEnabled(true);
         */
        
    }
    
    public void openURI(SwordURI uri) {
        bookViewer.openURI(uri);
    }
    
    public BookViewer getBookViewer() {
        return bookViewer;
    }
    
    private void hyperlinkUpdate(HyperlinkEvent evt) {
        EventType eventType = evt.getEventType();
        String uri = evt.getDescription();
        SwordURI swordURI = SwordURI.createURI(uri);
        
        if (swordURI == null) {
            Logger logger = Logger.getLogger(ParallelBookTopComponent.class.getName());
            logger.log(Level.WARNING, "invalid SwordURI: " + uri);
        }

        if (eventType.equals(HyperlinkEvent.EventType.ACTIVATED)) {
            String fragment = swordURI.getFragment();
            if (fragment.length() > 0) {
                if (fragment.charAt(0) == '#') {
                    return;
                }
            }

            BookViewManager.getInstance().openURI(swordURI);
        } else if (eventType.equals(HyperlinkEvent.EventType.ENTERED)) {
            StatusDisplayer.getDefault().setStatusText(swordURI.toString());
            showToolTip(swordURI);
        } else if (eventType.equals(HyperlinkEvent.EventType.EXITED)) {
            hideToolTip();
        }
    }

    private void showToolTip(SwordURI swordURI) {
        if (swordURI == null) { return; }
        
        ViewerHints viewerHints = bookViewer.getViewerHints();
        Boolean val = (Boolean)viewerHints.get(ViewerHints.TOOLTIP_POPUP);
        if (val.booleanValue() == false) {
            return;
        }

        Book book = getToolTipBook(swordURI);
        Key key = null;


        if (book != null) {
            key = book.getValidKey(swordURI.getFragment());
        }
        
        if (book == null || key == null) {
            return;
        }

        Point p = new Point(linkToolTipLocation.x, linkToolTipLocation.y);
        JComponent comp = bookViewer.getViewerComponent();

        BookToolTipFactory.getInstance().getToolTip().show(book, key, comp, p.x, p.y);
    }
    
    private void hideToolTip() {
        BookToolTipFactory.getInstance().getToolTip().hide();
    }

    /** Return the book from the preferences. If not defined in preferences,
     * it will return in the following order
     * - return StrongsGreek, StrongsHebrew, Robinson <br/>
     * - return the first available book in the category list <br/>
     */
    private Book getToolTipBook(SwordURI swordURI) {
        Book book = null;

        if (swordURI == null) {
            return book;
        }

        String bookName = swordURI.getPath();

        if (bookName.equals("")) {
            switch (swordURI.getType()) {
                case GREEK_STRONGS:
                    bookName = BookViewerOptions.getInstance().getDefaultGreekStrongs();
                    if (bookName == null) {
                        List books = Books.installed().getBooks(BookFilters.getGreekDefinitions());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case HEBREW_STRONGS:
                    bookName = BookViewerOptions.getInstance().getDefaultHebrewStrongs();
                    if (bookName == null) {
                        List books = Books.installed().getBooks(BookFilters.getHebrewDefinitions());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case GREEK_MORPH:
                    bookName = BookViewerOptions.getInstance().getDefaultGreekMorph();
                    if (bookName == null) {
                        List books = Books.installed().getBooks(BookFilters.getGreekParse());
                        if (!books.isEmpty()) {
                            bookName = ((Book) books.get(0)).getInitials();
                        }
                    }
                    break;
                case HEBREW_MORPH:
                    // TODO need to implement this
                    //bookName = BookViewerOptions.getInstance().getDefaultHebrewMorph();

                    List books = Books.installed().getBooks(BookFilters.getHebrewParse());
                    if (!books.isEmpty()) {
                        bookName = ((Book) books.get(0)).getInitials();
                    }
                    break;
                default:
                    break;
            }
        }

        if (bookName != null) {
            if (!bookName.equals("")) {
                book = Books.installed().getBook(bookName);
            }
        }

        return book;
    }
    
     protected class ReindexAction extends AbstractAction {
        public ReindexAction() {
            putValue(Action.NAME, NbBundle.getMessage(ReindexAction.class, "CTL_ReindexAction"));
        }

        public void actionPerformed(ActionEvent evt) {
            BookViewer bookViewer = getBookViewer();
            if (bookViewer == null) {
                return;
            }
            
            List<Book> books = bookViewer.getBooks();
            if (books != null) {
                Indexer.getInstance().createIndex(books, true);
            }
        }
    }
}

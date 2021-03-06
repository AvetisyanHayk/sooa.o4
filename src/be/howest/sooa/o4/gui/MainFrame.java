package be.howest.sooa.o4.gui;

import be.howest.sooa.o4.domain.Genre;
import be.howest.sooa.o4.domain.Movie;
import be.howest.sooa.o4.data.GenreRepository;
import be.howest.sooa.o4.data.MovieRepository;
import be.howest.sooa.o4.moviedb.DBException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author hayk
 */
public class MainFrame extends javax.swing.JFrame {

    private transient MovieRepository movieRepo;
    private transient GenreRepository genreRepo;
    private transient Movie selectedMovie;
    private final GenreDialog genreDialog;
    private final MovieDialog movieDialog;

    /**
     * Creates new form MainForm
     */
    public MainFrame() {
        initComponents();
        genreDialog = new GenreDialog(this);
        centerScreen(genreDialog);
        movieDialog = new MovieDialog(this);
        centerScreen(movieDialog);
    }

    public void confirmAuthentication() {
        movieRepo = new MovieRepository();
        genreRepo = new GenreRepository();
        addListeners();
        fillGenres();
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addListeners() {
        addExitButtonActionListener();
        addEditGenreButtonActionListener();
        addAddGenreButtonActionListener();
        addAddMovieButtonActionListener();
        addEditMovieButtonActionListener();
        addDeleteMovieButtonActionListener();
        addListActionListeners();
        addMainMenuItemListeners();
    }

    private void addExitButtonActionListener() {
        exitButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            dispose();
        });
    }

    public void addDialogKeyListener(JDialog dialog) {
        KeyStroke escapeStroke
                = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        String dispatchWindowClosingActionMapKey
                = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey,
                new DialogClosingOnEscapeAction(dialog));
    }

    public void addEditGenreButtonActionListener() {
        editGenreButton.addActionListener((ActionEvent e) -> {
            Genre selectedGenre = (Genre) genresList.getModel().getSelectedItem();
            genreDialog.setGenre(selectedGenre);
            genreDialog.setTitle("Edit Genre");
            genreDialog.setVisible(true);
        });
    }

    public void addAddGenreButtonActionListener() {
        addGenreButton.addActionListener((ActionEvent e) -> {
            genreDialog.setGenre(null);
            genreDialog.setTitle("Add Genre");
            genreDialog.setVisible(true);
        });
    }

    private void addAddMovieButtonActionListener() {
        addMovieButton.addActionListener((ActionEvent e) -> {
            ComboBoxModel<Genre> model = genresList.getModel();
            movieDialog.setGenresListModel(model);
            movieDialog.setMovie(null);
            movieDialog.setTitle("Add Movie");
            movieDialog.setVisible(true);
        });
    }

    private void addEditMovieButtonActionListener() {
        editMovieButton.addActionListener((ActionEvent e) -> {
            ComboBoxModel<Genre> model = genresList.getModel();
            movieDialog.setGenresListModel(model);
            movieDialog.setMovie(selectedMovie);
            movieDialog.setTitle("Edit Movie");
            movieDialog.setVisible(true);
        });
    }

    private void addDeleteMovieButtonActionListener() {
        deleteMovieButton.addActionListener((ActionEvent e) -> {
            Object[] options = {"Do not delete", "Delete Movie"};
            String message
                        = String.format("Please, apply you want to delete \"%s\" (%d).",
                                selectedMovie.getTitle(), selectedMovie.getYear());
            int result = JOptionPane.showOptionDialog(this,
                    message,
                    "Delete movie?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (result == 1) {
                deleteMovie(selectedMovie);
            }
        });
    }

    private void addListActionListeners() {
        genresList.addItemListener(new GenreItemListener(this));
        moviesList.addListSelectionListener(
                new MovieListSelectionListener(this));
        moviesList.addMouseListener(new MoviesListClickListener(this));
    }

    private void addMainMenuItemListeners() {
        addExitMenuItemListener();
        addAboutMenuItemListener();
    }

    private void addExitMenuItemListener() {
        exitMenuItem.addItemListener((ItemEvent e) -> {
            setVisible(false);
            dispose();
        });
    }

    private void addAboutMenuItemListener() {
        aboutMenuItem.addItemListener((ItemEvent e) -> {
            String message = "Movie management system (c) 2018 Hayk AVETISYAN";
            JOptionPane.showMessageDialog(this, message, "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Fill, Clear, and Select">
    public void fillGenres() {
        DefaultComboBoxModel<Genre> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        genreRepo.findAll().forEach(genre -> {
            model.addElement(genre);
        });
        genresList.setModel(model);
    }

    private void fillMovies(Genre genre) {
        DefaultListModel<Movie> model = new DefaultListModel<>();
        if (genre != null) {
            movieRepo.findByGenre(genre).forEach(movie -> {
                model.addElement(movie);
            });
        }
        moviesList.setModel(model);
    }

    private void clearMovies() {
        DefaultListModel<Movie> model = (DefaultListModel) moviesList.getModel();
        model.removeAllElements();
        editMovieButton.setEnabled(false);
    }

    public void selectGenre(Genre genre) {
        genresList.setSelectedItem(genre);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Data Manipilation">
    private void connectToDatabse() {
        PasswordDialog dialog = new PasswordDialog(this);
        dialog.setTitle("Connect to database");
        centerScreen(dialog);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (movieRepo == null || genreRepo == null) {
                    close();
                }
            }
        });
        dialog.setVisible(true);
    }

    public void saveMovie(Movie movie) throws DBException {
        try {
            if (!movieRepo.exists(movie)) {
                movieRepo.save(movie);
                fillMovies(movie.getGenre());
                movieDialog.hideDialog();
            } else {
                String message
                        = String.format("Movie \"%s\" from %d already exists",
                                movie.getTitle(), movie.getYear());
                showWarning(message);
            }
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
    }

    public void updateMovie(Movie movie) throws DBException {
        try {
            if (!movieRepo.existsAsOther(movie)) {
                movieRepo.update(movie);
                fillGenres();
                selectGenre(movie.getGenre());
                movieDialog.hideDialog();
            } else {
                String message
                        = String.format("Movie \"%s\" from %d already exists",
                                movie.getTitle(), movie.getYear());
                showWarning(message);
            }
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
    }

    public void deleteMovie(Movie movie) {
        try {
            movieRepo.delete(selectedMovie);
            fillMovies(selectedMovie.getGenre());
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
    }

    public void saveGenre(Genre genre) {
        try {
            if (!genreRepo.exists(genre)) {
                genreRepo.save(genre);
                fillGenres();
            } else {
                showWarning("Genre \"" + genre.getName() + "\" already exists.");
            }
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
        selectGenre(genre);
        genreDialog.hideDialog();
    }

    public void updateGenre(Genre genre) {
        try {
            if (!genreRepo.existsAsOther(genre)) {
                genreRepo.update(genre);
                fillGenres();
                selectGenre(genre);
                genreDialog.hideDialog();
            } else {
                showWarning("Genre \"" + genre.getName() + "\" already exists.");
            }
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
    }

    public void deleteGenre(Genre genre) {
        try {
            int movieCount = genreRepo.movieCountByGenre(genre);
            if (movieCount == 0) {
                genreRepo.delete(genre);
                updateGUIAfterDeletingGeners();
            } else {
                int result = showWarningToDeleteGenreWithMovies(movieCount, genre.getName());
                if (result == 1) {
                    genreRepo.deleteWithMovies(genre);
                    updateGUIAfterDeletingGeners();
                }
            }
            genreDialog.hideDialog();
        } catch (DBException ex) {
            showWarning(ex.getMessage());
        }
    }

    public void updateGUIAfterDeletingGeners() {
        fillGenres();
        fillMovies(null);
        editGenreButton.setEnabled(false);
    }

    private int showWarningToDeleteGenreWithMovies(int movieCount, String genreName) {
        Object[] options = {"Do not delete", "Delete Movies"};
        String message;
        if (movieCount > 1) {
            message = String.format("There are %d %s movies.%n"
                    + "Do you want to delete them all?", movieCount, genreName);
        } else {
            message = String.format("There is 1 %s movie.%n"
                    + "Do you want to delete it?", genreName);
        }
        return JOptionPane.showOptionDialog(this,
                message, "Delete genre together with movies?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Custom functions">
    public void centerScreen() {
        centerScreen(this);
    }

    private void centerScreen(Window window) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - window.getWidth()) / 2;
        final int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    public void close() {
        setVisible(false);
        dispose();
    }

    // </editor-fold>
    //
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelH1 = new javax.swing.JLabel();
        genresList = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        moviesList = new javax.swing.JList<>();
        addMovieButton = new javax.swing.JButton();
        editMovieButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        editGenreButton = new javax.swing.JButton();
        deleteMovieButton = new javax.swing.JButton();
        addGenreButton = new javax.swing.JButton();
        mainMenu = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Movie Management System");
        setResizable(false);

        labelH1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        labelH1.setText("Movie Management System");

        jScrollPane1.setViewportView(moviesList);

        addMovieButton.setText("Add Movie...");
        addMovieButton.setName(""); // NOI18N

        editMovieButton.setText("Edit Movie...");
        editMovieButton.setEnabled(false);

        exitButton.setText("Exit");

        editGenreButton.setText("Edit...");
        editGenreButton.setEnabled(false);

        deleteMovieButton.setText("Delete Movie");
        deleteMovieButton.setEnabled(false);
        deleteMovieButton.setName(""); // NOI18N

        addGenreButton.setText("Add...");

        menuFile.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        menuFile.add(exitMenuItem);

        mainMenu.add(menuFile);

        menuHelp.setText("Edit");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        menuHelp.add(aboutMenuItem);

        mainMenu.add(menuHelp);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addMovieButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editMovieButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteMovieButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(exitButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelH1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(genresList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editGenreButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addGenreButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelH1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genresList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editGenreButton)
                    .addComponent(addGenreButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addMovieButton)
                    .addComponent(editMovieButton)
                    .addComponent(exitButton)
                    .addComponent(deleteMovieButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, "Movie management system (c) 2018 Hayk AVETISYAN");
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(ex.getMessage());
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MainFrame newMainFrame = new MainFrame();
            newMainFrame.centerScreen();
            newMainFrame.setVisible(true);
            newMainFrame.connectToDatabse();
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addGenreButton;
    private javax.swing.JButton addMovieButton;
    private javax.swing.JButton deleteMovieButton;
    private javax.swing.JButton editGenreButton;
    private javax.swing.JButton editMovieButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JComboBox<Genre> genresList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelH1;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JList<Movie> moviesList;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc="Custom Listeners">
    private static class MovieListSelectionListener implements ListSelectionListener {

        final MainFrame frame;

        MovieListSelectionListener(MainFrame frame) {
            this.frame = frame;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {

            boolean isAdjusting = e.getValueIsAdjusting();

            if (isAdjusting) {
                Movie movie = frame.moviesList.getSelectedValue();
                Genre genre = (Genre) frame.genresList.getSelectedItem();
                movie.setGenre(genre);
                frame.selectedMovie = movie;
                boolean enabled = frame.selectedMovie != null;
                frame.editMovieButton.setEnabled(enabled);
                frame.deleteMovieButton.setEnabled(enabled);
            }
        }
    }

    private static class GenreItemListener implements ItemListener {

        final MainFrame frame;

        GenreItemListener(MainFrame frame) {
            this.frame = frame;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean itemSelected = e.getStateChange() == ItemEvent.SELECTED;
            if (itemSelected) {
                Genre selected = (Genre) e.getItem();
                frame.fillMovies(selected);
            } else {
                frame.clearMovies();
            }
            frame.editGenreButton.setEnabled(itemSelected);
        }
    }

    private static class DialogClosingOnEscapeAction extends AbstractAction {

        final JDialog dialog;

        DialogClosingOnEscapeAction(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispatchEvent(new WindowEvent(
                    dialog, WindowEvent.WINDOW_CLOSING));
        }
    }

    private static class MoviesListClickListener implements MouseListener {

        final MainFrame frame;

        MoviesListClickListener(MainFrame frame) {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                frame.editMovieButton.doClick();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    // </editor-fold>
}

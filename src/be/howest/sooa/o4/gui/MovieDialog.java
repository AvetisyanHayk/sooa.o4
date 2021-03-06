package be.howest.sooa.o4.gui;

import be.howest.sooa.o4.domain.Genre;
import be.howest.sooa.o4.domain.Movie;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Hayk
 */
public class MovieDialog extends javax.swing.JDialog {

    private transient Movie movie;
    private final MainFrame frame;

    public MovieDialog(MainFrame frame) {
        this(frame, null, null);
    }

    public MovieDialog(MainFrame frame, ComboBoxModel model) {
        this(frame, model, null);
    }

    public MovieDialog(MainFrame frame, ComboBoxModel model, Movie movie) {
        super(frame, true);
        initComponents();
        this.frame = frame;
        setMovie(movie);
        fillYears();
        fillStars();
        addActionListeners();
        setGenresListModel(model);
    }

    public final void setMovie(Movie movie) {
        this.movie = movie;
    }

    public final void setGenresListModel(ComboBoxModel model) {
        if (model != null) {
            genresList.setModel(model);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addActionListeners() {
        frame.addDialogKeyListener(this);
        addCancelButtonActionListener();
        addSaveButtonActionListener();
        addFieldEnterKeyActionListeners();
    }

    private void addCancelButtonActionListener() {
        cancelButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            dispose();
        });
    }

    private void addSaveButtonActionListener() {
        saveButton.addActionListener((ActionEvent e) -> {
            Genre genre = (Genre) genresList.getModel().getSelectedItem();
            String movieTitle = titleField.getText().trim();
            Integer year = (Integer) yearsList.getSelectedItem();
            Integer stars = (Integer) starsList.getSelectedItem();
            if (genre == null || "".equals(movieTitle)) {
                showWarning(genre, movieTitle);
            } else {
                saveMovie(genre, movieTitle, year, stars);
            }
        });
    }

    private void showWarning(Genre genre, String movieTitle) {
        StringBuilder sb = new StringBuilder();
        if (genre == null) {
            sb.append("Please, select genre from the list.");
            if ("".equals(movieTitle)) {
                sb.append("\n");
            }
        }
        if ("".equals(movieTitle)) {
            sb.append("Title field may not be empty!");
        }
        JOptionPane.showMessageDialog(this, sb, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    private void addFieldEnterKeyActionListeners() {
        titleField.addActionListener((ActionEvent e) -> {
            saveButton.doClick();
        });
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Fill, Clear, and Select">
    private void initFields() {
        genresList.setSelectedItem(movie.getGenre());
        yearsList.setSelectedItem(movie.getYear());
        starsList.setSelectedItem(movie.getStars());
        titleField.setText(movie.getTitle());
    }

    private void fillYears() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 1888; year <= currentYear + 5; year++) {
            model.addElement(year);
        }
        yearsList.setModel(model);
    }

    private void fillStars() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(null);
        int maxStars = 5;
        for (int stars = 1; stars <= maxStars; stars++) {
            model.addElement(stars);
        }
        starsList.setModel(model);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Data Manipulation">
    private void saveMovie(Genre genre, String movieTitle, int year, Integer stars) {
        if (movie == null) {
            buildMovie(genre, movieTitle, year, stars);
            frame.saveMovie(movie);
            movie = null;
        } else {
            updateMovie(genre, movieTitle, year, stars);
            frame.updateMovie(movie);
        }
    }
    
    private void updateMovie(Genre genre, String movieTitle, int year, Integer stars) {
        movie.setGenre(genre);
        movie.setTitle(movieTitle);
        movie.setYear(year);
        movie.setStars(stars);
    }

    private void buildMovie(Genre genre, String movieTitle, int year, Integer stars) {
        movie = new Movie(movieTitle,
                year,
                stars);
        movie.setGenre(genre);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Custom Functions">
    public void hideDialog() {
        movie = null;
        setVisible(false);
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (movie != null) {
            initFields();
        } else {
            titleField.setText("");
            yearsList.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
            starsList.setSelectedItem(null);
        }
        super.setVisible(visible);
    }

    // </editor-fold>
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genresList = new javax.swing.JComboBox<>();
        yearLabel = new javax.swing.JLabel();
        yearsList = new javax.swing.JComboBox<>();
        starsList = new javax.swing.JComboBox<>();
        starsLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(null);
        setResizable(false);

        yearLabel.setText("Year");

        starsLabel.setText("Stars");

        titleLabel.setText("Title");

        saveButton.setText("Save Movie");

        cancelButton.setText("Do not save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(genresList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(yearLabel)
                                    .addComponent(yearsList, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(starsLabel)
                                    .addComponent(starsList, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(titleLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton)))
                        .addGap(0, 196, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(genresList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yearLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(starsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(starsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<Genre> genresList;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel starsLabel;
    private javax.swing.JComboBox<Integer> starsList;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel yearLabel;
    private javax.swing.JComboBox<Integer> yearsList;
    // End of variables declaration//GEN-END:variables
}

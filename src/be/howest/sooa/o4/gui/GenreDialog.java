package be.howest.sooa.o4.gui;

import be.howest.sooa.o4.domain.Genre;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Hayk
 */
public class GenreDialog extends java.awt.Dialog {

    private transient Genre genre;

    /**
     * Creates new form GenreDialog
     *
     * @param parent
     * @param modal
     */
    public GenreDialog(java.awt.Frame parent, boolean modal) {
        this(parent, modal, null);
    }

    public GenreDialog(Frame parent, boolean modal, Genre genre) {
        super(parent, modal);
        initComponents();
        this.genre = genre;
        if (genre != null) {
            deleteButton.setEnabled(true);
            genreField.setText(genre.toString());
        }
        addActionListeners();
    }

    private void addActionListeners() {
        addCancelButtonActionListener();
        addSaveButtonActionListener();
        if (genre != null) {
            addDeleteGenreButtonActionListener();
        }
    }

    private void addCancelButtonActionListener() {
        cancelButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            dispose();
        });
    }

    private void addSaveButtonActionListener() {
        saveButton.addActionListener((ActionEvent e) -> {
            String newGenre = genreField.getText().trim();
            if ("".equals(newGenre)) {
                showWarningForRequiredData(newGenre);
            } else {
                saveGenre(newGenre);
            }
        });
    }

    private void addDeleteGenreButtonActionListener() {
        deleteButton.addActionListener((ActionEvent e) -> {
            Object[] options = {"Do not delete", "Delete Genre"};
            int result = JOptionPane.showOptionDialog(this,
                    "Please, apply you want to delete the following genre:\n"
                    + genre,
                    "Delete genre?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (result == 1) {
                MainFrame frame = (MainFrame) getParent();
                frame.deleteGenre(genre);
            }
        });
    }

    private void saveGenre(String newGenre) {
        MainFrame frame = (MainFrame) getParent();
        if (genre == null) {
            buildGenre(newGenre);
            frame.saveGenre(genre);
        } else {
            updateGenre(genre, newGenre);
            frame.updateGenre(genre);
        }
        setVisible(false);
        dispose();
    }

    private void buildGenre(String newGenre) {
        genre = new Genre(newGenre);
        genre.setName(newGenre);
    }

    private void updateGenre(Genre genre, String newGenre) {
        genre.setName(newGenre);
    }

    private void showWarningForRequiredData(String newGenre) {
        String message = "Genre field may not be empty!";
        JOptionPane.showMessageDialog(this, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genreLabel = new javax.swing.JLabel();
        genreField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        genreLabel.setText("Genre");

        saveButton.setText("Save Genre");

        cancelButton.setText("Do not save");

        deleteButton.setText("Delete Genre");
        deleteButton.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(genreField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(genreLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addComponent(deleteButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(genreLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton)
                    .addComponent(deleteButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        genreLabel.getAccessibleContext().setAccessibleName("Genre");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GenreDialog dialog = new GenreDialog(new java.awt.Frame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField genreField;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}

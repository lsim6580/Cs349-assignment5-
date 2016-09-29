import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

public class PhotoViewerLayout extends JFrame {
    ImageIcon image;
    JLabel imageLabel;
    JTextArea descriptionTextArea;
    JTextField dateTextField;
    JButton nextButton;
    JButton prevButton;
    PhotoContainer photoContainer;
    JTextField pictureNumberTextField;
    JLabel pictureCountLabel;
    boolean editing = false;
    public PhotoViewerLayout() throws FileNotFoundException{
        photoContainer = new PhotoContainer();
        imageLabel = new JLabel("", SwingConstants.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);


        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("photoClass.txt"));
            photoContainer = (PhotoContainer) ois.readObject();
            ois.close();

        }
        catch (Exception e){
            System.out.println("no photoContainer type Found");
            Photo photo = new Photo();
            photo.setSource("src/Baby.jpg");
            photo.setDescription("BABY");
            photoContainer.add(photo);
            imageLabel.setIcon(photo.getSource());


        }


//        Photo photo = new Photo();
//        photo.setSource("src/Baby.jpg");
//        photoContainer.add(photo);




        JLabel descriptionLabel = new JLabel("Description:");
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setPreferredSize(new Dimension(descriptionLabel.getPreferredSize().width,dateLabel.getPreferredSize().height));
        descriptionTextArea = new JTextArea(4,20);
        dateTextField = new JTextField();

        if(photoContainer.length() == 0){
            pictureNumberTextField = new JTextField(0);
            pictureCountLabel = new JLabel(" of 0" );
        }
        else{
            pictureNumberTextField = new JTextField(Integer.toString(photoContainer.getIndex()+1));
            pictureCountLabel = new JLabel(" of "+ photoContainer.length());
            imageLabel.setIcon(photoContainer.getCurrent().getSource());
            descriptionTextArea.setText(photoContainer.getCurrent().getDescription());
            dateTextField = new JTextField(photoContainer.getCurrent().getDate());


        }

        Container contentPane = getContentPane();





        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));

        JPanel descriptionPane = new JPanel();
        descriptionPane.setLayout(new FlowLayout(FlowLayout.LEFT));


        descriptionPane.add(descriptionLabel);
        descriptionPane.add(descriptionTextArea);

        JPanel datePane = new JPanel();
//		datePane.setLayout(new FlowLayout(FlowLayout.LEFT));
//		datePane.setLayout(new BoxLayout(datePane, BoxLayout.LINE_AXIS));


        datePane.add(dateLabel);
        datePane.add(dateTextField);
        //datePane.add(Box.createHorizontalGlue());
        JPanel buttonPane = new JPanel();
        JButton addButton = new JButton("Add Photo");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Photo photo = new Photo();
                    photo.setSource(selectedFile.getAbsolutePath());
                    photoContainer.add(photo);
                    pictureCountLabel.setText("of "+ photoContainer.length());
                    updateUI(photoContainer);
                    nextButton.setEnabled(true);
                        imageLabel.setIcon(photoContainer.getCurrent().getSource());

                   // System.out.println(photoContainer.getPhoto(1).getSource());


                }
            }
        });
        JButton deleteButton  = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(photoContainer.length()> 1){
                    photoContainer.delete(photoContainer.getIndex());
                    imageLabel.setIcon(photoContainer.getCurrent().getSource());
                    updateUI(photoContainer);
                    pictureCountLabel.setText("of "+ photoContainer.length());

                }
                else if(photoContainer.length() ==1){
                    JOptionPane.showMessageDialog(null, "There must be at least one photo in the Frame");
                }
                if(photoContainer.length() ==1){
                    prevButton.setEnabled(false);
                    nextButton.setEnabled(false);

                }
            }
        });
        buttonPane.add(deleteButton);
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePhoto(photoContainer);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("photoClass.txt"));
                    oos.writeObject(photoContainer);
                    oos.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        buttonPane.add(saveButton);
        buttonPane.add(addButton);

        JPanel leftRightPane = new JPanel();
        leftRightPane.setLayout(new BorderLayout());
        leftRightPane.add(datePane,BorderLayout.WEST);
        leftRightPane.add(buttonPane,BorderLayout.EAST);


        JPanel southButtonPanel = new JPanel();
        prevButton = new JButton("<prev");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePhoto(photoContainer);
                photoContainer.prev();
                updateUI(photoContainer);

                if(!photoContainer.hasPrev()){
                    prevButton.setEnabled(false);
                }
                if(!nextButton.isEnabled()){
                    nextButton.setEnabled(true);
                }
            }
        });
        nextButton = new JButton("next>");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                savePhoto(photoContainer);

                photoContainer.next();



                updateUI(photoContainer);
                if(!photoContainer.hasNext()){
                    nextButton.setEnabled(false);
                }
                if(!prevButton.isEnabled()){
                    prevButton.setEnabled(true);
                }
            }
        });

        southButtonPanel.add(pictureNumberTextField);
        southButtonPanel.add(pictureCountLabel);
        southButtonPanel.add(prevButton);
        southButtonPanel.add(nextButton);
        FlowLayout flowLayout = (FlowLayout) southButtonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);


        controlPane.add(descriptionPane);
        controlPane.add(leftRightPane);
        controlPane.add(southButtonPanel);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create the first menu.
        JMenu menu = new JMenu("File");
        JMenu view = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        menuBar.add(view);
        JMenuItem browse =new JMenuItem("Browse");
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editing = false;
                addButton.setVisible(false);
                saveButton.setVisible(false);
                deleteButton.setVisible(false);
                dateTextField.setEnabled(false);
                dateTextField.setEnabled(false);
            }
        });

        JMenuItem maintain = new JMenuItem("Maintain");
        maintain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton.setVisible(true);
                saveButton.setVisible(true);
                deleteButton.setVisible(true);
                dateTextField.setEnabled(true);
                descriptionTextArea.setEnabled(true);
            }
        });

        // Create an item for the first menu
        JMenuItem exitMenuItem = new JMenuItem("Exit",KeyEvent.VK_X);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //menuItem.setMnemonic(KeyEvent.VK_X); // set in constructor
        menu.add(exitMenuItem);
        view.add(browse);
        view.add(maintain);

        contentPane.add(controlPane, BorderLayout.SOUTH); // Or PAGE_END
    }

    public void savePhoto(PhotoContainer photoContainer){
        photoContainer.getCurrent().setDate(dateTextField.getText());
        photoContainer.getCurrent().setDescription(descriptionTextArea.getText());
    }

    public void updateUI(PhotoContainer photoContainer){
        dateTextField.setText(photoContainer.getCurrent().getDate());
        descriptionTextArea.setText(photoContainer.getCurrent().getDescription());
        pictureNumberTextField.setText(Integer.toString(photoContainer.getIndex()+1));
        imageLabel.setIcon(photoContainer.getCurrent().getSource());

    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new PhotoViewerLayout();
        frame.pack();
        frame.setVisible(true);
    }
}
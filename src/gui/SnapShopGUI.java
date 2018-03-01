/*
 * TCSS 305 - Autumn 2016
 * Assignment 4 - SnapShop
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The GUI for SnapShop.
 * 
 * @author Leah Ruisenor
 * @version November 2016
 */
public class SnapShopGUI extends JFrame {

    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = -2144739668936849562L;
    
    /** JPanel in the northwest, to contain filter buttons. */
    private final JPanel myNwPanel;
    
    /** JPanel in the southwest, to contain open, save and close buttons. */
    private final JPanel mySwPanel;
    
    /** JPanel in the west, to contain all the buttons. */
    private final JPanel myWestPanel;
    
    /** Image panel to contain the image. */
    private final JPanel myImagePanel;
    
    /** Hashmap, to map buttons to filters. */
    private final  Map<JButton, Filter> myButtonMap;
    
    /** Filter button names. */
    private final String[] myFilterButtonNames = {"Edge Detect", "Edge Highlight", 
                                                  "Flip Horizontal", "Flip Vertical", 
                                                  "Grayscale", "Sharpen", "Soften"};
    
    /** The filters. */
    private final Filter[] myFilterButtons = {new EdgeDetectFilter(),
                                              new EdgeHighlightFilter(), 
                                              new FlipHorizontalFilter(),
                                              new FlipVerticalFilter(), new GrayscaleFilter(),
                                              new SharpenFilter(), new SoftenFilter()};
    
    /** Directory for the images. */
    private File myImageDirectory;
    
    /** Chooser for the image. */
    private final JFileChooser myChooseFile;
    
    /** The image to edit. */
    private PixelImage myImage;
    
    /** The image label. */
    private JLabel myImageLabel;
    

    /**
     * Constructor for initializing my fields.
     */
    public SnapShopGUI() {
        super("TCSS 305 SnapShop");
        myWestPanel = new JPanel();
        myNwPanel = new JPanel();
        mySwPanel = new JPanel();
        myImagePanel = new JPanel();
        myButtonMap = new HashMap<>();
        myImageDirectory =  new File(".");
        myChooseFile = new JFileChooser(myImageDirectory);
    }

    /**
     * Starts the GUI and sets it to visible.
     */
    public final void start() {    
//        javax.swing.JOptionPane.showMessageDialog(null, "Welcome to SnapShop");
//        setLocationRelativeTo(null); // sort of centers the pane but not really
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        makeNwPanel(false);
        makeSwPanel(false);
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }


    /**
     * Method to make a northwest box of filter buttons for panel.
     * 
     * @param theButtonClick is true or false for enabling click-able button.
     */
    private void makeNwPanel(final boolean theButtonClick) {
        
        for (int i = 0; i < myFilterButtonNames.length; i++) {
            final JButton button = new JButton(myFilterButtonNames[i]);

            myButtonMap.put(button, myFilterButtons[i]);

            myNwPanel.setLayout(new BoxLayout(myNwPanel, BoxLayout.Y_AXIS));
            add(Box.createVerticalGlue());
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                                button.getMinimumSize().height));
            myNwPanel.add(button);
            button.setEnabled(theButtonClick);
            
            button.addActionListener((theEvent) -> { 
                filterTheImage(myImage, button);
            });
        }
        myWestPanel.setLayout(new BorderLayout());
        myWestPanel.add(myNwPanel, BorderLayout.NORTH);
        myWestPanel.add(mySwPanel, BorderLayout.SOUTH);
        add(myWestPanel, BorderLayout.WEST);

    }
    
    /**
     * Filtering the image per what button was clicked.
     * 
     * @param theImage Image to be filtered.
     * @param theButton Which button was clicked.
     */
    private void filterTheImage(final PixelImage theImage, final JButton theButton) {
        myButtonMap.get(theButton).filter(theImage);
        myImageLabel.setIcon(new ImageIcon(theImage));
        pack();
        setMinimumSize(null);
    }
    
    /**
     * Method to make a southwest panel for open, save and close image button.
     * 
     * @param theButtonClick is true or false for enabling click-able button.
     */
    private void makeSwPanel(final boolean theButtonClick) {
        
        final JButton openButton = new JButton("Open...");
        final JButton saveButton = new JButton("Save As...");
        final JButton closeImageButton = new JButton("Close Image"); 
        final Dimension buttonSize = new Dimension(136, openButton.getMinimumSize().height);
 
        mySwPanel.setLayout(new BoxLayout(mySwPanel, BoxLayout.Y_AXIS));
        final Box box = Box.createVerticalBox();
 
        openButton.setMaximumSize(buttonSize);
        saveButton.setMaximumSize(buttonSize);
        closeImageButton.setMaximumSize(buttonSize);
        
        box.add(openButton);
        box.add(saveButton);
        box.add(closeImageButton);
        
        mySwPanel.add(box, BorderLayout.SOUTH);
             
        saveButton.setEnabled(theButtonClick);
        closeImageButton.setEnabled(theButtonClick);
        
        openButton.addActionListener((theEvent) -> { 
            try {
                openTheFile();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        });

        saveButton.addActionListener((theEvent) -> {
            try {
                saveTheFile();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        });
        
        closeImageButton.addActionListener((theEvent) -> { 
            closeTheFile();
        });
//        myWestPanel.add(mySwPanel, BorderLayout.SOUTH);
//        add(myWestPanel, BorderLayout.WEST);
    }
    
    /**
     * Creates an image panel in the upper north corner next to the filter buttons.
     * 
     * @param theImageFileName is the name of the file with the image.
     * @throws IOException when image fails to load.
     */
    private void makeImagePanel(final File theImageFileName) throws IOException {
        myImage = PixelImage.load(new File(theImageFileName.toString()));
        myImageLabel = new JLabel();
        myImageLabel.setIcon(new ImageIcon(myImage));
        myImagePanel.removeAll();
        myImagePanel.setLayout(new BoxLayout(myImagePanel, BoxLayout.Y_AXIS));
        myImagePanel.add(myImageLabel);
        add(myImagePanel, BorderLayout.CENTER);
        setMinimumSize(null);
        pack();
        setMinimumSize(getSize());
    }

    /**
     * Sends an error message if image file is not an image file.
     * 
     * @throws IOException when the image fails.
     */
    private void openTheFile() throws IOException {
        final int selected = myChooseFile.showOpenDialog(this);
        if (myChooseFile.getSelectedFile() != null && selected != JFileChooser.CANCEL_OPTION) {
            if (hasGoodExtension(myChooseFile.getSelectedFile())) {
                myImageDirectory = myChooseFile.getCurrentDirectory();
                myNwPanel.removeAll();
                mySwPanel.removeAll();
                makeNwPanel(true);
                makeSwPanel(true);
                makeImagePanel(myChooseFile.getSelectedFile());
            } else {
                JOptionPane.showMessageDialog(null, 
                                              "The selected file did not contain an image!",
                                              "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Saves the file when open button is clicked.
     * 
     * @throws IOException when the image fails to save.
     */
    private void saveTheFile() throws IOException {
        if (myChooseFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            myImage.save(myChooseFile.getSelectedFile());
        }
    }
    
    /**
     * Closes the file when the Close Image button is clicked.
     */
    private void closeTheFile() {
        myImagePanel.removeAll();
        myNwPanel.removeAll();
        mySwPanel.removeAll();
        makeNwPanel(false);
        makeSwPanel(false); 
        setMinimumSize(null);
        pack();
        setMinimumSize(getSize());
    }
    
    /**
     * Checks if the file that was clicked on is even a image file.
     * 
     * @param theImageFileName is image file name that is going to be checked.
     * @return will return true if the file has a good extension, 
     * if not it will pop up an error message.
     */
    private boolean hasGoodExtension(final File theImageFileName) {
        boolean isImageFile = false;
        final String[] goodExtensions = {".jpg", ".png", ".gif"};
        final String[] imageName = theImageFileName.toString().split(" ");

        for (int i = 0; i < goodExtensions.length; i++) {
            if (imageName[imageName.length - 1].toLowerCase().contains(goodExtensions[i])) {
                isImageFile = true;
            }
        }
        return isImageFile;
    }
}
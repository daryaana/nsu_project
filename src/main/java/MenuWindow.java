import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuWindow extends JFrame {
    private JToolBar toolBar = new JToolBar();
    private MainPanel mainPanel = new MainPanel();
    private int redInit = 2, greenInit = 2, blueInit = 2;
    private JFileChooser fileChooser = null;
    ActionListener blurAction = e -> {
        mainPanel.blurFilter();
    };
    ActionListener waterAction = e -> {
        mainPanel.watercolorFilter();
    };
    ActionListener openAction = e -> {
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image formats", "png", "bmp", "jpg"));
        int f = fileChooser.showOpenDialog(null);
        if (f == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            mainPanel.loadFile(file);
        }
    };
    ActionListener sharpnessAction = e -> {
        mainPanel.sharpnessFilter();
    };
    ActionListener doubleAction = e -> mainPanel.doubleFilter();
    ActionListener aboutAuthorAction = e -> aboutAuthor();
    ActionListener blackWhiteAction = e -> mainPanel.blackWhiteFilter();
    ActionListener mirrorAction = e -> mainPanel.mirrorFilter();
    //ActionListener selectAction = e -> mainPanel.selected();
    ActionListener negativeAction = e -> mainPanel.negativeFilter();
    ActionListener copyCtoB = e -> mainPanel.c2b();
    ActionListener embossingAction = e -> mainPanel.embossing();
    ActionListener rotateAction = e -> {
        mainPanel.rotateFilter();
        rotateSettings();
    };
    ActionListener sobelAction = e -> {
        mainPanel.sobelFilter();
        sobelSettings();
    };
    ActionListener orderedAction=e->{
        orderedSettings();
        mainPanel.orderedFilter();
    };
    ActionListener robertAction = e -> {
        mainPanel.robertFilter();
        robertSettings();
    };

    private void robertSettings() {
        JDialog dialog = new JDialog(this, "Set contour", true);
        JPanel contourPanel = new JPanel();
        JSlider contourSlider = new JSlider(JSlider.HORIZONTAL);
        TextField contourField = new TextField("", 10);
        JButton save = new JButton("Save");
        int threshold = 10;

        contourPanel.setLayout(new GridLayout(3, 2));

        save.addActionListener(d -> dialog.dispose());


        contourSlider.setMinimum(0);
        contourSlider.setMaximum(100);

        contourField.setText(String.valueOf(threshold));
        contourSlider.setValue(threshold);

        contourField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                contourSlider.setValue(Integer.valueOf(contourField.getText()));
            }
        });


        contourSlider.addChangeListener(s -> {
            int c = contourSlider.getValue();
            contourField.setText(String.valueOf(c));
            mainPanel.setThreshold(c);
            mainPanel.robertFilter();

        });

        contourPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Threshold"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        contourPanel.add(contourField);
        contourPanel.add(contourSlider);
        contourPanel.add(save);

        dialog.add(contourPanel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300, 130));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void orderedSettings() {
        JDialog dialog = new JDialog(this, "Set dithering", true);
        JPanel ditheringPanel = new JPanel();
        TextField ditheringFieldRed = new TextField("", 10);
        TextField ditheringFieldGreen = new TextField("", 10);
        TextField ditheringFieldBlue = new TextField("", 10);
        JButton save = new JButton("Save");
        int red = 2;
        int green = 2;
        int blue = 2;

        ditheringPanel.setLayout(new GridLayout(4, 1));

        ditheringFieldRed.setText(String.valueOf(red));
        ditheringFieldGreen.setText(String.valueOf(green));
        ditheringFieldBlue.setText(String.valueOf(blue));

        save.addActionListener(d -> dialog.dispose());
        Object o = new Object();

        ditheringFieldRed.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                synchronized (o) {
                    mainPanel.setRed(Integer.valueOf(ditheringFieldRed.getText()));
                    mainPanel.orderedFilter();
                }
            }
        });

        ditheringFieldGreen.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                synchronized (o) {
                    mainPanel.setGreen(Integer.valueOf(ditheringFieldGreen.getText()));
                    mainPanel.orderedFilter();
                }
            }
        });

        ditheringFieldBlue.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) {
                synchronized (o) {
                    mainPanel.setBlue(Integer.valueOf(ditheringFieldBlue.getText()));
                    mainPanel.orderedFilter();
                }
            }
        });

        ditheringPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Dithering"), BorderFactory.createEmptyBorder(5,5,5,5)));
        ditheringPanel.add(ditheringFieldRed);
        ditheringPanel.add(new JLabel("Red"));
        ditheringPanel.add(ditheringFieldRed);
        ditheringPanel.add(ditheringFieldGreen);
        ditheringPanel.add(new JLabel("Green"));
        ditheringPanel.add(ditheringFieldGreen);
        ditheringPanel.add(ditheringFieldBlue);
        ditheringPanel.add(new JLabel("Blue"));
        ditheringPanel.add(ditheringFieldBlue);
        ditheringPanel.add(save);


        dialog.add(ditheringPanel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(250, 180));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void sobelSettings() {
        JDialog dialog = new JDialog(this, "Set contour", true);
        JPanel contourPanel = new JPanel();
        JSlider contourSlider = new JSlider(JSlider.HORIZONTAL);
        TextField contourField = new TextField("", 10);
        JButton save = new JButton("Save");
        int threshold = 10;
        contourPanel.setLayout(new GridLayout(3, 2));

        save.addActionListener(d -> dialog.dispose());


        contourSlider.setMinimum(0);
        contourSlider.setMaximum(100);

        contourField.setText(String.valueOf(threshold));
        contourSlider.setValue(threshold);

        contourField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                contourSlider.setValue(Integer.valueOf(contourField.getText()));
            }
        });


        contourSlider.addChangeListener(s -> {
            int c = contourSlider.getValue();
            contourField.setText(String.valueOf(c));
            mainPanel.setThreshold(c);
            mainPanel.sobelFilter();
        });

        contourPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Threshold"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        contourPanel.add(contourField);
        contourPanel.add(contourSlider);
        contourPanel.add(save);

        dialog.add(contourPanel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300, 130));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    MenuWindow() {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        mainPanel.setRGB(redInit, greenInit, blueInit);
    }


    public void createToolbarNegative(JToolBar toolBar) {
        JButton negativeFButton = new JButton();
        negativeFButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/negative.png")));
        negativeFButton.setToolTipText("Negative");
        toolBar.add(negativeFButton);
        negativeFButton.addActionListener(negativeAction);
    }

    public void createToolbarBlur(JToolBar toolBar) {
        JButton blurButton = new JButton();
        blurButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/blur.png")));
        blurButton.setToolTipText("Blur");
        toolBar.add(blurButton);
        blurButton.addActionListener(blurAction);
    }

    public void createToolbarSharpness(JToolBar toolBar) {
        JButton sharpnessButton = new JButton();
        sharpnessButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/sharpness.png")));
        sharpnessButton.setToolTipText("Sharpness");
        toolBar.add(sharpnessButton);
        sharpnessButton.addActionListener(sharpnessAction);
    }
    public void createToolbarOrdered(JToolBar toolBar) {
        JButton orderedButton = new JButton();
        orderedButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/ordered.png")));
        orderedButton.setToolTipText("Ordered");
        toolBar.add(orderedButton);
        orderedButton.addActionListener(orderedAction);
    }


    public void createToolbarCopyC2B(JToolBar toolBar) {
        JButton copyButton = new JButton();
        copyButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/copy.png")));
        copyButton.setToolTipText("Copy C2B");
        toolBar.add(copyButton);
        copyButton.addActionListener(copyCtoB);
    }

    public void createToolbarDouble(JToolBar toolBar) {
        JButton doubleButton = new JButton();
        doubleButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/double.png")));
        doubleButton.setToolTipText("Double");
        toolBar.add(doubleButton);
        doubleButton.addActionListener(doubleAction);
    }

    public void createToolbarAbout(JToolBar toolBar) {
        JButton aboutButton = new JButton();
        aboutButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/about.png")));
        aboutButton.setToolTipText("About author");
        toolBar.add(aboutButton);
        aboutButton.addActionListener(aboutAuthorAction);
    }

    public void createToolbarBlackWhiteF(JToolBar toolBar) {
        JButton blackWhiteButton = new JButton();
        blackWhiteButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/blackwhite.png")));
        blackWhiteButton.setToolTipText("Black & White");
        toolBar.add(blackWhiteButton);
        blackWhiteButton.addActionListener(blackWhiteAction);
    }
    public void createToolbarMirrorF(JToolBar toolBar) {
        JButton mirrorButton = new JButton();
        mirrorButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/blackwhite.png")));
        mirrorButton.setToolTipText("Mirror");
        toolBar.add(mirrorButton);
        mirrorButton.addActionListener(mirrorAction);
    }


    public void createToolbarWaterColor(JToolBar toolBar) {
        JButton waterButton = new JButton();
        waterButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/water.png")));
        waterButton.setToolTipText("Watercolor");
        toolBar.add(waterButton);
        waterButton.addActionListener(waterAction);
    }

    public void createToolbarEmbossing(JToolBar toolBar) {
        JButton embossingButton = new JButton();
        embossingButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/embossing.png")));
        embossingButton.setToolTipText("Embossing");
        toolBar.add(embossingButton);
        embossingButton.addActionListener(embossingAction);
    }

    public void createToolbarRotate(JToolBar toolBar) {
        JButton rotateButton = new JButton();
        rotateButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/rotate.png")));
        rotateButton.setToolTipText("Rotate");
        toolBar.add(rotateButton);
        rotateButton.addActionListener(rotateAction);
    }

    public void createToolbarSobel(JToolBar toolBar) {
        JButton sobelButton = new JButton();
        sobelButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/sobel.png")));
        sobelButton.setToolTipText("Sobel");
        toolBar.add(sobelButton);
        sobelButton.addActionListener(sobelAction);
    }

    public void createToolbarRobert(JToolBar toolBar) {
        JButton robertButton = new JButton();
        robertButton.setIcon(new ImageIcon(MenuWindow.class.getResource("images/robert.png")));
        robertButton.setToolTipText("Robert");
        toolBar.add(robertButton);
        robertButton.addActionListener(robertAction);
    }


    public void aboutAuthor() {
        JFrame windowInfoAuthor = new JFrame("About an author");
        ImageIcon photoAuthor = new ImageIcon(new ImageIcon(MenuWindow.class.getResource("images/photo.jpg")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)/*"images/avatar.png"*/);
        windowInfoAuthor.setSize(new Dimension(450, 350));
        windowInfoAuthor.setLocationRelativeTo(null);
        windowInfoAuthor.setVisible(true);
        windowInfoAuthor.setResizable(false);

        JLabel label = new JLabel(photoAuthor);
        JLabel describe = new JLabel("<html> Hello, I'm Darya Smirnova.<br>I'm studying at the  FIT NSU,<br> group 16208. <br>I create this game <br> for you, thx < 3</html>");
        windowInfoAuthor.add(label, BorderLayout.WEST);
        windowInfoAuthor.add(describe, BorderLayout.EAST);
        JButton close = new JButton("OK");
        windowInfoAuthor.add(close, BorderLayout.SOUTH);
        close.addActionListener(e -> windowInfoAuthor.dispose());

    }

    private void rotateSettings() {
        JDialog dialog = new JDialog(this, "Set angle", true);
        JPanel anglePanel = new JPanel();
        JSlider angleSlider = new JSlider(JSlider.HORIZONTAL);
        TextField angleField = new TextField("", 10);
        JButton save = new JButton("Save");
        int angle = 0;

        dialog.setLayout(new BorderLayout());
        anglePanel.setLayout(new GridLayout(2, 2));

        angleSlider.setMaximum(180);
        angleSlider.setMinimum(-180);

        save.addActionListener(d -> dialog.dispose());

        angleField.setText(String.valueOf(angle));
        angleSlider.setValue(angle);

        angleField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                angleSlider.setValue(Integer.valueOf(angleField.getText()));
            }
        });

        angleSlider.addChangeListener(s -> {
            int a = angleSlider.getValue();
            angleField.setText(String.valueOf(a));
            mainPanel.setAngle(a);
            mainPanel.rotateFilter();
        });

        anglePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Angle"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        anglePanel.add(angleField);
        anglePanel.add(angleSlider);
        anglePanel.add(save);

        dialog.add(anglePanel);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300, 130));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void createMenu() {

        setSize(1090, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu filterMenu = new JMenu("Filters");
        JMenu helpMenu = new JMenu("Help");
        JRadioButtonMenuItem filtersSelect = new JRadioButtonMenuItem("Select");
        fileMenu.add(filtersSelect);

        /**
         * TOOLBAR
         * */
      //  createToolbarSelect(toolBar);
        createToolbarCopyC2B(toolBar);
        createToolbarBlackWhiteF(toolBar);
        createToolbarNegative(toolBar);
        createToolbarBlur(toolBar);
        createToolbarWaterColor(toolBar);
        createToolbarSharpness(toolBar);
        createToolbarEmbossing(toolBar);

        createToolbarDouble(toolBar);
        createToolbarRotate(toolBar);
        createToolbarSobel(toolBar);
        createToolbarRobert(toolBar);
        createToolbarOrdered(toolBar);
        createToolbarAbout(toolBar);
        createToolbarMirrorF(toolBar);
        toolBar.setFloatable(false);
        menuBar.add(fileMenu);
        menuBar.add(filterMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);


        add(toolBar, BorderLayout.PAGE_START);


        getContentPane().add(mainPanel);


/**
 * подпункты в меню*/
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");


        JMenuItem blackWhiteFMenuItem = new JMenuItem("Black and White");
        JMenuItem negativeFMenuItem = new JMenuItem("Negative");
        JMenuItem blurFMenuItem = new JMenuItem("Blur");

        JMenuItem infoProjectMenuItem = new JMenuItem("About project");

        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();//зделитель при нью и опен
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);

        filterMenu.add(blackWhiteFMenuItem);
        filterMenu.add(negativeFMenuItem);
        filterMenu.add(blurFMenuItem);

        helpMenu.add(infoProjectMenuItem);


        openMenuItem.addActionListener(openAction);
        blackWhiteFMenuItem.addActionListener(blackWhiteAction);
        blurFMenuItem.addActionListener(blurAction);


/**обработаем клик на кнопку exit*/
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(1);

            }
        });


        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);


    }
}


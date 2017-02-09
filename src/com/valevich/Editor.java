package com.valevich;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.Preferences;

import static java.util.prefs.Preferences.userNodeForPackage;

public class Editor {
    // Режим рисования 
    private int mode = 1;
    private int xPad;
    private int xf;
    private int yf;
    private int yPad;
    private int thickness = 2;
    private boolean isPressed = false;
    // текущий цвет
    private Color currentColor = Color.black;
    private Color themeColor = Color.DARK_GRAY;
    private Color themeColorSecond = Color.black;
    private Color themeAccentColor = Color.WHITE;

    private JFrame frame;
    private MyPanel panel;
    private MyMenuBar menuBar;
    private JMenu fileMenu;
    private JButton colorbutton;
    private JColorChooser colorChooser;
    // поверхность рисования
    private BufferedImage image;
    // если мы загружаем картинку
    private boolean isLoading = false;
    private String fileName;

    public Editor() {
        setTheme();
        configureFrame();
        createColorChooser();
    }

    private void setTheme() {
        Preferences prefs = Preferences.userNodeForPackage(com.valevich.Editor.class);
        String defaultValue = "DARK";
        String themeString = prefs.get("theme", defaultValue);
        if (!themeString.equals(defaultValue)) {
            themeColor = Color.WHITE;
            themeColorSecond = Color.LIGHT_GRAY;
            themeAccentColor = Color.black;
        }
    }

    private void configureFrame() {
        frame = new JFrame("Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.add(createPanel());
        frame.add(createToolbar());
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // если делаем загрузку, то изменение размеров формы
                // отрабатываем в коде загрузки
                if (!isLoading) {
                    panel.setSize(frame.getWidth(), frame.getHeight() - 60);
                    BufferedImage tempImage = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D d2 = tempImage.createGraphics();
                    d2.setColor(themeColor);
                    d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    tempImage.setData(image.getRaster());
                    image = tempImage;
                    panel.repaint();
                }
                isLoading = false;
            }
        });
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        menuBar = new MyMenuBar(themeColorSecond);
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        fileMenu = new JMenu("Файл");
        fileMenu.setForeground(themeAccentColor);
        fileMenu.add(createNewFileMenuItem());
        fileMenu.add(createLoadFileMenuItem());
        fileMenu.add(createSaveFileMenuItem());
        fileMenu.add(createSaveAsFileMenuItem());
        createThemeMenu(fileMenu);
        return fileMenu;
    }

    private void createThemeMenu(JMenu menu) {
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        Preferences prefs = Preferences.userNodeForPackage(com.valevich.Editor.class);
        JRadioButtonMenuItem lightThemeItem = new JRadioButtonMenuItem("Светлая тема");
        lightThemeItem.setSelected(!prefs.get("theme", "DARK").equals("DARK"));
        lightThemeItem.setMnemonic(KeyEvent.VK_R);
        lightThemeItem.addActionListener(e -> {
            prefs.put("theme","LIGHT");
            themeColor = Color.WHITE;
            themeColorSecond = Color.LIGHT_GRAY;
            themeAccentColor = Color.black;
            switchTheme();
        });
        group.add(lightThemeItem);
        menu.add(lightThemeItem);

        JRadioButtonMenuItem darkThemeItem = new JRadioButtonMenuItem("Темная тема");
        darkThemeItem.setMnemonic(KeyEvent.VK_O);
        darkThemeItem.setSelected(prefs.get("theme", "DARK").equals("DARK"));
        darkThemeItem.addActionListener(e -> {
            prefs.put("theme","DARK");
            themeColor = Color.DARK_GRAY;
            themeColorSecond = Color.black;
            themeAccentColor = Color.WHITE;
            switchTheme();
        });
        group.add(darkThemeItem);
        menu.add(darkThemeItem);
    }

    private void switchTheme() {
        Graphics2D d2 = (Graphics2D) image.getGraphics();
        d2.setColor(themeColor);
        d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        panel.repaint();
        menuBar.setColor(themeColorSecond);
        menuBar.repaint();
        fileMenu.setForeground(themeAccentColor);
        fileMenu.repaint();
    }

    private JMenuItem createNewFileMenuItem() {
        Action loadAction = new AbstractAction("Новый") {
            public void actionPerformed(ActionEvent event) {
                Graphics2D d2 = (Graphics2D) image.getGraphics();
                d2.setColor(themeColor);
                d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                panel.repaint();
            }
        };
        return new JMenuItem(loadAction);
    }

    private JMenuItem createLoadFileMenuItem() {
        Action loadAction = new AbstractAction("Открыть") {
            public void actionPerformed(ActionEvent event) {
                JFileChooser jf = new JFileChooser();
                int result = jf.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        // при выборе изображения подстраиваем размеры формы
                        // и панели под размеры данного изображения
                        fileName = jf.getSelectedFile().getAbsolutePath();
                        File iF = new File(fileName);
                        jf.addChoosableFileFilter(new TextFileFilter(".png"));
                        jf.addChoosableFileFilter(new TextFileFilter(".jpg"));
                        image = ImageIO.read(iF);
                        isLoading = true;
                        //frame.setSize(image.getWidth() + 40, image.getWidth() + 80);
                        panel.setSize(image.getWidth(), image.getWidth());
                        panel.repaint();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(frame, "Такого файла не существует");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Исключение ввода-вывода");
                    } catch (Exception ignored) {
                    }
                }
            }
        };
        return new JMenuItem(loadAction);
    }

    private JMenuItem createSaveFileMenuItem() {
        Action saveAction = new AbstractAction("Сохранить") {
            public void actionPerformed(ActionEvent event) {
                try {
                    JFileChooser jf = new JFileChooser();
                    // Создаем фильтры  файлов
                    TextFileFilter pngFilter = new TextFileFilter(".png");
                    TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                    if (fileName == null) {
                        // Добавляем фильтры
                        jf.addChoosableFileFilter(pngFilter);
                        jf.addChoosableFileFilter(jpgFilter);
                        int result = jf.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            fileName = jf.getSelectedFile().getAbsolutePath();
                        }
                    }
                    // Смотрим какой фильтр выбран
                    if (jf.getFileFilter() == pngFilter) {
                        ImageIO.write(image, "png", new File(fileName + ".png"));
                    } else {
                        ImageIO.write(image, "jpeg", new File(fileName + ".jpg"));
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода-вывода");
                }
            }
        };
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift S"));
        JMenuItem item = new JMenuItem(saveAction);
        item.getActionMap().put("save_file", saveAction);
        item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put((KeyStroke) saveAction.getValue(Action.ACCELERATOR_KEY),"save_file");
        return item;
    }

    private JMenuItem createSaveAsFileMenuItem() {
        Action saveAsAction = new AbstractAction("Сохранить как...") {
            public void actionPerformed(ActionEvent event) {
                try {
                    JFileChooser jf = new JFileChooser();
                    // Создаем фильтры для файлов
                    TextFileFilter pngFilter = new TextFileFilter(".png");
                    TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                    // Добавляем фильтры
                    jf.addChoosableFileFilter(pngFilter);
                    jf.addChoosableFileFilter(jpgFilter);
                    int result = jf.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        fileName = jf.getSelectedFile().getAbsolutePath();
                    }
                    // Смотрим какой фильтр выбран
                    if (jf.getFileFilter() == pngFilter) {
                        ImageIO.write(image, "png", new File(fileName + ".png"));
                    } else {
                        ImageIO.write(image, "jpeg", new File(fileName + ".jpg"));
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода-вывода");
                }
            }
        };
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        JMenuItem item = new JMenuItem(saveAsAction);
        item.getActionMap().put("save_file", saveAsAction);
        item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put((KeyStroke) saveAsAction.getValue(Action.ACCELERATOR_KEY),"save_file");

        return item;
    }

    private JPanel createPanel() {
        panel = new MyPanel();
        panel.setBounds(0, 40, 260, 260);
        panel.setBackground(themeColor);
        panel.setOpaque(true);

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isPressed) {
                    Graphics g = image.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    // установка цвета
                    g2.setColor(currentColor);
                    g2.setStroke(new BasicStroke(thickness));
                    switch (mode) {
                        // кисть
                        case 1:
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // ластик
                        case 2:
                            g2.setColor(themeColor);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                    }
                    xPad = e.getX();
                    yPad = e.getY();
                }
                panel.repaint();
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Graphics g = image.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                // установка цвета
                g2.setColor(currentColor);
                g2.setStroke(new BasicStroke(thickness));
                switch (mode) {
                    // кисть
                    case 1:
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    // ластик
                    case 2:
                        g2.setColor(themeColor);
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    // текст
                    case 3:
                        // устанавливаем фокус для панели,
                        // чтобы печатать на ней текст
                        panel.requestFocus();
                        break;
                }
                xPad = e.getX();
                yPad = e.getY();

                isPressed = true;
                panel.repaint();
            }

            public void mousePressed(MouseEvent e) {
                xPad = e.getX();
                yPad = e.getY();
                xf = e.getX();
                yf = e.getY();
                isPressed = true;
            }

            public void mouseReleased(MouseEvent e) {

                Graphics g = image.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                // установка цвета
                g2.setColor(currentColor);
                g2.setStroke(new BasicStroke(thickness));
                // Общие рассчеты для овала и прямоугольника
                int x1 = xf, x2 = xPad, y1 = yf, y2 = yPad;
                if (xf > xPad) {
                    x2 = xf;
                    x1 = xPad;
                }
                if (yf > yPad) {
                    y2 = yf;
                    y1 = yPad;
                }
                switch (mode) {
                    // линия
                    case 4:
                        g.drawLine(xf, yf, e.getX(), e.getY());
                        break;
                    // круг
                    case 5:
                        g.drawOval(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    // прямоугольник
                    case 6:
                        g.drawRect(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                }
                xf = 0;
                yf = 0;
                isPressed = false;
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                // устанавливаем фокус для панели,
                // чтобы печатать на ней текст
                panel.requestFocus();
            }

            public void keyTyped(KeyEvent e) {
                if (mode == 3) {
                    Graphics g = image.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    // установка цвета
                    g2.setColor(currentColor);
                    g2.setStroke(new BasicStroke(thickness));

                    String str = "";
                    str += e.getKeyChar();
                    g2.setFont(new Font("Arial", 0, 15 * thickness));
                    g2.drawString(str, xPad, yPad);
                    xPad += 15 * thickness;
                    // устанавливаем фокус для панели,
                    // чтобы печатать на ней текст
                    panel.requestFocus();
                    panel.repaint();
                }
            }
        });

        return panel;
    }

    private JToolBar createToolbar() {

        MyToolBar toolbar = new MyToolBar("Toolbar", JToolBar.HORIZONTAL,themeColorSecond);

        JButton brushbutton = new JButton();
        brushbutton.setIcon(getIcon("/res/brush.png"));
        brushbutton.addActionListener(event -> mode = 1);
        toolbar.add(brushbutton);

        JButton lasticbutton = new JButton();
        lasticbutton.setIcon(getIcon("/res/lastic.png"));
        lasticbutton.addActionListener(event -> mode = 2);
        toolbar.add(lasticbutton);

        JButton textbutton = new JButton();
        textbutton.setIcon(getIcon("/res/text.png"));
        textbutton.addActionListener(event -> mode = 3);
        toolbar.add(textbutton);

        JButton linebutton = new JButton();
        linebutton.setIcon(getIcon("/res/line.png"));
        linebutton.addActionListener(event -> mode = 4);
        toolbar.add(linebutton);

        JButton elipsbutton = new JButton();
        elipsbutton.setIcon(getIcon("/res/elips.png"));
        elipsbutton.addActionListener(event -> mode = 5);
        toolbar.add(elipsbutton);

        JButton rectbutton = new JButton();
        rectbutton.setIcon(getIcon("/res/rect.png"));
        rectbutton.addActionListener(event -> mode = 6);
        toolbar.add(rectbutton);

        Integer[] thicknesses = {2, 3, 4, 5, 6, 7, 8, 9, 10};

        JComboBox<Integer> thicknessBox = new JComboBox<>(thicknesses);
        //thicknessBox.setBounds(300,0,50,40);
        thicknessBox.setSelectedIndex(0);
        thicknessBox.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            thickness = (int) cb.getSelectedItem();
        });
        toolbar.add(thicknessBox);

        colorbutton = new JButton();
        colorbutton.setBackground(currentColor);
        //colorbutton.setBounds(350,0,50,40);
        colorbutton.addActionListener(event -> {
            ColorDialog colorDialog = new ColorDialog(frame, "Выбор цвета",colorChooser);
            colorDialog.setBounds(0,0,400,400);
            colorDialog.setVisible(true);
        });
        toolbar.add(colorbutton);

        toolbar.setBounds(0, 0, 300, 40);
        toolbar.setFloatable(false);
        toolbar.setLayout(new GridLayout(1,8));

        return toolbar;

    }

    private ImageIcon getIcon(String path) {
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return image == null ? null : new ImageIcon(image);
    }

    private void createColorChooser() {
        colorChooser = new JColorChooser(currentColor);
        colorChooser.getSelectionModel().addChangeListener(e -> {
            currentColor = colorChooser.getColor();
            colorbutton.setBackground(currentColor);
        });
    }

    public static void start() {
        SwingUtilities.invokeLater(Editor::new);
    }


    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            if (image == null) {
                image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = image.createGraphics();
                d2.setColor(themeColor);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

}
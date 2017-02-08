package com.valevich;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Editor {
    // Режим рисования 
    private int mode = 0;
    private int xPad;
    int xf;
    int yf;
    int yPad;
    int thickness;
    private boolean isPressed = false;
    // текущий цвет
    private Color currentColor = Color.black;
    private JFrame frame;
    private MyPanel panel;
    private JButton colorbutton;
    private JColorChooser colorChooser;
    // поверхность рисования
    private BufferedImage image;
    // если мы загружаем картинку
    private boolean isLoading = false;
    private String fileName;

    public Editor() {

        configureFrame();


        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.VERTICAL);

        JButton penbutton = new JButton(new ImageIcon("pen.png"));
        penbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 0;
            }
        });
        toolbar.add(penbutton);
        JButton brushbutton = new JButton(new ImageIcon("brush.png"));
        brushbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 1;
            }
        });
        toolbar.add(brushbutton);

        JButton lasticbutton = new JButton(new ImageIcon("lastic.png"));
        lasticbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 2;
            }
        });
        toolbar.add(lasticbutton);

        JButton textbutton = new JButton(new ImageIcon("text.png"));
        textbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 3;
            }
        });
        toolbar.add(textbutton);

        JButton linebutton = new JButton(new ImageIcon("res/line.png"));
        linebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 4;
            }
        });
        toolbar.add(linebutton);

        JButton elipsbutton = new JButton(new ImageIcon("elips.png"));
        elipsbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 5;
            }
        });
        toolbar.add(elipsbutton);

        JButton rectbutton = new JButton(new ImageIcon("rect.png"));
        rectbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 6;
            }
        });
        toolbar.add(rectbutton);

        toolbar.setBounds(0, 0, 30, 300);
        frame.add(toolbar);

        // Тулбар для кнопок
        JToolBar colorbar = new JToolBar("Colorbar", JToolBar.HORIZONTAL);
        colorbar.setBounds(30, 0, 260, 30);
        colorbutton = new JButton();
        colorbutton.setBackground(currentColor);
        colorbutton.setBounds(15, 5, 20, 20);
        colorbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ColorDialog coldi = new ColorDialog(frame, "Выбор цвета");
                coldi.setVisible(true);
            }
        });
        colorbar.add(colorbutton);

        JButton redbutton = new JButton();
        redbutton.setBackground(Color.red);
        redbutton.setBounds(40, 5, 15, 15);
        redbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.red;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(redbutton);

        JButton orangebutton = new JButton();
        orangebutton.setBackground(Color.orange);
        orangebutton.setBounds(60, 5, 15, 15);
        orangebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.orange;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(orangebutton);

        JButton yellowbutton = new JButton();
        yellowbutton.setBackground(Color.yellow);
        yellowbutton.setBounds(80, 5, 15, 15);
        yellowbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.yellow;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(yellowbutton);

        JButton greenbutton = new JButton();
        greenbutton.setBackground(Color.green);
        greenbutton.setBounds(100, 5, 15, 15);
        greenbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.green;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(greenbutton);

        JButton bluebutton = new JButton();
        bluebutton.setBackground(Color.blue);
        bluebutton.setBounds(120, 5, 15, 15);
        bluebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.blue;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(bluebutton);

        JButton cyanbutton = new JButton();
        cyanbutton.setBackground(Color.cyan);
        cyanbutton.setBounds(140, 5, 15, 15);
        cyanbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.cyan;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(cyanbutton);

        JButton magentabutton = new JButton();
        magentabutton.setBackground(Color.magenta);
        magentabutton.setBounds(160, 5, 15, 15);
        magentabutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.magenta;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(magentabutton);

        JButton whitebutton = new JButton();
        whitebutton.setBackground(Color.white);
        whitebutton.setBounds(180, 5, 15, 15);
        whitebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.white;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(whitebutton);

        JButton blackbutton = new JButton();
        blackbutton.setBackground(Color.black);
        blackbutton.setBounds(200, 5, 15, 15);
        blackbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentColor = Color.black;
                colorbutton.setBackground(currentColor);
            }
        });
        colorbar.add(blackbutton);
        colorbar.setLayout(null);
        frame.add(colorbar);

        colorChooser = new JColorChooser(currentColor);
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                currentColor = colorChooser.getColor();
                colorbutton.setBackground(currentColor);
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isPressed == true) {
                    Graphics g = image.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    // установка цвета
                    g2.setColor(currentColor);
                    switch (mode) {
                        // карандаш
                        case 0:
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // кисть
                        case 1:
                            g2.setStroke(new BasicStroke(3.0f));
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // ластик
                        case 2:
                            g2.setStroke(new BasicStroke(3.0f));
                            g2.setColor(Color.WHITE);
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
                switch (mode) {
                    // карандаш
                    case 0:
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    // кисть
                    case 1:
                        g2.setStroke(new BasicStroke(3.0f));
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    // ластик
                    case 2:
                        g2.setStroke(new BasicStroke(3.0f));
                        g2.setColor(Color.WHITE);
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
                    g2.setStroke(new BasicStroke(2.0f));

                    String str = new String("");
                    str += e.getKeyChar();
                    g2.setFont(new Font("Arial", 0, 15));
                    g2.drawString(str, xPad, yPad);
                    xPad += 10;
                    // устанавливаем фокус для панели,
                    // чтобы печатать на ней текст
                    panel.requestFocus();
                    panel.repaint();
                }
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // если делаем загрузку, то изменение размеров формы
                // отрабатываем в коде загрузки
                if (isLoading == false) {
                    panel.setSize(frame.getWidth() - 40, frame.getHeight() - 80);
                    BufferedImage tempImage = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
                    d2.setColor(Color.white);
                    d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    tempImage.setData(image.getRaster());
                    image = tempImage;
                    panel.repaint();
                }
                isLoading = false;
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void configureFrame() {
        frame = new JFrame("Графический редактор");
        frame.setBounds(350,0,1000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.add(createPanel());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 350, 60);
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.add(createLoadFileMenuItem());
        fileMenu.add(createSaveFileMenuItem());
        fileMenu.add(createSaveAsFileMenuItem());
        return fileMenu;
    }

    private JMenuItem createLoadFileMenuItem() {
        Action loadAction = new AbstractAction("Загрузить") {
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
                        frame.setSize(image.getWidth() + 40, image.getWidth() + 80);
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
        return new JMenuItem(saveAction);
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
        return new JMenuItem(saveAsAction);
    }

    private JPanel createPanel() {
        panel = new MyPanel();
        panel.setBounds(30, 30, 260, 260);
        panel.setBackground(Color.white);
        panel.setOpaque(true);
        return panel;
    }

    public static void start() {
        SwingUtilities.invokeLater(Editor::new);
    }

    class ColorDialog extends JDialog {
        public ColorDialog(JFrame owner, String title) {
            super(owner, title, true);
            add(colorChooser);
            setSize(200, 200);
        }
    }

    class MyPanel extends JPanel {
        public MyPanel() {
        }

        public void paintComponent(Graphics g) {
            if (image == null) {
                image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = (Graphics2D) image.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    // Фильтр картинок
    class TextFileFilter extends FileFilter {
        private String ext;

        public TextFileFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(java.io.File file) {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }

        public String getDescription() {
            return "*" + ext;
        }
    }
}
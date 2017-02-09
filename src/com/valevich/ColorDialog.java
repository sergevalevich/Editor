package com.valevich;

import javax.swing.*;

class ColorDialog extends JDialog {
    ColorDialog(JFrame owner, String title,JColorChooser colorChooser) {
        super(owner, title, true);
        add(colorChooser);
        setSize(200, 200);
    }

}

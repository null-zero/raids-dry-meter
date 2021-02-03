package com.raidsdrymeter.ui;

import com.raidsdrymeter.data.UniqueItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class UniquePanel extends JPanel {
    private final float alphaHas = 1.0f;

    private static final Dimension panelSize = new Dimension(215, 50);
    private static final Border panelBorder = new EmptyBorder(3, 0, 3, 0);
    private static final Color panelBackgroundColor = ColorScheme.DARKER_GRAY_COLOR;

    UniquePanel(final Collection<UniqueItem> items, final ItemManager itemManager, final int itemMissingAlpha)
    {
        float alphaMissing = itemMissingAlpha / 100f;
        final JPanel panel = new JPanel();
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setBorder(new EmptyBorder(3, 6, 0, 3));


        this.setLayout(new BorderLayout());
        this.setBorder(panelBorder);
        this.setBackground(panelBackgroundColor);
        this.setPreferredSize(panelSize);


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 5;

        for(final UniqueItem l : items)
        {
            final int quantity = l.getQty();
            final float alpha = (quantity > 0 ? alphaHas : alphaMissing);
            final AsyncBufferedImage image = itemManager.getImage(l.getItemID(), quantity, quantity > 1);
            final BufferedImage opaque = ImageUtil.alphaOffset(image, alpha);

            final JLabel icon = new JLabel();
            icon.setToolTipText(buildToolTip(l, quantity));
            icon.setIcon(new ImageIcon(opaque));
            icon.setVerticalAlignment(SwingConstants.CENTER);
            icon.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(icon, c);
            c.gridx++;

            // in case the image is blank we will refresh it upon load
            // Should only trigger if image hasn't been added
            image.onLoaded(() ->
            {
                icon.setIcon(new ImageIcon(ImageUtil.alphaOffset(image, alpha)));
                icon.revalidate();
                icon.repaint();
            });
        }
        this.add(panel, BorderLayout.NORTH);
    }

    private static String buildToolTip(final UniqueItem item, final int qty)
    {
        String s = "<html>" + item.getName();

        if(item.getPrice() > 0){
            s += "<br/>(x" + QuantityFormatter.quantityToStackSize(item.getPrice()) + ")"; //getPrice
        }

        if (qty > 0)
        {
            s += "<br/>x " + QuantityFormatter.formatNumber(qty);
        }

        if(item.getPrice() > 0 && qty > 0)
        {
            s += "<br/>(" + QuantityFormatter.quantityToStackSize(qty * item.getPrice()) + ")</html;";
        }


        s += "</html>";

        return s;
    }

}

package com.raidsdrymeter.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.border.Border;

class SymmetricalDashedBorder implements Border {

	private final float[] dashPattern;
	private final int borderWidth;
	private final Color borderColor;

	private final int sides;

	public SymmetricalDashedBorder(float[] dashPattern, int borderWidth, Color borderColor, int sides) {
		this.dashPattern = dashPattern;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		this.sides = sides;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, dashPattern, 0));
		g2d.setColor(borderColor);

		if (this.sides == 1)
		{
			// Draw left border
			g2d.drawLine(x + borderWidth / 2, y, x + borderWidth / 2, y + height - 1);
		}
		if (this.sides == 2)
		{
			// Draw right border
			g2d.drawLine(x + width - 1 - borderWidth / 2, y, x + width - 1 - borderWidth / 2, y + height - 1);
		}

		if (this.sides == 3) {
			g2d.drawLine(x + borderWidth / 2, y, x + borderWidth / 2, y + height - 1);
			g2d.drawLine(x + width - 1 - borderWidth / 2, y, x + width - 1 - borderWidth / 2, y + height - 1);
		}
		g2d.dispose();
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(borderWidth, borderWidth, borderWidth, borderWidth);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}
}
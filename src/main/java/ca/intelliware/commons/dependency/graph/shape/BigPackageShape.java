package ca.intelliware.commons.dependency.graph.shape;

import static ca.intelliware.commons.dependency.graph.shape.CommonImage.PACKAGE_BIG_ICON;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

import ca.intelliware.commons.dependency.Node;

public class BigPackageShape<T> extends PackageShape<T> {
	
	public BigPackageShape() {
		setDimension(new Dimension(120, 70));
	}

	protected void draw(Graphics2D graphics, Node<T> node) {
		double x = (getWidth() - getPackageImage().getIconWidth()) / 2.0;
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(getPackageImage().getImage(), (int) x, 0, null);
		graphics.setColor(getPlot().getShapeLineColor());
		drawNodeName(graphics, node);
	}
	
	@Override
	protected ImageIcon getPackageImage() {
		return PACKAGE_BIG_ICON.getImage();
	}

	@Override
	protected double getTextAreaWidth() {
		return getWidth();
	}
}

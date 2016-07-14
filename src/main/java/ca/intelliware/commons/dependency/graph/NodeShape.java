package ca.intelliware.commons.dependency.graph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import ca.intelliware.commons.dependency.Node;

/**
 * <p>A class that takes responsibility for drawing a particular shape.  This class can 
 * be subclassed to provide different shapes for various types of directed graph images.
 * 
 * <p>The simplest representation of a node is to draw a rectangle.
 * 
 * @author BC Holmes
 */
public class NodeShape<T> {

	private Dimension dimension = new Dimension(100, 50);
	private Plot plot;
	private Font font;

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	protected void draw(Graphics2D graphics, Node<T> node) {
		graphics.setPaint(getPlot().getShadowColor());
		graphics.fill(new Rectangle2D.Float(3, 3, this.dimension.width, this.dimension.height));

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setPaint(this.plot.getShapeFillColor());
		graphics.fill(new Rectangle2D.Float(0, 0, this.dimension.width, this.dimension.height));
		graphics.setColor(this.plot.getShapeLineColor());
		graphics.draw(new Rectangle2D.Float(0, 0, this.dimension.width, this.dimension.height));

		drawNodeName(graphics, node);
	}

	protected void drawNodeName(Graphics2D graphics, Node<T> node) {
		drawNodeName(graphics, node, this.dimension.getWidth() / 2.0, this.dimension.getHeight() / 2.0);
	}

	protected void drawNodeName(Graphics2D graphics, Node<T> node, double x, double y) {
		drawNodeName(graphics, node, new Point2D.Double(x, y));
	}
	
	protected void drawNodeName(Graphics2D graphics, Node<T> node, Point2D.Double centredAt) {
		graphics = (Graphics2D) graphics.create();
		try {
			graphics.translate(centredAt.getX(), centredAt.getY());
			graphics.setFont(this.font);
			drawString(graphics, node.getName());
		} finally {
			graphics.dispose();
		}
	}

	/**
	 * @param graphics - a temporary graphics reference, where 0,0 is the centre of the
	 *                   text to be drawn.  We assume that it'll be disposed immediately
	 *                   after this method call.
	 * @param text - the text to draw
	 */
	protected void drawString(Graphics2D graphics, String text) {
		FontMetrics metrics = graphics.getFontMetrics();
		Rectangle2D bounds = metrics.getStringBounds(text, graphics);
		double y = metrics.getAscent() + metrics.getLeading() - (bounds.getHeight() / 2.0);
		double x = -bounds.getWidth() / 2.0;
		graphics.drawString(text, (float) x, (float) y);
	}

	public double getHeight() {
		return this.dimension.getHeight();
	}

	public double getWidth() {
		return this.dimension.getWidth();
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * <p>Scan the list of nodes to determine information about how to draw each one.  
	 * For example, this method might try to determine the best font size to render the
	 * names in the space available.
	 * 
	 * @param graphics
	 * @param nodes
	 */
	public void initialize(Graphics2D graphics, List<Node<T>> nodes) {
		FontMetrics metrics = graphics.getFontMetrics();
		double width = 1.0;
		for (Node<T> node : nodes) {
			Rectangle2D bounds = metrics.getStringBounds(node.getName(), graphics);
			width = Math.max(bounds.getWidth(), width);
		}
		
		double ratio = Math.max(1.0, width / getTextAreaWidth());
		
		Font font = graphics.getFont();
		AffineTransform transform = new AffineTransform();
		transform.scale(1.00 / ratio, 1.0 / ratio);
		this.font = font.deriveFont(transform);
	}

	protected double getTextAreaWidth() {
		return getWidth() * 0.8;
	}

	protected Font getFont() {
		return this.font;
	}

	protected void setFont(Font font) {
		this.font = font;
	}
}

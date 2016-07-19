package ca.intelliware.commons.dependency.graph;

import static ca.intelliware.commons.dependency.graph.CoordinateSystem.HEIGHT_SCALE_FACTOR;
import static ca.intelliware.commons.dependency.graph.CoordinateSystem.WIDTH_SCALE_FACTOR;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import ca.intelliware.commons.dependency.DependencyManager;
import ca.intelliware.commons.dependency.Layer;
import ca.intelliware.commons.dependency.Node;
import ca.intelliware.commons.dependency.graph.Graph.BasicVertex;

@SuppressWarnings("unchecked")
public class Grapher<T> {

	public interface Drawer {
		public void drawGraph(Graphics2D g2);
	}

	private final DependencyManager<T> dependencyManager;
	private Plot plot = new Plot();
	private NodeShape<T> shape = new NodeShape<T>();
	private ArrowShape arrowShape = new ArrowShape();
	private Map<Object, Rectangle2D> locations = Collections.synchronizedMap(new HashMap<Object, Rectangle2D>());
	private Graph graph;
	private CoordinateSystem coordinateSystem;

	public Grapher(DependencyManager<T> dependencyManager) {
		this.dependencyManager = dependencyManager;
	}

	protected synchronized void draw(Graphics2D graphics, Rectangle2D rectangle) {
		double scale = calculateScale(rectangle);
		draw(graphics, rectangle, scale);
	}

	private synchronized void initialize() {
		this.shape.setPlot(this.plot);
		this.arrowShape.setPlot(this.plot);
		this.graph = new SugiyamaAlgorithm().apply(this.dependencyManager);
	}
	
	private synchronized void draw(Graphics2D graphics, Rectangle2D rectangle, double scale) {
		rectangle = scale(scale, rectangle);
		graphics.scale(1.0/scale, 1.0/scale);
		
		this.shape.initialize(graphics, this.dependencyManager.getLayeredGraph().getNodes());
		
		drawLayerBars(graphics, rectangle);

		this.coordinateSystem = new CoordinateSystem(this.shape, this.graph.getHeight());
		double excessWidth = rectangle.getWidth() - this.coordinateSystem.getWidth(this.graph.getWidth());
		double excessHeight = rectangle.getHeight() - this.coordinateSystem.getTotalHeight();
		graphics.translate(excessWidth / 2.0, excessHeight);
		rectangle = new Rectangle2D.Double(rectangle.getX() + getLeftBorder(),
				rectangle.getY(),
				rectangle.getWidth() - (2 * getLeftBorder()), rectangle.getHeight());

		
		drawBoxes(graphics);
		drawArrows(graphics);
	}

	private Rectangle2D scale(double scale, Rectangle2D rectangle) {
		return new Rectangle2D.Double(rectangle.getX(), rectangle.getY(),
				rectangle.getWidth() * scale, rectangle.getHeight() * scale);
	}

	private double calculateScale(Rectangle2D rectangle) {
		Dimension dimension = getPreferredDimension();
		if (dimension.getHeight() / rectangle.getHeight() > dimension.getWidth() / rectangle.getWidth()) {
			return dimension.getHeight() / rectangle.getHeight();
		} else {
			return dimension.getWidth() / rectangle.getWidth();
		}
	}

	private Dimension getPreferredDimension() {
		List<Layer<Node<T>>> layers = this.dependencyManager.getNodeLayers();
		double height = layers.size() * getLayerBandHeight();
		double shapeWidth = this.shape.getWidth();
		double width = this.graph.getWidth() * shapeWidth * WIDTH_SCALE_FACTOR + getLeftBorder();
		
		Dimension dimension = new Dimension();
		dimension.setSize(width, height);
		return dimension;
	}

	private double getLeftBorder() {
		return 5.0;
	}

	private void drawArrows(Graphics2D graphics) {
		for (GraphLayer layer : this.graph.getLayers()) {
			drawArrows(graphics, layer);
		}
	}

	private void drawArrows(Graphics2D graphics, GraphLayer layer) {
		if (layer.getLevelNumber() > 0) {
			for (Vertex vertex : layer.getOrderedContents()) {
				List<Vertex> dependencies = vertex.getNeighboursInLayer(layer.getLevelNumber()-1);
				for (Vertex dependency : dependencies) {
					drawArrow(graphics, vertex, dependency);
				}
			}
		}
	}

	private void drawArrow(Graphics2D graphics, Vertex vertex, Vertex dependency) {
		if (!dependency.isDummy() && !vertex.isDummy()) {
			Node<T> node = ((Graph.BasicVertex) vertex).getNode();
			Object object = ((Graph.BasicVertex) dependency).getNode().getItem();
			Arrow line = BoundsUtil.getEndPoints(getBounds(node.getItem()), getBounds(object));
			this.arrowShape.drawArrow(graphics, line);
			if (((BasicVertex) vertex).isBidirectionalWith((BasicVertex) dependency)) {
				Point2D to = getBoundsCenter(node.getItem(), 0.05 * this.shape.getWidth());
				Point2D from = getBoundsCenter(object, 0.05 * this.shape.getWidth());
				
				Arrow arrow = new Arrow(Arrays.asList(from, to));
				arrow = arrow.clipEnd(getBounds(node.getItem()));
				arrow = arrow.clipStart(getBounds(object));
				System.out.println(arrow);
				this.arrowShape.drawArrow(graphics, arrow);
			}
		} else if (!vertex.isDummy() && dependency.isDummy()) {
			this.arrowShape.drawArrow(graphics, getArrow(dependency));
		}
	}

	private Point2D getBoundsCenter(Object item, double offset) {
		Rectangle2D bounds = getBounds(item);
		return new Point2D.Double(bounds.getCenterX() + offset, bounds.getCenterY());
	}

	private Arrow getArrow(Vertex dependency) {
		DummyVertex lastDummy = getLastDummy((DummyVertex) dependency);
		Vertex end = lastDummy.getLower();
		
		DummyVertex firstDummy = getFirstDummy((DummyVertex) dependency);
		Vertex start = firstDummy.getUpper();
		
		boolean reversed = end.getLayer() > start.getLayer();
		
		Vertex previous = reversed ? end : start;
		Point2D.Double point1 = new Point2D.Double(this.coordinateSystem.getCenterX(dependency),
				this.coordinateSystem.getTopY(reversed ? lastDummy : firstDummy ));
		Arrow arrow = BoundsUtil.getEndPoints(getBounds(previous), point1);
		
		for (Vertex v = reversed ? lastDummy : firstDummy; v.isDummy(); 
				v = reversed ? ((DummyVertex) v).getLower() : ((DummyVertex) v).getUpper()) {
			
			if (previous.isDummy()) {
				Point2D top = new Point2D.Double(this.coordinateSystem.getCenterX(previous),
						this.coordinateSystem.getBottomY(previous));
				Point2D bottom = new Point2D.Double(this.coordinateSystem.getCenterX(v),
						this.coordinateSystem.getTopY(v));
				arrow = Arrow.join(arrow, new Arrow(Arrays.asList(top, bottom)));
			}
			previous = v;
		}

		DummyVertex penultimate = reversed ? firstDummy : lastDummy;
		Point2D.Double point2 = new Point2D.Double(this.coordinateSystem.getCenterX(penultimate),
				this.coordinateSystem.getBottomY(penultimate.getLayer()));
		arrow = Arrow.join(arrow, BoundsUtil.getEndPoints(point2, getBounds(reversed ? start : end)));
		
		return reversed ? Arrow.reverse(arrow) : arrow;
	}

	private DummyVertex getFirstDummy(DummyVertex vertex) {
		if (vertex.getUpper().isDummy()) {
			return getFirstDummy((DummyVertex) vertex.getUpper());
		} else {
			return vertex;
		}
	}

	private Rectangle2D getBounds(Vertex vertex) {
		return getBounds(((Graph.BasicVertex) vertex).getNode().getItem());
	}

	private DummyVertex getLastDummy(DummyVertex vertex) {
		if (vertex.getLower().isDummy()) {
			return getLastDummy((DummyVertex) vertex.getLower());
		} else {
			return vertex;
		}
	}

	private Rectangle2D getBounds(Object item) {
		return this.locations.get(item);
	}

	private void drawBoxes(Graphics2D graphics) {
		List<GraphLayer> layers = this.graph.getLayers();
		for (int i = 0, length = layers.size() ; i < length; i++) {
			drawLayer(graphics, i, layers.get(i));
		}
	}

	private void drawLayer(Graphics2D graphics, int layerNumber, GraphLayer layer) {
		for (Vertex vertex : layer.getOrderedContents()) {
			this.coordinateSystem.getCenterX(vertex);
			if (!vertex.isDummy()) {
				Node<T> node = ((Graph.BasicVertex) vertex).getNode();
				Graphics2D g = (Graphics2D) graphics.create();
				double x = this.coordinateSystem.getLeftX(vertex);
				double y = this.coordinateSystem.getTopY(vertex);
				g.translate(x, y);
				this.locations.put(node.getItem(),
						new Rectangle2D.Double(x, y,
								this.shape.getWidth(),
								this.shape.getHeight()));
				drawNode(g, node);
			}
		}
	}

	private void drawNode(Graphics2D graphics, Node<T> node) {
		this.shape.draw(graphics, node);
	}

	private void drawLayerBars(Graphics2D graphics, Rectangle2D r) {
		double height = getLayerBandHeight();
		boolean alternate = false;
		for (double i = r.getHeight()-height; i > -height; i -= (height)) {
			graphics.setPaint(alternate ? this.plot.getLayerBackgroundColor() : this.plot.getLayerAlternatingColor());
			graphics.fill(new Rectangle2D.Double(r.getX(), i, r.getWidth(), height));
			alternate = !alternate;
		}
	}

	private double getLayerBandHeight() {
		return this.shape.getHeight() * HEIGHT_SCALE_FACTOR;
	}

	public synchronized Dimension createPng(OutputStream output, final int width, final int height) throws IOException {
		initialize();
		Drawer drawer = new Drawer() {
			public void drawGraph(Graphics2D g2) {
				draw(g2, new Rectangle2D.Float(0, 0, width, height));
			}
		};
		Dimension d = new Dimension(width, height);
		ImageIO.write(createBufferedImage(d, drawer), "png", output);
		return d;
	}
	public synchronized Dimension createPng(OutputStream output) throws IOException {
		initialize();
		final Dimension dimension = getPreferredDimension();
		Drawer drawer = new Drawer() {
			public void drawGraph(Graphics2D g2) {
				draw(g2, new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight()), 1.0);
			}
		};
		ImageIO.write(createBufferedImage(dimension, drawer), "png", output);
		return dimension;
	}

	private BufferedImage createBufferedImage(final Dimension dimension, Drawer drawer) {
		BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		drawer.drawGraph(g2);
		g2.dispose();
		return image;
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public NodeShape<T> getShape() {
		return this.shape;
	}

	public void setShape(NodeShape<T> shape) {
		this.shape = shape;
	}
}

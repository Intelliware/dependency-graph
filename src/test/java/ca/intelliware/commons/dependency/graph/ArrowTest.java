package ca.intelliware.commons.dependency.graph;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import junit.framework.TestCase;

public class ArrowTest extends TestCase {
	
	public void testClipEnd() throws Exception {
		Arrow arrow = new Arrow(Arrays.asList(asPoint(0,0), asPoint(5,5)));
		Arrow clipped = arrow.clipEnd(new Rectangle2D.Double(4, 4, 2, 2));
		
		assertEquals("x", 4.0, clipped.getLastPoint().getX());
		assertEquals("y", 4.0, clipped.getLastPoint().getY());
	}

	public void testClipStart() throws Exception {
		Arrow arrow = new Arrow(Arrays.asList(asPoint(1,1), asPoint(5,5)));
		Arrow clipped = arrow.clipStart(new Rectangle2D.Double(0, 0, 2, 2));
		
		assertEquals("x", 2.0, clipped.getFirstPoint().getX());
		assertEquals("y", 2.0, clipped.getFirstPoint().getY());
	}
	
	public void testClipStartVertical() throws Exception {
		Arrow arrow = new Arrow(Arrays.asList(asPoint(1,1), asPoint(5,1)));
		Arrow clipped = arrow.clipStart(new Rectangle2D.Double(0, 4, 2, 2));
		
		assertEquals("x", 2.0, clipped.getFirstPoint().getX());
		assertEquals("y", 1.0, clipped.getFirstPoint().getY());
	}
	
	public void testClipEndVertical() throws Exception {
		Arrow arrow = new Arrow(Arrays.asList(asPoint(5,5), asPoint(1,5)));
		Arrow clipped = arrow.clipEnd(new Rectangle2D.Double(0, 4, 2, 2));
		
		assertEquals("x", 2.0, clipped.getLastPoint().getX());
		assertEquals("y", 5.0, clipped.getLastPoint().getY());
	}
	
	public void testClipEndDownward() throws Exception {
		Arrow arrow = new Arrow(Arrays.asList(asPoint(0,10), asPoint(5,5)));
		Arrow clipped = arrow.clipEnd(new Rectangle2D.Double(4, 4, 2, 2));
		
		assertEquals("x", 4.0, clipped.getLastPoint().getX());
		assertEquals("y", 6.0, clipped.getLastPoint().getY());
	}
	
	private Point2D asPoint(double x, double y) {
		return new Point2D.Double(x, y);
	}
}

package ca.intelliware.commons.dependency.graph;

import junit.framework.TestCase;

public class CoordinateSystemTest extends TestCase {
	
	private final class Coordinate implements HorizontalCoordinate {
		private final double x;
		public Coordinate(double x) {
			this.x = x;
		}
		public double getPosition() {
			return this.x;
		}
	}

	public void testGetWidth() throws Exception {
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		assertEquals("width", 500.0, coordinateSystem.getWidth(4));
	}	
	
	public void testGetTopY() throws Exception {
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		assertEquals("top x", 325.0, coordinateSystem.getTopY(0));
		assertEquals("bottom x", 375.0, coordinateSystem.getBottomY(0));
		assertEquals("top x", 225.0, coordinateSystem.getTopY(1));
		assertEquals("bottom x", 275.0, coordinateSystem.getBottomY(1));
		assertEquals("total", 400.0, coordinateSystem.getTotalHeight());
	}
	
	public void testGetCentreX() throws Exception {
		
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		MockVertex vertex = new MockVertex("fred", 0, 0);
		vertex.setHorizontalCoordinate(new Coordinate(0.5));
		assertEquals("centre x", 62.5, coordinateSystem.getCenterX(vertex));
		assertEquals("left x", 12.5, coordinateSystem.getLeftX(vertex));

		MockVertex vertex2 = new MockVertex("barney", 1, 0);
		vertex2.setHorizontalCoordinate(new Coordinate(2.5));
		
		assertEquals("centre x2", 312.5, coordinateSystem.getCenterX(vertex2));
		assertEquals("left x2", 262.5, coordinateSystem.getLeftX(vertex2));
	}

}

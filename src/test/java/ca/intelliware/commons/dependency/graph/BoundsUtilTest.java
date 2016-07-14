package ca.intelliware.commons.dependency.graph;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

public class BoundsUtilTest extends TestCase {

	public void testCalculationOfEndPointsWithOneBoxOnTopOfOther() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(0, 0, 200, 100), new Rectangle2D.Double(0, 400, 200, 100));
		assertEquals("P1.x", 100.0, arrow.getFirstPoint().getX());
		assertEquals("P2.x", 100.0, arrow.getLastPoint().getX());
	}

	public void testCalculationOfEndPointsSimpleCase() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(0, 0, 200, 100), new Rectangle2D.Double(400, 400, 200, 100));
		assertEquals("P1.x", 150.0, arrow.getFirstPoint().getX());
		assertEquals("P2.x", 450.0, arrow.getLastPoint().getX());
		
		assertFalse("upward", arrow.isPointingUpward());
	}
	
	public void testCalculationOfEndPointsFromLowerToUpper() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(400, 400, 200, 100), new Rectangle2D.Double(0, 0, 200, 100));
		assertEquals("P1.x", 450.0, arrow.getFirstPoint().getX());
		assertEquals("P2.x", 150.0, arrow.getLastPoint().getX());
		
		assertTrue("upward", arrow.isPointingUpward());
	}
}

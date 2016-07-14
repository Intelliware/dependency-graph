package ca.intelliware.commons.dependency.graph;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

public class LinearFunctionTest extends TestCase {

	private static final double THIRTY_DEGREES_IN_RADIANS = Math.PI / 6.0;
	private static final double FORTY_FIVE_DEGREES_IN_RADIANS = Math.PI / 4.0;

	public void testCreateTrivialLinearFunction() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 1), new Point2D.Double(1, 3));
		assertEquals("slope", 2.0, function.getSlope());
		assertEquals("c", 1.0, function.getC());
		assertEquals("y", 5.0, function.getY(2));
		assertEquals("x", 0.0, function.getX(1));
	}

	public void testGetThetaOnStandard45DegreeAngle() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 0), new Point2D.Double(1, 1));
		assertEquals("theta", FORTY_FIVE_DEGREES_IN_RADIANS, function.getTheta(), 0.0000001);
	}
	
	public void testGetThetaOnStandard30DegreeAngle() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 0), new Point2D.Double(Math.sqrt(3.0), 1.0));
		assertEquals("theta", THIRTY_DEGREES_IN_RADIANS, function.getTheta(), 0.0000001);
	}
}

package ca.intelliware.commons.dependency.graph;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

class ArrowShape {
	
	private Plot plot;
	private double arrowHeadSize = 5.0;

	public void drawArrow(Graphics2D graphics, Arrow arrow) {
		graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		if (arrow.isPointingUpward()) {
			graphics.setColor(this.plot.getReverseArrowColor());
		} else {
			graphics.setColor(this.plot.getArrowColor());
		}
		graphics.setStroke(this.plot.getArrowStroke());
		graphics.draw(arrow.toShape());
		
		drawArrowHead(graphics, arrow);
	}

	protected void drawArrowHead(Graphics2D graphics, Arrow arrow) {
		graphics = (Graphics2D) graphics.create();
		try {
			graphics.setStroke(new BasicStroke());
			graphics.translate(arrow.getLastPoint().getX(), arrow.getLastPoint().getY());
			
			graphics.rotate(arrow.getAngleOfLastSegment());
			
			GeneralPath path = new GeneralPath();
			path.moveTo((float) this.arrowHeadSize, (float) -this.arrowHeadSize);
			path.lineTo(0, 0);
			path.lineTo((float) this.arrowHeadSize, (float) this.arrowHeadSize);
			graphics.draw(path);
		} finally {
			graphics.dispose();
		}
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
}

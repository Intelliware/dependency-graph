package ca.intelliware.commons.dependency.graph;

import junit.framework.TestCase;

public class BlockTest extends TestCase {
	
	public void testWidth() throws Exception {
		
		Block block = new Block(new BlockManager(), 0);
		block.add(new MockVertex("v1", 0, 0, true));
		assertEquals("width 1", 0.1, block.getWidth());

		block.add(new MockVertex("v2", 0, 1, true));
		assertEquals("width 2", 0.1, block.getWidth());

		block.add(new MockVertex("v3", 0, 2, false));
		assertEquals("width 3", 1.0, block.getWidth());
	}
	
	public void testFirstPosition() throws Exception {
		BlockManager blockManager = new BlockManager();
		Block block = new Block(blockManager, 0);
		block.add(new MockVertex("v1", 0, 0, true));

		block.setRelativeHorizontalCoordinate(block.after(null));
		assertEquals("coordinate", 0.05, block.getRelativeHorizontalCoordinate());
	}
	public void testNextPosition() throws Exception {
		BlockManager blockManager = new BlockManager();
		Block first = new Block(blockManager, 0);
		first.add(new MockVertex("v1", 0, 0));
		first.setRelativeHorizontalCoordinate(first.after(null));
		
		Block block = new Block(blockManager, 0);
		block.add(new MockVertex("v2", 0, 1, true));
		block.setRelativeHorizontalCoordinate(block.after(first));
		
		assertEquals("coordinate", 1.05, block.getRelativeHorizontalCoordinate());
	}
}

package ca.intelliware.commons.dependency.graph;

import junit.framework.TestCase;

public class ClusterTest extends TestCase {
	
	public void testExcessShift() throws Exception {
		
		Block block = new Block(new BlockManager(), 1);
		block.add(new MockVertex("v1", 0, 0));
		Cluster cluster = new Cluster(block);
		cluster.setShift(3.0);
		
		assertEquals("excess shift", 2.5, cluster.getExcessShift());
	}
	
	public void testExcessShiftNegative() throws Exception {
		
		Block block = new Block(new BlockManager(), 1);
		block.add(new MockVertex("v1", 0, 0));
		Cluster cluster = new Cluster(block);
		cluster.setShift(-1.0);
		
		assertEquals("excess shift", -1.5, cluster.getExcessShift());
	}
}

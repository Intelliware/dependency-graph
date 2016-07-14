package ca.intelliware.commons.dependency.graph;

import junit.framework.TestCase;
import ca.intelliware.commons.dependency.DependencyManager;

public class SugiyamaAlgorithmTest extends TestCase {

	public void testExampleWithSimpleLayout() throws Exception {
		DependencyManager<String> manager = createDependencies();
		Graph graph = new SugiyamaAlgorithm().apply(manager);
		assertNotNull("graph", graph);
		assertEquals("number of layers", 3, graph.getLayers().size());
	}

	public void testLayers() throws Exception {
		DependencyManager<String> manager = createDependencies();
		Graph graph = new SugiyamaAlgorithm().apply(manager);

		for (GraphLayer layer : graph.getLayers()) {
			layer.orderVertices();
			for (Vertex vertex : layer.getVertices()) {
				System.out.println(layer.getLevelNumber() + " " + vertex.getOrdinal() + " " + vertex);
			}
		}
	}

	private DependencyManager<String> createDependencies() {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("pebbles", "fred");
		manager.add("fred", "mr_slate");
		manager.add("pebbles", "wilma");
		manager.add("bam-bam", "barney");
		manager.add("bam-bam", "betty");
		manager.add("dino", "fred");
		return manager;
	}
}

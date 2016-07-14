package ca.intelliware.commons.dependency.graph.shape;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ca.intelliware.commons.dependency.DependencyManager;
import ca.intelliware.commons.dependency.graph.Grapher;

public class PackageGraphWithSimpleCycles {
	
	public static void main(String[] args) throws Exception {
		new PackageGraphWithSimpleCycles().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub4", "ca.intelliware.example.sub5");
		manager.add("ca.intelliware.example.sub5", "ca.intelliware.example.sub4");

		OutputStream output = new FileOutputStream("c:/temp/PackageGraphWithSimpleCycles.png");
		try {
			Grapher grapher = new Grapher(manager);
			grapher.setShape(new PackageShape<String>());
			Dimension d = grapher.createPng(output);
			System.out.println(d);
		} finally {
			output.close();
		}
	}
}

package ca.intelliware.commons.dependency.graph.shape;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ca.intelliware.commons.dependency.DependencyManager;
import ca.intelliware.commons.dependency.graph.Grapher;

public class PackageGraphWithCycles {
	
	public static void main(String[] args) throws Exception {
		new PackageGraphWithCycles().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example.sub2");
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub3", "ca.intelliware.example.sub1");
		manager.add("ca.intelliware.example.sub5", "ca.intelliware.example.sub1");
		manager.add("ca.intelliware.example.sub2", "ca.intelliware.example.sub4");
		manager.add("ca.intelliware.example.sub4", "ca.intelliware.example.sub5");
		manager.add("ca.intelliware.example", "ca.intelliware.example.sub3");

		OutputStream output = new FileOutputStream("c:/temp/PackageGraphWithCycles.png");
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

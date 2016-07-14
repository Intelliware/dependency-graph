package ca.intelliware.commons.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import ca.intelliware.commons.dependency.DependencyManager;
import ca.intelliware.commons.dependency.graph.Grapher;

public class PackageGraphWithCrossings {
	
	public static void main(String[] args) throws Exception {
		new PackageGraphWithCrossings().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example.sub2");
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub3", "ca.intelliware.example.sub2");
		manager.add("ca.intelliware.example.sub3", "ca.intelliware.example");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "PackageGraphWithCrossings.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new BigPackageShape<String>());
			grapher.createPng(output);
		} finally {
			output.close();
		}
	}
}

package ca.intelliware.commons.dependency.graph.shape;

import javax.swing.ImageIcon;


public enum CommonImage implements ImageProvider {

	XML_DOCUMENT("xml.png"), CLASS_ICON("classIcon.png"), PACKAGE_ICON("package.png"),
	PACKAGE_BIG_ICON("packageBig.png");

	private final String resourceName;
	private ImageIcon image;

	private CommonImage(String resourceName) {
		this.resourceName = resourceName;
	}

	public ImageIcon getImage() {
		if (this.image == null) {
			this.image = new ImageIcon(getClass().getResource(this.resourceName));
		}
		return this.image;
	}
}

package com.example.microej;

import ej.mwt.Widget;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;

public interface Page {
	String getName();
	String getDescription();
	int getAccentColor();
	void populateStylesheet(CascadingStylesheet stylesheet);
	Widget getContentWidget();
}

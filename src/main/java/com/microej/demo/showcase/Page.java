package com.microej.demo.showcase;

import ej.mwt.Widget;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;

public interface Page {
	String getName();
	void populateStylesheet(CascadingStylesheet stylesheet);
	Widget getContentWidget();
}

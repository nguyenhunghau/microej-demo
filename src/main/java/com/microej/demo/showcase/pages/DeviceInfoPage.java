package com.microej.demo.showcase.pages;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.microui.display.Colors;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.widget.basic.Label;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class DeviceInfoPage implements Page {

	private static final int SECTION = 3000;
	private static final int KEY = 3001;
	private static final int VALUE = 3002;
	private static final int ERROR_LABEL = 3003;

	@Override
	public String getName() {
		return "Device & System Info";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(SECTION));
		style.setColor(AppStyle.CYAN);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(new ClassSelector(KEY));
		style.setColor(AppStyle.TEXT_GRAY);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(VALUE));
		style.setColor(AppStyle.YELLOW);
		style.setPadding(new UniformOutline(2));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));
		style.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));

		style = stylesheet.getSelectorStyle(new ClassSelector(ERROR_LABEL));
		style.setColor(AppStyle.RED);
		style.setPadding(new UniformOutline(2));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		// --- Device API (native SNI calls) ---
		addSection(main, "Device API -> native SNI -> BSP");

		String arch;
		try {
			arch = ej.util.Device.getArchitecture();
		} catch (Throwable t) {
			arch = "[Simulator: " + t.getClass().getName() + "]";
		}
		addRow(main, "Device.getArchitecture():", arch);

		String deviceId;
		try {
			byte[] id = ej.util.Device.getId();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < id.length && i < 8; i++) {
				String hex = Integer.toHexString(id[i] & 0xFF);
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
				if (i < id.length - 1) {
					sb.append(':');
				}
			}
			deviceId = sb.toString();
		} catch (Throwable t) {
			deviceId = "[Simulator: " + t.getClass().getName() + "]";
		}
		addRow(main, "Device.getId():", deviceId);

		// --- Runtime (JVM native calls) ---
		addSection(main, "Runtime API -> native JVM");

		Runtime rt = Runtime.getRuntime();
		addRow(main, "Runtime.freeMemory():", rt.freeMemory() + " bytes");
		addRow(main, "Runtime.totalMemory():", rt.totalMemory() + " bytes");

		long used = rt.totalMemory() - rt.freeMemory();
		addRow(main, "Used memory:", used + " bytes");

		// --- System (native calls) ---
		addSection(main, "System API -> native");

		addRow(main, "System.currentTimeMillis():", System.currentTimeMillis() + "ms");

		String encoding = System.getProperty("file.encoding");
		addRow(main, "System.getProperty(file.encoding):", encoding != null ? encoding : "null");

		String microejVersion = System.getProperty("com.microej.runtime.version");
		addRow(main, "getProperty(runtime.version):", microejVersion != null ? microejVersion : "null");

		return main;
	}

	private void addSection(List parent, String title) {
		Label label = new Label(title);
		label.addClassSelector(SECTION);
		parent.addChild(label);
	}

	private void addRow(List parent, String key, String value) {
		Label keyLabel = new Label(key);
		keyLabel.addClassSelector(KEY);
		Label valueLabel = new Label(value);
		valueLabel.addClassSelector(VALUE);

		List row = new List(LayoutOrientation.HORIZONTAL);
		row.addChild(keyLabel);
		row.addChild(valueLabel);
		parent.addChild(row);
	}
}

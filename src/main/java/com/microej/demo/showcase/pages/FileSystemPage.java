package com.microej.demo.showcase.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.microej.demo.showcase.AppStyle;
import com.microej.demo.showcase.Page;

import ej.microui.display.Colors;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.StateSelector;
import ej.mwt.stylesheet.selector.combinator.AndCombinator;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;

public class FileSystemPage implements Page {

	private static final int SECTION = 3300;
	private static final int INFO = 3301;
	private static final int ACTION_BTN = 3302;
	private static final int RESULT = 3303;

	private static final String TEST_FILE = "/tmp/microej_test.txt";
	private static final String TEST_DATA = "Hello from MicroEJ! Written via java.io -> native FS driver";

	private Label resultLabel;

	@Override
	public String getName() {
		return "File System";
	}

	@Override
	public void populateStylesheet(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(SECTION));
		style.setColor(AppStyle.CYAN);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(new ClassSelector(INFO));
		style.setColor(AppStyle.TEXT_GRAY);
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new ClassSelector(ACTION_BTN));
		style.setDimension(new FixedDimension(200, 22));
		style.setBackground(new RectangularBackground(AppStyle.BUTTON_BG));
		style.setColor(Colors.WHITE);
		style.setPadding(new UniformOutline(4));

		style = stylesheet.getSelectorStyle(
				new AndCombinator(new ClassSelector(ACTION_BTN), new StateSelector(StateSelector.ACTIVE)));
		style.setBackground(new RectangularBackground(AppStyle.PURPLE));

		style = stylesheet.getSelectorStyle(new ClassSelector(RESULT));
		style.setColor(AppStyle.YELLOW);
		style.setPadding(new UniformOutline(4));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));
		style.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));
	}

	@Override
	public Widget getContentWidget() {
		List main = new List(LayoutOrientation.VERTICAL);

		addSection(main, "File System API -> native FS driver -> flash/SD");
		addInfo(main, "API: java.io.File, FileOutputStream, FileInputStream");
		addInfo(main, "Path: " + TEST_FILE);

		// Write file
		Button writeBtn = new Button("FileOutputStream.write()");
		writeBtn.addClassSelector(ACTION_BTN);
		writeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					FileOutputStream fos = new FileOutputStream(TEST_FILE);
					fos.write(TEST_DATA.getBytes());
					fos.close();
					setResult("WRITE OK: " + TEST_DATA.length() + " bytes written");
				} catch (Throwable t) {
					setResult("WRITE: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(writeBtn);

		// Read file
		Button readBtn = new Button("FileInputStream.read()");
		readBtn.addClassSelector(ACTION_BTN);
		readBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					File f = new File(TEST_FILE);
					if (!f.exists()) {
						setResult("READ: File not found. Write first!");
						return;
					}
					FileInputStream fis = new FileInputStream(TEST_FILE);
					byte[] buf = new byte[(int) f.length()];
					int read = fis.read(buf);
					fis.close();
					setResult("READ OK (" + read + "b): " + new String(buf, 0, Math.min(read, 40)));
				} catch (Throwable t) {
					setResult("READ: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(readBtn);

		// File info
		Button infoBtn = new Button("File.exists() / length()");
		infoBtn.addClassSelector(ACTION_BTN);
		infoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					File f = new File(TEST_FILE);
					setResult("exists=" + f.exists() + " length=" + f.length());
				} catch (Throwable t) {
					setResult("INFO: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(infoBtn);

		// Delete file
		Button deleteBtn = new Button("File.delete()");
		deleteBtn.addClassSelector(ACTION_BTN);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					File f = new File(TEST_FILE);
					boolean deleted = f.delete();
					setResult("DELETE: " + (deleted ? "OK" : "FAILED (file not found?)"));
				} catch (Throwable t) {
					setResult("DELETE: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(deleteBtn);

		this.resultLabel = new Label("Tap buttons to call native FS functions");
		this.resultLabel.addClassSelector(RESULT);
		main.addChild(this.resultLabel);

		return main;
	}

	private void setResult(String text) {
		this.resultLabel.setText(text);
		this.resultLabel.requestRender();
	}

	private void addSection(List parent, String title) {
		Label l = new Label(title);
		l.addClassSelector(SECTION);
		parent.addChild(l);
	}

	private void addInfo(List parent, String text) {
		Label l = new Label(text);
		l.addClassSelector(INFO);
		parent.addChild(l);
	}
}

package com.microej.demo.showcase;

import com.microej.demo.showcase.pages.DeviceInfoPage;
import com.microej.demo.showcase.pages.DisplayHardwarePage;
import com.microej.demo.showcase.pages.FileSystemPage;
import com.microej.demo.showcase.pages.LedButtonPage;
import com.microej.demo.showcase.pages.McuTempPage;
import com.microej.demo.showcase.pages.NetworkPage;
import com.microej.demo.showcase.pages.TouchHardwarePage;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.mwt.Desktop;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.style.outline.border.RectangularBorder;
import ej.mwt.stylesheet.CachedStylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.StateSelector;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.stylesheet.selector.combinator.AndCombinator;
import ej.widget.basic.Button;
import ej.widget.basic.Label;
import ej.widget.basic.OnClickListener;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;
import ej.widget.container.SimpleDock;

public class DemoApp {

	private static final int TITLE = 2000;
	private static final int MENU_BUTTON = 2001;
	private static final int BACK_BUTTON = 2002;
	private static final int PAGE_TITLE = 2003;
	private static final int MENU_LIST = 2004;
	private static final int SUBTITLE = 2005;

	// 8 pages = 8 board features of STM32F7508-DK
	private static final Page[] PAGES = {
		new DeviceInfoPage(),        // 1. Device info + Runtime memory
		new DisplayHardwarePage(),   // 2. LCD 480x272
		new TouchHardwarePage(),     // 3. Touch screen (FT5336)
		new LedButtonPage(),         // 4. LED + User Button (GPIO)
		new McuTempPage(),           // 5. MCU internal temp sensor (ADC)
		new FileSystemPage(),        // 6. SD card (File I/O)
		new NetworkPage(),           // 7. Ethernet (TCP/DNS)
	};

	private DemoApp() {
	}

	public static void main(String[] args) {
		MicroUI.start();
		showMenu();
	}

	private static void showMenu() {
		CascadingStylesheet stylesheet = new CascadingStylesheet();
		addMenuStyles(stylesheet);

		Label title = new Label("STM32F7508-DK Board Demo");
		title.addClassSelector(TITLE);

		Label subtitle = new Label("7 hardware features | Java -> SNI -> BSP");
		subtitle.addClassSelector(SUBTITLE);

		List menuList = new List(LayoutOrientation.VERTICAL);
		menuList.addClassSelector(MENU_LIST);

		for (int i = 0; i < PAGES.length; i++) {
			final int pageIndex = i;
			String label = (i + 1) + ". " + PAGES[i].getName();
			Button btn = new Button(label);
			btn.addClassSelector(MENU_BUTTON);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick() {
					showPage(pageIndex);
				}
			});
			menuList.addChild(btn);
		}

		SimpleDock dock = new SimpleDock(LayoutOrientation.VERTICAL);
		dock.setFirstChild(title);
		dock.setCenterChild(menuList);
		dock.setLastChild(subtitle);

		Desktop desktop = new Desktop();
		desktop.setStylesheet(new CachedStylesheet(stylesheet));
		desktop.setWidget(dock);
		Display.getDisplay().requestShow(desktop);
	}

	private static void showPage(int pageIndex) {
		Page page = PAGES[pageIndex];

		CascadingStylesheet stylesheet = new CascadingStylesheet();
		addPageStyles(stylesheet);
		page.populateStylesheet(stylesheet);

		Label pageTitle = new Label(page.getName());
		pageTitle.addClassSelector(PAGE_TITLE);

		Button backBtn = new Button("< Menu");
		backBtn.addClassSelector(BACK_BUTTON);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				showMenu();
			}
		});

		List titleBar = new List(LayoutOrientation.HORIZONTAL);
		titleBar.addChild(backBtn);
		titleBar.addChild(pageTitle);

		Widget content = page.getContentWidget();

		SimpleDock dock = new SimpleDock(LayoutOrientation.VERTICAL);
		dock.setFirstChild(titleBar);
		dock.setCenterChild(content);

		Desktop desktop = new Desktop();
		desktop.setStylesheet(new CachedStylesheet(stylesheet));
		desktop.setWidget(dock);
		Display.getDisplay().requestShow(desktop);
	}

	private static void addMenuStyles(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(TITLE));
		style.setColor(AppStyle.TEXT_WHITE);
		style.setBackground(new RectangularBackground(AppStyle.BG_ACCENT));
		style.setPadding(new UniformOutline(8));

		style = stylesheet.getSelectorStyle(new ClassSelector(SUBTITLE));
		style.setColor(AppStyle.TEXT_GRAY);
		style.setPadding(new UniformOutline(4));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));

		style = stylesheet.getSelectorStyle(new ClassSelector(MENU_BUTTON));
		style.setColor(Colors.WHITE);
		style.setBorder(new RectangularBorder(AppStyle.DIVIDER, 1));
		style.setPadding(new UniformOutline(7));
		style.setBackground(new RectangularBackground(AppStyle.BG_CARD));

		style = stylesheet.getSelectorStyle(
				new AndCombinator(new ClassSelector(MENU_BUTTON), new StateSelector(StateSelector.ACTIVE)));
		style.setBackground(new RectangularBackground(AppStyle.PURPLE));

		style = stylesheet.getSelectorStyle(new ClassSelector(MENU_LIST));
		style.setPadding(new UniformOutline(2));

		style = stylesheet.getSelectorStyle(new TypeSelector(Label.class));
		style.setColor(AppStyle.TEXT_WHITE);

		style = stylesheet.getSelectorStyle(new TypeSelector(SimpleDock.class));
		style.setBackground(new RectangularBackground(AppStyle.BG_DARK));
	}

	private static void addPageStyles(CascadingStylesheet stylesheet) {
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(PAGE_TITLE));
		style.setColor(AppStyle.TEXT_WHITE);
		style.setPadding(new UniformOutline(5));

		style = stylesheet.getSelectorStyle(new ClassSelector(BACK_BUTTON));
		style.setDimension(new FixedDimension(70, 22));
		style.setBackground(new RectangularBackground(AppStyle.BG_ACCENT));
		style.setColor(Colors.WHITE);
		style.setPadding(new UniformOutline(3));

		style = stylesheet.getSelectorStyle(
				new AndCombinator(new ClassSelector(BACK_BUTTON), new StateSelector(StateSelector.ACTIVE)));
		style.setBackground(new RectangularBackground(AppStyle.PURPLE));

		style = stylesheet.getSelectorStyle(new TypeSelector(Label.class));
		style.setColor(AppStyle.TEXT_WHITE);

		style = stylesheet.getSelectorStyle(new TypeSelector(SimpleDock.class));
		style.setBackground(new RectangularBackground(AppStyle.BG_DARK));
	}
}

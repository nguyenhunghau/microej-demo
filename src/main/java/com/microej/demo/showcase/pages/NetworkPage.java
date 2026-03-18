package com.microej.demo.showcase.pages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

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

public class NetworkPage implements Page {

	private static final int SECTION = 3400;
	private static final int INFO = 3401;
	private static final int ACTION_BTN = 3402;
	private static final int RESULT = 3403;

	private Label resultLabel;

	@Override
	public String getName() {
		return "Network";
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
		style.setDimension(new FixedDimension(220, 22));
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

		addSection(main, "Network API -> native NET driver -> Ethernet/WiFi");
		addInfo(main, "API: java.net.InetAddress, Socket");
		addInfo(main, "Each call -> SNI -> lwIP / BSD sockets in BSP");

		// DNS resolve
		Button dnsBtn = new Button("InetAddress.getByName(\"google.com\")");
		dnsBtn.addClassSelector(ACTION_BTN);
		dnsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					InetAddress addr = InetAddress.getByName("google.com");
					setResult("DNS OK: google.com -> " + addr.getHostAddress());
				} catch (Throwable t) {
					setResult("DNS: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(dnsBtn);

		// Localhost resolve
		Button localBtn = new Button("InetAddress.getLocalHost()");
		localBtn.addClassSelector(ACTION_BTN);
		localBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					InetAddress addr = InetAddress.getLocalHost();
					setResult("LocalHost: " + addr.getHostName() + " / " + addr.getHostAddress());
				} catch (Throwable t) {
					setResult("LocalHost: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(localBtn);

		// TCP connect
		Button tcpBtn = new Button("Socket.connect(google.com:80)");
		tcpBtn.addClassSelector(ACTION_BTN);
		tcpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					long start = System.currentTimeMillis();
					Socket socket = new Socket("google.com", 80);
					long elapsed = System.currentTimeMillis() - start;
					String localAddr = socket.getLocalAddress().getHostAddress();
					socket.close();
					setResult("TCP OK: connected in " + elapsed + "ms from " + localAddr);
				} catch (Throwable t) {
					setResult("TCP: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(tcpBtn);

		// Loopback
		Button loopBtn = new Button("InetAddress.getByName(\"127.0.0.1\")");
		loopBtn.addClassSelector(ACTION_BTN);
		loopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				try {
					InetAddress addr = InetAddress.getByName("127.0.0.1");
					setResult("Loopback: " + addr.getHostAddress() + " isLoopback=" + addr.isLoopbackAddress());
				} catch (Throwable t) {
					setResult("Loopback: " + t.getClass().getName() + ": " + t.getMessage());
				}
			}
		});
		main.addChild(loopBtn);

		this.resultLabel = new Label("Tap buttons to call native network functions");
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

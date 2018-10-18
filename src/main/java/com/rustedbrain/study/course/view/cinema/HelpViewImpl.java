package com.rustedbrain.study.course.view.cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = VaadinUI.HELP_VIEW)
public class HelpViewImpl extends VerticalLayout implements HelpView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8207127625810289913L;
	private Panel helpPanel;
	private Panel menuPanel;
	private List<HelpViewListener> helpViewListeners = new ArrayList<>();

	public HelpViewImpl() {
		addComponentsAndExpand(getMenuPanel());
		addComponentsAndExpand(getHelpPanel());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		helpViewListeners.forEach(listener -> listener.entered(event));
	}

	private Panel getMenuPanel() {
		if (menuPanel == null) {
			menuPanel = new Panel();
		}
		return menuPanel;
	}

	private Panel getHelpPanel() {
		if (helpPanel == null) {
			helpPanel = new Panel();
		}
		return helpPanel;
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	@Override
	public void setHelpTittleTextMap(Map<String, String> helpTittleTextMap) {
		Accordion helpAccordion = new Accordion();
		for (Map.Entry<String, String> entry : helpTittleTextMap.entrySet()) {
			Label label = new Label(entry.getValue(), ContentMode.HTML);
			label.setWidth(100.0f, Unit.PERCENTAGE);

			final VerticalLayout layout = new VerticalLayout(label);
			layout.setMargin(true);

			helpAccordion.addTab(layout, entry.getKey());
		}
		getHelpPanel().setContent(helpAccordion);
	}

	@Override
	@Autowired
	public void addHelpViewListener(HelpViewListener helpViewListener) {
		helpViewListener.setView(this);
		this.helpViewListeners.add(helpViewListener);
	}

	@Override
	public void fillMenuPanel(AuthenticationService authenticationService) {
		getMenuPanel().setContent(new MenuComponent(authenticationService));
	}
}
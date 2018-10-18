package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public interface HelpView extends ApplicationView {

	void setHelpTittleTextMap(Map<String, String> helpTittleTextMap);

	@Autowired
	void addHelpViewListener(HelpViewListener helpViewListener);

	void fillMenuPanel(AuthenticationService authenticationService);

	interface HelpViewListener {

		void entered(ViewChangeListener.ViewChangeEvent event);

		void setView(HelpView helpView);
	}
}
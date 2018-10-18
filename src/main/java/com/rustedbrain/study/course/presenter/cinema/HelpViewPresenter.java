package com.rustedbrain.study.course.presenter.cinema;

import com.rustedbrain.study.course.model.exception.ResourceException;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.HelpView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@UIScope
@SpringComponent
public class HelpViewPresenter implements HelpView.HelpViewListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8239732496677642717L;

	private static final Logger logger = Logger.getLogger(HelpViewPresenter.class.getName());

	private final CinemaService cinemaService;
	private final AuthenticationService authenticationService;
	private HelpView helpView;

	@Autowired
	public HelpViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
		this.authenticationService = authenticationService;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		this.helpView.fillMenuPanel(authenticationService);
		try {
			Map<String, String> helpTittleTextMap = cinemaService.getHelpTittleTextMap();
			helpView.setHelpTittleTextMap(helpTittleTextMap);
		} catch (ParserConfigurationException | IOException | SAXException | ResourceException e) {
			logger.log(Level.WARNING, "Error occurred during retrieving help for help page", e);
			helpView.showError(e.getMessage());
		}
	}

	public void setView(HelpView helpView) {
		this.helpView = helpView;
	}
}

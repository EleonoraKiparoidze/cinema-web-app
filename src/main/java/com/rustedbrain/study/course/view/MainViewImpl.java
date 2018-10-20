package com.rustedbrain.study.course.view;

import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.components.CityComboBox;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@UIScope
@SpringView(name = VaadinUI.MAIN_VIEW)
public class MainViewImpl extends VerticalLayout implements MainView {

	private static final long serialVersionUID = -2044419449921952251L;
	private Collection<MainViewListener> mainViewListeners = new ArrayList<>();
	private Panel menuPanel;
	private Panel mainViewPanel;
	private CharacterCitiesPanel panel;

	public MainViewImpl() {
		addComponentsAndExpand(getMenuViewPanel());
		addComponentsAndExpand(getMainViewPanel());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		mainViewListeners.forEach(MainViewListener::entered);
	}

	private Panel getMenuViewPanel() {
		if ( menuPanel == null ) {
			menuPanel = new Panel();
		}
		return menuPanel;
	}

	private Panel getMainViewPanel() {
		if ( mainViewPanel == null ) {
			mainViewPanel = new Panel();
		}
		return mainViewPanel;
	}

	@Override
	public void fillFilmScreeningsPanel(List<FilmScreening> screenings) {

	}

	@Override
	@Autowired
	public void addMainViewListener(MainViewListener mainViewListener) {
		mainViewListener.setView(this);
		this.mainViewListeners.add(mainViewListener);
	}

	@Override
	public void fillMenuPanel(AuthenticationService authenticationService) {
		getMenuViewPanel().setContent(new MenuComponent(authenticationService));
	}

	@Override
	public void showCitySelectionPanel(List<City> cities) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		final Label label = new Label("City was not identified, please select your city", ContentMode.TEXT);
		label.getWidth();
		layout.addComponent(label);
		CityComboBox cityComboBox = new CityComboBox(cities);
		cityComboBox.addSelectionListener((SingleSelectionListener<City>) singleSelectionEvent -> {
			Optional<City> cityOptional = singleSelectionEvent.getSelectedItem();
			cityOptional.ifPresent(city -> {
				mainViewListeners.forEach(x -> x.comboBoxCitySelectionValueSelected(city));
			});
		});
		layout.addComponent(cityComboBox);
		this.panel = new CharacterCitiesPanel(cities);
		layout.addComponent(panel);
		mainViewPanel.setContent(layout);
	}

	@Override
	public void setSelectedCharacterCities(Character character) {
		panel.setSelectedCharacterCities(character);
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

	private class CharacterCitiesPanel extends VerticalLayout {

		private static final long serialVersionUID = -3987481690619424904L;
		private final Map<Character, List<City>> charCityMap;
		private final Panel citiesAlphabetPanel = new Panel();
		private final Panel citiesLinksPanel = new Panel();
		private final List<Button> characterButtons;

		public CharacterCitiesPanel(List<City> cities) {
			this.charCityMap = getCitiesMap(cities);
			this.addComponents(citiesAlphabetPanel, citiesLinksPanel);
			this.characterButtons = createAlphabetButtons();
			this.showAlphabetButtons();
		}

		private void setSelectedCharacter(Character character) {
			Optional<Button> buttonOptional =
					characterButtons.stream().filter(button -> button.getCaption().charAt(0) == character).findAny();
			if ( buttonOptional.isPresent() ) {
				characterButtons.forEach(button -> button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED));
				buttonOptional.get().setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
		}

		private void showAlphabetButtons() {
			HorizontalLayout layout = new HorizontalLayout();
			for (Button button : characterButtons) {
				layout.addComponent(button);
			}
			citiesAlphabetPanel.setContent(layout);
		}

		private Map<Character, List<City>> getCitiesMap(List<City> cities) {
			Map<Character, List<City>> charCityMap = new TreeMap<>();
			for (City city : cities) {
				Character character = city.getName().charAt(0);
				if ( charCityMap.containsKey(character) ) {
					charCityMap.get(character).add(city);
				} else {
					charCityMap.put(character, new ArrayList<>(Collections.singletonList(city)));
				}
			}
			return charCityMap;
		}

		private void showSelectedCharacterCitiesPanel(Character character) {
			List<City> cities = charCityMap.get(character);
			VerticalLayout verticalLayout = new VerticalLayout();
			for (City city : cities) {
				Button button = new Button(city.getName(), (Button.ClickListener) event -> mainViewListeners
						.forEach(listener -> listener.cityButtonClicked(city.getId())));
				button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				verticalLayout.addComponent(button);
			}
			citiesLinksPanel.setContent(verticalLayout);
		}

		private List<Button> createAlphabetButtons() {
			List<Button> characterButtons = new LinkedList<>();
			for (Character character : charCityMap.keySet()) {
				Button button = new Button(character.toString(), (Button.ClickListener) event -> mainViewListeners
						.forEach(listener -> listener.alphabetButtonClicked(character)));
				button.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				characterButtons.add(button);
			}
			return characterButtons;
		}

		void setSelectedCharacterCities(Character character) {
			this.setSelectedCharacter(character);
			this.showSelectedCharacterCitiesPanel(character);
		}
	}
}
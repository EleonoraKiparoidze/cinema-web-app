package com.rustedbrain.study.course.view;

import com.rustedbrain.study.course.model.persistence.cinema.Cinema;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.FilmScreening;
import com.rustedbrain.study.course.service.AuthenticationService;

import java.util.List;

public interface MainView extends ApplicationView {

    void fillFilmScreeningsPanel(List<FilmScreening> screenings);

    void addMainViewListener(MainViewListener mainViewListener);

    void fillMenuPanel(AuthenticationService authenticationService);

    void showCitySelectionPanel(List<City> cities);

	void setSelectedCharacterCities(Character character);

    interface MainViewListener {

        void buttonDeleteCityClicked(String cityName);

        void buttonCreateCityClicked(String cityName);

        void buttonShowCinemaClicked(Cinema cinema);

        void entered();

        void comboBoxCitySelectionValueSelected(City city);

        void setView(MainView mainView);

        void cityButtonClicked(long city);

        void alphabetButtonClicked(Character character);
    }
}

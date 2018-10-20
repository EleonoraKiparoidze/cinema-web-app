package com.rustedbrain.study.course.presenter.cinema;

import static com.rustedbrain.study.course.presenter.cinema.TicketBuyingViewPresenter.PARAM_SEPARATOR;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.service.CinemaService;
import com.rustedbrain.study.course.view.cinema.TicketsInfoView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class TicketsInfoViewPresenter implements TicketsInfoView.TicketsInfoViewListener, Serializable {

	private static final long serialVersionUID = 1825173376730261455L;

	public static final String TICKETS_ID_PARAM_KEY = "tickets";

	private final CinemaService cinemaService;
	private TicketsInfoView view;
	private List<TicketInfo> ticketInfos;

	@Autowired
	public TicketsInfoViewPresenter(CinemaService cinemaService, AuthenticationService authenticationService) {
		this.cinemaService = cinemaService;
	}

	@Override
	public void setView(TicketsInfoView view) {
		this.view = view;
	}

	@Override
	public void entered(ViewChangeListener.ViewChangeEvent event) {
		Map<String, String> parameterMap = event.getParameterMap();
		List<Long> seatIds = Arrays.stream(parameterMap.get(TICKETS_ID_PARAM_KEY).split(PARAM_SEPARATOR))
				.map(String::trim).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
		this.ticketInfos = cinemaService.getTicketsInfo(seatIds);
		this.view.showTicketsInfo(ticketInfos);

	}
}

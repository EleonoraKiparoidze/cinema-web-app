package com.rustedbrain.study.course.view.cinema;

import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TicketsInfoView extends ApplicationView {

	@Autowired
	void addListener(TicketsInfoViewListener listener);

	void showTicketsInfo(List<TicketInfo> ticketInfos);

	public interface TicketsInfoViewListener {

		void setView(TicketsInfoView view);

		void entered(ViewChangeListener.ViewChangeEvent event);
	}
}

package com.rustedbrain.study.course.view.cinema;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.view.ApplicationView;
import com.vaadin.navigator.ViewChangeListener;

public interface TicketsPayView extends ApplicationView {

	@Autowired
	void addListener(TicketsPayViewListener listener);

	void showTicketsInfo(List<TicketInfo> ticketInfos);

	public interface TicketsPayViewListener {

		void setView(TicketsPayView view);

		void entered(ViewChangeListener.ViewChangeEvent event);
	}

	void showPayForm(List<TicketInfo> ticketInfos);
}
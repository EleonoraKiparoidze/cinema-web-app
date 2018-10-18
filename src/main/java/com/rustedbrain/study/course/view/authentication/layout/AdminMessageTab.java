package com.rustedbrain.study.course.view.authentication.layout;

import java.util.ArrayList;
import java.util.List;

import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.rustedbrain.study.course.view.authentication.ProfileView.ViewListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdminMessageTab extends Panel {

	private static final long serialVersionUID = -7671104382938814079L;
	private List<ChangeRequest> changeRequests;
	private VerticalLayout layout;
	private List<User> users = new ArrayList<>();
	List<ProfileView.ViewListener> listeners;

	public AdminMessageTab(List<ViewListener> listeners, List<ChangeRequest> changeRequests, List<User> users) {
		this.listeners = listeners;
		this.changeRequests = changeRequests;
		this.users = users;
		setContent(new Panel(createAcceptedMessageLayout()));
	}

	private VerticalLayout createAcceptedMessageLayout() {
		layout = new VerticalLayout();
		for (ChangeRequest changeRequest : changeRequests) {
			if ( !changeRequest.isAccepted() ) {
				User user = users.stream().filter(userPredicate -> userPredicate.getId() == changeRequest.getUserId())
						.findAny().get();
				Label changeRequestLable = new Label("User " + user.getLogin() + " want to change "
						+ changeRequest.getFieldName() + " to " + changeRequest.getValue());
				VerticalLayout verticalLayout =
						new VerticalLayout(changeRequestLable, createAcceptPanel(changeRequest.getUserId(),
								changeRequest.getFieldName(), changeRequest.getValue(), changeRequest.getId()));
				layout.addComponent(new Panel(verticalLayout));
			}
		}
		return layout;
	}

	private Layout createAcceptPanel(long userId, String fieldName, String value, long changeRequestId) {
		Button acceptButton =
				new Button("Accept", (Button.ClickListener) event -> listeners.forEach(viewListener -> viewListener
						.buttonAcceptChangeRequestClicked(userId, fieldName, value, changeRequestId)));
		acceptButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button declineButton =
				new Button("Decline", (Button.ClickListener) event -> listeners.forEach(viewListener -> viewListener
						.buttonDeclineChangeRequestClicked(userId, fieldName, value, changeRequestId)));
		declineButton.addStyleName(ValoTheme.BUTTON_DANGER);
		return new HorizontalLayout(acceptButton, declineButton);
	}

}

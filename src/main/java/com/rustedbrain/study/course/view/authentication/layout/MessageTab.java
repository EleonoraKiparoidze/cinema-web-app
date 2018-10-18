package com.rustedbrain.study.course.view.authentication.layout;

import java.util.List;

import com.rustedbrain.study.course.model.persistence.authorization.ChangeRequest;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MessageTab extends Panel {

	private static final long serialVersionUID = -5716930841797306128L;
	private List<ChangeRequest> changeRequests;
	private VerticalLayout layout;
	private User currUser;

	public MessageTab(User currUser, List<ChangeRequest> changeRequests) {
		this.currUser = currUser;
		this.changeRequests = changeRequests;
		setContent(new Panel(createMessageLayout()));
	}

	private VerticalLayout createMessageLayout() {
		this.layout = new VerticalLayout();
		VerticalLayout messageLayout = new VerticalLayout();
		for (ChangeRequest changeRequest : changeRequests) {
			if ( changeRequest.getUserId() == currUser.getId() ) {
				Label changeRequestLable = new Label(
						"Request to change " + changeRequest.getFieldName() + " to " + changeRequest.getValue() + " is "
								+ (changeRequest.isAccepted() ? "accepted;" : " not accepted;"));
				messageLayout.addComponent(changeRequestLable);
			}
		}
		layout.addComponent(new Panel(messageLayout));
		return layout;
	}

}

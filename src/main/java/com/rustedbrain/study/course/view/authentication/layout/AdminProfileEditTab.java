package com.rustedbrain.study.course.view.authentication.layout;

import com.rustedbrain.study.course.model.persistence.authorization.User;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.view.authentication.ProfileView;
import com.vaadin.data.HasValue;
import com.vaadin.ui.*;

import java.util.List;

public class AdminProfileEditTab extends ProfileEditTab {

	private static final long serialVersionUID = -3842108913206602034L;
	private List<User> users;

	public AdminProfileEditTab(List<ProfileView.ViewListener> listeners, User currUser, List<User> users,
			List<City> cities) {
		super(listeners, currUser, cities);
		this.users = users;
		this.layout.addComponent(new Panel(createUserSelectionLayout()), 0);
		this.layout.addComponent(new Panel(createBlockUnblockControlPanel()));
	}

	private Layout createUserSelectionLayout() {
		ComboBox<User> userComboBox = new ComboBox<>("User", users);
		userComboBox.setItemCaptionGenerator(User::getLogin);
		userComboBox.setEmptySelectionAllowed(false);
		userComboBox.setSelectedItem(currUser);
		userComboBox.addValueChangeListener((HasValue.ValueChangeListener<User>) event -> {
			User selectedUser = event.getValue();
			listeners.forEach(viewListener -> viewListener.comboBoxEditUserSelected(selectedUser));
		});
		Button showMeButton = new Button("Me", (Button.ClickListener) event -> listeners
				.forEach(viewListener -> viewListener.buttonEditShowMeClicked(users)));
		showMeButton.setSizeFull();

		HorizontalLayout layout = new HorizontalLayout(showMeButton, userComboBox);
		layout.setMargin(true);
		return layout;
	}

	private Layout createBlockUnblockControlPanel() {
		Button blockButton = new Button("Block", (Button.ClickListener) event -> listeners
				.forEach(viewListener -> viewListener.buttonBlockClicked(currUser.getId())));
		Button unblockButton = new Button("Unblock", (Button.ClickListener) event -> listeners
				.forEach(viewListener -> viewListener.buttonUnblockClicked(currUser.getId())));
		return new HorizontalLayout(blockButton, unblockButton);
	}

	@Override
	public void setSelectedUser(User selectedUser) {
		this.currUser = selectedUser;
		this.layout = super.createUserInfoEditLayout();
		this.layout.addComponent(new Panel(createUserSelectionLayout()), 0);
		this.layout.addComponent(new Panel(createBlockUnblockControlPanel()));
		setContent(new Panel(this.layout));
	}
}
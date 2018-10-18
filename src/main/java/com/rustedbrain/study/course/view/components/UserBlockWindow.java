package com.rustedbrain.study.course.view.components;

import com.vaadin.ui.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserBlockWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5707645639377588313L;

	private static final String WINDOW_TITTLE = "Block User";

	private List<Listener> listeners = new ArrayList<>();

	public UserBlockWindow(String userLogin, long userId) {
		super(WINDOW_TITTLE);
		super.setSizeUndefined();
		super.setModal(true);
		super.setResizable(false);
		super.setDraggable(false);

		Label warnLabel = new Label(
				"If you want to block user \"" + userLogin + "\", please specify date and description");
		DateTimeField blockDateTimeField = new DateTimeField("Block date", LocalDateTime.now());
		TextArea blockDescrTextField = new TextArea("Reason of blocking");
		blockDescrTextField.setSizeFull();
		Button buttonBlock = new Button("Block",
				(Button.ClickListener) event -> listeners
						.forEach(viewListener -> viewListener.buttonBlockSubmitClicked(userId,
								blockDateTimeField.getValue(), blockDescrTextField.getValue())));
		super.setContent(new VerticalLayout(warnLabel, blockDateTimeField, blockDescrTextField, buttonBlock));
	}

	public boolean addListener(Listener listener) {
		return listeners.add(listener);
	}

	public interface Listener {

		void buttonBlockSubmitClicked(long userId, LocalDateTime blockUntil, String blockDescription);
	}
}

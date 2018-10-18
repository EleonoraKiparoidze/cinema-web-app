package com.rustedbrain.study.course.view;

import com.vaadin.navigator.View;

public interface ApplicationView extends View {

	void showWarning(String message);

	void showError(String message);

	void reload();
}

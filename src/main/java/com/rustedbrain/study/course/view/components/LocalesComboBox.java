package com.rustedbrain.study.course.view.components;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocalesComboBox extends ComboBox<Locale> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2226110728366702583L;

	public LocalesComboBox(String caption) {
		super(caption);
		setItemCaptionGenerator(Locale::getDisplayName);
		List<Locale> locales = Arrays.asList(new Locale("ru", "RU"), Locale.US, Locale.ENGLISH);
		setItems(locales);
		setEmptySelectionAllowed(false);
		setVisible(true);
	}

	@Override
	public void attach() {
		super.attach();
		setValue(VaadinSession.getCurrent().getLocale());
	}
}

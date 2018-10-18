package com.rustedbrain.study.course.view.cinema;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.rustedbrain.study.course.model.dto.UserRole;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.CommentReputation;
import com.rustedbrain.study.course.model.persistence.cinema.Movie;
import com.rustedbrain.study.course.service.AuthenticationService;
import com.rustedbrain.study.course.view.VaadinUI;
import com.rustedbrain.study.course.view.components.MenuComponent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScope
@SpringView(name = VaadinUI.MOVIE_VIEW)
public class MovieViewImpl extends VerticalLayout implements MovieView {

	private static final long serialVersionUID = 5307694571348535300L;
	public static final String MOVIE_ATTRIBUTE = "movie";
	private List<MovieView.Listener> listeners = new ArrayList<>();
	private Panel moviePanel;
	private Panel commentsPanel;
	private Window userBlockWindow;

	@Autowired
	public MovieViewImpl(AuthenticationService authenticationService) {
		addComponentsAndExpand(new Panel(new MenuComponent(authenticationService)));
		addComponentsAndExpand(new Panel(getMoviePanel()));
		addComponentsAndExpand(new Panel(getCommentsPanel()));
	}

	private Panel getMoviePanel() {
		if ( moviePanel == null ) {
			moviePanel = new Panel();
		}
		return moviePanel;
	}

	private Panel getCommentsPanel() {
		if ( commentsPanel == null ) {
			commentsPanel = new Panel();
		}
		return commentsPanel;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		listeners.forEach(listener -> listener.entered(event));
	}

	@Override
	public void showMovieInfo(Movie movie, boolean authorized, String login, boolean admin, boolean moderator) {
		moviePanel.setContent(getMovieInfoLayout(movie));
		commentsPanel.setContent(getMovieCommentsLayout(movie.getComments(), authorized, login, admin, moderator));
	}

	private Component getMovieCommentsLayout(Set<Comment> commentsSet, boolean authorized, String login, boolean admin,
			boolean moderator) {
		VerticalLayout layout = new VerticalLayout();
		List<Panel> commentsPanels =
				commentsSet.stream().sorted(Comparator.comparing(Comment::getRegistrationDate))
						.map(comment -> createCommentPanel(comment,
								(authorized && comment.getUser().getLogin().equals(login)), admin, moderator))
						.collect(Collectors.toList());

		if ( commentsPanels.isEmpty() ) {
			layout.addComponent(new Label("No comments yet created..."));
		} else {
			commentsPanels.forEach(layout::addComponent);
		}

		if ( authorized ) {
			TextArea textArea = new TextArea();
			textArea.setSizeFull();
			Button buttonCreateMessage = new Button("Create message", (Button.ClickListener) event -> listeners
					.forEach(listener -> listener.buttonCreateMessageClicked(textArea.getValue())));
			layout.addComponents(textArea, buttonCreateMessage);
		}

		return layout;
	}

	private Panel createCommentPanel(Comment comment, boolean creator, boolean admin, boolean moderator) {
		HorizontalLayout commentLayout = new HorizontalLayout();
		commentLayout.setMargin(true);
		Button buttonProfile = new Button(comment.getUser().getLogin(), (Button.ClickListener) event -> listeners
				.forEach(listener -> listener.buttonProfileClicked(comment.getUser().getId())));
		buttonProfile.setWidth(200, Unit.PIXELS);
		commentLayout.addComponent(buttonProfile);

		VerticalLayout messageLayout = new VerticalLayout();
		messageLayout.setMargin(false);
		messageLayout.addComponent(new Panel(new Label(comment.getRegistrationDate().toString())));
		messageLayout.addComponent(new Label(comment.getMessage()));

		HorizontalLayout layout = new HorizontalLayout();

		HorizontalLayout reputationLayout = new HorizontalLayout();
		reputationLayout.addComponent(new Button("+",
				(Button.ClickListener) event -> listeners.forEach(listener -> listener.buttonPlusClicked(comment))));
		long plusesCount = comment.getCommentReputations().stream().filter(CommentReputation::isPlus).count();
		long minusesCount = comment.getCommentReputations().size() - plusesCount;
		String reputationString = plusesCount >= minusesCount ? ("+" + (plusesCount - minusesCount))
				: ("-" + (minusesCount - plusesCount));
		reputationLayout.addComponent(new Label(reputationString));
		reputationLayout.addComponent(new Button("-",
				(Button.ClickListener) event -> listeners.forEach(listener -> listener.buttonMinusClicked(comment))));
		layout.addComponent(reputationLayout);

		if ( creator || admin || moderator ) {
			layout.addComponentsAndExpand(new Button("Delete", (Button.ClickListener) event -> listeners
					.forEach(listener -> listener.buttonDeleteCommentClicked(comment.getId()))));

			Button editButton = new Button("Edit");
			editButton.addClickListener(
					event -> messageLayout.addComponent(getEditPopup(comment.getId(), comment.getMessage())));
			layout.addComponentsAndExpand(editButton);
		}
		if ( admin || moderator ) {
			layout.addComponentsAndExpand(new Button("Block", (Button.ClickListener) event -> listeners
					.forEach(listener -> listener.buttonBlockClicked(comment.getId(), comment.getUser().getId()))));
			layout.addComponentsAndExpand(new Button("Block And Delete",
					(Button.ClickListener) event -> listeners.forEach(listener -> listener
							.buttonBlockAndDeleteClicked(comment.getId(), comment.getUser().getId()))));
		}
		layout.setSizeFull();

		messageLayout.addComponent(new Panel(layout));
		commentLayout.addComponentsAndExpand(new Panel(messageLayout));
		return new Panel(commentLayout);
	}

	private PopupView getEditPopup(long commentId, String message) {
		TextField commentTextField = new TextField("New comment", message);
		VerticalLayout changePopupContent = new VerticalLayout();
		changePopupContent.addComponent(commentTextField);
		changePopupContent
				.addComponent(new Button("Change", (Button.ClickListener) event -> listeners.forEach(listener -> {
					listener.buttonEditCommentClicked(commentId, commentTextField.getValue());
					reload();
				})));
		PopupView changePopup = new PopupView(null, changePopupContent);
		changePopup.setSizeUndefined();
		changePopup.setPopupVisible(true);
		return changePopup;
	}

	private Layout getMovieInfoLayout(Movie movie) {
		GridLayout movieInfoPosterLayout = new GridLayout();
		movieInfoPosterLayout.setMargin(false);
		movieInfoPosterLayout.setColumns(2);
		movieInfoPosterLayout.setSizeFull();

		VerticalLayout posterLayout = new VerticalLayout();
		posterLayout.setWidth(375, Unit.PIXELS);
		Image image = new Image("", new FileResource(new File(movie.getPosterPath())));
		image.setWidth(365, Unit.PIXELS);
		image.setHeight(490, Unit.PIXELS);
		posterLayout.addComponent(image);
		movieInfoPosterLayout.addComponent(posterLayout, 0, 0);

		VerticalLayout infoLayout = new VerticalLayout();
		Label nameLabel = new Label("Name: " + movie.getLocalizedName());
		nameLabel.setSizeFull();
		infoLayout.addComponent(nameLabel);
		Label originalNameLabel = new Label("Original name: " + movie.getOriginalName());
		originalNameLabel.setSizeFull();
		infoLayout.addComponent(originalNameLabel);
		Label countryLabel = new Label("Country: " + movie.getCountry());
		countryLabel.setSizeFull();
		infoLayout.addComponent(countryLabel);
		Label releaseDateLabel =
				new Label("Year: " + movie.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).getYear());
		releaseDateLabel.setSizeFull();
		infoLayout.addComponent(releaseDateLabel);
		Label descriptionLabel = new Label(movie.getDescription());
		descriptionLabel.setSizeFull();
		infoLayout.addComponent(descriptionLabel);
		movieInfoPosterLayout.addComponent(infoLayout, 1, 0);

		movieInfoPosterLayout.setColumnExpandRatio(1, 2);
		return movieInfoPosterLayout;
	}

	@Override
	@Autowired
	public void addCinemaViewListener(MovieView.Listener listener) {
		listener.setView(this);
		this.listeners.add(listener);
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	@Override
	public void closeUserBlockWindow() {
		if ( this.userBlockWindow != null ) {
			this.userBlockWindow.close();
		}
	}

	@Override
	public void showUserBlockWindow(long userId, String login, UserRole userRole, long commentId) {
		closeUserBlockWindow();
		UI.getCurrent().addWindow(getUserBlockWindow(userId, login, commentId, false));
	}

	@Override
	public void showUserBlockDeleteWindow(long userId, String login, UserRole userRole, long commentId) {
		closeUserBlockWindow();
		UI.getCurrent().addWindow(getUserBlockWindow(userId, login, commentId, true));
	}

	private Window getUserBlockWindow(long userId, String login, long commentId, boolean delete) {
		userBlockWindow = new Window("Block User");
		userBlockWindow.addCloseListener((Window.CloseListener) e -> userBlockWindow = null);
		userBlockWindow.setSizeUndefined();
		userBlockWindow.setModal(true);
		userBlockWindow.setResizable(false);
		userBlockWindow.setDraggable(false);

		Label warnLabel = new Label("If you want to block user \"" + login + "\", please specify date and description");
		DateTimeField blockDateTimeField = new DateTimeField("Block date", LocalDateTime.now());
		TextArea blockDescrTextField = new TextArea("Reason of blocking");
		blockDescrTextField.setSizeFull();
		Button buttonBlock = new Button("Block");
		if ( delete ) {
			buttonBlock.addClickListener(event -> listeners.forEach(viewListener -> {
				viewListener.buttonBlockSubmitClicked(userId, blockDateTimeField.getValue(),
						blockDescrTextField.getValue());
				viewListener.buttonDeleteCommentClicked(commentId);

			}));
		} else {
			buttonBlock.addClickListener(event -> listeners.forEach(viewListener -> viewListener
					.buttonBlockSubmitClicked(userId, blockDateTimeField.getValue(), blockDescrTextField.getValue())));
		}
		userBlockWindow.setContent(new VerticalLayout(warnLabel, blockDateTimeField, blockDescrTextField, buttonBlock));
		return userBlockWindow;
	}
}
package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.authorization.User;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "message")
public class Comment extends DatabaseEntity {

	private static final long serialVersionUID = -5328296118776124391L;
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	@ManyToOne
	private Movie movie;
	@Column(name = "message", length = 512, nullable = false)
	private String message;
	@Column(name = "edited")
	private boolean edited;
	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private Set<CommentReputation> commentReputations;

	public Comment() {
	}

	public Comment(User user, Movie movie, String message) {
		this.user = user;
		this.movie = movie;
		this.message = message;
	}

	public Set<CommentReputation> getCommentReputations() {
		return commentReputations;
	}

	public void setCommentReputations(Set<CommentReputation> commentReputations) {
		this.commentReputations = commentReputations;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		Comment comment1 = (Comment) o;

		return user.equals(comment1.user) && message.equals(comment1.message);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + user.hashCode();
		result = 31 * result + message.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Comment{" + "user=" + user + ", movie=" + movie + ", message='" + message + '\'' + ", edited=" + edited
				+ '}';
	}
}

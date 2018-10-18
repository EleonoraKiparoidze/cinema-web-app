package com.rustedbrain.study.course.model.persistence.cinema;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.authorization.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "commentReputation")
public class CommentReputation extends DatabaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1604635725109976727L;
	private boolean plus;
	@ManyToOne(fetch = FetchType.EAGER)
	private Comment comment;
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isPlus() {
		return plus;
	}

	public void setPlus(boolean plus) {
		this.plus = plus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		CommentReputation that = (CommentReputation) o;

		if (!comment.equals(that.comment))
			return false;
		return user.equals(that.user);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + comment.hashCode();
		result = 31 * result + user.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "CommentReputation{" + "plus=" + plus + ", comment=" + comment + ", user=" + user + '}';
	}
}

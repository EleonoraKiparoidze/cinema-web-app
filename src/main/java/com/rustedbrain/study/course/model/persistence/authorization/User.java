package com.rustedbrain.study.course.model.persistence.authorization;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;
import com.rustedbrain.study.course.model.persistence.cinema.City;
import com.rustedbrain.study.course.model.persistence.cinema.Comment;
import com.rustedbrain.study.course.model.persistence.cinema.CommentReputation;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "userId")
@Table(name = "user")
public class User extends DatabaseEntity {

	private static final long serialVersionUID = 5038832294900133878L;
	@Column(name = "login", length = 64, nullable = false, unique = true)
	protected String login;
	@Column(name = "password", length = 64, nullable = false)
	protected String password;
	@Column(name = "name", length = 64)
	protected String name;
	@Column(name = "surname", length = 64)
	protected String surname;
	@Column(name = "email", length = 64, nullable = false, unique = true)
	protected String email;
	@Column(name = "birthday")
	protected Date birthday;
	@ManyToOne
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	protected City city;
	@Column(name = "blockPeriod")
	private Date blockPeriod;
	@Column(name = "blockDescription")
	private String blockDescription;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Comment> comments;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private Set<CommentReputation> commentReputations;

	public User(String login, String password, String email) {
		this.login = login;
		this.password = password;
		this.email = email;
	}

	public User() {
	}

	public Set<CommentReputation> getCommentReputations() {
		return commentReputations;
	}

	public void setCommentReputations(Set<CommentReputation> commentReputations) {
		this.commentReputations = commentReputations;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String getBlockDescription() {
		return blockDescription;
	}

	public void setBlockDescription(String blockDescription) {
		this.blockDescription = blockDescription;
	}

	public Date getBlockPeriod() {
		return blockPeriod;
	}

	public void setBlockPeriod(Date blockPeriod) {
		this.blockPeriod = blockPeriod;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		if ( !super.equals(o) )
			return false;

		User user = (User) o;

		return email.equals(user.email);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + email.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "User{" + "login='" + login + '\'' + ", name='" + name + '\'' + ", surname='" + surname + '\'' + '}';
	}
}

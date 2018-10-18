package com.rustedbrain.study.course.model.persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class DatabaseEntity implements Serializable {

	private static final long serialVersionUID = 7773652274376011976L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	@Column(name = "registrationDate")
	private Date registrationDate;
	@Column(name = "lastAccessDate")
	private Date lastAccessDate;

	public DatabaseEntity() {
	}

	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;

		DatabaseEntity that = (DatabaseEntity) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		return "DatabaseEntity{" + "id=" + id + ", registrationDate=" + registrationDate + '}';
	}
}

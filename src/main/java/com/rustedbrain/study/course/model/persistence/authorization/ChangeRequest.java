package com.rustedbrain.study.course.model.persistence.authorization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.rustedbrain.study.course.model.persistence.DatabaseEntity;

@Entity
@Table(name = "changeRequest")
public class ChangeRequest extends DatabaseEntity {

	private static final long serialVersionUID = 3105327626155974718L;
	@Column(name = "table", length = 64, nullable = false)
	private String table;
	@Column(name = "fieldName", length = 64, nullable = false)
	private String fieldName;
	@Column(name = "userId", nullable = false)
	private long userId;
	@Column(name = "value", length = 64)
	private String value;
	@Column(name = "accepted")
	private boolean accepted;

	public boolean isAccepted() {
		return this.accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public ChangeRequest(String table, String fieldName, long userId, String value) {
		super();
		this.table = table;
		this.fieldName = fieldName;
		this.userId = userId;
		this.value = value;
	}

	public ChangeRequest() {
	}

	public String getTable() {
		return this.table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.accepted ? 1231 : 1237);
		result = prime * result + ((this.fieldName == null) ? 0 : this.fieldName.hashCode());
		result = prime * result + ((this.table == null) ? 0 : this.table.hashCode());
		result = prime * result + (int) (this.userId ^ (this.userId >>> 32));
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj )
			return true;
		if ( !super.equals(obj) )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		ChangeRequest changeRequest = (ChangeRequest) obj;

		return table.equals(changeRequest.table) && fieldName.equals(changeRequest.fieldName)
				&& userId == changeRequest.userId && value.equals(changeRequest.value);
	}

	@Override
	public String toString() {
		return "ChangeRequest [table=" + this.table + ", fieldName=" + this.fieldName + ", value=" + this.value + "]";
	}

}

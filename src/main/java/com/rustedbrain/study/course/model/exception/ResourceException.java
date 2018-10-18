package com.rustedbrain.study.course.model.exception;

/**
 * The MyBusinessException wraps all checked standard Java exception and
 * enriches them with a custom error code. You can use this code to retrieve
 * localized error messages and to link to our online documentation.
 *
 */
public class ResourceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4320203462434491289L;

	public ResourceException(Exception e) {
		super(e);
	}
}

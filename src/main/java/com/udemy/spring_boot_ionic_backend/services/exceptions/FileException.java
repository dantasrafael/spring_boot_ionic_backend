package com.udemy.spring_boot_ionic_backend.services.exceptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

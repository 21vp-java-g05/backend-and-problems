package main.frontend.backend.utils;

import java.sql.Date;

public class Time {
	private java.util.Date current = new java.util.Date();

	public java.sql.Date currentTime() {
		return new Date(current.getTime());
	}
}

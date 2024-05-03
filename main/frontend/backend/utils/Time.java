package main.frontend.backend.utils;

import java.sql.*;

public class Time {
	private java.util.Date time;

	public Time() { time = new java.util.Date(); }
	public Time(long other) { time = new java.util.Date(other); }
	public Time(java.util.Date other) { time = other; }
	public Time(java.sql.Date other) { time = new java.util.Date(other.getTime()); }

	public java.sql.Date currentTime() {
		return new Date(time.getTime());
	}
	public Timestamp getTimestamp() {
		return new Timestamp(time.getTime());
	}

	@Override
	public String toString() {
		return String.valueOf(getTimestamp());
	}
}

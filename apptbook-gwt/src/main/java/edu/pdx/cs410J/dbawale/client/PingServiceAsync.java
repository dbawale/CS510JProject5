package edu.pdx.cs410J.dbawale.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;

/**
 * The client-side interface to the ping service
 */
public interface PingServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void ping(AsyncCallback<AppointmentBook> async);

  void getAppts(AsyncCallback<AppointmentBook> async);

  void addAppt(Appointment appt, String owner, AsyncCallback<Void> async);

  void getOwner(AsyncCallback<String> async);

  void search(Date start, Date end, AsyncCallback<ArrayList<Appointment>>async);
}

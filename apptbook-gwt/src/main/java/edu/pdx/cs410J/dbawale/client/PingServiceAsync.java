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

  /**
   * Return the AppointmentBook on the server
   * @param async The asynchronous callback
     */
  void getAppts(AsyncCallback<AppointmentBook> async);

  /**
   * Add a new appointment to the AppointmentBook on server
   * @param appt The appointment book to be added
   * @param owner The owner of the appointment book
   * @param async The asynchronous callback
     */
  void addAppt(Appointment appt, String owner, AsyncCallback<Void> async);

  /**
   * Return the owner name of the AppointmentBook on server
   * @param async The asynchronous callback
     */
  void getOwner(AsyncCallback<String> async);

  /**
   * Search for appointments on the server
   * @param start Starting date of search
   * @param end Ending date of search
   * @param async The asynchronous callback
     */
  void search(Date start, Date end, AsyncCallback<ArrayList<Appointment>>async);
}

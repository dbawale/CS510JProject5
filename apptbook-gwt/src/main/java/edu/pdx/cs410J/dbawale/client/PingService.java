package edu.pdx.cs410J.dbawale.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.Date;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("ping")
public interface PingService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  public AppointmentBook ping();

  void addAppt(Appointment appt, String owner);

  String getOwner();

  public AppointmentBook getAppts();

  ArrayList<Appointment> search(Date start, Date end);
}

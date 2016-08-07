package edu.pdx.cs410J.dbawale.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dbawale.client.Appointment;
import edu.pdx.cs410J.dbawale.client.AppointmentBook;
import edu.pdx.cs410J.dbawale.client.PingService;

import java.util.ArrayList;
import java.util.Date;

/**
 * The server-side implementation of the AppointmentBook service
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{
  private AppointmentBook book = new AppointmentBook();
  Boolean first = true;

  /**
   * Creates a new, blank appointment book and returns it
   * @return The newly created appointment book
     */
  @Override
  public AppointmentBook ping() {
    AppointmentBook book = new AppointmentBook();
    book.addAppointment(new Appointment());
    return book;
  }

  /**
   * Adds a new appointment to the appointment book
   * @param appt The Appointment to be added
   * @param owner The owner of the AppointmentBook to which this appointment is to be added
     */
  @Override
  public void addAppt(Appointment appt, String owner) {
    if(first==true){
      first=false;
      book = new AppointmentBook(owner);
    }
      book.addAppointment(appt);
  }

  /**
   * Returns the owner of the appointment book on server
   * @return The name of the owner
     */
  @Override
  public String getOwner() {
    return book.getOwnerName();
  }

  /**
   * Returns the current appointment book
   * @return The current appointment book
     */
  @Override
  public AppointmentBook getAppts() {
    return this.book;
  }

  /**
   * Searches for appointments between the range start and end
   * @param start The start date for the search
   * @param end The end date for the search
   * @return An ArrayList of appointments matching the search parameters
     */
  @Override
  public ArrayList<Appointment> search(Date start, Date end) {
    ArrayList<Appointment> appts = (ArrayList<Appointment>) book.getAppointments();
    ArrayList<Appointment> toret = new ArrayList<>();
    for(Appointment appt : appts)
    {
      if(appt.getBeginTime().compareTo(start)>=0&&appt.getEndTime().compareTo(end)<=0)
      {
        toret.add(appt);
      }
    }
    return toret;
  }

  /**
   * Handles unexpected things
   * @param unhandled A throwable with the unhandled error.
     */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}

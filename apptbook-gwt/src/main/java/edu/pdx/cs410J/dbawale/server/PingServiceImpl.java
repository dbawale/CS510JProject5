package edu.pdx.cs410J.dbawale.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dbawale.client.Appointment;
import edu.pdx.cs410J.dbawale.client.AppointmentBook;
import edu.pdx.cs410J.dbawale.client.PingService;

/**
 * The server-side implementation of the division service
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{
  private AppointmentBook book = new AppointmentBook();

  @Override
  public AppointmentBook ping() {
    AppointmentBook book = new AppointmentBook();
    book.addAppointment(new Appointment());
    return book;
  }

  @Override
  public void addAppt(Appointment appt, String owner) {

      book.addAppointment(appt);
  }

  @Override
  public String getOwner() {
    return book.getOwnerName();
  }

  @Override
  public AppointmentBook getAppts() {
    return this.book;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}

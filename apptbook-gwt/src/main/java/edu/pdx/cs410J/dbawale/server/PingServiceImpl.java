package edu.pdx.cs410J.dbawale.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dbawale.client.Appointment;
import edu.pdx.cs410J.dbawale.client.AppointmentBook;
import edu.pdx.cs410J.dbawale.client.PingService;

import java.util.ArrayList;
import java.util.Date;

/**
 * The server-side implementation of the division service
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{
  private AppointmentBook book = new AppointmentBook();
  Boolean first = true;

  @Override
  public AppointmentBook ping() {
    AppointmentBook book = new AppointmentBook();
    book.addAppointment(new Appointment());
    return book;
  }

  @Override
  public void addAppt(Appointment appt, String owner) {
    if(first==true){
      first=false;
      book = new AppointmentBook(owner);
    }
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

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}

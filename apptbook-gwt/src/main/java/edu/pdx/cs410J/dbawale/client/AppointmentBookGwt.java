package edu.pdx.cs410J.dbawale.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.thirdparty.common.css.compiler.ast.ParseException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  private String owner;
  private String descriptionstr;
  private Date apptstartDate;
  private Date apptendDate;
  private Date searchStartDate;
  private Date searchEndDate;

  DateBox startdateBox;
  DateBox enddateBox;
  DateBox searchStartDateBox;
  DateBox searchEndDateBox;
  Date startDate,endDate;
  Button newApptSubmitBtn;
  Button searchApptSubmitBtn;
  HorizontalPanel hpanelmain = new HorizontalPanel();
  VerticalPanel vpaneladdappt = new VerticalPanel();
  VerticalPanel vpanelallappts = new VerticalPanel();
  VerticalPanel vpanelsearch = new VerticalPanel();
  HorizontalPanel hpanelsearchstartdate;
  HorizontalPanel hpanelsearchenddate;
  TabLayoutPanel tabpanel = new TabLayoutPanel(2.0, Style.Unit.EM);
  @VisibleForTesting
  Button button = new Button("Show All");

  String helpstring = "This website lets you create an appointment book.\n" +
          "It also lets you add appointments.\n" +
          "Functionality for searching for appointments is present.\n" +
          "Simply navigate through the tabs and try it out yourself!";

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;
    addWidgets();
  }

  private void addWidgets() {
    addAllApptsTab();
    AddAppointmentFormToTab();
    vpanelallappts.setWidth("100%");
    tabpanel.add(vpanelallappts,"All Appointments");
    addSearchAppts();

  }

  private void addAllApptsTab() {

    vpanelallappts.add(button);
    button.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        PingServiceAsync async = GWT.create(PingService.class);
        async.getAppts(new AsyncCallback<AppointmentBook>() {
          @Override
          public void onFailure(Throwable throwable) {
            alerter.alert(throwable.toString());
          }

          @Override
          public void onSuccess(AppointmentBook appointmentBook) {
            ArrayList appts = (ArrayList)appointmentBook.getAppointments();
            vpanelallappts.add(new Label("There are " + appts.size() + " appointments in this appointment book"));
            vpanelallappts.add(new Label("These apppointments are: "));
            for(int i=0;i<appts.size();i++)
            {
              Appointment appt = (Appointment)appts.get(i);
              vpanelallappts.add(new Label( (i+1)+ ": " + appt.description));
              vpanelallappts.add(new Label( "Starts At: " + appt.getBeginTimeString() + " Ends At: " + appt.getEndTimeString()));
              long diff = appt.endTime.getTime() - appt.beginTime.getTime();
              //vpanelallappts.add(new Label("   The duration of this appointment is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes. \n\n");
              //alerter.alert(appt.toString());
            }
            button.setEnabled(false);
          }
        });
      }
    });
//    button.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent clickEvent) {
//        PingServiceAsync async = GWT.create(PingService.class);
//        async.ping(new AsyncCallback<AppointmentBook>() {
//
//          @Override
//          public void onFailure(Throwable ex) {
//            alerter.alert(ex.toString());
//          }
//
//          @Override
//          public void onSuccess(AppointmentBook airline) {
//            StringBuilder sb = new StringBuilder(airline.toString());
//            Collection<Appointment> flights = airline.getAppointments();
//            for (Appointment flight : flights) {
//              sb.append(flight);
//              sb.append("\n");
//            }
//            alerter.alert(sb.toString());
//
//          }
//        });
//      }
//    });
  }

  private void addSearchAppts() {
    addWidgetsToSearchTab();
    searchApptSubmitBtn.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchStartDate = searchStartDateBox.getValue();
        searchEndDate = searchEndDateBox.getValue();
//        alerter.alert(DateTimeFormat.getShortDateTimeFormat().format(searchStartDate));
//        alerter.alert(DateTimeFormat.getShortDateTimeFormat().format(searchEndDate));
        PingServiceAsync async = GWT.create(PingService.class);
        async.search(searchStartDate, searchEndDate, new AsyncCallback<ArrayList<Appointment>>() {
          @Override
          public void onFailure(Throwable throwable) {
            alerter.alert("Error searching on server");
          }

          @Override
          public void onSuccess(ArrayList<Appointment> appointments) {
            int i=1;
            if(appointments.size()>0) {
              vpanelsearch.add(new Label("The search returned " + appointments.size() + " appointments. These are:"));
              for (Appointment appt : appointments) {
                vpanelsearch.add(new Label(i + ": " + appt.description));
                vpanelsearch.add(new Label("Starts At: " + appt.getBeginTimeString() + " Ends At: " + appt.getEndTimeString()));
                i++;
              }
            }
            else {
              alerter.alert("No appointments in given time range");
            }
            searchApptSubmitBtn.setEnabled(false);
          }
        });
      }
    });
    tabpanel.add(vpanelsearch,"Search Appointments");
    tabpanel.add(new Label(this.helpstring),"Help");
    tabpanel.setHeight("90%");
  }

  private void addWidgetsToSearchTab() {
    hpanelsearchstartdate = new HorizontalPanel();
    hpanelsearchstartdate.add(new Label("Start Date:"));
    searchStartDateBox = new DateBox();
    searchStartDateBox.setValue(new Date());
    hpanelsearchstartdate.add(searchStartDateBox);
    vpanelsearch.add(hpanelsearchstartdate);

    hpanelsearchenddate = new HorizontalPanel();
    hpanelsearchenddate.add(new Label("End Date:"));
    searchEndDateBox = new DateBox();
    searchEndDateBox.setValue(new Date());
    hpanelsearchenddate.add(searchEndDateBox);
    vpanelsearch.add(hpanelsearchenddate);
    vpanelsearch.setWidth("100%");

    searchApptSubmitBtn = new Button("Search!");
    vpanelsearch.add(searchApptSubmitBtn);
  }

  private void AddAppointmentFormToTab() {
    final TextBox ownername = new TextBox();
    ownername.setName("Owner");
    ownername.setFocus(true);
    Label ownerlabel = new Label("Owner:");
    ownerlabel.setWidth("50%");
    HorizontalPanel ownerpanel = new HorizontalPanel();
//    ownerpanel.setWidth("100%");
    ownerpanel.add(ownerlabel);
    ownerpanel.add(ownername);


    final TextBox description = new TextBox();
    description.setName("Description");
    Label descriptionlabel = new Label("Description:");
    descriptionlabel.setWidth("50%");
    HorizontalPanel descriptionpanel = new HorizontalPanel();
//    descriptionpanel.setWidth("100%");
    descriptionpanel.add(descriptionlabel);
    descriptionpanel.add(description);

    startdateBox = new DateBox();
    startdateBox.setValue(new Date());
    enddateBox = new DateBox();

    enddateBox.setValue(new Date());

    tabpanel.setWidth("100%");
    vpaneladdappt.add(new Label("Fill in the details, then click Add"));
    vpaneladdappt.add(new Label("All fields are required"));
    vpaneladdappt.add(ownerpanel);
    vpaneladdappt.add(descriptionpanel);
    HorizontalPanel startdatepanel = new HorizontalPanel();
    startdatepanel.add(new Label("Start Date: "));
    startdatepanel.add(startdateBox);
    vpaneladdappt.add(startdatepanel);
    HorizontalPanel enddatepanel = new HorizontalPanel();
    enddatepanel.add(new Label("End Date: "));
    enddatepanel.add(enddateBox);
    vpaneladdappt.add(enddatepanel);
    vpaneladdappt.setWidth("100%");

    vpaneladdappt.setCellHeight(ownerpanel,"10%");
    vpaneladdappt.setCellHeight(descriptionpanel,"10%");
    tabpanel.add(vpaneladdappt,"Add Appointment");
    newApptSubmitBtn = new Button("Add");
    vpaneladdappt.add(newApptSubmitBtn);
    newApptSubmitBtn.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if(!ownername.getValue().equals("")&&!description.getValue().equals("")&& startdateBox.getValue()!=null && enddateBox.getValue()!=null) {
         // alerter.alert(ownername.getText() + description.getText() + startdateBox.getValue().toString() + enddateBox.getValue().toString());
          //if(dateTimeFormat.parse(startdateBox,"yyyy-MM-dd HH:mm",true))
          //alerter.alert(DateTimeFormat.getShortDateTimeFormat().format(startdateBox.getValue()));
          //perform input validation here


          owner = ownername.getText();
          descriptionstr = description.getText();
          apptstartDate= startdateBox.getValue();

          apptendDate = enddateBox.getValue();

          PingServiceAsync async = GWT.create(PingService.class);
          async.getOwner(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
              alerter.alert("Could not add appointment");
            }

            @Override
            public void onSuccess(String s) {

              if(!s.equals(owner)&&!s.equals(null))
              {
                alerter.alert("Owner name not found on server");
              }
              else{
                //async call to add appointment
                PingServiceAsync async1 = GWT.create(PingService.class);
                async1.addAppt(new Appointment(descriptionstr, apptstartDate, apptendDate), owner, new AsyncCallback<Void>() {
                  @Override
                  public void onFailure(Throwable throwable) {
                    alerter.alert("Could not add appointment");
                  }

                  @Override
                  public void onSuccess(Void aVoid) {
                    alerter.alert("Appointment added!");
                  }
                });
              }
            }
          });
        }
        else
        {
          alerter.alert("All fields are required! If you typed in all fields, check format of date/time.");
        }
        ownername.setText("");
        description.setText("");
        startdateBox.setValue(new Date());
        enddateBox.setValue(new Date());
      }
    });
  }

  @Override
  public void onModuleLoad() {
    RootLayoutPanel rootPanel = RootLayoutPanel.get();
//    rootPanel.add(button);
    tabpanel.addSelectionHandler(new SelectionHandler<Integer>() {
      @Override
      public void onSelection(SelectionEvent<Integer> selectionEvent) {
        int tabid = selectionEvent.getSelectedItem();
        if(tabid == 1)
        {
          vpanelallappts.clear();
          button.setEnabled(true);
          vpanelallappts.add(button);
        }
        if(tabid ==2)
        {
          vpanelsearch.clear();
          searchApptSubmitBtn.setEnabled(true);
          vpanelsearch.add(hpanelsearchstartdate);
          vpanelsearch.add(hpanelsearchenddate);
          vpanelsearch.add(searchApptSubmitBtn);
        }

      }
    });
    rootPanel.add(tabpanel);
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}

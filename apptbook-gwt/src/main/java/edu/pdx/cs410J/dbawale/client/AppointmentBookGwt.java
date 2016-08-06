package edu.pdx.cs410J.dbawale.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import java.util.Collection;
import java.util.Date;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;
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
  TabLayoutPanel tabpanel = new TabLayoutPanel(2.0, Style.Unit.EM);
  @VisibleForTesting
  Button button;
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
    button = new Button("Show All");
    vpanelallappts.add(button);
    button.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        PingServiceAsync async = GWT.create(PingService.class);
        async.ping(new AsyncCallback<AppointmentBook>() {

          @Override
          public void onFailure(Throwable ex) {
            alerter.alert(ex.toString());
          }

          @Override
          public void onSuccess(AppointmentBook airline) {
            StringBuilder sb = new StringBuilder(airline.toString());
            Collection<Appointment> flights = airline.getAppointments();
            for (Appointment flight : flights) {
              sb.append(flight);
              sb.append("\n");
            }
            alerter.alert(sb.toString());

          }
        });
      }
    });
  }

  private void addSearchAppts() {
    HorizontalPanel hpanelsearchstartdate = new HorizontalPanel();
    hpanelsearchstartdate.add(new Label("Start Date:"));
    searchStartDateBox = new DateBox();
    searchStartDateBox.setValue(new Date());
    hpanelsearchstartdate.add(searchStartDateBox);
    vpanelsearch.add(hpanelsearchstartdate);

    HorizontalPanel hpanelsearchenddate = new HorizontalPanel();
    hpanelsearchenddate.add(new Label("End Date:"));
    searchEndDateBox = new DateBox();
    searchEndDateBox.setValue(new Date());
    hpanelsearchenddate.add(searchEndDateBox);
    vpanelsearch.add(hpanelsearchenddate);
    vpanelsearch.setWidth("100%");

    searchApptSubmitBtn = new Button("Search!");
    vpanelsearch.add(searchApptSubmitBtn);
    tabpanel.add(vpanelsearch,"Search Appointments");
    tabpanel.add(new Label(this.helpstring),"Help");
    tabpanel.setHeight("90%");
  }

  private void AddAppointmentFormToTab() {
    TextBox ownername = new TextBox();
    ownername.setName("Owner");
    ownername.setFocus(true);
    Label ownerlabel = new Label("Owner:");
    ownerlabel.setWidth("50%");
    HorizontalPanel ownerpanel = new HorizontalPanel();
//    ownerpanel.setWidth("100%");
    ownerpanel.add(ownerlabel);
    ownerpanel.add(ownername);


    TextBox description = new TextBox();
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
        alerter.alert("Submit clicked");
      }
    });
  }

  @Override
  public void onModuleLoad() {
    RootLayoutPanel rootPanel = RootLayoutPanel.get();
//    rootPanel.add(button);
    rootPanel.add(tabpanel);
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}

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
import edu.pdx.cs410J.AbstractAppointment;

import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;
  HorizontalPanel hpanelmain = new HorizontalPanel();
  VerticalPanel vpaneltab1 = new VerticalPanel();
  TabLayoutPanel tabpanel = new TabLayoutPanel(2.0, Style.Unit.EM);
  @VisibleForTesting
  Button button;

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
    button = new Button("Ping Server");
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

    tabpanel.setWidth("100%");
    vpaneltab1.add(ownerpanel);
    vpaneltab1.add(descriptionpanel);
    vpaneltab1.setWidth("100%");

    vpaneltab1.setCellHeight(ownerpanel,"10%");
    vpaneltab1.setCellHeight(descriptionpanel,"10%");
    tabpanel.add(vpaneltab1,"Add Appointment");
    button.setWidth("50%");
    button.setHeight("20%");
    tabpanel.add(button,"Ping Button");
    tabpanel.setHeight("90%");
//    vpaneltab1.add(description);
//    hpanelmain.add(vpaneltab1);
//    hpanelmain.add(button);
//    hpanelmain.setWidth("");

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

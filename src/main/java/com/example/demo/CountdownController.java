package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownController {

    @FXML
    private TextField textFieldDatumInput;
    @FXML
    private Label labelVisszaszamlalo;

    private LocalDateTime jelenlegiDatum;

    private LocalDateTime BevittDatum;
    final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
    @FXML
    private void Visszaszamlal() {
        Timer timer = new Timer();
        jelenlegiDatum = LocalDateTime.now();
        String formatted = jelenlegiDatum.format(DATE_FORMAT);
        String datumstring =  textFieldDatumInput.getText();
        if (DatumCheck(datumstring)) {
            BevittDatum = LocalDateTime.parse(datumstring, DATE_FORMAT);
            if (BevittDatum.isAfter(jelenlegiDatum)) {

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            jelenlegiDatum = LocalDateTime.now();
                            Duration diff = Duration.between(jelenlegiDatum, BevittDatum);
                            Period diff2 = Period.between(jelenlegiDatum.toLocalDate(), BevittDatum.toLocalDate());
                            //ezt stackoverflowrol loptam egy kicsit bugos mert ha hónap fordulónál van akkor -1 napot mutat sooo ezzel amugy pont azt akartam elérni hogy ne mutasson még 1 napot mikor 1 napon belül ér
                            diff2 = diff2.minusDays(BevittDatum.toLocalTime().compareTo(jelenlegiDatum.toLocalTime()) >= 0 ? 0 : 1);
                               String id = String.format("%d év %d hó %d nap %s:%s:%s", diff2.getYears(),diff2.getMonths(), diff2.getDays(),
                                   diff.toHoursPart() < 10 ? "0" + diff.toHoursPart() : diff.toHoursPart(),
                                   diff.toMinutesPart() < 10 ? "0" + diff.toMinutesPart() : diff.toMinutesPart(),
                                   diff.toSecondsPart() < 10 ? "0" + diff.toSecondsPart() : diff.toSecondsPart());
                            labelVisszaszamlalo.setText(id);
                            if (diff.getSeconds() == 0) {
                                timer.cancel();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Lejárt a visszaszámlálás");
                                alert.setHeaderText("Elérkeztél a visszaszámláslás végére");
                                alert.show();
                            }
                        });
                    }
                };
                timer.schedule(task, 0, 100);


            } else {
                labelVisszaszamlalo.setText("ön időutazó?");
            }

        } else {
            labelVisszaszamlalo.setText("Nem Megfelelő Formátum");
        }


    }
    private boolean DatumCheck(String datumstring) {
        try {
            LocalDateTime.parse(datumstring, DATE_FORMAT);
            return true;
        } catch (Exception E) {
            return false;
        }
    }

}
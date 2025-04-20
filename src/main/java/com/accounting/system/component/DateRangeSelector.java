package com.accounting.system.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateRangeSelector extends VBox {
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final ComboBox<String> presetPeriods;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;

    public DateRangeSelector() {
        this.startDate = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();

        // Create date pickers
        startDatePicker = new DatePicker(LocalDate.now().withDayOfMonth(1));
        endDatePicker = new DatePicker(LocalDate.now());
        
        startDatePicker.setPromptText("Start Date");
        endDatePicker.setPromptText("End Date");

        // Create preset periods combo box
        presetPeriods = new ComboBox<>(FXCollections.observableArrayList(
            "Current Month", "Last Month", "Current Quarter", "Last Quarter", 
            "Current Year", "Last Year", "Custom"
        ));
        presetPeriods.setValue("Current Month");

        // Create clear button
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clear());

        // Layout
        HBox datePickersBox = new HBox(5, 
            new Label("From"), startDatePicker,
            new Label("To"), endDatePicker
        );
        
        HBox controlsBox = new HBox(5,
            new Label("Period:"), presetPeriods,
            clearButton
        );

        getChildren().addAll(controlsBox, datePickersBox);
        setSpacing(5);
        setPadding(new Insets(5));

        // Setup listeners
        setupListeners();
        
        // Initial setup
        updateDatesFromPreset("Current Month");
    }

} 
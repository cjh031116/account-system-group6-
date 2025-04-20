package com.accounting.system.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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

    private void setupListeners() {
        // Update properties when date pickers change
        startDatePicker.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                startDate.set(newVal);
                if (endDatePicker.getValue() != null && 
                    newVal.isAfter(endDatePicker.getValue())) {
                    endDatePicker.setValue(newVal);
                }
                presetPeriods.setValue("Custom");
            }
        });

        endDatePicker.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                endDate.set(newVal);
                if (startDatePicker.getValue() != null && 
                    newVal.isBefore(startDatePicker.getValue())) {
                    startDatePicker.setValue(newVal);
                }
                presetPeriods.setValue("Custom");
            }
        });

        // Handle preset period selection
        presetPeriods.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.equals("Custom")) {
                updateDatesFromPreset(newVal);
            }
        });
    }

    private void updateDatesFromPreset(String preset) {
        LocalDate now = LocalDate.now();
        
        switch (preset) {
            case "Current Month":
                startDatePicker.setValue(now.withDayOfMonth(1));
                endDatePicker.setValue(now.with(TemporalAdjusters.lastDayOfMonth()));
                break;
                
            case "Last Month":
                LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
                startDatePicker.setValue(firstDayOfLastMonth);
                endDatePicker.setValue(firstDayOfLastMonth.with(TemporalAdjusters.lastDayOfMonth()));
                break;
                
            case "Current Quarter":
                LocalDate firstDayOfQuarter = now.with(now.getMonth().firstMonthOfQuarter())
                    .withDayOfMonth(1);
                startDatePicker.setValue(firstDayOfQuarter);
                endDatePicker.setValue(firstDayOfQuarter.plusMonths(2)
                    .with(TemporalAdjusters.lastDayOfMonth()));
                break;
                
            case "Last Quarter":
                LocalDate firstDayOfLastQuarter = now.minusMonths(3)
                    .with(now.minusMonths(3).getMonth().firstMonthOfQuarter())
                    .withDayOfMonth(1);
                startDatePicker.setValue(firstDayOfLastQuarter);
                endDatePicker.setValue(firstDayOfLastQuarter.plusMonths(2)
                    .with(TemporalAdjusters.lastDayOfMonth()));
                break;
                
            case "Current Year":
                startDatePicker.setValue(now.withDayOfYear(1));
                endDatePicker.setValue(now.with(TemporalAdjusters.lastDayOfYear()));
                break;
                
            case "Last Year":
                LocalDate lastYear = now.minusYears(1);
                startDatePicker.setValue(lastYear.withDayOfYear(1));
                endDatePicker.setValue(lastYear.with(TemporalAdjusters.lastDayOfYear()));
                break;
        }
    }

    private void clear() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        presetPeriods.setValue("Custom");
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate date) {
        startDatePicker.setValue(date);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate date) {
        endDatePicker.setValue(date);
    }
} 
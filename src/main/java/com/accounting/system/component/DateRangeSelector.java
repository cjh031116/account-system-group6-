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

/**
 * Custom component for selecting a date range with preset periods.
 * Provides a user interface for selecting start and end dates with common period presets.
 */
public class DateRangeSelector extends VBox {
    // UI Components for date selection
    private final DatePicker startDatePicker; // Picker for selecting start date
    private final DatePicker endDatePicker; // Picker for selecting end date
    private final ComboBox<String> presetPeriods; // Dropdown for selecting preset periods
    
    // Properties to track selected dates
    private final ObjectProperty<LocalDate> startDate; // Property for start date
    private final ObjectProperty<LocalDate> endDate; // Property for end date

    /**
     * Constructor initializes the date range selector with default values.
     * Sets up the UI components and their initial state.
     */
    public DateRangeSelector() {
        // Initialize date properties
        this.startDate = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();

        // Create and configure date pickers
        startDatePicker = new DatePicker(LocalDate.now().withDayOfMonth(1));
        endDatePicker = new DatePicker(LocalDate.now());
        
        // Set prompt text for date pickers
        startDatePicker.setPromptText("Start Date");
        endDatePicker.setPromptText("End Date");

        // Create and configure preset periods combo box
        presetPeriods = new ComboBox<>(FXCollections.observableArrayList(
            "Current Month", "Last Month", "Current Quarter", "Last Quarter", 
            "Current Year", "Last Year", "Custom"
        ));
        presetPeriods.setValue("Current Month");

        // Create clear button
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clear());

        // Create layout containers
        HBox datePickersBox = new HBox(5, 
            new Label("From"), startDatePicker,
            new Label("To"), endDatePicker
        );
        
        HBox controlsBox = new HBox(5,
            new Label("Period:"), presetPeriods,
            clearButton
        );

        // Add components to the main container
        getChildren().addAll(controlsBox, datePickersBox);
        setSpacing(5);
        setPadding(new Insets(5));

        // Set up event listeners
        setupListeners();
        
        // Initialize with current month
        updateDatesFromPreset("Current Month");
    }

    /**
     * Sets up event listeners for date changes and preset selection.
     * Updates the date properties and preset selection based on user input.
     */
    private void setupListeners() {
        // Listen for start date changes
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

        // Listen for end date changes
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

        // Listen for preset period changes
        presetPeriods.valueProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.equals("Custom")) {
                updateDatesFromPreset(newVal);
            }
        });
    }

    /**
     * Updates the date range based on the selected preset period.
     * @param preset The name of the preset period to apply
     */
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

    /**
     * Clears the selected dates and resets to custom period.
     */
    private void clear() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        presetPeriods.setValue("Custom");
    }

    /**
     * Gets the selected start date.
     * @return The start date, or null if not set
     */
    public LocalDate getStartDate() {
        return startDate.get();
    }

    /**
     * Gets the start date property for binding.
     * @return The start date property
     */
    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    /**
     * Sets the start date.
     * @param date The date to set as start date
     */
    public void setStartDate(LocalDate date) {
        startDatePicker.setValue(date);
    }

    /**
     * Gets the selected end date.
     * @return The end date, or null if not set
     */
    public LocalDate getEndDate() {
        return endDate.get();
    }

    /**
     * Gets the end date property for binding.
     * @return The end date property
     */
    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    /**
     * Sets the end date.
     * @param date The date to set as end date
     */
    public void setEndDate(LocalDate date) {
        endDatePicker.setValue(date);
    }
} 
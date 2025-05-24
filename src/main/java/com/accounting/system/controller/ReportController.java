package com.accounting.system.controller;

import com.accounting.system.model.Voucher;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportController {
    @FXML
    private TextArea questionArea;
    @FXML
    private TextArea aiAnswerArea;
    @FXML
    private Button queryButton;
    @FXML
    private Button cancelButton;

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private PieChart pieChart;

    private VoucherService voucherService;

    /**
     * Constructor initializes the controller with default values and services.
     */
    public ReportController() {
        this.voucherService = new VoucherServiceImpl();
    }

    public void setVoucherService(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @FXML
    private void initialize() {
        List<Voucher> list = voucherService.getAllVouchers();
        loadLineChartData(list);
        loadPieChartData(list);
    }

    private void loadLineChartData(List<Voucher> list) {
        List<String> monthList = list.stream().map(Voucher::getMonth).distinct().sorted().toList();
        Map<String, Double> incomeMap = list.stream().filter(v -> "Income".equals(v.getDirection())).collect(Collectors.groupingBy(Voucher::getMonth, Collectors.summingDouble(Voucher::getTotalDebit)));
        Map<String, Double> expenseMap = list.stream().filter(v -> "Expense".equals(v.getDirection())).collect(Collectors.groupingBy(Voucher::getMonth, Collectors.summingDouble(Voucher::getTotalDebit)));
        XYChart.Series<String, Number> income = new XYChart.Series<>();
        income.setName("Income");
        for (String month : monthList) {
            income.getData().add(new XYChart.Data<>(month, incomeMap.getOrDefault(month, 0d)));
        }
        XYChart.Series<String, Number> expense = new XYChart.Series<>();
        expense.setName("Expense");
        for (String month : monthList) {
            expense.getData().add(new XYChart.Data<>(month, expenseMap.getOrDefault(month, 0d)));
        }
        Platform.runLater(() -> {
            lineChart.getData().clear();
            lineChart.getData().add(income);
            lineChart.getData().add(expense);
        });
    }

    private void loadPieChartData(List<Voucher> list) {
        Map<String, Double> map = list.stream().filter(v -> "Expense".equals(v.getDirection())).collect(Collectors.groupingBy(Voucher::getCategory, Collectors.summingDouble(Voucher::getTotalDebit)));
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        Platform.runLater(() -> {
            pieChart.setData(pieData);
        });
    }

} 
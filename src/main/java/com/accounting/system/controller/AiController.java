package com.accounting.system.controller;

import com.accounting.system.common.Utils;
import com.accounting.system.service.AiService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AiServiceImpl;
import com.alibaba.fastjson2.JSON;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AiController {
    @FXML
    private TextArea questionArea;
    @FXML
    private TextArea aiAnswerArea;
    @FXML
    private Button queryButton;
    @FXML
    private Button cancelButton;

    private AiService aiService;
    private VoucherService voucherService;
    @FXML
    private HBox statusBox;
    /**
     * Constructor initializes the controller with default values and services.
     */
    public AiController() {
        this.aiService = new AiServiceImpl();
    }

    public void setAiService(AiService aiService) {
        this.aiService = aiService;
    }

    public void setVoucherService(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleQuery() {
        try {
            aiAnswerArea.setText(aiService.suggestion(voucherService.getAllVouchers()));
        } catch (Exception e) {
            Utils.showError("Error", "Failed to save voucher: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) queryButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }


    @FXML
    private void handleClear() {
        // Clear both text areas
        questionArea.clear();
        aiAnswerArea.clear();

        // Hide status box if it exists and is visible
        if (statusBox != null && statusBox.isVisible()) {
            statusBox.setVisible(false);
        }

        // Set focus back to question area for better UX
        questionArea.requestFocus();
    }
} 
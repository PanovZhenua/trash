package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorController {

    @FXML
    private TextField displayTextField;

    private BigDecimal num1 = BigDecimal.ZERO;
    private String operation = "";
    private boolean startNewNumber = true;

    @FXML
    public void processDigit(javafx.event.ActionEvent event) {
        if (startNewNumber) {
            displayTextField.setText("");
            startNewNumber = false;
        }
        String value = ((Button)event.getSource()).getText();
        displayTextField.setText(displayTextField.getText() + value);
    }

    @FXML
    public void processOperator(javafx.event.ActionEvent event) {
        String value = ((Button)event.getSource()).getText();

        if (value.equals("=")) {
            calculate();
            return;
        } else if (value.equals("+/-")) {
            changeSign();
            return;
        }

        if (!operation.isEmpty()) {
            calculate();
        }

        try {
            num1 = new BigDecimal(displayTextField.getText());
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
            startNewNumber = true;
            return;
        }

        operation = value;
        displayTextField.setText("");
        startNewNumber = true;
    }

    private void calculate() {
        if (operation.isEmpty()) return;

        try {
            BigDecimal num2 = new BigDecimal(displayTextField.getText());
            BigDecimal result = calculate(num1, num2, operation);
            displayTextField.setText(String.valueOf(result));
            num1 = result;
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
        } finally {
            operation = "";
            startNewNumber = true;
        }
    }

    private BigDecimal calculate(BigDecimal num1, BigDecimal num2, String operation) {
        switch (operation) {
            case "+":
                return num1.add(num2);
            case "-":
                return num1.subtract(num2);
            case "*":
                return num1.multiply(num2);
            case "/":
                if (num2.compareTo(BigDecimal.ZERO) == 0) {
                    displayTextField.setText("Деление на ноль!");
                    return BigDecimal.ZERO;
                }
                return num1.divide(num2, 10, RoundingMode.HALF_UP);
            case "%":
                return num1.multiply(num2.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
            default:
                return BigDecimal.ZERO;
        }
    }

    @FXML
    public void clearDisplay() {
        displayTextField.setText("0");
        num1 = BigDecimal.ZERO;
        operation = "";
        startNewNumber = true;
    }

    private void changeSign() {
        try {
            BigDecimal num = new BigDecimal(displayTextField.getText());
            num = num.multiply(new BigDecimal("-1"));
            displayTextField.setText(num.toString());
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
        }
    }
}

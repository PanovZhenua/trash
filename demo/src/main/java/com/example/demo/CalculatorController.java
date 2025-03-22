package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorController {

    @FXML
    private TextField displayTextField;
    @FXML
    private TextField historyTextField;

    private BigDecimal number1 = BigDecimal.ZERO;
    private BigDecimal number2 = BigDecimal.ZERO;
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
        updateHistory();
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
            calculateIntermediate();
        }

        try {
            number1 = new BigDecimal(displayTextField.getText());
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
            startNewNumber = true;
            return;
        }

        operation = value;
        displayTextField.setText("");
        startNewNumber = true;
        historyTextField.setText(historyTextField.getText() + " " + operation);
    }

    private void calculateIntermediate() {
        try {
            number2 = new BigDecimal(displayTextField.getText());
            BigDecimal result = calculate(number1, number2, operation);
            number1 = result;
            displayTextField.setText(String.valueOf(result));
            historyTextField.setText(historyTextField.getText() + " " + operation + " " + number2 + " = " + result);
            operation = "";
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
        } catch (ArithmeticException e) {
            displayTextField.setText(e.getMessage());
            historyTextField.setText(historyTextField.getText() + " " + operation + " " + number2 + " = Ошибка\n");
        }
    }

    private void calculate() {
        if (operation.isEmpty()) return;

        try {
            number2 = new BigDecimal(displayTextField.getText());
            BigDecimal result = calculate(number1, number2, operation);
            displayTextField.setText(String.valueOf(result));

            // Удалите дублирование второго числа в истории
            String history = historyTextField.getText();
            int lastSpaceIndex = history.lastIndexOf(' ');
            if (lastSpaceIndex != -1) {
                String lastOperation = history.substring(lastSpaceIndex + 1);
                historyTextField.setText(history.substring(0, lastSpaceIndex + 1) + number2 + " = " + result + "\n");
            } else {
                historyTextField.setText(number2 + " = " + result + "\n");
            }

            number1 = result;
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
        } catch (ArithmeticException e) {
            displayTextField.setText(e.getMessage());
            historyTextField.setText(historyTextField.getText() + " " + number2 + " = Ошибка\n");
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
                    throw new ArithmeticException("Деление на ноль!");
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
        historyTextField.setText("");
        number1 = BigDecimal.ZERO;
        number2 = BigDecimal.ZERO;
        operation = "";
        startNewNumber = true;
    }

    private void changeSign() {
        try {
            BigDecimal num = new BigDecimal(displayTextField.getText());
            num = num.multiply(new BigDecimal("-1"));
            displayTextField.setText(num.toString());
            updateHistory();
        } catch (NumberFormatException e) {
            displayTextField.setText("Ошибка");
        }
    }

    private void updateHistory() {
        if (historyTextField.getText().isEmpty()) {
            historyTextField.setText(displayTextField.getText());
        } else if (historyTextField.getText().charAt(historyTextField.getText().length() - 1) == ' ') {
            historyTextField.setText(historyTextField.getText() + displayTextField.getText());
        } else {
            historyTextField.setText(historyTextField.getText() + " " + displayTextField.getText());
        }
    }
}

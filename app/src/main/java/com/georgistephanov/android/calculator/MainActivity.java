/**
 * I started this project as my first android app project in order to start
 * learning android development. I will aim to write a fully functional
 * calculator with the basic functionality and a pleasant UI. Hopefully,
 * I'll be able to finish this app within less than two weeks, but in the worst
 * case scenario, I would want this app finished by 13th of December.
 *
 * Project started on: 25-Nov-2017
 */

package com.georgistephanov.android.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends Activity {
	private static int MAXIMUM_NUMBER_OF_DIGITS = 15;
	private StringBuilder inputNumber;
	private Character operation;
	private StringBuilder secondInputNumber;

	private TextView v_inputNumber;
	private TextView v_operation;
	private TextView v_secondInputNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputNumber = new StringBuilder();
		secondInputNumber = new StringBuilder();

		v_inputNumber = (TextView) findViewById(R.id.input_number);
		v_inputNumber.setText("0");

		v_operation = (TextView) findViewById(R.id.operation);
		v_secondInputNumber = (TextView) findViewById(R.id.second_input_number);

		/*formatter = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
		formatter.setMaximumFractionDigits(12);*/
	}

	public void onNumberClick(View view) {
		Button pressedButton = (Button) view;
		updateInputField(pressedButton.getText().toString());
	}

	public void onClearButtonClick(View view) {
		if (inputNumber.length() > 0) {
			inputNumber = new StringBuilder();
			v_inputNumber.setText("0");

			if (operation != null) {
				operation = null;
				v_operation.setVisibility(View.GONE);
			}

			if (secondInputNumber.length() > 0) {
				secondInputNumber = new StringBuilder();
				v_secondInputNumber.setText("");
			}
		}
	}

	public void onPointClick(View view) {
		if (operation == null) {
			if (!(inputNumber.toString().contains("."))) {
				inputNumber.append(".");
				v_inputNumber.setText(inputNumber.toString());
			}
		}
		else {
			if (!(secondInputNumber.toString().contains("."))) {
				secondInputNumber.append(".");
				v_secondInputNumber.setText(secondInputNumber.toString());
			}
		}
	}

	public void onOperationClick(View view) {
		if (operation == null) {
			Button operationButton = (Button) view;
			operation = operationButton.getText().toString().charAt(0);

			v_operation.setText("" + operation);
			v_operation.setVisibility(View.VISIBLE);
		}
	}

	private void updateInputField(String toAppend) {
		if (operation == null) {
			if (inputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS ||
					inputNumber.toString().contains(".") && inputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS + 1) {

				// Disallows entering leading zeros
				if ( !(inputNumber.length() < 1 && toAppend.equals("0"))) {
					inputNumber.append(toAppend);
					v_inputNumber.setText(inputNumber.toString());
				}
			} else {
				// Maximum length of the number is reached
				Toast.makeText(this, R.string.max_input_size_toast_message, Toast.LENGTH_SHORT).show();
			}
		} else {
			// Inputting the second number
			if (secondInputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS ||
					secondInputNumber.toString().contains(".") && secondInputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS + 1) {

				// Disallows entering leading zeros
				if ( !(secondInputNumber.length() < 1 && toAppend.equals("0"))) {
					secondInputNumber.append(toAppend);
					v_secondInputNumber.setText(secondInputNumber.toString());
				}
			} else {
				// Maximum length of the number is reached
				Toast.makeText(this, R.string.max_input_size_toast_message, Toast.LENGTH_SHORT).show();
			}
		}
	}
}

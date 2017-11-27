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
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static int MAXIMUM_NUMBER_OF_DIGITS = 15;
	private DisplayMetrics displayMetrics;

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

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	}

	public void onNumberClick(View view) {
		Button pressedButton = (Button) view;
		updateInputField(pressedButton.getText().toString());

		checkFieldSizeOnInput();
	}

	private void checkFieldSizeOnInput() {
		int totalInputFieldWidth = getInputFieldWidth();
		float currentTextSize = getCurrentTextSize();

		if ( totalInputFieldWidth > 1200 ) {
			v_inputNumber.setTextSize(currentTextSize - 3);
			v_secondInputNumber.setTextSize(currentTextSize - 3);
			v_operation.setTextSize(currentTextSize - 3);
		}
	}
	private void checkFieldSizeOnDelete() {
		int totalInputFieldWidth = getInputFieldWidth();
		float currentTextSize = getCurrentTextSize();

		// TODO: This 40 should go as property and also 1200 is 12/14 of the whole resolution for other resolutions
		if ( totalInputFieldWidth < 1150 && currentTextSize < 40 ) {
			v_inputNumber.setTextSize(currentTextSize + 3);
			v_secondInputNumber.setTextSize(currentTextSize + 3);
			v_operation.setTextSize(currentTextSize + 3);
		}
	}
	private int getInputFieldWidth() {
		int totalInputFieldWidth = 0;

		if ( inputNumber.length() > 0 ) {
			totalInputFieldWidth += v_inputNumber.getWidth();
		}
		if ( operation != null ) {
			totalInputFieldWidth += v_operation.getWidth();

			if ( secondInputNumber.length() > 0 ) {
				totalInputFieldWidth += v_secondInputNumber.getWidth();
			}
		}

		return totalInputFieldWidth;
	}
	private float getCurrentTextSize() {
		return v_inputNumber.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
	}

	public void onPointClick(View view) {
		if (operation == null) {
			if (!(inputNumber.toString().contains("."))) {
				inputNumber.append(".");
				v_inputNumber.setText(inputNumber.toString());

				checkFieldSizeOnInput();
			}
		}
		else {
			if (!(secondInputNumber.toString().contains("."))) {
				secondInputNumber.append(".");
				v_secondInputNumber.setText(secondInputNumber.toString());

				checkFieldSizeOnInput();
			}
		}
	}

	public void onOperationClick(View view) {
		if (operation == null) {
			Button operationButton = (Button) view;
			operation = operationButton.getText().toString().charAt(0);

			v_operation.setText("" + operation);
			v_operation.setVisibility(View.VISIBLE);

			checkFieldSizeOnInput();
		}
	}

	public void onChangeSignClick(View view) {
		if (operation == null) {
			// The first number is being inputted
			if ( inputNumber.length() > 0 ) {

				if (inputNumber.charAt(0) != '-') {
					// Currently positive, make it negative
					inputNumber.insert(0, '-');
				} else {
					// Currently negative, make it positive
					inputNumber.deleteCharAt(0);
				}

				v_inputNumber.setText(inputNumber.toString());

			} else {
				// No number
			}
		}
		else {
			// The second number is being inputted
			if ( secondInputNumber.length() > 0 ) {
				if ( secondInputNumber.charAt(0) != '-' ) {
					// Currently positive, make it negative
					secondInputNumber.insert(0, '-');
				} else {
					// Currently negative, make it positive
					secondInputNumber.deleteCharAt(0);
				}

				v_secondInputNumber.setText(secondInputNumber.toString());

			} else {
				// No number
			}
		}
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

			// If the text size has been shrunk -> restore it
			// TODO: Read this from the resources
			v_inputNumber.setTextSize(40);
			v_secondInputNumber.setTextSize(40);
			v_operation.setTextSize(40);
		}
	}


	public void onDeleteButtonClick(View view) {
		// Check backwards and remove the last element in the correct order
		if (secondInputNumber.length() > 0) {
			secondInputNumber.deleteCharAt(secondInputNumber.length() - 1);
			v_secondInputNumber.setText(secondInputNumber.toString());
		}
		else if (operation != null) {
			operation = null;
			v_operation.setVisibility(View.GONE);
		}
		else if (inputNumber.length() > 0) {
			inputNumber.deleteCharAt(inputNumber.length() - 1);
			v_inputNumber.setText(inputNumber.toString());
		}
		else {
			// Empty input field
			return;
		}

		checkFieldSizeOnDelete();
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

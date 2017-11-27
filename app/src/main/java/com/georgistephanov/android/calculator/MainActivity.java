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
	private TextView v_inputNumber;
	private Character operation;
	//private DecimalFormat formatter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputNumber = new StringBuilder();
		v_inputNumber = (TextView) findViewById(R.id.input_number);
		v_inputNumber.setText("0");

		/*formatter = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
		formatter.setMaximumFractionDigits(12);*/
	}

	public void onOperationButtonClick(View view) {
		Button pressedButton = (Button) view;
		double numberPressed = Double.parseDouble(pressedButton.getText().toString());

		if ( inputNumber.length() == 0 && numberPressed == 0 ) {
			return;
		}

		updateInputField(pressedButton.getText().toString());
	}

	public void onClearButtonClick(View view) {
		if (inputNumber.length() > 0) {
			inputNumber = new StringBuilder();
			v_inputNumber.setText("0");
		}
	}

	public void onPointClick(View view) {
		if ( !(inputNumber.toString().contains("."))) {
			inputNumber.append(".");
			v_inputNumber.setText(inputNumber.toString());
		}
	}

	public void onOperationClick(View view) {
		if (operation == null) {
			Button operationButton = (Button) view;
			operation = operationButton.getText().toString().charAt(0);
		}
	}

	private void updateInputField(String toAppend) {
		if ( inputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS ||
				inputNumber.toString().contains(".") && inputNumber.length() < MAXIMUM_NUMBER_OF_DIGITS + 1 ) {

			inputNumber.append(toAppend);
			v_inputNumber.setText(inputNumber.toString());

		} else {
			// Maximum length of the number is reached
			Toast.makeText(this, R.string.max_input_size_toast_message, Toast.LENGTH_SHORT).show();
		}
	}
}

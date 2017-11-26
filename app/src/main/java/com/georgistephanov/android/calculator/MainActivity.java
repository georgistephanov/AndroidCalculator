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

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends Activity {
	private StringBuilder inputNumber;
	private String inputNumberString;
	private TextView v_inputNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputNumber = new StringBuilder();
		v_inputNumber = (TextView) findViewById(R.id.input_number);
	}

	public void onOperationButtonClick(View view) {
		if ( inputNumber.length() < 12 ||
				inputNumber.toString().contains(".") && inputNumber.length() < 13) {
			Button pressedButton = (Button) view;
			double numberPressed = Double.parseDouble(pressedButton.getText().toString());

			if ( inputNumber.length() == 0 && numberPressed == 0 ) {
				return;
			}

			inputNumber.append(pressedButton.getText().toString());
			v_inputNumber.setText(DecimalFormat.getNumberInstance(Locale.US).format(Double.parseDouble(inputNumber.toString())));

		} else {
			Toast.makeText(this, R.string.max_input_size_toast_message, Toast.LENGTH_SHORT).show();
		}
	}

	public void onClearButtonClick(View view) {
		if (inputNumber.length() > 0) {
			inputNumber = new StringBuilder();
			v_inputNumber.setText("");
		}
	}

	public void onPointPressed(View view) {
		if ( !(inputNumber.toString().contains("."))) {
			inputNumber.append(".");
		}
	}
}

/**
 * I started this project as my first android app project in order to start
 * learning android development. I will aim to write a fully functional
 * calculator with basic functionality and replicate the UI of the Samsung
 * integrated calculator app. The deadline I have set up for myself is 13-Dec-2017.
 * However, I'll try to finish this app within two weeks.
 *
 * @started 25-Nov-2017
 * @finished
 * @author Georgi Stefanov
 */

package com.georgistephanov.android.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;


// TODO: Create a method to set and display any of the views if necessary

public class MainActivity extends Activity {
	private static final float DEFAULT_INPUT_FIELD_TEXT_SIZE = 40;
	private static final int INPUT_FIELD_TEXT_SIZE_DECREMENT = 3;
	private static DisplayMetrics displayMetrics;

	private StringBuilder inputNumber;
	private Character operation;
	private StringBuilder secondInputNumber;
	private StringBuilder answer;

	private TextView v_inputNumber;
	private TextView v_operation;
	private TextView v_secondInputNumber;
	private TextView v_answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputNumber = new StringBuilder();
		secondInputNumber = new StringBuilder();
		answer = new StringBuilder();

		v_inputNumber = findViewById(R.id.input_number);
		v_inputNumber.setText("0");

		v_operation = findViewById(R.id.operation);
		v_secondInputNumber = findViewById(R.id.second_input_number);
		v_answer = findViewById(R.id.answer);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	}

	/**
	 * This method is invoked when any of the 10 number buttons has been pressed
	 * @param view the button pressed
	 */
	public void onNumberClick(View view) {
		Button pressedButton = (Button) view;
		_updateInputField(pressedButton.getText().toString());

		_checkFieldSizeOnInput();
	}

	/**
	 * This method is invoked when the point has been pressed
	 * @param view the point button
	 */
	public void onPointClick(View view) {
		if (operation == null) {
			if (!(inputNumber.toString().contains("."))) {
				inputNumber.append(".");
				v_inputNumber.setText(inputNumber.toString());

				_checkFieldSizeOnInput();
			}
		}
		else {
			if (!(secondInputNumber.toString().contains("."))) {
				secondInputNumber.append(".");
				v_secondInputNumber.setText(secondInputNumber.toString());

				_checkFieldSizeOnInput();
			}
		}
	}

	/**
	 * This method is invoked when any of the five operation buttons has been pressed
	 * (plus, minus, multiply, divide, percentage)
	 * @param view the operation button being pressed
	 */
	public void onOperationClick(View view) {
		if (inputNumber.length() > 0 && (operation == null || secondInputNumber.length() == 0)) {
			Button operationButton = (Button) view;
			operation = operationButton.getText().toString().charAt(0);

			v_operation.setText(operation.toString());
			v_operation.setVisibility(View.VISIBLE);

			_checkFieldSizeOnInput();
		}
		else if (answer.length() > 0) {
			// Hide the answer an set the answer value to the input number
			v_answer.setVisibility(View.GONE);
			inputNumber = answer;

			// Clear the answer and the secondInputNumber values
			answer = new StringBuilder();
			secondInputNumber = new StringBuilder();

			Button operationButton = (Button) view;
			operation = operationButton.getText().toString().charAt(0);

			v_inputNumber.setText(inputNumber);
			v_operation.setText(operation.toString());
			v_operation.setVisibility(View.VISIBLE);

			_checkFieldSizeOnInput();
		}
	}

	/**
	 * This method is invoked when the change sign button (+/-) has been pressed.
	 * It changes the sign of the number which is currently being entered.
	 * (Toggles between positive and negative)
	 * @param view the change sign button
	 */
	public void onChangeSignClick(View view) {
		// If the answer is visible set it as a value for the inputNumber and clear the answer
		if (answer.length() > 0) {
			inputNumber = answer;
			v_inputNumber.setText(inputNumber.toString());
			_cleearAnswer();
		}

		// TODO: Refactor this method
		if (operation == null) {
			// The first number is being inputted
			if ( inputNumber.length() > 0 ) {

				// Detect the case in which only a point has been entered so far and ignore it
				if (inputNumber.length() == 1 && inputNumber.charAt(0) == '.') {
					return;
				}

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
				// Detect the case in which only a point has been entered so far and ignore it
				if (secondInputNumber.length() == 1 && secondInputNumber.charAt(0) == '.') {
					return;
				}

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

	/**
	 * This method is invoked when the Clear (C) button has been pressed.
	 * It clears everything which has been inputted so far and leaves the user with
	 * just-opened-like application.
	 * @param view the clear button
	 */
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
			v_inputNumber.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_secondInputNumber.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_operation.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
		}

		if (answer.length() > 0) {
			answer = new StringBuilder();
			v_answer.setText("");
			v_answer.setVisibility(View.GONE);
		}
	}

	/**
	 * This method is invoked when the blue arrow button for deletion has been pressed.
	 * It deletes the last digit or the operation sign depending on what is the last
	 * element of the input as of the moment. It operates on a FIFO basis (just like stack).
	 * @param view the delete arrow button
	 */
	public void onDeleteButtonClick(View view) {
		// Check backwards and remove the last element in the correct order
		if (answer.length() > 0) {
			// If delete has been pressed on an answer, clear everything
			onClearButtonClick(view);
		}
		else if (secondInputNumber.length() > 0) {
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

		_checkFieldSizeOnDelete();
	}

	/**
	 * Performes the operation which has been inputted
	 * @param view the equal sign button
	 */
	public void onEqualSignClick(View view) {
		if ( v_answer.getVisibility() == View.GONE ) {
			if (inputNumber.length() > 0 && operation != null && secondInputNumber.length() > 0) {
				double a = Double.parseDouble(inputNumber.toString());
				double b = Double.parseDouble(secondInputNumber.toString());

				switch (operation) {
					case '+':
						answer.append(Operations.ADD.apply(a, b));
						_updateAnswer();
						break;
					case '-':
						answer.append(Operations.SUBTRACT.apply(a, b));
						_updateAnswer();
						break;
					case 'x':
						answer.append(Operations.MULTIPLY.apply(a, b));
						_updateAnswer();
						break;
					case '/':
						answer.append(Operations.DIVIDE.apply(a, b));
						_updateAnswer();
						break;
					default:
						break;
				}
			} else {
				Toast.makeText(this, R.string.invalid_operation_message, Toast.LENGTH_SHORT).show();
			}
		}
		else {
			// The answer is visible, therefore just reapply the operation
			double a = Double.parseDouble(answer.toString());
			double b = Double.parseDouble(secondInputNumber.toString());

			answer = new StringBuilder();

			switch (operation) {
				case '+':
					answer.append(Operations.ADD.apply(a, b));
					_updateAnswer();
					break;
				case '-':
					answer.append(Operations.SUBTRACT.apply(a, b));
					_updateAnswer();
					break;
				case 'x':
					answer.append(Operations.MULTIPLY.apply(a, b));
					_updateAnswer();
					break;
				case '/':
					answer.append(Operations.DIVIDE.apply(a, b));
					_updateAnswer();
					break;
				default:
					break;
			}
		}
	}

	/* =========== Helper Methods =========== */

	/**
	 * Calculates the width of the whole input field (the initial number, the operation
	 * sign (if present) and the second number (if present)).
	 * @return the width of the input field
	 */
	private int _getInputFieldWidth() {
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

	/**
	 * Calculates the correct text size which is being used for the input field. Takes the
	 * text size from v_inputNumber, as the three input views are supposed to be the same size.
	 * @return the current text size in scaled pixels
	 */
	private float _getCurrentTextSize() {
		return v_inputNumber.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
	}

	/**
	 * Adds a digit to the correct number, depending on which one is currently being entered.
	 * If the number has reached its maximum number of digits, it shows a toast message.
	 * @param toAppend string containing the number to be appended to the end of the correct number
	 */
	private void _updateInputField(String toAppend) {
		int MAXIMUM_NUMBER_OF_DIGITS = 15;

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

	/**
	 * Checks if the input field (the initial input number, the operation sign and the
	 * second input number) has reached the end of the screen. If so, it prevents the elements
	 * from going down by readjusting the textSize of the elements by a constant factor
	 */
	private void _checkFieldSizeOnInput() {
		int totalInputFieldWidth = _getInputFieldWidth();
		float currentTextSize = _getCurrentTextSize();

		if ( totalInputFieldWidth > displayMetrics.widthPixels * 0.85 ) {
			v_inputNumber.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_secondInputNumber.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_operation.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
		}
	}

	/**
	 * Checks if the input field (the initial input number, the operation sign and the
	 * second input number) has been resized upon deletion and if so, it restores the size
	 * of the text by the constant factor it had been decreased by.
	 */
	private void _checkFieldSizeOnDelete() {
		int totalInputFieldWidth = _getInputFieldWidth();
		float currentTextSize = _getCurrentTextSize();

		if ( totalInputFieldWidth < displayMetrics.widthPixels * 0.8 && currentTextSize < DEFAULT_INPUT_FIELD_TEXT_SIZE ) {
			v_inputNumber.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_secondInputNumber.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_operation.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
		}
	}

	// TODO: Format the string answer to scientific notation
	// TODO: Reduce the textSize if it's too big
	private void _updateAnswer() {
		v_answer.setText(answer.toString());
		v_answer.setVisibility(View.VISIBLE);

		v_operation.setVisibility(View.GONE);

		v_inputNumber.setText("");
		v_secondInputNumber.setText("");
	}

	private void _cleearAnswer() {
		if (answer.length() > 0) {
			answer = new StringBuilder();
			v_answer.setVisibility(View.GONE);

			_clearOperation();
			_clearSecondInput();
		}
	}

	private void _clearOperation() {
		if (operation != null) {
			operation = null;
			v_operation.setVisibility(View.GONE);
		}
	}

	private void _clearSecondInput() {
		if (secondInputNumber.length() > 0) {
			secondInputNumber = new StringBuilder();
			v_secondInputNumber.setText("");
		}
	}
}

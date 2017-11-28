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
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


// TODO: Create a method to set and display any of the views if necessary

public class MainActivity extends Activity {
	private static final float DEFAULT_INPUT_FIELD_TEXT_SIZE = 40;
	private static final int INPUT_FIELD_TEXT_SIZE_DECREMENT = 3;
	private static DisplayMetrics displayMetrics;

	private RelativeLayout relativeLayoutInput;

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

		relativeLayoutInput = findViewById(R.id.rl_input);

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
		if (answer.length() > 0) {
			_clearAnswer();
		}

		if (operation == null) {
			if (!(inputNumber.toString().contains("."))) {

				if ( inputNumber.length() == 0 ) {
					inputNumber.append("0.");
				} else {
					inputNumber.append(".");
				}

				v_inputNumber.setText(inputNumber.toString());

				_checkFieldSizeOnInput();
			}
		}
		else {
			if (!(secondInputNumber.toString().contains(".")) || secondInputNumber.length() == 0) {

				if ( secondInputNumber.length() == 0 ) {
					secondInputNumber.append("0.");
				} else {
					secondInputNumber.append(".");
				}

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
			StringBuilder tempAnswerHolder = answer;
			_clearAnswer();

			inputNumber = tempAnswerHolder;
			v_inputNumber.setText(inputNumber.toString());
		}

		// TODO: Refactor this method
		if (operation == null) {
			// The first number is being inputted
			if ( inputNumber.length() > 0 ) {
				changeSign(inputNumber, v_inputNumber);
			}
		}
		else {
			// The second number is being inputted
			if ( secondInputNumber.length() > 0 ) {
				changeSign(secondInputNumber, v_secondInputNumber);
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
		if (answer.length() > 0) {
			_clearAnswer();
		}
		else if (inputNumber.length() > 0) {
			inputNumber = new StringBuilder();
			v_inputNumber.setText("0");

			_clearOperation();
			_clearSecondInput();

			// If the text size has been shrunk -> restore it
			_restoreTextSize();
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
			return;	// Return to avoid checking the field size upon delete
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
		double a;
		double b;

		if ( v_answer.getVisibility() == View.GONE ) {

			if (inputNumber.length() > 0 && operation != null && secondInputNumber.length() > 0) {
				a = Double.parseDouble(inputNumber.toString());
				b = Double.parseDouble(secondInputNumber.toString());

			} else if (inputNumber.length() > 0 && operation == null) {
				// If just a number has been entered with no operation don't do anything
				return;

			} else {
				// Prints invalid operation message
				_showInvalidOperationMessage();
				return;
			}
		}
		else {
			// The answer is visible, therefore just reapply the operation
			a = Double.parseDouble(answer.toString());
			b = Double.parseDouble(secondInputNumber.toString());

			// Clear the answer as it is stored now in a
			answer = new StringBuilder();
		}


		double calculatedAnswer;

		switch (operation) {
			case '+':
				calculatedAnswer = Operations.ADD.apply(a, b);
				break;
			case '-':
				calculatedAnswer = Operations.SUBTRACT.apply(a, b);
				break;
			case 'x':
				 calculatedAnswer = Operations.MULTIPLY.apply(a, b);
				break;
			case '/':
				if (b == 0) {
					// Prevent division by zero
					_showInvalidOperationMessage();
					return;
				} else {
					 calculatedAnswer = Operations.DIVIDE.apply(a, b);
				}
				break;
			default:
				return;
		}

		answer.append(calculatedAnswer);
		_updateAnswer();
		_restoreInputFieldTextSize();
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
	private float _getCurrentTextSize(TextView v) {
		return v.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
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
		float currentTextSize = _getCurrentTextSize(v_inputNumber);

		int paddingToSubtract = (int) DEFAULT_INPUT_FIELD_TEXT_SIZE;
		paddingToSubtract += relativeLayoutInput.getPaddingLeft() + relativeLayoutInput.getPaddingRight();
		paddingToSubtract += v_operation.getPaddingLeft() + v_operation.getPaddingRight();

		if ( totalInputFieldWidth > displayMetrics.widthPixels - paddingToSubtract ) {
			v_inputNumber.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_secondInputNumber.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_operation.setTextSize(currentTextSize - INPUT_FIELD_TEXT_SIZE_DECREMENT);
		}
	}

	/**
	 * Restores the input field text views text size if it had been shrunk
	 */
	private void _restoreInputFieldTextSize() {
		if ( _getCurrentTextSize(v_inputNumber) != DEFAULT_INPUT_FIELD_TEXT_SIZE ) {
			v_inputNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_operation.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_secondInputNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_INPUT_FIELD_TEXT_SIZE);
		}

		if ( _getCurrentTextSize(v_answer) != DEFAULT_INPUT_FIELD_TEXT_SIZE ) {
			v_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_INPUT_FIELD_TEXT_SIZE);
		}
	}

	/**
	 * Checks if the input field (the initial input number, the operation sign and the
	 * second input number) has been resized upon deletion and if so, it restores the size
	 * of the text by the constant factor it had been decreased by.
	 */
	private void _checkFieldSizeOnDelete() {
		int totalInputFieldWidth = _getInputFieldWidth();
		float currentTextSize = _getCurrentTextSize(v_inputNumber);

		if ( totalInputFieldWidth < displayMetrics.widthPixels * 0.8 && currentTextSize < DEFAULT_INPUT_FIELD_TEXT_SIZE ) {
			v_inputNumber.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_secondInputNumber.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
			v_operation.setTextSize(currentTextSize + INPUT_FIELD_TEXT_SIZE_DECREMENT);
		}
	}

	/**
	 * Changes the sign of the number and updates the text view so that it
	 * shows the change.
	 * @param input the StringBuilder containing the number that is being manipulated
	 * @param v_input the TextView that should be updated
	 */
	private void changeSign(StringBuilder input, TextView v_input) {
		// Prevent minus 0
		if (input.toString().equals("0.")) {
			return;
		}

		if ( input.charAt(0) != '-' ) {
			// Currently positive, make it negative
			input.insert(0, '-');
		} else {
			// Currently negative, make it positive
			input.deleteCharAt(0);
		}

		// Update the text of the view
		v_input.setText(input);
	}

	/**
	 * Updates the answer view with the current answer that had been calculated
	 * and hides the operation and second input number so that they could be reused
	 * if equal sign is pressed consecutively and deletes the initial input number
	 * as it is no longer necessary.
	 */
	private void _updateAnswer() {
		v_answer.setText(answer.toString());
		v_answer.setVisibility(View.VISIBLE);

		// Doesn't delete the operation because it can be reapplied
		v_operation.setVisibility(View.GONE);
		v_secondInputNumber.setText("");

		// Deletes the initial input number as it is no longer necessary
		_clearInputNumber();
	}

	/**
	 * Deletes the answer if it has been displayed and deletes everything else
	 * to return the app to just-opened-like state.
	 */
	private void _clearAnswer() {
		if (answer.length() > 0) {
			answer = new StringBuilder();
			v_answer.setVisibility(View.GONE);

			_clearInputNumber();
			_clearOperation();
			_clearSecondInput();
		}
	}

	/**
	 * Deletes the initial inputted number and updates the view to show an empty string.
	 */
	private void _clearInputNumber() {
		if (inputNumber.length() > 0) {
			inputNumber = new StringBuilder();
			v_inputNumber.setText("");
		}
	}

	/**
	 * Deletes the inputted operation and updates the view to show an empty string.
	 */
	private void _clearOperation() {
		if (operation != null) {
			operation = null;
			v_operation.setVisibility(View.GONE);
		}
	}

	/**
	 * Deletes the second inputted number and updates the view to show an empty string.
	 */
	private void _clearSecondInput() {
		if (secondInputNumber.length() > 0) {
			secondInputNumber = new StringBuilder();
			v_secondInputNumber.setText("");
		}
	}

	/**
	 * Restores the text size of the input field views if it had been shrunk
	 */
	private void _restoreTextSize() {
		if (v_inputNumber.getTextSize() != DEFAULT_INPUT_FIELD_TEXT_SIZE) {
			v_inputNumber.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_secondInputNumber.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
			v_operation.setTextSize(DEFAULT_INPUT_FIELD_TEXT_SIZE);
		}
	}

	/**
	 * Shows a toast with the invalid operation message
	 */
	private void _showInvalidOperationMessage() {
		Toast.makeText(this, R.string.invalid_operation_message, Toast.LENGTH_SHORT).show();
	}
}

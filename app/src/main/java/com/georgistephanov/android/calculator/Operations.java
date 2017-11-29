package com.georgistephanov.android.calculator;

import java.math.BigDecimal;

/**
 * Created by Georgi on 28-Nov-17.
 */

public enum Operations implements Operation {
	ADD			((a, b) -> a + b),
	SUBTRACT 	((a, b) -> a - b),
	MULTIPLY	((a, b) -> a * b),
	DIVIDE		((a, b) -> a / b),
	PERCENTAGE  ((a, b) -> (a * b) / 100),
	POWER		((a, b) -> Math.pow(a, b));

	private Operation operation;

	Operations(Operation operation) {
		this.operation = operation;
	}

	public double apply(double a, double b) {
		return operation.apply(a, b);
	}
}

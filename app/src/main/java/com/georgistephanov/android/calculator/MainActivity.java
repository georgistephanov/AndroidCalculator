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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

	private static final String[] calculatorButtons = {
		"C", "()", "%", "/",
		"7", "8", "9", "X",
		"4", "5", "6", "-",
		"1", "2", "3", "+",
		"+/-", "0", ".", "="};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}

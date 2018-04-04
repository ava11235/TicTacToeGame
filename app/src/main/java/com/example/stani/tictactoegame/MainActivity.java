package com.example.stani.tictactoegame;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
        private Button gameGrid[][] = new Button[3][3];
        private Button newGameButton;
        private TextView messageTextView;

        // set up preferences
        private SharedPreferences prefs;

        private int turn;
        private String message;
        private boolean gameOver;
        private String gameString;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // get references to widgets
            gameGrid[0][0] = (Button) findViewById(R.id.square1);
            gameGrid[0][1] = (Button) findViewById(R.id.square2);
            gameGrid[0][2] = (Button) findViewById(R.id.square3);
            gameGrid[1][0] = (Button) findViewById(R.id.square4);
            gameGrid[1][1] = (Button) findViewById(R.id.square5);
            gameGrid[1][2] = (Button) findViewById(R.id.square6);
            gameGrid[2][0] = (Button) findViewById(R.id.square7);
            gameGrid[2][1] = (Button) findViewById(R.id.square8);
            gameGrid[2][2] = (Button) findViewById(R.id.square9);
            newGameButton = (Button) findViewById(R.id.newGameButton);
            messageTextView = (TextView) findViewById(R.id.messageTextView);

            // get default SharedPreferences object
            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            // set event handlers
            for (int x = 0; x < gameGrid.length; x++) {
                for (int y = 0; y < gameGrid[x].length; y++) {
                    gameGrid[x][y].setOnClickListener(this);
                }
            }

            newGameButton.setOnClickListener(this);

            setStartingValues();
        }

        @Override
        public void onPause() {

            // create game string
            gameString = "";
            String square = "";
            for (int x = 0; x < gameGrid.length; x++) {
                for (int y = 0; y < gameGrid[x].length; y++) {
                    square = gameGrid[x][y].getText().toString();
                    if (square.equals("")) {
                        square = " ";  // use a space for an empty square
                    }
                    gameString += square;
                }
            }

            // save the instance variables
            Editor editor = prefs.edit();
            editor.putInt("turn", turn);
            editor.putString("message", message);
            editor.putBoolean("gameOver", gameOver);
            editor.putString("gameString", gameString);
            editor.commit();

            super.onPause();
        }

        @Override
        public void onResume() {
            super.onResume();

            // restore the instance variables
            turn = prefs.getInt("turn", 1);
            gameOver = prefs.getBoolean("gameOver", false);
            message = prefs.getString("message", "Player X's turn");
            gameString = prefs.getString("gameString", "         ");

            // set the message on its widget
            messageTextView.setText(message);

            // use game string to restore squares
            int i = 0;
            for (int x = 0; x < gameGrid.length; x++) {
                for (int y = 0; y < gameGrid[x].length; y++) {
                    String square = gameString.substring(i, i + 1);
                    gameGrid[x][y].setText(square);
                    i++;
                }
            }
        }

        private void setStartingValues() {
            turn = 1;
            gameOver = false;
            message = "Player X's turn";
            messageTextView.setText(message);
            gameString = "         ";   // 9 spaces (one for each square)
        }

        private void clearGrid() {
            for (int x = 0; x < gameGrid.length; x++) {
                for (int y = 0; y < gameGrid[x].length; y++) {
                    gameGrid[x][y].setText(" ");   // use a space for each square
                }
            }
        }

        private void startNewGame() {
            clearGrid();
            setStartingValues();
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.newGameButton:
                    startNewGame();
                    break;
                default:
                    if (!gameOver) {
                        Button b = (Button) v;

                        if (b.getText().equals(" ")) {
                            if (turn % 2 != 0) {
                                b.setText("X");
                                message = "Player O's turn";
                            } else {
                                b.setText("O");
                                message = "Player X's turn";
                            }

                            turn++;
                            checkForGameOver();
                        } else {
                            message = "That square is taken. Try again.";
                        }
                    }
                    messageTextView.setText(message);
            }
        }

        private void checkForGameOver() {
            // Check for a match
            // Rows
            for (int x = 0; x < 3; x++) {
                if (!gameGrid[x][0].getText().equals(" ") &&
                        gameGrid[x][0].getText().equals(gameGrid[x][1].getText()) &&
                        gameGrid[x][1].getText().equals(gameGrid[x][2].getText())
                        ) {
                    message = gameGrid[x][0].getText() + " wins!";
                    gameOver = true;
                    return;
                }
            }

            // Columns
            for (int y = 0; y < 3; y++) {
                if (!gameGrid[0][y].getText().equals(" ") &&
                        gameGrid[0][y].getText().equals(gameGrid[1][y].getText()) &&
                        gameGrid[1][y].getText().equals(gameGrid[2][y].getText())
                        ) {
                    message = gameGrid[0][y].getText() + " wins!";
                    gameOver = true;
                    return;
                }
            }

            // Diagonal 1
            if (!gameGrid[0][0].getText().equals(" ") &&
                    gameGrid[0][0].getText().equals(gameGrid[1][1].getText()) &&
                    gameGrid[1][1].getText().equals(gameGrid[2][2])
                    ) {
                message = gameGrid[0][0].getText() + " wins!";
                gameOver = true;
                return;
            }

            // Diagonal 2
            if (!gameGrid[2][0].getText().equals(" ") &&
                    gameGrid[2][0].getText().equals(gameGrid[1][1].getText()) &&
                    gameGrid[0][2].getText().equals(gameGrid[1][1].getText())
                    ) {
                message = gameGrid[2][0].getText() + " wins!";
                gameOver = true;
                return;
            }

            if (turn > 9) {
                message = "It's a tie!";
                gameOver = true;
                return;
            }

            gameOver = false;
        }

    }

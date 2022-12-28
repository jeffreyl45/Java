import java.util.Random;
import java.util.Scanner;

/**
     * File Name: Lu_HangmanCPT
     * Description: ICS3U CPT- creating a game of Hangman using Java while integrating concepts from various parts of the course
     * Date: July 23 2021
     * Modified: July 26, 2021
     */
    public class Lu_HangmanCPT {
        public static void main(String[] args) {
            // Scanner to read user input SCANNER
            Scanner s = new Scanner(System.in);
            // DEFINING A VARIABLE
            int option = 0;
            // WHILE LOOP
            while (option != 3) {
                // PRINT STATEMENT
                System.out.println("\nMenu: \n1. How to play \n2. Play Hangman \n3. Exit");
                System.out.println("Press 1, 2 or 3 to interact with the menu.");
                option = s.nextInt();
                // SWITCH STATEMENT
                switch (option) {
                    // CASE EXAMPLE
                    case 1:
                        // CALLING A METHOD
                        displayRules();
                        break;
                    case 2:
                        playHangman(s);
                        break;
                    case 3:
                        System.out.println("Exiting program.");
                        break;
                    default:
                        System.out.println("This is not a valid option");
                        break;

                }
            }

        }

        /**
         * Description: prints the rules
         * Precondition: none
         * Postcondition: displays the rules for the user
         */
        //METHOD
        public static void displayRules() {
            System.out.println("Hangman is a word guessing game, the objective is to guess the hidden word/phrase.");
            System.out.println("You have the option of guessing one letter at a time or attempt to guess the whole word/phrase.");
            System.out.println("By choosing to guess the entire hidden word/phrase at any point, you risk losing the game immediately if you guess wrong!");
            System.out.println("Each letter of the hidden word/phrase is represented by a -.");
            System.out.println("Everytime you guess a letter right, it will replace any dashes hiding that letter in the word/phrase.");
            System.out.println("If you guess wrong, a part of the man will be hung!");
            System.out.println("If you have 6 wrong guesses, then the whole man will be hung and you lose the game!");

        }

        /**
         * Description: this method chooses a random word from the wordBank array by generating a random number and using it to retrieve the word
         * with that random number index (the word found is set as the secretWord)
         * Precondition: none
         * Postcondition: a random word will be selected as the secretWord for the game
         */
        // returns the secretWord as a String
        public static String generateSecretWord() {
            // STRING ARRAY EXAMPLE
            String[] wordBank = {
                    "green",
                    "red",
                    "blue",
                    "yellow",
                    "purple",
                    "orange",
                    "ice cream",
                    "coding is fun",
            };
            // GENERATING A RANDOM NUMBER
            Random r = new Random();
            String secretWord = wordBank[r.nextInt(wordBank.length)];
            // RETURN STATEMENT EXAMPLE
            return secretWord;
        }

        /**
         * Description: This method starts and controls the game
         * Precondition: secretWord must be initialized
         * Postcondition: The game ends (6 wrongGuesses or all letters of secretWord are guessed) and the player is either taken to the
         * displayWinMessage if they win or displayLossMessage if they lose
         * @param s - Scanner required to read player guesses
         */
        public static void playHangman(Scanner s) {
            int wrongGuesses = 0;
            boolean gameWon = false;
            // empty before the game starts because no letters have been guessed
            String previouslyGuessedLetters = "";
            System.out.println("Let's play Hangman!");
            String secretWord = generateSecretWord();
            // initialize successfullyGuessedPositions for the start of the game
            boolean[] successfullyGuessedPositions = initializeSuccessfullyGuessedPositions(secretWord);
            // keep playing until the game is won or lost
            while (!isGameLost(wrongGuesses, successfullyGuessedPositions) && !isGameWon(successfullyGuessedPositions)) {
                showHangman(wrongGuesses);
                System.out.println();
                System.out.println("Secret Word:");
                // do a println here to improve spacing
                System.out.println();
                showHiddenSecretWord(successfullyGuessedPositions, secretWord);
                System.out.println();
                // informs the user each round what letters they have already guessed and cannot guess again
                System.out.println("Previously Guessed Letters: " + previouslyGuessedLetters);
                // do a println here to improve spacing
                System.out.println("\nPlease guess a letter (if you want to guess the whole word, type \"!\").");
                String guess = s.next();
                if (guess.equals("!")) {
                    // if the boolean value returned is true, gameWon is true
                    gameWon = handleFullWordGuess(s, secretWord, successfullyGuessedPositions);
                    if (!gameWon) {
                        wrongGuesses = 6;
                    }

                } else if (guess.length() != 1) {
                    // handles the player if they choose to do an invalid input of more than 1 letter
                    System.out.println("Please only enter 1 letter.");
                } else if (previouslyGuessedLetters.contains(guess)) {
                    // ensures that the player does not guess the same letter again
                    System.out.println("You have already guessed this letter.");
                } else if (!Character.isLetter(guess.charAt(0))) {
                    // ensures that the player does not enter any non-letter characters
                    System.out.println("You may only enter a letter.");
                } else {
                    // the guess has been verified to be a valid single letter input
                    // adds the guess to previouslyGuessedLetters so it cannot be guessed again
                    previouslyGuessedLetters = previouslyGuessedLetters + guess;
                    if (!handleSingleLetterGuess(guess, secretWord, successfullyGuessedPositions)) {
                        wrongGuesses = wrongGuesses + 1;
                    } else {
                        gameWon = isGameWon(successfullyGuessedPositions);
                    }
                }
            }
            if (gameWon) {
                printWinMessage(secretWord);
            } else {
                // game is lost, display losing message
                printLossMessage(secretWord);
            }
        }

        /**
         * Description: This method sets successfullyGuessedPositions to false except for any spaces in the secretWord
         * Precondition: successfullyGuessedPositions is an allocated array
         * Postcondition: every element of successfullyGuessedPositions is set to false except spaces
         * @param secretWord - the word/phrase the player is trying to guess
         * @return - returns boolean[] initializedsuccessfullyGuessedPositions
         */
        public static boolean[] initializeSuccessfullyGuessedPositions(String secretWord) {
            boolean[] successfullyGuessedPositions = new boolean[secretWord.length()];
            for (int i = 0; i < successfullyGuessedPositions.length; i++) {
                // this if statement checks every character in the secretWord for a space
                if (secretWord.substring(i, i + 1).equals(" ")) {
                    // spaces are free and shown to the player in Hangman
                    successfullyGuessedPositions[i] = true;
                } else {
                    // apart from spaces, the only other possibility is a letter which is hidden
                    successfullyGuessedPositions[i] = false;
                }
            }
            return successfullyGuessedPositions;

        }

        /**
         * Description: This method prints a dash for every letter in secretWord who's position has not been
         * marked as true in successfullyGuessedPositions
         * Precondition: secretWord and successfullyGuesssedPositions have been initialized, they should have the same length
         * Postcondition: displays the word with non-guessed letters hidden by dashes and guessed letter revealed
         * @param successfullyGuessedPositions - array representing the guessed and unguessed letters by position
         * @param secretWord - the word/phrase the player is trying to guess
         */
        public static void showHiddenSecretWord(boolean[] successfullyGuessedPositions, String secretWord) {
            // FOR NESTED LOOP EXAMPLE
            for (int i = 0; i < successfullyGuessedPositions.length; i++) {
                //IF STATEMENT EXAMPLE
                if (!successfullyGuessedPositions[i]) {
                    // do not want println as that will print the word vertically PRINT FORMAT EXAMPLE
                    System.out.printf("-");
                }
                // when the successfullyGuessedPositions[i] is true, the dash is replaced by the correct guess or a predetermined space
                // ELSE STATEMENT EXAMPLE
                else {
                    // PRINTING A SUBSTRING EXAMPLE
                    System.out.printf(secretWord.substring(i, i + 1));
                }
            }
        }

        /**
         * Description: This method uses a switch case to display the hangman picture corresponding to the number of wrongGuesses
         * Preconditon: wrongGuesses must be an integer between 0 and 6
         * Postcondition: the corresponding hangman picture is displayed
         * @param wrongGuesses - the number of wrong guesses the player has made (6 is the maximum)
         */
        public static void showHangman(int wrongGuesses) {
            switch (wrongGuesses) {
                case 0:
                    System.out.println("_______");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    break;
                case 1:
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("|");
                    break;
                case 2:
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|     |");
                    System.out.println("|");
                    System.out.println("|");
                    break;
                case 3:
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|     |/");
                    System.out.println("|");
                    System.out.println("|");
                    break;
                case 4:
                    // must have a \\ (double backslash) to print "\" as it is an "illegal escape character" in java
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|    \\|/");
                    System.out.println("|");
                    System.out.println("|");
                    break;
                case 5:
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|    \\|/");
                    System.out.println("|      \\");
                    System.out.println("|");
                    break;
                case 6:
                    System.out.println("_______");
                    System.out.println("|     0");
                    System.out.println("|    \\|/");
                    System.out.println("|    / \\");
                    System.out.println("|");
                    break;
            }
        }

        /**
         * Description: This method will handle outcomes of the user choosing to attempt to guess the full word
         * Precondition: secretWord, successfullyGuessedPositions should be initialized
         * Postcondition: the user either wins(if they guess the word) or loses (if they do not guess the word)
         * @param s - Scanner to collect user's guess
         * @param successfullyGuessedPositions - array representing the guessed and unguessed letters by position
         */
        public static boolean handleFullWordGuess(Scanner s, String secretWord, boolean[] successfullyGuessedPositions) {
            System.out.println("Please guess the full word.");
            // s.nextLine() prevents the enter from becoming the guess after the player types "!"
            s.nextLine();
            String fullWordGuess = s.nextLine();
            if (fullWordGuess.equalsIgnoreCase(secretWord)) {
                // set all the successfully guessed positions to true (this is a winning condition)
                for (int i = 0; i < successfullyGuessedPositions.length; i++) {
                    successfullyGuessedPositions[i] = true;
                }
                return true;
            } else {
                return false;
            }

        }

        /**
         * Description: Prints the win message
         * Precondition: secretWord should be set, player has won the game
         * Postcondition: Informs the user they won and what the secretWord was
         * @param secretWord - the word/phrase the player is trying to guess
         */
        public static void printWinMessage(String secretWord) {
            System.out.println("You won!");
            System.out.println("The secret word was: " + secretWord);
        }

        /**
         * Description: Prints the loss message
         * Precondition: secretWord should be set, player has lost the game
         * Postcondition: Informs the user they lost and what the secretWord was. Also prints the full hangman (head, body, legs, arms)
         * @param secretWord - the word/phrase the player is trying to guess
         */
        public static void printLossMessage(String secretWord) {
            showHangman(6);
            System.out.println("You lost!");
            System.out.println("The secret word was: " + secretWord);
        }

        /**
         * Description: This method will check to see if the user's single letter guess matches any letters of secretWord
         * Informs the user if their letter was in the word or not
         * Precondition: secretWord and successfullyGuessedPositions should be initialized
         * Postcondition: adds one wrongGuesses if the user's guess does not match any letters of secretWord, marks the positions of
         * any of the same letters as the user's guess in successfullyGuessedPositions as true
         * @param guess                        - the user's input (one letter guess)
         * @param secretWord                   - the word/phrase the player is trying to guess
         * @param successfullyGuessedPositions - array representing the guessed and unguessed letters by position
         * @return - boolean true if at least one letter matched the guess and boolean false otherwise
         */
        public static boolean handleSingleLetterGuess(String guess, String secretWord, boolean[] successfullyGuessedPositions) {
            boolean atLeastOneLetterMatched = false;
            for (int i = 0; i < secretWord.length(); i++) {
                if (guess.equalsIgnoreCase(secretWord.substring(i, i + 1))) {
                    successfullyGuessedPositions[i] = true;
                    atLeastOneLetterMatched = true;
                }
            }
            if (atLeastOneLetterMatched) {
                System.out.println("This letter was in the word!");
            } else {
                System.out.println("This letter is not in the word");
            }
            return atLeastOneLetterMatched;


        }

        /**
         * Description: this method checks if the game has been won by verifying every element of the successfullyGuessedPositions
         * array is true
         * Precondition: successfullyGuessedPositions is initialized
         * Postcondition: user wins (returns true) if all successfullyGuessedPositions are true, user continues to next turn
         * if any successfullyGussedPositions is false
         * @param successfullyGuessedPositions - array representing the guessed and unguessed letters by position
         * @return - false if any boolean[] successfullyGuessedPositions is false and true otherwise
         */
        public static boolean isGameWon(boolean[] successfullyGuessedPositions) {
            for (int i = 0; i < successfullyGuessedPositions.length; i++) {
                if (!successfullyGuessedPositions[i]) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Description: this method checks if the game has been lost
         * Precondition: successfullyGuessedPositions is initialized
         * Postcondition: player loses if wrongGuesses is equal to 6, continues to the next turn otherwise
         *
         * @param wrongGuesses                 - number of wrong guesses made by the user
         * @param successfullyGuessedPositions - array representing the guessed and unguessed letters by position
         * @return - true if the player has 6 wrong guesses, false otherwise
         */
        public static boolean isGameLost(int wrongGuesses, boolean[] successfullyGuessedPositions) {
            if (wrongGuesses == 6) {
                return true;
            }
            return false;
        }
    }






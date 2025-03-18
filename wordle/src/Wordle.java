import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Wordle {
    public static final String BOLD = "\u001B[1m";
    public static final String GRAY_BG = "\u001B[100m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        Set<String> allowedGuesses = new HashSet<>();
        Set<String> answers = new HashSet<>();

        try {
            allowedGuesses.addAll(Files.readAllLines(Paths.get("data/wordle-allowed-guesses.txt")));
            answers.addAll(Files.readAllLines(Paths.get("data/wordle-answers-alphabetical.txt")));
        } catch(IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        List<String> answerList = new ArrayList<>(answers);
        Random rand = new Random();
        String answer = answerList.get(rand.nextInt(answerList.size()));

        System.out.println(BOLD + "Wordle" + RESET+ "\nGuess a 5-letter word.");
        Scanner sc = new Scanner(System.in);
        int guesses = 6;

        while(guesses > 0) {
            System.out.print("> ");
            String guess = sc.nextLine().toLowerCase();

            if (guess.length() != 5) {
                System.out.println("Guess a 5-letter word.");
            } else if (!(allowedGuesses.contains(guess) || answers.contains(guess))) {
                System.out.println("Not in word list.");
            } else {
                --guesses;

                if (guess.equals(answer)) {
                    for (char ch : guess.toCharArray()) {
                        System.out.print(GREEN_BG + " " + Character.toUpperCase(ch) + " " + RESET + " ");
                    }

                    System.out.println("\nAnswer: " + BOLD + answer.toUpperCase() + RESET + "\nCongratulations!");
                    sc.close();
                    return;
                } else {
                    char[] clue = new char[5];
                    Arrays.fill(clue, '-');
                    boolean[] answerFlags = new boolean[5];

                    for (int i = 0; i < guess.length(); i++) {
                        if (guess.charAt(i) == answer.charAt(i)) {
                            clue[i] = 'G';
                            answerFlags[i] = true;
                        }
                    }

                    for (int i = 0; i < guess.length(); i++) {
                        if (clue[i] == '-') {
                            for (int j = 0; j < answer.length(); j++) {
                                if (guess.charAt(i) == answer.charAt(j) && !answerFlags[j]) {
                                    clue[i] = 'Y';
                                    answerFlags[j] = true;
                                    break;
                                }
                            }
                        }
                    }
                    
                    for (int i = 0; i < clue.length; i++) {
                        char currChar = guess.charAt(i);
                        if (clue[i] == 'G') {
                            System.out.print(GREEN_BG + " " + Character.toUpperCase(currChar) + " " + RESET + " ");
                        } else if (clue[i] == 'Y') {
                            System.out.print(YELLOW_BG + " " + Character.toUpperCase(currChar) + " " + RESET + " ");
                        } else {
                            System.out.print(GRAY_BG + " " + Character.toUpperCase(currChar) + " " + RESET + " ");
                        }
                    }

                    System.out.println();
                }
            }
        }

        System.out.println("Answer: " + BOLD + answer.toUpperCase() + RESET + "\nBetter luck next time!");
        sc.close();
    }
}

package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int SIZE = 9;
    private static final char UNEXPLORED = '.';
    private static final char MARKED = '*';
    private static final char FREE = '/';
    private static boolean[][] hasMine;
    private static char[][] minefield;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("How many mines do you want on the field?");
        int noOfMines = sc.nextInt();
        sc.nextLine();

        // Initialize minefield and mine tracking
        hasMine = new boolean[SIZE][SIZE];
        minefield = new char[SIZE][SIZE];
        initializeMinefield(noOfMines);
        boolean firstMove = true;

        while (true) {
            printMinefield();
            System.out.println("Set/unset mine marks or claim a cell as free:");
            String[] input = sc.nextLine().split(" ");
            int column = Integer.parseInt(input[0]) - 1;
            int row = Integer.parseInt(input[1]) - 1;
            String command = input[2];

            if (command.equals("mine")) {
                toggleMark(row, column);
            } else if (command.equals("free")) {
                if (firstMove) {
                    ensureFirstMoveSafe(row, column);
                    firstMove = false;
                }
                if (!exploreCell(row, column)) {
                    printMinefieldWithMines();
                    System.out.println("You stepped on a mine and failed!");
                    break;
                }
            }

            if (checkWinCondition(noOfMines)) {
                System.out.println("Congratulations! You found all the mines!");
                break;
            }
        }
    }

    private static void initializeMinefield(int mineToAdd) {
        // Initialize all cells as unexplored
        for (char[] row : minefield) Arrays.fill(row, UNEXPLORED);
        Random r = new Random();
        while (mineToAdd > 0) {
            int randRow = r.nextInt(SIZE);
            int randCol = r.nextInt(SIZE);
            if (!hasMine[randRow][randCol]) {
                hasMine[randRow][randCol] = true;
                mineToAdd--;
            }
        }
    }

    private static void ensureFirstMoveSafe(int row, int col) {
        if (hasMine[row][col]) {
            // Move the mine to a new location
            hasMine[row][col] = false;
            Random r = new Random();
            while (true) {
                int newRow = r.nextInt(SIZE);
                int newCol = r.nextInt(SIZE);
                if (newRow != row || newCol != col) {
                    if (!hasMine[newRow][newCol]) {
                        hasMine[newRow][newCol] = true;
                        break;
                    }
                }
            }
        }
    }

    private static void toggleMark(int row, int col) {
        if (minefield[row][col] == UNEXPLORED) {
            minefield[row][col] = MARKED;
        } else if (minefield[row][col] == MARKED) {
            minefield[row][col] = UNEXPLORED;
        }
    }

    private static boolean exploreCell(int row, int col) {
        if (hasMine[row][col]) return false;
        if (minefield[row][col] == UNEXPLORED || minefield[row][col] == MARKED) {
            int adjacentMines = countAdjacentMines(row, col);
            if (adjacentMines == 0) {
                minefield[row][col] = FREE;
                // Explore adjacent cells
                for (int i = Math.max(0, row - 1); i <= Math.min(SIZE - 1, row + 1); i++) {
                    for (int j = Math.max(0, col - 1); j <= Math.min(SIZE - 1, col + 1); j++) {
                        if (minefield[i][j] == UNEXPLORED || minefield[i][j] == MARKED) {
                            exploreCell(i, j);
                        }
                    }
                }
            } else {
                minefield[row][col] = Character.forDigit(adjacentMines, 10);
            }
        }
        return true;
    }

    private static int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(SIZE - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(SIZE - 1, col + 1); j++) {
                if (hasMine[i][j]) count++;
            }
        }
        return count;
    }

    private static boolean checkWinCondition(int noOfMines) {
        int correctMarks = 0;
        int exploredCells = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (minefield[i][j] == MARKED && hasMine[i][j]) correctMarks++;
                if (minefield[i][j] != UNEXPLORED && minefield[i][j] != MARKED) exploredCells++;
            }
        }
        return correctMarks == noOfMines && exploredCells == (SIZE * SIZE - noOfMines);
    }

    private static void printMinefield() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(minefield[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    private static void printMinefieldWithMines() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(hasMine[i][j] ? 'X' : minefield[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }
}
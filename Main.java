package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("How many mines do you want on the field?");
        int noOfMines = sc.nextInt();
        sc.nextLine();
        int countMines = noOfMines;
        int x = 9;
        int y = 9;
        char[][] mineSweep;

            mineSweep = intializeMine(x, y, noOfMines);
            mineSweep = getNumberedSweep(mineSweep);
            printlnMine(mineSweep);
        do {
            System.out.println("Set/delete mines marks (x and y coordinates):");
            String[] coord = sc.nextLine().split(" ");
            int j = Integer.parseInt(coord[0]) - 1;
            int i = Integer.parseInt(coord[1]) - 1;
            if(mineSweep[i][j] == '.') {
                mineSweep[i][j] = '*';
                printlnMine(mineSweep);
            } else if(mineSweep[i][j] == '*') {
                mineSweep[i][j] = '.';
                printlnMine(mineSweep);
            } else if (mineSweep[i][j] == 'X') {
                mineSweep[i][j] = '*';
                countMines--;
                printlnMine(mineSweep);
            }else {
                System.out.println("There is a number here!");
            }

        } while(countMines > 0);
        System.out.println("Congratulations! You found all mines!");
    }

    private static char[][] getNumberedSweep(char[][] mineSweep) {
        for(int i = 0; i < mineSweep.length; i++){
            for(int j = 0; j < mineSweep[i].length; j++) {
                if(mineSweep[i][j] == '.') {
                    int mineCount = getSurrondingMines(i, j, mineSweep);
                    if (mineCount > 0) {
                        mineSweep[i][j] = Character.forDigit(mineCount, 10);
                    }
                }
            }
        }
        return mineSweep;
    }

    private static void printlnMine(char[][] mineSweep) {
        System.out.println(" |123456789|\n" +
                "-|---------|");
        for(int i = 0; i < mineSweep.length; i++){
            System.out.print(i + 1 + "|");
            for(int j = 0; j < mineSweep[i].length; j++) {
                if(mineSweep[i][j] == 'X') {
                    System.out.print(".");
                } else {
                    System.out.print(mineSweep[i][j]);
                }

            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }

    private static char[][] intializeMine(int x, int y, int mineToAdd) {
        char[][] init = new char[x][y];
        Random r = new Random();
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++) {
                int rand = r.nextInt(10);
                if(mineToAdd > 0 && rand < 7){
                    init[i][j] = 'X';
                    mineToAdd--;
                } else {
                    init[i][j] = '.';
                }
            }
        }
        return init;
    }

    private static int getSurrondingMines(int i, int j, char[][] init) {
        int count = 0;
        if((i == 0 && j == 0)) {
            if(init[i + 1][j + 1] == 'X') {
                count++;
            }
            if(init[i][j + 1] == 'X') {
                count++;
            }
            if(init[i + 1][j] == 'X') {
                count++;
            }
            return count;
        } else if (i == init.length - 1 && j == 0) {
            if(init[i - 1][j + 1] == 'X') {
                count++;
            }
            if(init[i - 1][j] == 'X') {
                count++;
            }
            if(init[i][j + 1] == 'X') {
                count++;
            }
            return count;
        } else if (i == 0 && j == init[i].length - 1) {
            if(init[i][j - 1] == 'X') {
                count++;
            }
            if(init[i + 1][j - 1] == 'X') {
                count++;
            }
            if(init[i + 1][j] == 'X') {
                count++;
            }
            return count;
        } else if (i == init.length - 1 && j == init[i].length - 1) {
            if(init[i - 1][j - 1] == 'X') {
                count++;
            }
            if(init[i][j - 1] == 'X') {
                count++;
            }
            if(init[i - 1][j] == 'X') {
                count++;
            }
            return count;
        }
        if (i == 0) {
            count = getCountBelow(i, j, init, count) + getCountsides(i, j, init, count);
        } else if(j == 0) {
            count = getCountUpDown(i, j, init, count) + getCountRight(i, j, init, count);
        } else if(i == init.length - 1) {
            count = getCountAbove(i, j, init, count) + getCountsides(i, j, init, count);
        } else if (j == init.length - 1) {
            count = getCountUpDown(i, j, init, count) + getCountLeft(i, j, init, count);
        } else {
            count = getCountUpDown(i, j, init, count) + getCountLeft(i, j, init, count) + getCountRight(i, j, init, count);
        }
        return count;
    }

    private static int getCountRight(int i, int j, char[][] init, int count) {
        if(init[i + 1][j + 1] == 'X') {
            count++;
        }
        if(init[i][j + 1] == 'X') {
            count++;
        }
        if(init[i - 1][j + 1] == 'X') {
            count++;
        }
        return count;
    }

    private static int getCountLeft(int i, int j, char[][] init, int count) {
        if(init[i - 1][j - 1] == 'X') {
            count++;
        }
        if(init[i][j - 1] == 'X') {
            count++;
        }
        if(init[i + 1][j - 1] == 'X') {
            count++;
        }
        return count;
    }

    private static int getCountUpDown(int i, int j, char[][] init, int count) {
        if(init[i + 1][j] == 'X') {
            count++;
        }
        if(init[i - 1][j] == 'X') {
            count++;
        }
        return count;
    }

    private static int getCountsides(int i, int j, char[][] init, int count) {
        if(init[i][j + 1] == 'X') {
            count++;
        }
        if(init[i][j - 1] == 'X') {
            count++;
        }
        return count;
    }

    private static int getCountBelow(int i, int j, char[][] init, int count) {
        if(init[i + 1][j + 1] == 'X') {
            count++;
        }
        if(init[i + 1][j - 1] == 'X') {
            count++;
        }
        if(init[i + 1][j] == 'X') {
            count++;
        }
        return count;
    }

    private static int getCountAbove(int i, int j, char[][] init, int count) {
        if(init[i - 1][j + 1] == 'X') {
            count++;
        }
        if(init[i - 1][j - 1] == 'X') {
            count++;
        }
        if(init[i - 1][j] == 'X') {
            count++;
        }
        return count;
    }


}



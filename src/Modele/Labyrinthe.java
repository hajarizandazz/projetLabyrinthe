package Modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Labyrinthe {
    public static final int PATH = 0;
    public static final int WALL = 1;
    public static final int MECHANT = 2;

    private final int rows;
    private final int columns;
    private final int[][] grid;
    private final List<Mechants.Position> mechantsPositions;
    private final Random random;
    private int exitRow;
    private int exitCol;

    public Labyrinthe(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new int[rows][columns];
        this.mechantsPositions = new ArrayList<>();
        this.random = new Random();
        generateLabyrinthe();
    }

    private void generateLabyrinthe() {
        // j'ai inisialiser toute les colonne comme des murs

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = WALL;
            }
        }

        // ici j'ai commencer à faire générer la labyrinthe à partir du coin supérieur gauche en utilisant une approche du parcours en profondeur (grid[1][1])

        grid[1][1] = PATH;
        Stack<Cell> stack = new Stack<>(); // j'ai utiliser une pile pour gerer les cellules a parcourir
        stack.push(new Cell(1, 1));

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            List<Cell> neighbors = getNeighbors(current);

            if (!neighbors.isEmpty()) {
                stack.push(current);
                Collections.shuffle(neighbors);//parmi les voisins disponibles il vas devenir la nouvelle partie de chemin
                Cell next = neighbors.get(0);
                grid[next.row][next.col] = PATH;
                grid[(current.row + next.row) / 2][(current.col + next.col) / 2] = PATH;
                stack.push(next);
            }
        }

        // ici j'ai mis la sortie et l'entree du labyrinthe

        grid[0][1] = PATH; // Entree
        exitRow = rows - 1;
        exitCol = columns - 2;
        grid[exitRow][exitCol] = PATH; // Sortie
    }

    private void placeMechants(int nombreMechants) {
        mechantsPositions.clear();
        while (mechantsPositions.size() < nombreMechants) {
            int row = random.nextInt(rows);
            int col = random.nextInt(columns);
            if (grid[row][col] == PATH && (row != 1 || col != 1) && (row != rows - 2 || col != columns - 2)) {
                mechantsPositions.add(new Mechants.Position(row, col, "mechant"));
                grid[row][col] = MECHANT;
            }
        }
    }

    public List<Mechants.Position> getMechants(int nombreMechants) {
        placeMechants(nombreMechants);
        return mechantsPositions;
    }

    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();

        if (cell.row - 2 > 0 && grid[cell.row - 2][cell.col] == WALL) {
            neighbors.add(new Cell(cell.row - 2, cell.col));
        }
        if (cell.row + 2 < rows - 1 && grid[cell.row + 2][cell.col] == WALL) {
            neighbors.add(new Cell(cell.row + 2, cell.col));
        }
        if (cell.col - 2 > 0 && grid[cell.row][cell.col - 2] == WALL) {
            neighbors.add(new Cell(cell.row, cell.col - 2));
        }
        if (cell.col + 2 < columns - 1 && grid[cell.row][cell.col + 2] == WALL) {
            neighbors.add(new Cell(cell.row, cell.col + 2));
        }

        return neighbors;
    }

    public int[][] getGrid() {
        return grid;
    }

    public List<Mechants.Position> getMechants() {
        return mechantsPositions;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getExitCol() {
        return exitCol;
    }

    private static class Cell {
        int row, col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}

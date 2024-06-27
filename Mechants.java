package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mechants {
    private final List<Position> positions;
    private final Random random;

    public Mechants() {
        this.positions = new ArrayList<>();
        this.random = new Random();
    }

    public void addMechant(int row, int col, String type) {
        positions.add(new Position(row, col, type));
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void moveMechants(int[][] grid) {
        for (Position pos : positions) {
            int oldRow = pos.getRow();
            int oldCol = pos.getCol();
            int newRow = oldRow;
            int newCol = oldCol;
            switch (random.nextInt(4)) {
                case 0: newRow--; break; // Haut
                case 1: newRow++; break; // Bas
                case 2: newCol--; break; // Gauche
                case 3: newCol++; break; // Droite
            }

            // Vérifier si le déplacement est valide (pas de mur)
            if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length && grid[newRow][newCol] == Labyrinthe.PATH) {
                // Réinitialiser la cellule précédente
                grid[oldRow][oldCol] = Labyrinthe.PATH;

                // Mettre à jour la position du méchant
                pos.setRow(newRow);
                pos.setCol(newCol);

                // Mettre à jour la nouvelle cellule
                grid[newRow][newCol] = Labyrinthe.MECHANT;
            }
        }
    }

    public static class Position {
        private int row;
        private int col;
        private final String type;

        public Position(int row, int col, String type) {
            this.row = row;
            this.col = col;
            this.type = type;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public String getType() {
            return type;
        }
    }
}

package Modele;

public class Player {
    private String name;
    private int score;
    private int lives;
    private int row;
    private int col;

    public Player(String name, int initialLives) {
        this.name = name;
        this.score = 0;  // Initial score
        this.lives = initialLives;
        this.row = 0;  // Initial row position
        this.col = 1;  // Initial column position
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        this.lives--;
    }

    public void gainLife() {
        this.lives++;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public void setLives(int i) {
        this.lives = i;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

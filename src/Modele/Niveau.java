package Modele;

public class Niveau {
    private int largeur;
    private int hauteur;
    private int nombreMechants;
    private double vitesseMechants;
    private int tempsMinuteur;

    public Niveau(int largeur, int hauteur, int nombreMechants, double vitesseMechants, int tempsMinuteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.nombreMechants = nombreMechants;
        this.vitesseMechants = vitesseMechants;
        this.tempsMinuteur = tempsMinuteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getNombreMechants() {
        return nombreMechants;
    }

    public double getVitesseMechants() {
        return vitesseMechants;
    }

    public int getTempsMinuteur() {
        return tempsMinuteur;
    }
}

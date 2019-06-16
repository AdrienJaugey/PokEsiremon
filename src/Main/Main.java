/*
 * Copyright (C) 2019 AdrienJaugey.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package Main;

import PokEsiremon.Capacite.Capacite;
import PokEsiremon.Combat.Combat;
import PokEsiremon.Combat.Dresseur;
import PokEsiremon.Combat.Dresseur_Ordi;
import static PokEsiremon.Combat.Enum_Action.ATTAQUER;
import static PokEsiremon.Combat.Enum_Action.SWITCHER;
import static PokEsiremon.Pokemon.Enum_Statut.NEUTRE;
import PokEsiremon.Pokemon.Pokedex;
import PokEsiremon.Pokemon.Pokemon;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author AdrienJaugey
 */
public class Main {
    public static void main(String[] args) throws Exception {
        try {
            //On instancie le pokédex (pour tout charger)
            Pokedex pkdx = Pokedex.get();
            Scanner scan = new Scanner(System.in);
            //On crée un combat et on y ajoute un joueur ordi à la place de l'humain (pour faire une simulation de combat)
            Combat combat = new Combat();
            Dresseur joueur = new Dresseur_Ordi();
            clear();
            int choixJeu = menu("Voulez-vous jouer ou générer un joueur controllé par l'ordinateur ?", 20, "Jouer", "Jouer avec une équipe aléatoire", "Générer joueur Ordi");
            if(choixJeu == 1){
                /**********************
                *   Choix du pseudo   *
                **********************/
                String nom = "";
                do{
                    clear();
                    System.out.println("Comment voulez-vous vous appeler ?");
                    nom = scan.nextLine();
                } while(menu("Confirmez-vous vouloir vous appeler " + nom + " ?", 10, "Non", "Oui") != 2);
                joueur = new Dresseur(nom);
                
                /*************************
                *   Choix des pokémons   *
                *************************/
                String pokemons[] = pkdx.getListePokemon();
                do{
                    clear();
                    int emplacement = joueur.getNbPokemonRestant();
                    int idPokemon = menu("Quel pokémon voulez vous en position " + emplacement + " ?", 32, pokemons);
                    if(menu("Confirmez-vous prendre " + pkdx.getNomPkmn(idPokemon) + " ?", 10, "Non", "Oui") == 1) continue;
                    joueur.setPokemon(idPokemon, emplacement);
                } while(joueur.getNbPokemonRestant() < 6);
                
                /**************************
                *   Choix des capacités   *
                **************************/
                Pokemon eq[] = joueur.getEquipe();
                for(int pokemon = 0; pokemon < 6; pokemon++){
                    ArrayList<Integer> capaPossibles = pkdx.getCapaciteId(eq[pokemon].getId(), "");
                    ArrayList<String>  capaPossiblesDesc = pkdx.getListeCapacite(eq[pokemon]);
                    for(int capacite = 0; capacite < 4; capacite++){
                        boolean sortie = false;
                        do{
                            clear();
                            String question = "Choisissez la capacite en position " + capacite + " pour " + eq[pokemon].getNom() + " :";
                            if(capacite != 0){
                                String ajout = "Vous avez déjà choisi :";
                                for(int i = 0; i < capacite; i++) ajout += " " + eq[pokemon].getCapacite(i).getNom();
                                question = ajout + "\n" + question;
                            }
                            int choixCapa = menu(question, 35, convertToStringArray(capaPossiblesDesc)) - 1;
                            String nomCapa = pkdx.getCapacite(capaPossibles.get(choixCapa)).getNom();
                            if(menu("Confirmez-vous prendre " + nomCapa + " ?", 10, "Non", "Oui") == 2){
                                int idCapa = capaPossibles.get(choixCapa);
                                capaPossiblesDesc.remove(choixCapa);
                                capaPossibles.remove(Integer.valueOf(idCapa));
                                joueur.setCapacitePokemon(pokemon, idCapa, capacite);
                                sortie = true;
                            }
                        } while(!sortie);
                    }
                }
                
            } else if(choixJeu == 2){
                String nom = "";
                do{
                    clear();
                    System.out.println("Comment voulez-vous vous appeler ?");
                    nom = scan.nextLine();
                } while(menu("Confirmez-vous vouloir vous appeler " + nom + " ?", 10, "Non", "Oui") != 2);
                joueur = new Dresseur(nom);
                joueur.genererEquipeAlea();
            } else {
                ((Dresseur_Ordi)joueur).setAdversaire(combat.getDresseur(1));
            }
            
            
            //On ajoute le joueur
            combat.ajouterJoueur1(joueur);
            
            //On lance le combat si c'est possible
            if(combat.canStart()){ 
                String debut = combat.lancerCombat();
                boolean first = true;
                int i = 0; 
                while(!combat.isDone()){
                    clear();
                    System.out.println(normaliserAffichage(" Tour " + ++i + " ", 64, '#', true));
                    System.out.println(Pokemon.affichageCombat(combat.getDresseur(0), combat.getDresseur(1)));
                    if(first){
                        first = false;
                        System.out.println(debut);
                    }
                    
                    //Le joueur choisi quoi faire 
                    if(choixJeu != 3 && !joueur.getPokemonActuel().attaqueEnCours()){
                        demanderAction(joueur);
                    }
                    
                    System.out.println("### Recap ###");
                    System.out.print(combat.debutNouveauTour());
                    System.out.println(combat.actionPremierJoueur());
                    
                    //Si un pokémon est KO et que le combat n'est pas terminé
                    if(combat.pokemonKO() && !combat.isDone()) {
                        if(choixJeu != 3 && joueur.getPokemonActuel().isKO()){
                            demanderChangement(joueur);
                        }
                        System.out.println(combat.changerKO());
                    } else if(!combat.isDone()){
                        System.out.print(combat.actionSecondJoueur());
                        if(combat.pokemonKO() && !combat.isDone()) {
                            if(choixJeu != 3 && joueur.getPokemonActuel().isKO() && joueur.switchPossible()){
                                demanderChangement(joueur);
                            }
                            System.out.print(combat.changerKO());

                        }
                    }
                    System.out.print(combat.terminerTour());
                    if(combat.pokemonKO() && !combat.isDone()) {
                        if(choixJeu != 3 && joueur.getPokemonActuel().isKO()){
                            demanderChangement(joueur);
                        }
                        System.out.println(combat.changerKO());
                    }
                    System.out.println("");
                    System.out.println("Appuyez sur Entrée pour continuer...");
                    System.out.println("Appuyez sur Ctrl-C sous Windows pour quitter...");
                    scan.nextLine();
                }
            } else {
                System.err.println("Il y a un problème, le combat n'a pas commencé.");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private static void demanderAction(Dresseur joueur) throws Exception{
        ArrayList<String> actions = new ArrayList<>();
        ArrayList<Integer> switchs = joueur.switchPossibles();
        if(joueur.switchPossible()){
            for(Integer pkmnSwitch : switchs){
                Pokemon pkmn = joueur.getEquipe()[pkmnSwitch];
                actions.add("Echanger avec " + pkmn.getNom() + " " + pkmn.getVie() + "/" + pkmn.getVieMax() + " PV"
                           + (pkmn.getStatut() == NEUTRE?"":" " + pkmn.getStatut().toString()));
            }
        }
        Pokemon actuel = joueur.getPokemonActuel();
        for(int capa = 0; capa < 4; capa++){
            Capacite capaActuelle = actuel.getCapacite(capa);
            actions.add("Lancer " + capaActuelle.getNom() + " " + actuel.getPPCapacite(capa) + " PP"
                       + (actuel.capaciteBloquee(capa)?" Capacité Bloquée":""));
        }
        int choixAction = menu("Que voulez-vous faire ?", 50, convertToStringArray(actions));
        if(choixAction <= switchs.size()){
            joueur.setAction(SWITCHER);
            joueur.setInfoAction(switchs.get(choixAction - 1));
        } else {
            joueur.setAction(ATTAQUER);
            joueur.setInfoAction(choixAction - switchs.size() - 1);
        }
    }
    
    private static void demanderChangement(Dresseur joueur){
        ArrayList<String> actions = new ArrayList<>();
        ArrayList<Integer> switchs = joueur.switchPossibles();
        if(joueur.switchPossible()){
            for(Integer pkmnSwitch : switchs){
                Pokemon pkmn = joueur.getEquipe()[pkmnSwitch];
                actions.add("Envoyer " + pkmn.getNom() + " " + pkmn.getVie() + "/" + pkmn.getVieMax() + " PV"
                           + (pkmn.getStatut() == NEUTRE?"":" " + pkmn.getStatut().toString()));
            }
        }
        joueur.setAction(SWITCHER);
        if(switchs.size() == 1) {
            joueur.setInfoAction(switchs.get(0));
        } else {
            int choixAction = menu("Quel pokémon voulez-vous envoyer ?", 50, convertToStringArray(actions));
            joueur.setInfoAction(switchs.get(choixAction - 1));
        }
        
    }
    
    private static String normaliserAffichage(String aAfficher, int tailleVoulue, char filler, boolean centrer){
        int taille = tailleVoulue - aAfficher.length(), ajoutGauche, ajoutDroite;
        if(centrer){
            ajoutGauche = taille/2;
            ajoutDroite = taille - ajoutGauche;
        } else {
            ajoutDroite = taille;
            ajoutGauche = 0;
        }
        for(int i = 0; i < ajoutGauche; i++) aAfficher = filler + aAfficher;
        for(int i = 0; i < ajoutDroite; i++) aAfficher += filler;
        return aAfficher;
    }
    
    private static int menu(String question, int taille, String... choix){
        int reponse = -1;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println(question);
            int pas = taille < 36 ? 3 : 2;
            for(int i = 0; i < choix.length; i+=pas){
                String ligne = normaliserAffichage(i+1 + " - " + choix[i], taille, ' ', false);
                if(i+1 < choix.length) ligne += " | " + normaliserAffichage(i+2 + " - " + choix[i+1], taille, ' ', false);
                if(taille < 36 && i+2 < choix.length) ligne += " | " + normaliserAffichage(i+3 + " - " + choix[i+2], taille, ' ', false);
                System.out.println(ligne);
            }
            reponse = sc.nextInt();
        } while(reponse < 1 || reponse > choix.length);
        return reponse;
    }
    
    private static String[] convertToStringArray(ArrayList<String> liste){
        String[] res = new String[liste.size()];
        for(int i = 0; i < liste.size(); i++){
            res[i] = liste.get(i);
        }
        return res;
    }
    
    public static void clear(){
        try{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            //System.out.println("Clear");
        } catch(Exception error){
            System.err.println(error.getLocalizedMessage());
        }
    }
}

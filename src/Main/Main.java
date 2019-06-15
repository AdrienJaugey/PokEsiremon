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

import Combat.Combat;
import Combat.Dresseur_Ordi;
import Pokemon.Pokedex;
import java.util.Scanner;

/**
 *
 * @author AdrienJaugey
 */
public class Main {
    public static void main(String[] args) {
        try {
            clear();
            Pokedex pkdx = Pokedex.get();
            System.out.println("coucou");
            Combat combat = new Combat();
            Dresseur_Ordi joueur = new Dresseur_Ordi();
            combat.ajouterHumain(joueur);
            joueur.setAdversaire(combat.getDresseur(1));
            /*joueur.setPokemon(25, 0);
            joueur.setCapacitePokemon(0, 51, 0);
            joueur.setCapacitePokemon(0, 51, 1);
            joueur.setCapacitePokemon(0, 51, 2);
            joueur.setCapacitePokemon(0, 51, 3);
            
            joueur.setPokemon(144, 1);
            joueur.setCapacitePokemon(1, 51, 0);
            joueur.setCapacitePokemon(1, 51, 1);
            joueur.setCapacitePokemon(1, 51, 2);
            joueur.setCapacitePokemon(1, 51, 3);
            
            joueur.setPokemon(145, 2);
            joueur.setCapacitePokemon(2, 51, 0);
            joueur.setCapacitePokemon(2, 51, 1);
            joueur.setCapacitePokemon(2, 51, 2);
            joueur.setCapacitePokemon(2, 51, 3);
            
            joueur.setPokemon(146, 3);
            joueur.setCapacitePokemon(3, 51, 0);
            joueur.setCapacitePokemon(3, 51, 1);
            joueur.setCapacitePokemon(3, 51, 2);
            joueur.setCapacitePokemon(3, 51, 3);
            
            joueur.setPokemon(150, 4);
            joueur.setCapacitePokemon(4, 51, 0);
            joueur.setCapacitePokemon(4, 51, 1);
            joueur.setCapacitePokemon(4, 51, 2);
            joueur.setCapacitePokemon(4, 51, 3);
            
            joueur.setPokemon(151, 5);
            joueur.setCapacitePokemon(5, 51, 0);
            joueur.setCapacitePokemon(5, 51, 1);
            joueur.setCapacitePokemon(5, 51, 2);
            joueur.setCapacitePokemon(5, 51, 3);*/
            
            /*for(int i : pkdx.getCapacitePokemon(20)){
                System.out.println(pkdx.getCapacite(i).getNom());
            }*/
            
            
            
            if(true){
                Scanner scan = new Scanner(System.in);
                clear();
                System.out.println(combat.lancerCombat());
                boolean first = true;
                int i = 0; 
                while(!combat.isDone()){
                    if(first) first = false; else clear();
                    System.out.println("##################### TOUR " + ++i + " ###################");
                    System.out.println("### JOUEUR 1 " + combat.getDresseur(0).getNbPokemonRestant() + "/6 ###");
                    System.out.println(combat.getDresseur(0).getPokemonActuel());
                    System.out.println("");
                    System.out.println("### JOUEUR 2 " + combat.getDresseur(1).getNbPokemonRestant() + "/6 ###");
                    System.out.println(combat.getDresseur(1).getPokemonActuel());
                    System.out.println("");
                    System.out.println("### Recap ###");
                    ((Dresseur_Ordi)combat.getDresseur(0)).choixActionSuivante();
                    ((Dresseur_Ordi)combat.getDresseur(1)).choixActionSuivante();
                    System.out.println(combat.debutNouveauTour());
                    System.out.println(combat.actionPremierJoueur());
                    if(combat.pokemonKO()) {
                        System.out.println(combat.changerKO());
                        System.out.println(combat.terminerTour());
                        continue;
                    }
                    System.out.println(combat.actionSecondJoueur());
                    if(combat.pokemonKO()) {
                        System.out.println(combat.changerKO());
                        
                    }
                    System.out.println(combat.terminerTour());
                    scan.nextLine();
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    public static void clear(){
        try{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            System.out.println("Clear");
        } catch(Exception error){
            System.err.println(error.getLocalizedMessage());
        }
    }
}

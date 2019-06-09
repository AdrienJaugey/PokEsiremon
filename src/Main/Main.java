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

import Pokemon.Capacite.Capacite;
import Pokemon.Capacite.EffetCapacite.Effet;
import Pokemon.Capacite.EffetCapacite.EffetStatut;
import static Pokemon.Capacite.Enum_Cible.*;
import static Pokemon.Enum_Statut.*;
import static Pokemon.Enum_TypePokemon.*;
import Pokemon.Pokedex;
import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey
 */
public class Main {
    public static void main(String[] args) {
        clear();
        Pokemon pika = new Pokemon(25);
        System.out.println(pika);
        Capacite eclair = new Capacite("Eclair", ELECTRIK, 50, 100);
        Effet e = new EffetStatut(ADVERSAIRE, PARALYSIE);
        eclair.addEffet(e);
        pika.setCapacite(eclair, 0);
        Pokemon carapuce = new Pokemon(7);
        System.out.println(carapuce);
        System.out.println(pika.utiliserCapacite(0, carapuce));
        System.out.println(carapuce);
        
        System.out.println("");
        System.out.println(Pokedex.get().getCapacite(0));
        System.out.println(Pokedex.get().getCapacite(1));
        for(Integer i : Pokedex.get().getCapacitePokemon(4)) System.out.println(i);
    }
    
    public static void clear(){
        try{
            System.out.println("Clear");
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch(Exception error){
            System.err.println(error.getLocalizedMessage());
        }
    }
}

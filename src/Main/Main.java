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
import Pokemon.Pokedex;

/**
 *
 * @author AdrienJaugey
 */
public class Main {
    public static void main(String[] args) {
        clear();
        Capacite c = null;
        for(int i = 0; i < Pokedex.NB_CAPACITE; i++){
            if(i == 24 || i == 46) continue;
            if(Pokedex.get().getCapacite(i) == null) break;
            else c = Pokedex.get().getCapacite(i);
        }
        System.out.println(c);
       
        /*Pokemon pika = new Pokemon(25);
        Pokemon carapuce = new Pokemon(7);
        Capacite eclair = new Capacite("Eclair", ELECTRIK, 40, 100, 1, false, 1, 1);
        try {
            pika.setCapacite(eclair, 0);
            System.out.println(pika.utiliserCapacite(0, carapuce));
            System.out.println(pika);
            System.out.println(carapuce);
            System.out.println("\n");
            System.out.println(pika.utiliserCapacite(0, carapuce));
            System.out.println(pika);
            System.out.println(carapuce);
            System.out.println("\n");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
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

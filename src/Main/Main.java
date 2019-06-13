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
        int lastIndex = 0;
        for(int i = 0; i < Pokedex.NB_CAPACITE; i++){
            if(i == 24 || i == 46) continue;
            if(Pokedex.get().getCapacite(i) == null) break;
            else c = Pokedex.get().getCapacite(i);
            lastIndex = i;
        }
        System.out.println("[" + lastIndex + "]");
        System.out.println(c);
       
        /*Pokemon pika = new Pokemon(25);
        Pokemon carapuce = new Pokemon(7);
        try {
            pika.setCapacite(51, 0);
            pika.setCapacite(1, 1);
            pika.setCapacite(2, 2);
            pika.setCapacite(3, 3);
            carapuce.setCapacite(57, 0);
            System.out.println(pika.utiliserCapacite(0, carapuce));
            System.out.println(pika);
            System.out.println(carapuce);
            System.out.println("\n");
            System.out.println(carapuce.utiliserCapacite(0, pika));
            System.out.println(carapuce.utiliserCapacite(0, pika));
            System.out.println(pika.utiliserCapacite(0, carapuce));
            System.out.println(pika.utiliserCapacite(1, carapuce));
            System.out.println(pika);
            System.out.println(carapuce);
            System.out.println("\n");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
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

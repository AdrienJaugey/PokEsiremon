/*
 * Copyright (C) 2019 AdrienJaugey <a.jaugey@gmail.com>.
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
package PokEsiremon.Personnage.Pokemon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Pokedex {
    private static Pokedex _instance;
    private final String[] _noms;
    private final TypePokemon[][] _types;
    private final int[][] _baseStats;
    
    private Pokedex(){
        _noms = new String[151];
        _baseStats = new int[151][6];
        _types = new TypePokemon[151][2];
        String line = "";
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_base_stats.csv"))) {

            while ((line = br.readLine()) != null) {
                String[] pkmn = line.split(";");
                _noms[i] = pkmn[1];
                _baseStats[i][0] = Integer.parseInt(pkmn[2]);
                _baseStats[i][1] = Integer.parseInt(pkmn[3]);
                _baseStats[i][2] = Integer.parseInt(pkmn[4]);
                _baseStats[i][3] = Integer.parseInt(pkmn[5]);
                _baseStats[i][4] = Integer.parseInt(pkmn[6]);
                _baseStats[i][5] = Integer.parseInt(pkmn[7]);

                i++;
            }

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    public static Pokedex get(){
        if(_instance == null) _instance = new Pokedex();
        return _instance;
    }
    
    public String getNom(int id){
        return _noms[id - 1];
    }
    
    public int[] getBaseStats(int id){
        return _baseStats[id - 1];
    }
    
    public TypePokemon[] getTypes(int id){
        return _types[id - 1];
    }
}

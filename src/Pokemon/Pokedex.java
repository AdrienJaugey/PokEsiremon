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
package Pokemon;

import Pokemon.Capacite.Capacite;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Pokedex {
    public static final int NB_POKEMON = 151;
    public static final int NB_CAPACITE = 1;
    private static Pokedex _instance;
    private final String[] _pkmnNom;
    private final TypePokemon[][] _pkmnType;
    private final int[][] _pkmnBaseStat;
    private final ArrayList<Integer>[] _pkmnCapacite;
    private final Capacite[] _capacite;
    
    private Pokedex(){
        _pkmnNom = new String[NB_POKEMON];
        _pkmnBaseStat = new int[NB_POKEMON][6];
        _pkmnType = new TypePokemon[NB_POKEMON][2];
        _pkmnCapacite = new ArrayList[NB_POKEMON];
        
        _capacite = new Capacite[NB_CAPACITE];
        
        String line = "";
        int i = 0;
        TypePokemon types[] = TypePokemon.values();
        try {
            Scanner scan = new Scanner(getClass().getResourceAsStream("pokemon_base_stats.csv"));
            while (scan.hasNextLine()) {
                String[] pkmn = scan.nextLine().split(";");
                _pkmnNom[i] = pkmn[1];
                _pkmnBaseStat[i][0] = Integer.parseInt(pkmn[2]);
                _pkmnBaseStat[i][1] = Integer.parseInt(pkmn[3]);
                _pkmnBaseStat[i][2] = Integer.parseInt(pkmn[4]);
                _pkmnBaseStat[i][3] = Integer.parseInt(pkmn[5]);
                _pkmnBaseStat[i][4] = Integer.parseInt(pkmn[6]);
                _pkmnBaseStat[i][5] = Integer.parseInt(pkmn[7]);
                _pkmnType[i][0] = types[Integer.parseInt(pkmn[8])];
                _pkmnType[i][1] = (Integer.parseInt(pkmn[9]) != -1 ? types[Integer.parseInt(pkmn[9])] : null);
                i++;
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    public static Pokedex get(){
        if(_instance == null) _instance = new Pokedex();
        return _instance;
    }
    
    public String getNomPkmn(int id){
        return _pkmnNom[id - 1];
    }
    
    public int[] getBaseStatsPkmn(int id){
        return _pkmnBaseStat[id - 1];
    }
    
    public TypePokemon[] getTypesPkmn(int id){
        return _pkmnType[id - 1];
    }
    
    public ArrayList<Integer> getCapacitePokemon(int id){
        return _pkmnCapacite[id];
    }
    
    public Capacite getCapacite(int id){
        return _capacite[id];
    }
}

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
package PokEsiremon.Personnage.Pokemon.Capacite;

import PokEsiremon.Personnage.Pokemon.Pokemon;
import PokEsiremon.Personnage.Pokemon.TypePokemon;

/**
 *
 * @author AdrienJaugey
 */
public class Capacite {
    private final String _nom;
    private final TypePokemon _typePkmn;
    private final TypeAttaque _typeAtq;
    private final int _puissance;
    private final int _precision;

    public Capacite(String nom, TypePokemon typePkmn, int puissance, int precision) {
        _nom = nom;
        _typePkmn = typePkmn;
        _typeAtq = TypeAttaque.get(typePkmn);
        _puissance = puissance;
        _precision = precision;
        
    }

    public TypePokemon getTypePkmn() {
        return _typePkmn;
    }

    public TypeAttaque getTypeAtq() {
        return _typeAtq;
    }
    
    public void utiliser(Pokemon sender, Pokemon receiver){
        
    }
}

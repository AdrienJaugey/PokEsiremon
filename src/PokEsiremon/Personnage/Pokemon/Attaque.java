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
package PokEsiremon.Personnage.Pokemon;

/**
 *
 * @author AdrienJaugey
 */
public class Attaque {
    private final TypePokemon typePkmn;
    private final TypeAttaque typeAtq;

    public Attaque(TypePokemon typePkmn, TypeAttaque typeAtq) {
        this.typePkmn = typePkmn;
        this.typeAtq = typeAtq;
    }

    public TypePokemon getTypePkmn() {
        return typePkmn;
    }

    public TypeAttaque getTypeAtq() {
        return typeAtq;
    }
    
    public void attaquer(Pokemon pkmn){
        
    }
}

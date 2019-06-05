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
package Pokemon.Capacite;

import Pokemon.Enum_TypePokemon;
import static Pokemon.Enum_TypePokemon.*;

/**
 *
 * @author AdrienJaugey
 */
public enum Enum_TypeAttaque {
    PHYSIQUE,
    SPECIALE;
    
    public static Enum_TypeAttaque get(Enum_TypePokemon type){
        if(type == PLANTE 
        || type == EAU 
        || type == FEU 
        || type == ELECTRIK 
        || type == PSY 
        || type == GLACE 
        || type == DRAGON){
            return SPECIALE;
        } else {
            return PHYSIQUE;
        }
    }
}
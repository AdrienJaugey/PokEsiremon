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

import Pokemon.Capacite.EffetCapacite.Effet;
import Pokemon.Enum_TypePokemon;
import Pokemon.Pokemon;
import java.util.ArrayList;

/**
 *
 * @author AdrienJaugey
 */
public class Capacite {
    private final String _nom;
    private final Enum_TypePokemon _typePkmn;
    private final Enum_TypeAttaque _typeAtq;
    private final int _puissance;
    private final int _precision;
    private final ArrayList<Effet> _effetCapacite;

    public Capacite(String nom, Enum_TypePokemon typePkmn, int puissance, int precision) {
        _nom = nom;
        _typePkmn = typePkmn;
        _typeAtq = Enum_TypeAttaque.get(typePkmn);
        _puissance = puissance;
        _precision = precision;
        _effetCapacite = new ArrayList<>();
    }

    public Enum_TypePokemon getTypePkmn() {
        return _typePkmn;
    }

    public Enum_TypeAttaque getTypeAtq() {
        return _typeAtq;
    }
    
    public String getNom(){
        return _nom;
    }

    public int getPuissance() {
        return _puissance;
    }

    public int getPrecision() {
        return _precision;
    }
    
    public void addEffet(Effet e){
        _effetCapacite.add(e);
    }
    
    public String utiliser(Pokemon lanceur, Pokemon cible){
        String res = "";
        cible.subir(_typeAtq, _typePkmn, (int) (lanceur.getAtq() + _puissance * 1.5));
        for(Effet e : _effetCapacite){
            res += e.agir(lanceur, cible) + "\n";
        }
        return res;
    }
}

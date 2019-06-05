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
package Pokemon.Capacite.EffetCapacite;

import Pokemon.Capacite.Enum_Cible;
import Pokemon.Enum_Statistique;
import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class EffetStatistique extends Effet {
    private final Enum_Statistique _stat;
    private final double _modifier;
    private final int _delta;
    
    public EffetStatistique(Enum_Cible cible, Enum_Statistique stat, int delta,  double modifier) {
        super(cible);
        _stat = stat;
        _delta = delta;
        _modifier = modifier;
    }

    @Override
    public String effet(Pokemon cible, Pokemon autre) {
        if(_delta == 0) return cible.modifierStatistique(_stat, _modifier);
        else return cible.modifierStatistique(_stat, _delta);
    }

    @Override
    public String toString() {
        String res = "";
        if(_delta > 0){
            res = "Ajoute " + (int)(_modifier * 100) + "%";
        } else if(_delta < 0){
            res = "Retire " + (int)(_modifier * 100) + "%";
        } else if(_modifier > 0){
            res = "Ajoute " + _delta + " points";
        } else {
            res = "Retire " + _delta + " points";
        }
        return res + " de " + _stat.toString() + " du pokémon " + super._cible.toString();
    }
    
}

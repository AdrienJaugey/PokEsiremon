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
import static Pokemon.Capacite.Enum_Cible.LANCEUR;
import Pokemon.Pokemon;
import Pokemon.Utils;
import static Pokemon.Utils.chance;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public abstract class Effet {
    protected final Enum_Cible _cible;
    protected final int _chance;

    public Effet(Enum_Cible cible, int chance) {
        this._cible = cible;
        this._chance = chance;
    }
            
    public String agir(Pokemon lanceur, Pokemon adversaire){
        String res = "";
        if(_cible == LANCEUR){
            if(Utils.chance(_chance)){
                res = effet(lanceur, adversaire);
            }
        }
        else {
            if(chance(_chance)){
                res = effet(adversaire, lanceur);
            }
        }
        return res;
    }
    
    public abstract String effet(Pokemon cible, Pokemon autre);
    
    public abstract String description();
    
    @Override
    public String toString(){
        return description() + " au pokémon " + _cible + (_chance < 100 ? " avec " + _chance + "% de chance." : ".");
    }

    /**
     * Permet de savoir si l'effet soigne
     * @return true si l'effet soigne les statuts, false sinon
     */
    public boolean soigne(){
        return false;
    }
    
    /**
     * Permet de savoir si l'effet rend des PV
     * @return true si l'effet rend des PV, false sinon
     */
    public boolean rendPV(){
        return false;
    }
}

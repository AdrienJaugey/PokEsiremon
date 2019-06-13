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
import static Pokemon.Enum_Statistique.VIE;
import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class EffetSpecial extends Effet{
    private Enum_EffetSpeciaux _type;
    private double _modifier;

    public EffetSpecial(Enum_Cible cible, int chance, Enum_EffetSpeciaux type, double modifier) {
        super(cible, chance);
        this._type = type;
        this._modifier = modifier;
    }
    
    @Override
    public String effet(Pokemon cible, Pokemon autre) {
        String res = "";
        switch (_type) {
            case PEUR: {
                res = cible.apeurer();
            } break;
            case CLONAGE:{
                int vieClone = cible.getVieMax() / 4;
                if(cible.getVie() > vieClone){
                    cible.modifierStatistique(VIE, -vieClone);
                    cible.creerClone();
                    res = cible.getNom() + " a créé un clone";
                }
            } break;
            case COPIE_TYPE: {
                cible.copierType(autre);
            } break;
            case NO_STATUS_CHANGE: {
                res = cible.activerBrume();
            } break;
            case CONTRECOUP: {
                cible.perdreVie((int) Math.round(autre.getDernierDegats() * _modifier));
                res = cible.getNom() + " se blesse en attaquant";
            } break;
            case COPIE_CAPACITE: res = cible.copierCapacite(autre); break;
            case ENTRAVE: res = cible.bloquerCapacite(2, 8); break;
            case DEGATS2VIE: {
                int restaure = (int)Math.round(autre.getDernierDegats() * _modifier);
                res = cible.modifierStatistique(VIE, restaure);
            } break;
        }
        return res;
    }

    @Override
    public String description() {
        String res = "";
        switch (_type) {
            case PEUR: res = "Apeure le pokémon " + super._cible.toString().toLowerCase(); break;
            case CLONAGE: res = "Crée un clone qui prend les dégats à la place du pokémon.\n\t  Retire 25% du maximum de PV qui deviennent la vie du clone"; break;
            case COPIE_TYPE: res = "Le pokémon prend le type du pokémon adverse"; break;
            case NO_STATUS_CHANGE: res = "Une brume se lève et empêche les changements de statistiques du pokémon lanceur"; break;
            case CONTRECOUP: res = "Le lanceur se blesse a hauteur de " + (int)(_modifier*100) + "% des dégats"; break;
            case ENTRAVE: res = "La dernière attaque du pokémon " + _cible.toString() + " est entravée.\n\t  S'il n'y en a pas, la capacité échoue"; break;
            case COPIE_CAPACITE : res = "Copie la dernière capacité utilisée par le pokémon adverse.\n\t  Echoue s'il n'y en a pas."; break;
            case DEGATS2VIE : res = "Restaure " + (int)(_modifier * 100) + "% des dégats réalisés en tant que vie"; break;
        }
        if(super._chance != 100) res += ", " + _chance + "% de chance de réussite.";
        else res += ".";
        return res;
    }
    
    @Override
    public String toString(){
        return this.description();
    }
    
}

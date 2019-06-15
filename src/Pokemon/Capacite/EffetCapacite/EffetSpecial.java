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

import Pokemon.Capacite.Capacite;
import static Pokemon.Capacite.EffetCapacite.Enum_EffetSpeciaux.*;
import Pokemon.Capacite.Enum_Cible;
import static Pokemon.Capacite.Enum_Cible.LANCEUR;
import static Pokemon.Enum_Statistique.VIE;
import static Pokemon.Enum_TypePokemon.PSY;
import Pokemon.Pokedex;
import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class EffetSpecial extends Effet{
    private final Enum_EffetSpeciaux _type;
    private final double _modifier;

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
            case BUEENOIRE : {
                res = cible.bueeNoire(cible);
                res += autre.bueeNoire(autre); 
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
            case METRONOME: {
                Pokedex pkdx = Pokedex.get();
                int idCapa = (int)Math.round(1 + Math.random() * pkdx.NB_CAPACITE);
                while(pkdx.getCapacite(idCapa) == null) idCapa = (int)Math.round(1 + Math.random() * pkdx.NB_CAPACITE);
                Capacite capa = pkdx.getCapacite(idCapa);
                res = cible.getNom() + " lance " + capa.getNom() + "." + capa.utiliser(cible, autre);
            } break;
            case MIMIQUE: {
                Capacite capa = autre.getDerniereCapacite();
                if (capa != null) {
                    res = cible.getNom() + " lance " + capa.getNom() + ".\n";
                    res += capa.utiliser(cible, autre);
                } else {
                    res = "Mimique a echoué";
                }
            } break;
            case RENVOI_DEGAT:{
                int degats = cible.getDernierDegats() * 2;
                res = autre.perdreVie(degats);
            } break;
            case SOIN : {
                res = cible.soigner();
            } break;
            case DEVOREVE:{
                if(cible.isAsleep()){
                    Capacite devoreve = new Capacite("Dévorêve", PSY, 100, 100, 1, false, 1, 1, false, false);
                    EffetSpecial effet = new EffetSpecial(LANCEUR, 100, DEGATS2VIE, 0.5);
                    devoreve.addEffet(effet);
                    devoreve.utiliser(autre, cible);
                    res = cible.getNom() + " perd " + cible.getDernierDegats() + " PV.";
                    res += "\n" + autre.getNom() + " récupère " + (int)Math.round(cible.getDernierDegats() / 2) + " PV.";
                } else {
                    res = cible.getNom() + " n'est pas endormi, Dévorêve échoue.";
                }
            }break;
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
            case BUEENOIRE: res = "Les changements de statistiques des deux pokémons sont annulés.\n\t  "
                            + "Le changement de statut de l'adversaire et l'effet de brume aussi"; break;
            case CONTRECOUP: res = "Le lanceur se blesse a hauteur de " + (_modifier*100) + "% des dégats"; break;
            case RENVOI_DEGAT: res = "Le pokémon " + _cible.toString() + " renvoit " + (_modifier * 100) + "% des dégats reçus"; break;
            case ENTRAVE: res = "La dernière attaque du pokémon " + _cible.toString() + " est entravée.\n\t  S'il n'y en a pas, la capacité échoue"; break;
            case COPIE_CAPACITE : res = "Copie la dernière capacité utilisée par le pokémon adverse jusqu'à retrait du pokémon.\n\t  Echoue s'il n'y en a pas"; break;
            case DEGATS2VIE : res = "Restaure " + (_modifier * 100) + "% des dégats réalisés en tant que vie"; break;
            case METRONOME : res = "Lance une capacité aléatoire, parmi toutes celles existantes"; break;
            case MIMIQUE : res = "Copie la dernière capacité utilisée par le pokémon adverse"; break;
            case SOIN : res = "Le pokémon " + _cible.toString() + " récupère toute sa vie.\n\t  Les changements de statuts sont annulés"; break;
            case DEVOREVE : res = "Attaque uniquement si le pokémon adverse dort.\n\t  Restaure 50% des dégats en tant que vie"; break;
        }
        if(super._chance != 100) res += ", " + _chance + "% de chance de réussite.";
        else res += ".";
        return res;
    }
    
    @Override
    public String toString(){
        return this.description();
    }

    @Override
    public boolean soigne() {
        return _type == SOIN;
    }
    
    @Override
    public boolean rendPV(){
        return _type == SOIN || _type == DEVOREVE ||_type == DEGATS2VIE;
    }
    
}

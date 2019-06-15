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
import Pokemon.Utils;
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
    private final int _critModifier;
    private final int _nbFrappeMin;
    private final int _nbFrappeMax;
    private final int _ppMax;
    private boolean _soigne;
    private boolean _rendPV;
    private final boolean _tourAvant;
    private final boolean _tourApres;
    private final ArrayList<Effet> _effetCapacite;

    public Capacite(String nom, Enum_TypePokemon typePkmn, int puissance, int precision, int ppMax, boolean boostCrit, int nbFrappeMin, int nbFrappeMax, boolean tourAvant, boolean tourApres) {
        _nom = nom;
        _typePkmn = typePkmn;
        _typeAtq = Enum_TypeAttaque.get(typePkmn);
        _puissance = puissance;
        _precision = precision;
        _ppMax = ppMax;
        _critModifier = (boostCrit ? 8 : 1);
        _nbFrappeMin = nbFrappeMin;
        _nbFrappeMax = nbFrappeMax;
        _effetCapacite = new ArrayList<>();
        _tourAvant = tourAvant;
        _tourApres = tourApres;
        _soigne = false;
    }

    public final Enum_TypePokemon getTypePkmn() {
        return _typePkmn;
    }

    public final Enum_TypeAttaque getTypeAtq() {
        return _typeAtq;
    }
    
    public final String getNom(){
        return _nom;
    }

    public final int getPuissance() {
        return _puissance;
    }

    public final int getPrecision() {
        return _precision;
    }
    
    public final int getPPMax(){
        return _ppMax;
    }
    
    public final void addEffet(Effet e){
        _effetCapacite.add(e);
        if(e.soigne()) {
            _soigne = true;
            _rendPV = true;
        }
        if(e.rendPV()) _rendPV = true;
    }
    
    public final String utiliser(Pokemon lanceur, Pokemon cible){
        String res = "";
        int pReussite = (int) Math.round((lanceur.getPrecision() * (double) this.getPrecision()) / cible.getEsquive());
        if(Utils.chance(pReussite)){
            lanceur.setDerniereCapacite(this);
            if(_puissance > 0){
                if(_nbFrappeMin == _nbFrappeMax && _nbFrappeMin == 1) res += cible.subir(this, lanceur);
                else {
                    for(int i = 0; i < _nbFrappeMin + (int)(Math.random()*_nbFrappeMax-_nbFrappeMin); i++)
                        res += cible.subir(this, lanceur);
                }
            }
            if(!cible.isKO()){
                for(Effet e : _effetCapacite){
                    res += "\n" + e.agir(lanceur, cible);
                }
            }
        } else {
            res = lanceur.getNom() + " n'a pas pu attaquer.";
        }
        
        return res;
    }

    @Override
    public String toString() {
        String res = _nom + " (" + _typePkmn + "/" + _typeAtq + ") " + _ppMax + " PP"
                   + "\nPuissance : ";
        if(_nom.equals("Dévorêve")) res += "100";
        else res += (_puissance == 0 ? " -" : _puissance);
        res += "\nPrecision : " + _precision + "%";
        if(!_effetCapacite.isEmpty()) res += "\nEffets :";
        for(Effet e : _effetCapacite){
            res += "\n\t- " + e.toString();
        }
        if(_critModifier == 8) res += "\nForte chance de coups critiques.";
        if(_nbFrappeMin > 1){
            if(_nbFrappeMin != _nbFrappeMax){
                res += "\nFrappe entre " + _nbFrappeMin + " et " + _nbFrappeMax + " fois l'adversaire.";
            } else {
                res += "\nFrappe " + _nbFrappeMin + " fois l'adversaire.";
            }
        }
        if(_tourAvant){
            res += "\nL'utilisateur se prépare au premier tour et attaque au second.";
        }
        if(_tourApres){
            res += "\nL'utilisateur se repose au tour suivant.";
        }
        return res;
    }

    public final double getModifierCrit() {
        return _critModifier;
    }

    public final boolean tourAttente(){
        return _tourAvant;
    }
    
    public final boolean tourRepos(){
        return _tourApres;
    }
    
    public final boolean soigne(){
        return _soigne;
    }
    
    public final boolean rendPV(){
        return _rendPV;
    }
}
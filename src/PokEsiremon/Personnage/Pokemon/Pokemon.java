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

import PokEsiremon.Personnage.Personnage;
import PokEsiremon.Personnage.Pokemon.Capacite.Capacite;
import static PokEsiremon.Personnage.Pokemon.Enum_Statut.*;

/**
 *
 * @author AdrienJaugey
 */
public class Pokemon extends Personnage{
    private TypePokemon _type[];
    private String _nom;
    private Capacite[] _attaques;
    private Enum_Statut _statut;
    private int _tourStatut;
    
    //Valeur des stats actuelles
    private int _vie;
    private int _atq;
    private int _def;
    private int _atqSpe;
    private int _defSpe;
    private int _vit;
    private int _exp;
    private int _niveau;
    
    //Valeur des stats Maximale
    private final int _vieMax;
    private final int _atqMax;
    private final int _defMax;
    private final int _atqSpeMax;
    private final int _defSpeMax;
    private final int _vitMax;
    
    public Pokemon(int id){
        Pokedex pkdx = Pokedex.get();
        _nom = pkdx.getNomPkmn(id);
        _statut = NEUTRE;
        _tourStatut = 0;
        int baseStats[] = pkdx.getBaseStatsPkmn(id);
        int dv[] = generateDV();
        _vieMax = hpFormula(baseStats[0], dv[0]);
        _atqMax = statFormula(baseStats[1], dv[1]);
        _defMax = statFormula(baseStats[2], dv[2]);
        _atqSpeMax = statFormula(baseStats[3], dv[3]);
        _defSpeMax = statFormula(baseStats[4], dv[4]);
        _vitMax = statFormula(baseStats[5], dv[5]);
        _attaques = new Capacite[4];
        _type = pkdx.getTypesPkmn(id);
        this.resetStats();
    }

    @Override
    public String toString() {
        String res = _nom + " (" + _type[0] + (_type[1] != null ? "/" + _type[1] : "" ) + ") Niv. " + _niveau;
        res += "\n\tVie :\t " + _vie + "/" + _vieMax;
        res += "\n\tAtq :\t " + _atqMax + "/" + _atqMax;
        res += "\n\tDef :\t " + _defMax + "/" + _defMax;
        res += "\n\tAtqSpe : " + _atqSpeMax + "/" + _atqSpeMax;
        res += "\n\tDefSpe : " + _defSpeMax + "/" + _defSpeMax;
        res += "\n\tVit :\t " + _vitMax + "/" + _vitMax;
        return res;
    }
    
    public TypePokemon[] getType() {
        return _type;
    }

    public String getNom() {
        return _nom;
    }

    public Capacite[] getAttaques() {
        return _attaques;
    }
    
    public void setAttaque(int id, int emplacement){
        this._attaques[emplacement] = Pokedex.get().getCapacite(id);
    }

    public int getVie() {
        return _vie;
    }

    public int getAtq() {
        return _atq;
    }

    public int getDef() {
        return _def;
    }

    public int getAtqSpe() {
        return _atqSpe;
    }

    public int getDefSpe() {
        return _defSpe;
    }

    public int getVitesse() {
        return _vit;
    }
    
    public int getExp(){
        return _exp;
    }
    
    public int getNiveau(){
        return _niveau;
    }

    public Enum_Statut getStatut() {
        return _statut;
    }

    public int getVieMax() {
        return _vieMax;
    }

    public int getAtqMax() {
        return _atqMax;
    }

    public int getDefMax() {
        return _defMax;
    }

    public int getAtqSpeMax() {
        return _atqSpeMax;
    }

    public int getDefSpeMax() {
        return _defSpeMax;
    }

    public int getVitesseMax() {
        return _vitMax;
    }
    
    private int hpFormula(int vieBase, int dv){
        return (int)((vieBase + dv + 82)*2 + 10);
    }
    
    private int statFormula(int statBase, int dv){
        return (int)((statBase + dv + 32)*2 + 5);
    }
    
    private int[] generateDV(){
        int DV[] = new int[6];
        for(int i = 0; i < 6; i++){
            DV[i] = (int)(Math.random() * 15);
        }
        return DV;
    }

    private void resetStats() {
        this._vie = this._vieMax;
        this._atq = this._atqMax;
        this._def = this._defMax;
        this._atqSpe = this._atqSpeMax;
        this._defSpe = this._defSpeMax;
        this._vit = this._vitMax;
        this._statut = NEUTRE;
        this._tourStatut = 0;
    }
    
}
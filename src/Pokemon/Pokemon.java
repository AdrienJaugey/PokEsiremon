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
package Pokemon;

import Pokemon.Capacite.Capacite;
import Pokemon.Capacite.Enum_TypeAttaque;
import static Pokemon.Capacite.Enum_TypeAttaque.*;
import static Pokemon.Enum_Statistique.VIE;
import static Pokemon.Enum_Statut.*;
import static Pokemon.Enum_TypePokemon.*;

/**
 *
 * @author AdrienJaugey
 */
public class Pokemon{
    private final Enum_TypePokemon _type[];
    private final String _nom;
    private final Capacite[] _capacites;
    private Enum_Statut _statut;
    Pokemon _cibleVampigraine; 
    private int _tourStatut;
    
    //Valeur des stats actuelles
    private int _vie;
    private int _atq;
    private int _def;
    private int _atqSpe;
    private int _defSpe;
    private int _vit;
    private int _precision;
    private int _esquive;
    
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
        _capacites = new Capacite[4];
        _type = pkdx.getTypesPkmn(id);
        this._precision = 100;
        this._esquive = 100;
        this._cibleVampigraine = null;
        this.resetStats();
    }

    @Override
    public String toString() {
        String res = _nom + " (" + _type[0] + (_type[1] != null ? "/" + _type[1] : "" ) + ") Niv. 100";
        res += "\n\tVie :\t " + _vie + "/" + _vieMax;
        res += "\n\tAtq :\t " + getAtq() + "/" + _atqMax;
        res += "\n\tDef :\t " + getDef() + "/" + _defMax;
        res += "\n\tAtqSpe : " + getAtqSpe() + "/" + _atqSpeMax;
        res += "\n\tDefSpe : " + getDefSpe() + "/" + _defSpeMax;
        res += "\n\tVit :\t " + getVitesse() + "/" + _vitMax;
        return res;
    }
    
    public Enum_TypePokemon[] getType() {
        return _type;
    }

    public String getNom() {
        return _nom;
    }

    public Capacite[] getAttaques() {
        return _capacites;
    }
    
    public void setCapacite(Capacite atq, int emplacement){
        this._capacites[emplacement] = atq;//Pokedex.get().getCapacite(id);
    }

    public int getVie() {
        return _vie;
    }

    public int getAtq() {
        if(_statut == BRULURE) return _atq/2; 
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
        if(_statut == PARALYSIE) return _vit/2;
        return _vit;
    }
    
    public Enum_Statut getStatut() {
        return _statut;
    }

    public int getPrecision() {
        return _precision;
    }

    public int getEsquive() {
        return _esquive;
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
    
    public String modifierStatistique(Enum_Statistique stat, int delta){
        String res = "";
        if(delta > 0) {
            res = stat.toString() + " de " + _nom + " augmente.";
        } else if(delta < 0){
            res = stat.toString() + " de " + _nom + " diminue.";
        }
        switch(stat){
            case VIE : {
                _vie = borne(0, _vie + delta, _vieMax);
                if(_vie == 0) {
                    gererKO();
                    res = this._nom + " est K.O.";
                } else if(delta > 0){
                    res = this._nom + " récupère " + delta + " PV.";
                } else if(delta < 0){
                    res = this._nom + " perd " + (-delta) + " PV.";
                }
            } break;
            case ATQ : {
                _atq = borne(0, _atq + delta, _atqMax);
            } break;
            case DEF : {
                _def = borne(0, _def + delta, _defMax);
            } break;
            case ATQSPE : {
                _atqSpe = borne(0, _atqSpe + delta, _atqSpeMax);
            } break;
            case DEFSPE : {
                _defSpe = borne(0, _defSpe + delta, _defSpeMax);
            } break;
            case VITESSE : {
                _vit = borne(0, _vit + delta, _vit);
            } break;
            case PRECISION : {
                _precision = borne(0, _precision + delta, _precision);
            } break;
            case ESQUIVE : {
                _esquive = borne(0, _esquive + delta, _esquive);
            } break;
        }
        return res;
    }
    
    public String modifierStatistique(Enum_Statistique stat, double modifier){
        String res = "";
        if(modifier > 0) {
            res = stat.toString() + " de " + _nom + " augmente.";
        } else if(modifier < 0){
            res = stat.toString() + " de " + _nom + " diminue.";
        }
        switch(stat){
            case VIE : {
                _vie = borne(0, (int)  Math.round(_vie * (1 + modifier)), _vieMax);
                if(_vie == 0) {
                    gererKO();
                    res = this._nom + " est K.O.";
                } else if(modifier > 0){
                    res = this._nom + " récupère " + (modifier * 100) + "% PV.";
                } else if(modifier < 0){
                    res = this._nom + " perd " + (-modifier * 100) + "% PV.";
                }
            } break;
            case ATQ : {
                _atq = borne(0, (int)  Math.round(_atq  * (1 + modifier)), _atqMax);
            } break;
            case DEF : {
                _def = borne(0, (int)  Math.round(_def  * (1 + modifier)), _defMax);
            } break;
            case ATQSPE : {
                _atqSpe = borne(0, (int)  Math.round(_atqSpe  * (1 + modifier)), _atqSpeMax);
            } break;
            case DEFSPE : {
                _defSpe = borne(0, (int)  Math.round(_defSpe  * (1 + modifier)), _defSpeMax);
            } break;
            case VITESSE : {
                _vit = borne(0, (int)  Math.round(_vit  * (1 + modifier)), _vit);
            } break;
            case PRECISION : {
                _precision = borne(0, (int)  Math.round(_precision  * (1 + modifier)), _precision);
            } break;
            case ESQUIVE : {
                _esquive = borne(0, (int) Math.round(_esquive  * (1 + modifier)), _esquive);
            } break;
        }
        return res;
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
        this.resetCibleVampigraine();
    }
    
    private boolean isType(Enum_TypePokemon type){
        return (_type[0] == type || _type[1] == type);
    }
    
    public void setStatut(Enum_Statut statut){
        this.setStatut(statut, null);
    }
    
    public String setStatut(Enum_Statut statut, Pokemon cibleVampigraine){
        String res = "";
        if(this._statut != statut && this._statut == NEUTRE){
            switch(statut){
                case BRULURE: {
                    if(!isType(FEU)){
                        _statut = BRULURE;
                        res = _nom + " est brûlé";
                    }
                } break;
                case GEL: {
                    if(!isType(GLACE)) {
                        _statut = GEL;
                        res = _nom + " est gelé";
                    }
                } break;
                case PARALYSIE: {
                    if(!isType(ELECTRIK)) {
                        _statut = PARALYSIE;
                        res = _nom + " est paralysé";
                    }
                } break;
                case EMPOISONNEMENT: {
                    if(!isType(POISON)) {
                        _statut = EMPOISONNEMENT;
                        res = _nom + " est empoisonné";
                    }
                } break;
                case SOMMEIL: {
                    _statut = SOMMEIL;
                    _tourStatut = 1 + (int)(Math.random() * 2);
                    res = _nom + " s'endort";
                } break;
                case CONFUSION: {
                    _statut = CONFUSION;
                    _tourStatut = 1 + (int)(Math.random() * 3);
                    res = _nom + " est confus";
                } break;
                case VAMPIGRAINE: {
                    if(!isType(PLANTE)) {
                        _statut = VAMPIGRAINE;
                        _cibleVampigraine = cibleVampigraine;
                        res = _nom + " est infecté";
                    }
                } break;
                default : { } break;
            }
        } 
        return res;
    }
    
    public String resetStatut(){
        String res = _nom + " n'est plus ";
        switch(_statut){
            case BRULURE: { res += "brûlé"; } break;
            case GEL: { res += "gelé"; } break;
            case PARALYSIE: { res += "paralysé"; } break;
            case EMPOISONNEMENT: { res += "empoisonné"; } break;
            case SOMMEIL: { res = _nom + " se réveille"; } break;
            case CONFUSION: { res += "confus"; } break;
            case VAMPIGRAINE: { res += "infecté"; } break;
        }
        _statut = NEUTRE;
        _cibleVampigraine = null;
        _tourStatut = 0;
        return res;
    }
    
    public void resetCibleVampigraine(){
        _cibleVampigraine = null;
    }
    
    public void debutTour(){
        if(_statut == SOMMEIL || _statut == CONFUSION){
            _tourStatut--;
            if(_tourStatut == 0) _statut = NEUTRE;
        } else if(_statut == GEL){
            if(chance(20)) _statut = NEUTRE;
        }
    }
    
    public String utiliserCapacite(int choixCapacite, Pokemon cible){
        String res = this._nom + " lance " + _capacites[choixCapacite].getNom() + ".";
        switch(_statut){
            case SOMMEIL:{
                res = this._nom + " n'a pas pu attaquer.";
            } break;
            case GEL:{
                if(_capacites[choixCapacite].getTypePkmn() == FEU){
                    this.resetStatut();
                    res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                } else {
                    res = this._nom + " n'a pas pu attaquer";
                }
            } break;
            case PARALYSIE:{
                if(chance(75)){
                    res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                } else {
                    res = this._nom + " n'a pas pu attaquer.";
                }
            } break;
            case CONFUSION:{
                if(chance(33)){
                    this.subir(PHYSIQUE, null, 40);
                    res = this._nom + " s'est blessé dans sa confusion.";
                }
                else res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
            } break;
            default :{
                res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
            } break;
        }
        return res;
    }
    
    public void subir(Enum_TypeAttaque typeAtq, Enum_TypePokemon typePkmn, int puissance){
        int defense = (typeAtq == PHYSIQUE ? getDef() : getDefSpe());
        double modifier = (typePkmn != null ? this.getModifier(typePkmn) : 1);
        int pv = (int) Math.round(puissance * modifier - defense);
        this.modifierStatistique(VIE, -pv);
    }
    
    private double getModifier(Enum_TypePokemon type){
        double modifier = 1;
        modifier *= _type[0].dmgModifier(type);
        if(_type[1] != null) modifier *= _type[1].dmgModifier(type);
        return modifier;
    }
    
    public void finTour(){
        if(_statut == EMPOISONNEMENT || _statut == BRULURE || _statut == VAMPIGRAINE){
            int pv = Math.min(_vie, (int) (_vieMax / 16.));
            this.modifierStatistique(VIE, -pv);
            if(_statut == VAMPIGRAINE && _cibleVampigraine != null){
                _cibleVampigraine.modifierStatistique(VIE, pv);
            }
        }
    }
    
    public void gererKO(){
        this.resetStats();
        this._vie = 0;
    }
    
    /********************************
    *   Méthodes basiques/utiles    *
    ********************************/
    
    private boolean chance(int chance){
        return (Math.random() * 100) <= chance;
    }
    
    private int borne(int min, int value, int max){
        return Math.max(min, Math.min(value, max));
    }
    
}
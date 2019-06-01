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

/**
 *
 * @author AdrienJaugey
 */
public class Pokemon extends Personnage{
    private TypePokemon _type1;
    private TypePokemon _type2;
    private String _nom;
    private String _surnom;
    private Attaque[] _attaques;
    
    //Valeur des stats de base
    private int _vieBase;
    private int _atqBase;
    private int _defBase;
    private int _atqSpeBase;
    private int _defSpeBase;
    private int _vitBase;
    
    //IVs
    private final int _vieIV;
    private final int _atqIV;
    private final int _defIV;
    private final int _atqSpeIV;
    private final int _defSpeIV;
    private final int _vitIV;
    
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
    private int _vieMax;
    private int _atqMax;
    private int _defMax;
    private int _atqSpeMax;
    private int _defSpeMax;
    private int _vitMax;
    
    public Pokemon(int id){
        this(id, 2);
    }
    
    public Pokemon(int id, int niveau){
        Pokedex pkdx = Pokedex.get();
        _nom = pkdx.getNom(id);
        int baseStats[] = pkdx.getBaseStats(id);
        _vieBase = baseStats[0];
        _atqBase = baseStats[1];
        _defBase = baseStats[2];
        _atqSpeBase = baseStats[3];
        _defSpeBase = baseStats[4];
        _vitBase = baseStats[5];
        _niveau = niveau;
        _surnom = null;
        _attaques = new Attaque[4];
        TypePokemon t[] = pkdx.getTypes(id);
        _type1 = t[0];
        _type2 = t[1];
        int IV[] = generateIV();
        _vieIV = IV[0];
        _atqIV = IV[1];
        _defIV = IV[2];
        _atqSpeIV = IV[3];
        _defSpeIV = IV[4];
        _vitIV = IV[5];
        this.updateMaxStats();
    }

    @Override
    public String toString() {
        String res = _nom + " (" + _type1 + (_type2 != null ? "/" + _type2 : "" ) + ") Niv. " + _niveau;
        res += "\n\tVie :\t " + _vie + "/" + _vieMax + "\t(" + _vieIV + ")";
        res += "\n\tAtq :\t " + _atqMax + "\t(" + _atqIV + ")";
        res += "\n\tDef :\t " + _defMax + "\t(" + _defIV + ")";
        res += "\n\tAtqSpe : " + _atqSpeMax + "\t(" + _atqSpeIV + ")";
        res += "\n\tDefSpe : " + _defSpeMax + "\t(" + _defSpeIV + ")";
        res += "\n\tVit :\t " + _vitMax + "\t(" + _vitIV + ")";
        res += "\nProchain niveau dans " + (this.getExpNextLvl() - _exp) + " exp.";
        return res;
    }
    
    

    public TypePokemon getType() {
        return _type1;
    }

    public String getNom() {
        return _nom;
    }

    public String getSurnom() {
        return _surnom;
    }

    public Attaque[] getAttaques() {
        return _attaques;
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
    
    public int ajouterExp(int exp){
        int lvlup = 0;
        if(exp > 0 && _niveau < 100){
            this._exp += exp;
            while(this._exp >= getExpNextLvl() && _niveau < 100){
                lvlup++;
                _niveau++;
            }
        }
        if(lvlup > 0) updateMaxStats();
        return lvlup;
    }
    
    /**
     * Permet d'obtenir la quantité d'experience nécessaire pour passer un _niveau
     * @return le nombre de niveaux passés
     */
    public int getExpNextLvl(){
        //Courbe rapide d'expérience 
        //https://www.pokepedia.fr/Courbe_d%27exp%C3%A9rience#La_courbe_.22rapide.22
        return (int)(0.8 * Math.pow(_niveau + 1, 3));
    }
    
    private int hpFormula(){
        return (int)(((2. * _vieBase + _vieIV) * _niveau) / 100 + _niveau + 10);
    }
    
    private int statFormula(int statBase, int statIV){
        return (int)(((2. * statBase + statIV) * _niveau) / 100 + 5);
    }
    
    private int[] generateIV(){
        int IV[] = new int[6];
        for(int i = 0; i < 31; i++){
            int index = (int)(Math.random() * 6);
            IV[index]++;
        }
        return IV;
    }
    
    private void updateMaxStats(){
       this._vieMax = hpFormula();
       this._atqMax = statFormula(_atqBase, _atqIV);
       this._defMax = statFormula(_defBase, _defIV);
       this._atqSpeMax = statFormula(_atqSpeBase, _atqSpeIV);
       this._defSpeMax = statFormula(_defSpeBase, _defSpeIV);
       this._vitMax = statFormula(_vitBase, _vitIV);
       resetCurrentStats();
    }

    private void resetCurrentStats() {
        this._vie = this._vieMax;
        this._atq = this._atqMax;
        this._def = this._defMax;
        this._atqSpe = this._atqSpeMax;
        this._defSpe = this._defSpeMax;
        this._vit = this._vitMax;
    }
    
}
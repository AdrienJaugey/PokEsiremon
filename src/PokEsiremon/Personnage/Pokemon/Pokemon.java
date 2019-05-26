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
    private TypePokemon type;
    private String nom;
    private String surnom;
    private Attaque attaques[];
    
    //Valeur des stats de base
    private int vieBase;
    private int atqBase;
    private int defBase;
    private int atqSpeBase;
    private int defSpeBase;
    private int vitBase;
    
    //Valeur des stats actuelles
    private int vie;
    private int atq;
    private int def;
    private int atqSpe;
    private int defSpe;
    private int vit;
    private int exp;
    private int niveau;
    
    //Valeur des stats Maximale
    private int vieMax;
    private int atqMax;
    private int defMax;
    private int atqSpeMax;
    private int defSpeMax;
    private int vitMax;
    
    public Pokemon(TypePokemon type, String nom){
        this(type, nom, 2);
    }
    
    public Pokemon(TypePokemon type, String nom, int niveau){
        this.type = type;
        this.nom = nom;
        generateBaseStats();
        this.niveau = niveau;
        updateMaxStats();
    }

    @Override
    public String toString() {
        String res = nom + " (" + type + ") Niv. " + niveau;
        res += "\nVie : " + vie + "/" + vieMax;
        res += "\n\tAtq : " + atqMax;
        res += "\n\tDef : " + defMax;
        res += "\n\tVit : " + vitMax;
        res += "\n\tAtqSpe : " + atqSpeMax;
        res += "\n\tDefSpe : " + defSpeMax;
        res += "\nProchain niveau dans " + (this.getExpNextLvl() - exp) + " exp.";
        return res;
    }
    
    

    public TypePokemon getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public String getSurnom() {
        return surnom;
    }

    public Attaque[] getAttaques() {
        return attaques;
    }

    public int getVie() {
        return vie;
    }

    public int getAtq() {
        return atq;
    }

    public int getDef() {
        return def;
    }

    public int getAtqSpe() {
        return atqSpe;
    }

    public int getDefSpe() {
        return defSpe;
    }

    public int getVitesse() {
        return vit;
    }
    
    public int getExp(){
        return exp;
    }
    
    public int getNiveau(){
        return niveau;
    }

    public int getVieMax() {
        return vieMax;
    }

    public int getAtqMax() {
        return atqMax;
    }

    public int getDefMax() {
        return defMax;
    }

    public int getAtqSpeMax() {
        return atqSpeMax;
    }

    public int getDefSpeMax() {
        return defSpeMax;
    }

    public int getVitesseMax() {
        return vitMax;
    }
    
    private int generateBaseStat(){
        return 30 + (int)(Math.random()*20);
    }
    
    private int generateBaseHP(){
        return 10 + (int)(Math.random()*20);
    }
    
    private void generateBaseStats(){
        this.vieBase = this.generateBaseHP();
        this.atqBase = this.generateBaseStat();
        this.defBase = this.generateBaseStat();
        this.atqSpeBase = this.generateBaseStat();
        this.defSpeBase = this.generateBaseStat();
        this.vitBase = this.generateBaseStat();
    }
    
    public int ajouterExp(int exp){
        int lvlup = 0;
        if(exp > 0 && niveau < 100){
            this.exp += exp;
            while(this.exp >= getExpNextLvl() && niveau < 100){
                lvlup++;
                niveau++;
            }
        }
        if(lvlup > 0) updateMaxStats();
        return lvlup;
    }
    
    /**
     * Permet d'obtenir la quantité d'experience nécessaire pour passer un niveau
     * @return le nombre de niveaux passés
     */
    public int getExpNextLvl(){
        //Courbe rapide d'expérience 
        //https://www.pokepedia.fr/Courbe_d%27exp%C3%A9rience#La_courbe_.22rapide.22
        return (int)(0.8 * Math.pow(niveau + 1, 3));
    }
    
    private int hpFormula(){
        return (int)((2. * vieBase * niveau) / 100 + niveau + 10);
    }
    
    private int statFormula(int stat){
        return (int)((2. * stat * niveau) / 100 + 5);
    }
    
    private void updateMaxStats(){
       this.vieMax = hpFormula();
       this.atqMax = statFormula(atqBase);
       this.defMax = statFormula(defBase);
       this.atqSpeMax = statFormula(atqSpeBase);
       this.defSpeMax = statFormula(defSpeBase);
       this.vitMax = statFormula(vitBase);
       resetCurrentStats();
    }

    private void resetCurrentStats() {
        this.vie = this.vieMax;
        this.atq = this.atqMax;
        this.def = this.defMax;
        this.atqSpe = this.atqSpeMax;
        this.defSpe = this.defSpeMax;
        this.vit = this.vitMax;
    }
    
}
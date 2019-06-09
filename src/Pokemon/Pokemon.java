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
import static Pokemon.Capacite.Enum_TypeAttaque.*;
import static Pokemon.Enum_Statistique.VIE;
import static Pokemon.Enum_Statut.*;
import static Pokemon.Enum_TypePokemon.*;
import java.util.ArrayList;

/**
 *
 * @author AdrienJaugey
 */
public class Pokemon{
    private final Enum_TypePokemon _type[];
    private final String _nom;
    private final int _id;
    private final Capacite[] _capacites;
    private final int[] _pp;
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
    
    /**
     * Constructeur de Pokemon
     * @param id l'id du pokémon à instancier
     */
    public Pokemon(int id){
        Pokedex pkdx = Pokedex.get();
        _nom = pkdx.getNomPkmn(id);
        _id = id;
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
        _pp = new int[4];
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
    
    /**
     * Permet d'obtenir la liste des capacités que peut apprendre ce pokémon
     * @return la liste des id des capacités
     */
    public ArrayList<Integer> getCapaciteUtilisable(){
        return Pokedex.get().getCapacitePokemon(_id);
    }
    
    /**
     * Permet de récupérer le(s) type(s) du pokémon
     * @return un tableau contenant le(s) type(s) (le deuxième peut être à null)
     */
    public Enum_TypePokemon[] getType() {
        return _type;
    }
    
    /**
     * Permet de récupérer le nom du pokémon
     * @return le nom du pokémon
     */
    public String getNom() {
        return _nom;
    }
    
    /**
     * Permet de récupérer un tableau contenant les capacités du pokémon
     * @return un tableau de capacités
     */
    public Capacite[] getCapacites() {
        return _capacites;
    }
    
    /**
     * Permet d'ajouter une capacité au pokémon à un emplacement choisi
     * @param capacite la capacité à ajouter
     * @param emplacement  l'emplacement où définir la capacité (entre 0 et 3)
     * @throws java.lang.Exception
     */
    public void setCapacite(Capacite capacite, int emplacement) throws Exception{
        if(emplacement < 0 || emplacement > 3) throw new Exception("Un pokémon n'a que 4 emplacements de capacité (entre 0 et 3)");
        this._capacites[emplacement] = capacite;
        this._pp[emplacement] = capacite.getPPMax();
    }
    
    /**
     * Permet d'ajouter une capacité à un pokémon à un emplacement donné
     * @param id l'id de la capacité à ajouter
     * @param emplacement l'emplacement où définir la capacité (entre 0 et 3)
     * @throws Exception 
     */
    public void setCapacite(int id, int emplacement) throws Exception{
        this.setCapacite(Pokedex.get().getCapacite(id), emplacement);
    }
    
    /**
     * Permet de récupérer la vie actuelle du pokémon
     * @return la vie actuelle du pokémon
     */
    public int getVie() {
        return _vie;
    }
    
    /**
     * Permet de récupérer la statistique d'attaque du pokémon
     * @return la valeur de la statistique
     */
    public int getAtq() {
        if(_statut == BRULURE) return _atq/2; 
        return _atq;
    }

    /**
     * Permet de récupérer la statistique de défense du pokémon
     * @return la valeur de la statistique
     */
    public int getDef() {
        return _def;
    }

    /**
     * Permet de récupérer la statistique d'attaque spéciale du pokémon
     * @return la valeur de la statistique
     */
    public int getAtqSpe() {
        return _atqSpe;
    }

    /**
     * Permet de récupérer la statistique de défense spéciale du pokémon
     * @return la valeur de la statistique
     */
    public int getDefSpe() {
        return _defSpe;
    }

    /**
     * Permet de récupérer la statistique de vitesse du pokémon
     * @return la valeur de la statistique
     */
    public int getVitesse() {
        if(_statut == PARALYSIE) return _vit/2;
        return _vit;
    }
    
    /**
     * Permet de récupérer le statut du pokémon
     * @return le statut du pokémon
     */
    public Enum_Statut getStatut() {
        return _statut;
    }

    /**
     * Permet de récupérer la précision du pokémon
     * @return la précision
     */
    public int getPrecision() {
        return _precision;
    }

    /**
     * Permet de récupérer l'esquive du pokémon
     * @return l'esquive du pokémon
     */
    public int getEsquive() {
        return _esquive;
    }
    
    /**
     * Permet d'obtenir le nombre de PP restant(s) pour une capacité
     * @param emplacement l'emplacement de la capacité dont on veut les PP
     * @return le nombre de PP restant(s)
     * @throws Exception 
     */
    public int getPPCapacite(int emplacement) throws Exception{
        if(_capacites[emplacement] == null) throw new Exception("Il n'y a pas de capacité ici");
        return _pp[emplacement];
    }
    
    /**
     * Permet de récupérer la valeur maximale de la vie du pokémon
     * @return la valeur maximale de la vie
     */
    public int getVieMax() {
        return _vieMax;
    }

    /**
     * Permet de récupérer la valeur maximale de la statistique d'attaque du pokémon
     * @return la valeur maximale de la statistique
     */
    public int getAtqMax() {
        return _atqMax;
    }

    /**
     * Permet de récupérer la valeur maximale de la statistique de défense du pokémon
     * @return la valeur maximale de la statistique
     */
    public int getDefMax() {
        return _defMax;
    }

    /**
     * Permet de récupérer la valeur maximale de la statistique d'attaque spéciale du pokémon
     * @return la valeur maximale de la statistique
     */
    public int getAtqSpeMax() {
        return _atqSpeMax;
    }

    /**
     * Permet de récupérer la valeur maximale de la statistique de défense spéciale du pokémon
     * @return la valeur maximale de la statistique
     */
    public int getDefSpeMax() {
        return _defSpeMax;
    }

    /**
     * Permet de récupérer la valeur maximale de la vitesse du pokémon
     * @return la valeur maximale de la statistique
     */
    public int getVitesseMax() {
        return _vitMax;
    }
    
    /**
     * Permet de retirer ou ajouter une quantité donnée à une statistique du pokémon
     * @param stat la statistique à modifier
     * @param delta la quantité à ajouter ou retirer
     * @return une description de la modification apportée
     */
    public String modifierStatistique(Enum_Statistique stat, int delta){
        String res = "";
        if(delta > 0) {
            res = stat.toString() + " de " + _nom + " augmente.";
        } else if(delta < 0){
            res = stat.toString() + " de " + _nom + " diminue.";
        }
        switch(stat){
            case VIE : {
                _vie = Utils.borne(0, _vie + delta, _vieMax);
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
                _atq = Utils.borne((int)(0.25*_atqMax), _atq + delta, 4 * _atqMax);
            } break;
            case DEF : {
                _def = Utils.borne((int)(0.25*_defMax), _def + delta, 4 * _defMax);
            } break;
            case ATQSPE : {
                _atqSpe = Utils.borne((int)(0.25 * _atqSpeMax), _atqSpe + delta, 4 * _atqSpeMax);
            } break;
            case DEFSPE : {
                _defSpe = Utils.borne((int)(0.25 * _defSpeMax), _defSpe + delta, 4 * _defSpeMax);
            } break;
            case VITESSE : {
                _vit = Utils.borne((int)(0.25 * _vitMax), _vit + delta, 4 * _vitMax);
            } break;
            case PRECISION : {
                _precision = Utils.borne(33, _precision + delta, 300);
            } break;
            case ESQUIVE : {
                _esquive = Utils.borne(33, _esquive + delta, 300);
            } break;
        }
        return res;
    }
    
    /**
     * Permet de retirer ou ajouter un pourcentage à une statistique d'un pokémon
     * @param stat la statistique à modifier
     * @param modifier le pourcentage à ajouter ou retirer
     * @return une description de la modification apportée
     */
    public String modifierStatistique(Enum_Statistique stat, double modifier){
        String res = "";
        if(modifier > 0) {
            res = stat.toString() + " de " + _nom + " augmente.";
        } else if(modifier < 0){
            res = stat.toString() + " de " + _nom + " diminue.";
        }
        switch(stat){
            case VIE : {
                _vie = Utils.borne(0, (int)  Math.round(_vie * (1 + modifier)), _vieMax);
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
                _atq = Utils.borne((int)(0.25*_atqMax), (int)  Math.round(_atq  * (1 + modifier)), 4 * _atqMax);
            } break;
            case DEF : {
                _def = Utils.borne((int)(0.25*_defMax), (int)  Math.round(_def  * (1 + modifier)), 4 * _defMax);
            } break;
            case ATQSPE : {
                _atqSpe = Utils.borne((int)(0.25 * _atqSpeMax), (int)  Math.round(_atqSpe  * (1 + modifier)), 4 * _atqSpeMax);
            } break;
            case DEFSPE : {
                _defSpe = Utils.borne((int)(0.25 * _defSpeMax), (int)  Math.round(_defSpe  * (1 + modifier)), 4 * _defSpeMax);
            } break;
            case VITESSE : {
                _vit = Utils.borne((int)(0.25 * _vitMax), (int)  Math.round(_vit * (1 + modifier)), 4 * _vitMax);
            } break;
            case PRECISION : {
                _precision = Utils.borne(33, (int)  Math.round(_precision  * (1 + modifier)), 300);
            } break;
            case ESQUIVE : {
                _esquive = Utils.borne(33, (int)  Math.round(_esquive  * (1 + modifier)), 300);
            } break;
        }
        return res;
    }
        
    /**
     * Permet de calculer la vie d'un pokémon au niveau 100 suivant sa vie de
     * base et un nombre aléatoire qui lui est attribué
     * @param vieBase La valeur de la statistique de vie de base
     * @param dv La valeur générée aléatoirement
     * @return la quantité de vie du pokémon au niveau 100
     */
    private int hpFormula(int vieBase, int dv){
        return (int)((vieBase + dv + 82)*2 + 10);
    }
    
    /**
     * Permet de connaître la quantité maximale d'une statitstique d'un pokémon
     * au niveau 100 à partir de la valeur de base de cette statistique ainsi
     * que d'une valeur générée aléatoirement.
     * @param statBase La valeur de base de la statistique
     * @param dv La valeur générée aléatoirement
     * @return La quantité de la statistique du pokémon au niveau 100
     */
    private int statFormula(int statBase, int dv){
        return (int)((statBase + dv + 32)*2 + 5);
    }
    
    /**
     * Permet de générer les Determinant Value (DV) qui servent à différencier
     * deux même pokémons à un niveau similaire en agissant plus ou moins sur 
     * l'évolution de leurs statistiques.
     * @return un tableau contenant les 6 DV du pokémon
     */
    private int[] generateDV(){
        int DV[] = new int[6];
        for(int i = 0; i < 6; i++){
            DV[i] = (int)(Math.random() * 15);
        }
        return DV;
    }

    /**
     * Permet de réinitialiser les statistiques actuelles d'un pokémon ainsi que
     * sont statut
     */
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
        for(int i = 0; i < 4; i++){
            if(_capacites[i] == null) continue;
            _pp[i] = _capacites[i].getPPMax();
        }
    }
    
    /**
     * Permet de savoir si un pokémon est d'un type ou non
     * @param type le type à savoir
     * @return true s'il est de ce type, false sinon
     */
    private boolean isType(Enum_TypePokemon type){
        return (_type[0] == type || _type[1] == type);
    }
    
    /***
     * Permet de donner un statut à un pokémon
     * @param statut le statut à donner
     * @return Une phrase descriptive
     */
    public String setStatut(Enum_Statut statut){
        return this.setStatut(statut, null);
    }
    
    /**
     * Permet de donner un statut à un pokémon
     * @param statut le statut à donner
     * @param cibleVampigraine le pokémon lanceur (utilisé pour Vampigraine)
     * @return Une phrase descriptive
     */
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
    
    /**
     * Permet de retirer le statut d'un pokémon
     * @return Une phrase descriptive
     */
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
    
    /**
     * Permet de réinitialiser le pokémon qui a lancé Vampigraine.
     * Typiquement, lorce que celui-ci est retiré du combat ou est K.O.
     */
    public void resetCibleVampigraine(){
        _cibleVampigraine = null;
    }
    
    /**
     * Permet de gérer les actions de début de tour
     */
    public void debutTour(){
        if(_statut == SOMMEIL || _statut == CONFUSION){
            _tourStatut--;
            if(_tourStatut == 0) _statut = NEUTRE;
        } else if(_statut == GEL){
            if(Utils.chance(20)) _statut = NEUTRE;
        }
    }
    
    /**
     * Le pokémon tente d'utiliser la capacité se trouvant en position 
     * choixCapacite sur le pokémon cible
     * @param choixCapacite la capacité à utiliser
     * @param cible le pokémon adversaire même si cela n'agit pas sur lui
     * @return Une phrase descriptive
     * @throws java.lang.Exception
     */
    public String utiliserCapacite(int choixCapacite, Pokemon cible) throws Exception{
        if(_capacites[choixCapacite] == null) throw new Exception("Il n'y a pas de capacité ici");
        String res = this._nom + " lance " + _capacites[choixCapacite].getNom() + ".";
        if (_pp[choixCapacite] > 0) {
            switch (_statut) {
                case SOMMEIL: {
                    res = this._nom + " n'a pas pu attaquer.";
                }
                break;
                case GEL: {
                    if (_capacites[choixCapacite].getTypePkmn() == FEU) {
                        this.resetStatut();
                        res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                        _pp[choixCapacite]--;
                    } else {
                        res = this._nom + " n'a pas pu attaquer";
                    }
                }
                break;
                case PARALYSIE: {
                    if (Utils.chance(75)) {
                        res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                        _pp[choixCapacite]--;
                    } else {
                        res = this._nom + " n'a pas pu attaquer.";
                    }
                }
                break;
                case CONFUSION: {
                    if (Utils.chance(33)) {
                        int pvPerdus = (int) Math.round((((42. * this.getAtq() * 40) / (this.getDef() * 50.)) + 2) * (0.85 + Math.random() * 0.15));                        
                        this.modifierStatistique(VIE, -pvPerdus);
                        res = this._nom + " s'est blessé dans sa confusion.";
                    } else {
                        res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                        
                    }
                    _pp[choixCapacite]--;
                }
                break;
                default: {
                    res += "\n" + _capacites[choixCapacite].utiliser(this, cible);
                    _pp[choixCapacite]--;
                }
                break;
            }
        } else {
            res = _nom + " n'a pas pu attaquer";
        }
        return res;
    }
    
    /**
     * Permet de calculer et retirer la vie qu'un pokémon perd face à une capacité
     * @param capa la capacité que subit le pokémon
     * @param lanceur le pokémon lanceur de la capacité
     * @return Une phrase descriptive
     */
    public String subir(Capacite capa, Pokemon lanceur){
        /*
                        ( 42 * AtqLanceur * PuiCapacité    )
            PVPerdus = (  ----------------------------- + 2 ) * STAB ? * Efficacité type * Crit(1.5) ? * Alea(0.85 - 1)
                        (       DefCible * 50              )
        */
        String res = "";
        int attaque, defense, puissance = capa.getPuissance();
        if(capa.getTypeAtq() == PHYSIQUE){
            attaque = lanceur.getAtq();
            defense = this.getDef();
        } else {
            attaque = lanceur.getAtqSpe();
            defense = this.getDefSpe();
        }
        double modifierSTAB = (lanceur.isType(capa.getTypePkmn()) ? 1.5 : 1);
        double modifierType = (capa.getTypePkmn() != null ? this.getModifier(capa.getTypePkmn()) : 1);
        if(modifierType <= 0.5) res+= "Ce n'est pas très efficace\n";
        else if(modifierType >= 2.) res += "C'est très efficace !\n";
        double chanceCrit = Pokedex.get().getBaseStatsPkmn(_id)[5];
        chanceCrit = (chanceCrit%2 == 0 ? chanceCrit : chanceCrit + 1) / 2;
        chanceCrit *= capa.getModifierCrit();
        chanceCrit = (chanceCrit / 255.) * 100;
        double modifierCrit = (Utils.chance((int)chanceCrit) ? 1.5 : 1);
        if(modifierCrit == 1.5) res += "Coup critique !\n"; 
        double modifierAlea = 0.85 + Math.random() * 0.15;
        int pvPerdus = (int) Math.round((((42. * attaque * puissance) / (defense * 50.)) + 2) * modifierSTAB * modifierType * modifierCrit * modifierAlea); 
        this.modifierStatistique(VIE, -pvPerdus);
        return res;
    }
    
    /**
     * Permet de calculer et retourner le coefficient de dégat d'une capacité
     * selon l'efficacité du type de celle-ci face aux types du pokémon qui la
     * reçoit
     * @param type le type de la capacité
     * @return le coefficient de modification de dégats
     */
    private double getModifier(Enum_TypePokemon type){
        double modifier = 1;
        modifier *= _type[0].dmgModifier(type);
        if(_type[1] != null) modifier *= _type[1].dmgModifier(type);
        return modifier;
    }
    
    /**
     * Permet de gérer les actions de fin de tour (par ex. la perte de vie à 
     * cause d'un statut...)
     */
    public void finTour(){
        if(_statut == EMPOISONNEMENT || _statut == BRULURE || _statut == VAMPIGRAINE){
            int pv = Math.min(_vie, (int) (_vieMax / 16.));
            this.modifierStatistique(VIE, -pv);
            if(_statut == VAMPIGRAINE && _cibleVampigraine != null){
                _cibleVampigraine.modifierStatistique(VIE, pv);
            }
        }
    }
    
    /**
     * Gère les actions lors d'un KO
     */
    public void gererKO(){
        this.resetStats();
        this._vie = 0;
    }
    
    /**
     * Permet de savoir si un pokémon est KO ou non
     * @return true si le pokémon est KO, false sinon
     */
    public boolean isKO() {
        return this.getVie() <= 0;
    }
    
}
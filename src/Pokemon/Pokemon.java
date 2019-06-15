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
    
    private Capacite _capaEnAttente;
    private boolean _tourAttente;
    private boolean _tourRepos;
    
    private final int _capaciteBloquee[];
    private boolean _peur;
    private boolean _brume;
    private int _vieClone;
    private int _dernierDegats;
    private Capacite _derniereCapaUtilisee;
    private Capacite _copie;
    private int _ppCopie;
    private int _emplacementCopie;
    
    
    //Valeur des stats actuelles
    private int _vie;
    
    private final int[] _niveauStat;
    private final double[] _niveauStat1 = { 0.25, 0.29, 0.33, 0.4, 0.5, 0.67, 1, 1.5, 2, 2.5, 3, 3.5, 4 };
    private final int[] _niveauStat2 = { 33, 38, 43, 50, 60, 75, 100, 133, 167, 200, 233, 267, 300 };
    
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
        _peur = false;
        _brume = false;
        this.resetClone();
        _dernierDegats = 0;
        _derniereCapaUtilisee = null;
        resetCopie();
        int baseStats[] = pkdx.getBaseStatsPkmn(id);
        int dv[] = generateDV();
        _vieMax = hpFormula(baseStats[0], dv[0]);
        _atqMax = statFormula(baseStats[1], dv[1]);
        _defMax = statFormula(baseStats[2], dv[2]);
        _atqSpeMax = statFormula(baseStats[3], dv[3]);
        _defSpeMax = statFormula(baseStats[4], dv[4]);
        _vitMax = statFormula(baseStats[5], dv[5]);
        _niveauStat = new int[7];
        for(int i = 0; i < 7; i++) _niveauStat[i] = 0;
        _capacites = new Capacite[4];
        _capaciteBloquee = new int[4];
        for(int i = 0; i < 4; i++) _capaciteBloquee[i] = 0;
        _pp = new int[4];
        _type = pkdx.getTypesPkmn(id);
        this._cibleVampigraine = null;
        this.resetStats();
        
        _capaEnAttente = null;
        _tourAttente = false;
        _tourRepos = false;
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

    /************************
    *   Getters Attributs   *
    ************************/
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
        if(_copie != null && _emplacementCopie >= 0 && _emplacementCopie < 4){
            Capacite[] res = _capacites;
            res[_emplacementCopie] = _copie;
            return res;
        }        
        return _capacites;
    }
    
    /**
     * Permet de récupérer le(s) type(s) du pokémon
     * @return un tableau contenant le(s) type(s) (le deuxième peut être à null)
     */
    public Enum_TypePokemon[] getType() {
        return _type;
    }
    
    /**
     * Permet de récupérer la vie actuelle du pokémon
     * @return la vie actuelle du pokémon
     */
    public int getVie() {
        return _vie;
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
     * Permet de récupérer le statut du pokémon
     * @return le statut du pokémon
     */
    public Enum_Statut getStatut() {
        return _statut;
    }

    /**
     * Permet de récupérer la dernière quantité de dégats perdus
     * @return les dégats perdus
     */
    public int getDernierDegats(){
        return _dernierDegats;
    }
    
    /**
     * Permet de récupérer la capacité d'un emplacement
     * @param emplacement l'emplacement de la capacité (entre 0 et 3)
     * @return la capacité
     * @throws Exception 
     */
    public Capacite getCapacite(int emplacement) throws Exception{
        if(emplacement < 0 || emplacement > 3) throw new Exception("Les emplacements de capacités sont entre 0 et 3.");
        if(emplacement == _emplacementCopie && _copie != null)return _copie;
        if(_capacites[emplacement] == null) throw new Exception("Il n'y a pas de capacité ici");
        else return _capacites[emplacement];
    }
    
    /**
     * Permet d'obtenir le nombre de PP restant(s) pour une capacité
     * @param emplacement l'emplacement de la capacité dont on veut les PP
     * @return le nombre de PP restant(s)
     * @throws Exception 
     */
    public int getPPCapacite(int emplacement) throws Exception{
        if(emplacement < 0 || emplacement > 3) throw new Exception("Les emplacements de capacités sont entre 0 et 3.");
        if(emplacement == _emplacementCopie && _copie != null) return _ppCopie;
        else return _pp[emplacement];
    }
    
    /**
     * Permet de retirer un PP à une capacité
     * @param emplacement l'emplacement de la capacité (entre 0 et 3)
     * @throws Exception 
     */
    public void baisserPPCapacite(int emplacement) throws Exception{
        if(emplacement < 0 || emplacement > 3) throw new Exception("Les emplacements de capacités sont entre 0 et 3.");
        if(emplacement == _emplacementCopie && _copie != null) _ppCopie = Math.max(0, _ppCopie - 1);
        else _pp[emplacement] = Math.max(0, _pp[emplacement] - 1);
    }
    
    /**
     * Permet d'obtenir le coefficient multiplicateur d'une statistique
     * @param idStat numéro entre 0 et 6 (Atq, Def, AtqSpe, DefSpe, Vit, Pre, Esq)
     * @return le coefficient
     */
    private double getNiveauStat(int idStat){
        if(idStat >= 5) return _niveauStat2[6 + _niveauStat[idStat]];
        else return _niveauStat1[6 + _niveauStat[idStat]];
    }
    
    /**
     * Permet de récupérer la dernière capacité utilisée par le pokémon
     * @return la capacite
     */
    public Capacite getDerniereCapacite(){
        return _derniereCapaUtilisee;
    }
    
    /**************************
    *   Getters Propriétés    *
    **************************/
    /**
     * Permet d'obtenir la liste des capacités que peut apprendre ce pokémon
     * @return la liste des id des capacités
     */
    public ArrayList<Integer> getCapaciteUtilisable(){
        return Pokedex.get().getCapacitePokemon(_id);
    }
    
    /**
     * Permet de récupérer la statistique d'attaque du pokémon
     * @return la valeur de la statistique
     */
    public int getAtq() {
        double atq = getNiveauStat(0) * _atqMax;
        if(_statut == BRULURE) atq /= 2; 
        return (int)Math.round(atq);
    }

    /**
     * Permet de récupérer la statistique de défense du pokémon
     * @return la valeur de la statistique
     */
    public int getDef() {
        double def = getNiveauStat(1) * _defMax;
        return (int)Math.round(def);
    }

    /**
     * Permet de récupérer la statistique d'attaque spéciale du pokémon
     * @return la valeur de la statistique
     */
    public int getAtqSpe() {
        double atqSpe = getNiveauStat(2) * _atqSpeMax;
        return (int)Math.round(atqSpe);
    }

    /**
     * Permet de récupérer la statistique de défense spéciale du pokémon
     * @return la valeur de la statistique
     */
    public int getDefSpe() {
        double defSpe = getNiveauStat(3) * _defSpeMax;
        return (int)Math.round(defSpe);
    }

    /**
     * Permet de récupérer la statistique de vitesse du pokémon
     * @return la valeur de la statistique
     */
    public int getVitesse() {
        double vitesse = getNiveauStat(4) * _vitMax;
        if(_statut == PARALYSIE)  vitesse /= 2;
        return (int)Math.round(vitesse);
    }
    
    /**
     * Permet de récupérer la précision du pokémon
     * @return la précision
     */
    public int getPrecision() {
        return (int)getNiveauStat(5);
    }

    /**
     * Permet de récupérer l'esquive du pokémon
     * @return l'esquive du pokémon
     */
    public int getEsquive() {
        return (int)getNiveauStat(6);
    }
    
    /**
     * Permet de définir la dernière capacité utlisée
     * @param capa la dernière capacité
     */
    public void setDerniereCapacite(Capacite capa){
        _derniereCapaUtilisee = capa;
    }
    
    /*****************************
    *  Gestion Stats et Statut   *
    *****************************/
    
    @Override
    public String toString() {
        String res = _nom + " (" + _type[0] + (_type[1] != null ? "/" + _type[1] : "" ) + ") Niv. 100";
        res += "\n\tVie :\t " + getVie() + "/" + _vieMax;
        res += "\n\tAtq :\t " + getAtq() + "/" + _atqMax;
        res += "\n\tDef :\t " + getDef() + "/" + _defMax;
        res += "\n\tAtqSpe : " + getAtqSpe() + "/" + _atqSpeMax;
        res += "\n\tDefSpe : " + getDefSpe() + "/" + _defSpeMax;
        res += "\n\tVit :\t " + getVitesse() + "/" + _vitMax;
        return res;
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
        this._capaciteBloquee[emplacement] = 0;
        if(capacite.getNom().equals("Copie")) _emplacementCopie = emplacement;
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
     * Permet de retirer ou ajouter une quantité donnée à une statistique du pokémon
     * @param stat la statistique à modifier
     * @param delta la quantité à ajouter ou retirer
     * @return une description de la modification apportée
     */
    public String modifierStatistique(Enum_Statistique stat, int delta){
        String res = stat.toString();
        switch(delta){
            case -3: res += " diminue énormément."; break;
            case -2: res += " diminue beaucoup."; break;
            case -1: res += " diminue."; break;
            case 1:  res += " augmente."; break;
            case 2:  res += " augmente beaucoup."; break;
            case 3:  res += " augmente énormément."; break;
        }
        switch(stat){
            case VIE : {
                if(delta < 0) res = perdreVie(-delta);
                else {
                    res = _nom + " récupère " + delta + " PV.";
                    _vie = Math.min(_vieMax, _vie + delta);
                }
            } break;
            default: 
                if(!isThereClone() && !_brume){
                    _niveauStat[stat.ordinal() - 1] = Utils.borne(-6, _niveauStat[stat.ordinal() - 1] + delta, 6);
                } else {
                    if(_brume){
                        res = "Aucun changement de statistiques ne peut affecter " + _nom + ".";
                    } else if(isThereClone()){
                        res = "Cela affecte le clone.";
                    }
                }
        }
        return res;
    }
    
    /**
     * Permet de retirer ou ajouter un pourcentage à une statistique d'un pokémon
     * @param stat la statistique à modifier
     * @param modifier le pourcentage à ajouter ou retirer
     * @return une description de la modification apportée
     * @throws java.lang.Exception
     */
    public String modifierStatistique(Enum_Statistique stat, double modifier) throws Exception{
        if(stat == VIE){
            int deltaPV = (int) Math.round(getVie() * (1 + modifier) - getVie());
            return modifierStatistique(stat, deltaPV);
        } else throw new Exception("Cas non géré");
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
        //la confusion et vampigraine sont censés être des statuts secondaires 
        //(i.e. ils peuvent subvenir en même temps qu'un statut principal (gel...)
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
     * Gère la perte de vie
     * @param degats la vie à retirer (>0)
     * @return Une phrase descriptive
     */
    public String perdreVie(int degats){
        String res = "";
        if(isThereClone()){
            _vieClone -= degats;
            _dernierDegats = degats;
            if(_vieClone < 0){
                this.resetStatut();
                _vieClone = 0;
                _dernierDegats = 0;
                res += "Le clone a disparu";
            } else {
                res += "Le clone a absorbé les dégats";
            }
        } else {
            _dernierDegats = Math.min(_vie, degats);
            _vie -= degats;
            if(isKO()){
                gererKO();
                res = _nom + " est KO";
            } else {
                res = _nom + " perd " + _dernierDegats + " PV";
            }
        }
        return res;
    }
    
    
    /*****************************
    *   Gestion des capacités    *
    *****************************/
    /**
     * Le pokémon tente d'utiliser la capacité se trouvant en position 
     * choixCapacite sur le pokémon cible
     * @param choixCapacite la capacité à utiliser
     * @param cible le pokémon adversaire même si cela n'agit pas sur lui
     * @return Une phrase descriptive
     * @throws java.lang.Exception
     */
    public String utiliserCapacite(int choixCapacite, Pokemon cible){
        String res;
        
        /****************
        * N'attaque pas *
        ****************/
        //Si le pokémon doit se reposer
        if(_tourRepos) {
            _tourRepos = false;
            return _nom + " se repose.";
        }
        //Si le pokémon est apeuré, il n'attaque pas
        if(_peur) return _nom + " est apeuré, il n'a pas pu attaquer.";
        //Si la capacité est bloquée mais que le pokémon a encore d'autres capacités possibles
        if (_capaciteBloquee[choixCapacite] != 0 && peutEncoreCombattre()){
            return _nom + " n'a pas pu attaquer, la capacité est bloquée.";
        }
        
        
        /*******************
        * Fait autre chose * 
        *******************/
        //Vient d'attendre pour lancer une capacité
        if(_tourAttente && _capaEnAttente != null){
            _tourAttente = false;
            res = this._nom + " poursuit " + _capaEnAttente.getNom() + ".";
            res += "\n" + _capaEnAttente.utiliser(this, cible);
            _capaEnAttente = null;
            return res;
        }
        
        
        /********************************
        * Essaye de lancer une capacité *
        ********************************/
        Capacite capa; 
        
        if(!peutEncoreCombattre()){                     //Ne peut plus combattre
            capa = Pokedex.get().getCapacite(0);
        } else {                                        //Peut combattre
            try {
                capa = getCapacite(choixCapacite);
                if (getPPCapacite(choixCapacite) <= 0) {
                    return _nom + " n'a pas pu attaquer.";
                }
                baisserPPCapacite(choixCapacite);
            } catch (Exception e) {
                return _nom + " n'a pas pu attaquer.";
            }
        }
              
        //Si le pokémon ne peut attaquer à cause du statut
        if(!canAttack(capa)) {
            //Si c'est à cause de la confusion, il se blesse
            if(_statut == CONFUSION) {
                int pvPerdus = (int) Math.round((((42. * this.getAtq() * 40) / (this.getDef() * 50.)) + 2) * (0.85 + Math.random() * 0.15));                                
                this.modifierStatistique(VIE, -pvPerdus);
                return _nom + " s'est blessé dans sa confusion.";
            }
            return this._nom + " n'a pas pu attaquer.";
        }
        
        //Le pokémon utilise la capacité
        res = this._nom + " lance " + capa.getNom() + ".";
        //Si la capacité nécessite un tour d'attente
        if(capa.tourAttente()){
            _tourAttente = true;
            _capaEnAttente = capa;
            res += "\n" + _nom + " se prépare.";
            return res;
        }
        _tourRepos = capa.tourRepos();
        //S'il est gelé et qu'il peut attaquer, la capacité est donc de type feu, il dégèle
        if(_statut == GEL) res += this.resetStatut();
        res += "\n" + capa.utiliser(this, cible);
        
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
     * Permet de calculer la chance d'asséner un coup critique
     * @param capa la capacité utilisée
     * @return la chance de coup critique
     */
    private int getChanceCrit(Capacite capa){
        double chanceCrit = Pokedex.get().getBaseStatsPkmn(_id)[5];
        chanceCrit = (chanceCrit%2 == 0 ? chanceCrit : chanceCrit + 1) / 2;
        chanceCrit *= capa.getModifierCrit();
        chanceCrit = (chanceCrit / 255.) * 100;
        return (int) chanceCrit;
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
        if(modifierType <= 0.5) res+= "Ce n'est pas très efficace";
        else if(modifierType >= 2.) res += "C'est très efficace !";
        
        double modifierCrit = (Utils.chance(getChanceCrit(capa)) ? 1.5 : 1);
        if(modifierCrit == 1.5) res += "Coup critique !"; 
        
        double modifierAlea = 0.85 + Math.random() * 0.15;
        
        int pvPerdus = (int) Math.round((((42. * attaque * puissance) / (defense * 50.)) + 2) * modifierSTAB * modifierType * modifierCrit * modifierAlea); 
        res += "\n" + this.modifierStatistique(VIE, -pvPerdus);
        return res;
    }
    
    
    /**********************************
    *   Gestion d'un tour de combat   *
    **********************************/
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
     * Permet de gérer les actions de fin de tour (par ex. la perte de vie à 
     * cause d'un statut...)
     */
    public void finTour(){
        for(int i = 0; i < 4; i++){
            if(_capaciteBloquee[i] > 0) _capaciteBloquee[i]--;
        }
        _peur = false;
        if(_statut == EMPOISONNEMENT || _statut == BRULURE || _statut == VAMPIGRAINE){
            int pv = Math.min(_vie, (int) (_vieMax / 16.));
            this.perdreVie(pv);
            if(_statut == VAMPIGRAINE && _cibleVampigraine != null){
                _cibleVampigraine.modifierStatistique(VIE, pv);
            }
        }
    }
    
    
    /************************************
    *   Gestion du retrait du combat    *
    ************************************/
    /**
     * Gère le comportement au renvoit du pokémon dans le sac
     */
    public void retourSac(){
        _brume = false;
        _peur = false;
        this.resetCibleVampigraine();
        if(_statut == VAMPIGRAINE) _statut = NEUTRE;
        resetType();
        resetClone();
        resetCopie();
    }
    
    /**
     * Gère les actions lors d'un KO
     */
    public void gererKO(){
        this.resetStats();
        this.retourSac();
        this._vie = 0;
    }
    
    
    /********************************************
    *   Méthodes Activation Effets Spéciaux     *
    *********************************************/
    /**
     * Permet d'activer la peur, le pokémon n'attaquera pas ce tour si ce n'est
     * pas encore fait
     * @return Une phrase descriptive
     */
    public String apeurer(){
        _peur = true;
        return _nom + " est apeuré.";
    }
    
    /**
     * Permet d'empêcher les changements de statuts
     * @return Une phrase descriptive
     */
    public String activerBrume(){
        _brume = true;
        return "Une brume se lève, aucun changement de statistiques ne peut affecter " + _nom + ".";
    }
    
    /**
     * Retire tous les changements de statistiques, de statut si ce n'est pas
     * l'adversaire et annule les effets de brume
     * @return Une description de ce qui s'est passé
     */
    public String bueeNoire(Pokemon lanceur) {
        String res = "Les changements de statistiques de " + _nom + " sont annulés.";
        for (int i = 0; i < _niveauStat.length; i++) {
            _niveauStat[i] = 0;
        }
        if(this != lanceur) res += "\n" + this.resetStatut();
        if(_brume){
            _brume = false;
            res += "\nLes effets de brume se dissipent.";
        }
        return res;
    }
    
    /**
     * Permet de copier le(s) type(s) d'un pokémon autre
     * @param aCopier le pokémon dont on copie le(s) type(s)
     */
    public void copierType(Pokemon aCopier){
        this._type[0] = aCopier._type[0];
        this._type[1] = aCopier._type[1];
    }

    /**
     * Permet de créer un clone d'un quart de vie max du pokémon qui prend les 
     * dégats à la place du pokémon.
     */
    public void creerClone() {
        _vieClone = _vieMax/4;
    }
    
    /**
     * Permet de copier la dernière attaque utilisée par un pokémon
     * @param aCopier le pokémon dont on copie la dernière capacité utilisée
     * @return Une phrase descriptive
     */
    public String copierCapacite(Pokemon aCopier){
        if(aCopier._derniereCapaUtilisee != null){
            _copie = aCopier._derniereCapaUtilisee;
            _ppCopie = 10;
            return _nom + " apprend " + _copie.getNom() + ".";
        } else {
            return "Copie a échoué.";
        }
    }
    
    public String bloquerCapacite(int minTour, int maxTour){
        String res = "";
        if(_derniereCapaUtilisee != null){
            res = _derniereCapaUtilisee.getNom();
            try {
                int aBloquer = -1;
                int i = 0;
                while(i < 4 && aBloquer == -1){
                    if(_capacites[i] != null && getCapacite(i).getNom().equals(res)) aBloquer = i;
                    i++;
                }
                if(aBloquer != -1 && _capaciteBloquee[aBloquer] == 0){
                    _capaciteBloquee[aBloquer] = minTour + (int)(Math.random() * (maxTour - minTour));
                    res += " est entravé(e)."; 
                }
                else res = "L'entrave a échouée.";
            } catch (Exception ex) { 
                res = "L'entrave a échoué.";
            }
        }
        return res;
    }
    
    /**
     * Permet de soigner les changements de statut et 
     * @return 
     */
    public String soigner(){
        this.resetStatut();
        this._vie = _vieMax;
        return _nom + " a été soigné";
    }
    
    
    /*************
    *   Resets   *
    *************/
    /**
     * Permet de réinitialiser les statistiques actuelles d'un pokémon ainsi que
     * sont statut
     */
    private void resetStats() {
        this._vie = this._vieMax;
        for(int i = 0; i < 7; i++) _niveauStat[i] = 0;
        this._statut = NEUTRE;
        this._tourStatut = 0;
        this.resetCibleVampigraine();
        for(int i = 0; i < 4; i++){
            if(_capacites[i] == null) continue;
            _pp[i] = _capacites[i].getPPMax();
        }
    }
    
    /**
     * Permet de retirer le statut d'un pokémon
     * @return Une phrase descriptive
     */
    public String resetStatut(){
        String res = _nom + " n'est plus ";
        switch(_statut){
            case BRULURE: { res += "brûlé."; } break;
            case GEL: { res += "gelé."; } break;
            case PARALYSIE: { res += "paralysé."; } break;
            case EMPOISONNEMENT: { res += "empoisonné."; } break;
            case SOMMEIL: { res = _nom + " se réveille."; } break;
            case CONFUSION: { res += "confus."; } break;
            case VAMPIGRAINE: { res += "infecté."; } break;
        }
        _statut = NEUTRE;
        _tourStatut = 0;
        resetCibleVampigraine();
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
     * Permet de reset un changement de type
     */
    public void resetType(){
        Enum_TypePokemon type[] = Pokedex.get().getTypesPkmn(_id);
        _type[0] = type[0];
        _type[1] = type[1];
    }
    
    /**
     * Permet de réinitialiser la copie d'une attaque
     */
    public final void resetCopie(){
        _copie = null;
        _ppCopie = 0;
    }
    
    /**
     * Permet de réinitialiser le clone
     */
    public final void resetClone(){
        _vieClone = 0;
    }
    
    
    /************************
    *   Méthodes de tests   *
    ************************/
    /**
     * Permet de savoir si un pokémon peut attaquer
     * @param capa la capacité a utiliser
     * @return true s'il peut, false sinon
     */
    public boolean canAttack(Capacite capa){
        boolean res = true;
        switch(_statut){
            case GEL: res = capa.getTypePkmn() == FEU; break;
            case PARALYSIE: res = Utils.chance(75); break;
            case CONFUSION: res = Utils.chance(67); break;
        }
        return res;
    }
    
    /**
     * Permet de savoir si la capacité se trouvant à un emplacement est bloquée
     * ou non
     * @param emplacement l'emplacement de la capacité (entre 0 et 3)
     * @return true si elle est bloquée, false sinon
     * @throws Exception 
     */
    public boolean capaciteBloquee(int emplacement) throws Exception{
        return getPPCapacite(emplacement) == 0 || _capaciteBloquee[emplacement] > 0;
    }
    
    /**
     * Permet de savoir si un pokémon est d'un type ou non
     * @param type le type à savoir
     * @return true s'il est de ce type, false sinon
     */
    private boolean isType(Enum_TypePokemon type){
        return (_type[0] == type || _type[1] == type);
    }
    
    /**
     * Permet de savoir si le pokémon est apeuré
     * @return true s'il l'est, false sinon
     */
    public boolean isAffraid(){
        return _peur;
    }
    
    /**
     * Permet de savoir si un pokémon est KO ou non
     * @return true si le pokémon est KO, false sinon
     */
    public boolean isKO() {
        return this.getVie() <= 0;
    }
    
    /**
     * Permet de savoir s'il y a un clone d'actif
     * @return true si oui, false sinon
     */
    public boolean isThereClone(){
        return _vieClone > 0;
    }
    
    /**
     * Permet de savoir si un pokémon peut encore combattre ou doit utiliser Lutte
     * @return true s'il peut combattre, false sinon
     */
    public boolean peutEncoreCombattre(){
        int i = 0;
        boolean peutCombattre = false;
        //Pour chaque capacité, tant que l'on n'a pas trouvé une capacité possible
        while(i < 4 && !peutCombattre){
            //S'il n'y a pas de capacité, on passe à la suivante
            if(_capacites[i] == null) {
                i++;
                continue;
            }
            //On considère que le pokémon peut attaquer si une capacité n'est pas bloquée et qu'il reste des PP
            boolean estBloquee = _capaciteBloquee[i] != 0;
            boolean restePP = _pp[i] > 0;
            //Si on se trouve sur l'emplacement de la copie et qu'il y a une capacité copiée
            if(i == _emplacementCopie && _copie != null) restePP = _ppCopie > 0; 
            peutCombattre = (!estBloquee && restePP); 
            
            i++;
        }
        return peutCombattre;
    }
    
    /**
     * Permet de savoir si un pokémon a une attaque en cours et ne peut rien faire d'autre
     * @return true s'il a une attaque en cours
     */
    public boolean attaqueEnCours(){
        return _tourAttente || _tourRepos;
    }
    
    /**
     * Permet de savoir si le pokémon est endormi
     * @return true s'il dort, false sinon
     */
    public boolean isAsleep() {
        return _statut == SOMMEIL;
    }

    
}
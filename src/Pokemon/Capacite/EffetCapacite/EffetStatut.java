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
import Pokemon.Enum_Statut;
import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class EffetStatut extends Effet {
    private final Enum_Statut _statut;
    
    public EffetStatut(Enum_Cible cible, Enum_Statut statut) {
        super(cible);
        _statut = statut;
    }

    @Override
    public String effet(Pokemon cible, Pokemon autre) {
        return cible.setStatut(_statut, autre);
    }

    @Override
    public String toString() {
        return "Inflige le statut " + _statut.toString() + " au Pokémon " + super._cible + " .";
    }
    
}

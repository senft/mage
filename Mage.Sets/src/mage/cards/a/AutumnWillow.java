/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.AsThoughEffectImpl;
import mage.abilities.keyword.ShroudAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPlayer;

/**
 * Gatecrash FAQ 21.01.2013
 *
 * Creatures your opponents control don't actually lose hexproof, although you
 * will ignore hexproof for purposes of choosing targets of spells and abilities
 * you control.
 *
 * Creatures that come under your control after Glaring Spotlight's last ability
 * resolves won't have hexproof but can't be blocked that turn.
 *
 * @author L_J
 */
public class AutumnWillow extends CardImpl {

    public AutumnWillow(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{4}{G}{G}");
        addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.AVATAR);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Shroud
        this.addAbility(ShroudAbility.getInstance());

        // {G}: Until end of turn, Autumn Willow can be the target of spells and abilities controlled by target player as though it didn't have shroud.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new AutumnWillowEffect(), new ManaCostsImpl("{G}"));
        ability.addTarget(new TargetPlayer());
        this.addAbility(ability);
    }

    public AutumnWillow(final AutumnWillow card) {
        super(card);
    }

    @Override
    public AutumnWillow copy() {
        return new AutumnWillow(this);
    }
}

class AutumnWillowEffect extends AsThoughEffectImpl {

    public AutumnWillowEffect() {
        super(AsThoughEffectType.SHROUD, Duration.EndOfTurn, Outcome.Benefit);
        staticText = "Until end of turn, Autumn Willow can be the target of spells and abilities controlled by target player as though it didn't have shroud";
    }

    public AutumnWillowEffect(final AutumnWillowEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public AutumnWillowEffect copy() {
        return new AutumnWillowEffect(this);
    }

    @Override
    public boolean applies(UUID sourceId, Ability source, UUID affectedControllerId, Game game) {
        if (affectedControllerId.equals(source.getFirstTarget())) {
            Permanent creature = game.getPermanent(sourceId);
            if (creature != null) {
                if (sourceId == source.getSourceId()) {
                    return true;
                }
            }
        }
        return false;
    }
}

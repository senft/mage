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
package mage.cards.t;

import java.util.Objects;
import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.RemoveVariableCountersSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.dynamicvalue.common.ManacostVariableValue;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.target.common.TargetCreatureOrPlayer;

/**
 *
 * @author jerekwilson
 */
public class TalonOfPain extends CardImpl {

    public TalonOfPain(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{4}");
        

        /*
         * Whenever a source you control other than Talon of Pain deals damage to an opponent, 
         * put a charge counter on Talon of Pain.
         */
        this.addAbility(new TalonOfPainTriggeredAbility());
           
        
        // {X}, {tap}, Remove X charge counters from Talon of Pain: Talon of Pain deals X damage to target creature or player.
        Ability ability  = new SimpleActivatedAbility(Zone.BATTLEFIELD, new DamageTargetEffect(new ManacostVariableValue()) , new ManaCostsImpl("{X}"));
        ability.addCost(new TapSourceCost());
        ability.addCost(new RemoveVariableCountersSourceCost(CounterType.CHARGE.createInstance()));
        ability.addTarget(new TargetCreatureOrPlayer());
        this.addAbility(ability);
        
        
    }

    public TalonOfPain(final TalonOfPain card) {
        super(card);
    }

    @Override
    public TalonOfPain copy() {
        return new TalonOfPain(this);
    }
    
    private class TalonOfPainTriggeredAbility extends TriggeredAbilityImpl {

        public TalonOfPainTriggeredAbility() {
            super(Zone.BATTLEFIELD, new TalonOfPainEffect());
        }

        public TalonOfPainTriggeredAbility(final TalonOfPainTriggeredAbility ability) {
            super(ability);
        }

        @Override
        public TalonOfPainTriggeredAbility copy() {
            return new TalonOfPainTriggeredAbility(this);
        }

        @Override
        public boolean checkEventType(GameEvent event, Game game) {
            return event.getType() == GameEvent.EventType.DAMAGED_PLAYER;
        }

        @Override
        public boolean checkTrigger(GameEvent event, Game game) {
            // to another player
            if (!Objects.equals(this.getControllerId(), event.getTargetId())) {
                // a source you control other than Talon of Pain
                UUID sourceControllerId = game.getControllerId(event.getSourceId());
                if (sourceControllerId != null 
                        && sourceControllerId.equals(this.getControllerId()) 
                        && this.getSourceId() != event.getSourceId() ) {
                    // return true so the effect will fire and a charge counter will be added
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getRule() {
            return "Whenever a source you control other than {this} deals damage to another player, " + super.getRule();
        }
    }

    private static class TalonOfPainEffect extends OneShotEffect {

        public TalonOfPainEffect() {
            super(Outcome.Damage);
            this.staticText = "put a charge counter on {this}.";
        }

        public TalonOfPainEffect(final TalonOfPainEffect effect) {
            super(effect);
        }

        @Override
        public TalonOfPainEffect copy() {
            return new TalonOfPainEffect(this);
        }

        @Override
        public boolean apply(Game game, Ability source) {
            Permanent permanent = game.getPermanent(source.getSourceId());
            if (permanent != null) {
                permanent.addCounters(CounterType.CHARGE.createInstance(), source, game);
                return true;
            }
            return false;
        }
    }
}

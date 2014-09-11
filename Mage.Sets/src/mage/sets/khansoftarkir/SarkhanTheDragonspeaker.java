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
package mage.sets.khansoftarkir;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.LoyaltyAbility;
import mage.abilities.common.BeginningOfDrawTriggeredAbility;
import mage.abilities.common.BeginningOfEndStepTriggeredAbility;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.abilities.effects.common.GetEmblemEffect;
import mage.abilities.effects.common.continious.BecomesCreatureSourceEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.effects.common.discard.DiscardHandControllerEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.abilities.keyword.HasteAbility;
import mage.abilities.keyword.IndestructibleAbility;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Rarity;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.command.Emblem;
import mage.game.permanent.token.Token;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author emerald000
 */
public class SarkhanTheDragonspeaker extends CardImpl {

    public SarkhanTheDragonspeaker(UUID ownerId) {
        super(ownerId, 119, "Sarkhan, the Dragonspeaker", Rarity.MYTHIC, new CardType[]{CardType.PLANESWALKER}, "{3}{R}{R}");
        this.expansionSetCode = "KTK";
        this.subtype.add("Sarkhan");
        this.color.setRed(true);
        this.addAbility(new EntersBattlefieldAbility(new AddCountersSourceEffect(CounterType.LOYALTY.createInstance(4)), false));

        // +1: Until end of turn, Sarkhan, the Dragonspeaker becomes a legendary 4/4 red Dragon creature with flying, indestructible, and haste.
        this.addAbility(new LoyaltyAbility(new BecomesCreatureSourceEffect(new SarkhanTheDragonspeakerToken(), "", Duration.EndOfTurn), 1));
        
        // -3: Sarkhan, the Dragonspeaker deals 4 damage to target creature.
        LoyaltyAbility ability = new LoyaltyAbility(new DamageTargetEffect(4), -3);
        ability.addTarget(new TargetCreaturePermanent());
        this.addAbility(ability);
        
        // -6: You get an emblem with "At the beginning of your draw step, draw two additional cards" and "At the beginning of your end step, discard your hand."
        Effect effect = new GetEmblemEffect(new SarkhanTheDragonspeakerEmblem());
        effect.setText("You get an emblem with \"At the beginning of your draw step, draw two additional cards\" and \"At the beginning of your end step, discard your hand.\"");
        this.addAbility(new LoyaltyAbility(effect, -6));
    }

    public SarkhanTheDragonspeaker(final SarkhanTheDragonspeaker card) {
        super(card);
    }

    @Override
    public SarkhanTheDragonspeaker copy() {
        return new SarkhanTheDragonspeaker(this);
    }
}

class SarkhanTheDragonspeakerToken extends Token {

    SarkhanTheDragonspeakerToken() {
        super("", "legendary 4/4 red Dragon creature with flying, indestructible, and haste");
        supertype.add("Legendary");
        power = new MageInt(4);
        toughness = new MageInt(4);
        color.setRed(true);
        subtype.add("Dragon");
        cardType.add(CardType.CREATURE);
        
        this.addAbility(FlyingAbility.getInstance());
        this.addAbility(IndestructibleAbility.getInstance());
        this.addAbility(HasteAbility.getInstance());
    }
}

class SarkhanTheDragonspeakerEmblem extends Emblem {

    SarkhanTheDragonspeakerEmblem() {
        setName("EMBLEM: Sarkhan, the Dragonspeaker");
        this.setExpansionSetCodeForImage("KTK");

        this.getAbilities().add(new BeginningOfDrawTriggeredAbility(Zone.COMMAND, new DrawCardSourceControllerEffect(2), TargetController.YOU, false));
        this.getAbilities().add(new BeginningOfEndStepTriggeredAbility(Zone.COMMAND, new DiscardHandControllerEffect(), TargetController.YOU, null, false));
    }
}

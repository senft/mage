package mage.deck;

import mage.abilities.Ability;
import mage.abilities.keyword.CompanionAbility;
import mage.abilities.keyword.PartnerAbility;
import mage.abilities.keyword.PartnerWithAbility;
import mage.cards.Card;
import mage.cards.ExpansionSet;
import mage.cards.Sets;
import mage.cards.decks.Constructed;
import mage.cards.decks.Deck;
import mage.filter.FilterMana;
import mage.util.ManaUtil;

import java.util.*;

/**
 * @author spjspj
 */
public class FreeformCommander extends Constructed {

    protected List<String> bannedCommander = new ArrayList<>();
    private static final Map<String, Integer> pdAllowed = new HashMap<>();

    public FreeformCommander() {
        super("Freeform Commander");
        for (ExpansionSet set : Sets.getInstance().values()) {
            setCodes.add(set.getCode());
        }

        // no banned cards
        this.banned.clear();
    }

    public FreeformCommander(String name) {
        super(name);
    }

    public FreeformCommander(String name, String shortName) {
        super(name, shortName);
    }

    @Override
    public int getDeckMinSize() {
        return 98;
    }

    @Override
    public int getSideboardMinSize() {
        return 1;
    }

    @Override
    public boolean validate(Deck deck) {
        boolean valid = true;
        invalid.clear();
        FilterMana colorIdentity = new FilterMana();
        Set<Card> commanders = new HashSet<>();
        Card companion = null;

        if (deck.getSideboard().size() == 1) {
            commanders.add(deck.getSideboard().iterator().next());
        } else if (deck.getSideboard().size() == 2) {
            Iterator<Card> iter = deck.getSideboard().iterator();
            Card card1 = iter.next();
            Card card2 = iter.next();
            if (card1.getAbilities().stream().anyMatch(ability -> ability instanceof CompanionAbility)) {
                companion = card1;
                commanders.add(card2);
            } else if (card2.getAbilities().stream().anyMatch(ability -> ability instanceof CompanionAbility)) {
                companion = card2;
                commanders.add(card1);
            } else {
                commanders.add(card1);
                commanders.add(card2);
            }
        } else if (deck.getSideboard().size() == 3) {
            Iterator<Card> iter = deck.getSideboard().iterator();
            Card card1 = iter.next();
            Card card2 = iter.next();
            Card card3 = iter.next();
            if (card1.getAbilities().stream().anyMatch(ability -> ability instanceof CompanionAbility)) {
                companion = card1;
                commanders.add(card2);
                commanders.add(card3);
            } else if (card2.getAbilities().stream().anyMatch(ability -> ability instanceof CompanionAbility)) {
                companion = card2;
                commanders.add(card1);
                commanders.add(card3);
            } else if (card3.getAbilities().stream().anyMatch(ability -> ability instanceof CompanionAbility)) {
                companion = card3;
                commanders.add(card1);
                commanders.add(card2);
            } else {
                invalid.put("Commander", "Sideboard must contain only the commander(s) and up to 1 companion");
                valid = false;
            }
        } else {
            invalid.put("Commander", "Sideboard must contain only the commander(s) and up to 1 companion");
            valid = false;
        }

        if (companion != null && deck.getCards().size() + deck.getSideboard().size() != 101) {
            invalid.put("Deck", "Must contain " + 101 + " cards (companion doesn't count for deck size): has " + (deck.getCards().size() + deck.getSideboard().size()) + " cards");
            valid = false;
        } else if (companion == null && deck.getCards().size() + deck.getSideboard().size() != 100) {
            invalid.put("Deck", "Must contain " + 100 + " cards: has " + (deck.getCards().size() + deck.getSideboard().size()) + " cards");
            valid = false;
        }

        Map<String, Integer> counts = new HashMap<>();
        countCards(counts, deck.getCards());
        countCards(counts, deck.getSideboard());
        valid = checkCounts(1, counts) && valid;

        Set<String> commanderNames = new HashSet<>();
        for (Card commander : commanders) {
            commanderNames.add(commander.getName());
        }
        for (Card commander : commanders) {
            if (!commander.isCreature() || !commander.isLegendary()) {
                invalid.put("Commander", "For Freeform Commander, the commander must be a creature or be legendary. Yours was: " + commander.getName());
                valid = false;
            }
            if (commanders.size() == 2) {
                if (!commander.getAbilities().contains(PartnerAbility.getInstance())) {
                    boolean partnersWith = commander.getAbilities()
                            .stream()
                            .filter(PartnerWithAbility.class::isInstance)
                            .map(PartnerWithAbility.class::cast)
                            .map(PartnerWithAbility::getPartnerName)
                            .anyMatch(commanderNames::contains);
                    if (!partnersWith) {
                        invalid.put("Commander", "Commander without Partner (" + commander.getName() + ')');
                        valid = false;
                    }
                }
            }
            ManaUtil.collectColorIdentity(colorIdentity, commander.getColorIdentity());
        }

        // no needs in cards check on wrong commanders
        if (!valid) {
            return false;
        }

        for (Card card : deck.getCards()) {
            if (!ManaUtil.isColorIdentityCompatible(colorIdentity, card.getColorIdentity())) {
                invalid.put(card.getName(), "Invalid color (" + colorIdentity.toString() + ')');
                valid = false;
            }
        }
        for (Card card : deck.getSideboard()) {
            if (!ManaUtil.isColorIdentityCompatible(colorIdentity, card.getColorIdentity())) {
                invalid.put(card.getName(), "Invalid color (" + colorIdentity.toString() + ')');
                valid = false;
            }
        }
        // Check for companion legality
        if (companion != null) {
            Set<Card> cards = new HashSet<>(deck.getCards());
            cards.addAll(commanders);
            for (Ability ability : companion.getAbilities()) {
                if (ability instanceof CompanionAbility) {
                    CompanionAbility companionAbility = (CompanionAbility) ability;
                    if (!companionAbility.isLegal(cards, getDeckMinSize())) {
                        invalid.put(companion.getName(), "Deck invalid for companion");
                        valid = false;
                    }
                    break;
                }
            }
        }
        return valid;
    }
}

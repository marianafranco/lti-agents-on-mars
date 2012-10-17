package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.List;

import env.Percept;

import model.Entity;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Retrieves the name of the opponent which is in the same position as me.
 * </p>
 * Use: jia.get_opponent_name(-A); </br>
 * Where: A is the name of the opponent in the same position as me.
 * 
 * @author mafranko
 */
public class get_opponent_name extends DefaultInternalAction {

	private static final long serialVersionUID = 2815473594430522520L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		List<Entity> opponents = model.getActiveOpponentsOnVertex(myPosition.getId());
		if (opponents.isEmpty()) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}
		for (Entity opponent : opponents) {
			if (opponent.getRole().equals(Percept.ROLE_SABOTEUR)) {
				return un.unifies(terms[0], ASSyntax.createString(opponent.getName()));
			}
		}
		for (Entity opponent : opponents) {
			if (opponent.getRole().equals(Percept.ROLE_REPAIRER)) {
				return un.unifies(terms[0], ASSyntax.createString(opponent.getName()));
			}
		}
		return un.unifies(terms[0], ASSyntax.createString(opponents.get(0).getName()));
	}
}

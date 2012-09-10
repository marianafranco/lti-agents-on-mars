package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.List;

import model.Entity;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Fixed the agent's world model and stop to try to attack in case of receive a
 * lastActionResult(failed_away).
 * </p>
 * Use: jia.stop_attack; </br>
 * 
 * @author mafranko
 */
public class stop_attack extends DefaultInternalAction {

	private static final long serialVersionUID = -4030825127580589589L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();
		List<Entity> opponents = model.getActiveOpponentsOnVertex(myPosition.getId());
		if (!opponents.isEmpty()) {
			Entity opponent = opponents.get(0);
			opponent.setVertex(new Vertex(0));
		}
		return true;
	}
}

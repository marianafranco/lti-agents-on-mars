package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns the position of the closer opponent of the given role.
 * </p>
 * Use: jia.closer_opponent(+R,-P); </br>
 * Where: R is the role and P the position of the closer opponent with this role.
 * 
 * @author mafranko
 */
public class closer_opponent extends DefaultInternalAction {

	private static final long serialVersionUID = 7802609769068834646L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		String role = ((StringTerm) terms[0]).getString();
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		List<Entity> opponents = model.getActiveOpponentByRole(role);

		int minDist = Integer.MAX_VALUE;
		int closerPosition = -1;
		for (Entity opponent : opponents) {
			Vertex v = opponent.getVertex();
			int dist = graph.getDistance(myPosition, v);
			if (dist < minDist) {
				closerPosition = v.getId();
			}
		}
		String vertex = "vertex" + closerPosition;
		return un.unifies(terms[1], ASSyntax.createString(vertex));
	}
}

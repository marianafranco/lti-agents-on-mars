package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns the position of the closer repairer coworker.
 * </p>
 * Use: jia.closer_repairer(-P); </br>
 * Where: P is the position of the closer repairer.
 * 
 * @author mafranko
 */
public class closer_repairer extends DefaultInternalAction {

	private static final long serialVersionUID = 7802609769068834646L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		List<Entity> repairers = model.getCoworkersByRole("repairer");

		int minDist = Integer.MAX_VALUE;
		int closerPosition = -1;
		for (Entity repairer : repairers) {
			Vertex v = repairer.getVertex();
			int dist = graph.getDistance(myPosition, v);
			if (dist < minDist) {
				closerPosition = v.getId();
			}
		}

		if (minDist <= 1) {
			return un.unifies(terms[0], ASSyntax.createString("neighbor"));
		}
		if (closerPosition == -1) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}

		String vertex = "vertex" + closerPosition;
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}
}

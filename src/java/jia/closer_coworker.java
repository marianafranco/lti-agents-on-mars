package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MartianArch;
import arch.WorldModel;

/**
 * Returns the closer agent and its position given a list of coworkers.
 * </p>
 * Use: jia.closer_agent(+Agents,-Ag,-Pos); </br>
 * Where: Agents is the list of agents, Ag is the closer agent and Pos its position.
 * 
 * @author mafranko
 */
public class closer_coworker extends DefaultInternalAction {

	private static final long serialVersionUID = -1135939414001408876L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MartianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		int minDist = Integer.MAX_VALUE;		
		int closerPosition = -1;
		List<Entity> coworkers = new ArrayList<Entity> (model.getCoworkers().values());
		for (Entity agent : coworkers) {
			Vertex v = agent.getVertex();
			if (null != v) {
				int dist = graph.getDistance(myPosition, v);
				if (dist <= minDist) {
					minDist = dist;
					closerPosition = v.getId();
				}
			}
		}

		if (closerPosition == -1) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}

		String vertex = "vertex" + closerPosition;
		return  un.unifies(terms[0], new Atom(vertex));
	}

}

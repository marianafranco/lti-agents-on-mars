package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Term;

import java.util.List;

import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns the closer agent and its position given a list of coworkers.
 * </p>
 * Use: jia.closer_agent(+Agents,-Ag,-Pos); </br>
 * Where: Agents is the list of agents, Ag is the closer agent and Pos its position.
 * 
 * @author mafranko
 */
public class closer_agent extends DefaultInternalAction {

	private static final long serialVersionUID = -1135939414001408876L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		List<Term> agents = ((ListTerm) terms[0]).getAsList();

		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();

		Vertex myPosition = model.getMyVertex();

		int minDist = Integer.MAX_VALUE;
		String closerAgent = null;
		String closerPosition = null;
		for (Term agent : agents) {
			String agentName = ((Atom) agent).getFunctor();
			Vertex v = model.getCoworkerPosition(agentName);
			int dist = graph.getDistance(myPosition, v);
			if (dist <= minDist) {
				minDist = dist;
				closerAgent = agentName;
				closerPosition = "vertex" + v.getId();
			}
		}
		return  un.unifies(terms[1], ASSyntax.createString(closerAgent))
				& un.unifies(terms[2], new Atom(closerPosition));
	}

}

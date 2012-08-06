package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;
import env.Percept;

public class escape  extends DefaultInternalAction {

	private static final long serialVersionUID = -5827900602735423794L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();

		Vertex myPosition = model.getMyVertex();

		List<Vertex> neighbors = new ArrayList<Vertex>(myPosition.getNeighbors());
		if (neighbors.isEmpty()) {
			return un.unifies(terms[0], ASSyntax.createString("none"));
		}

		for (Vertex neighbor : neighbors) {
			if (neighbor.getTeam().equals(WorldModel.myTeam)
					&& !model.hasActiveOpponentOnVertex(neighbor)) {
				String vertex = "vertex" + neighbor.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}

		for (Vertex neighbor : neighbors) {
			if ((neighbor.getTeam().equals(Percept.TEAM_NONE)
					|| neighbor.getTeam().equals(Percept.TEAM_UNKNOWN))
					&& !model.hasActiveOpponentOnVertex(neighbor)) {
				String vertex = "vertex" + neighbor.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}

		for (Vertex neighbor : neighbors) {
			if (!model.hasActiveOpponentOnVertex(neighbor)) {
				String vertex = "vertex" + neighbor.getId();
				return un.unifies(terms[0], ASSyntax.createString(vertex));
			}
		}

		// TODO go to the vertex which will cost less energy
		String vertex = "vertex" + neighbors.get(0).getId();
		return un.unifies(terms[0], ASSyntax.createString(vertex));
	}
}

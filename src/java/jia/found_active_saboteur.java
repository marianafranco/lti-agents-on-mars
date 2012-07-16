package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.List;

import model.Entity;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns true or false, indicating if was found an active opponent with role of saboteur.
 * </p>
 * Use: jia.found_active_saboteur; </br>
 * 
 * @author mafranko
 */
public class found_active_saboteur extends DefaultInternalAction {

	private static final long serialVersionUID = 4839803790133301621L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		List<Entity> saboteurs = model.getActiveOpponentByRole("saboteur");
		if (null == saboteurs || saboteurs.isEmpty()) {
			return false;
		}
		Graph graph = model.getGraph();
		Vertex myPosition = model.getMyVertex();
		for (Entity saboteur : saboteurs) {
			if (graph.existsPath(myPosition, saboteur.getVertex())) {
				return true;
			}
		}
		return false;
	}

}

package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import model.graph.Graph;
import model.graph.Vertex;
import arch.MarcianArch;
import arch.WorldModel;

/**
 * Returns true or false to indicate if there are edges not surveyed near my position.
 * </p>
 * Use: jia.is_survey_goal; </br>
 * 
 * @author mafranko
 */
public class is_survey_goal extends DefaultInternalAction {

	private static final long serialVersionUID = 3333738624988187827L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		Vertex myPosition = model.getMyVertex();
		if (null == myPosition) {
			return false;
		}
		for (Vertex neighbor : myPosition.getNeighbors()) {
			int value = graph.getEdgeValue(myPosition.getId(), neighbor.getId());
			if (value == -1) {
				return true;
			}
		}
		return false;
	}
}

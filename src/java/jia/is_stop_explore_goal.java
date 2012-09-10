package jia;

import model.graph.Graph;
import arch.MarcianArch;
import arch.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * Returns true or false to indicate if the agent has explored at least 25% of the graph.
 * </p>
 * Use: jia.is_stop_explore_goal; </br>
 * 
 * @author mafranko
 *
 */
public class is_stop_explore_goal extends DefaultInternalAction {

	private static final long serialVersionUID = -5676585459307717849L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Graph graph = model.getGraph();
		int maxNumOfVertices = graph.getMaxNumOfVertices();
		int numOfVertices = graph.getVertices().size();
		float percent =  numOfVertices / maxNumOfVertices;
		if (percent > 0.25) {
			return true;
		} else {
			return false;
		}
	}

}

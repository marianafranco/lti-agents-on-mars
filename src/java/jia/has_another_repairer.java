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
import env.Percept;

public class has_another_repairer extends DefaultInternalAction {

	private static final long serialVersionUID = 8243465155921899435L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		WorldModel model = ((MarcianArch) ts.getUserAgArch()).getModel();
		Vertex myPosition = model.getMyVertex();

		List<Entity> repairers = model.getCoworkersByRole("repairer");
		for (Entity repairer : repairers) {
			if (repairer.getVertex().equals(myPosition)
					&& !repairer.getStatus().equals(Percept.STATUS_DISABLED)
					&& compareIDs(ts.getUserAgArch().getAgName(), repairer.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean compareIDs(String ag1, String ag2) {
		String name1 = ag1.replace("marcian", "");
		String name2 = ag2.replace("marcian", "");
		int id1 = Integer.parseInt(name1);
		int id2 = Integer.parseInt(name2);
		if (id1 > id2) {
			return true;
		} else {
			return false;
		}
	}
}

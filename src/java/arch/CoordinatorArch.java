package arch;

import jason.asSemantics.Message;
import jason.asSyntax.Literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import c4jason.CAgentArch;

/**
 * Coordinator architecture.
 * 
 * @author mafranko
 */
public class CoordinatorArch extends CAgentArch {

	/**
	 * The agent's world model.
	 */
	private WorldModel model;


	public CoordinatorArch() {
		super();
		model = new WorldModel(getAgName());
	}

	/**
	 * Reads the agent's mailbox and updates the world model. Only relevant messages are
	 * leaved in the list of perception for the agent. 
	 */
	@Override
    public void checkMail() {
		super.checkMail();
		List<Literal> percepts = convertMessageQueueToLiteralList(getTS().getC().getMailBox());
		model.update(percepts);

		Iterator<Message> im = getTS().getC().getMailBox().iterator();
		while (im.hasNext()) {
			Message message  = im.next();
			Literal  percept = Literal.parseLiteral(message.getPropCont().toString());
			String  p = percept.getFunctor();

//            String  ms = message.getPropCont().toString();
//            logger.info("[" + getAgName() + "] receved mail: " + ms);

			if (p.equals("visibleEdge") || p.equals("visibleEntity")
    				|| p.equals("visibleVertex") || p.equals("probedVertex")
    				|| p.equals("surveyedEdge") || p.equals("saboteur")
    				|| p.equals("inspectedEntity")) {
				im.remove();	// removes the percept from the mailbox
			}
		}

	}

	private List<Literal> convertMessageQueueToLiteralList(Queue<Message> messages) {
		List<Literal> literals = new ArrayList<Literal>();
		for (Message message : messages) {
			Literal  p = Literal.parseLiteral(message.getPropCont().toString());
			literals.add(p);
		}
		return literals;
	}

	public WorldModel getModel() {
		return this.model;
	}
}

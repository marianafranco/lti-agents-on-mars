package model.graph;

import java.util.Comparator;

/**
 * Vertex comparator.
 * 
 * @author mafranko
 */
public class VertexComparator implements Comparator<Vertex>{

	@Override
	public int compare(Vertex o1, Vertex o2) {
		int value1 = o1.getValue();
		int value2 = o2.getValue();
		if (value1 < value2) {
			return 1;
		} else if (value1 > value2) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
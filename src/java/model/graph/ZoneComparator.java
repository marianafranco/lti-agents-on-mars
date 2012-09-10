package model.graph;

import java.util.Comparator;
import java.util.List;

/**
 * Zone comparator.
 * 
 * @author mafranko
 */
public class ZoneComparator implements Comparator<List<Vertex>>{

	@Override
	public int compare(List<Vertex> o1, List<Vertex> o2) {
		int value1 = zoneValue(o1);
		int value2 = zoneValue(o2);
		if (value1 < value2) {
			return 1;
		} else if (value1 > value2) {
			return -1;
		} else {
			return 0;
		}
	}

	private int zoneValue(List<Vertex> zone) {
		int value = 0;
		for (Vertex v : zone) {
			value = value + v.getValue();
		}
		return value;
	}
}
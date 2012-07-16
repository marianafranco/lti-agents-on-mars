package model.graph;

/**
 * Edge class.
 * 
 * @author mafranko
 */
public class Edge {

	private int v1;		// the vertex id
	private int v2;		// the other vertex id

	public Edge(int v1, int v2) {
		if (v1 < v2) {
			this.v1 = v1;
			this.v2 = v2;
		} else {
			this.v1 = v2;
			this.v2 = v1;
		}
	}

	public int getV1() {
		return v1;
	}

	public int getV2() {
		return v2;
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		if (object instanceof Edge) {
			Edge other = (Edge) object;
			if (this.v1 == other.v1 && this.v2 == other.v2) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 23;
		hash = hash * 31 + v1;
		hash = hash * 31 + v2;
		return hash;
	}

	@Override
	public String toString() {
		return "edge(" + v1 + "," + v2 + ")";
	}
}

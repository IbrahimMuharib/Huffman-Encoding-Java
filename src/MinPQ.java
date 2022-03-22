class PQElement { // also used us BTNode
	public char character;
	public int frequency;
	public PQElement right;
	public PQElement left;
	public String binary;

	public PQElement(char e, int pr) {
		character = e;
		frequency = pr;
		right = left = null;
		binary = null;
	}
}

class PQNode {
	public PQElement data;
	public PQNode next;

	public PQNode() {
		next = null;
	}

	public PQNode(PQElement d) {
		data = d;
	}
}

public class MinPQ {

	protected int size;
	private PQNode head;

	/* tail is of no use here. */
	public MinPQ() {
		head = null;
		size = 0;
	}

	public int length() {
		return size;
	}

	public boolean full() {
		return false;
	}

	public void enqueue(PQElement d) {
		PQNode tmp = new PQNode(d);
		if ((size == 0) || (d.frequency < head.data.frequency)) {
			tmp.next = head;
			head = tmp;
		} else {
			PQNode p = head;
			PQNode q = null;
			while ((p != null) && (d.frequency >= p.data.frequency)) {
				q = p;
				p = p.next;
			}
			tmp.next = p;
			q.next = tmp;
		}
		size++;
	}

	public PQElement serve() {
		PQNode node = head;
		PQElement pqe = head.data;
		head = head.next;
		size--;
		return pqe;
	}

}

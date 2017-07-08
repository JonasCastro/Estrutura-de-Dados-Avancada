
import java.util.LinkedList;


public class HeapBinario<K extends Comparable<K>> {

	private LinkedList<K> heap;

	public HeapBinario() {
		this.heap = new LinkedList<>();
	}

	public void insert(K x) {
		heap.add(x);
		swimUp(heap.size() - 1);

	}

	private void swimUp(int m) {
		int f = m;

		f = getPai(f);
		// System.out.println(f +" "+m);
		// System.out.println( ((HuffmanTree.Node)heap.get(f)).getCh() +" comp
		// "+ ((HuffmanTree.Node)heap.get(m)).getCh());

		// for (int ii = 0; ii< heap.size(); ii++){
		// System.out.print("("+ ((HuffmanTree.Node)heap.get(ii)).getFreq() +
		// ","+((HuffmanTree.Node)heap.get(ii)).getCh() +")");
		// }
		System.out.println();
		while (m >= 1 && heap.get(f).compareTo(heap.get(m)) > 0) {

			K aux = heap.get(f);
			heap.set(f, heap.get(m));
			heap.set(m, aux);
			// System.out.println("TROCAOU");
			// for (int ii = 0; ii< heap.size(); ii++){
			// System.out.print("("+ ((HuffmanTree.Node)heap.get(ii)).getFreq()
			// + ","+((HuffmanTree.Node)heap.get(ii)).getCh() +")");
			// }
			m = f;

			f = getPai(f);

		}

	}

	private int getPai(int f) {
		if (f == 2)
			f /= 2;
		if (f % 2 == 1)
			f /= 2;
		else
			f = (f - 1) / 2;
		return f;
	}

	public K extractMin() {
		K x = heap.get(0);
		heap.set(0, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);
		sinkDown(0);
		return x;
	}

	public K min() {
		return heap.get(0);
	}

	private void sinkDown(int m) {
		int i = m;

		if (heap.size() == 2) {
			K min1 = heap.get(0);
			K min2 = heap.get(1);
			if (min1.compareTo(min2) > 0) {
				if (min1.compareTo(min2) > 0) {
					K aux = min1;
					heap.set(0, heap.get(1));
					heap.set(1, aux);

				}
			}

		}

		while (2 * i + 1 < heap.size() - 1) {

			K min1 = heap.get(2 * i + 1);
			K min2 = heap.get(2 * i + 2);

			if (min1.compareTo(min2) < 0) {

				heap.set(2 * i + 1, heap.get(i));
				heap.set(i, min1);
				i = i * 2 + 1;

			} else {
				heap.set(2 * i + 2, heap.get(i));
				heap.set(i, min2);
				i = i * 2 + 2;

			}

		}

	}

	public void decreasekey(int i, K key) {
		if (heap.get(i - 1).compareTo(key) > 0) {
			heap.set(i - 1, key);

			swimUp(i - 1);
		} else {
			heap.set(i - 1, key);
			sinkDown(i - 1);
		}

	}

	public boolean isEmpty() {
		return heap.size() == 0;
	}

	public void delete(Integer i) {
		heap.set(i, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);

		int m = i;

		if (i % 2 == 1)
			m++;

		if (heap.get(i).compareTo(heap.get(m / 2)) < 0)
			swimUp(i);

		else
			sinkDown(i);

	}

	public void meld(HeapBinario h2) {
		//
		// for (int i = 0; i < h2.heap.size(); i++)
		// insert(h2.heap);
	}

	public LinkedList<K> getHeap() {
		return heap;
	}

	public void printer(HeapBinario heap) {
		// for (K x : heap.getHeap())
		// System.out.println(x);
	}

	public static void main(String[] args) {
		HeapBinario h = new HeapBinario();

		h.insert(12);
		h.insert(9);
		h.insert(7);
		h.insert(22);
		h.insert(3);
		h.insert(26);
		h.insert(14);

		h.insert(0);

		System.out.println(h.isEmpty());
		System.out.println("insere Heap");
		h.printer(h);
		System.out.println();
		System.out.println();
		System.out.println("Extrair m�nimo");
		h.extractMin();
		h.printer(h);
		System.out.println();
		System.out.println();
		System.out.println("delete index");
		h.delete(2);
		h.printer(h);
	}

	public int size() {
		return heap.size();
	}
}

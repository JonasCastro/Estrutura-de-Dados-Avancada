

import java.util.ArrayList;

public class BinaryHeap<K extends Comparable<K>> {

	ArrayList<K> heap = new ArrayList<>();
	

	public ArrayList<K> getHeap() {
		return heap;
	}

	public BinaryHeap(ArrayList<K> array) {
		makeHeap(array);
		heap = array;
	}

	public BinaryHeap() {

	}

	private void makeHeap(ArrayList<K> array) {
		for (int i = (array.size() - 1) / 2; i >= 0; i--) {
			sinkDown(array, i);
		}
	}

	private void swap(ArrayList<K> array, int child, int parent) {
		K k = array.get(child);
		array.set(child, array.get(parent));
		array.set(parent, k);
	}

	public boolean isEmpty() {
		return heap.size() == 0;
	}

	public int size() {
		return heap.size();
	}

	public void insert(K key) {
		heap.add(key);
		swimUp(heap, heap.size());
	}

	public K extractMin() {
		if (heap.size() == 1) {
			K min = heap.get(0);
			heap.remove(0);
			return min;
		}
		else {
			int maxi = heap.size() - 2;
			K min = heap.get(0);
			heap.remove(0);
			K max = heap.get(maxi);
			heap.remove(max);
			heap.add(0, max);
			sinkDown(heap, 0);
			return min;
		}
	}

	public void decreaseKey(K key, K newKey) {
		int i = heap.indexOf(key);
		if (newKey.compareTo(key) > 0 || newKey.equals(key))
			return;
		heap.set(heap.indexOf(key), newKey);
		swimUp(heap, i + 1);
	}

	public K findMin() {
		return heap.get(0);
	}

	public void delete(K key) {
		int i = heap.indexOf(key);
		K max = heap.get(heap.size() - 1);
		heap.remove(max);
		heap.set(i, max);
		sinkDown(heap, i);
		swimUp(heap, i + 1);
	}

	private void sinkDown(ArrayList<K> array, int i) {
		int j = i;
		int n = heap.size() - 1;
		while ((2 * j) + 1 <= n) {
			int f = (2 * j) + 1;
			if (f < n && array.get(f).compareTo(array.get(f + 1)) > 0)
				f = f + 1;
			if (array.get(j).compareTo(array.get(f)) < 0 || array.get(j).equals(array.get(f)))
				j = n;
			else {
				swap(array, f, j);
				j = f;
			}
		}
	}

	private void swimUp(ArrayList<K> array, int i) {
		while (i >= 2 && array.get(i / 2 - 1).compareTo(array.get(i - 1)) > 0) {
			swap(array, i / 2 - 1, i - 1);
			i = i / 2;
		}
	}

	public static void main(String args[]) {
		// Random gerador = new Random();
		int arr[] = { 6, 7 };
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i = 0; i < 2; i++) {
			array.add(arr[i]);
			// array.add(gerador.nextInt(50));
		}

		BinaryHeap<Integer> heap = new BinaryHeap<>(array);

		showHeap(array);
		showExtractedMin(array, heap);
		showExtractedMin(array, heap);
	}

	private static void showHeap(ArrayList<Integer> array) {
		for (Integer i : array) {
			System.out.println(i);
		}
		System.out.println("");
	}

	private static void showExtractedMin(ArrayList<Integer> array, BinaryHeap<Integer> heap) {
		System.out.println("\nMin. extracted: " + heap.extractMin() + "\n");

		showHeap(array);
	}
}

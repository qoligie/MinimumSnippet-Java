import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//used for a web page when you are searching for documents with specific terms
public class MinimumSnippet {
	// an array list to hold space for us to create a snippet in our helper method
	// as we look for the smallest subsequence
	private ArrayList<String> tempSnip;
	// start position of snippet
	private int start;
	// end position of snippet
	private int end;
	// minimum length of snippet
	private int minLength;
	// arrayList to store terms we are looking for
	private ArrayList<String> terms;
	// an array list to store all terms in our doc
	private ArrayList<String> docList;
	// an array list to hold the elements in the snippet
	private ArrayList<String> snippet;

	/*
	 * given a document represented as a list and a list of terms.This method will
	 * find the shortest subsequence of the document that has all of the terms from
	 * the list that was passed in
	 */
	public MinimumSnippet(Iterable<String> document, List<String> terms) {

		this.snippet = new ArrayList<>();
		this.tempSnip = new ArrayList<>();
		// creating an array list to hold all the strings in the document
		docList = new ArrayList<>();
		// looping thru document arrayList
		for (String word : document) {
			// adding each string from document to the new array list
			docList.add(word);
		}
		this.terms = new ArrayList<>();
		for (String term : terms) {

			// filling arrayList with terms we are looking for
			this.terms.add(term);
		}
		// the smallest size a snippet can be & it has to contain all desired terms
		this.minLength = this.terms.size();

		if (terms.isEmpty()) {
			// ensuring all terms are in the snippet before we do anything
			throw new IllegalArgumentException();
		} else {
			minSnip();
		}

	}

	// private helper method to find the minimum snippet
	private void minSnip() {
		// checking if the document has all the desired terms b4 we do anything
		if (foundAllTerms()) {
			// a flag to keep track of if we should shrink the window/ add terms to the
			// snippet
			boolean addTerm = false;

			/*
			 * variable to keep track of the starting position of the snippet as we loop. it
			 * will continue to increment as we shrink the snippet
			 */
			int posCount = 0;
			while (!(tempSnip.size() == minLength)) {
				/*
				 * loop thru doc while index is < length list
				 */
				for (int currElement = posCount; currElement < docList.size(); currElement++) {

					/*
					 * if the snippet contains all the desired terms there is no need to continue to
					 * loop thru the document, so we get out of the loop
					 */

					if (tempSnip.containsAll(terms)) {
						break;
					} else {
						// loop thru list of terms
						for (int element = 0; element < terms.size(); element++) {
							/*
							 * comparing each term in the document to each of the desired terms in the term
							 * array list
							 */
							if (terms.get(element).equals(docList.get(currElement)) && addTerm == false) {
								/*
								 * the flag tells us that we are going to keep adding elements from the
								 * document, since we are comparing each term in our document to each term in
								 * our arrayList, if we find a match, we switch the flag to true which tells us
								 * it should be apart of our snippet
								 */
								addTerm = true;
							}

							/*
							 * if we haven't found all terms to add to our snippet array list and our flag
							 * is true, we add the next element from the doc list at that specific index
							 */
							if (addTerm && !(tempSnip.containsAll(terms))) {
								tempSnip.add(docList.get(currElement));
								// if array list only has 1 term, this will be the start of the snippet
								if (tempSnip.size() == 1) {
									/*
									 * setting our start position equal to current element in our loop thru document
									 * because this is our first element which we have already identified as a term
									 * in the document so it should be included in the snippet
									 */
									this.start = currElement;

								}
								/*
								 * if the minimum snippet is found and has all of the desired terms, the end of
								 * the snippet should be equal to our current element because at this point, our
								 * snippet contains all the desired terms, so there is no need to continue
								 * adding terms. the current element in our outer for loop keeps track of what
								 * position we are at in the document array list
								 */
								if (tempSnip.size() == minLength && tempSnip.containsAll(terms)) {

									end = currElement;
								}
								/*
								 * if snippet has all terms, there is no need to keep adding terms, so we set
								 * our flag to false and break out of the loop. there is no need to keep going
								 * thru the document if our snippet contains all the desired terms
								 */
								if (tempSnip.containsAll(terms)) {

									addTerm = false;
								}
								break;
							}

						}
					}
				}
				/*
				 * if we have found a snippet with the smallest possible length and has all
				 * desired terms we have found the minimum snippet and we can assign the holder
				 * to the snippet and break out of the loop because we would no longer need to
				 * search the document and compare terms
				 */
				if (minLength == tempSnip.size() && tempSnip.containsAll(terms)) {
					snippet = tempSnip;
					break;
				} else {
					/*
					 * starting the snippet over and running the loop again, but this time it will
					 * start at a new position in the document until we can find a snippet that
					 * contains all our terms
					 */
					tempSnip = new ArrayList<String>();
					posCount++;
				}
				/*
				 * if starting position of snippet equals the doc size, we start back over at 0
				 * in the snippet, but increment its minimum length if we haven't found all
				 * terms at the lowest possible minimum length. meaning there is a word in the
				 * document /snippet that is in between the desired terms. so there is no
				 * snippet in the document with all of the desired terms side by side
				 */
				if (posCount == docList.size()) {
					posCount = 0;
					minLength++;
				}

			}
		}
	}

	/*
	 * this method returns a boolean (true/false) if all desired terms (terms from
	 * the list of terms) were found in the document
	 */
	public boolean foundAllTerms() {

		// an array of booleans to help keep track of whether terms were found
		boolean[] termFound = new boolean[terms.size()];
		// setting each element to false means we haven't found that term yet
		Arrays.fill(termFound, false);

		for (String word : docList) {
			if (terms.contains(word)) {
				int index = terms.indexOf(word);
				/*
				 * we have found that term at that specific index so we make it true , so that
				 * we know we found it
				 */
				termFound[index] = true;
			}
		}

		for (boolean found : termFound) {
			if (found == false) {
				/*
				 * meaning we haven't found all terms if even on of the elements in the boolean
				 * array is false
				 */

				return false;
			}
		}
		/*
		 * if the loop runs and finds no false element in the boolean array, we have
		 * found all terms
		 */
		return true;

	}

	// this method returns the index of the first element in the snippet
	public int getStartingPos() {
		// makes sure all terms are found in the document
		if (!foundAllTerms()) {
			throw new IllegalArgumentException();
		} else {
			return start; // returning the index of the first element in the snippet
		}

	}

	// this return the index of the last element in the snippet
	public int getEndingPos() {
		// makes sure all terms are found in the document
		if (!foundAllTerms()) {
			throw new IllegalArgumentException();
		} else {
			return end;
		}
	}

	// this method returns the total number of elements in the snippet
	public int getLength() {
		// makes sure all terms are found in the document
		if (!foundAllTerms()) {
			throw new IllegalArgumentException();
		} else {
			// number of terms in the smallest snippet
			return snippet.size();
		}
	}

	/*
	 * this method is responsible for returning the position of the element found at
	 * that index in the snippet, in the document
	 */
	public int getPos(int index) {
		// makes sure all terms are found in the document
		if (!foundAllTerms()) {
			throw new IllegalArgumentException();
		}
		// stores the term at the index that was passing in from the list of terms
		String term = terms.get(index);
		// looping thru the snippet
		for (int docPos = start; docPos <= end; docPos++) {
			/*
			 * check if the term matches the docPos (element in the document at each index)
			 */
			if (term.equals(docList.get(docPos))) {
				// and returns its index/ position if it meets the condition
				return docPos;
			}
		}

		/*
		 * a negative value to represent that the term wasn't found in the doc at that
		 * index
		 */
		return -1;

	}

}

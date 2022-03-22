# Huffman-Encoding-Java
Huffman Coding and decoding implement in java using Min priority queues and Binary search trees.

Huffman coding and decoding is an algorithm that assigns a binary code to each character based on the number of occurrences of said character, and the characters are then stored in a certain data structure, in our case a BST.

The decoding stage of the algorithm is to trace back from the root of the tree, until a valid character is found.


DATA STRUCTURES:
•	Minimum Priority Queue (MinPQ): used to sort the characters based on their frequencies, and then to build the BST (could also use a Minheap instead).
•	Binary Search Tree (BST): a binary tree used to construct the Huffman tree.
•	frequencies array: used to build the MinPQ.
•	Binary codes array (dictionary): stores the binary codes of each character.

---------------------------------------------------------------------------------------------------------------------------------------------------------------


Click on run to activate the program.

A menu will pop in your console containing options for text input methods, it goes as follows:

	Welcome to our Huffman Encoding Program!
	---------------------------------------
	press 1 to enter your own english text
	or press 2 to read from the LoremIpsum text generator
	or press 3 to read from the sampleText
	or press 4 to read from random letter generator
	or press 5 to read randomly from the 8000 most common english words
	or press 6 for automated Plotter
	or press 7 to shut down

1. Choosing option 1 will allow you enter a text of your choice, make sure it is in english. otherwise it wouldnt work.

2. Choosing option 2 will generate a random text from the library we've attached, a prompt will pop up asking you what is the number of words you want, please enter a number otherwise an error will occur. 

3. Choosing option 3 will read a text from the files we have attached.

4. Choosing option 4 will generate random letters from a random letter generator, a prompt will pop up asking you what is the number of letters you want, please enter a number otherwise an error will occur.

5. Choosing option 5 will generate random words from a list of the 8000 most commonly used words, a prompt will pop up asking you what is the number of letters you want, please enter a number otherwise an error will occur.

the output of these cases should be:

A. The text generated or read.

B. All the nodes of the tree. 

C.The text after encoding : 

D. Number of bits used.

E. Number of bits using Ascii 

F. percentage of memory used after huffman encoding. 

G. The text after decoding (without lineBreaks) 

H. A new window will open showing the graph of the tree.

6. Choosing option 6 will use the plotter function of the code,its a function that calls options 2, 3,and 4 with fixed parameters, the output of this case will differ from all the previous cases, it would print 
the percentage of memory used compared to Ascii,for all input sizes and input methods, and an array of input sizes used and 3 arrays of times for each input methods, they are then used to plot the times to determine the complexity.

7. Choosing option 7 will shut down the program, a message saying ("Goodbye") will show and the program terminates after.

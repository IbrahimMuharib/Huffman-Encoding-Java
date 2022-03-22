import java.nio.file.*;
import java.util.*;
import de.svenjacobs.loremipsum.*;

public class HuffmanTree {
	public HuffmanTree Huffman;
	public PQElement head;
	public MinPQ frequencyMinPQ;
	public String[] BinaryCodeArray;
	public String Text;
	public Scanner scan = new Scanner(System.in);
	public String binaryText;

	public HuffmanTree() {
		head = null; // the head(root) of the tree
		frequencyMinPQ = new MinPQ(); // priority queue for sorting based on minimum frequency
		BinaryCodeArray = new String[95]; // 95 ASCII characters
		Text = ""; // the English text to be encoded
	}

	public static void main(String[] args) {
		System.out.println("Welcome to my Huffman Encoding Program!");
		System.out.println("---------------------------------------");
		while (true) {
			HuffmanTree Huffman = new HuffmanTree();
			System.out.println("press 1 to enter your own english text");
			System.out.println("or press 2 to read from the LoremIpsum text generator");
			System.out.println("or press 3 to read from the sampleText");
			System.out.println("or press 4 to read from random letter generator");
			System.out.println("or press 5 to read randomly from the 8000 most common english words");
			System.out.println("or press 6 for automated Plotter");
			System.out.println("or press 7 to shut down");
			int input = Huffman.scan.nextInt(); // reads the user's input
			Huffman.scan.nextLine();
			if (input == 6) {
				int[] n = new int[11];
				for (int i = 0; i <= 2; i++)
					n = Huffman.Plotter(i, n.length, 10, 10000);
				System.out.print("[ ");
				for (int i = 0; i < n.length; i++) {
					System.out.print(n[i] + " ");
				}
				System.out.println("]");
				System.out.println("---------------------------------------");
				continue;
			}
			if (input == 7) {
				System.out.println("GoodBye!");
				break;
			} else if (input > 7 || input <= 0) {
				System.out.println("try again.");
				System.out.println("---------------------------------------");
				continue;
			}
			Huffman.Text = Huffman.generateText(input);
			Huffman.outputResults(true);
			canvas c = new canvas(Huffman.head);

		}

	}

	public int[] Plotter(int input, int repeats, int m, int M) {
		HuffmanTree plottingTree;
		int[] n = new int[repeats];
		long[] times = new long[repeats];
		int max = M;
		int min = m;
		int increment = (int) ((max - min + 0.0) / (n.length - 1.0)) + 1;
		for (int i = 0; i < n.length; i++) {
			plottingTree = new HuffmanTree();
			int N = increment * i;
			if (i == 0)
				N = min;
			plottingTree.Text = plottingTree.generateTextAutomated(input, N);
			long time = System.currentTimeMillis();
			plottingTree.outputResults(false);
			time = System.currentTimeMillis() - time;
			times[i] = time;
			n[i] = N;
			double ratio = plottingTree.binaryText.length() / (plottingTree.Text.length() * 8.0);
			System.out.printf("percentage of memory used after huffman encoding is : %.2f %c %n", 100 * ratio, '%');
		}
		System.out.print("[ ");
		for (int i = 0; i < times.length; i++) {
			System.out.print(times[i] + " ");
		}
		System.out.println("]");
		return n;

	}

	public String generateTextAutomated(int input, int n) {
		String Text = "";
		LoremIpsum textSource = new LoremIpsum();
		if (input == 0) {// common words

			for (int i = 0; i < n; i++) {
				Text += commonWords[(int) (8000 * Math.random())] + " ";
			}

		} else if (input == 1) {// random letters

			for (int i = 0; i < n * 5; i++) {
				Text += (char) (int) (95 * Math.random() + 32);
			}

		} else if (input == 2) {// loremipsum
			Text = textSource.getWords(n, (int) (50 * Math.random()));

		}
		return Text;
	}

	public String generateText(int input) {
		String Text = "";
		LoremIpsum textSource = new LoremIpsum();
		if (input == 5)

		{
			System.out.println("enter text length in number of words :");
			int textlength = scan.nextInt();
			scan.nextLine();
			for (int i = 0; i < textlength; i++) {
				Text += commonWords[(int) (8000 * Math.random())] + " ";
			}

		} else if (input == 4) {
			System.out.println("enter text length in number of letters :");
			int textlength = scan.nextInt();
			scan.nextLine();

			for (int i = 0; i < textlength; i++) {
				Text += (char) (int) (95 * Math.random() + 32);
			}
		} else if (input == 3) {

			// reads the text file from the project folder

			try {
				Text = readFileAsString("sample_text.txt");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		else if (input == 2) {
			System.out.println("enter text length in number of words :");// smaller numbers (length < 50) yield
																			// better (more random) results
			int textlength = scan.nextInt();
			scan.nextLine();
			Text = textSource.getWords(textlength, (int) (50 * Math.random()));

		} else if (input == 1) {
			System.out.println("please enter your text : ");
			Text = scan.nextLine();
			System.out.println();
		}
		return Text;
	}

	public void outputResults(boolean print) {
		if (print) {
			System.out.println("Text before encoding: ");
			System.out.println(this.Text);
		}
		this.TextFiletoMinPQ(); // builds a priority queue based on letter frequencies in the text

		this.head = this.buildBT(); // builds a binary tree based on the previous priority queue

		this.fillStrings(this.head, "");// fills a dictionary(array) of letter with their corresponding binary codes
		if (print) {
			System.out.println("the binary Tree (Breadth first) : ");
			this.print(this.head);// prints the tree breadth first
		}
		this.binaryText = this.TexttoBinary();// uses the binary codes dictionary to turn the text to binary
		String TextResult = this.BinarytoText(binaryText);// turns it back to English text
		if (print) {
			System.out.println();
			System.out.println("the text after encoding : ");
			System.out.println(binaryText);
			System.out.println("Number of bits used is : " + binaryText.length());
			System.out.println("Number of bits using Ascii : " + (8 * this.Text.length()));
			double ratio = this.binaryText.length() / (this.Text.length() * 8.0);
			System.out.printf("percentage of memory used after huffman encoding is : %.2f %c %n", 100 * ratio, '%');
			System.out.println("The text after decoding (without lineBreaks) : ");
			System.out.println(TextResult);
			System.out.println("---------------------------------------");
		}
	}

	private String BinarytoText(String binaryText) {
		PQElement currentNode = head;
		String TextResult = "";
		for (int i = 0; i < binaryText.length(); i++) {
			char currentCharacter = binaryText.charAt(i);
			switch (currentCharacter) {
			case '0':
				currentNode = currentNode.left;
				break;
			case '1':
				currentNode = currentNode.right;
				break;
			}

			if (currentNode.binary != null) {
				TextResult += currentNode.character;
				currentNode = head;
			}

		}
		return TextResult;

	}

	public String TexttoBinary() {
		char[] charArray = Text.toCharArray();
		String binaryText = "";
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] >= 32 && charArray[i] <= 126)
				binaryText += BinaryCodeArray[charArray[i] - 32];
			if (charArray[i] == 10)
				binaryText += BinaryCodeArray[',' - 32];

		}
		return binaryText;

	}

	public void fillStrings(PQElement current, String binary) {
		if (current.character >= 32 && current.character <= 126) {
			BinaryCodeArray[current.character - 32] = binary;
			current.binary = binary;
		}
		if (current.left != null)
			fillStrings(current.left, binary + "0");
		if (current.right != null)
			fillStrings(current.right, binary + "1");

	}

	public PQElement buildBT() {
		PQElement current = null;
		if (frequencyMinPQ.length() == 1) {
			PQElement current1 = frequencyMinPQ.serve();
			current = new PQElement((char) -1, current1.frequency);
			current.left = current1;
		} else {
			while (frequencyMinPQ.length() > 1) {
				PQElement current1 = frequencyMinPQ.serve();
				PQElement current2 = frequencyMinPQ.serve();
				current = new PQElement((char) -1, current1.frequency + current2.frequency);
				current.left = current1;
				current.right = current2;
				frequencyMinPQ.enqueue(current);

			}
		}
		return current;

	}

	private static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		return data;
	}

	public MinPQ TextFiletoMinPQ() {
		char[] textArray = Text.toCharArray();

		// sortArray(textArray, 0, textArray.length - 1); // sorting array to help find
		// the frequencies

		int[] frequencyArray = new int[95];// 95 ASCII characters

		for (int i = 0; i < textArray.length; i++) {
			int currentletter = textArray[i];
			if (currentletter >= 32 && currentletter <= 126) {
				frequencyArray[currentletter - 32]++;
			}
			if (currentletter == 10)
				frequencyArray[',' - 32]++;

		}
		for (int i = 0; i < frequencyArray.length; i++) { // moves from frequency array to frequencyMinPQ
			if (frequencyArray[i] != 0)
				frequencyMinPQ.enqueue(new PQElement((char) (i + 32), frequencyArray[i]));
		}

		return frequencyMinPQ; // return the resulting priority queue based on the frequency

	}

	public void print(PQElement root) {
		int h = height(root);
		int i;
		for (i = 1; i <= h; i++)
			printGivenLevel(root, i);
	}

	int height(PQElement root) {
		if (root == null)
			return 0;
		else {
			int lheight = height(root.left);
			int rheight = height(root.right);

			if (lheight > rheight)
				return (lheight + 1);
			else
				return (rheight + 1);
		}
	}

	void printGivenLevel(PQElement root, int level) {
		if (root == null)
			return;
		if (level == 1) {
			if (root.binary != null) {
				System.out.print("( " + root.character + " , " + root.frequency + " , " + root.binary + " ) " + " ");
			} else {
				System.out.print("( " + root.character + " , " + root.frequency + " ) " + " ");
			}
		} else if (level > 1) {
			printGivenLevel(root.left, level - 1);
			printGivenLevel(root.right, level - 1);
		}
	}

	// the 8000 most common English words
	public static String[] commonWords = { "the", "of", "and", "to", "a", "in", "for", "is", "on", "that", "by", "this",
			"with", "i", "you", "it", "not", "or", "be", "are", "from", "at", "as", "your", "all", "have", "new",
			"more", "an", "was", "we", "will", "home", "can", "us", "about", "if", "page", "my", "has", "search",
			"free", "but", "our", "one", "other", "do", "no", "information", "time", "they", "site", "he", "up", "may",
			"what", "which", "their", "news", "out", "use", "any", "there", "see", "only", "so", "his", "when",
			"contact", "here", "business", "who", "web", "also", "now", "help", "get", "pm", "view", "online", "c", "e",
			"first", "am", "been", "would", "how", "were", "me", "s", "services", "some", "these", "click", "its",
			"like", "service", "x", "than", "find", "price", "date", "back", "top", "people", "had", "list", "name",
			"just", "over", "state", "year", "day", "into", "email", "two", "health", "n", "world", "re", "next",
			"used", "go", "b", "work", "last", "most", "products", "music", "buy", "data", "make", "them", "should",
			"product", "system", "post", "her", "city", "t", "add", "policy", "number", "such", "please", "available",
			"copyright", "support", "message", "after", "best", "software", "then", "jan", "good", "video", "well", "d",
			"where", "info", "rights", "public", "books", "high", "school", "through", "m", "each", "links", "she",
			"review", "years", "order", "very", "privacy", "book", "items", "company", "r", "read", "group", "sex",
			"need", "many", "user", "said", "de", "does", "set", "under", "general", "research", "university",
			"january", "mail", "full", "map", "reviews", "program", "life", "know", "games", "way", "days",
			"management", "p", "part", "could", "great", "united", "hotel", "real", "f", "item", "international",
			"center", "ebay", "must", "store", "travel", "comments", "made", "development", "report", "off", "member",
			"details", "line", "terms", "before", "hotels", "did", "send", "right", "type", "because", "local", "those",
			"using", "results", "office", "education", "national", "car", "design", "take", "posted", "internet",
			"address", "community", "within", "states", "area", "want", "phone", "dvd", "shipping", "reserved",
			"subject", "between", "forum", "family", "l", "long", "based", "w", "code", "show", "o", "even", "black",
			"check", "special", "prices", "website", "index", "being", "women", "much", "sign", "file", "link", "open",
			"today", "technology", "south", "case", "project", "same", "pages", "uk", "version", "section", "own",
			"found", "sports", "house", "related", "security", "both", "g", "county", "american", "photo", "game",
			"members", "power", "while", "care", "network", "down", "computer", "systems", "three", "total", "place",
			"end", "following", "download", "h", "him", "without", "per", "access", "think", "north", "resources",
			"current", "posts", "big", "media", "law", "control", "water", "history", "pictures", "size", "art",
			"personal", "since", "including", "guide", "shop", "directory", "board", "location", "change", "white",
			"text", "small", "rating", "rate", "government", "children", "during", "usa", "return", "students", "v",
			"shopping", "account", "times", "sites", "level", "digital", "profile", "previous", "form", "events",
			"love", "old", "john", "main", "call", "hours", "image", "department", "title", "description", "non", "k",
			"y", "insurance", "another", "why", "shall", "property", "class", "cd", "still", "money", "quality",
			"every", "listing", "content", "country", "private", "little", "visit", "save", "tools", "low", "reply",
			"customer", "december", "compare", "movies", "include", "college", "value", "article", "york", "man",
			"card", "jobs", "provide", "j", "food", "source", "author", "different", "press", "u", "learn", "sale",
			"around", "print", "course", "job", "canada", "process", "teen", "room", "stock", "training", "too",
			"credit", "point", "join", "science", "men", "categories", "advanced", "west", "sales", "look", "english",
			"left", "team", "estate", "box", "conditions", "select", "windows", "photos", "gay", "thread", "week",
			"category", "note", "live", "large", "gallery", "table", "register", "however", "june", "october",
			"november", "market", "library", "really", "action", "start", "series", "model", "features", "air",
			"industry", "plan", "human", "provided", "tv", "yes", "required", "second", "hot", "accessories", "cost",
			"movie", "forums", "march", "la", "september", "better", "say", "questions", "july", "yahoo", "going",
			"medical", "test", "friend", "come", "dec", "server", "pc", "study", "application", "cart", "staff",
			"articles", "san", "feedback", "again", "play", "looking", "issues", "april", "never", "users", "complete",
			"street", "topic", "comment", "financial", "things", "working", "against", "standard", "tax", "person",
			"below", "mobile", "less", "got", "blog", "party", "payment", "equipment", "login", "student", "let",
			"programs", "offers", "legal", "above", "recent", "park", "stores", "side", "act", "problem", "red", "give",
			"memory", "performance", "social", "q", "august", "quote", "language", "story", "sell", "options",
			"experience", "rates", "create", "key", "body", "young", "america", "important", "field", "few", "east",
			"paper", "single", "ii", "age", "activities", "club", "example", "girls", "additional", "password", "z",
			"latest", "something", "road", "gift", "question", "changes", "night", "ca", "hard", "texas", "oct", "pay",
			"four", "poker", "status", "browse", "issue", "range", "building", "seller", "court", "february", "always",
			"result", "audio", "light", "write", "war", "nov", "offer", "blue", "groups", "al", "easy", "given",
			"files", "event", "release", "analysis", "request", "fax", "china", "making", "picture", "needs",
			"possible", "might", "professional", "yet", "month", "major", "star", "areas", "future", "space",
			"committee", "hand", "sun", "cards", "problems", "london", "washington", "meeting", "rss", "become",
			"interest", "id", "child", "keep", "enter", "california", "porn", "share", "similar", "garden", "schools",
			"million", "added", "reference", "companies", "listed", "baby", "learning", "energy", "run", "delivery",
			"net", "popular", "term", "film", "stories", "put", "computers", "journal", "reports", "co", "try",
			"welcome", "central", "images", "president", "notice", "god", "original", "head", "radio", "until", "cell",
			"color", "self", "council", "away", "includes", "track", "australia", "discussion", "archive", "once",
			"others", "entertainment", "agreement", "format", "least", "society", "months", "log", "safety", "friends",
			"sure", "faq", "trade", "edition", "cars", "messages", "marketing", "tell", "further", "updated",
			"association", "able", "having", "provides", "david", "fun", "already", "green", "studies", "close",
			"common", "drive", "specific", "several", "gold", "feb", "living", "sep", "collection", "called", "short",
			"arts", "lot", "ask", "display", "limited", "powered", "solutions", "means", "director", "daily", "beach",
			"past", "natural", "whether", "due", "et", "electronics", "five", "upon", "period", "planning", "database",
			"says", "official", "weather", "mar", "land", "average", "done", "technical", "window", "france", "pro",
			"region", "island", "record", "direct", "microsoft", "conference", "environment", "records", "st",
			"district", "calendar", "costs", "style", "url", "front", "statement", "update", "parts", "aug", "ever",
			"downloads", "early", "miles", "sound", "resource", "present", "applications", "either", "ago", "document",
			"word", "works", "material", "bill", "apr", "written", "talk", "federal", "hosting", "rules", "final",
			"adult", "tickets", "thing", "centre", "requirements", "via", "cheap", "nude", "kids", "finance", "true",
			"minutes", "else", "mark", "third", "rock", "gifts", "europe", "reading", "topics", "bad", "individual",
			"tips", "plus", "auto", "cover", "usually", "edit", "together", "videos", "percent", "fast", "function",
			"fact", "unit", "getting", "global", "tech", "meet", "far", "economic", "en", "player", "projects",
			"lyrics", "often", "subscribe", "submit", "germany", "amount", "watch", "included", "feel", "though",
			"bank", "risk", "thanks", "everything", "deals", "various", "words", "linux", "jul", "production",
			"commercial", "james", "weight", "town", "heart", "advertising", "received", "choose", "treatment",
			"newsletter", "archives", "points", "knowledge", "magazine", "error", "camera", "jun", "girl", "currently",
			"construction", "toys", "registered", "clear", "golf", "receive", "domain", "methods", "chapter", "makes",
			"protection", "policies", "loan", "wide", "beauty", "manager", "india", "position", "taken", "sort",
			"listings", "models", "michael", "known", "half", "cases", "step", "engineering", "florida", "simple",
			"quick", "none", "wireless", "license", "paul", "friday", "lake", "whole", "annual", "published", "later",
			"basic", "sony", "shows", "corporate", "google", "church", "method", "purchase", "customers", "active",
			"response", "practice", "hardware", "figure", "materials", "fire", "holiday", "chat", "enough", "designed",
			"along", "among", "death", "writing", "speed", "html", "countries", "loss", "face", "brand", "discount",
			"higher", "effects", "created", "remember", "standards", "oil", "bit", "yellow", "political", "increase",
			"advertise", "kingdom", "base", "near", "environmental", "thought", "stuff", "french", "storage", "oh",
			"japan", "doing", "loans", "shoes", "entry", "stay", "nature", "orders", "availability", "africa",
			"summary", "turn", "mean", "growth", "notes", "agency", "king", "monday", "european", "activity", "copy",
			"although", "drug", "pics", "western", "income", "force", "cash", "employment", "overall", "bay", "river",
			"commission", "ad", "package", "contents", "seen", "players", "engine", "port", "album", "regional", "stop",
			"supplies", "started", "administration", "bar", "institute", "views", "plans", "double", "dog", "build",
			"screen", "exchange", "types", "soon", "sponsored", "lines", "electronic", "continue", "across", "benefits",
			"needed", "season", "apply", "someone", "held", "ny", "anything", "printer", "condition", "effective",
			"believe", "organization", "effect", "asked", "eur", "mind", "sunday", "selection", "casino", "pdf", "lost",
			"tour", "menu", "volume", "cross", "anyone", "mortgage", "hope", "silver", "corporation", "wish", "inside",
			"solution", "mature", "role", "rather", "weeks", "addition", "came", "supply", "nothing", "certain", "usr",
			"executive", "running", "lower", "necessary", "union", "jewelry", "according", "dc", "clothing", "mon",
			"com", "particular", "fine", "names", "robert", "homepage", "hour", "gas", "skills", "six", "bush",
			"islands", "advice", "career", "military", "rental", "decision", "leave", "british", "teens", "pre", "huge",
			"sat", "woman", "facilities", "zip", "bid", "kind", "sellers", "middle", "move", "cable", "opportunities",
			"taking", "values", "division", "coming", "tuesday", "object", "lesbian", "appropriate", "machine", "logo",
			"length", "actually", "nice", "score", "statistics", "client", "ok", "returns", "capital", "follow",
			"sample", "investment", "sent", "shown", "saturday", "christmas", "england", "culture", "band", "flash",
			"ms", "lead", "george", "choice", "went", "starting", "registration", "fri", "thursday", "courses",
			"consumer", "hi", "airport", "foreign", "artist", "outside", "furniture", "levels", "channel", "letter",
			"mode", "phones", "ideas", "wednesday", "structure", "fund", "summer", "allow", "degree", "contract",
			"button", "releases", "wed", "homes", "super", "male", "matter", "custom", "virginia", "almost", "took",
			"located", "multiple", "asian", "distribution", "editor", "inn", "industrial", "cause", "potential", "song",
			"cnet", "ltd", "los", "hp", "focus", "late", "fall", "featured", "idea", "rooms", "female", "responsible",
			"inc", "communications", "win", "associated", "thomas", "primary", "cancer", "numbers", "reason", "tool",
			"browser", "spring", "foundation", "answer", "voice", "eg", "friendly", "schedule", "documents",
			"communication", "purpose", "feature", "bed", "comes", "police", "everyone", "independent", "ip",
			"approach", "cameras", "brown", "physical", "operating", "hill", "maps", "medicine", "deal", "hold",
			"ratings", "chicago", "forms", "glass", "happy", "tue", "smith", "wanted", "developed", "thank", "safe",
			"unique", "survey", "prior", "telephone", "sport", "ready", "feed", "animal", "sources", "mexico",
			"population", "pa", "regular", "secure", "navigation", "operations", "therefore", "ass", "simply",
			"evidence", "station", "christian", "round", "paypal", "favorite", "understand", "option", "master",
			"valley", "recently", "probably", "thu", "rentals", "sea", "built", "publications", "blood", "cut",
			"worldwide", "improve", "connection", "publisher", "hall", "larger", "anti", "networks", "earth", "parents",
			"nokia", "impact", "transfer", "introduction", "kitchen", "strong", "tel", "carolina", "wedding",
			"properties", "hospital", "ground", "overview", "ship", "accommodation", "owners", "disease", "tx",
			"excellent", "paid", "italy", "perfect", "hair", "opportunity", "kit", "classic", "basis", "command",
			"cities", "william", "express", "anal", "award", "distance", "tree", "peter", "assessment", "ensure",
			"thus", "wall", "ie", "involved", "el", "extra", "especially", "interface", "pussy", "partners", "budget",
			"rated", "guides", "success", "maximum", "ma", "operation", "existing", "quite", "selected", "boy",
			"amazon", "patients", "restaurants", "beautiful", "warning", "wine", "locations", "horse", "vote",
			"forward", "flowers", "stars", "significant", "lists", "technologies", "owner", "retail", "animals",
			"useful", "directly", "manufacturer", "ways", "est", "son", "providing", "rule", "mac", "housing", "takes",
			"iii", "gmt", "bring", "catalog", "searches", "max", "trying", "mother", "authority", "considered", "told",
			"xml", "traffic", "programme", "joined", "input", "strategy", "feet", "agent", "valid", "bin", "modern",
			"senior", "ireland", "sexy", "teaching", "door", "grand", "testing", "trial", "charge", "units", "instead",
			"canadian", "cool", "normal", "wrote", "enterprise", "ships", "entire", "educational", "md", "leading",
			"metal", "positive", "fl", "fitness", "chinese", "opinion", "mb", "asia", "football", "abstract", "uses",
			"output", "funds", "mr", "greater", "likely", "develop", "employees", "artists", "alternative",
			"processing", "responsibility", "resolution", "java", "guest", "seems", "publication", "pass", "relations",
			"trust", "van", "contains", "session", "multi", "photography", "republic", "fees", "components", "vacation",
			"century", "academic", "assistance", "completed", "skin", "graphics", "indian", "prev", "ads", "mary", "il",
			"expected", "ring", "grade", "dating", "pacific", "mountain", "organizations", "pop", "filter", "mailing",
			"vehicle", "longer", "consider", "int", "northern", "behind", "panel", "floor", "german", "buying", "match",
			"proposed", "default", "require", "iraq", "boys", "outdoor", "deep", "morning", "otherwise", "allows",
			"rest", "protein", "plant", "reported", "hit", "transportation", "mm", "pool", "mini", "politics",
			"partner", "disclaimer", "authors", "boards", "faculty", "parties", "fish", "membership", "mission", "eye",
			"string", "sense", "modified", "pack", "released", "stage", "internal", "goods", "recommended", "born",
			"unless", "richard", "detailed", "japanese", "race", "approved", "background", "target", "except",
			"character", "usb", "maintenance", "ability", "maybe", "functions", "ed", "moving", "brands", "places",
			"php", "pretty", "trademarks", "phentermine", "spain", "southern", "yourself", "etc", "winter", "rape",
			"battery", "youth", "pressure", "submitted", "boston", "incest", "debt", "keywords", "medium", "television",
			"interested", "core", "break", "purposes", "throughout", "sets", "dance", "wood", "msn", "itself",
			"defined", "papers", "playing", "awards", "fee", "studio", "reader", "virtual", "device", "established",
			"answers", "rent", "las", "remote", "dark", "programming", "external", "apple", "le", "regarding",
			"instructions", "min", "offered", "theory", "enjoy", "remove", "aid", "surface", "minimum", "visual",
			"host", "variety", "teachers", "isbn", "martin", "manual", "block", "subjects", "agents", "increased",
			"repair", "fair", "civil", "steel", "understanding", "songs", "fixed", "wrong", "beginning", "hands",
			"associates", "finally", "az", "updates", "desktop", "classes", "paris", "ohio", "gets", "sector",
			"capacity", "requires", "jersey", "un", "fat", "fully", "father", "electric", "saw", "instruments",
			"quotes", "officer", "driver", "businesses", "dead", "respect", "unknown", "specified", "restaurant",
			"mike", "trip", "pst", "worth", "mi", "procedures", "poor", "teacher", "xxx", "eyes", "relationship",
			"workers", "farm", "fucking", "georgia", "peace", "traditional", "campus", "tom", "showing", "creative",
			"coast", "benefit", "progress", "funding", "devices", "lord", "grant", "sub", "agree", "fiction", "hear",
			"sometimes", "watches", "careers", "beyond", "goes", "families", "led", "museum", "themselves", "fan",
			"transport", "interesting", "blogs", "wife", "evaluation", "accepted", "former", "implementation", "ten",
			"hits", "zone", "complex", "th", "cat", "galleries", "references", "die", "presented", "jack", "flat",
			"flow", "agencies", "literature", "respective", "parent", "spanish", "michigan", "columbia", "setting",
			"dr", "scale", "stand", "economy", "highest", "helpful", "monthly", "critical", "frame", "musical",
			"definition", "secretary", "angeles", "networking", "path", "australian", "employee", "chief", "gives",
			"kb", "bottom", "magazines", "packages", "detail", "francisco", "laws", "changed", "pet", "heard", "begin",
			"individuals", "colorado", "royal", "clean", "switch", "russian", "largest", "african", "guy", "titles",
			"relevant", "guidelines", "justice", "connect", "bible", "dev", "cup", "basket", "applied", "weekly", "vol",
			"installation", "described", "demand", "pp", "suite", "vegas", "na", "square", "chris", "attention",
			"advance", "skip", "diet", "army", "auction", "gear", "lee", "os", "difference", "allowed", "correct",
			"charles", "nation", "selling", "lots", "piece", "sheet", "firm", "seven", "older", "illinois",
			"regulations", "elements", "species", "jump", "cells", "module", "resort", "facility", "random", "pricing",
			"dvds", "certificate", "minister", "motion", "looks", "fashion", "directions", "visitors", "documentation",
			"monitor", "trading", "forest", "calls", "whose", "coverage", "couple", "giving", "chance", "vision",
			"ball", "ending", "clients", "actions", "listen", "discuss", "accept", "automotive", "naked", "goal",
			"successful", "sold", "wind", "communities", "clinical", "situation", "sciences", "markets", "lowest",
			"highly", "publishing", "appear", "emergency", "developing", "lives", "currency", "leather", "determine",
			"milf", "temperature", "palm", "announcements", "patient", "actual", "historical", "stone", "bob",
			"commerce", "ringtones", "perhaps", "persons", "difficult", "scientific", "satellite", "fit", "tests",
			"village", "accounts", "amateur", "ex", "met", "pain", "xbox", "particularly", "factors", "coffee", "www",
			"settings", "cum", "buyer", "cultural", "steve", "easily", "oral", "ford", "poster", "edge", "functional",
			"root", "au", "fi", "closed", "holidays", "ice", "pink", "zealand", "balance", "monitoring", "graduate",
			"replies", "shot", "nc", "architecture", "initial", "label", "thinking", "scott", "llc", "sec", "recommend",
			"canon", "hardcore", "league", "waste", "minute", "bus", "provider", "optional", "dictionary", "cold",
			"accounting", "manufacturing", "sections", "chair", "fishing", "effort", "phase", "fields", "bag",
			"fantasy", "po", "letters", "motor", "va", "professor", "context", "install", "shirt", "apparel",
			"generally", "continued", "foot", "mass", "crime", "count", "breast", "techniques", "ibm", "rd", "johnson",
			"sc", "quickly", "dollars", "websites", "religion", "claim", "driving", "permission", "surgery", "patch",
			"heat", "wild", "measures", "generation", "kansas", "miss", "chemical", "doctor", "task", "reduce",
			"brought", "himself", "nor", "component", "enable", "exercise", "bug", "santa", "mid", "guarantee",
			"leader", "diamond", "israel", "se", "processes", "soft", "servers", "alone", "meetings", "seconds",
			"jones", "arizona", "keyword", "interests", "flight", "congress", "fuel", "username", "walk", "fuck",
			"produced", "italian", "paperback", "classifieds", "wait", "supported", "pocket", "saint", "rose",
			"freedom", "argument", "competition", "creating", "jim", "drugs", "joint", "premium", "providers", "fresh",
			"characters", "attorney", "upgrade", "di", "factor", "growing", "thousands", "km", "stream", "apartments",
			"pick", "hearing", "eastern", "auctions", "therapy", "entries", "dates", "generated", "signed", "upper",
			"administrative", "serious", "prime", "samsung", "limit", "began", "louis", "steps", "errors", "shops",
			"bondage", "del", "efforts", "informed", "ga", "ac", "thoughts", "creek", "ft", "worked", "quantity",
			"urban", "practices", "sorted", "reporting", "essential", "myself", "tours", "platform", "load",
			"affiliate", "labor", "immediately", "admin", "nursing", "defense", "machines", "designated", "tags",
			"heavy", "covered", "recovery", "joe", "guys", "integrated", "configuration", "cock", "merchant",
			"comprehensive", "expert", "universal", "protect", "drop", "solid", "cds", "presentation", "languages",
			"became", "orange", "compliance", "vehicles", "prevent", "theme", "rich", "im", "campaign", "marine",
			"improvement", "vs", "guitar", "finding", "pennsylvania", "examples", "ipod", "saying", "spirit", "ar",
			"claims", "porno", "challenge", "motorola", "acceptance", "strategies", "mo", "seem", "affairs", "touch",
			"intended", "towards", "sa", "goals", "hire", "election", "suggest", "branch", "charges", "serve",
			"affiliates", "reasons", "magic", "mount", "smart", "talking", "gave", "ones", "latin", "multimedia", "xp",
			"tits", "avoid", "certified", "manage", "corner", "rank", "computing", "oregon", "element", "birth",
			"virus", "abuse", "interactive", "requests", "separate", "quarter", "procedure", "leadership", "tables",
			"define", "racing", "religious", "facts", "breakfast", "kong", "column", "plants", "faith", "chain",
			"developer", "identify", "avenue", "missing", "died", "approximately", "domestic", "sitemap",
			"recommendations", "moved", "houston", "reach", "comparison", "mental", "viewed", "moment", "extended",
			"sequence", "inch", "attack", "sorry", "centers", "opening", "damage", "lab", "reserve", "recipes", "cvs",
			"gamma", "plastic", "produce", "snow", "placed", "truth", "counter", "failure", "follows", "eu", "weekend",
			"dollar", "camp", "ontario", "automatically", "des", "minnesota", "films", "bridge", "native", "fill",
			"williams", "movement", "printing", "baseball", "owned", "approval", "draft", "chart", "played", "contacts",
			"cc", "jesus", "readers", "clubs", "lcd", "wa", "jackson", "equal", "adventure", "matching", "offering",
			"shirts", "profit", "leaders", "posters", "institutions", "assistant", "variable", "ave", "dj",
			"advertisement", "expect", "parking", "headlines", "yesterday", "compared", "determined", "wholesale",
			"workshop", "russia", "gone", "codes", "kinds", "extension", "seattle", "statements", "golden",
			"completely", "teams", "fort", "cm", "wi", "lighting", "senate", "forces", "funny", "brother", "gene",
			"turned", "portable", "tried", "electrical", "applicable", "disc", "returned", "pattern", "ct", "hentai",
			"boat", "named", "theatre", "laser", "earlier", "manufacturers", "sponsor", "classical", "icon", "warranty",
			"dedicated", "indiana", "direction", "harry", "basketball", "objects", "ends", "delete", "evening",
			"assembly", "nuclear", "taxes", "mouse", "signal", "criminal", "issued", "brain", "sexual", "wisconsin",
			"powerful", "dream", "obtained", "false", "da", "cast", "flower", "felt", "personnel", "passed", "supplied",
			"identified", "falls", "pic", "soul", "aids", "opinions", "promote", "stated", "stats", "hawaii",
			"professionals", "appears", "carry", "flag", "decided", "nj", "covers", "hr", "em", "advantage", "hello",
			"designs", "maintain", "tourism", "priority", "newsletters", "adults", "clips", "savings", "iv", "graphic",
			"atom", "payments", "rw", "estimated", "binding", "brief", "ended", "winning", "eight", "anonymous", "iron",
			"straight", "script", "served", "wants", "miscellaneous", "prepared", "void", "dining", "alert",
			"integration", "atlanta", "dakota", "tag", "interview", "mix", "framework", "disk", "installed", "queen",
			"vhs", "credits", "clearly", "fix", "handle", "sweet", "desk", "criteria", "pubmed", "dave",
			"massachusetts", "diego", "hong", "vice", "associate", "ne", "truck", "behavior", "enlarge", "ray",
			"frequently", "revenue", "measure", "changing", "votes", "du", "duty", "looked", "discussions", "bear",
			"gain", "festival", "laboratory", "ocean", "flights", "experts", "signs", "lack", "depth", "iowa",
			"whatever", "logged", "laptop", "vintage", "train", "exactly", "dry", "explore", "maryland", "spa",
			"concept", "nearly", "eligible", "checkout", "reality", "forgot", "handling", "origin", "knew", "gaming",
			"feeds", "billion", "destination", "scotland", "faster", "intelligence", "dallas", "bought", "con", "ups",
			"nations", "route", "followed", "specifications", "broken", "tripadvisor", "frank", "alaska", "zoom",
			"blow", "battle", "residential", "anime", "speak", "decisions", "industries", "protocol", "query", "clip",
			"partnership", "editorial", "nt", "expression", "es", "equity", "provisions", "speech", "wire",
			"principles", "suggestions", "rural", "shared", "sounds", "replacement", "tape", "strategic", "judge",
			"spam", "economics", "acid", "bytes", "cent", "forced", "compatible", "fight", "apartment", "height",
			"null", "zero", "speaker", "filed", "gb", "netherlands", "obtain", "bc", "consulting", "recreation",
			"offices", "designer", "remain", "managed", "pr", "failed", "marriage", "roll", "korea", "banks", "fr",
			"participants", "secret", "bath", "aa", "kelly", "leads", "negative", "austin", "favorites", "toronto",
			"theater", "springs", "missouri", "andrew", "var", "perform", "healthy", "translation", "estimates", "font",
			"assets", "injury", "mt", "joseph", "ministry", "drivers", "lawyer", "figures", "married", "protected",
			"proposal", "sharing", "philadelphia", "portal", "waiting", "birthday", "beta", "fail", "gratis", "banking",
			"officials", "brian", "toward", "won", "slightly", "assist", "conduct", "contained", "lingerie", "shemale",
			"legislation", "calling", "parameters", "jazz", "serving", "bags", "profiles", "miami", "comics", "matters",
			"houses", "doc", "postal", "relationships", "tennessee", "wear", "controls", "breaking", "combined",
			"ultimate", "wales", "representative", "frequency", "introduced", "minor", "finish", "departments",
			"residents", "noted", "displayed", "mom", "reduced", "physics", "rare", "spent", "performed", "extreme",
			"samples", "davis", "daniel", "bars", "reviewed", "row", "oz", "forecast", "removed", "helps", "singles",
			"administrator", "cycle", "amounts", "contain", "accuracy", "dual", "rise", "usd", "sleep", "mg", "bird",
			"pharmacy", "brazil", "creation", "static", "scene", "hunter", "addresses", "lady", "crystal", "famous",
			"writer", "chairman", "violence", "fans", "oklahoma", "speakers", "drink", "academy", "dynamic", "gender",
			"eat", "permanent", "agriculture", "dell", "cleaning", "constitutes", "portfolio", "practical", "delivered",
			"collectibles", "infrastructure", "exclusive", "seat", "concerns", "colour", "vendor", "originally",
			"intel", "utilities", "philosophy", "regulation", "officers", "reduction", "aim", "bids", "referred",
			"supports", "nutrition", "recording", "regions", "junior", "toll", "les", "cape", "ann", "rings", "meaning",
			"tip", "secondary", "wonderful", "mine", "ladies", "henry", "ticket", "announced", "guess", "agreed",
			"prevention", "whom", "ski", "soccer", "math", "import", "posting", "presence", "instant", "mentioned",
			"automatic", "healthcare", "viewing", "maintained", "ch", "increasing", "majority", "connected", "christ",
			"dan", "dogs", "sd", "directors", "aspects", "austria", "ahead", "moon", "participation", "scheme",
			"utility", "preview", "fly", "manner", "matrix", "containing", "combination", "devel", "amendment",
			"despite", "strength", "guaranteed", "turkey", "libraries", "proper", "distributed", "degrees", "singapore",
			"enterprises", "delta", "fear", "seeking", "inches", "phoenix", "rs", "convention", "shares", "principal",
			"daughter", "standing", "voyeur", "comfort", "colors", "wars", "cisco", "ordering", "kept", "alpha",
			"appeal", "cruise", "bonus", "certification", "previously", "hey", "bookmark", "buildings", "specials",
			"beat", "disney", "household", "batteries", "adobe", "smoking", "bbc", "becomes", "drives", "arms",
			"alabama", "tea", "improved", "trees", "avg", "achieve", "positions", "dress", "subscription", "dealer",
			"contemporary", "sky", "utah", "nearby", "rom", "carried", "happen", "exposure", "panasonic", "hide",
			"permalink", "signature", "gambling", "refer", "miller", "provision", "outdoors", "clothes", "caused",
			"luxury", "babes", "frames", "viagra", "certainly", "indeed", "newspaper", "toy", "circuit", "layer",
			"printed", "slow", "removal", "easier", "src", "liability", "trademark", "hip", "printers", "faqs", "nine",
			"adding", "kentucky", "mostly", "eric", "spot", "taylor", "trackback", "prints", "spend", "factory",
			"interior", "revised", "grow", "americans", "optical", "promotion", "relative", "amazing", "clock", "dot",
			"hiv", "identity", "suites", "conversion", "feeling", "hidden", "reasonable", "victoria", "serial",
			"relief", "revision", "broadband", "influence", "ratio", "pda", "importance", "rain", "onto", "dsl",
			"planet", "webmaster", "copies", "recipe", "zum", "permit", "seeing", "proof", "dna", "diff", "tennis",
			"bass", "prescription", "bedroom", "empty", "instance", "hole", "pets", "ride", "licensed", "orlando",
			"specifically", "tim", "bureau", "maine", "sql", "represent", "conservation", "pair", "ideal", "specs",
			"recorded", "don", "pieces", "finished", "parks", "dinner", "lawyers", "sydney", "stress", "cream", "ss",
			"runs", "trends", "yeah", "discover", "sexo", "ap", "patterns", "boxes", "louisiana", "hills", "javascript",
			"fourth", "nm", "advisor", "mn", "marketplace", "nd", "evil", "aware", "wilson", "shape", "evolution",
			"irish", "certificates", "objectives", "stations", "suggested", "gps", "op", "remains", "acc", "greatest",
			"firms", "concerned", "euro", "operator", "structures", "generic", "encyclopedia", "usage", "cap", "ink",
			"charts", "continuing", "mixed", "census", "interracial", "peak", "tn", "competitive", "exist", "wheel",
			"transit", "dick", "suppliers", "salt", "compact", "poetry", "lights", "tracking", "angel", "bell",
			"keeping", "preparation", "attempt", "receiving", "matches", "accordance", "width", "noise", "engines",
			"forget", "array", "discussed", "accurate", "stephen", "elizabeth", "climate", "reservations", "pin",
			"playstation", "alcohol", "greek", "instruction", "managing", "annotation", "sister", "raw", "differences",
			"walking", "explain", "smaller", "newest", "establish", "gnu", "happened", "expressed", "jeff", "extent",
			"sharp", "lesbians", "ben", "lane", "paragraph", "kill", "mathematics", "aol", "compensation", "ce",
			"export", "managers", "aircraft", "modules", "sweden", "conflict", "conducted", "versions", "employer",
			"occur", "percentage", "knows", "mississippi", "describe", "concern", "backup", "requested", "citizens",
			"connecticut", "heritage", "personals", "immediate", "holding", "trouble", "spread", "coach", "kevin",
			"agricultural", "expand", "supporting", "audience", "assigned", "jordan", "collections", "ages",
			"participate", "plug", "specialist", "cook", "affect", "virgin", "experienced", "investigation", "raised",
			"hat", "institution", "directed", "dealers", "searching", "sporting", "helping", "perl", "affected", "lib",
			"bike", "totally", "plate", "expenses", "indicate", "blonde", "ab", "proceedings", "favourite",
			"transmission", "anderson", "utc", "characteristics", "der", "lose", "organic", "seek", "experiences",
			"albums", "cheats", "extremely", "verzeichnis", "contracts", "guests", "hosted", "diseases", "concerning",
			"developers", "equivalent", "chemistry", "tony", "neighborhood", "nevada", "kits", "thailand", "variables",
			"agenda", "anyway", "continues", "tracks", "advisory", "cam", "curriculum", "logic", "template", "prince",
			"circle", "soil", "grants", "anywhere", "psychology", "responses", "atlantic", "wet", "circumstances",
			"edward", "investor", "identification", "ram", "leaving", "wildlife", "appliances", "matt", "elementary",
			"cooking", "speaking", "sponsors", "fox", "unlimited", "respond", "sizes", "plain", "exit", "entered",
			"iran", "arm", "keys", "launch", "wave", "checking", "costa", "belgium", "printable", "holy", "acts",
			"guidance", "mesh", "trail", "enforcement", "symbol", "crafts", "highway", "buddy", "hardcover", "observed",
			"dean", "setup", "poll", "booking", "glossary", "fiscal", "celebrity", "styles", "denver", "unix", "filled",
			"bond", "channels", "ericsson", "appendix", "notify", "blues", "chocolate", "pub", "portion", "scope",
			"hampshire", "supplier", "cables", "cotton", "bluetooth", "controlled", "requirement", "authorities",
			"biology", "dental", "killed", "border", "ancient", "debate", "representatives", "starts", "pregnancy",
			"causes", "arkansas", "biography", "leisure", "attractions", "learned", "transactions", "notebook",
			"explorer", "historic", "attached", "opened", "tm", "husband", "disabled", "authorized", "crazy",
			"upcoming", "britain", "concert", "retirement", "scores", "financing", "efficiency", "sp", "comedy",
			"adopted", "efficient", "weblog", "linear", "commitment", "specialty", "bears", "jean", "hop", "carrier",
			"edited", "constant", "visa", "mouth", "jewish", "meter", "linked", "portland", "interviews", "concepts",
			"nh", "gun", "reflect", "pure", "deliver", "wonder", "hell", "lessons", "fruit", "begins", "qualified",
			"reform", "lens", "alerts", "treated", "discovery", "draw", "mysql", "classified", "relating", "assume",
			"confidence", "alliance", "fm", "confirm", "warm", "neither", "lewis", "howard", "offline", "leaves",
			"engineer", "lifestyle", "consistent", "replace", "clearance", "connections", "inventory", "converter",
			"suck", "organisation", "babe", "checks", "reached", "becoming", "blowjob", "safari", "objective",
			"indicated", "sugar", "crew", "legs", "sam", "stick", "securities", "allen", "pdt", "relation", "enabled",
			"genre", "slide", "montana", "volunteer", "tested", "rear", "democratic", "enhance", "switzerland", "exact",
			"bound", "parameter", "adapter", "processor", "node", "formal", "dimensions", "contribute", "lock",
			"hockey", "storm", "micro", "colleges", "laptops", "mile", "showed", "challenges", "editors", "mens",
			"threads", "bowl", "supreme", "brothers", "recognition", "presents", "ref", "tank", "submission", "dolls",
			"estimate", "encourage", "navy", "kid", "regulatory", "inspection", "consumers", "cancel", "limits",
			"territory", "transaction", "manchester", "weapons", "paint", "delay", "pilot", "outlet", "contributions",
			"continuous", "db", "czech", "resulting", "cambridge", "initiative", "novel", "pan", "execution",
			"disability", "increases", "ultra", "winner", "idaho", "contractor", "ph", "episode", "examination",
			"potter", "dish", "plays", "bulletin", "ia", "pt", "indicates", "modify", "oxford", "adam", "truly",
			"epinions", "painting", "committed", "extensive", "affordable", "universe", "candidate", "databases",
			"patent", "slot", "psp", "outstanding", "ha", "eating", "perspective", "planned", "watching", "lodge",
			"messenger", "mirror", "tournament", "consideration", "ds", "discounts", "sterling", "sessions", "kernel",
			"boobs", "stocks", "buyers", "journals", "gray", "catalogue", "ea", "jennifer", "antonio", "charged",
			"broad", "taiwan", "und", "chosen", "demo", "greece", "lg", "swiss", "sarah", "clark", "labour", "hate",
			"terminal", "publishers", "nights", "behalf", "caribbean", "liquid", "rice", "nebraska", "loop", "salary",
			"reservation", "foods", "gourmet", "guard", "properly", "orleans", "saving", "nfl", "remaining", "empire",
			"resume", "twenty", "newly", "raise", "prepare", "avatar", "gary", "depending", "illegal", "expansion",
			"vary", "hundreds", "rome", "arab", "lincoln", "helped", "premier", "tomorrow", "purchased", "milk",
			"decide", "consent", "drama", "visiting", "performing", "downtown", "keyboard", "contest", "collected",
			"nw", "bands", "boot", "suitable", "ff", "absolutely", "millions", "lunch", "dildo", "audit", "push",
			"chamber", "guinea", "findings", "muscle", "featuring", "iso", "implement", "clicking", "scheduled",
			"polls", "typical", "tower", "yours", "sum", "misc", "calculator", "significantly", "chicken", "temporary",
			"attend", "shower", "alan", "sending", "jason", "tonight", "dear", "sufficient", "holdem", "shell",
			"province", "catholic", "oak", "vat", "awareness", "vancouver", "governor", "beer", "seemed",
			"contribution", "measurement", "swimming", "spyware", "formula", "constitution", "packaging", "solar",
			"jose", "catch", "jane", "pakistan", "ps", "reliable", "consultation", "northwest", "sir", "doubt", "earn",
			"finder", "unable", "periods", "classroom", "tasks", "democracy", "attacks", "kim", "wallpaper",
			"merchandise", "const", "resistance", "doors", "symptoms", "resorts", "biggest", "memorial", "visitor",
			"twin", "forth", "insert", "baltimore", "gateway", "ky", "dont", "alumni", "drawing", "candidates",
			"charlotte", "ordered", "biological", "fighting", "transition", "happens", "preferences", "spy", "romance",
			"instrument", "bruce", "split", "themes", "powers", "heaven", "br", "bits", "pregnant", "twice",
			"classification", "focused", "egypt", "physician", "hollywood", "bargain", "wikipedia", "cellular",
			"norway", "vermont", "asking", "blocks", "normally", "lo", "spiritual", "hunting", "diabetes", "suit", "ml",
			"shift", "chip", "res", "sit", "bodies", "photographs", "cutting", "wow", "simon", "writers", "marks",
			"flexible", "loved", "favourites", "mapping", "numerous", "relatively", "birds", "satisfaction",
			"represents", "char", "indexed", "pittsburgh", "superior", "preferred", "saved", "paying", "cartoon",
			"shots", "intellectual", "moore", "granted", "choices", "carbon", "spending", "comfortable", "magnetic",
			"interaction", "listening", "effectively", "registry", "crisis", "outlook", "massive", "denmark",
			"employed", "bright", "treat", "header", "cs", "poverty", "formed", "piano", "echo", "que", "grid",
			"sheets", "patrick", "experimental", "puerto", "revolution", "consolidation", "displays", "plasma",
			"allowing", "earnings", "voip", "mystery", "landscape", "dependent", "mechanical", "journey", "delaware",
			"bidding", "consultants", "risks", "banner", "applicant", "charter", "fig", "barbara", "cooperation",
			"counties", "acquisition", "ports", "implemented", "sf", "directories", "recognized", "dreams", "blogger",
			"notification", "kg", "licensing", "stands", "teach", "occurred", "textbooks", "rapid", "pull", "hairy",
			"diversity", "cleveland", "ut", "reverse", "deposit", "seminar", "investments", "latina", "nasa", "wheels",
			"sexcam", "specify", "accessibility", "dutch", "sensitive", "templates", "formats", "tab", "depends",
			"boots", "holds", "router", "concrete", "si", "editing", "poland", "folder", "womens", "css", "completion",
			"upload", "pulse", "universities", "technique", "contractors", "milfhunter", "voting", "courts", "notices",
			"subscriptions", "calculate", "mc", "detroit", "alexander", "broadcast", "converted", "metro", "toshiba",
			"anniversary", "improvements", "strip", "specification", "pearl", "accident", "nick", "accessible",
			"accessory", "resident", "plot", "qty", "possibly", "airline", "typically", "representation", "regard",
			"pump", "exists", "arrangements", "smooth", "conferences", "uniprotkb", "beastiality", "strike",
			"consumption", "birmingham", "flashing", "lp", "narrow", "afternoon", "threat", "surveys", "sitting",
			"putting", "consultant", "controller", "ownership", "committees", "penis", "legislative", "researchers",
			"vietnam", "trailer", "anne", "castle", "gardens", "missed", "malaysia", "unsubscribe", "antique", "labels",
			"willing", "bio", "molecular", "upskirt", "acting", "heads", "stored", "exam", "logos", "residence",
			"attorneys", "milfs", "antiques", "density", "hundred", "ryan", "operators", "strange", "sustainable",
			"philippines", "statistical", "beds", "breasts", "mention", "innovation", "pcs", "employers", "grey",
			"parallel", "honda", "amended", "operate", "bills", "bold", "bathroom", "stable", "opera", "definitions",
			"von", "doctors", "lesson", "cinema", "asset", "ag", "scan", "elections", "drinking", "blowjobs",
			"reaction", "blank", "enhanced", "entitled", "severe", "generate", "stainless", "newspapers", "hospitals",
			"vi", "deluxe", "humor", "aged", "monitors", "exception", "lived", "duration", "bulk", "successfully",
			"indonesia", "pursuant", "sci", "fabric", "edt", "visits", "primarily", "tight", "domains", "capabilities",
			"pmid", "contrast", "recommendation", "flying", "recruitment", "sin", "berlin", "cute", "organized", "ba",
			"para", "siemens", "adoption", "improving", "cr", "expensive", "meant", "capture", "pounds", "buffalo",
			"organisations", "plane", "pg", "explained", "seed", "programmes", "desire", "expertise", "mechanism",
			"camping", "ee", "jewellery", "meets", "welfare", "peer", "caught", "eventually", "marked", "driven",
			"measured", "medline", "bottle", "agreements", "considering", "innovative", "marshall", "massage", "rubber",
			"conclusion", "closing", "tampa", "thousand", "meat", "legend", "grace", "susan", "ing", "ks", "adams",
			"python", "monster", "alex", "bang", "villa", "bone", "columns", "disorders", "bugs", "collaboration",
			"hamilton", "detection", "ftp", "cookies", "inner", "formation", "tutorial", "med", "engineers", "entity",
			"cruises", "gate", "holder", "proposals", "moderator", "sw", "tutorials", "settlement", "portugal",
			"lawrence", "roman", "duties", "valuable", "erotic", "tone", "collectables", "ethics", "forever", "dragon",
			"busy", "captain", "fantastic", "imagine", "brings", "heating", "leg", "neck", "hd", "wing", "governments",
			"purchasing", "scripts", "abc", "stereo", "appointed", "taste", "dealing", "commit", "tiny", "operational",
			"rail", "airlines", "liberal", "livecam", "jay", "trips", "gap", "sides", "tube", "turns", "corresponding",
			"descriptions", "cache", "belt", "jacket", "determination", "animation", "oracle", "er", "matthew", "lease",
			"productions", "aviation", "hobbies", "proud", "excess", "disaster", "console", "commands", "jr",
			"telecommunications", "instructor", "giant", "achieved", "injuries", "shipped", "bestiality", "seats",
			"approaches", "biz", "alarm", "voltage", "anthony", "nintendo", "usual", "loading", "stamps", "appeared",
			"franklin", "angle", "rob", "vinyl", "highlights", "mining", "designers", "melbourne", "ongoing", "worst",
			"imaging", "betting", "scientists", "liberty", "wyoming", "blackjack", "argentina", "era", "convert",
			"possibility", "analyst", "commissioner", "dangerous", "garage", "exciting", "reliability", "thongs", "gcc",
			"unfortunately", "respectively", "volunteers", "attachment", "ringtone", "finland", "morgan", "derived",
			"pleasure", "honor", "asp", "oriented", "eagle", "desktops", "pants", "columbus", "nurse", "prayer",
			"appointment", "workshops", "hurricane", "quiet", "luck", "postage", "producer", "represented", "mortgages",
			"dial", "responsibilities", "cheese", "comic", "carefully", "jet", "productivity", "investors", "crown",
			"par", "underground", "diagnosis", "maker", "crack", "principle", "picks", "vacations", "gang", "semester",
			"calculated", "cumshot", "fetish", "applies", "casinos", "appearance", "smoke", "apache", "filters",
			"incorporated", "nv", "craft", "cake", "notebooks", "apart", "fellow", "blind", "lounge", "mad",
			"algorithm", "semi", "coins", "andy", "gross", "strongly", "cafe", "valentine", "hilton", "ken", "proteins",
			"horror", "su", "exp", "familiar", "capable", "douglas", "debian", "till", "involving", "pen", "investing",
			"christopher", "admission", "epson", "shoe", "elected", "carrying", "victory", "sand", "madison",
			"terrorism", "joy", "editions", "cpu", "mainly", "ethnic", "ran", "parliament", "actor", "finds", "seal",
			"situations", "fifth", "allocated", "citizen", "vertical", "corrections", "structural", "municipal",
			"describes", "prize", "sr", "occurs", "jon", "absolute", "disabilities", "consists", "anytime", "substance",
			"prohibited", "addressed", "lies", "pipe", "soldiers", "nr", "guardian", "lecture", "simulation", "layout",
			"initiatives", "ill", "concentration", "classics", "lbs", "lay", "interpretation", "horses", "lol", "dirty",
			"deck", "wayne", "donate", "taught", "bankruptcy", "mp", "worker", "optimization", "alive", "temple",
			"substances", "prove", "discovered", "wings", "breaks", "genetic", "restrictions", "participating",
			"waters", "promise", "thin", "exhibition", "prefer", "ridge", "cabinet", "modem", "harris", "mph",
			"bringing", "sick", "dose", "evaluate", "tiffany", "tropical", "collect", "bet", "composition", "toyota",
			"streets", "nationwide", "vector", "definitely", "shaved", "turning", "buffer", "purple", "existence",
			"commentary", "larry", "limousines", "developments", "def", "immigration", "destinations", "lets", "mutual",
			"pipeline", "necessarily", "syntax", "li", "attribute", "prison", "skill", "chairs", "nl", "everyday",
			"apparently", "surrounding", "mountains", "moves", "popularity", "inquiry", "ethernet", "checked",
			"exhibit", "throw", "trend", "sierra", "visible", "cats", "desert", "postposted", "ya", "oldest", "rhode",
			"nba", "busty", "coordinator", "obviously", "mercury", "steven", "handbook", "greg", "navigate", "worse",
			"summit", "victims", "epa", "spaces", "fundamental", "burning", "escape", "coupons", "somewhat", "receiver",
			"substantial", "tr", "progressive", "cialis", "bb", "boats", "glance", "scottish", "championship", "arcade",
			"richmond", "sacramento", "impossible", "ron", "russell", "tells", "obvious", "fiber", "depression",
			"graph", "covering", "platinum", "judgment", "bedrooms", "talks", "filing", "foster", "modeling", "passing",
			"awarded", "testimonials", "trials", "tissue", "nz", "memorabilia", "clinton", "masters", "bonds",
			"cartridge", "alberta", "explanation", "folk", "org", "commons", "cincinnati", "subsection", "fraud",
			"electricity", "permitted", "spectrum", "arrival", "okay", "pottery", "emphasis", "roger", "aspect",
			"workplace", "awesome", "mexican", "confirmed", "counts", "priced", "wallpapers", "hist", "crash", "lift",
			"desired", "inter", "closer", "assumes", "heights", "shadow", "riding", "infection", "firefox", "lisa",
			"expense", "grove", "eligibility", "venture", "clinic", "korean", "healing", "princess", "mall", "entering",
			"packet", "spray", "studios", "involvement", "dad", "buttons", "placement", "observations", "vbulletin",
			"funded", "thompson", "winners", "extend", "roads", "subsequent", "pat", "dublin", "rolling", "fell",
			"motorcycle", "yard", "disclosure", "establishment", "memories", "nelson", "te", "arrived", "creates",
			"faces", "tourist", "cocks", "av", "mayor", "murder", "sean", "adequate", "senator", "yield",
			"presentations", "grades", "cartoons", "pour", "digest", "reg", "lodging", "tion", "dust", "hence", "wiki",
			"entirely", "replaced", "radar", "rescue", "undergraduate", "losses", "combat", "reducing", "stopped",
			"occupation", "lakes", "butt", "donations", "associations", "citysearch", "closely", "radiation", "diary",
			"seriously", "kings", "shooting", "kent", "adds", "nsw", "ear", "flags", "pci", "baker", "launched",
			"elsewhere", "pollution", "conservative", "guestbook", "shock", "effectiveness", "walls", "abroad", "ebony",
			"tie", "ward", "drawn", "arthur", "ian", "visited", "roof", "walker", "demonstrate", "atmosphere",
			"suggests", "kiss", "beast", "ra", "operated", "experiment", "targets", "overseas", "purchases", "dodge",
			"counsel", "federation", "pizza", "invited", "yards", "assignment", "chemicals", "gordon", "mod", "farmers",
			"rc", "queries", "bmw", "rush", "ukraine", "absence", "nearest", "cluster", "vendors", "mpeg", "whereas",
			"yoga", "serves", "woods", "surprise", "lamp", "rico", "partial", "shoppers", "phil", "everybody",
			"couples", "nashville", "ranking", "jokes", "cst", "http", "ceo", "simpson", "twiki", "sublime",
			"counseling", "palace", "acceptable", "satisfied", "glad", "wins", "measurements", "verify", "globe",
			"trusted", "copper", "milwaukee", "rack", "medication", "warehouse", "shareware", "ec", "rep", "dicke",
			"kerry", "receipt", "supposed", "ordinary", "nobody", "ghost", "violation", "configure", "stability", "mit",
			"applying", "southwest", "boss", "pride", "institutional", "expectations", "independence", "knowing",
			"reporter", "metabolism", "keith", "champion", "cloudy", "linda", "ross", "personally", "chile", "anna",
			"plenty", "solo", "sentence", "throat", "ignore", "maria", "uniform", "excellence", "wealth", "tall", "rm",
			"somewhere", "vacuum", "dancing", "attributes", "recognize", "brass", "writes", "plaza", "pdas", "outcomes",
			"survival", "quest", "publish", "sri", "screening", "toe", "thumbnail", "trans", "jonathan", "whenever",
			"nova", "lifetime", "api", "pioneer", "booty", "forgotten", "acrobat", "plates", "acres", "venue",
			"athletic", "thermal", "essays", "behaviour", "vital", "telling", "fairly", "coastal", "config", "cf",
			"charity", "intelligent", "edinburgh", "vt", "excel", "modes", "obligation", "campbell", "wake", "stupid",
			"harbor", "hungary", "traveler", "urw", "segment", "realize", "regardless", "lan", "enemy", "puzzle",
			"rising", "aluminum", "wells", "wishlist", "opens", "insight", "sms", "shit", "restricted", "republican",
			"secrets", "lucky", "latter", "merchants", "thick", "trailers", "repeat", "syndrome", "philips",
			"attendance", "penalty", "drum", "glasses", "enables", "nec", "iraqi", "builder", "vista", "jessica",
			"chips", "terry", "flood", "foto", "ease", "arguments", "amsterdam", "orgy", "arena", "adventures",
			"pupils", "stewart", "announcement", "tabs", "outcome", "xx", "appreciate", "expanded", "casual", "grown",
			"polish", "lovely", "extras", "gm", "centres", "jerry", "clause", "smile", "lands", "ri", "troops",
			"indoor", "bulgaria", "armed", "broker", "charger", "regularly", "believed", "pine", "cooling", "tend",
			"gulf", "rt", "rick", "trucks", "cp", "mechanisms", "divorce", "laura", "shopper", "tokyo", "partly",
			"nikon", "customize", "tradition", "candy", "pills", "tiger", "donald", "folks", "sensor", "exposed",
			"telecom", "hunt", "angels", "deputy", "indicators", "sealed", "thai", "emissions", "physicians", "loaded",
			"fred", "complaint", "scenes", "experiments", "balls", "afghanistan", "dd", "boost", "spanking",
			"scholarship", "governance", "mill", "founded", "supplements", "chronic", "icons", "tranny", "moral", "den",
			"catering", "aud", "finger", "keeps", "pound", "locate", "camcorder", "pl", "trained", "burn",
			"implementing", "roses", "labs", "ourselves", "bread", "tobacco", "wooden", "motors", "tough", "roberts",
			"incident", "gonna", "dynamics", "lie", "crm", "rf", "conversation", "decrease", "cumshots", "chest",
			"pension", "billy", "revenues", "emerging", "worship", "bukkake", "capability", "ak", "fe", "craig",
			"herself", "producing", "churches", "precision", "damages", "reserves", "contributed", "solve", "shorts",
			"reproduction", "minority", "td", "diverse", "amp", "ingredients", "sb", "ah", "johnny", "sole",
			"franchise", "recorder", "complaints", "facing", "sm", "nancy", "promotions", "tones", "passion",
			"rehabilitation", "maintaining", "sight", "laid", "clay", "defence", "patches", "weak", "refund", "usc",
			"towns", "environments", "trembl", "divided", "blvd", "reception", "amd", "wise", "emails", "cyprus", "wv",
			"odds", "correctly", "insider", "seminars", "consequences", "makers", "hearts", "geography", "appearing",
			"integrity", "worry", "ns", "discrimination", "eve", "carter", "legacy", "marc", "pleased", "danger",
			"vitamin", "widely", "processed", "phrase", "genuine", "raising", "implications", "functionality",
			"paradise", "hybrid", "reads", "roles", "intermediate", "emotional", "sons", "leaf", "pad", "glory",
			"platforms", "ja", "bigger", "billing", "diesel", "versus", "combine", "overnight", "geographic", "exceed",
			"bs", "rod", "saudi", "fault", "cuba", "hrs", "preliminary", "districts", "introduce", "silk",
			"promotional", "kate", "chevrolet", "babies", "bi", "karen", "compiled", "romantic", "revealed",
			"specialists", "generator", "albert", "examine", "jimmy", "graham", "suspension", "bristol", "margaret",
			"compaq", "sad", "correction", "wolf", "slowly", "authentication", "communicate", "rugby", "supplement",
			"showtimes", "cal", "portions", "infant", "promoting", "sectors", "samuel", "fluid", "grounds", "fits",
			"kick", "regards", "meal", "ta", "hurt", "machinery", "bandwidth", "unlike", "equation", "baskets",
			"probability", "pot", "dimension", "wright", "img", "barry", "proven", "schedules", "admissions", "cached",
			"warren", "slip", "studied", "reviewer", "involves", "quarterly", "rpm", "profits", "devil", "grass",
			"comply", "marie", "florist", "illustrated", "cherry", "continental", "alternate", "deutsch", "achievement",
			"limitations", "kenya", "webcam", "cuts", "funeral", "nutten", "earrings", "enjoyed", "automated",
			"chapters", "pee", "charlie", "quebec", "nipples", "passenger", "convenient", "dennis", "mars", "francis",
			"tvs", "sized", "manga", "noticed", "socket", "silent", "literary", "egg", "mhz", "signals", "caps",
			"orientation", "pill", "theft", "childhood", "swing", "symbols", "lat", "meta", "humans", "analog",
			"facial", "choosing", "talent", "dated", "flexibility", "seeker", "wisdom", "shoot", "boundary", "mint",
			"packard", "offset", "payday", "philip", "elite", "gi", "spin", "holders", "believes", "swedish", "poems",
			"deadline", "jurisdiction", "robot", "displaying", "witness", "collins", "equipped", "stages", "encouraged",
			"sur", "winds", "powder", "broadway", "acquired", "assess", "wash", "cartridges", "stones", "entrance",
			"gnome", "roots", "declaration", "losing", "attempts", "gadgets", "noble", "glasgow", "automation",
			"impacts", "rev", "gospel", "advantages", "shore", "loves", "induced", "ll", "knight", "preparing", "loose",
			"aims", "recipient", "linking", "extensions", "appeals", "cl", "earned", "illness", "islamic", "athletics",
			"southeast", "ieee", "ho", "alternatives", "pending", "parker", "determining", "lebanon", "corp",
			"personalized", "kennedy", "gt", "sh", "conditioning", "teenage", "soap", "ae", "triple", "cooper", "nyc",
			"vincent", "jam", "secured", "unusual", "answered", "partnerships", "destruction", "slots", "increasingly",
			"migration", "disorder", "routine", "toolbar", "basically", "rocks", "conventional", "titans", "applicants",
			"wearing", "axis", "sought", "genes", "mounted", "habitat", "firewall", "median", "guns", "scanner",
			"herein", "occupational", "animated", "horny", "judicial", "rio", "hs", "adjustment", "hero", "integer",
			"treatments", "bachelor", "attitude", "camcorders", "engaged", "falling", "basics", "montreal", "carpet",
			"rv", "struct", "lenses", "binary", "genetics", "attended", "difficulty", "punk", "collective", "coalition",
			"pi", "dropped", "enrollment", "duke", "walter", "ai", "pace", "besides", "wage", "producers", "ot",
			"collector", "arc", "hosts", "interfaces", "advertisers", "moments", "atlas", "strings", "dawn",
			"representing", "observation", "feels", "torture", "carl", "deleted", "coat", "mitchell", "mrs", "rica",
			"restoration", "convenience", "returning", "ralph", "opposition", "container", "yr", "defendant", "warner",
			"confirmation", "app", "embedded", "inkjet", "supervisor", "wizard", "corps", "actors", "liver",
			"peripherals", "liable", "brochure", "morris", "bestsellers", "petition", "eminem", "recall", "antenna",
			"picked", "assumed", "departure", "minneapolis", "belief", "killing", "bikini", "memphis", "shoulder",
			"decor", "lookup", "texts", "harvard", "brokers", "roy", "ion", "diameter", "ottawa", "doll", "ic",
			"podcast", "tit", "seasons", "peru", "interactions", "refine", "bidder", "singer", "evans", "herald",
			"literacy", "fails", "aging", "nike", "intervention", "pissing", "fed", "plugin", "attraction", "diving",
			"invite", "modification", "alice", "latinas", "suppose", "customized", "reed", "involve", "moderate",
			"terror", "younger", "thirty", "mice", "opposite", "understood", "rapidly", "dealtime", "ban", "temp",
			"intro", "mercedes", "zus", "assurance", "fisting", "clerk", "happening", "vast", "mills", "outline",
			"amendments", "tramadol", "holland", "receives", "jeans", "metropolitan", "compilation", "verification",
			"fonts", "ent", "odd", "wrap", "refers", "mood", "favor", "veterans", "quiz", "mx", "sigma", "gr",
			"attractive", "xhtml", "occasion", "recordings", "jefferson", "victim", "demands", "sleeping", "careful",
			"ext", "beam", "gardening", "obligations", "arrive", "orchestra", "sunset", "tracked", "moreover",
			"minimal", "polyphonic", "lottery", "tops", "framed", "aside", "outsourcing", "licence", "adjustable",
			"allocation", "michelle", "essay", "discipline", "amy", "ts", "demonstrated", "dialogue", "identifying",
			"alphabetical", "camps", "declared", "dispatched", "aaron", "handheld", "trace", "disposal", "shut",
			"florists", "packs", "ge", "installing", "switches", "romania", "voluntary", "ncaa", "thou", "consult",
			"phd", "greatly", "blogging", "mask", "cycling", "midnight", "ng", "commonly", "pe", "photographer",
			"inform", "turkish", "coal", "cry", "messaging", "pentium", "quantum", "murray", "intent", "tt", "zoo",
			"largely", "pleasant", "announce", "constructed", "additions", "requiring", "spoke", "aka", "arrow",
			"engagement", "sampling", "rough", "weird", "tee", "refinance", "lion", "inspired", "holes", "weddings",
			"blade", "suddenly", "oxygen", "cookie", "meals", "canyon", "goto", "meters", "merely", "calendars",
			"arrangement", "conclusions", "passes", "bibliography", "pointer", "compatibility", "stretch", "durham",
			"furthermore", "permits", "cooperative", "muslim", "xl", "neil", "sleeve", "netscape", "cleaner", "cricket",
			"beef", "feeding", "stroke", "township", "rankings", "measuring", "cad", "hats", "robin", "robinson",
			"jacksonville", "strap", "headquarters", "sharon", "crowd", "tcp", "transfers", "surf", "olympic",
			"transformation", "remained", "attachments", "dv", "dir", "entities", "customs", "administrators",
			"personality", "rainbow", "hook", "roulette", "decline", "gloves", "israeli", "medicare", "cord", "skiing",
			"cloud", "facilitate", "subscriber", "valve", "val", "hewlett", "explains", "proceed", "flickr", "feelings",
			"knife", "jamaica", "priorities", "shelf", "bookstore", "timing", "liked", "parenting", "adopt", "denied",
			"fotos", "incredible", "britney", "freeware", "fucked", "donation", "outer", "crop", "deaths", "rivers",
			"commonwealth", "pharmaceutical", "manhattan", "tales", "katrina", "workforce", "islam", "nodes", "tu",
			"fy", "thumbs", "seeds", "cited", "lite", "ghz", "hub", "targeted", "organizational", "skype", "realized",
			"twelve", "founder", "decade", "gamecube", "rr", "dispute", "portuguese", "tired", "titten", "adverse",
			"everywhere", "excerpt", "eng", "steam", "discharge", "ef", "drinks", "ace", "voices", "acute", "halloween",
			"climbing", "stood", "sing", "tons", "perfume", "carol", "honest", "albany", "hazardous", "restore",
			"stack", "methodology", "somebody", "sue", "ep", "housewares", "reputation", "resistant", "democrats",
			"recycling", "hang", "gbp", "curve", "creator", "amber", "qualifications", "museums", "coding", "slideshow",
			"tracker", "variation", "passage", "transferred", "trunk", "hiking", "lb", "damn", "pierre", "jelsoft",
			"headset", "photograph", "oakland", "colombia", "waves", "camel", "distributor", "lamps", "underlying",
			"hood", "wrestling", "suicide", "archived", "photoshop", "jp", "chi", "bt", "arabia", "gathering",
			"projection", "juice", "chase", "mathematical", "logical", "sauce", "fame", "extract", "specialized",
			"diagnostic", "panama", "indianapolis", "af", "payable", "corporations", "courtesy", "criticism",
			"automobile", "confidential", "rfc", "statutory", "accommodations", "athens", "northeast", "downloaded",
			"judges", "sl", "seo", "retired", "isp", "remarks", "detected", "decades", "paintings", "walked", "arising",
			"nissan", "bracelet", "ins", "eggs", "juvenile", "injection", "yorkshire", "populations", "protective",
			"afraid", "acoustic", "railway", "cassette", "initially", "indicator", "pointed", "hb", "jpg", "causing",
			"mistake", "norton", "locked", "eliminate", "tc", "fusion", "mineral", "sunglasses", "ruby", "steering",
			"beads", "fortune", "preference", "canvas", "threshold", "parish", "claimed", "screens", "cemetery",
			"planner", "croatia", "flows", "stadium", "venezuela", "exploration", "mins", "fewer", "sequences",
			"coupon", "nurses", "ssl", "stem", "proxy", "gangbang", "astronomy", "lanka", "opt", "edwards", "drew",
			"contests", "flu", "translate", "announces", "mlb", "costume", "tagged", "berkeley", "voted", "killer",
			"bikes", "gates", "adjusted", "rap", "tune", "bishop", "pulled", "corn", "gp", "shaped", "compression",
			"seasonal", "establishing", "farmer", "counters", "puts", "constitutional", "grew", "perfectly", "tin",
			"slave", "instantly", "cultures", "norfolk", "coaching", "examined", "trek", "encoding", "litigation",
			"submissions", "oem", "heroes", "painted", "lycos", "ir", "zdnet", "broadcasting", "horizontal", "artwork",
			"cosmetic", "resulted", "portrait", "terrorist", "informational", "ethical", "carriers", "ecommerce",
			"mobility", "floral", "builders", "ties", "struggle", "schemes", "suffering", "neutral", "fisher", "rat",
			"spears", "prospective", "dildos", "bedding", "ultimately", "joining", "heading", "equally", "artificial",
			"bearing", "spectacular", "coordination", "connector", "brad", "combo", "seniors", "worlds", "guilty",
			"affiliated", "activation", "naturally", "haven", "tablet", "jury", "dos", "tail", "subscribers", "charm",
			"lawn", "violent", "mitsubishi", "underwear", "basin", "soup", "potentially", "ranch", "constraints",
			"crossing", "inclusive", "dimensional", "cottage", "drunk", "considerable", "crimes", "resolved", "mozilla",
			"byte", "toner", "nose", "latex", "branches", "anymore", "oclc", "delhi", "holdings", "alien", "locator",
			"selecting", "processors", "pantyhose", "plc", "broke", "nepal", "zimbabwe", "difficulties", "juan",
			"complexity", "msg", "constantly", "browsing", "resolve", "barcelona", "presidential", "documentary", "cod",
			"territories", "melissa", "moscow", "thesis", "thru", "jews", "nylon", "palestinian", "discs", "rocky",
			"bargains", "frequent", "trim", "nigeria", "ceiling", "pixels", "ensuring", "hispanic", "cv", "cb",
			"legislature", "hospitality", "gen", "anybody", "procurement", "diamonds", "espn", "fleet", "untitled",
			"bunch", "totals", "marriott", "singing", "theoretical", "afford", "exercises", "starring", "referral",
			"nhl", "surveillance", "optimal", "quit", "distinct", "protocols", "lung", "highlight", "substitute",
			"inclusion", "hopefully", "brilliant", "turner", "sucking", "cents", "reuters", "ti", "fc", "gel", "todd",
			"spoken", "omega", "evaluated", "stayed", "civic", "assignments", "fw", "manuals", "doug", "sees",
			"termination", "watched", "saver", "thereof", "grill", "households", "gs", "redeem", "rogers", "grain",
			"aaa", "authentic", "regime", "wanna", "wishes", "bull", "montgomery", "architectural", "louisville",
			"depend", "differ", "macintosh", "movements", "ranging", "monica", "repairs", "breath", "amenities",
			"virtually", "cole", "mart", "candle", "hanging", "colored", "authorization", "tale", "verified", "lynn",
			"formerly", "projector", "bp", "situated", "comparative", "std", "seeks", "herbal", "loving", "strictly",
			"routing", "docs", "stanley", "psychological", "surprised", "retailer", "vitamins", "elegant", "gains",
			"renewal", "vid", "genealogy", "opposed", "deemed", "scoring", "expenditure", "panties", "brooklyn",
			"liverpool", "sisters", "critics", "connectivity", "spots", "oo", "algorithms", "hacker", "madrid",
			"similarly", "margin", "coin", "bbw", "solely", "fake", "salon", "collaborative", "norman", "fda",
			"excluding", "turbo", "headed", "voters", "cure", "madonna", "commander", "arch", "ni", "murphy", "thinks",
			"thats", "suggestion", "hdtv", "soldier", "phillips", "asin", "aimed", "justin", "bomb", "harm", "interval",
			"mirrors", "spotlight", "tricks", "reset", "brush", "investigate", "thy", "expansys", "panels", "repeated",
			"assault", "connecting", "spare", "logistics", "deer", "kodak", "tongue", "bowling", "tri", "danish", "pal",
			"monkey", "proportion", "filename", "skirt", "florence", "invest", "honey", "um", "analyses", "drawings",
			"significance", "scenario", "ye", "fs", "lovers", "atomic", "approx", "symposium", "arabic", "gauge",
			"essentials", "junction", "protecting", "nn", "faced", "mat", "rachel", "solving", "transmitted",
			"weekends", "screenshots", "produces", "oven", "ted", "intensive", "chains", "kingston", "sixth", "engage",
			"deviant", "noon", "switching", "quoted", "adapters", "correspondence", "farms", "imports", "supervision",
			"cheat", "bronze", "expenditures", "sandy", "separation", "testimony", "suspect", "celebrities", "macro",
			"sender", "mandatory", "boundaries", "crucial", "syndication", "gym", "celebration", "kde", "adjacent",
			"filtering", "tuition", "spouse", "exotic", "viewer", "signup", "threats", "luxembourg", "puzzles",
			"reaching", "vb", "damaged", "cams", "receptor", "piss", "laugh", "joel", "surgical", "destroy", "citation",
			"pitch", "autos", "yo", "premises", "perry", "proved", "offensive", "imperial", "dozen", "benjamin",
			"deployment", "teeth", "cloth", "studying", "colleagues", "stamp", "lotus", "salmon", "olympus",
			"separated", "proc", "cargo", "tan", "directive", "fx", "salem", "mate", "dl", "starter", "upgrades",
			"likes", "butter", "pepper", "weapon", "luggage", "burden", "chef", "tapes", "zones", "races", "isle",
			"stylish", "slim", "maple", "luke", "grocery", "offshore", "governing", "retailers", "depot", "kenneth",
			"comp", "alt", "pie", "blend", "harrison", "ls", "julie", "occasionally", "cbs", "attending", "emission",
			"pete", "spec", "finest", "realty", "janet", "bow", "penn", "recruiting", "apparent", "instructional",
			"phpbb", "autumn", "traveling", "probe", "midi", "permissions", "biotechnology", "toilet", "ranked",
			"jackets", "routes", "packed", "excited", "outreach", "helen", "mounting", "recover", "tied", "lopez",
			"balanced", "prescribed", "catherine", "timely", "talked", "upskirts", "debug", "delayed", "chuck",
			"reproduced", "hon", "dale", "explicit", "calculation", "villas", "ebook", "consolidated", "boob",
			"exclude", "peeing", "occasions", "brooks", "equations", "newton", "oils", "sept", "exceptional", "anxiety",
			"bingo", "whilst", "spatial", "respondents", "unto", "lt", "ceramic", "prompt", "precious", "minds",
			"annually", "considerations", "scanners", "atm", "xanax", "eq", "pays", "cox", "fingers", "sunny", "ebooks",
			"delivers", "je", "queensland", "necklace", "musicians", "leeds", "composite", "unavailable", "cedar",
			"arranged", "lang", "theaters", "advocacy", "raleigh", "stud", "fold", "essentially", "designing",
			"threaded", "uv", "qualify", "fingering", "blair", "hopes", "assessments", "cms", "mason", "diagram",
			"burns", "pumps", "slut", "ejaculation", "footwear", "sg", "vic", "beijing", "peoples", "victor", "mario",
			"pos", "attach", "licenses", "utils", "removing", "advised", "brunswick", "spider", "phys", "ranges",
			"pairs", "sensitivity", "trails", "preservation", "hudson", "isolated", "calgary", "interim", "assisted",
			"divine", "streaming", "approve", "chose", "compound", "intensity", "technological", "syndicate",
			"abortion", "dialog", "venues", "blast", "wellness", "calcium", "newport", "antivirus", "addressing",
			"pole", "discounted", "indians", "shield", "harvest", "membrane", "prague", "previews", "bangladesh",
			"constitute", "locally", "concluded", "pickup", "desperate", "mothers", "nascar", "iceland",
			"demonstration", "governmental", "manufactured", "candles", "graduation", "mega", "bend", "sailing",
			"variations", "moms", "sacred", "addiction", "morocco", "chrome", "tommy", "springfield", "refused",
			"brake", "exterior", "greeting", "ecology", "oliver", "congo", "glen", "botswana", "nav", "delays",
			"synthesis", "olive", "undefined", "unemployment", "cyber", "verizon", "scored", "enhancement", "newcastle",
			"clone", "dicks", "velocity", "lambda", "relay", "composed", "tears", "performances", "oasis", "baseline",
			"cab", "angry", "fa", "societies", "silicon", "brazilian", "identical", "petroleum", "compete", "ist",
			"norwegian", "lover", "belong", "honolulu", "beatles", "lips", "escort", "retention", "exchanges", "pond",
			"rolls", "thomson", "barnes", "soundtrack", "wondering", "malta", "daddy", "lc", "ferry", "rabbit",
			"profession", "seating", "dam", "cnn", "separately", "physiology", "lil", "collecting", "das", "exports",
			"omaha", "tire", "participant", "scholarships", "recreational", "dominican", "chad", "electron", "loads",
			"friendship", "heather", "passport", "motel", "unions", "treasury", "warrant", "sys", "solaris", "frozen",
			"occupied", "josh", "royalty", "scales", "rally", "observer", "sunshine", "strain", "drag", "ceremony",
			"somehow", "arrested", "expanding", "provincial", "investigations", "icq", "ripe", "yamaha", "rely",
			"medications", "hebrew", "gained", "rochester", "dying", "laundry", "stuck", "solomon", "placing", "stops",
			"homework", "adjust", "assessed", "advertiser", "enabling", "encryption", "filling", "downloadable",
			"sophisticated", "imposed", "silence", "scsi", "focuses", "soviet", "possession", "cu", "laboratories",
			"treaty", "vocal", "trainer", "organ", "stronger", "volumes", "advances", "vegetables", "lemon", "toxic",
			"dns", "thumbnails", "darkness", "pty", "ws", "nuts", "nail", "bizrate", "vienna", "implied", "span",
			"stanford", "sox", "stockings", "joke", "respondent", "packing", "statute", "rejected", "satisfy",
			"destroyed", "shelter", "chapel", "gamespot", "manufacture", "layers", "wordpress", "guided",
			"vulnerability", "accountability", "celebrate", "accredited", "appliance", "compressed", "bahamas",
			"powell", "mixture", "zoophilia", "bench", "univ", "tub", "rider", "scheduling", "radius", "perspectives",
			"mortality", "logging", "hampton", "christians", "borders", "therapeutic", "pads", "butts", "inns", "bobby",
			"impressive", "sheep", "accordingly", "architect", "railroad", "lectures", "challenging", "wines",
			"nursery", "harder", "cups", "ash", "microwave", "cheapest", "accidents", "travesti", "relocation",
			"stuart", "contributors", "salvador", "ali", "salad", "np", "monroe", "tender", "violations", "foam",
			"temperatures", "paste", "clouds", "competitions", "discretion", "tft", "tanzania", "preserve", "jvc",
			"poem", "vibrator", "unsigned", "staying", "cosmetics", "easter", "theories", "repository", "praise",
			"jeremy", "venice", "jo", "concentrations", "vibrators", "estonia", "christianity", "veteran", "streams",
			"landing", "signing", "executed", "katie", "negotiations", "realistic", "dt", "cgi", "showcase", "integral",
			"asks", "relax", "namibia", "generating", "christina", "congressional", "synopsis", "hardly", "prairie",
			"reunion", "composer", "bean", "sword", "absent", "photographic", "sells", "ecuador", "hoping", "accessed",
			"spirits", "modifications", "coral", "pixel", "float", "colin", "bias", "imported", "paths", "bubble",
			"por", "acquire", "contrary", "millennium", "tribune", "vessel", "acids", "focusing", "viruses", "cheaper",
			"admitted", "dairy", "admit", "mem", "fancy", "equality", "samoa", "gc", "achieving", "tap", "stickers",
			"fisheries", "exceptions", "reactions", "leasing", "lauren", "beliefs", "ci", "macromedia", "companion",
			"squad", "analyze", "ashley", "scroll", "relate", "divisions", "swim", "wages", "additionally", "suffer",
			"forests", "fellowship", "nano", "invalid", "concerts", "martial", "males", "victorian", "retain",
			"colours", "execute", "tunnel", "genres", "cambodia", "patents", "copyrights", "yn", "chaos", "lithuania",
			"mastercard", "wheat", "chronicles", "obtaining", "beaver", "updating", "distribute", "readings",
			"decorative", "kijiji", "confused", "compiler", "enlargement", "eagles", "bases", "vii", "accused", "bee",
			"campaigns", "unity", "loud", "conjunction", "bride", "rats", "defines", "airports", "instances",
			"indigenous", "begun", "cfr", "brunette", "packets", "anchor", "socks", "validation", "parade",
			"corruption", "stat", "trigger", "incentives", "cholesterol", "gathered", "essex", "slovenia", "notified",
			"differential", "beaches", "folders", "dramatic", "surfaces", "terrible", "routers", "cruz", "pendant",
			"dresses", "baptist", "scientist", "starsmerchant", "hiring", "clocks", "arthritis", "bios", "females",
			"wallace", "nevertheless", "reflects", "taxation", "fever", "pmc", "cuisine", "surely", "practitioners",
			"transcript", "myspace", "theorem", "inflation", "thee", "nb", "ruth", "pray", "stylus", "compounds",
			"pope", "drums", "contracting", "topless", "arnold", "structured", "reasonably", "jeep", "chicks", "bare",
			"hung", "cattle", "mba", "radical", "graduates", "rover", "recommends", "controlling", "treasure", "reload",
			"distributors", "flame", "levitra", "tanks", "assuming", "monetary", "elderly", "pit", "arlington", "mono",
			"particles", "floating", "extraordinary", "tile", "indicating", "bolivia", "spell", "hottest", "stevens",
			"coordinate", "kuwait", "exclusively", "emily", "alleged", "limitation", "widescreen", "compile",
			"squirting", "webster", "struck", "rx", "illustration", "plymouth", "warnings", "construct", "apps",
			"inquiries", "bridal", "annex", "mag", "gsm", "inspiration", "tribal", "curious", "affecting", "freight",
			"rebate", "meetup", "eclipse", "sudan", "ddr", "downloading", "rec", "shuttle", "aggregate", "stunning",
			"cycles", "affects", "forecasts", "detect", "sluts", "actively", "ciao", "ampland", "knee", "prep", "pb",
			"complicated", "chem", "fastest", "butler", "shopzilla", "injured", "decorating", "payroll", "cookbook",
			"expressions", "ton", "courier", "uploaded", "shakespeare", "hints", "collapse", "americas", "connectors",
			"twinks", "unlikely", "oe", "gif", "pros", "conflicts", "techno", "beverage", "tribute", "wired", "elvis",
			"immune", "latvia", "travelers", "forestry", "barriers", "cant", "jd", "rarely", "gpl", "infected",
			"offerings", "martha", "genesis", "barrier", "argue", "incorrect", "trains", "metals", "bicycle",
			"furnishings", "letting", "arise", "guatemala", "celtic", "thereby", "irc", "jamie", "particle",
			"perception", "minerals", "advise", "humidity", "bottles", "boxing", "wy", "dm", "bangkok", "renaissance",
			"pathology", "sara", "bra", "ordinance", "hughes", "photographers", "bitch", "infections", "jeffrey",
			"chess", "operates", "brisbane", "configured", "survive", "oscar", "festivals", "menus", "joan",
			"possibilities", "duck", "reveal", "canal", "amino", "phi", "contributing", "herbs", "clinics", "mls",
			"cow", "manitoba", "analytical", "missions", "watson", "lying", "costumes", "strict", "dive", "saddam",
			"circulation", "drill", "offense", "threesome", "bryan", "cet", "protest", "handjob", "assumption",
			"jerusalem", "hobby", "tries", "transexuales", "invention", "nickname", "fiji", "technician", "inline",
			"executives", "enquiries", "washing", "audi", "staffing", "cognitive", "exploring", "trick", "enquiry",
			"closure", "raid", "ppc", "timber", "volt", "intense", "div", "playlist", "registrar", "showers",
			"supporters", "ruling", "steady", "dirt", "statutes", "withdrawal", "myers", "drops", "predicted", "wider",
			"saskatchewan", "jc", "cancellation", "plugins", "enrolled", "sensors", "screw", "ministers", "publicly",
			"hourly", "blame", "geneva", "freebsd", "veterinary", "acer", "prostores", "reseller", "dist", "handed",
			"suffered", "intake", "informal", "relevance", "incentive", "butterfly", "tucson", "mechanics", "heavily",
			"swingers", "fifty", "headers", "mistakes", "numerical", "ons", "geek", "uncle", "defining", "xnxx",
			"counting", "reflection", "sink", "accompanied", "assure", "invitation", "devoted", "princeton", "jacob",
			"sodium", "randy", "spirituality", "hormone", "meanwhile", "proprietary", "timothy", "childrens", "brick",
			"grip", "naval", "thumbzilla", "medieval", "porcelain", "avi", "bridges", "pichunter", "captured", "watt",
			"thehun", "decent", "casting", "dayton", "translated", "shortly", "cameron", "columnists", "pins", "carlos",
			"reno", "donna", "andreas", "warrior", "diploma", "cabin", "innocent", "bdsm", "scanning", "ide",
			"consensus", "polo", "valium", "copying", "rpg", "delivering", "cordless", "patricia", "horn", "eddie",
			"uganda", "fired", "journalism", "pd", "prot", "trivia", "adidas", "perth", "frog", "grammar", "intention",
			"syria", "disagree", "klein", "harvey", "tires", "logs", "undertaken", "tgp", "hazard", "retro", "leo",
			"livesex", "statewide", "semiconductor", "gregory", "episodes", "boolean", "circular", "anger", "diy",
			"mainland", "illustrations", "suits", "chances", "interact", "snap", "happiness", "arg", "substantially",
			"bizarre", "glenn", "ur", "auckland", "olympics", "fruits", "identifier", "geo", "worldsex", "ribbon",
			"calculations", "doe", "jpeg", "conducting", "startup", "suzuki", "trinidad", "ati", "kissing", "wal",
			"handy", "swap", "exempt", "crops", "reduces", "accomplished", "calculators", "geometry", "impression",
			"abs", "slovakia", "flip", "guild", "correlation", "gorgeous", "capitol", "sim", "dishes", "rna",
			"barbados", "chrysler", "nervous", "refuse", "extends", "fragrance", "mcdonald", "replica", "plumbing",
			"brussels", "tribe", "neighbors", "trades", "superb", "buzz", "transparent", "nuke", "rid", "trinity",
			"charleston", "handled", "legends", "boom", "calm", "champions", "floors", "selections", "projectors",
			"inappropriate", "exhaust", "comparing", "shanghai", "speaks", "burton", "vocational", "davidson", "copied",
			"scotia", "farming", "gibson", "pharmacies", "fork", "troy", "ln", "roller", "introducing", "batch",
			"organize", "appreciated", "alter", "nicole", "latino", "ghana", "edges", "uc", "mixing", "handles",
			"skilled", "fitted", "albuquerque", "harmony", "distinguished", "asthma", "projected", "assumptions",
			"shareholders", "twins", "developmental", "rip", "zope", "regulated", "triangle", "amend", "anticipated",
			"oriental", "reward", "windsor", "zambia", "completing", "gmbh", "buf", "ld", "hydrogen", "webshots",
			"sprint", "comparable", "chick", "advocate", "sims", "confusion", "copyrighted", "tray", "inputs",
			"warranties", "genome", "escorts", "documented", "thong", "medal", "paperbacks", "coaches", "vessels",
			"harbour", "walks", "sucks", "sol", "keyboards", "sage", "knives", "eco", "vulnerable", "arrange",
			"artistic", "bat", "honors", "booth", "indie", "reflected", "unified", "bones", "breed", "detector",
			"ignored", "polar", "fallen", "precise", "sussex", "respiratory", "notifications", "msgid", "transexual",
			"mainstream", "invoice", "evaluating", "lip", "subcommittee", "sap", "gather", "suse", "maternity",
			"backed", "alfred", "colonial", "mf", "carey", "motels", "forming", "embassy", "cave", "journalists",
			"danny", "rebecca", "slight", "proceeds", "indirect", "amongst", "wool", "foundations", "msgstr", "arrest",
			"volleyball", "mw", "adipex", "horizon", "nu", "deeply", "toolbox", "ict", "marina", "liabilities",
			"prizes", "bosnia", "browsers", "decreased", "patio", "dp", "tolerance", "surfing", "creativity", "lloyd",
			"describing", "optics", "pursue", "lightning", "overcome", "eyed", "ou", "quotations", "grab", "inspector",
			"attract", "brighton", "beans", "bookmarks", "ellis", "disable", "snake", "succeed", "leonard", "lending",
			"oops", "reminder", "nipple", "xi", "searched", "behavioral", "riverside", "bathrooms", "plains", "sku",
			"ht", "raymond", "insights", "abilities", "initiated", "sullivan", "za", "midwest", "karaoke", "trap",
			"lonely", "fool", "ve", "nonprofit", "lancaster", "suspended", "hereby", "observe", "julia", "containers",
			"attitudes", "karl", "berry", "collar", "simultaneously", "racial", "integrate", "bermuda", "amanda",
			"sociology", "mobiles", "screenshot", "exhibitions", "kelkoo", "confident", "retrieved", "exhibits",
			"officially", "consortium", "dies", "terrace", "bacteria", "pts", "replied", "seafood", "novels", "rh",
			"rrp", "recipients", "playboy", "ought", "delicious", "traditions", "fg", "jail", "safely", "finite",
			"kidney", "periodically", "fixes", "sends", "durable", "mazda", "allied", "throws", "moisture", "hungarian",
			"roster", "referring", "symantec", "spencer", "wichita", "nasdaq", "uruguay", "ooo", "hz", "transform",
			"timer", "tablets", "tuning", "gotten", "educators", "tyler", "futures", "vegetable", "verse", "highs",
			"humanities", "independently", "wanting", "custody", "scratch", "launches", "ipaq", "alignment",
			"masturbating", "henderson", "bk", "britannica", "comm", "ellen", "competitors", "nhs", "rocket", "aye",
			"bullet", "towers", "racks", "lace", "nasty", "visibility", "latitude", "consciousness", "ste", "tumor",
			"ugly", "deposits", "beverly", "mistress", "encounter", "trustees", "watts", "duncan", "reprints", "hart",
			"bernard", "resolutions", "ment", "accessing", "forty", "tubes", "attempted", "col", "midlands", "priest",
			"floyd", "ronald", "analysts", "queue", "dx", "sk", "trance", "locale", "nicholas", "biol", "yu", "bundle",
			"hammer", "invasion", "witnesses", "runner", "rows", "administered", "notion", "sq", "skins", "mailed",
			"oc", "fujitsu", "spelling", "arctic", "exams", "rewards", "beneath", "strengthen", "defend", "aj",
			"frederick", "medicaid", "treo", "infrared", "seventh", "gods", "une", "welsh", "belly", "aggressive",
			"tex", "advertisements", "quarters", "stolen", "cia", "sublimedirectory", "soonest", "haiti", "disturbed",
			"determines", "sculpture", "poly", "ears", "dod", "wp", "fist", "naturals", "neo", "motivation", "lenders",
			"pharmacology", "fitting", "fixtures", "bloggers", "mere", "agrees", "passengers", "quantities",
			"petersburg", "consistently", "powerpoint", "cons", "surplus", "elder", "sonic", "obituaries", "cheers",
			"dig", "taxi", "punishment", "appreciation", "subsequently", "om", "belarus", "nat", "zoning", "gravity",
			"providence", "thumb", "restriction", "incorporate", "backgrounds", "treasurer", "guitars", "essence",
			"flooring", "lightweight", "ethiopia", "tp", "mighty", "athletes", "humanity", "transcription", "jm",
			"holmes", "complications", "scholars", "dpi", "scripting", "gis", "remembered", "galaxy", "chester",
			"snapshot", "caring", "loc", "worn", "synthetic", "shaw", "vp", "segments", "testament", "expo", "dominant",
			"twist", "specifics", "itunes", "stomach", "partially", "buried", "cn", "newbie", "minimize", "darwin",
			"ranks", "wilderness", "debut", "generations", "tournaments", "bradley", "deny", "anatomy", "bali", "judy",
			"sponsorship", "headphones", "fraction", "trio", "proceeding", "cube", "defects", "volkswagen",
			"uncertainty", "breakdown", "milton", "marker", "reconstruction", "subsidiary", "strengths", "clarity",
			"rugs", "sandra", "adelaide", "encouraging", "furnished", "monaco", "settled", "folding", "emirates",
			"terrorists", "airfare", "comparisons", "beneficial", "distributions", "vaccine", "belize", "crap", "fate",
			"viewpicture", "promised", "volvo", "penny", "robust", "bookings", "threatened", "minolta", "republicans",
			"discusses", "gui", "porter", "gras", "jungle", "ver", "rn", "responded", "rim", "abstracts", "zen",
			"ivory", "alpine", "dis", "prediction", "pharmaceuticals", "andale", "fabulous", "remix", "alias",
			"thesaurus", "individually", "battlefield", "literally", "newer", "kay", "ecological", "spice", "oval",
			"implies", "cg", "soma", "ser", "cooler", "appraisal", "consisting", "maritime", "periodic", "submitting",
			"overhead", "ascii", "prospect", "shipment", "breeding", "citations", "geographical", "donor", "mozambique",
			"tension", "href", "benz", "trash", "shapes", "wifi", "tier", "fwd", "earl", "manor", "envelope", "diane",
			"homeland", "disclaimers", "championships", "excluded", "andrea", "breeds", "rapids", "disco", "sheffield",
			"bailey", "aus", "endif", "finishing", "emotions", "wellington", "incoming", "prospects", "lexmark",
			"cleaners", "bulgarian", "hwy", "eternal", "cashiers", "guam", "cite", "aboriginal", "remarkable",
			"rotation", "nam", "preventing", "productive", "boulevard", "eugene", "ix", "gdp", "pig", "metric",
			"compliant", "minus", "penalties", "bennett", "imagination", "hotmail" };
	/*
	 * quick sort: private void sortArray(char[] text, int l, int r) { if (l < r) {
	 * int s = partition(text, l, r); sortArray(text, l, s - 1); sortArray(text, s +
	 * 1, r); } }
	 * 
	 * private static int partition(char[] A, int l, int r) { int p = A[l], i = l +
	 * 1, j = r; while (i < j) { while (A[i] <= p && i < j) i++; while (A[j] > p &&
	 * i < j) j--; int tmp = A[i]; A[i] = A[j]; A[j] = (char) tmp; } int s; if (A[i]
	 * <= p) s = i; else s = i - 1; int tmp = A[l]; A[l] = A[s]; A[s] = (char) tmp;
	 * return s; }
	 */
}

# NaturalLanguageProcessing
Implemented different NLP algorithms like Viterbi,N-grams,BrillsTagger.
## Task 1:
I choosed the brown text corpus which contains more than 
o 1,000 articles
o 100,000 words

## Task 2: 
<ol>
  <li>Coffee</li>
  <li>Tea</li>
  <li>Milk</li>
</ol>
Implemented a shallow NLP pipeline to perform the following:
Keyword search index creation
? Segment the News articles into sentences
? Tokenize the sentences into words
? Index the keywords into search index such as Lucene or SOLR
o Natural language query parsing and search
? Segment an user?s input natural language query into sentences
? Tokenize the sentences into words
? Run a search/match against the search index created from the corpus
o Evaluate the results of at least 10 search queries for the top-10 returned articles


3. Task 3: Implement a deeper NLP pipeline to perform the following:
o Semantic search index creation
? Segment the News articles into sentences
? Tokenize the sentences into words
? Lemmatize the words to extract lemmas as features
? Stem the words to extract stemmed words as features
? Part-of-speech (POS) tag the words to extract POS tag features
? Syntactically parse the sentence and extract phrases, head words, and dependency parse relations as features
? Using WordNet, extract hypernymns, hyponyms, meronyms, and holonyms as features
? Index the various NLP features as separate search fields in a search index such as Lucene or SOLR
o Natural language query parsing and search
? Run the above described deeper NLP on an user?s input natural language and extract search query features
? Run a search/match against the separate or combination of search index fields created from the corpus
o Evaluate the results of at least 10 search queries for the top-10 returned articles
Note: you are free to implement or use a third-party tool such as:
1. NLTK: http://www.nltk.org/
2. Stanford NLP: http://nlp.stanford.edu/software/corenlp.shtml
3. Apache OpenNLP: http://opennlp.apache.org/

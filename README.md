
# Semantic Search Project
## Objective:
The project goal is to develop a semantic search application based on Apache Solr. The Google search engine is one among the best search applications developed till now across the globe. The Google search engine incorporates some aspects of the semantic search technique. The obvious intention of a semantic search application is to interpret the user input and understand the semantic meaning and use this knowledge to provide a better result. The primary goal of this project is to implement a keyword-based semantic search and then bring improvements on the result by clever application of Natural Language Processing Techniques.

I divided the project in to four tasks.
## Task 1:
I choosed the brown text corpus which contains more than 1,000 articles and 100,000 words.Please Check src/task1.py for the code.
Brown corpus is from :http://www.sls.hawaii.edu/bley-vroman/brown.txt

## Task 2: 
In Task2,Implemented a shallow NLP pipeline to perform the following:
<ol>
  <li>Keyword search index creation</li>
  <li>Segment the News articles into sentences</li>
  <li>Tokenize the sentences into words</li>
   <li>Index the keywords into search index such as Lucene or SOLR</li>
  <li>Run a search/match against the search index created from the corpus for a user query</li>
</ol>
To execute Task2: 
- Download Solr 7.1.0 from http://www.apache.org/dyn/closer.lua/lucene/solr/7.1.0.
- Extract solr binaries.In Navigate to solr-7.1.0 folder in terminal and run  bin/solr start -p 
- create a core using bin/solr create -c 
- open solr admin on any browser http://localhost:8983/solr/#/.

## Task 3: 
 Implemented a deeper NLP pipeline to perform the following:
* Semantic search index creation uing solr.
* Segment the News articles into sentences
* Tokenize the sentences into words
* Lemmatize the words to extract lemmas as features
* Stem the words to extract stemmed words as features
* Part-of-speech (POS) tag the words to extract POS tag features
* Syntactically parse the sentence and extract phrases, head words, and dependency parse relations as features
* Using WordNet, extract hypernymns, hyponyms, meronyms, and holonyms as features
* Index the various NLP features as separate search fields in a search index such as Lucene or SOLR.
#### Natural language query parsing and search
* Run the above described deeper NLP on an users input natural language and extract search query features.
* Run a search/match against the separate or combination of search index fields created from the corpus.

To execute Task3:
- I used StandfordCoreNLP for extracting the phrases.
- Modify local_corenlp_path to server_corenlp_path to use online version apis maxent tagger vs averaged tagger.
- Navigate to stanfordcorenlp folder and execute the below line to run the server    
  java -mx4g -cp "*"edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000
 

## Task 4:
  Improved the results using the DisMax Query Parser.
  To Do: To improve the results, we can even add the word2Vec model.
  
## Programming Tools: 
  Tools: Python, Brown, Corpus, Solr/Lucene, Pysolr
  NLP Tools: WordNet, Nltk, StanfordCoreNLP

Resources used for this Project are :
1. NLTK: http://www.nltk.org/
2. Stanford NLP: http://nlp.stanford.edu/software/corenlp.shtml
3. Apache OpenNLP: http://opennlp.apache.org/

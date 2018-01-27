'''
1. POS Tagging
Train the corpus using nltk.tag.braudt
2. Stems

3. Lemmas


4. Head words

5. Hypernyms
similarity of words between all hypernyms, highest similarity word should be taken

6. Hyponyms

7. Meronyms

8. Holonyms

9. weights testing (1, 1, 1, 1)
'''
from nltk import word_tokenize
from task3 import SemanticSearch
from gensim.models import Word2Vec as w2v
from nltk.corpus import brown

import pysolr

def train_corpus(corpus):
    b = w2v(brown.sents())
    b.batch_words

def query_solr(input, limit, core):
    solr = pysolr.Solr('http://localhost:8983/solr/' + core)
    query = process_query(input)

    params = {'defType': 'dismax',
              'rows': limit,
              'start': 0,
              'qf': 'lemmas^5.3 hypernyms^6.1 head_word^3.6',
              'sort': 'score desc',
              'fl': 'score, id, tokens'}

    results = solr.search(q=query, **params)

    print("Top 10 documents that closely match the query")
    i = 1
    print "No Document   Score              Content"
    for result in results:
        sentence = ' '.join([str(r) for r in result['tokens']])
        print str(i) + "\t" + result['id'] + "\t" + str(result['score']) + "\t" + sentence
        i = i + 1


def process_query(query):
    ss = SemanticSearch()
    tokens = word_tokenize(query)
    # tags = boost(ss.get_pos_tags(tokens), 10)
    tags = ss.get_pos_tags(tokens)
    lemmas = ss.get_lemmas(tokens, tags)
    stems = ss.get_stems(tokens)
    head_word = ss.get_head_word(query)
    hypernyms = ss.get_hypernyms(tokens)
    hyponyms = ss.get_hyponyms(tokens)
    meronyms = ss.get_meronyms(tokens)
    holonyms = ss.get_holonyms(tokens)
    query = "tokens: " + "||".join(tokens)
    query = query + " pos_tags:" + "||".join(tags)
    query = query + " lemmas: " + "||".join(lemmas)
    query = query + " stem_words: " + "||".join(stems)
    query = query + " hypernyms: " + "||".join(hypernyms)
    query = query + " hyponyms: " + "||".join(hyponyms)
    query = query + " meronyms: " + "||".join(meronyms)
    query = query + " holonyms: " + "||".join(holonyms)

    print query
    return query

def boost(tokens, weight):
    return [token + "^"+str(weight) for token in tokens]


if __name__ == '__main__':
    input = raw_input('Enter your query\n')
    # ss = SemanticSearch()
    # ss.query_solr(input, 'task3')
    query_solr(input, 10, 'task3')
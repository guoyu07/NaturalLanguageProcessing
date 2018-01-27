# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

from nltk.tokenize import word_tokenize
from nltk import tokenize
import re, collections
import pysolr

class Shallow_Search:
    def process_corpus(self, corpus):
        articles = corpus.read().split("\n\n")
        lst = []
        count = 0
        for article in articles:
            if article[0] == '#' and article[-1] == '#':
                continue
            else:
                count = count + 1
                tmp = "".join(article.split('\n'))
                lines = tokenize.sent_tokenize(tmp)
                # lines = re.split('\s\s\s', tmp)
                for j in range(0, len(lines)):
                    id = "".join(['D', str(count), 'S', str(j+1)])
                    lst.append(self.get_dictionary(lines[j], id))
        return lst

    def get_dictionary(self, line, id):
        map = collections.OrderedDict()
        '''Build id '''
        map['id'] = id
        '''Tokenizing'''
        map['tokens'] = word_tokenize(line)
        return map
    
    def add_fields(self, data, core):
        solr = pysolr.Solr('http://localhost:8983/solr/'+core)
        solr.delete(q='*:*') #deletes existing fields and data from solr
        print 'Indexing Core for Task 2'
        solr.add(data ) # indexes data into the core specified
        print 'Completed indexing..'
        
    def query_solr(self, input, core):
        solr = pysolr.Solr('http://localhost:8983/solr/' + core)
        query = self.process_query(input)
        results = solr.search(q=query, sort='score desc', fl='score, id, tokens')

        print("Top 10 documents that closely match the query")
        i = 1
        print "No Document   Score              Content"
        for result in results:
            sentence = ' '.join([str(r) for r in result['tokens']])
            print str(i) + "\t" + result['id']+ "\t" + str(result['score']) + "\t" + sentence
            i = i + 1

    def process_query(self, query):
        tokens = word_tokenize(query)
        query = "tokens:" + "||".join(tokens)
        print query
        return query

if __name__ == '__main__':
    search = Shallow_Search()

    action = raw_input("Enter 1 to Index, 2 to query\n")
    if int(action) == 1:
        corpus = open('brown.txt')
        data = search.process_corpus(corpus)
        search.add_fields(data, core ='task2')
    elif int(action) == 2:
        query = raw_input("Enter your query\n")
        search.query_solr(query, core='task2')

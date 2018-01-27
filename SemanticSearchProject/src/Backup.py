from _functools import reduce
import collections
import io
import json
import os

from nltk import tokenize
from nltk.tokenize import word_tokenize
import pysolr

import pandas as pd
import csv


class IndexCreation:

    def preprocessCorpus(self, path):
        print("Pre-processing and Tokenizing...")
        data = self.readArticles(path)
        data = self.removeArticleTitle(data)

        indexWordsMap = self.createIndexMap(data)
        with io.open('MainData.csv', 'w', encoding='utf-8', errors='ignore') as f:
            w = csv.writer(f)
            w.writerows(indexWordsMap.items())
        wordsDFrame = pd.DataFrame(list(indexWordsMap.items()), columns=['id', 'words'])

        jsonFileName = 'Task2.json'
        wordsDFrame.to_json(jsonFileName, orient='records')
        return data, indexWordsMap, wordsDFrame, jsonFileName

    def readArticles(self, path):
        data = []
        for f in sorted(os.listdir(path), key=lambda x: int(x.split('.')[0])):
            with io.open(path + f, 'r', encoding='utf-8', errors='ignore') as dataFile:
                data.append(dataFile.read())
        return data

    def removeArticleTitle(self, data):
        for i in range(len(data)):
            sentences = tokenize.sent_tokenize(data.pop(i).strip())
            temp = sentences.pop(0).split('\n\n')
            if len(temp) == 2:
                sentences.insert(0, temp[1])
            data.insert(i, sentences)
        return data

    def createIndexMap(self, data):
        indexWordsMap = collections.OrderedDict()
        for i in range(0, len(data)):
            for j in range(0, len(data[i])):
                index = 'A' + str(i + 1) + 'S' + str(j + 1)
                indexWordsMap[index] = list(set(word_tokenize(data[i][j])))
        return indexWordsMap

    # Refer https://github.com/Parsely/python-solr/blob/master/pythonsolr/pysolr.py
    def indexFeaturesWithSolr(self, jsonFileName):
        print("Indexing...")
        solr = pysolr.Solr('http://localhost:8983/solr/task3')
        # solr.delete(q='*:*')

        with open("/Users/deepaks/Documents/workspace/Semantic_Search_Engine/pkg/" + jsonFileName, 'rb') as jsonFile:
            entry = json.load(jsonFile)
        solr.add(entry)


    def processQueryToExtractWords(self, query):
        return list(set(word_tokenize(query)))
    
    def searchInSolr(self, query):
        solr = pysolr.Solr('http://localhost:8983/solr/task2')
        query = "words:" + " || words:".join(query)
        results = solr.search(query)
        print("Top 10 documents that closely match the query")
        for result in results:
            print(result['id'])


if __name__ == '__main__':
    ic = IndexCreation()
    path = '/Users/deepaks/Documents/workspace/Semantic_Search_Engine/Data/'
    data, indexWordsMap, wordsDFrame, jsonFileName = ic.preprocessCorpus(path)
    ic.indexFeaturesWithSolr(jsonFileName)
    
    query = input("Enter the input query: ")
    processedQuery = ic.processQueryToExtractWords(query)
    ic.searchInSolr(processedQuery) 


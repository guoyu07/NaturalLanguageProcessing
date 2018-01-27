# from task3 import SemanticSearch as ss
from stanfordcorenlp import StanfordCoreNLP
from nltk import word_tokenize
import collections, pysolr, re


def  get_head_words(corpus):
    articles = corpus.read().split("\n\n")
    lst = []
    count = 0
    for article in articles:
        if article[0] == '#' and article[-1] == '#':
            continue
        else:
            count = count + 1
            tmp = "".join(article.split('\n'))
            lines = re.split('\s\s\s', tmp)
            for j in range(0, len(lines)):
                id = "".join(['D', str(count), 'S', str(j + 1)])
                # lst.append(self.get_dictionary(tokens, id))
                # tokens = word_tokenize(lines[j])
                map = get_dictionary(lines[j], id)
                lst.append(map)
                print map
    return lst

def get_dictionary(line, id):
    # try:
    map = collections.OrderedDict()
    '''Build id '''
    map['id'] = id
    '''Root words'''
    try:
        map['head_words'] = get_head_word(line)
    except:
        map['head_words'] = ''
    return map

def get_head_word(line):
    # https://github.com/Lynten/stanford-corenlp
    nlp = StanfordCoreNLP('http://127.0.0.1', port=9000)  # standalone method, with jar files downloaded locally
    # nlp = StanfordCoreNLP('http://nlp.stanford.edu:8080/corenlp/', port=8080)  # standalone method, with jar files downloaded locally
    # nlp = StanfordCoreNLP(r'C:/packages/nlp/stanford-corenlp-full-2017-06-09/') # with a working internet connection
    dep_parse = nlp.dependency_parse(line)
    ''' sentence = 'workers dumped sacks into a bin'
    0 workers 1 dumped 2 sacks 3 into 4 a 5 bin 6
    Dependency Parsing:
    [(u'ROOT', 0, 2), (u'nsubj', 2, 1), (u'dobj', 2, 3), (u'case', 6, 4), (u'det', 6, 5), (u'nmod', 2, 6)]
    head word: dumped # ROOT
    '''
    text = word_tokenize(line)
    head = ''
    if len(dep_parse) == 0:
        return head
    elif (len(dep_parse[0]) > 2) and (dep_parse[0][0] == u'ROOT') and len(text) > 0:
        head = text[dep_parse[0][2] - 1]
    print line, head
    return head


def update_corpus(data, core):
    solr = pysolr.Solr('http://localhost:8983/solr/' + core)
    solr.delete(q='*:*')  # deletes existing fields and data from solr
    print 'Indexing head words...'
    solr.add(data)  # indexes data into the core specified
    print 'Completed indexing head words'


if __name__ == '__main__':
    head_words = get_head_words(corpus=open('brown_1100.txt'))
    update_corpus(head_words, 'task3')
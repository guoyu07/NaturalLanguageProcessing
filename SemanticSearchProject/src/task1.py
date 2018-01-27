import re
from nltk import word_tokenize
def process_corpus(corpus):
    articles = corpus.read().split("\n\n")
    lst = []
    count = 0
    article_count, line_count, word_count, document_count = 0, 0, 0, 0
    for article in articles:
        if article[0] == '#' and article[-1] == '#':
            document_count = document_count + 1
            continue
        else:
            article_count = article_count + 1
            if article_count > 1100:
                print article
                break
            count = count + 1
            tmp = "".join(article.split('\n'))
            lines = re.split('\s\s\s', tmp)
            lineNo = 0
            for j in range(0, len(lines)):
                lineNo = lineNo + 1
                line_count = line_count + 1
                word_count = word_count + len(word_tokenize(lines[j]))
                print 'D'+str(article_count)+'L'+str(lineNo)
    print "Documents: ", article_count, "Lines: ", line_count
    print "Articles: ", document_count, "Words: ", word_count

if __name__=='__main__':
    process_corpus(open('brown_1100.txt'))

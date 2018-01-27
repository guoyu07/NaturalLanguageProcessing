# Step 1:
download brown corpus as a single file from http://www.sls.hawaii.edu/bley-vroman/brown.txt
download Solr 7.1.0 for windows/Mac/Linux from http://www.apache.org/dyn/closer.lua/lucene/solr/7.1.0

# Step 2:
extract solr binaries
navigate to solr-7.1.0 folder (C://solr-7.1.0)
run bin/solr start -p <port no> //8983 default
create a core by bin/solr create -c <your collection> //can add more parameters such as shards, replicas in this step

open solr admin on any browser http://localhost:8983/solr/#/ to check if your solr is running
choose Core Admin if the core that is created earlier is present here

# Step 3:
## Task2
Run script task2.py to add data to the core created earlier, change the name of the core in this file at the following line 
solr = pysolr.Solr('http://localhost:8983/solr/<your collection>')<currently at 42>
specify the address of the corpus (brown.txt) from step 1 in this file

This step will add data (fields and data eg: [{id: "A1S1", tokens: "There is a cat on the wall.")}] to the earlier created core and also allows you to query the solr from console

# Step 4:
## Task3
TO-DO
for head words, modify local_corenlp_path to server_corenlp_path to use online version apis
maxent tagger vs averaged tagger, how to use particular tagger, advantages vs disadvantages
go to stanfordcorenlp folder and execute the below line to run a server for
java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000


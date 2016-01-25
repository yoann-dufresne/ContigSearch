ContigSearch
=============

Usage
----------
If the jar file is up to date, just download it, otherwise download all the code and run compile.sh  
Then run the command line:  
`java -cp "ContigSearch.jar" main.Main {options}`  
  
Options:  
`-n <filename>` The path for the file containing the nodes  
`-e <filename>` The path for the file containing the edges  
`-b <basename>` The basename for the output files  
`-N x` Set to x the node filter parameter. The node filter parameter is used to delete nodes
containing only x or less reads.  
`-E x` Set to x the edge filter parameter. The edge filter parameter is used to delete edges
representing only x or less edges from the original graph.  
`-oc` Set true for the optimization contig flag. If the oc flag is true, the algorithm will try
to move nodes from hubs to contigs. If there is one neighbor contig for this node, it moves,
otherwise nothing append.

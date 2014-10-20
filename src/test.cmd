::type five.txt | java  -ea lessmoon.nlp.util.DicReader
::type dic.txt | java -ea lessmoon.nlp.util.Dictionary > dicTree.txt
type testdata.txt | java -ea lessmoon.nlp.main.Main > seg.txt
pause

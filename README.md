#TokenXML

![image](https://travis-ci.org/mattmaynes/TokenXML.svg)

##Lightweight standalone Java XML parsing.
This project has been written in (nearly) vanilla Java to allow for 
portability across platforms with no modifications. There are a few 
dependancies on `java.util` that are being worked out. TokenXML offers
extra support for special XML nodes such as DOCTYPE declarations, 
comment nodes and others. TokenXML also includes a formatter that
can be used to 'pretty print' an XML document.

##Downloading
All of the source code is included in the src directory. There
currently is no release version of the code but there should be 
in the near future.

##Compiling
There is a `make` file included which is not very protable. There
should soon be an ant script that will do the same task.

To compile manually use the following

```
javac -sourcepath src -d bin src/emex/xml/*.java src/emex/xml/parse/*.java

Windows
javac -sourcepath src -d bin src\emex\xml\*.java src\emex\xml\parse\*.java
```



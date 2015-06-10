Aliyun server revises a few API, so we have to change the code accordingly.
If you only modify cunstomlized package, then simply replace them in the new SampleCode.zip would work.


*************************************************************
To run the code, you need to download maven and jdk 1.8+

1. replace setting.xml in \maven\conf with setting.xml in this directory

2. open a command line window and enter this directory

3. mvn compile
It may take minutes or even hours to automatically download dependent libraries. Fortunately, you get rid of it later when it was done.

4. mvn package
At this moment, you will get a .jar file named as "IJCAI-1.0-jar-with-dependencies.jar"
Please rename this file as "project.jar""!!


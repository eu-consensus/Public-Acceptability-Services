Public-Acceptability-Services
=======================

Instructions about the webservices usage are included in the index page of the project.

Deployment:

The project will use a glassfish jdbc resource pool that you will have to create. Instructions about that can be found online in the Glassfish documentation.
After the pool is created and tested you will have to set the resource name. 
To do that you will have to edit the GlobalVariables class file and change the databasePool variable into the new value.
The default value is "consensus_pool_resource". After that the database connection should be ready.

In the same class file there is a variable called trainDataDir. 
You will have to change it to point in the directory where the training data are stored for the Sentiment Analysis algorithms.
The training data are included in the present rar file and they are located in the path "ConsensusPublicOpinion\web\trainData".
Do not change the filenames of the files contained in this folder but feel free to relocate them updating this variable accordingly.

These two should be the only options you need to preconfigure. If you need further help or any bugs come up please contact me at vpsomak@mail.ntua.gr.

Software Used:
NetBeans IDE 8.0.2 (Build 201411181905)
Java(TM) SE Runtime Environment 1.7.0_45-b18
Windows 7 version 6.1
GlassFish Server Open Source Edition 4.1 (build 13)
MySql Version 14.14 Distribution 5.5.40 for debian-linux-gnu (x86_64)


For more information please contact me at vpsomak@mail.ntua.gr

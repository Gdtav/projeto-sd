# Program Architecture

    Multicast Servers
    RMI Servers (Main and Backup)
    RMI Client
    Web Interface

## Multicast Servers ##

 - Each Server keeps a copy of the programs' Database
 - How to update all copies at the same time? Checksum?
 - Only one Server services one request at a time

## RMI Servers ##

 - Contains Methods for interacting with the Multicast Servers through the message protocol defined in _Protocol.md_
 - Backup server continuously checks on the main server for quick recovery of errors, becoming the main server afterwards

## RMI Client ##

 - Simple UI with Graphical Interface 
 - Interacts with the RMI server through UDP
 - Directly deals with download/upload of music files with one Multicast Server through TCP (IP Request)

## Web Interface ##

 - Implementation with JSP's and Struts2
 - Deployment on Tomcat Server
 - MVC architecture
     - DropBean object for 
        - storage of session-related information
        - communication with RMI server
     - Action classes defined through the struts.xml for interaction with the user
     - JavaServer Pages for dinamically present content to the user
 
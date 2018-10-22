# Protocol Definition

In order to provide communication between Multicast servers and RMI Servers,
 a protocol has been defined for message standardization.

## Message Format

The messages' format will be as such:

    key | value ; key2 | value2

All key-value pairs in a single message will be related to a single object,
 separated by a \n character at the end of each message. As an example,
 
    type | artist ; name | Artist Name ; activity_start | 1970 ; activity_end | 2000
    
Fuck this. Pedro, end this shit!
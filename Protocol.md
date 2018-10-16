# Protocol Definition

In order to provide communication between Multicast servers and RMI Servers,
 a protocol has been defined for message standardization.

## Message Format

The messages' format will be as such:

    key | value ; key2 | value2

All key-value pairs in a single message will be related to a single object,
 separated by a \n character at the end of each message.
 
    nani?

## Description

This project implements a distributed room-rental management platform consisting of a Java-based backend and an Android frontend. The backend follows 
a Master–Worker architecture, where the Master handles client requests, distributes data across multiple nodes using hashing, coordinates parallel 
processing through a MapReduce-style workflow, and ensures thread-safe updates for reservations. Worker nodes store room information in memory, manage 
availability, and process filtering and booking requests sent by the Master. On the manager side, a console application allows owners to register 
accommodations, define availability periods, and review bookings. Renters use an Android application to browse listings, apply search filters, book 
rooms, and rate their stay, with all communication performed asynchronously over TCP sockets. The system supports scalable deployment, concurrent 
client handling, and optional active replication for fault tolerance. Development is delivered in two phases: 
first the full backend with a basic client for search and booking, and then the complete Android interface with all required features integrated.
Done as part of a university assignment.

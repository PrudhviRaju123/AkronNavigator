
**Introduction:** New students in the University of Akron have to go through Roo Bus Schedule about dropping stations and pick-ups for utilizing this service.  For a new entrant in the campus, sometimes find it little difficult to sort out which Roo Bus can drop him to the nearest location to his destination (places which are not dropping stations like banks, Federal Building  ...etc.).

Visualizing the user’s GPS location and his destination on Google Maps would be the need of the hour for better utilization of Roo service. As the part of term project, I am designing an Android App which shows the navigation from your current GPS location to destination on different modes like walking and Roo Bus. 

**Navigation using walking:** In this mode, the user will be directed to destination using the walking lanes. 

**Navigation using Roo Bus:** In this mode, the user will directed to destination using combination of multiple navigations.

1.	Navigation from current user location to the nearest Roo Bus stop using walking lanes
2.	Navigation from Roo Bus pick up station  to nearest destination Drop point  
3.	Navigation from Roo Bus Drop point to destination using walking lane

**Background:** The Roo Express Shuttle Service provides transportation to University buildings in the downtown area, neighborhoods near campus, and the parking lots on the North side of campus. During the fall and spring semesters the shuttle also services students on the weekends to and from the downtown area.

Implementation:

a.	After the user opens the mobile app, the user GPS location is recorded using Google Location Listener.

b.	Database instance is created and to retrieve the GPS locations of WEST ROUTE Bus.

c.	The user input’s his destination using his voice or by typing.

d.	GPS location of the destination are derived using Google geocoding API from the destination which is in words ex:  “Student Union”.

e.	Distance between the GPS user location and every Roo stop is calculated and the Roo stop with minimum distance to the user location is considered as initial location.

f.	Similarly the distance between every Roo stop GPS location destination is calculated and the minimum distance to the destination is considered as destination location.

g.	When the user choses  the option to display the way for  destination in walking  Lane , The GPS  location of the user and destination location values are send to html page in which JavaScript displays the Google Maps and highlights the polyline path between user location to destination . 



Polylines between user location and destination using walking lanes
![](https://sites.google.com/site/uakron12345/home/11.jpg)


h.	When the user choses  the option to display the way for  destination in Roo Bus  mode , The GPS  location of the user and destination location values along with relevant Roo stop GPS locations are send to html page in which JavaScript displays the Google Maps and highlights the polyline path between user location to destination . Below Figure shows the highlighted path between source and destination


 Polyline showing source to destination using Roo Bus mode
![](https://sites.google.com/site/uakron12345/home/12.png)



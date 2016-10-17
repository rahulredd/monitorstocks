# stockmonitor
1)	Export it to Eclipse.
(Right click in Package Explorer -> Import -> Existing Maven Projects)
2)	In Eclipse, right click on the project name, Run As -> Run on Server.
3)	Alternatively, after starting the tomcat server on Eclipse, just enter the below URL in a web browser.
http://localhost:8080/MonitorStocks/stockMonitor.html
4)	It will open the web page which looks the screenshot below



5)	For options Add a Company, Get Company Stocks, Delete a Company and Company Stock History a company ticker has to be entered in the text box. 
Ex:- AMZN (or) MSFT (or) APPL (or) GOOGL
‘Add a Company’ option just adds a new company to the database.
‘Get Company Stocks’ gets latest stocks of the company every 5 minutes and updates it to the database.
‘Delete a Company’ option deletes a company from the database. 



6)	Company Stock History 
This option displays a column chart using Charts.js with the historic stock values for a company.
(Please use “FB/AMZN” when trying it on your machine as the remote database has several stock entries for these company).




7)	List Companies
This option will list all the companies in the database.




8)	List Companies – List Stocks 
Lists latest stocks for all companies in the database. It displays stocks of only the companies that have at least one stock entry in the database.

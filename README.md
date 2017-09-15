[![GitHub version](https://badge.fury.io/gh/d-Rickyy-b%2FTSF-Parser.svg)](https://badge.fury.io/gh/d-Rickyy-b%2FTSF-Parser)

# TSF-Parser
This is a Java application to parse and download the [TSF](https://core.telegram.org/tsi) leaderboards to a local database

## Database
To use this application you need a SQLite v3 Database. I recommend [SQLite Browser](http://sqlitebrowser.org/) for creating and editing the database. You'll find an empty database in the 'Release' section of this repo. You can create one on your own with the following SQL statement:

```SQL
CREATE TABLE 'stats' (
	'name'	TEXT NOT NULL UNIQUE,
	'region' TEXT,
	'jan'	INTEGER NOT NULL DEFAULT 0,
	'feb'	INTEGER NOT NULL DEFAULT 0,
	'mar'	INTEGER NOT NULL DEFAULT 0,
	'apr'	INTEGER NOT NULL DEFAULT 0,
	'may'	INTEGER NOT NULL DEFAULT 0,
	'jun'	INTEGER NOT NULL DEFAULT 0,
	'jul'	INTEGER NOT NULL DEFAULT 0,
	'aug'	INTEGER NOT NULL DEFAULT 0,
	'sep'	INTEGER NOT NULL DEFAULT 0,
	'oct'	INTEGER NOT NULL DEFAULT 0,
	'nov'	INTEGER NOT NULL DEFAULT 0,
	'dec'	INTEGER NOT NULL DEFAULT 0
);
```

## Usage

You can run the .jar file as it is. Then a GUI opens up and you can use it as other programs.

OR you can use it as a commandline program. Just pass the **full** path to the database file as a command line parameter. This will look something like this:

```java -jar TSF_Parser_v0.x.x.jar C:\Users\<yourName>\Desktop\tsf-database.db```

The program will then launch in a command prompt.

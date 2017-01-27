[![GitHub version](https://badge.fury.io/gh/d-Rickyy-b%2FTSF-Parser.svg)](https://badge.fury.io/gh/d-Rickyy-b%2FTSF-Parser)

# TSF-Parser
This is a Java application to parse and download the [TSF](https://core.telegram.org/tsi) leaderboards to a local database

## Database
To use this application you need a SQLite v3 Database. I recommend [SQLite Browser](http://sqlitebrowser.org/) for creating and editing the database. You can create one on your own with the following SQL statement:

```SQL
CREATE TABLE 'stats' (
	'name'	TEXT NOT NULL UNIQUE,
	'region'	TEXT,
	'jan'	INTEGER NOT NULL DEFAULT 0,
	'feb'	INTEGER NOT NULL DEFAULT 0,
	'mar'	INTEGER NOT NULL DEFAULT 0,
	'apr'	INTEGER NOT NULL DEFAULT 0,
	'mai'	INTEGER NOT NULL DEFAULT 0,
	'jun'	INTEGER NOT NULL DEFAULT 0,
	'jul'	INTEGER NOT NULL DEFAULT 0,
	'aug'	INTEGER NOT NULL DEFAULT 0,
	'sep'	INTEGER NOT NULL DEFAULT 0,
	'oct'	INTEGER NOT NULL DEFAULT 0,
	'nov'	INTEGER NOT NULL DEFAULT 0,
	'dec'	INTEGER NOT NULL DEFAULT 0
);
```

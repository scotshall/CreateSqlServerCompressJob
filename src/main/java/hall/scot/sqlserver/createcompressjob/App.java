package hall.scot.sqlserver.createcompressjob;

/**
 * Creates a SQL Server script that will create a SQL Agent job to the following:
 * <ol>
 * <li>Compress all tables in a database</li>
 * <li>Compress all indexes in a database</li>
 * <li>Shrink the database data file</li>
 * <li>Rebuild the database indexes</li>
 * </ol>
 * <p>
 * Prior to running this program, you need to generate the list of tables and indexes in the 
 * database.  The following SQL statments will list all tables and indexes in order from smallest to 
 * largest in size.  Save the results in src/resources/Tables.csv and src/resources/Indexes.csv, 
 * respectively.
 * <p>
 * List tables:<br>
 * <pre>
 * SET NOCOUNT ON
    SELECT 
        s.name AS SchemaName,
        o.name AS TableName,
        'ALTER TABLE ' + '[' + s.[name] + ']'+'.' + '[' + o.[name] + ']' + ' REBUILD PARTITION = ALL WITH (DATA_COMPRESSION=PAGE);' AS CompressStatement 
        FROM sys.objects AS o WITH (NOLOCK)
    INNER JOIN sys.indexes AS i WITH (NOLOCK) ON o.[object_id] = i.[object_id]
    INNER JOIN sys.schemas AS s WITH (NOLOCK) ON o.[schema_id] = s.[schema_id]
    INNER JOIN sys.dm_db_partition_stats AS ps WITH (NOLOCK) ON i.[object_id] = ps.[object_id] AND ps.[index_id] = i.[index_id]
    WHERE o.[type] = 'U'
    ORDER BY ps.[reserved_page_count]
 * </pre>
 * List indexes:
 * <pre>
 * SET NOCOUNT ON
SELECT 
	i.name AS IndexName,
	s.name AS SchemaName,
	o.name AS TableName,
	'ALTER INDEX '+ '[' + i.[name] + ']' + ' ON ' + '[' + s.[name] + ']' + '.' + '[' + o.[name] + ']' + ' REBUILD PARTITION = ALL WITH (DATA_COMPRESSION=PAGE);' AS CompressStatement
FROM sys.objects AS o WITH (NOLOCK)  
INNER JOIN sys.indexes AS i WITH (NOLOCK) ON o.[object_id] = i.[object_id]
INNER JOIN sys.schemas s WITH (NOLOCK) ON o.[schema_id] = s.[schema_id]
INNER JOIN sys.dm_db_partition_stats AS ps WITH (NOLOCK) ON i.[object_id] = ps.[object_id] AND ps.[index_id] = i.[index_id]
WHERE o.type = 'U' AND i.[index_id] >0
ORDER BY ps.[reserved_page_count]
 * </pre>
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}

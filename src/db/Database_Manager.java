package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Statement;
import java.util.Vector;

public class Database_Manager
{

	// ----------------------------url_db ====
	// "jdbc:mysql://localhost:3306/Crawler_Database"
	// ----------------------------user_db ==== "root"
	// ----------------------------pass_db ==== "*******"
	private Connection connection;
	private Statement stmt = null;
	public Vector<Integer> list_of_Rep_W = new Vector<Integer>();

	// private Server hsqlServer ;
	// ----------------------------- get date with format "yyyy-MM-dd"
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Calendar cal = Calendar.getInstance();
	public int depth;

	// ===================================================

	public Database_Manager()
	{
		connection = null;
		stmt = null;
	}

	// ===================================================

	public void Openconn(String url_db, String user_db, String pass_db) // throws
																		// ClassNotFoundException
	{
		try
		{
			// hsqlServer = new Server();
			// hsqlServer.start();

			// Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection(url_db, user_db, pass_db);
			// stmt = connection.createStatement();
		} catch (java.sql.SQLException e)
		{
			System.out.println(e.getMessage());
		}
		;

	}

	// ===================================================

	public void Closeconn()
	{
		try
		{
			if (connection != null)
				connection.close();
		} catch (java.sql.SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// ===================================================

	public void Insert(String W_site, String URL, int Depth)
	{
		try
		{
			ResultSet rs1;
			// ResultSet rs1 =
			// connection.prepareStatement("select * from Crawler_Database.Websites where Website ='"+W_site+"' ;").executeQuery();
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.Websites where Website ='"
							+ W_site + "' ;");
			// rs1.next();rs1.getString(1) != null
			if (rs1.next())
			{
				connection
						.prepareStatement(
								"insert into Crawler_Database.URLs (URL,W_id,Insertion_Date,Crawled,Depth) values ('"
										+ URL
										+ "',"
										+ rs1.getInt(1)
										+ ",'"
										+ dateFormat.format(cal.getTime())
												.toString()
										+ "',false,"
										+ Depth + ");").execute();
				// connection.prepareStatement("insert into Crawler_Database.URLs (URL,W_id,Insertion_Date,Crawled) values ('"+URL+"',"+rs1.getInt(1)+",'"+dateFormat.format(cal.getTime()).toString()+"',false);").execute();
			} else
			{
				// stmt = connection.createStatement();

				connection.prepareStatement(
						"insert into Crawler_Database.Websites (Website) values ('"
								+ W_site + "');").execute();
				// ResultSet rs2 =
				// connection.prepareStatement("select * from Crawler_Database.Websites where Website ='"+W_site+"' ;").executeQuery();
				rs1 = stmt
						.executeQuery("select * from Crawler_Database.Websites where Website ='"
								+ W_site + "' ;");
				rs1.next();
				connection
						.prepareStatement(
								"insert into Crawler_Database.URLs (URL,W_id,Insertion_Date,Crawled,Depth) values ('"
										+ URL
										+ "',"
										+ rs1.getInt(1)
										+ ",'"
										+ dateFormat.format(cal.getTime())
												.toString()
										+ "',false,"
										+ Depth + ");").execute();
				// connection.prepareStatement("insert into Crawler_Database.URLs (URL,W_id,Insertion_Date,Crawled) values ('"+URL+"',"+rs1.getInt(1)+",'"+dateFormat.format(cal.getTime()).toString()+"',false);").execute();

			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// ===================================================

	public void Update(String URL, String C_Title, String C_Page)
	{
		try
		{
			ResultSet rs1;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.URLs where URL ='"
							+ URL + "' ;");
			if (rs1.next())
			{
				// ResultSet rs1; =
				// connection.prepareStatement("select * from Crawler_Database.URLs where URL ='"+URL+"' ;").executeQuery();
				connection.prepareStatement(
						"insert into Crawler_Database.content (C_ID,Title,T_Page) values ("
								+ rs1.getInt(1) + ",'" + C_Title + "','"
								+ C_Page + "');").execute();
				connection.prepareStatement(
						"UPDATE Crawler_Database.URLs SET Last_revision_Date = '"
								+ dateFormat.format(cal.getTime()).toString()
								+ "' , Crawled = true Where U_ID = "
								+ rs1.getInt(1) + ";").execute();
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// ===================================================

	public void Delete(String W_site)
	{
		try
		{
			connection.prepareStatement(
					"DELETE FROM Crawler_Database.Websites where Website = '"
							+ W_site + "' ;").execute();
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	// ===================================================

	public boolean Is_Content_Wsite(String W_site)
	{
		boolean state = false;
		try
		{
			ResultSet rs1;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.Websites where Website ='"
							+ W_site + "' ;");
			if (rs1.next())
			{
				state = true;
			} else
				state = false;
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return state;
	}

	// ===================================================

	public boolean Is_Content_Urls(String URL_site)
	{
		boolean state = false;
		try
		{
			ResultSet rs1;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.URLs where URL ='"
							+ URL_site + "' ;");
			if (rs1.next())
			{
				state = true;
				// return state;
			} else
			{
				state = false;
				// return state;
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return state;
	}

	// ===================================================

	public String GET_Waited_Link(int max_depth)
	{
		try
		{
			String URL;
			ResultSet rs1;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select  * from Crawler_Database.URLs where Crawled = false AND Locked = false AND Depth <> "
							+ max_depth + " order by U_ID ASC LIMIT 1 ;");
			if (rs1.next())
			{
				URL = rs1.getString(2);

				depth = rs1.getInt(8);
				connection.prepareStatement(
						"UPDATE Crawler_Database.URLs SET Locked = true Where U_ID = "
								+ rs1.getInt(1) + ";").execute();
				return URL;
			} else
			{
				return null;
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}

	// ===================================================

	public void UnlockAll()
	{
		try
		{
			connection
					.prepareStatement(
							"UPDATE Crawler_Database.URLs SET Locked = false Where Locked = true;")
					.execute();
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

	}

	// ===================================================
	public void Insert_KeyWord(String word, String URL, int Rep)
	{
		try
		{
			ResultSet rs1;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.URLs where URL ='"
							+ URL + "' ;");
			if (rs1.next())
			{
				connection.prepareStatement(
						"insert into Crawler_Database.key_Words (Word,W_U_ID,Repetition) values ('"
								+ word + "'," + rs1.getInt(1) + "," + Rep
								+ ");").execute();

			} else
			{
				System.out.println("Please .. insert URL into Database ");
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

	}

	// ===================================================
	public Vector<String> Search(String word)
	{
		Vector<String> list_of_urls = new Vector<String>();
		Vector<Integer> list_of_idurls = new Vector<Integer>();
		list_of_Rep_W.clear();
		list_of_urls.clear();
		list_of_idurls.clear();
		try
		{
			ResultSet rs1, rs2;
			stmt = connection.createStatement();
			rs1 = stmt
					.executeQuery("select * from Crawler_Database.key_Words where Word ='"
							+ word + "' ;");
			while (rs1.next())
			{
				list_of_idurls.add(rs1.getInt("W_U_ID"));
				list_of_Rep_W.add(rs1.getInt("Repetition"));
			}
			for (int i = 0; i < list_of_idurls.size(); i++)
			{
				rs2 = stmt
						.executeQuery("select * from Crawler_Database.URLs where U_ID ='"
								+ list_of_idurls.get(i) + "' ;");
				while (rs2.next())
				{
					list_of_urls.add(rs2.getString("URL"));
				}
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return list_of_urls;
	}
	// ===================================================
}

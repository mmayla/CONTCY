package contcy;

import java.io.IOException;
import java.security.acl.LastOwnerException;

import javax.management.DynamicMBean;

import db.Database_Manager;

public class Spider
{
	// Data members
	private PageCrawler PC;
	private Database_Manager DBM;
	private int MAX_DEPTH;
	private int MAX_KEYWORDS;
	private int MAX_CrawlingPages;
	private int CrawledPages;

	// Methods
	public Spider(int maxtocrawl)
	{
		PC = new PageCrawler();
		DBM = new Database_Manager();
		DBM.Openconn("jdbc:mysql://127.0.0.1:3306/Crawler_Database", "root","");

		MAX_DEPTH = 10;
		MAX_KEYWORDS = 5;
		MAX_CrawlingPages = maxtocrawl;
		CrawledPages = 0;
	}

	/*
	 * RUN
	 */
	public void run()
	{
		// get oldest non-crawled url
		String parent_url;
		int parent_depth;
		while (true)
		{
			try
			{
				if (CrawledPages >= MAX_CrawlingPages)
					return;

				parent_url = DBM.GET_Waited_Link(MAX_DEPTH);
				System.out.println(parent_url);
				parent_depth = DBM.depth;
				if (parent_url != null) // found
				{
					PC.setPage(parent_url);
					PC.Crawl();
					System.out.println("################# UPDATE");
					DBM.Update(parent_url.trim(), PC.title_str, PC.body_str);
					insertAll(parent_url.trim(), parent_depth);

					// insert keywords
					for (int kw = 0; kw < MAX_KEYWORDS; kw++)
					{
						if (kw >= PC.keyWords.size())
							break;
						DBM.Insert_KeyWord(PC.keyWords.get(kw).word,
								parent_url, PC.keyWords.get(kw).count);
					}

					// TODO states output
					System.out.println(parent_url);
				} else
				// not found
				{
					// TODO states output
					System.out.println("End Crawling");
					break;
				}
			} catch (IOException e)
			{
				System.err.println("## Broken link");
				continue;
			} catch (Exception e)
			{
				System.err.println("## Broken link");
				continue;
			}

			CrawledPages++;
			System.out.println("Crawled : " + CrawledPages);
		}
	}

	// Database methods
	/*
	 * insert seed url
	 */
	public void insertSeed(String seed)
	{
		// insert in db
		seed = insertLastSlash(seed);
		DBM.Insert(PageCrawler.getWebsiteURL(seed), seed, 0);
	}
    
	/**
	 * 
	 * @param url to handle it's last slash
	 * @return url after handling
	 */
	private String insertLastSlash(String url)
	{
		//if(url.charAt(url.length()-1)!='/')
			//url = url.concat("/");
		return url;
	}
	
	/*
	 * insert urls to db
	 */
	private void insertAll(String website_url, int pdepth)
	{
		int i = 0;
		for (String url : PC.links_list)
		{
			// TODO states output
			url = insertLastSlash(url);
			System.out.println("*** Inserting(" + i++ + "/"
					+ (PC.links_list.size() - 1) + "): " + url);

			// is url domain different that parent url domain
			if (!PageCrawler.getWebsiteURL(url).equals(
					PageCrawler.getWebsiteURL(website_url)))
			{
				// is url domain exist
				if (DBM.Is_Content_Wsite(PageCrawler.getWebsiteURL(url)))
				{
					// is not url exist
					if (!DBM.Is_Content_Urls(url))
					{
						// insert url with depth = max depth-1 (to be crawled
						// once)
						DBM.Insert(PageCrawler.getWebsiteURL(url), url,
								MAX_DEPTH - 1);
					}
				} else
				{
					// is url exist
					if (!DBM.Is_Content_Urls(url))
					{
						// insert the domain url and crawled url
						DBM.Insert(PageCrawler.getWebsiteURL(url),
								PageCrawler.getWebsiteURL(url), 0);
						DBM.Insert(PageCrawler.getWebsiteURL(url), url, 0);
					}
				}
			} else
				DBM.Insert(PageCrawler.getWebsiteURL(website_url), url,
						pdepth + 1);
		}
	}
}

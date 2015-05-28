package contcy;

import java.io.IOException;
import java.util.Vector;

import javax.xml.transform.Templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utility.WordCntPair;
//TODO: delete last '/'
public class PageCrawler
{
	// Data members
	// page variables
	private String page_url;
	private Elements links_elems;
	private Elements all_elems;
	private Document page;

	// result variables
	protected Vector<String> body_list;
	protected Vector<WordCntPair> keyWords;
	protected Vector<String> links_list;
	protected String title_str;
	protected String body_str;

	/**
	 * Constructor
	 */
	public PageCrawler()
	{
		// initialize list
		body_list = new Vector<String>();
		links_list = new Vector<String>();
		keyWords = new Vector<WordCntPair>();
	}

	/**
	 * 
	 * @param purl
	 *            the URL to crawl
	 * @throws IOException
	 */
	public void setPage(String purl) throws IOException
	{
		try
		{
			System.out.println("Connecting to: " + purl);
			page_url = purl;
			page = Jsoup.connect(purl).get();
			all_elems = page.getAllElements();
			links_elems = page.select("a[href]");
		} catch (IOException e)
		{
			System.err.println("PageCrawler: Error getting page");
			throw e;
		}
	}

	/**
	 * get all links from the page
	 */
	private void fillLinksList()
	{
		// clear list
		links_list.clear();
		int frst, lst;
		// add links without redundancy
		String temp;
		for (Element elem : links_elems)
		{
			temp = elem.absUrl("href").toLowerCase().trim();

			frst = temp.indexOf('.');
			lst = temp.lastIndexOf('.');

			if (!links_list.contains(temp) && !temp.contains("#")
					&& frst == lst)
			{
				if (temp.trim().length() > 0
						&& temp.substring(0, 4).equals("http"))
					links_list.add(temp);
			}
		}
	}

	/**
	 * get all words in page
	 */
	private void fillBodyList()
	{
		// clear list
		body_list.clear();
		keyWords.clear();
		body_str = "";

		// add words without redundancy
		String temp;
		for (Element elem : all_elems)
		{
			// split line to strings then insert them in the list
			String[] templist;
			temp = elem.text().toLowerCase().trim();
			temp = temp.replaceAll("[^a-zA-Z ]", "");

			templist = temp.split(" ");

			for (String str : templist)
			{
				str = str.trim();

				// key words counting
				if (!findAndIncrement(str))
				{
					// TODO eliminate all non nouns
					WordCntPair newpair = new WordCntPair(str, 0);
					keyWords.add(newpair);
				}

				// form the body words
				if (str.trim().length() > 0)
				{
					body_list.add(str);
					body_str = body_str.concat(" " + str);
				}
			}
		}
	}

	/**
	 * 
	 * @param str
	 *            the word in pair to find then increment it's counter
	 * @return true if found and false in not find and increment the word
	 *         counter else do nothing
	 */
	private boolean findAndIncrement(String str)
	{
		if (str.trim().length() <= 3)
			return true;

		for (int i = 0; i < keyWords.size(); i++)
		{
			if (keyWords.get(i).word.equals(str))
			{
				keyWords.get(i).increment();
				return true;
			}
		}
		return false;
	}

	/**
	 * sort keywords list in descending order
	 */
	private void sortKeyWords()
	{
		int max = 0, indx = 0;
		WordCntPair tmppair;
		for (int i = 0; i < keyWords.size(); i++)
		{
			max = keyWords.get(i).count;
			indx = i;
			for (int k = i + 1; k < keyWords.size(); k++)
			{
				if (keyWords.get(k).count > max)
				{
					max = keyWords.get(k).count;
					indx = k;
				}
			}

			if (indx != i)
			{
				tmppair = keyWords.get(indx);
				keyWords.setElementAt(keyWords.get(i), indx);
				keyWords.setElementAt(tmppair, i);
			}
		}
	}

	/**
	 * fill keywords list with all important tags and title of page
	 */
	private void fillTitleList()
	{
		// get title
		String title = page.title().toLowerCase().trim();

		// get meta description content
		String description = "";
		Elements desc = page.select("meta[name=description]");
		if (desc.size() > 0)
			description = desc.get(0).attr("content");

		// get meta keyword content
		String keywords = "";
		Elements key = page.select("meta[name=keywords]");
		if (key.size() > 0)
			keywords = key.first().attr("content");

		title_str = title + " " + description + " " + keywords;
		title_str = title_str.replaceAll("[^a-zA-Z ]", "");
		title_str = title_str.replaceAll(" +", " ");

		// fill the keywords
		String[] templist;
		templist = title_str.split(" ");

		for (String str : templist)
		{
			// key words counting
			// key words counting
			if (!findAndIncrement(str.toLowerCase()))
			{
				// TODO eliminate all non nouns
				WordCntPair newpair = new WordCntPair(str.toLowerCase(), 0);
				keyWords.add(newpair);
			}
		}
	}

	/**
	 * 
	 * @param url
	 *            the URL to get the parent website from
	 * @return the parent website URL
	 */
	public static String getWebsiteURL(String url)
	{
		int firstslashindx = url.indexOf('/');
		int slashindx = url.indexOf('/', firstslashindx + 2);
		if (slashindx == -1)
			return url;
		else
		{
			return url.substring(0, slashindx);
		}
	}

	/**
	 * Fill all lists
	 */
	public void Crawl()
	{	
		fillLinksList();
		fillBodyList();
		fillTitleList();
		sortKeyWords();
	}

	/*
	 * TEST
	 */
	public void test_print()
	{
		Crawl();

		// page info
		System.out.println("##### Website URL: ");
		System.out.println(getWebsiteURL(page_url));
		System.out.println("##### Page URL: ");
		System.out.println(page_url);

		// title
		System.out.println("##### Title : ");
		System.out.println(title_str);

		// links
		System.out.println("##### Links: ");
		for (String str : links_list)
		{
			System.out.println(str);
		}

		// body
		System.out.println("##### Body: ");
		for (String str : body_list)
		{
			// System.out.println(str);
		}

		// keywords
		System.out.println("##### Keywords: ");
		for (WordCntPair pair : keyWords)
		{
			System.out.println(pair.word + " - " + pair.count);
		}
	}
}

import java.io.IOException;

import contcy.PageCrawler;
import contcy.Spider;

public class Source
{

	public static void main(String[] args) throws IOException
	{
		// test
		System.out.println("Running...");

		Spider myspider = new Spider(10);
		myspider.insertSeed("http://en.wikipedia.org/wiki/Triangulum_Australe");
		myspider.run();

		System.out.println("Finish...");

		// test crawler
		// PageCrawler PC = new PageCrawler();
		// PC.setPage("http://en.wikipedia.org/wiki/Triangulum_Australe");
		// PC.Crawl();
		// PC.test_print();
	}

}

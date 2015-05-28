package utility;

public class WordCntPair
{
	public String word;
	public int count;
	
	public WordCntPair(String wrd, int cnt)
	{
		this.word = wrd;
		this.count = cnt;
	}
	
	public void increment()
	{
		count++;
	}
}

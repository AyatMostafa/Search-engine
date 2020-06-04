package jdbc_demo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
public class cacheFormer {
   public static String forResult;
   public static List<String> cache;
	public cacheFormer() {
		// TODO Auto-generated constructor stub
	}
	
	public static void MakeCache(List<String> urlBag)
	{
		cache = new ArrayList<String>();
		for(int i=0;i<urlBag.size();i++)
		{
			cache.add(urlBag.get(i));
	    }
//		CacheManager cm = CacheManager.getInstance();
//		cm.clearAll();
//		//cm.addCache("URLData");
//		//2. Create a cache called "cache1"
//		Cache cache;
//		if(cm.getCache("URL") != null)
//		{
//			System.out.println(cm.getCache("URL"));
//			cm.addCache("URLData");
//		}
//		cache = cm.getCache("URLData");
//
//		CacheConfiguration config = cache.getCacheConfiguration();
//		config.setTimeToIdleSeconds(888000);
//		config.setTimeToLiveSeconds(888000);
//		config.setMaxEntriesLocalHeap(10000);
//		config.setMaxEntriesLocalDisk(10000);
//		for(int i=0;i<urlBag.size();i++)
//		{
//			String key=Integer.toString(i) ;
//			Element element = new Element(key, urlBag.get(i));
//			cache.put(element);
//		//	cache.replace(element);
////			Element ele = cache.get(key);
////			String output = (ele == null ? null : ele.getObjectValue().toString());
////			System.out.println(output);
//		}
	}
	public static int GetNumberOfURL()
	{
		return cache.size();
	}
	
	
	public static String getCacheData(int pageid)
	{
//		CacheManager singletonManager = CacheManager.getInstance();
//		Cache test = singletonManager.getCache("URLData");
//		Element ele = test.get(Integer.toString(i));
//		String output = (ele == null ? null : ele.getObjectValue().toString());
		forResult = "";
		int total = 10;
		pageid = (pageid-1)*total;   
		int	end = pageid + total;
		for(int i = pageid; i < end; i++ )
		{
		   if(i >= cache.size())
			   break;
		   String output = cache.get(i);
		   URL_Data Obj = URL_Data.getRecords(output);
		   if(Obj != null)
		   {
			   forResult += "<form id= '"+ i + "'> <a type='submit' onclick = \" doSomething(event) \" href= ";
			   forResult += Obj.getURL() + " > " +  Obj.getURL() + " </a> <br> <p> " + Obj.getpartOfContent() + "</p> </form> ";  
				//System.out.println(forResult);
		   }
		}
		return forResult;
	}
	public static String getCacheImage(int pageid)
	{
		forResult = "<div class='row'>";
		int total = 12;
		pageid = (pageid-1)*total;   
		int	end = pageid + total;
		for(int i = pageid; i < end; i++ )
		{
		   if(i >= cache.size())
			   break;
		   String output = cache.get(i);
		   forResult += "<div class='column'> <img style=\"width:100%;height:250px;display:inline-block\" onclick = \" doSomething(event) \" src= " + output + "> </div> ";  
		}
		forResult += "</div>";
		
		return forResult;
		
	}
}
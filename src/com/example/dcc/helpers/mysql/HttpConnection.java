package com.example.dcc.helpers.mysql;

/**
 * This class is used to manage all HTTP connections. It will do this through static methods in order to increase
 * Portability between classes within this app.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dcc.helpers.Friend;
import com.example.dcc.helpers.Links;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.hacks.DCCCookieSpecFactory;
import com.example.dcc.helpers.hacks.DCCCookieStore;
import com.example.dcc.helpers.hacks.DCCRedirectHandler;

public class HttpConnection {

	private static final String HOST = "www.virtualdiscoverycenter.net";
	private static final String REFERER = "http://www.virtualdiscoverycenter.net/intern/";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0";
	private static final String LOG = "Dcc.HttpConnection";

	//private static final String LOG = "Dcc.HttpConnection";

	public static synchronized Document getParseToXML(User user, String page){
		try {

			if(user == null) user = ObjectStorage.getUser();

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(page);

			client.setCookieStore(new DCCCookieStore());
			client.getCookieSpecs().register("easy", getCookieSpec());
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

			HttpResponse response = client.execute(new HttpHost(HOST), get);

			get.setHeader("Cookie", user.cookies);
			get.setHeader("User-Agent", USER_AGENT);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				total.append(line);

			Document doc = Jsoup.parse(total.toString());

			return doc;

		} catch (UnsupportedEncodingException e) {
			Log.e("Requesting Page", e.getLocalizedMessage());
		} catch (ClientProtocolException e) {
			Log.e("Requesting Page", e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e("Requesting Page", e.getLocalizedMessage());
		}

		return null;
	}


	private static synchronized org.w3c.dom.Document getXMLfromURL(User user, String uri){
		try{
			if(user == null) user = ObjectStorage.getUser();


			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(uri);

			client.setCookieStore(new DCCCookieStore());
			client.getCookieSpecs().register("easy", getCookieSpec());
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");


			get.setHeader("Cookie", user.cookies);
			get.setHeader("User-Agent", USER_AGENT);

			Log.e("url", HOST+uri);
			for(Header h : get.getAllHeaders()){
				Log.e("Header" , h.getValue());
			}
			
			HttpResponse response =  client.execute(new HttpHost(HOST), get);
			Log.e("al", response.getStatusLine().getReasonPhrase());
			String xml = EntityUtils.toString(response.getEntity());
			Log.e("al", xml);
			org.w3c.dom.Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();


			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);


			return doc;
		}catch(Exception e){
			e.printStackTrace();
		}


		return null;
	}


	private static synchronized HttpResponse postResponse(User user, String page, List<NameValuePair> nvp, boolean holdFirst){

		try {

			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(page);

			client.setCookieStore(new DCCCookieStore());
			client.getCookieSpecs().register("easy", getCookieSpec());
			client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");

			if (nvp != null)
				post.setEntity(new UrlEncodedFormEntity(nvp));
			post.setHeader(new BasicHeader("Referer", REFERER));
			post.setHeader("User-Agent", USER_AGENT);

			// This will stop redirects if activated
			if (holdFirst)
				client.setRedirectHandler(new DCCRedirectHandler());

			HttpResponse response = client.execute(new HttpHost(HOST), post);

			return response;

		} catch (UnsupportedEncodingException e) {
			Log.e("Requesting Page", e.getLocalizedMessage());
		} catch (ClientProtocolException e) {
			Log.e("Requesting Page", e.getLocalizedMessage());
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Requesting Page", e.getLocalizedMessage());
		}
		Log.e("asdf", "returning null post response");
		return null;
	}

	/**
	 * Authenticates the user and stores cookies.
	 * 
	 * @param log
	 * @param pwd
	 * @return Returns the cookies received from the wordpress site
	 * @throws URISyntaxException
	 */
	public static synchronized boolean login(User user, String log, String pwd) {
		String page = "/wp-login.php";

		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("rememberme", "forever"));
		nvp.add(new BasicNameValuePair("redirect_to", "/intern/"));
		nvp.add(new BasicNameValuePair("log", log));
		nvp.add(new BasicNameValuePair("pwd", pwd));
		nvp.add(new BasicNameValuePair("submit", ""));

		HttpResponse response = postResponse(user, page, nvp, true);

		Header[] cookies = (Header[]) response.getHeaders("Set-Cookie");

		Log.e("length", cookies.length + "");
		StringBuilder sb = new StringBuilder();

		// Add all the cookies to the cookie string
		for (int h = 0; h < cookies.length; h++) {
			Header c = cookies[h];

			if(c.getValue().startsWith("wordpress_test") || c.getValue().startsWith("wordpress_logged")){
				sb.append(c.getValue()+";");
			}
		}

		user.cookies = sb.toString();
		try {
			buildUser(user, response);
			ObjectStorage.setUser(user);
			return true;
		} catch (Exception e) {
			Log.e("Error", e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Takes an input stream and parses all relevant data from the HTML file
	 * into the user object.
	 * 
	 * @param u
	 * @param response
	 * @throws Exception
	 */
	private static synchronized void buildUser(User user, HttpResponse response) {

		try {
			Document doc = getParseToXML(user, "/intern/");
			Element span = doc.getElementById("ffl-logged-in-user");
			user.setName(span.text());
			span = doc.getElementsByClass("username").first();
			user.setHandle(span.text());

			Elements div = doc.getElementById("buddyhead_img").children();
			Element image = div.first();
			String urlS = image.attr("src");

			URL url = new URL(urlS);
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
			user.setImage(bmp);


		}catch(Exception e){
			e.printStackTrace();
			Log.e("Build User", e.getLocalizedMessage());
		}
	}

	/**
	 * Used to gather a list of friends of the user
	 * 
	 * @param cookieStore
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static void getFriends(User u){
		try{
			Document doc = getParseToXML(u, u.getURL(Links.FRIENDS));
			Log.e("added friend", "Adding Friend");
			Element e = doc.getElementById("members-list");
			Elements friends = e.getElementsByTag("li");
			Log.e("added friend", friends.size()+"");

			for(Element friend : friends){
				Friend f = new Friend();

				Element temp = e.getElementsByClass("item-avatar").first();
				Element img = temp.getElementsByTag("a").first().getElementsByTag("img").first();
				f.setImgURL(img.attr("src"));
				f.setName(img.attr("alt").replaceAll("Profile picture of ", ""));	
				f.setPage(friend.getElementsByClass("item-title").get(0).getElementsByTag("a").attr("href"));
				String url = f.getPage().substring(0, f.getPage().length()-1);
				f.setHandle(url.substring(url.lastIndexOf("/")+1));

				u.addFriend(f);
				ObjectStorage.setUser(u);
			}
		}catch(Exception e){
			Log.e("Add Friends", e.getLocalizedMessage());
		}
	}

	/**
	 * Method used to bypass some cookie security features.
	 * 
	 * @return
	 */
	private static CookieSpecFactory getCookieSpec() {
		return new DCCCookieSpecFactory();
	}


	public static synchronized List<News> getNews(){
		List<News> newsPaper = new ArrayList<News>();

		try{
			org.w3c.dom.Document doc = getXMLfromURL(ObjectStorage.getUser(), "/news/feed/");
			NodeList nodeList = doc.getElementsByTagName("item");

			for(int i=0; i < nodeList.getLength(); i++){
				
				try{
					
					NodeList n = nodeList.item(i).getChildNodes();
					News news = new News();
					
					news.setTitle(n.item(1).getTextContent());
					news.setLink(n.item(3).getTextContent());
					news.setPubdate(n.item(7).getTextContent());
					news.setPublisher(n.item(9).getTextContent());
					news.setCategory(n.item(11).getTextContent());
					news.setText(n.item(13).getTextContent());

					news.logNews();
					newsPaper.add(news);
					
				}catch(Exception e){
					Log.e("adding loop", e.getLocalizedMessage());
				}
			}
			return newsPaper;
		}catch(Exception e){
			Log.e("something", e.getLocalizedMessage());
		}
		return newsPaper;
	}
}


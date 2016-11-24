package org.xmlrpc.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.boonex.oo.Main;
import com.boonex.oo.R;

import android.util.Xml;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * XMLRPCClient allows to call remote XMLRPC method.
 * 
 *  * <p>
 * The following table shows how XML-RPC types are mapped to java call parameters/response values.
 * </p>
 * 
 * <p>
 * <table border="2" align="center" cellpadding="5">
 * <thead><tr><th>XML-RPC Type</th><th>Call Parameters</th><th>Call Response</th></tr></thead>
 * 
 * <tbody>
 * <td>int, i4</td><td>byte<br />Byte<br />short<br />Short<br />int<br />Integer</td><td>int<br />Integer</td>
 * </tr>
 * <tr>
 * <td>i8</td><td>long<br />Long</td><td>long<br />Long</td>
 * </tr>
 * <tr>
 * <td>double</td><td>float<br />Float<br />double<br />Double</td><td>double<br />Double</td>
 * </tr>
 * <tr>
 * <td>string</td><td>String</td><td>String</td>
 * </tr>
 * <tr>
 * <td>boolean</td><td>boolean<br />Boolean</td><td>boolean<br />Boolean</td>
 * </tr>
 * <tr>
 * <td>dateTime.iso8601</td><td>java.util.Date<br />java.util.Calendar</td><td>java.util.Date</td>
 * </tr>
 * <tr>
 * <td>base64</td><td>byte[]</td><td>byte[]</td>
 * </tr>
 * <tr>
 * <td>array</td><td>java.util.List&lt;Object&gt;<br />Object[]</td><td>Object[]</td>
 * </tr>
 * <tr>
 * <td>struct</td><td>java.util.Map&lt;String, Object&gt;</td><td>java.util.Map&lt;String, Object&gt;</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 */

/**
 * http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872
 * http://blog.crazybob.org/2010/02/android-trusting-ssl-certificates.html
 * Allows you to trust certificates from additional KeyStores in addition to
 * the default KeyStore
 */
class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {
	protected SSLContext sslContext = SSLContext.getInstance("TLS");

	public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
		super(null, null, null, null, null, null);
		sslContext.init(null, new TrustManager[]{new AdditionalKeyStoresTrustManager(keyStore)}, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
		return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}



	/**
	 * Based on http://download.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#X509TrustManager
	 */
	public static class AdditionalKeyStoresTrustManager implements X509TrustManager {

		protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();


		protected AdditionalKeyStoresTrustManager(KeyStore... additionalkeyStores) {
			final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();

			try {
				// The default Trustmanager with default keystore
				final TrustManagerFactory original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				original.init((KeyStore) null);
				factories.add(original);

				for( KeyStore keyStore : additionalkeyStores ) {
					final TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					additionalCerts.init(keyStore);
					factories.add(additionalCerts);
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}



            /*
             * Iterate over the returned trustmanagers, and hold on
             * to any that are X509TrustManagers
             */
			for (TrustManagerFactory tmf : factories)
				for( TrustManager tm : tmf.getTrustManagers() )
					if (tm instanceof X509TrustManager)
						x509TrustManagers.add( (X509TrustManager)tm );


			if( x509TrustManagers.size()==0 )
				throw new RuntimeException("Couldn't find any X509TrustManagers");

		}

		/*
         * Delegate to the default trust manager.
         */
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
			defaultX509TrustManager.checkClientTrusted(chain, authType);
		}

		/*
         * Loop over the trustmanagers until we find one that accepts our server
         */
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			for( X509TrustManager tm : x509TrustManagers ) {
				try {
					tm.checkServerTrusted(chain,authType);
					return;
				} catch( CertificateException e ) {
					// ignore
				}
			}
			throw new CertificateException();
		}

		public X509Certificate[] getAcceptedIssuers() {
			final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
			for( X509TrustManager tm : x509TrustManagers )
				list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
			return list.toArray(new X509Certificate[list.size()]);
		}
	}

}

public class XMLRPCClient {	
	private static final String TAG_METHOD_CALL = "methodCall";
	private static final String TAG_METHOD_NAME = "methodName";
	private static final String TAG_METHOD_RESPONSE = "methodResponse";
	private static final String TAG_PARAMS = "params";
	private static final String TAG_PARAM = "param";
	private static final String TAG_FAULT = "fault";
	private static final String TAG_FAULT_CODE = "faultCode";
	private static final String TAG_FAULT_STRING = "faultString";

	private HttpClient client;
	private HttpPost postMethod;
	private XmlSerializer serializer;

	/**
	 * XMLRPCClient constructor. Creates new instance based on server URI
	 * @param XMLRPC server URI
	 */
	public XMLRPCClient(URI uri) {
		postMethod = new HttpPost(uri);
		postMethod.addHeader("Content-Type", "text/xml");
		
		// WARNING
		// I had to disable "Expect: 100-Continue" header since I had 
		// two second delay between sending http POST request and POST body 
		HttpParams params = postMethod.getParams();
		HttpProtocolParams.setUseExpectContinue(params, false);		
				
		client = this.createHttpClient();
		HttpParams paramsClient = client.getParams();
		HttpClientParams.setRedirecting(paramsClient, false); // manage redirects manually
		
		serializer = Xml.newSerializer();
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = Main.MainActivity.getResources().openRawResource(R.raw.letsencrypt); // http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872
			try {
				trusted.load(in, "ez24get".toCharArray());
			} finally {
				in.close();
			}
			return new AdditionalKeyStoresSSLSocketFactory(trusted);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private HttpClient createHttpClient()
	{
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSslSocketFactory(), 443));

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, registry);

		return new DefaultHttpClient(conMgr, params);
	}

	/**
	 * Convenience constructor. Creates new instance based on server String address
	 * @param XMLRPC server address
	 */
	public XMLRPCClient(String url) {
		this(URI.create(url));
	}
	
	/**
	 * Convenience XMLRPCClient constructor. Creates new instance based on server URL
	 * @param XMLRPC server URL
	 */
	public XMLRPCClient(URL url) {
		this(URI.create(url.toExternalForm()));
	}

	/**
	 * Call method with optional parameters. This is general method.
	 * If you want to call your method with 0-8 parameters, you can use more
	 * convenience call methods
	 * 
	 * @param method name of method to call
	 * @param params parameters to pass to method (may be null if method has no parameters)
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object[] params) throws Exception {
		return callXMLRPC(method, params);
	}
	
	/**
	 * Convenience method call with no parameters
	 * 
	 * @param method name of method to call
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method) throws Exception {
		return callXMLRPC(method, null);
	}
	
	/**
	 * Convenience method call with one parameter
	 * 
	 * @param method name of method to call
	 * @param p0 method's parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0) throws Exception {
		Object[] params = {
			p0,
		};
		return callXMLRPC(method, params);
	}
	
	/**
	 * Convenience method call with two parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1) throws Exception {
		Object[] params = {
			p0, p1,
		};
		return callXMLRPC(method, params);
	}
	
	/**
	 * Convenience method call with three parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2) throws Exception {
		Object[] params = {
			p0, p1, p2,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Convenience method call with four parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3) throws Exception {
		Object[] params = {
			p0, p1, p2, p3,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Convenience method call with five parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4) throws Exception {
		Object[] params = {
			p0, p1, p2, p3, p4,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Convenience method call with six parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) throws Exception {
		Object[] params = {
			p0, p1, p2, p3, p4, p5,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Convenience method call with seven parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @param p6 method's 7th parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) throws Exception {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Convenience method call with eight parameters
	 * 
	 * @param method name of method to call
	 * @param p0 method's 1st parameter
	 * @param p1 method's 2nd parameter
	 * @param p2 method's 3rd parameter
	 * @param p3 method's 4th parameter
	 * @param p4 method's 5th parameter
	 * @param p5 method's 6th parameter
	 * @param p6 method's 7th parameter
	 * @param p7 method's 8th parameter
	 * @return deserialized method return value
	 * @throws Exception
	 */
	public Object call(String method, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) throws Exception {
		Object[] params = {
			p0, p1, p2, p3, p4, p5, p6, p7,
		};
		return callXMLRPC(method, params);
	}

	/**
	 * Call method with optional parameters
	 * 
	 * @param method name of method to call
	 * @param params parameters to pass to method (may be null if method has no parameters)
	 * @return deserialized method return value
	 * @throws Exception
	 */
	private Object callXMLRPC(String method, Object[] params) throws Exception {
		    	
		// prepare POST body
		StringWriter bodyWriter = new StringWriter();
		serializer.setOutput(bodyWriter);
		serializer.startDocument(null, null);
		serializer.startTag(null, TAG_METHOD_CALL);
		// set method name
		serializer.startTag(null, TAG_METHOD_NAME).text(method).endTag(null, TAG_METHOD_NAME);
		if (params != null && params.length != 0) {
			// set method params
			serializer.startTag(null, TAG_PARAMS);
			for (int i=0; i<params.length; i++) {
				serializer.startTag(null, TAG_PARAM).startTag(null, XMLRPCSerializer.TAG_VALUE);
				XMLRPCSerializer.serialize(serializer, params[i]);				
				serializer.endTag(null, XMLRPCSerializer.TAG_VALUE).endTag(null, TAG_PARAM);
			}
			serializer.endTag(null, TAG_PARAMS);
		}
		serializer.endTag(null, TAG_METHOD_CALL);
		serializer.endDocument();

		// set POST body
		HttpEntity entity = new StringEntity(bodyWriter.toString());
		postMethod.setEntity(entity);
		
		// add cookies for logged in user
		String sCookie = Main.getCookieForLoggedInUser();
		if (sCookie != null)
			postMethod.addHeader("Cookie", sCookie);
		
		// execute HTTP POST request				
		HttpResponse response = client.execute(postMethod);
		
		// check status code
		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
						
			Header h = response.getFirstHeader("Location");			
			throw new XMLRPCRedirectException (h.getValue());
			
		} else if (statusCode != HttpStatus.SC_OK) {
			
			throw new Exception("HTTP status code: " + statusCode + " != " + HttpStatus.SC_OK);
			
		}
		
		
		// parse response stuff
		//
		// setup pull parser
		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
		entity = response.getEntity();
		Reader reader = new InputStreamReader(entity.getContent());		
		pullParser.setInput(reader);
		
		// lets start pulling...
		pullParser.nextTag();
		pullParser.require(XmlPullParser.START_TAG, null, TAG_METHOD_RESPONSE);
		
		pullParser.nextTag(); // either TAG_PARAMS (<params>) or TAG_FAULT (<fault>)  
		String tag = pullParser.getName();
		if (tag.equals(TAG_PARAMS)) {
			// normal response
			pullParser.nextTag(); // TAG_PARAM (<param>)
			pullParser.require(XmlPullParser.START_TAG, null, TAG_PARAM);
			pullParser.nextTag(); // TAG_VALUE (<value>)
			// no parser.require() here since its called in XMLRPCSerializer.deserialize() below
			
			// deserialize result
			Object obj = XMLRPCSerializer.deserialize(pullParser);
			entity.consumeContent();
			return obj;
		} else
		if (tag.equals(TAG_FAULT)) {
			// fault response
			pullParser.nextTag(); // TAG_VALUE (<value>)
			// no parser.require() here since its called in XMLRPCSerializer.deserialize() below

			// deserialize fault result
			Map<String, Object> map = (Map<String, Object>) XMLRPCSerializer.deserialize(pullParser);
			String faultString = (String) map.get(TAG_FAULT_STRING);
			int faultCode = (Integer) map.get(TAG_FAULT_CODE);
			entity.consumeContent();
			throw new Exception(faultString + " [code " + faultCode + "]");
		} else {
			entity.consumeContent();
			throw new Exception("bad tag " + tag + " in response");
		}

	}
}

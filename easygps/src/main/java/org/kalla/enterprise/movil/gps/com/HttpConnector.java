package org.kalla.enterprise.movil.gps.com;

public class HttpConnector {

    String str;
    static final int READ_TIME = 10000;
    static final int CONNECT_TIME = 15000;

    public static String sendJsonPOST(String url, String parameters) throws Exception {
//		HttpClient client = new DefaultHttpClient();
//		HttpPost request = new HttpPost(url);
//
//		StringEntity s = new StringEntity(parameters);
//		s.setContentEncoding("UTF-8");
//        s.setContentType("application/json");
//		request.setEntity(s);
//		request.setHeader("Content-type", "application/json");
//
//		HttpResponse response = client.execute(request);

        return null;// processServerResponse(response);
    }
    public static String processServerResponse() throws Exception {
        //public static String processServerResponse(HttpResponse response) throws Exception {
		/*InputStreamReader in = null;

		in = new InputStreamReader(response.getEntity().getContent(), "ISO-8859-1");

		ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1)
			bStrm.write(ch);
		String str = new String(bStrm.toByteArray());
		bStrm.close();

		in.close();
		if (response.getStatusLine().getStatusCode()!=200)
			throw new HttpResponseException(response.getStatusLine().getStatusCode(), str);*/
        return null;// str;
    }

}
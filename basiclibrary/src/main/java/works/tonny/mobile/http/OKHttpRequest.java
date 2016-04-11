package works.tonny.mobile.http;

import android.util.Base64;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import works.tonny.mobile.Application;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;

public class OKHttpRequest extends AbstractHttpRequest {

    private static OkHttpClient cookieLessClient = new OkHttpClient();
    private static OkHttpClient cookieNessClient = new OkHttpClient();
    private static String USER_AGENT = "Mozilla/5.0 (Android " + android.os.Build.VERSION.RELEASE + ") " + android.os.Build.MODEL + " OKHttpCient/2.4.0";

    static {
        // 初始化cookie管理的连接
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        cookieNessClient.setCookieHandler(cookieManager);
        cookieLessClient.setConnectTimeout(10, TimeUnit.SECONDS);
        cookieLessClient.setWriteTimeout(100, TimeUnit.SECONDS);
        cookieLessClient.setReadTimeout(300, TimeUnit.SECONDS);

        cookieNessClient.setConnectTimeout(10, TimeUnit.SECONDS);
        cookieNessClient.setWriteTimeout(100, TimeUnit.SECONDS);
        cookieNessClient.setReadTimeout(300, TimeUnit.SECONDS);
    }

    private OkHttpClient client;

    private Response response;

    private Map<String, List<String>> headers;

    private Map<String, List<String>> formParams;

    private Map<String, File> files;

    private Method method;

    private String url;

    private String requestContentType;

    private int responseCode;
    private Call call;

    public OKHttpRequest(Method method, String url) {
        super();
        this.method = method;
        client = cookieLessClient;
        this.url = url;
        setHeader("User-Agent", USER_AGENT);
    }

    /**
     * 使用CookieManager管理Cookie
     *
     * @return
     */
    @Override
    public HttpRequest useCookieManager() {
        client = cookieNessClient;
        return this;
    }

    /**
     * 添加头信息
     *
     * @param name
     * @param value
     * @return
     */
    @Override
    public HttpRequest addHeader(String name, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<String, List<String>>();
        }
        if (!headers.containsKey(name)) {
            headers.put(name, new ArrayList<String>());
        }
        headers.get(name).add(value);
        return this;
    }

    /**
     * 设置头信息，以前的清除
     *
     * @param name
     * @param value
     * @return
     */
    @Override
    public HttpRequest setHeader(String name, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<String, List<String>>();
        }

        if (!headers.containsKey(name)) {
            headers.put(name, new ArrayList<String>());
        } else {
            headers.get(name).clear();
        }
        headers.get(name).add(value);
        return this;
    }

    /**
     * 添加表单数据
     *
     * @param name
     * @param value
     * @return
     */
    @Override
    public HttpRequest addFormParam(String name, String value) {
        if (formParams == null) {
            formParams = new LinkedHashMap<String, List<String>>();
        }
        if (!formParams.containsKey(name)) {
            formParams.put(name, new ArrayList<String>());
        }
        Log.info(name + " " + formParams.get(name));
        formParams.get(name).add(value);
        return this;
    }

    /**
     * 设置表单数据，清除已添加的数据
     *
     * @param name
     * @param value
     * @return
     */
    @Override
    public HttpRequest setFormParam(String name, String value) {
        if (formParams == null) {
            formParams = new LinkedHashMap<String, List<String>>();
        }
        if (!formParams.containsKey(name)) {
            formParams.put(name, new ArrayList<String>());
        } else {
            formParams.clear();
        }
        formParams.get(name).add(value);
        Log.info(name + " " + formParams.get(name));
        return this;
    }

    /**
     * 添加附件上传
     *
     * @param file
     */
    @Override
    public HttpRequest addFile(String name, File file) {
        if (files == null) {
            files = new LinkedHashMap<String, File>();
        }
        files.put(name, file);
        return this;
    }

    /**
     * 初始化请求
     *
     * @return
     */
    private Request.Builder init() {
        Request.Builder builder = new Request.Builder();
        // 添加头信息
        if (headers != null) {
            for (String name : headers.keySet()) {
                List<String> header = headers.get(name);
                for (String h : header) {
                    builder.addHeader(name, h);
                }
            }
            if (Application.getUser() != null) {
                String value = "Basic " + Base64.encodeToString((Application.getUser().getUsername() + ":" + Application.getUser().getPassword()).getBytes(), Base64.NO_WRAP);
                Log.info(value);
                builder.addHeader("Authorization", value);
            }
        }
        return builder;
    }

    /**
     * get
     *
     * @throws IOException
     */
    private void get() throws IOException {
        Request.Builder builder = init();
        Request request = builder.url(url).build();
        call = client.newCall(request);
        response = call.execute();
    }

    /**
     * post
     *
     * @throws IOException
     */
    private void post() throws IOException {
        Request.Builder builder = init();
        RequestBody body = null;
        if (files == null && formParams != null) {
            FormEncodingBuilder bd = new FormEncodingBuilder();
            for (String name : formParams.keySet()) {
                for (String value : formParams.get(name)) {
                    bd.addEncoded(name, value == null ? "" : value);
                }
            }
            body = bd.build();
        } else if (files != null) {
            MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
            if (formParams != null) {
                for (String name : formParams.keySet()) {
                    for (String value : formParams.get(name)) {
//                        multipartBuilder.addFormDataPart(name, value);
                        multipartBuilder.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"" + name + "\""),
                                RequestBody.create(null, value));
//                        RequestBody.cre
                    }
                }
            }

            for (String name : files.keySet()) {
                File file = files.get(name);
//                multipartBuilder.addPart(RequestBody.create(MediaType.parse(requestContentType == null ? "application/unknown" : requestContentType), file));
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\""),
                        RequestBody.create(MediaType.parse(requestContentType == null ? "application/unknown" : requestContentType), file));
                Log.info(file.getAbsolutePath() + " " + file.length());
            }
            body = multipartBuilder.build();
        }

        // RequestBody body =
        // RequestBody.create(MediaType.parse(requestContentType), "hello");
        Request request = builder.url(url).post(body).build();
        call = client.newCall(request);
        response = call.execute();
    }

    /**
     * 取消访问
     */
    @Override
    public void cancel() {
        if (call != null)
            call.cancel();
    }

    private void doExecute() throws IOException, AuthException {
        Log.info(method + " : " + url);
        if (method == Method.Get) {
            get();
        } else {
            post();
        }
        responseCode = response.code();
        if (responseCode == 401) {
            throw new AuthException();
        }
    }

    /**
     * 无返回请求
     *
     * @throws IOException
     */
    public void execute() throws HttpRequestException, AuthException {
        try {
            doExecute();
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpRequestException(e);
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求返回文本内容
     *
     * @return
     * @throws HttpRequestException
     */
    @Override
    public String executeToString() throws HttpRequestException, AuthException {
        try {
            doExecute();
            return response.body().string();
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {

            throw new HttpRequestException(e);
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (IOException ie) {
                    throw new HttpRequestException(ie);
                }
            }
        }
    }

    /**
     * 请求并写到文件
     *
     * @param file
     * @throws HttpRequestException
     */
    @Override
    public void executeToFile(File file) throws HttpRequestException, AuthException {
        FileOutputStream outputStream = null;
        try {
            doExecute();
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            outputStream = new FileOutputStream(file);
            IOUtils.copy(response.body().byteStream(), outputStream);
            outputStream.flush();
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {

            throw new HttpRequestException(e);
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (IOException ie) {
                    throw new HttpRequestException(ie);
                }
            }
            IOUtils.close(outputStream);
        }

    }

    /**
     * 请求并输出到流
     *
     * @param targetOutputStream
     * @throws IOException
     */
    @Override
    public void executeToOutputStream(OutputStream targetOutputStream) throws HttpRequestException, AuthException {
        try {
            doExecute();

            IOUtils.copy(response.body().byteStream(), targetOutputStream);
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {

            throw new HttpRequestException(e);
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (IOException ie) {
                    throw new HttpRequestException(ie);
                }
            }
        }
    }

    @Override
    public byte[] executeToBytes() throws HttpRequestException, AuthException {
        try {
            doExecute();
            return response.body().bytes();
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {

            throw new HttpRequestException(e);
        } finally {
            if (response != null) {
                try {
                    response.body().close();
                } catch (IOException ie) {
                    throw new HttpRequestException(ie);
                }
            }
        }
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

}

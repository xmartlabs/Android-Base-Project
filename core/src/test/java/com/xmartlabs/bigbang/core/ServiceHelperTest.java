package com.xmartlabs.bigbang.core;

import com.xmartlabs.bigbang.core.helper.ServiceHelper;

import org.junit.Before;
import org.junit.Test;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ServiceHelperTest {
  private static final String REQUEST_URL = "https://github.com/xmartlabs/bigbang";

  private Response response;

  @Before
  public void setUp() {
    Request request = new Request.Builder()
        .url(REQUEST_URL)
        .build();
    response = new Response.Builder()
        .code(200).message("OK")
        .protocol(Protocol.HTTP_1_1)
        .request(request)
        .build();
  }

  @Test
  public void getUrl() {
    assertThat(ServiceHelper.INSTANCE.getUrl(response), equalTo(REQUEST_URL));
  }

  @Test
  public void addParameterNameToEndOfUrl() {
    String url = ServiceHelper.INSTANCE.addParameterNameToEndOfUrl(REQUEST_URL, "test");
    url = ServiceHelper.INSTANCE.addParameterNameToEndOfUrl(url, "test2");

    String expectedResult = REQUEST_URL + "/{test}/{test2}";

    assertThat(url, equalTo(expectedResult));
  }

  @Test
  public void addParameterToUrl() {
    String url = ServiceHelper.INSTANCE.addParameterNameToEndOfUrl(REQUEST_URL, "test");
    url = ServiceHelper.INSTANCE.addParameterNameToEndOfUrl(url, "test2");

    String parametrizedUrl = ServiceHelper.INSTANCE.addParameterToUrl(url, "test", "replacedTest");
    parametrizedUrl = ServiceHelper.INSTANCE.addParameterToUrl(parametrizedUrl, "test2", "replacedTest2");

    String expectedResult = REQUEST_URL + "/replacedTest/replacedTest2";
    assertThat(parametrizedUrl, equalTo(expectedResult));
  }
}

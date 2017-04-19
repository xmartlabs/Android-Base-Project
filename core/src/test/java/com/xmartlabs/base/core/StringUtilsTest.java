package com.xmartlabs.base.core;

import com.xmartlabs.base.core.helper.StringUtils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringUtilsTest {
  @Test
  public void nullString() {
    assertThat(StringUtils.isNullOrEmpty(null), is(true));
  }

  @Test
  public void emptyString() {
    assertThat(StringUtils.isNullOrEmpty(""), is(true));
  }

  @Test
  public void length3String() {
    assertThat(StringUtils.isNullOrEmpty("abc"), is(false));
  }

  @Test
  public void capitalizeEmptyString() {
    assertThat(StringUtils.capitalizeFirstChar(""), equalTo(""));
  }

  @Test
  public void capitalizeFirstLetter() {
    assertThat(StringUtils.capitalizeFirstChar("capitalize first letter"), equalTo("Capitalize first letter"));
  }
}

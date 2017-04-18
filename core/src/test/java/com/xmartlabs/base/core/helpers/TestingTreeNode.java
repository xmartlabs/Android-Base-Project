package com.xmartlabs.base.core.helpers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestingTreeNode {
  int priority;
  String tag;
  String message;
  Throwable throwable;
}

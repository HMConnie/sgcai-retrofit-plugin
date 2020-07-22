package com.sgcai.andoid.retrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Service {
    public String requestPath;
    public String responsePath;
    public String observablePath;
    public String observableClassName;
    public String baseParamClass;
    public String baseBeanClass;
    public List<ObservableBean> observables;


    public static class ObservableBean {
        public String requestMethod;
        public String url;
        public String requestJson;
        public String responseJson;

        private void checkUrl() {
            if (url == null || "".equals(url)) {
                throw new RuntimeException("url不能为空");
            }
        }

        private String urlClassName() {
            checkUrl();
            String urlClassName = "";
            String[] split = url.split("/");
            List<String> list = new ArrayList<>(Arrays.asList(split));
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String splitUrl = iterator.next();
                if (splitUrl == null || "".equals(splitUrl)) {
                    iterator.remove();
                }
            }
            for (int i = 0; i < list.size(); i++) {
                String splitUrl = list.get(i);
                if (i > 0) {
                    String first = splitUrl.substring(0, 1);
                    String newFirst = first.toUpperCase();
                    splitUrl = splitUrl.replaceFirst(first, newFirst);
                }
                urlClassName += splitUrl;
            }
            return urlClassName;
        }

        public String methodName() {
            return urlClassName();
        }

        public String requestClassName() {
            String requestClassName = urlClassName();
            String first = requestClassName.substring(0, 1);
            String newFirst = first.toUpperCase();
            return requestClassName.replaceFirst(first, newFirst) + "Param";
        }

        public String responseClassName() {
            String responseClassName = urlClassName();
            String first = responseClassName.substring(0, 1);
            String newFirst = first.toUpperCase();
            return responseClassName.replaceFirst(first, newFirst) + "Bean";
        }


    }

}

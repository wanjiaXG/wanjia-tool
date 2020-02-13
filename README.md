## 个人小工具类
WebClient 基于okhttp3乱写的类  
用法
```java
//GET请求
        WebClient client = WebClient.getInstance();
        String get = client.load("...").open().body();
        System.out.println(get);

        //POST请求
        String post = client.load("...")
                .addPost("key1", "value1")
                .addPost("key2", "value2")
                .addHeader("Head1", "value1").open().body();
        System.out.println(post);

        //上传文件
        String upload = client.load("...")
                .addFile("myFile","C:\\test.txt")
                .open().body();
        System.out.println(upload);
```

## 微信APP支付body乱码问题 --排坑日记(1)

~~~
Content: 涉及到的工具类：

1. 微信开发文档： https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
2. HttpUtil : 微信H5w网页支付http请求工具类
3. WxPayConfig : 微信支付商户配置类
4. MaryunHttpUtils : 微信APP支付http请求工具类
5. OrderGeneratedUtils :  微信订单生成工具类。
6. WxPayUtils : 微信自带的工具类(包括 随机字符串、xml与map数据转换、MD5加密、获取时间戳(秒、毫秒))
7. WxRamdomNumberUtils : 微信订单总金额格式化
8. XiaomiGoodsInfoUtil : XML文档读取工具类。
~~~
	
一、场景重现

我们的业务场景为从xml文档中加载商品信息，如下图。

商品名 body 即为文档中的notice字段的值。具体实例为下图中的片段

异常情况为：

    一、body未转换编码时
    二、body进行编码转换时


二、解决思路

就编码问题而言,有俩种解决办法：

    一、body = new String(body.getBytes("ISO-8859-1"),"UTF-8"); 通过字节码转换编码格式
    二、RequestEntity requestEntity = new StringRequestEntity(argJson,"application/json","UTF-8"); HttpClient在在字节流读取流的时候转换编码。 

三、解决方案

1. 对body未单独进行编码转换，而是在向httpClient发送post请求时对字节流进行编码强转。
2. 添加一个请求头 theaderList.add(new UHeader("Content-Type", "application/x-www-form-urlencoded"));  
3. 由于之前使用的H5网页支付的HttpUtil工具类，所以bug重现不止，后来改用 MaryunHttpUtils。

四、[源码地址](https://github.com/noseparte/paymentServer)
	     


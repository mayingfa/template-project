## 项目介绍

SpringBoot整合Elasticsearch

> SpringBoot 2.3.7.RELEASE

> Elasticsearch 7.14.0 版本

项目使用 ElasticsearchRepository 和 ElasticsearchRestTemplate 两种方式操作ES完成以下功能

- CRUD基本操作
- 批量添加/删除数据
- 分页查询商品
- 排序查询商品
- 根据商品名检索
- 关键词高亮搜索
- 自定义高级搜索
- Knife4j生成Api文档

项目启动后，访问控制台输出的Knife4j接口文档地址，通过Swagger接口文档预览接口信息

> 相关文章推荐

- https://www.jianshu.com/p/bd2da1cde6f5
- https://www.jianshu.com/p/a5322dc5ae7f
- https://blog.csdn.net/u013089490/article/details/84323903
- https://blog.csdn.net/wwwzhouzy/article/details/118638946
- https://blog.csdn.net/numbbe/article/details/109826032

## Elasticsearch 相关

elasticsearch下载地址：https://www.elastic.co/cn/downloads/elasticsearch

Kibana下载地址：https://www.elastic.co/cn/downloads/kibana

ik分词器下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases

elasticsearch-head插件下载地址：https://github.com/mobz/elasticsearch-head

文章推荐：https://blog.csdn.net/qq_26966709/article/details/119522717

```bash
PUT /mall_product/
{
    "mappings": {
    	"properties": {
    		"name": {
    			"type": "text",
    			"analyzer":"ik_max_word"
    		},
    		"type": {
    			"type": "keyword"
    		},
    		"brand": {
    			"type": "keyword"
    		},
    		"price": {
    			"type": "double"
    		},
    		"sale_time": {
    			"type": "date",
    			"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
    		},
    		"url": {
    			"type": "keyword"
    		}
    	}	
    }
}

GET /mall_product/

POST /mall_product/_search  

DELETE /mall_product
```

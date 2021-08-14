package top.qiudb.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.qiudb.pojo.Product;

import java.util.List;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/10 20:33
 * @description Elasticsearch 持久层
 */

public interface ProductRepository extends ElasticsearchRepository<Product, Long> {
    List<Product> findByNameLike(String keyword);
}

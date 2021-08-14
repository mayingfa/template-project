package top.qiudb.service;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.qiudb.dao.ProductRepository;
import top.qiudb.pojo.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/10 20:38
 * @description 业务层实现类
 */

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    private static final String GOODS_EXIST = "商品已存在";
    private static final String GOODS_NO_EXIST = "商品不存在";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_SALE_TIME = "saleTime";


    /**
     * 根据商品Id判断商品是否存在
     *
     * @param id 商品Id
     * @return 布尔值 (存在（true）/ 不存在（false）)
     */
    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * @param id 商品Id
     * @return Product 商品信息
     */
    @Override
    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    /**
     * 查询所有商品
     *
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> findAll() {
        return getProductList(productRepository.findAll());
    }

    /**
     * 根据商品名检索
     *
     * @param keyword 关键词
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> findByNameLike(String keyword) {
        return productRepository.findByNameLike(keyword);
    }

    /**
     * 分页查询
     *
     * @param pageNum  当前页
     * @param pageSize 每页大小
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> findAll(int pageNum, int pageSize) {
        if (pageNum - 1 < 0) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        // 构造分页类
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return getProductList(productRepository.findAll(pageable));
    }

    /**
     * 排序查询
     *
     * @param field 排序字段
     * @param asc   升序（true）/ 降序（false）
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> findAll(String field, boolean asc) {
        // 构造排序类
        Sort sort = Sort.by(Sort.Direction.DESC, field);
        if (asc) {
            sort = Sort.by(Sort.Direction.ASC, field);
        }
        return getProductList(productRepository.findAll(sort));
    }

    /**
     * 关键词高亮搜索
     *
     * @param keyword 关键词
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> highLightSearch(String keyword) {
        // 构建 BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 查询条件
        if (!StringUtils.isEmpty(keyword)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(FIELD_NAME, keyword));
            boolQueryBuilder.should(QueryBuilders.matchQuery(FIELD_BRAND, keyword));
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(
                        new Field(FIELD_NAME).preTags("<em style=\"color:red;\">")
                                .postTags("</em>").fragmentSize(100),
                        new Field(FIELD_BRAND).preTags("<em style=\"color:red;\">")
                                .postTags("</em>").fragmentSize(100))
                .build();
        SearchHits<Product> search = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
        // 保存返回结果
        List<Product> productList = new ArrayList<>();
        if (search.getTotalHits() > 0) {
            // 获取查询结果高亮字段集合
            List<SearchHit<Product>> searchHits = search.getSearchHits();
            // 遍历返回的内容进行处理
            searchHits.forEach(searchHit -> {
                Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
                if (highlightFields.get(FIELD_NAME) != null) {
                    searchHit.getContent().setName(highlightFields.get(FIELD_NAME).get(0));
                }
                if (highlightFields.get(FIELD_BRAND) != null) {
                    searchHit.getContent().setBrand(highlightFields.get(FIELD_BRAND).get(0));
                }
            });
            search.getSearchHits().forEach(productSearchHit ->
                    productList.add(productSearchHit.getContent()));
        }
        return productList;
    }

    /**
     * 高级检索
     *
     * @param name      名称
     * @param type      分类
     * @param brand     品牌
     * @param startTime 开始时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param endTime   结束时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param priceDesc 价格排序(降序（true）、升序（false）)
     * @param minPrice  最低价
     * @param maxPrice  最高价
     * @param pageNum   当前页，从0开始
     * @param pageSize  每页大小
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> advancedSearch(String name, String type, String brand, String startTime, String endTime,
                                        boolean priceDesc, double minPrice, double maxPrice, int pageNum, int pageSize) throws ParseException {
        // 校验参数
        if (pageNum - 1 < 0) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        // 构造分页对象
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // BoolQueryBuilder (Elasticsearch Query)
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(name)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(FIELD_NAME, name));
        }

        if (!StringUtils.isEmpty(type)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(FIELD_TYPE, type));
        }

        if (!StringUtils.isEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(FIELD_BRAND, brand));
        }

        if (!StringUtils.isEmpty(minPrice)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(FIELD_PRICE).gte(minPrice));
        }

        if (!StringUtils.isEmpty(maxPrice)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(FIELD_PRICE).lte(maxPrice));
        }

        // 解析日期时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isEmpty(startTime)) {
            Date startDate = format.parse(startTime);
            boolQueryBuilder.must(QueryBuilders.rangeQuery(FIELD_SALE_TIME).gte(startDate.getTime()));
        }

        if (!StringUtils.isEmpty(endTime)) {
            Date endDate = format.parse(endTime);
            boolQueryBuilder.must(QueryBuilders.rangeQuery(FIELD_SALE_TIME).lte(endDate.getTime()));
        }

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                // 先价格排序，再上架时间降序排序
                .withSort(SortBuilders.fieldSort(FIELD_PRICE).order(priceDesc ? SortOrder.DESC : SortOrder.ASC))
                .withSort(SortBuilders.fieldSort(FIELD_SALE_TIME).order(SortOrder.DESC))
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();

        SearchHits<Product> search = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
        List<Product> productList = new ArrayList<>();
        if (search.getTotalHits() > 0) {
            search.getSearchHits().forEach(productSearchHit ->
                    productList.add(productSearchHit.getContent()));
        }
        return productList;
    }

    /**
     * 保存或更新商品
     *
     * @param product 商品信息
     * @param insert  插入标识 插入（true）/ 更新（false）
     */
    @Override
    public void save(Product product, boolean insert) throws RuntimeException {
        if (insert) {
            if (this.existsById(product.getId())) {
                throw new RuntimeException(GOODS_EXIST);
            }
            product.setSaleTime(new Date());
        } else {
            if (!this.existsById(product.getId())) {
                throw new RuntimeException(GOODS_NO_EXIST);
            }
        }
        productRepository.save(product);
    }

    /**
     * 批量插入
     *
     * @param products 商品列表
     */
    @Override
    public void saveAll(List<Product> products) {
        products.forEach(product -> product.setSaleTime(new Date()));
        productRepository.saveAll(products);
    }

    /**
     * 根据商品Id删除数据
     *
     * @param id 商品Id
     */
    @Override
    public void deleteById(Long id) throws RuntimeException {
        if (!this.existsById(id)) {
            throw new RuntimeException(GOODS_NO_EXIST);
        }
        productRepository.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param products 商品列表
     */
    @Override
    public void deleteAll(List<Product> products) {
        productRepository.deleteAll(products);
    }

    /**
     * 将迭代器转为商品列表
     *
     * @param products 迭代器
     */
    private List<Product> getProductList(Iterable<Product> products) {
        List<Product> productList = new ArrayList<>();
        products.forEach(productList::add);
        return productList;
    }
}

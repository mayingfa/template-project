package top.qiudb.service;

import top.qiudb.pojo.Product;
import java.text.ParseException;
import java.util.List;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/10 20:37
 * @description 业务层
 */
public interface ProductService {
    /**
     * 根据商品Id判断商品是否存在
     *
     * @param id 商品Id
     */
    boolean existsById(Long id);

    /**
     * 根据商品Id查询
     *
     * @param id 商品Id
     */
    Product findById(Long id);

    /**
     * 查询所有商品
     */
    List<Product> findAll();

    /**
     * 根据商品名检索
     *
     * @param keyword 关键词
     */
    List<Product> findByNameLike(String keyword);

    /**
     * 分页查询
     *
     * @param pageNum  当前页
     * @param pageSize 每页大小
     */
    List<Product> findAll(int pageNum, int pageSize);

    /**
     * 排序查询
     *
     * @param field 排序字段
     * @param asc   升序（true）/ 降序（false）
     */
    List<Product> findAll(String field, boolean asc);

    /**
     * 关键词高亮搜索
     *
     * @param keyword 关键词
     */
    List<Product> highLightSearch(String keyword);


    /**
     * 高级搜索
     *
     * @param name      名称
     * @param type      分类
     * @param brand     品牌
     * @param minPrice  最低价
     * @param maxPrice  最高价
     * @param priceDesc 价格排序(降序（true）、升序（false）)
     * @param startTime 开始时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param endTime   结束时间(格式：yyyy-MM-dd HH:mm:ss)
     * @param pageNum   当前页，从0开始
     * @param pageSize  每页大小
     * @return {@link Product} 返回商品列表
     */
    List<Product> advancedSearch(String name, String type, String brand,
                                 String startTime, String endTime, boolean priceDesc,
                                 double minPrice, double maxPrice, int pageNum, int pageSize) throws ParseException;


    /**
     * 保存或更新商品
     *
     * @param product 商品信息
     * @param insert  插入标识 插入（true）/ 更新（false）
     */
    void save(Product product, boolean insert) throws Exception;

    /**
     * 批量插入
     *
     * @param products 商品列表
     */
    void saveAll(List<Product> products);

    /**
     * 根据商品Id删除数据
     *
     * @param id 商品Id
     */
    void deleteById(Long id);

    /**
     * 批量删除
     *
     * @param products 商品列表
     */
    void deleteAll(List<Product> products);
}

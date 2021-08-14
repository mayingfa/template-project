package top.qiudb.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.qiudb.enums.ResultCode;
import top.qiudb.pojo.Product;
import top.qiudb.service.ProductService;
import top.qiudb.vo.ResultVO;

import java.text.ParseException;
import java.util.List;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/11 19:19
 * @description Elasticsearch Demo测试
 */

@RestController
@RequestMapping("/product")
@Api(tags = "商品管理接口")
public class ElasticsearchController {
    @Autowired
    ProductService productService;

    private static final String NO_GOODS = "暂无商品";
    private static final String FIELD_PRICE = "price";

    /**
     * 获取某一个商品
     *
     * @param id 商品id
     */
    @GetMapping("/{id}")
    @ApiOperation("根据Id获取某个商品")
    public ResultVO<Object> queryProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return new ResultVO<>(ResultCode.SUCCESS, product);
        }
        return new ResultVO<>(ResultCode.FAILED, NO_GOODS);
    }

    /**
     * 获取全部商品
     */
    @GetMapping("/")
    @ApiOperation("查询全部商品")
    public ResultVO<Object> queryAllProduct() {
        List<Product> productList = productService.findAll();
        return getObjectResultVO(productList);
    }

    /**
     * 根据关键词搜索某个商品信息
     *
     * @param keyword 关键词
     */
    @GetMapping("/search/{keyword}")
    @ApiOperation("根据商品名检索")
    public ResultVO<Object> queryProductByName(@PathVariable String keyword) {
        List<Product> productList = productService.findByNameLike(keyword);
        return getObjectResultVO(productList);
    }

    /**
     * 分页查询全部商品
     *
     * @param pageNum  当前页
     * @param pageSize 每页大小
     */
    @GetMapping("/page")
    @ApiOperation("分页查询商品")
    public ResultVO<Object> queryAllProduct(@RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "5") int pageSize) {
        List<Product> productList = productService.findAll(pageNum, pageSize);
        return getObjectResultVO(productList);
    }

    /**
     * 排序查询全部商品
     *
     * @param asc 升序（true）/ 降序（false）
     */
    @GetMapping("/sort")
    @ApiOperation("排序查询商品")
    public ResultVO<Object> queryAllProduct(boolean asc) {
        List<Product> productList = productService.findAll(FIELD_PRICE, asc);
        return getObjectResultVO(productList);
    }

    /**
     * 关键词高亮搜索
     *
     * @param keyword 关键词
     */
    @GetMapping("/highlight/{keyword}")
    @ApiOperation("关键词高亮搜索")
    public ResultVO<Object> highLightSearch(@PathVariable String keyword) {
        List<Product> productList = productService.highLightSearch(keyword);
        return getObjectResultVO(productList);
    }

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
     * @return {@link ResultVO}
     * <p>
     * 使用elasticsearchRestTemplate模板搜索会更加灵活
     */
    @GetMapping("/search")
    @ApiOperation("高级搜索")
    public ResultVO<Object> advancedSearch(String name, String type, String brand,
                                           String startTime, String endTime, boolean priceDesc,
                                           @RequestParam(defaultValue = "0") double minPrice,
                                           @RequestParam(defaultValue = "9999") double maxPrice,
                                           @RequestParam(defaultValue = "1") int pageNum,
                                           @RequestParam(defaultValue = "5") int pageSize) {
        try {
            List<Product> productList = productService.advancedSearch(name, type, brand, startTime, endTime, priceDesc,
                    minPrice, maxPrice, pageNum, pageSize);
            return getObjectResultVO(productList);
        } catch (ParseException e) {
            return new ResultVO<>(ResultCode.FAILED, "日期时间解析失败");
        }
    }

    /**
     * 插入单个商品信息
     *
     * @param product 商品信息
     */
    @PutMapping("/")
    @ApiOperation("添加商品")
    public ResultVO<String> addProduct(@RequestBody Product product) {
        try {
            productService.save(product, true);
        } catch (Exception e) {
            return new ResultVO<>(ResultCode.FAILED, e.getMessage());
        }
        return new ResultVO<>(ResultCode.SUCCESS, "商品添加成功");
    }

    /**
     * 批量插入
     *
     * @param products 商品列表
     */
    @PutMapping("/batch")
    @ApiOperation("批量添加商品")
    public ResultVO<String> batchAddProduct(@RequestBody List<Product> products) {
        productService.saveAll(products);
        return new ResultVO<>(ResultCode.SUCCESS, "商品批量添加成功");
    }

    /**
     * 更新商品信息
     *
     * @param product 商品信息
     */
    @PostMapping("/")
    @ApiOperation("更新商品信息")
    public ResultVO<String> updateProduct(@RequestBody Product product) {
        try {
            productService.save(product, false);
        } catch (Exception e) {
            return new ResultVO<>(ResultCode.FAILED, e.getMessage());
        }
        return new ResultVO<>(ResultCode.SUCCESS, "商品更新成功");
    }

    /**
     * 根据商品id删除数据
     *
     * @param id 商品id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据Id删除商品")
    public ResultVO<String> delProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
        } catch (Exception e) {
            return new ResultVO<>(ResultCode.FAILED, e.getMessage());
        }
        return new ResultVO<>(ResultCode.SUCCESS, "商品删除成功");
    }

    /**
     * 批量删除
     *
     * @param products 商品列表
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除商品")
    public ResultVO<String> batchDelProduct(@RequestBody List<Product> products) {
        productService.deleteAll(products);
        return new ResultVO<>(ResultCode.SUCCESS, "商品批量删除成功");
    }

    /**
     * 统一返回查询结果
     *
     * @param productList 商品列表
     */
    private ResultVO<Object> getObjectResultVO(List<Product> productList) {
        if (productList.isEmpty()) {
            return new ResultVO<>(ResultCode.FAILED, NO_GOODS);

        }
        return new ResultVO<>(ResultCode.SUCCESS, productList);
    }
}

package top.qiudb.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/10 20:26
 * @description 商品信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product")
@ApiModel(value = "Product", description = "商品描述信息")
public class Product implements Serializable {
    @Id
    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "商品ID", required = true, notes = "商品id不允许重复")
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "商品分类", required = true, example = "手机")
    private String type;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "商品品牌", required = true, example = "华为")
    private String brand;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "商品价格", required = true, example = "5999.9")
    private Double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "上架时间", example = "2021-08-15")
    private Date saleTime;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "商品图片链接", required = true)
    private String url;
}

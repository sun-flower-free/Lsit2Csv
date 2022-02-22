# Lsit2Csv

将List<任意实体类> 转换成csv文件小程序

## 原理

通过类获得成员变量名称，拼接字符串获得成员的get方法，通过反射获得成员变量值，将这个值注入列表，根据列表创建csv文件

## 注意事项

类中所有成员变量都需要有get方法，否则会反射失败

## 参数请求

```
@param exportData 源数据List
@param map csv文件的列表头map
@param outPutPath 文件路径
@param fileName 文件名称
```
[TOC]

## 服务创建：create_service

**前端发送**

```json
{
	'userId':number
	'serviceName':stringtime
}
```

**后端返回**

```json
{
	'code':200
	'stateCode':0/1/2;
}
0创建失败
1成功
2服务名重复
```

## 服务名修改：updateService

前端发送

```json
{
  "serviceId":12,
  "newName":"aaaaaaaaa"
}
```

 后端返回

```json
{
	'code':200
	'stateCode':0/1
}
0修改失败
1成功
```



## 服务删除：deleteService

**前端发送**

```json
{
    "serviceId":12
}
```

**后端返回**

```json
{
	'code':200
	'statecode':0/1
}
0删除失败
1成功
```
## 服务列表获取：inquiryService

**前端发送**

```json
{
    "userId":464
}
```

**后端返回**

```json
成功：{
    "stateCode": 1,
    "serviceInfo": [
        {
            "serviceId": 17,
            "serviceName": "Andrew Gibson",
            "createdTime": "2005-02-08T01:53:08",
            "lastTime": "2006-03-10T18:18:43"
        }
 }
失败：{'code': 200, 'stateCode': 0}
```

## 具体服务获取：detailedService

**前端发送**

```json
{
    "serviceId":3030 
}
```

**后端返回**

```json
成功：{
    "stateCode": 1,
    "imagelist": [
        {
            'data': file_base64, 
            'id': file.id,
            'positionIndex': file.position_index,
         }]
}
失败：{'code': 200, 'stateCode': 0}
```
## 图片添加：addPage

**前端发送**

```json
form_data{
	'serviceId':string,
	'insertIndex':number,
	'file':image/pdf   
}
```

**后端返回**

```json
{
	'stateCode':0/1
    'imagelist':{
        	'data':iamge二进制数据，
        	'id':file表中id,
        	'positionIndex':file表中position_index,
        	},
    }
}
失败：{'code': 200, 'stateCode': 0}
```

## 图片拖拽：movePage

前端发送

```json
{
	'id':newindex(number)
}
```

后端返回

```json
{
    "code": 200,
    "stateCode": 1
}
0更新失败
1下标更新成功
```

## 图片删除：deletePage

**前端发送**

```json
{
    "serviceId":3030,
    "positionIndex":1
}
```

**后端返回**

```json
{
    "code": 200,
    "stateCode": 1
}
0删除失败
1成功
```

## PDF下载：download_pdf

前端发送

```json
{
    "serviceId":3030
}
```

后端返回

```json
成功：pdf流式文件，文件名为"uid+service_name"\
失败：{
    "code": 200,
    "stateCode": 0
}
```

<img src="C:\Users\34312\AppData\Roaming\Typora\typora-user-images\image-20250318194244063.png" alt="image-20250318194244063" style="zoom: 50%;" />

## 图片下载：downloadImages

前端发送

```json
{
    "serviceId":3030
}
```

后端返回

```
成功：zip压缩包流式文件，文件名为"uid+service_name"
失败：{
    "code": 200,
    "stateCode": 0
}
```

<img src="C:\Users\34312\AppData\Roaming\Typora\typora-user-images\image-20250318194529802.png" alt="image-20250318194529802" style="zoom:50%;" />

## 图片OCR：ocrFormula

前端发送

```json
form_data:上传图片文件
```

后端返回

```json
成功：{
    "0": {
        "latex":
        "mathml":
    }
}
失败：{
    "code": 200,
    "stateCode": 0
}
```

## 文本提取：transformText

前端发送

```
form_data:上传图片/视频/音频
```

后端返回

```json
成功：{
    'stateCode': 1,
    "texts": texts,
    "translated":translated_texts（默认中英互译）
}
失败：{
    "code":200,
 	"stateCode":0
}
```

## 文本翻译：transformLanguage

前端发送

```json
{
    "toLanguage": "fr",
    "texts": "Hello, how are you?"
}
```

后端返回

```json
成功：{
    "stateCode": 1,
    "translated": "Bonjour, comment allez-vous?"
}
失败：
{
    "code":200,
 	"stateCode":0
}
```

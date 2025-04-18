------

## 用户控制逻辑

### 用户登录

**API:** `/user/login`  **POST**

**前端发送:**

```json
{
    "userName": "用户输入的用户名",
    "passwordHash": "用户输入的密码"
}
```

**后端返回:**

```json
{
    "stateCode": 0/1/2 int
}
```

- `stateCode: 0` 用户不存在
- `stateCode: 1` 密码错误
- `stateCode: 2` 登录成功

------

### 用户注册

**API:** `/user/register `**POST**

**前端发送:**

```json
{
    "userName": "用户输入的用户名",
    "passwordHash": "用户输入的密码",
    "email": "用户邮箱"
}
```

**后端返回:**

```json
{
    "stateCode": 0/1/2 int
}
```

- `stateCode: 0` 用户名重复
- `stateCode: 1` 邮箱重复
- `stateCode: 2` 成功注册

------

## 管理员控制逻辑

### 管理员登录

**API:** `/admin/login` **POST**

**前端发送:**

```json
{
    "adminName": "管理员输入的用户名",
    "adminPasswordHash": "管理员输入的密码"
}
```

**后端返回:**

```json
{	
    "stateCode": number int,
    "adminId": adminId  int,
    "level": 管理员级别 int   // 暂定太麻烦了
    "accessToken": string
    "refreshToken":string
}
```

- `stateCode: 0` 用户不存在
- `stateCode: 1` 密码错误
- `stateCode: 2` 密码正确

**人脸验证流程:    (待定）**

1. 前端先验证账户密码成功后，获取管理员人脸数据并发送到后端。
2. 后端从数据库中查找对应管理员的人脸数据进行验证。
3. 返回最终登录结果。

**API:** `/AdminLoginFace` **POST**

**前端发送人脸数据:**

- 前端在验证账户密码成功后，将人脸数据发送至后端。
- 发送数据格式：

```json
{
	"userName": "管理员输入的用户名",
    "data": "待定的字符串格式人脸数据"
}
```

**后端返回人脸验证结果:**

```json
{
    "stateCode": number int
}
```

- `statecode: 0` 人脸未识别
- `statecode: 1` 人脸正确

### 管理员注册

**API:** `/admin/register` **POST**

**前端发送:**

```json
{
    "adminName": "管理员输入的用户名",
    "adminPasswordHash": "管理员输入的密码",
    "level": 管理员级别  int, // 后续再处理，这块比较复杂，可能不加
}
```

**后端返回:**

```json
{
    "stateCode": number  int
}
```

- `stateCode: 0` 用户名重复
- `stateCode: 1` 成功注册

## 用户界面

### 界面信息获取

**API**:/getUserHomePageInfo  **GET**

```json
{
    "applyUrlText": "香炉供台上摆",
    "backgroundUrl": "http://192.168.137.80:8080//AITool//images//background.jpg",
    "dim1": "111",
    "dim2": "222",
    "dim3": "333",
    "feedbackText": "11111",
    "id": 1,
    "pageName": "HomePage",
    "topNavigationText1": "来财！来财！来财！",
    "topNavigationText2": "我们都是憋佬仔!!!"
}
```



### 板块获取

**API:** `/getUserBlocks`   **GET**

**数据获取逻辑:**

1. 先从 `block_widgets` 中选取 `is_visible`= true 的行。
2. 按照 `display_order` 对这些行排序。
3. 每个 `block_widget_id` 和 `module_widget_id` 组合成一对。
4. 根据 `module_widget_id` 查询 `module_widget` 的基本信息。
5. 筛选出每个 `module_widget` 中 `is_visible`= true 的部分。
6. 再按 `display_order` 对 `module_widgets` 排序。

**返回格式示例:**

```json
[
    {
        "displayOrder": 1,
        "iconUrl": "http://192.168.137.80:8080/AITool/images/置顶网页.svg",
        "id": 1,
        "isVisible": 1,
        "name": "置顶网页"，
        "moduleWidgetUserDTOs": [
            {
                "displayOrder": 1,
                "fatherId": 1,
                "iconUrl": "http://192.168.137.80:8080/AITool/images/文献翻译.png",
                "id": 1,
                "isVisible": 1,
                "name": "文献翻译",
                "url": "https://www.scholaread.cn/?ref="
            }, 。。。。。。
         ]
	}, 。。。。
]
        
```

------

### 评价指标

**API:** `/userFeedback` **POST**

**前端发送:**

```json
{
    "userId": "[用户ID]",
    "widgetId": "[组件ID]",
    "dimension1": "[维度1评分]",
    "dimension2": "[维度2评分]",
    "dimension3": "[维度3评分]"
}
```

**后端返回:**

```json
{
    "stateCode": number  int
}
```

- `statecode: 0` 提交失败
- `statecode: 1` 提交成功

------

### 申请收录

**API:** `/userApplyUrl` **POST**   

**前端发送:**

```json
{
    "widgetUrl": "[组件URL]",
    "blockId": "[所属主类ID]",
    "name": "[组件名称]",
    "iconUrl": "[图标URL]"
}
```

**后端返回:**

```json
{
    "stateCode": number   int
}
```

- `statecode: 0` 提交失败
- `statecode: 1` 提交成功

------

### 组件使用

**API:** `/userUseModuleLog` **POST**

**前端发送:**

```json
{
    "widgetId": "[组件ID]",
    "userId": "[用户ID]"
}
```

------

### 图片验证码

**API:** `/captcha/image` **POST**

**获取验证码**

**前端发送:**

```json
{
    "userId": [用户ID] int,
}
```

**后端返回:**

```json
{
    "image": "[验证码图片字符串形式的二进制流]",
    "stateCode": number  int
}
```

- `statecode: 0` 验证码获取失败

- `statecode: 1` 验证码获取成功

  

**API：/captcha/validate**

**提交验证码**

**前端发送:**

```json
{
    "userId": 用户ID  int,
    "codeInput": "用户输入"
}
```

**后端返回:**

```json
{
    "stateCode": number  int
}
```

- `statecode: 0` 验证码错误
- statecode:1 超时
- `statecode: 2 验证码正确

## 管理员界面

### 上传接口

```json
{
    "data": 
    等一下这个我看下这个咋用的在和你说
}
```

### 修改homepage信息

/updateHomePageInfo

前端直接把更新后的homepage穿过来

后端返回

```json
{
    "stateCode": 0/1     // 0失败 1成功
}
```

### 板块获取

**API:** `/getAdminBlocks`   **GET**

**数据获取逻辑:**

1. 先从 `block_widgets` 中选取所有行。

2. 按照 `display_order` ，`is_visible`对这些行排序(is_visible=flase的在最后)。

3. 每个 `block_widget_id` 和 `module_widget_id` 组合成一对。

4. 根据 `module_widget_id` 查询 `module_widget` 的基本信息。

5. 每个 `module_widget` 选取所有行。

6. 再按 `display_order` 对 `module_widgets` 排序(is_visible=flase的在最后)。

7. 每个`module_widget` 中带有以下信息

   ```json
   {
       id: number
       fatherid: number
       name: string
       url: string
       icon_url: string
       dimension1: number
       dimension2: number
       dimension3: number
       display_order: number
       is_visible: boolean
       click_count: number
       abstract:string
   }
   dim1: number
   dim2: number
   dim3: number
   click_count: number
   默认一星期，指的是从最近的星期一开始的数据
   
   ```

**前端在请求url发送**

```JSON
?day = 任意天数
```

具体天数

**返回格式示例:**

```json
[
    {
        "displayOrder": 1,
        "iconUrl": "http://192.168.137.80:8080/AITool/images/置顶网页.svg",
        "id": 1,
        "imgUrl": "",
        "introduction": "",
        "isVisible": 1,
        "name": "置顶网页",
        "moduleWidgetAdminDTOs": [
            {
                "avgDimension1": 1.0,
                "avgDimension2": 1.0,
                "avgDimension3": 1.0,
                "clickCount": 1,
                "displayOrder": 1,
                "fatherId": 1,
                "iconUrl": "http://192.168.137.80:8080/AITool/images/文献翻译.png",
                "id": 1,
                "introduction": "",
                "isVisible": 1,
                "name": "文献翻译",
                "url": "https://www.scholaread.cn/?ref="
            },....
         ]
    },...
]
```

### 板块顺序修改

/updateBlockOrder

**要按照事务的标准处理**

**前端发送**

```json
{
    {
    'id':blockid1,
    'displayOrder':1
	},
	{
    'id':blockid2,
    'displayOrder':2
	},
}
```

**后端返回**

```json
{
  "stateCode": number
}
- `statecode: 0` 修改失败
- `statecode: 1` 修改成功
```

### 板块添加

```
/addBlockWidget
```

```json
{
        name: string
        iconUrl: string
        introduction:string
        imgUrl:string
        isVisible: boolean
    	displayOrder: int
}
```

**后端返回**

```json
{
   "stateCode": number
}
- `statecode: 0` 修改失败
- `statecode: 1` 修改成功
```

### 板块信息修改

/updateBlockInfo

前端把修改过的板块的信息都返回过来

后端返回

```json
{
    "stateCode": 0/1     // 0失败 1成功
}
```



### 模块顺序修改

/updateModuleOrder

**要按照事务的标准处理**

**前端发送**

```json
{
    {
     'id':moudule1,
     'displayOrder':1
     'fatherId':,
     },
    {
        'id':moudule2,
        'displayOrder':2,
    	'fatherId':,
     },
}

type1的data很多条
type2仅两条
有两种情况
一种全是type1
一种type1 + type2
```

**后端返回**

```json
{
   "stateCode": number
}
- `statecode: 0` 修改失败
- `statecode: 1` 修改成功
```



### 模块添加

/addModule

```json
{
	fatherId:number
    name: string
    url: string
    iconUrl: string
    isVisible: boolean
    introduction:string
    displayOrder: number
}
```

**后端返回**

```json
{
   "stateCode": number
}
- `statecode: 0` 修改失败
- `statecode: 1` 修改成功
```

### 模块信息修改

/updateModuleInfo

前端把修改过的模块发过来

后端返回

```josn
{
    "stateCode": 0/1     // 0失败 1成功
}
```




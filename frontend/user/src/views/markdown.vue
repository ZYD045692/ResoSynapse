<template>
    <el-aside style="height: 100%;width: 100%; padding: 0">
        <el-main class="main-container">
            <div class="svg-container">
                <svg ref="svgRef"></svg>
            </div>
            <footer class="footer">
                <div class="controls">
                    <el-button @click="zoomIn" plain>
                        <el-icon><ZoomIn /></el-icon>放大
                    </el-button>
                    <el-button @click="zoomOut" plain>
                        <el-icon><ZoomOut /></el-icon>缩小
                    </el-button>
                    <el-button @click="fitToScreen"  plain>
                        <el-icon><FullScreen /></el-icon>适应
                    </el-button>
                    <el-button @click="onSave" plain>
                        <el-icon><Download /></el-icon>下载
                    </el-button>
                </div>
            </footer>
        </el-main>
    </el-aside>
</template>
<script setup lang="ts">
import { ref, onMounted, onUpdated, defineExpose, watch } from 'vue'
import { Transformer } from 'markmap-lib'
import { Markmap } from 'markmap-view'
import * as htmlToImage from 'html-to-image'
import { saveAs } from 'file-saver'
import { ZoomIn, ZoomOut, FullScreen, Download } from '@element-plus/icons-vue'

const defaultInitValue1 =`
# 智汇研析（ResoSynapse）

## 用户引流界面

### 用户认证与管理

- **用户注册**：用户通过填写用户名、邮箱和密码完成注册，前端验证信息格式合法性后，后端进行校验并加密密码存储，确保账号唯一性。
- **用户登录**：用户输入用户名和密码登录，后端验证用户存在性和密码正确性。

### 交互模块

- **主页信息获取**：用户访问主页时，系统提供页面的基本信息，包括导航文本、背景图片等。
- **板块获取与展示**：系统从数据库中获取可见的板块和组件信息，按照设定顺序和规则展示给用户。
- **组件使用**：用户点击使用各类组件，系统记录用户的使用行为。
- **评价反馈**：用户对使用过的组件进行评分反馈，提交多维度的评分数据。
- **申请收录**：用户可提交资源信息，申请将资源添加到平台。
- **智能助手**：用户文字问答、AIGC技术支持

### 安全与验证

- **图片验证码获取与验证**：用户可获取图片验证码，提交验证码进行验证。

## 管理员界面

### 管理员认证与管理

- **管理员登录**：管理员输入用户名和密码登录，可选择人脸识别验证，提高安全性。
- **管理员注册**：管理员通过填写用户名、密码等信息完成注册。

### 管理员界面与管理模块

- **修改homepage信息**：管理员可更新主页信息，调整页面展示内容。
- **板块管理**：管理员获取所有板块信息，包括可见和不可见的，可对板块进行添加、修改、删除等操作，并调整板块的显示顺序。
- **组件管理**：管理员对组件进行添加、修改、删除等操作，调整组件在板块中的显示顺序。

## 用户界面

### PDF处理功能板块

#### PDF服务管理

- **服务创建与修改**：用户可创建新的PDF处理服务，也可修改已有服务的名称。
- **服务删除与列表获取**：用户可删除不再需要的服务，也可获取所有服务的列表信息。

#### PDF内容操作

- **具体服务获取**：用户获取特定服务的详细信息，包括图片列表。
- **图片添加与管理**：用户向服务中添加图片或PDF文件，指定插入位置。
- **图片拖拽与删除**：用户可对服务中的图片进行拖拽排序或删除操作。

#### PDF转换与下载

- **PDF下载**：用户将服务中的图片合成PDF文件下载。
- **图片下载**：用户将服务中的图片打包成ZIP文件下载。

### 公式提取

- **图片OCR**：用户上传图片，系统识别其中的公式等内容，返回LaTex格式的文本。

### 文本提取与翻译

- **文本提取与翻译**：用户上传图片、视频或音频，系统提取其中的文本内容并可进行翻译

### 大模型加思维导图

- **聊天功能**：正常聊天，支持上传文件，流式输出与非流式输出切换
- **思维导图提取**：自动提取对话中的思维导图并提供下载


`
const defaultInitValue = `
# Web 开发学习路径

## HTML 基础
### HTML 标签与元素
学习 HTML 的基本标签和语义化元素，掌握如何构建网页结构。

### HTML 表单与输入
了解表单元素的使用，包括各种输入类型和表单验证。

### HTML 多媒体与嵌入
学习如何在网页中嵌入图片、视频、音频等多媒体内容。

## CSS 基础
### CSS 选择器与样式
掌握 CSS 选择器的使用，学习如何为网页元素添加样式。

###  CSS 布局与定位
了解 CSS 的盒模型，学习如何使用浮动、定位等技术进行网页布局。

###  CSS 动画与过渡
学习 CSS 动画和过渡效果的实现，提升网页的交互体验。

`

const transformer = new Transformer()
const props = defineProps({
    initValue: {
        type: String,
        default: defaultInitValue
    }
})
const initValue = ref(defaultInitValue);

if (props.initValue !== null && props.initValue !== undefined && props.initValue !== '') {
    initValue.value = props.initValue;
}
watch(() => props.initValue, (newVal) => {
    if (newVal !== null && newVal !== undefined) {
        initValue.value = newVal;
    }
});


const mm = ref()
const svgRef = ref()

const update = () => {
    const { root } = transformer.transform(initValue.value)
    mm.value.setData(root)
    mm.value.fit()
}

const zoomIn = () => mm.value.rescale(1.25)

const zoomOut = () => mm.value.rescale(0.8)

const fitToScreen = () => mm.value.fit()

// 暴露方法给父组件
defineExpose({
    fit: fitToScreen
})

const onSave = async () => {
    const dataUrl = await htmlToImage.toPng(svgRef.value)
    saveAs(dataUrl, 'markmap.png')
}

onMounted(() => {
    mm.value = Markmap.create(svgRef.value)
    update()
    setTimeout(() => {
        fitToScreen()
    }, 100)
})

onUpdated(update)


</script>

<style scoped>
.el-container {
    height: 100%;
}

.el-aside {
    background: #f5f5f5;
    padding: 20px;
    box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
}

.main-container {
    height: 100%;
    padding: 0;
    display: flex;
    flex-direction: column;
    background: white;
    border-radius: 8px;
    overflow: hidden;
    position: relative;
}

.svg-container {
    flex: 1;
    min-height: 0;
    width: 100%;
}

svg {
    width: 100%;
    height: 100%;
}

.footer {
    background: #fff;
    border-top: 1px solid #eee;
    padding: 10px 0;
    position: relative;
}

.controls {
    display: flex;
    gap: 8px;
    justify-content: center;
    padding: 0 20px;
}
</style>